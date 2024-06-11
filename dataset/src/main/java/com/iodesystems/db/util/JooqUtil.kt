package com.iodesystems.db.util

import org.jooq.Field
import org.jooq.impl.DSL

class JooqUtil {
  fun <T> Field<T>.ifNull(
    vararg value: Field<T>
  ): Field<T> {
    return DSL.coalesce(this, *value).`as`(this.name)
  }

  fun <T> Field<T>.ifNull(
    vararg values: T
  ): Field<T> {
    return DSL.coalesce(
      this,
      *values.map { DSL.`val`(it) }.toTypedArray()
    ).`as`(this.name)
  }
}
