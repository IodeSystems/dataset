package com.iodesystems.db.search.model

import com.iodesystems.db.search.SearchConditionProvider

data class TargetedSearch(val name: String, val searchConditionProvider: SearchConditionProvider)
