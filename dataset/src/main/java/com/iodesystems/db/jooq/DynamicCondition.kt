@file:Suppress("DEPRECATION", "removal", "UnstableApiUsage")

package com.iodesystems.db.jooq

import org.jooq.*

class DynamicCondition(
  val block: () -> Condition
) : SQL, QueryPartInternal {
  @Deprecated("Deprecated in Java")
  override fun rendersContent(p0: Context<*>) = true

  @Deprecated("Deprecated in Java")
  override fun accept(p0: Context<*>) {
    p0.visit(block())
  }

  override fun clauses(p0: Context<*>?) = emptyArray<Clause>()

  override fun declaresFields() = true

  override fun declaresTables() = true

  override fun declaresWindows() = true

  override fun declaresCTE() = true

  override fun generatesCast() = true
}
