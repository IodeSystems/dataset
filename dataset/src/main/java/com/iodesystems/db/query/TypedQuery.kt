package com.iodesystems.db.query

import com.iodesystems.db.http.DataSet.Order.Direction
import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import org.jooq.*
import org.jooq.impl.DSL

data class TypedQuery<T : Table<R>, R : Record, M>(
  val table: T,
  val mapper: (R) -> M,
  val conditions: List<Condition> = emptyList(),
  val offset: Int = 0,
  val limit: Int = 100,
  val order: List<SortField<*>> = emptyList(),
  val fields: Map<String, FieldConfiguration<*>> = emptyMap(),
  val searches: Map<String, (query: String, table: T) -> Condition?> = emptyMap(),
  val openSearches: List<String> = emptyList(),
  val lastSearchCorrected: String? = null
) {

  val fieldsByNameLower by lazy { fields.mapKeys { it.key.lowercase() } }
  val searchesByNameLower by lazy { searches.mapKeys { it.key.lowercase() } }

  enum class Nulls {
    FIRST, LAST
  }

  data class SearchRendered(
    val search: String, val condition: Condition?
  )

  data class DataSet<T : Table<R>, R : Record, M>(val db: DSLContext, val query: TypedQuery<T, R, M>) {

    fun search(search: String): DataSet<T, R, M> {
      return copy(db = db, query = query.search(search))
    }

    fun query(): SelectQuery<R> {
      val jooqQuery = db.selectQuery(query.table)
      if (query.offset > 0) {
        jooqQuery.addOffset(query.offset)
      }
      if (query.limit > -1) {
        jooqQuery.addLimit(query.limit)
      }
      for (ordering in query.order) {
        jooqQuery.addOrderBy(ordering)
      }
      jooqQuery.addConditions(query.conditions)
      return jooqQuery
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

    fun count(): Long {
      return db.selectCount().from(query.table).where(query.conditions).fetchOne(0, Long::class.java)!!
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
      conditions = conditions + rendered.condition, lastSearchCorrected = rendered.search
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
            { s -> targetedSearch(s, table) }
          } else {
            fieldsByNameLower[target]?.search
          }
        } else {
          // No targeted condition provider
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
            val searchCondition = searches[openSearch]!!(value.value, table)
            fieldsCondition = fieldsCondition?.or(searchCondition) ?: searchCondition
          }
          // Negate the condition if necessary
          if (value.negated) fieldsCondition = fieldsCondition?.not()
          termCondition = mergeCondition(termCondition, fieldsCondition, value.conjunction)
        }
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
    val orderable: Boolean = false,
    val direction: Direction? = null,
    val search: ((String) -> Condition?)? = null,
    val primaryKey: Boolean = false,
    val type: String? = null,
    val open: Boolean = true,
    val orderNulls: Nulls? = null
  ) {


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
      private var orderNulls: Nulls? = null
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
          orderNulls = orderNulls
        )
      }
    }
  }

  data class Builder<T : Table<R>, R : Record, M>(
    var query: TypedQuery<T, R, M>,
    var doDetectFields: Boolean = false,
  ) {


    fun <T> field(
      fieldUnscoped: Field<T>, init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit)? = null
    ) {
      // Ensure that the field is scoped to the outside table
      val field =
        DSL.field(DSL.name(fieldUnscoped.qualifiedName.last()), fieldUnscoped.dataType)
      val builder = FieldConfiguration.Builder(field)
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
      search: (query: String, table: T) -> Condition?
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
      db: DSLContext, typesOnly: Boolean = false
    ) {
      DataSet(db, query.limit(0)).result().recordType().fields().map { field ->
        val existing = query.fields[field.name]
        val dataType = field.dataType.converter.toType().simpleName
        if (existing == null) {
          if (!typesOnly) {
            field(field) {
              type = dataType
            }
          }
        } else {
          query = query.copy(
            fields = query.fields + Pair(
              field.name, existing.copy(
                type = dataType
              )
            )
          )
        }
      }
    }
  }

  companion object {
    fun <R : Record, M> forTable(
      table: Table<R>, mapper: (R) -> M, init: (Builder<Table<R>, R, M>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, M> {
      val builder = Builder(
        TypedQuery(table = table, mapper = mapper)
      )
      init?.let { builder.it() }
      return builder.query
    }

    fun <R : Record> forTableMaps(
      table: Table<R>, init: (Builder<Table<R>, R, MutableMap<String, Any>>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, MutableMap<String, Any>> {
      return forTable(table, { r -> r.intoMap() }, init)
    }

    fun <R : Record> forTableRecords(
      table: Table<R>, init: (Builder<Table<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, R> {
      return forTable(table, { r -> r }, init)
    }
  }
}


