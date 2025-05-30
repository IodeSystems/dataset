package com.iodesystems.db.query

import com.iodesystems.db.http.DataSet.Order.Direction
import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.util.StringUtil.camelToTitleCase
import com.iodesystems.db.util.StringUtil.isCamelCase
import com.iodesystems.db.util.StringUtil.isSnakeCase
import com.iodesystems.db.util.StringUtil.snakeToCamelCase
import com.iodesystems.db.util.StringUtil.snakeToTitleCase
import com.iodesystems.db.util.StringUtil.titleCase
import org.jooq.*
import org.jooq.impl.DSL
import org.slf4j.LoggerFactory
import java.util.stream.Stream

data class TypedQuery<T : Select<R>, R : Record, M>(
  val table: T,
  val mapper: (R) -> M,
  val conditions: List<Condition> = emptyList(),
  val offset: Int = 0,
  val limit: Int = -1,
  val order: List<SortField<*>> = emptyList(),
  val fields: Map<String, FieldConfiguration<*>> = emptyMap(),
  val searches: Map<String, (query: String) -> Condition?> = emptyMap(),
  val openSearches: List<String> = emptyList(),
  val lastSearchCorrected: String? = null
) {

  val fieldsByNameLower by lazy { fields.mapKeys { it.key.lowercase() } }
  val searchesByNameLower by lazy { searches.mapKeys { it.key.lowercase() } }

  fun <F> fieldConfiguration(field: Field<F>, alias: String? = null): FieldConfiguration<F>? {
    val name = alias ?: fieldName(field.name).name.lowercase()
    return fieldsByNameLower[name]?.let {
      @Suppress("UNCHECKED_CAST")
      it as FieldConfiguration<F>
    }
  }

  fun <F> field(field: Field<F>, alias: String? = null): Field<F>? {
    return fieldConfiguration(field, alias)?.field
  }

  enum class Nulls {
    FIRST, LAST
  }

  data class SearchRendered(
    val search: String, val condition: Condition?
  )

  data class DataSet<T : Select<R>, R : Record, M>(val db: DSLContext, val query: TypedQuery<T, R, M>) {

    fun field(name: String): FieldConfiguration<*>? {
      return query.fieldsByNameLower[name.lowercase()]
    }

    fun field(field: Field<*>): FieldConfiguration<*>? {
      return query.fields[field.name]
    }

    fun search(search: String): DataSet<T, R, M> {
      return copy(db = db, query = query.search(search))
    }

    fun query(): Select<R> {
      val withConditions = if (query.conditions.isNotEmpty()) {
        // Merge conditions if we have them
        val existingConditions = query.table.`$where`()
        if (existingConditions != null) {
          query.table.`$where`(DSL.and(query.conditions + existingConditions))
        } else {
          query.table.`$where`(DSL.and(query.conditions))
        }
      } else {
        query.table
      }
      val tableOrdered = if (query.order.isNotEmpty()) {
        withConditions.`$orderBy`(query.order)
      } else {
        withConditions
      }
      val tableWithOffset = if (query.offset > 0) {
        tableOrdered.`$offset`(DSL.`val`(query.offset))
      } else {
        tableOrdered
      }
      val tableWithLimit = if (query.limit > -1) {
        tableWithOffset.`$limit`(DSL.`val`(query.limit))
      } else {
        tableWithOffset
      }
      tableWithLimit.attach(db.configuration())
      return tableWithLimit
    }

    internal fun result(): Result<R> {
      return query().fetch()
    }

    override fun toString(): String {
      return query().toString()
    }

    fun first(): M? {
      val result = copy(query = query.one()).fetch()
      return if (result.isEmpty()) {
        null
      } else result[0]
    }

    fun page(page: Int, pageSize: Int): List<M> {
      return copy(query = query.page(page, pageSize)).fetch()
    }


    fun fetch(): List<M> {
      val result: Result<R> = result()
      return result.map(query.mapper)
    }

    fun stream(): Stream<M> {
      return query().stream().map(query.mapper)
    }

    fun count(): Long {
      return db.selectCount().from(query()).fetchOne(0, Long::class.java)!!
    }

    fun empty(): Boolean {
      return copy(query = query.one()).count() != 1L
    }

    fun order(field: String, sortOrder: SortOrder, nulls: Nulls? = null): DataSet<T, R, M> {
      return copy(query = query.order(field, sortOrder, nulls))
    }

    fun clearOrder(): DataSet<T, R, M> {
      return copy(query = query.clearOrder())

    }
  }

  fun one(): TypedQuery<T, R, M> {
    return copy(limit = 1)
  }


  fun page(page: Int, pageSize: Int): TypedQuery<T, R, M> {
    return copy(offset = page * pageSize, limit = pageSize)
  }

  fun search(query: String): TypedQuery<T, R, M> {
    val rendered = renderSearch(query)
    if (rendered.condition == null) {
      return this
    }
    return copy(
      conditions = conditions + rendered.condition,
      lastSearchCorrected = rendered.search
    )
  }

  fun where(condition: Condition): TypedQuery<T, R, M> {
    return copy(
      conditions = conditions + condition
    )
  }

  fun renderSearch(search: String): SearchRendered {
    val searchConditions = mutableListOf<Condition>()
    val searchParser = SearchParser()
    try {
      var termsCondition: Condition? = null
      val searchParsed = searchParser.parse(search)
      for (term in searchParsed.terms) {
        val conditionProvider: ((s: String) -> Condition?)? = if (term.target != null) {
          // We have a target!
          val target = term.target.lowercase()
          // Check if we have a targeted search
          val targetedSearch = searchesByNameLower[target]
          if (targetedSearch != null) {
            { s -> targetedSearch(s) }
          } else {
            val targetField = fieldsByNameLower[target]
            if (targetField == null) {
              log.warn("Requested field $target not found in configured fields, searching open fields instead")
              null
            } else if (targetField.search == null) {
              log.warn("Configured field $target has no search, but was requested, searching open fields instead")
              null
            } else {
              targetField.search
            }
          }
        } else {
          null
        }
        var termCondition: Condition? = null
        for (value in term.values) {
          if (conditionProvider != null) {
            val targetedSearchCondition = conditionProvider(value.value)
            termCondition = if (termCondition == null) {
              targetedSearchCondition
            } else if (value.conjunction == Conjunction.AND) {
              DSL.and(termCondition, targetedSearchCondition)
            } else {
              DSL.or(termCondition, targetedSearchCondition)
            }
            // Stop processing this term value
            continue
          }
          // No targeted search, so we'll search all open fields
          var fieldsCondition: Condition? = null
          for (field in fields.values) {
            // Do we have a search for this field?
            val fieldSearch = field.search ?: continue
            // Is this field open for searching?
            if (!field.open) continue
            val fieldSearchCondition = fieldSearch(value.value)
            fieldsCondition = fieldsCondition?.or(fieldSearchCondition) ?: fieldSearchCondition
          }

          // Also perform open searches
          for (openSearch in openSearches) {
            val searchCondition = searches[openSearch]!!(value.value)
            fieldsCondition = fieldsCondition?.or(searchCondition) ?: searchCondition
          }
          // Negate the condition if necessary
          if (value.negated) fieldsCondition = fieldsCondition?.not()
          termCondition = mergeCondition(termCondition, fieldsCondition, value.conjunction)
        }
        if (term.negated) termCondition = termCondition?.not()
        termsCondition = mergeCondition(termsCondition, termCondition, term.conjunction)
      }
      if (termsCondition != null) {
        searchConditions.add(termsCondition)
      }
      return SearchRendered(searchParsed.search, DSL.or(searchConditions))
    } catch (e: InvalidSearchStringException) {
      throw SneakyInvalidSearchStringException(
        "Invalid search while building conditions:" + e.message, e
      )
    }

  }

  private fun mergeCondition(
    termsCondition: Condition?, termCondition: Condition?, conjunction: Conjunction = Conjunction.OR
  ): Condition? {
    if (termCondition == null) return termsCondition
    if (termsCondition == null) return termCondition
    if (conjunction == Conjunction.AND) return DSL.and(termsCondition, termCondition)
    return DSL.or(termsCondition, termCondition)
  }

  fun clearOrder(): TypedQuery<T, R, M> {
    return copy(order = emptyList())
  }

  fun order(field: String, sortOrder: SortOrder?, nulls: Nulls? = null): TypedQuery<T, R, M> {
    val fieldConfiguration = fields[field] ?: return this

    return order(fieldConfiguration.field, sortOrder, fieldConfiguration.orderNulls ?: nulls)
  }

  fun order(field: Field<*>, order: SortOrder?, nulls: Nulls? = null): TypedQuery<T, R, M> {
    return copy(
      order = this.order + field.sort(order)
        .let { if (nulls == null) it else if (nulls == Nulls.FIRST) it.nullsFirst() else it.nullsLast() })
  }

  fun data(db: DSLContext): DataSet<T, R, M> {
    return DataSet(db, this)
  }

  fun offset(offset: Int): TypedQuery<T, R, M> {
    return copy(offset = offset)
  }

  fun limit(limit: Int): TypedQuery<T, R, M> {
    return copy(limit = limit)
  }

  fun <U> map(converter: (M) -> U): TypedQuery<T, R, U> {
    val newMapper: (R) -> U = { record -> converter(mapper(record)) }
    return TypedQuery(
      table = table,
      mapper = newMapper,
      conditions = conditions,
      offset = offset,
      limit = limit,
      order = order,
      fields = fields,
      searches = searches,
    )
  }


  data class FieldConfiguration<T>(
    val field: Field<T>,
    val name: String = field.name,
    val title: String = name,
    val external: Boolean,
    val orderable: Boolean = false,
    val direction: Direction? = null,
    val search: ((String) -> Condition?)? = null,
    val primaryKey: Boolean = false,
    val type: String? = null,
    val open: Boolean = true,
    val orderNulls: Nulls? = null,
    val mapper: Mapper<T, Any>? = null,
  ) {

    data class Mapper<A, B>(
      val type: Class<B>,
      val mapper: (A, Record) -> B
    )

    data class Builder<T>(
      val field: Field<T>,
      var name: String = field.name,
      var title: String = name,
      private var orderable: Boolean = false,
      private var direction: Direction? = null,
      private var search: ((String) -> Condition?)? = null,
      var primaryKey: Boolean = false,
      var type: String? = null,
      private var open: Boolean = true,
      private var orderNulls: Nulls? = null,
      private var mapper: Mapper<T, Any>? = null,
      var external: Boolean = false
    ) {

      fun orderable(direction: Direction? = null, nulls: Nulls? = null) {
        this.direction = direction
        orderable = true
        orderNulls = nulls
      }

      fun search(open: Boolean? = null, fn: (s: String) -> Condition?) {
        if (open != null) this.open = open
        search = fn
      }

      fun primaryKey() {
        primaryKey = true
      }

      fun <B> map(type: Class<B>, fn: (T, record: Record) -> B) {
        this.type = type.simpleName
        @Suppress("UNCHECKED_CAST")
        this.mapper = Mapper(type, fn) as Mapper<T, Any>
      }

      fun build(): FieldConfiguration<T> {
        return FieldConfiguration(
          field = field,
          name = name,
          title = title,
          orderable = orderable,
          search = search,
          primaryKey = primaryKey,
          type = type,
          open = open,
          direction = direction,
          orderNulls = orderNulls,
          external = external,
        )
      }
    }
  }

  data class Builder<T : Select<R>, R : Record, M>(
    var query: TypedQuery<T, R, M>,
    var doDetectFields: Boolean = false,
  ) {


    fun <T> field(
      field: Field<T>,
      init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit)? = null
    ) {
      // Ensure that the field is scoped to the outside table
      val builder = FieldConfiguration.Builder(field)

      val name = fieldName(field.name)
      builder.name = name.name
      builder.title = name.title

      init?.let { builder.it(field) }
      val built = builder.build()
      query = query.copy(
        fields = query.fields + Pair(built.name, built),
        order = query.order + (if (built.direction == null) emptyList() else listOf(
          built.field.sort(
            when (built.direction) {
              Direction.ASC -> SortOrder.ASC
              Direction.DESC -> SortOrder.DESC
            }
          )
        ))
      )
    }

    fun search(
      name: String,
      open: Boolean = false,
      search: (query: String) -> Condition?
    ) {
      query = query.copy(
        searches = query.searches + Pair(name, search),
        openSearches = if (open) query.openSearches + name else query.openSearches
      )
    }

    fun fields(vararg fields: Field<T>) {
      fields.map {
        field(it)
      }
    }

    fun autoDetectFields(
      db: DSLContext,
    ) {
      db.selectFrom(query.table).limit(0).fetch().recordType().fields()
        .map { field ->
          val existing = query.fields.values.firstOrNull {
            it.field.name == field.name
          }
          val fieldType = field.dataType.converter.toType().simpleName
          if (existing == null) {
            val unqualifiedName = DSL.name(field.name)
            val unqualifiedField = field.`as`(unqualifiedName)
            field(unqualifiedField) {
              external = true
              type = fieldType
            }
          } else {
            if (existing.type != fieldType) {
              query = query.copy(
                fields = query.fields + Pair(
                  fieldName(existing.name).name, existing.copy(
                    type = fieldType
                  )
                )
              )
            }
          }
        }
    }
  }

  companion object {
    val log = LoggerFactory.getLogger(TypedQuery::class.java)

    data class FieldName(
      val title: String,
      val name: String,
    )

    fun fieldName(name: String): FieldName {
      return if (name.isCamelCase()) {
        FieldName(name.camelToTitleCase(), name)
      } else if (name.isSnakeCase()) {
        FieldName(name.snakeToTitleCase(), name.snakeToCamelCase())
      } else {
        FieldName(name.titleCase(), name)
      }
    }

    fun <R : Record, M> forTable(
      table: Select<R>,
      mapper: (R) -> M,
      init: (Builder<Select<R>, R, M>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, M> {
      val builder = Builder(TypedQuery(table = table, mapper = mapper))
      init?.let { builder.it() }
      return builder.query
    }

    fun <R : Record> forTableMaps(
      table: Select<R>,
      init: (Builder<Select<R>, R, MutableMap<String, Any>>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, MutableMap<String, Any>> {
      return forTable(table, { r -> r.intoMap() }, init)
    }

    fun <R : Record> forTableRecords(
      table: Select<R>, init: (Builder<Select<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, R> {
      return forTable(table, { r -> r }, init)
    }
  }
}

private fun <K, V> Map<K, V>.partition(function: (Map.Entry<K, V>) -> Boolean): Pair<Map<K, V>, Map<K, V>> {
  val first = mutableMapOf<K, V>()
  val second = mutableMapOf<K, V>()
  for (entry in entries) {
    if (function(entry)) {
      first[entry.key] = entry.value
    } else {
      second[entry.key] = entry.value
    }
  }
  return Pair(first, second)
}


