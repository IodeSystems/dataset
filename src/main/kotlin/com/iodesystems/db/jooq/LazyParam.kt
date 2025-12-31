@file:Suppress("DEPRECATION", "removal", "UnstableApiUsage")

package com.iodesystems.db.jooq

import org.jooq.*
import org.jooq.impl.DSL

/**
 * A lazy-evaluated JOOQ field whose value is computed at query execution time.
 *
 * This is more primitive and composable than LazyCondition - it creates a lazy
 * value that can be used anywhere in a query (conditions, fields, etc).
 *
 * Not typically used directly - use `lazyValue { }` in the DataSet DSL instead.
 *
 * @param T The type of value this parameter produces
 * @param type The Java class of the value type
 * @param supplier Lambda that computes the value at query execution time
 */
class LazyParam<T>(
  private val type: Class<T>,
  private val supplier: () -> T
) : SQL, QueryPartInternal {

  @Deprecated("Deprecated in Java")
  override fun rendersContent(ctx: Context<*>) = true

  @Deprecated("Deprecated in Java")
  override fun accept(ctx: Context<*>) {
    // Evaluate the supplier and render the value at query execution time
    ctx.visit(DSL.`val`(supplier(), type))
  }

  override fun clauses(ctx: Context<*>?) = emptyArray<Clause>()

  override fun declaresFields() = false

  override fun declaresTables() = false

  override fun declaresWindows() = false

  override fun declaresCTE() = false

  override fun generatesCast() = false
}

/**
 * Internal helper for creating lazy parameters.
 * Not typically used directly - use `lazyValue { }` in the DataSet DSL instead.
 */
inline fun <reified T> lazyParam(noinline supplier: () -> T): LazyParam<T> {
  return LazyParam(T::class.java, supplier)
}
