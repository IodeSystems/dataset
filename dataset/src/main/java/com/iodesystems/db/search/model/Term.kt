package com.iodesystems.db.search.model

data class Term(val target: String?, val conjunction: Conjunction, val values: List<TermValue>) {

  constructor(target: String?, conjunction: Conjunction, value: String, vararg extra: String) : this(
    target,
    conjunction,
    convert(value, *extra)
  )

  constructor(conjunction: Conjunction, value: String, vararg extra: String) : this(
    null,
    conjunction,
    convert(value, *extra)
  )

  constructor(value: String, vararg extra: String) : this(null, Conjunction.AND, convert(value, *extra))

  companion object {
    private fun convert(value: String, vararg extra: String): List<TermValue> {
      val termValues = ArrayList<TermValue>()
      termValues.add(TermValue(Conjunction.AND, value))
      for (s in extra) {
        termValues.add(TermValue(Conjunction.AND, s))
      }
      return termValues
    }
  }
}
