package com.iodesystems.db.search.model

import org.jooq.Condition
import org.jooq.Field
import org.jooq.impl.DSL
import java.util.*

object Search {
  fun isLong(search: String?, field: Field<Long?>): Condition? {
    return try {
      field.eq(java.lang.Long.valueOf(search))
    } catch (ignored: IllegalArgumentException) {
      null
    }
  }

  fun equalsStringIgnoreCase(search: String?, field: Field<String?>): Condition {
    return field.equalIgnoreCase(search)
  }

  fun containsIgnoreCase(search: String?, field: Field<String?>): Condition {
    return field.containsIgnoreCase(search)
  }

  @JvmStatic
  fun contains(search: String?, field: Field<String?>): Condition {
    return field.contains(search)
  }

  fun startsWithIgnoreCaseOrEmpty(search: String, field: Field<String?>): Condition {
    return if (search.isEmpty()) {
      DSL.trueCondition()
    } else {
      field.lower().startsWith(search.lowercase(Locale.getDefault()))
    }
  }

  fun startsWithIgnoreCase(search: String, field: Field<String?>): Condition {
    return field.lower().startsWith(search.lowercase(Locale.getDefault()))
  }

  fun equalsString(search: String?, field: Field<String?>): Condition {
    return field.startsWith(search)
  }

  @JvmStatic
  fun <F> eq(s: String, fField: Field<F>, convert: (s: String) -> F): Condition {
    return fField.eq(convert(s))
  }

  fun isBool(s: String, field: Field<Boolean?>): Condition? {
    var s = s
    s = s.lowercase(Locale.getDefault())
    if (s == "y" || s == "yes" || s == "t" || s == "true" || s == "1") {
      return field.eq(true)
    } else if (s == "n" || s == "no" || s == "f" || s == "false" || s == "0") {
      return field.eq(false)
    }
    return null
  }
}
