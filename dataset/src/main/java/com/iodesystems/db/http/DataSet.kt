package com.iodesystems.db.http

import com.iodesystems.db.query.TypedQuery
import org.jooq.DSLContext
import org.jooq.SortOrder

class DataSet {

  data class Request(
    var search: String? = null,
    var partition: String? = null,
    val ordering: List<Order> = emptyList(),
    var page: Int = 0,
    var pageSize: Int = 0,
  )

  data class Response<T>(
    val recordsFiltered: Long, val recordsTotal: Long, val data: List<T>, val columns: List<Column>
  ) {
    companion object {
      fun <T> fromRequest(
        db: DSLContext, query: TypedQuery<*, *, T>, request: Request
      ): Response<T> {
        var dataSet = query.data(db)
        val partition = request.partition
        if (!partition.isNullOrEmpty()) {
          dataSet = dataSet.search(partition)
        }

        val recordsTotal = dataSet.count()

        if (request.ordering.isNotEmpty()) {
          for (ordering in request.ordering) {
            dataSet = dataSet.order(
              ordering.field, if (Order.Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
            )
          }
        }
        val data = dataSet.page(request.page, request.pageSize)
        if (data.size < request.pageSize) {
          data.size
        } else {
          dataSet.clearOrder().count()
        }

        val search = request.search
        val recordsFiltered = if (!search.isNullOrEmpty()) {
          dataSet = dataSet.search(search)
          dataSet.count()
        } else {
          recordsTotal
        }

        return Response(
          recordsTotal = recordsTotal,
          recordsFiltered = recordsFiltered,
          columns = dataSet.query.fields.values.map { field ->
            val order = dataSet.query.order.find { it.name == field.name }?.order
            Column(
              name = field.name,
              title = field.title,
              mapping = field.mapping,
              type = field.type,
              searchable = field.search != null,
              orderable = field.orderable,
              sortDirection = when (order) {
                null -> null
                SortOrder.ASC -> Order.Direction.ASC
                else -> Order.Direction.DESC
              },
              primaryKey = field.primaryKey
            )
          },
          data = dataSet.page(request.page, request.pageSize)
        )
      }
    }

    data class Column(
      val name: String,
      val title: String,
      val mapping: String,
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
