package com.iodesystems.db.search.model

data class Term(
  val values: List<TermValue>,
  val target: String?,
  val conjunction: Conjunction,
  val negated: Boolean = false,
) {
  companion object {
    fun simple(
      value: String,
      conjunction: Conjunction = Conjunction.AND,
      target: String? = null,
      negated: Boolean = false
    ): Term {
      return simple(listOf(value), conjunction, target, negated)
    }

    fun simple(
      values: List<String>,
      conjunction: Conjunction = Conjunction.AND,
      target: String? = null,
      negated: Boolean = false
    ): Term {
      return Term(values.map { TermValue(Conjunction.AND, it, negated) }, target, conjunction, negated)
    }
  }
}
