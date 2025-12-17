package com.iodesystems.db.http

import com.iodesystems.db.http.DataSet.Order.Direction
import com.iodesystems.db.query.Fields
import com.iodesystems.db.query.TypedQuery
import org.jooq.*
import org.jooq.impl.DSL
import java.util.*

class DataSet {
  companion object {

    fun build(init: (Fields<Record>.(Fields<Record>) -> Unit)): Fields<Record> {
      return build(
        Record::class.java,
        init
      )
    }

    fun buildForMaps(init: (Fields<Map<String, *>>.(Fields<Map<String, *>>) -> Unit)): Fields<Map<String, *>> {
      @Suppress("UNCHECKED_CAST")
      return build(Map::class.java as Class<Map<String, *>>, init)
    }

    fun <T> build(
      mappedType: Class<T>,
      init: (Fields<T>.(Fields<T>) -> Unit)
    ): Fields<T> {
      return Fields(
        mappedType = mappedType,
        customMapper = null,
        fields = mutableListOf(),
        searches = mutableListOf(),
        init = init
      )
    }

    fun <R : Record> forTable(
      table: Table<R>,
      init: (TypedQuery.Builder<Select<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, R> {
      return TypedQuery.forTable(DSL.selectFrom(table), { it }, init)
    }

    fun <R : Record, T> forTable(
      table: Table<R>,
      mapper: (List<Record>) -> List<T>,
      init: (TypedQuery.Builder<Select<R>, R, T>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, T> {
      return TypedQuery.forTable(DSL.selectFrom(table), mapper, init)
    }

    fun <R : Record> forTable(
      table: Select<R>,
      init: (TypedQuery.Builder<Select<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Select<R>, R, R> {
      return TypedQuery.forTable(table, { it }, init)
    }


    enum class RequestTransform {
      SEARCH, PARTITION, ORDERING, SELECTION
    }


  }

  data class Selection(
    val include: Boolean,
    val keys: List<List<String>>
  )

  data class Request(
    val search: String? = null,
    val partition: String? = null,
    val ordering: List<Order>? = null,
    val page: Int? = null,
    val pageSize: Int? = null,
    val showCounts: Boolean? = null,
    val showColumns: Boolean? = null,
    val selection: Selection? = null
  ) {
    fun <T> toResponse(
      db: DSLContext,
      dataSet: TypedQuery<*, *, T>
    ): Response<T> {
      return Response.fromRequest(db, dataSet, this)
    }

    fun <T : Select<R>, R : Record, M> transform(
      query: TypedQuery<T, R, M>,
      transform: EnumSet<RequestTransform> = EnumSet.allOf(RequestTransform::class.java),
      unlimit: Boolean = false
    ): TypedQuery<T, R, M> {
      return query
        .let { queryUnfiltered ->
          // Apply selection keys
          if (selection != null && transform.contains(RequestTransform.SELECTION)) {
            // Build key set
            val fields = query.fields.entries.mapNotNull { f ->
              if (f.value.primaryKey) {
                val field = f.value.field
                f.value.search ?: { s -> field.cast(String::class.java).eq(s) }
              } else {
                null
              }
            }
            // Convert keys to rows
            val condition = DSL.or(
              selection.keys.map { keyRow ->
                DSL.and(
                  keyRow.mapIndexed { index, key ->
                    fields[index](key)
                  }
                )
              }).let {
              // Apply inversion
              if (selection.include) {
                it
              } else {
                it.not()
              }
            }
            queryUnfiltered.where(condition)
          } else {
            queryUnfiltered
          }
        }
        .let {
          // Apply partition
          if (!partition.isNullOrEmpty() && transform.contains(RequestTransform.PARTITION)) {
            it.search(partition)
          } else {
            it
          }
        }
        .let {
          // Apply search
          if (!search.isNullOrEmpty() && transform.contains(RequestTransform.SEARCH)) {
            it.search(search)
          } else {
            it
          }
        }
        .let {
          // Apply ordering
          if (!ordering.isNullOrEmpty() && transform.contains(RequestTransform.ORDERING)) {
            it.clearOrder()
              .let { dataSet ->
                for (ordering in ordering) {
                  dataSet.order(
                    ordering.field,
                    if (Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
                  )
                }
                dataSet
              }
          } else {
            it
          }
        }.let {
          // Apply limit and offset
          if (unlimit) {
            it.page(1, -1)
          } else if (pageSize != null && page != null) {
            it.page(page, pageSize)
          } else {
            it
          }
        }
    }
  }

  data class Response<T>(
    val data: List<T>,
    val count: Count? = null,
    val columns: List<Column>? = null,
    val searchRendered: String? = null
  ) {
    data class Count(
      val inPartition: Long,
      val inQuery: Long,
    )

    companion object {

      fun <T> fromRequest(
        db: DSLContext,
        query: TypedQuery<*, *, T>,
        request: Request,
        defaultPageSize: Int = 50,
      ): Response<T> {
        var dataSet = query.data(db)

        // Apply partition first
        val partition = request.partition
        if (!partition.isNullOrEmpty()) {
          dataSet = dataSet.search(partition)
        }

        // Maybe fetch counts?
        var count: Count? = null
        if (request.showCounts == true) {
          if (request.search.isNullOrEmpty()) {
            val inPartition = dataSet.count()
            count = Count(inPartition, inPartition)
          } else {
            val inPartition = dataSet.count()
            if (inPartition == 0L) {
              count = Count(0, 0)
            } else {
              dataSet = dataSet.search(request.search)
              val inQuery = dataSet.count()
              count = Count(inPartition, inQuery)
            }
          }
        } else {
          if (!request.search.isNullOrEmpty()) {
            dataSet = dataSet.search(request.search)
          }
        }

        val shouldQuery = request.showCounts != true || (count?.inQuery ?: 0) > 0

        // Apply ordering
        if (!request.ordering.isNullOrEmpty()) {
          dataSet = dataSet.clearOrder()
          for (ordering in request.ordering) {
            dataSet = dataSet.order(
              ordering.field,
              if (Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
            )
          }
        }
        val page = request.page ?: 0
        val pageSize = request.pageSize ?: defaultPageSize
        val data = if (shouldQuery) {
          dataSet.page(page, pageSize)
        } else {
          emptyList()
        }
        // Do we even want these columns?
        val columns = if (request.showColumns == true) {
          dataSet.query.fields.values.map { field ->
            val order = dataSet.query.order.find { it.name == field.field.name }?.order
            Column(
              name = field.name,
              title = field.title,
              type = field.field.dataType.type.simpleName,
              searchable = field.search != null,
              orderable = field.orderable,
              sortDirection = when (order) {
                null -> null
                SortOrder.ASC -> Direction.ASC
                else -> Direction.DESC
              },
              primaryKey = field.primaryKey,
            )
          }
        } else {
          null
        }

        return Response(
          count = count,
          columns = columns,
          data = data,
          // Only return the search if we applied a search here.
          // We ignore partitioning and sub-searches and only pay attention to the request searching
          searchRendered = if (request.search.isNullOrEmpty()) "" else dataSet.query.lastSearchCorrected
        )
      }
    }

    data class Column(
      val name: String,
      val title: String,
      val searchable: Boolean,
      val orderable: Boolean,
      val sortDirection: Direction?,
      val primaryKey: Boolean,
      val type: String?,
    ) {
      override fun toString(): String {
        return name
      }
    }
  }

  data class Order(
    val field: String,
    val order: Direction
  ) {
    enum class Direction {
      ASC, DESC
    }
  }
}

// Data classes for field configuration
data class FieldConfig<F>(
  val field: Field<F>,
  val isPrimaryKey: Boolean = false,
  val isOrderable: Boolean = false,
  val orderDirection: Direction? = null,
  val searchFunction: ((Field<F>, String) -> Condition?)? = null,
  val globalSearch: Boolean = true
)

// Field configuration builder
class FieldConfigBuilder<F>(private val field: Field<F>) {
  private var isPrimaryKey = false
  private var isOrderable = false
  private var orderDirection: Direction? = null
  private var searchFunction: ((Field<F>, String) -> Condition?)? = null
  private var globalSearch = true

  fun primaryKey() {
    isPrimaryKey = true
  }

  fun orderable(direction: Direction = Direction.ASC) {
    isOrderable = true
    orderDirection = direction
  }

  fun search(global: Boolean = true, fn: (Field<F>, String) -> Condition?) {
    globalSearch = global
    searchFunction = fn
  }

  internal fun build(): FieldConfig<F> {
    return FieldConfig(field, isPrimaryKey, isOrderable, orderDirection, searchFunction, globalSearch)
  }
}
