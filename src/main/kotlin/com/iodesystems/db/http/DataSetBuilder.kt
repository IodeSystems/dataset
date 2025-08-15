package com.iodesystems.db.http

import com.iodesystems.db.query.TypedQuery
import org.jooq.Record
import org.jooq.Select

// New DataSetBuilder API implementation
object DataSetBuilder {
  fun <T : Record> build(block: DataSetBuilderContext.() -> Select<T>): TypedQuery<Select<T>, T, T> {
    val context = DataSetBuilderContext()
    val query = block(context)
    return context.buildTypedQuery(query)
  }

  fun buildRecords(block: DataSetBuilderContext.() -> Select<out Record>): TypedQuery<Select<Record>, Record, Record> {
    val context = DataSetBuilderContext()
    val query = block(context)
    @Suppress("UNCHECKED_CAST")
    return context.buildTypedQuery(query) as TypedQuery<Select<Record>, Record, Record>
  }

}
