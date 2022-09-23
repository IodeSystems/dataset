package com.iodesystems.db.search

import org.jooq.Condition
import org.jooq.Field

interface SearchFieldConditionProvider<K, T : Field<K>> {
  fun search(search: String, field: T): Condition
}
