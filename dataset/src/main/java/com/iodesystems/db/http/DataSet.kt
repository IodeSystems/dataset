package com.iodesystems.db.http

import com.iodesystems.db.query.TypedQuery
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.SortOrder
import org.jooq.Table
import org.jooq.impl.DSL
import java.util.*

class DataSet {
  companion object {
    fun <R : Record, M> forTable(
      table: Table<R>,
      mapper: (R) -> M,
      init: (TypedQuery.Builder<Table<R>, R, M>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, M> {
      return TypedQuery.forTable(table, mapper, init)
    }

    fun <R : Record> forTable(
      table: Table<R>,
      init: (TypedQuery.Builder<Table<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, R> {
      return TypedQuery.forTable(table, { it }, init)
    }

    fun <R : Record> forTableMaps(
      table: Table<R>,
      init: (TypedQuery.Builder<Table<R>, R, MutableMap<String, Any>>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, MutableMap<String, Any>> {
      return TypedQuery.forTableMaps(table, init)
    }

    fun <R : Record> forTableRecords(
      table: Table<R>, init: (TypedQuery.Builder<Table<R>, R, R>.() -> Unit)? = null
    ): TypedQuery<Table<R>, R, R> {
      return TypedQuery.forTableRecords(table, init)
    }

    enum class RequestTransform {
      SEARCH, PARTITION, ORDERING, SELECTION
    }

    fun <T : Table<R>, R : Record, M> applyRequestTransforms(
      query: TypedQuery<T, R, M>,
      request: Request,
      transform: EnumSet<RequestTransform> = EnumSet.allOf(RequestTransform::class.java)
    ): TypedQuery<T, R, M> {
      return query
        .let { queryUnfiltered ->
          // Apply selection keys
          if (request.selection != null && transform.contains(RequestTransform.SELECTION)) {
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
              request.selection.keys.map { keyRow ->
                DSL.and(
                  keyRow.mapIndexed { index, key ->
                    fields[index](key)
                  }
                )
              }).let {
              // Apply inversion
              if (request.selection.include) {
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
          if (!request.partition.isNullOrEmpty() && transform.contains(RequestTransform.PARTITION)) {
            it.search(request.partition)
          } else {
            it
          }
        }
        .let {
          // Apply search
          if (!request.search.isNullOrEmpty() && transform.contains(RequestTransform.SEARCH)) {
            it.search(request.search)
          } else {
            it
          }
        }
        .let {
          // Apply ordering
          if (!request.ordering.isNullOrEmpty() && transform.contains(RequestTransform.ORDERING)) {
            it.clearOrder()
              .let { dataSet ->
                for (ordering in request.ordering) {
                  dataSet.order(
                    ordering.field,
                    if (Order.Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
                  )
                }
                dataSet
              }
          } else {
            it
          }
        }
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
  )

  data class Response<T>(
    val data: List<T>,
    val count: Count? = null,
    val columns: List<Column>? = null,
    val searchRendered: String? = null
  ) {
    data class Count(
      val inPartition: Long,
      val inQuery: Long
    )

    companion object {

      fun <T> fromRequest(
        db: DSLContext,
        query: TypedQuery<*, *, T>,
        request: Request
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
            dataSet = dataSet.search(request.search)
            val inQuery = dataSet.count()
            count = Count(inPartition, inQuery)
          }
        } else {
          if (!request.search.isNullOrEmpty()) {
            dataSet = dataSet.search(request.search)
          }
        }

        // Apply ordering
        if (!request.ordering.isNullOrEmpty()) {
          dataSet = dataSet.clearOrder()
          for (ordering in request.ordering) {
            dataSet = dataSet.order(
              ordering.field,
              if (Order.Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
            )
          }
        }
        // Apply limit and offset
        val page = request.page ?: 0
        val pageSize = request.pageSize ?: 50
        val data = dataSet.page(page, pageSize)
        // Do we even want these columns?
        val columns = if (request.showColumns == true) {
          dataSet.query.fields.values.map { field ->
            val order = dataSet.query.order.find { it.name == field.field.name }?.order
            Column(
              name = field.name,
              title = field.title,
              type = field.type,
              searchable = field.search != null,
              orderable = field.orderable,
              sortDirection = when (order) {
                null -> null
                SortOrder.ASC -> Order.Direction.ASC
                else -> Order.Direction.DESC
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
      val sortDirection: Order.Direction?,
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
