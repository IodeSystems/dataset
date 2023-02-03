package com.iodesystems.db.search

import org.jooq.Condition

interface SearchConditionProvider {
    fun search(search: String): Condition
}
