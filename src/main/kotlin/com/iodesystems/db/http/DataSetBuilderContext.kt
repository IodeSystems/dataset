package com.iodesystems.db.http

import com.iodesystems.db.query.TypedQuery
import org.jooq.Condition
import org.jooq.Field
import org.jooq.Record
import org.jooq.Select

// Builder context for the DSL - simplified to only handle field configuration
class DataSetBuilderContext {
  private val fieldConfigs = mutableListOf<FieldConfig<*>>()

  fun <F> field(jooqField: Field<F>, config: FieldConfigBuilder<F>.() -> Unit): Field<F> {
    val configBuilder = FieldConfigBuilder(jooqField)
    config(configBuilder)
    fieldConfigs.add(configBuilder.build())
    return jooqField
  }

  internal fun <T : Record> buildTypedQuery(query: Select<T>): TypedQuery<Select<T>, T, T> {
    val dataSetFields = DataSet.build { fieldsBuilder ->
      fieldConfigs.forEach { config ->
        fieldsBuilder.field(config.field) { f ->
          if (config.isPrimaryKey) {
            primaryKey = true
          }
          if (config.isOrderable) {
            val direction = when (config.orderDirection) {
              DataSet.Order.Direction.ASC -> DataSet.Order.Direction.ASC
              DataSet.Order.Direction.DESC -> DataSet.Order.Direction.DESC
              else -> DataSet.Order.Direction.ASC
            }
            orderable(direction)
          }
          config.searchFunction?.let { searchFn ->
            search(config.globalSearch) { searchString ->
              @Suppress("UNCHECKED_CAST")
              (searchFn as (Field<Any>, String) -> Condition)(f as Field<Any>, searchString)
            }
          }
        }
      }
    }

    @Suppress("UNCHECKED_CAST")
    return dataSetFields.toTypedQuery { _ -> query } as TypedQuery<Select<T>, T, T>
  }
}
