package com.iodesystems.db.query

import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import org.jooq.*
import org.jooq.impl.DSL
import java.util.*

data class TypedQuery<T : Table<R>, R : Record, M>(
  val table: T,
  val mapper: (R) -> M,
  val conditions: List<Condition> = emptyList(),
  val offset: Int = 0,
  val limit: Int = 100,
  val order: List<SortField<*>> = emptyList(),
  val fields: Map<String, FieldConfiguration<*>> = emptyMap(),
  val searches: Map<String, (query: String, table: T) -> Condition?> = emptyMap(),
  val searchesToApply: List<String> = emptyList(),
) {

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
      val searchesToApply = query.getConditionsWithSearch()
      if (searchesToApply != null) {
        jooqQuery.addConditions(searchesToApply)
      }
      return jooqQuery
    }

    internal fun result(): Result<*> {
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
      val result: Result<R> = result() as Result<R>
      return result.map(query.mapper)
    }

    fun count(): Long {
      return db.selectCount().from(query.table).where(query.getConditionsWithSearch()).fetchOne(0, Long::class.java)!!
    }

    fun empty(): Boolean {
      return copy(query = query.one()).count() != 1L
    }

    fun order(field: String, sortOrder: SortOrder): DataSet<T, R, M> {
      return copy(query = query.order(field, sortOrder))
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
    return copy(
      searchesToApply = searchesToApply + query
    )
  }

  fun where(condition: Condition): TypedQuery<T, R, M> {
    return copy(
      conditions = conditions + condition
    )
  }

  fun getConditionsWithSearch(): Condition? {
    if (searchesToApply.isEmpty()) {
      return null
    }
    val searchConditions: MutableList<Condition> = conditions.toMutableList()
    val searchParser = SearchParser()
    for (search in searchesToApply) {
      try {
        var termsCondition: Condition? = null
        for (term in searchParser.parse(search)) {
          val conditionProvider: ((s: String) -> Condition?)? = if (term.target != null) {
            val target = term.target.lowercase()
            val targetedSearch = searches[target]
            if (targetedSearch != null) {
              { s -> targetedSearch(s, table) }
            } else {
              fields[target]?.search
            }
          } else {
            null
          }
          var termCondition: Condition? = null
          for (value in term.values) {
            if (conditionProvider != null) {
              val targetedSearchCondition = conditionProvider(value.value)
              if (termCondition == null) {
                termCondition = targetedSearchCondition
                continue
              } else if (value.conjunction == Conjunction.AND) {
                termCondition = DSL.and(termCondition, targetedSearchCondition)
                continue
              } else {
                termCondition = DSL.or(termCondition, targetedSearchCondition)
                continue
              }
            }
            var fieldsCondition: Condition? = null
            for (field in fields.values) {
              val fieldSearch = field.search ?: continue
              val fieldSearchCondition = fieldSearch(value.value)
              fieldsCondition = fieldsCondition?.or(fieldSearchCondition) ?: fieldSearchCondition
            }
            termCondition = mergeCondition(termCondition, fieldsCondition, value.conjunction)
          }
          termsCondition = mergeCondition(termsCondition, termCondition, term.conjunction)
        }
        if (termsCondition != null) {
          searchConditions.add(termsCondition)
        }
      } catch (e: InvalidSearchStringException) {
        throw SneakyInvalidSearchStringException(
          "Invalid search while building conditions:" + e.message, e
        )
      }
    }
    return if (searchConditions.isEmpty()) {
      null
    } else {
      DSL.and(searchConditions)
    }
  }

  private fun mergeCondition(
    termsCondition: Condition?, termCondition: Condition?, conjunction: Conjunction?
  ): Condition? {
    var termsCondition = termsCondition
    termsCondition = if (termCondition == null) {
      return termsCondition
    } else if (termsCondition == null) {
      termCondition
    } else if (conjunction == Conjunction.AND) {
      DSL.and(termsCondition, termCondition)
    } else {
      DSL.or(termsCondition, termCondition)
    }
    return termsCondition
  }

  fun clearOrder(): TypedQuery<T, R, M> {
    return copy(order = emptyList())
  }

  fun order(field: String, sortOrder: SortOrder?): TypedQuery<T, R, M> {
    val fieldConfiguration = fields[field.lowercase(Locale.getDefault())] ?: return this
    return order(fieldConfiguration.field, sortOrder)
  }

  fun order(field: Field<*>, order: SortOrder?): TypedQuery<T, R, M> {
    return copy(order = this.order + field.sort(order))
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
      searchesToApply = searchesToApply,
    )
  }


  data class FieldConfiguration<T>(
    val field: Field<T>,
    val name: String = field.name,
    val title: String = name,
    val orderable: Boolean = false,
    val search: ((String) -> Condition?)? = null,
    val primaryKey: Boolean = false,
    val type: String? = null,
  ) {
    data class Builder<T>(
      val field: Field<T>,
      var name: String = field.name,
      var title: String = name,
      var orderable: Boolean = false,
      var search: ((String) -> Condition?)? = null,
      var primaryKey: Boolean = false,
      var type: String? = null,

      ) {
      fun build(): FieldConfiguration<T> {
        return FieldConfiguration(
          field = field,
          name = name,
          title = title,
          orderable = orderable,
          search = search,
          primaryKey = primaryKey,
          type = type,
        )
      }
    }
  }

  data class Builder<T : Table<R>, R : Record, M>(
    var query: TypedQuery<T, R, M>,
    var doDetectFields: Boolean = false,
    var camelMapping: Boolean = true,
  ) {

    fun disableAutoCamelMapping(set: Boolean = true) {
      camelMapping = set
    }

    private fun mapFieldName(name: String): String {
      if (!camelMapping) {
        return name
      }
      return name
        .lowercase()
        .split("_")
        .joinToString("") { it.replaceFirstChar { c -> c.uppercase() } }
        .replaceFirstChar { it.lowercase() }
    }

    fun <T> field(
      field: Field<T>, init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit)? = null
    ) {
      val builder = FieldConfiguration.Builder(field)
      builder.name = mapFieldName(builder.name)
      init?.let { builder.it(field) }
      query = query.copy(fields = query.fields + Pair(builder.name.lowercase(), builder.build()))
    }

    fun search(
      name: String, search: (query: String, table: T) -> Condition
    ) {
      query = query.copy(searches = query.searches + Pair(name.lowercase(), search))
    }

    fun fields(vararg fields: Field<T>) {
      fields.map {
        field(it)
      }
    }

    fun autoDetectFields(
      db: DSLContext,
      typesOnly: Boolean = false
    ) {
      DataSet(db, query.limit(0)).result().recordType().fields().map { field ->
        val fieldName = mapFieldName(field.name).lowercase()
        val existing = query.fields[fieldName]
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
              existing.name.lowercase(), existing.copy(
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
      table: Table<R>,
      mapper: (R) -> M,
      init: (Builder<Table<R>, R, M>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, M> {
      val builder = Builder(
        TypedQuery(table = table, mapper = mapper)
      )
      init?.let { builder.it() }
      return builder.query
    }

    fun <R : Record> forTableMaps(
      table: Table<R>,
      init: (Builder<Table<R>, R, MutableMap<String, Any>>.() -> Unit)? = null
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


