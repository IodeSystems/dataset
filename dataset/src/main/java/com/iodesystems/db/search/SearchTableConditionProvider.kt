package com.iodesystems.db.search

import org.jooq.Condition
import org.jooq.Table

interface SearchTableConditionProvider<T : Table<*>> {
  fun search(search: String, table: T): Condition
}
