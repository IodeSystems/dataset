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
}
