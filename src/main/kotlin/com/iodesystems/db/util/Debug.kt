package com.iodesystems.db.util

import com.iodesystems.db.DataSet
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select

data class Debug(
  val req: DataSet.Request,
  val query: DataSet<*, *, *>,
  val search: String,
  val searchConditions: String,
  val partition: String,
  val partitionConditions: String,
  val sql: String,
) {
  companion object {
    fun <T : Select<R>, R : Record, M> DataSet<T, R, M>.debug(db: DSLContext, req: DataSet.Request): Debug {
      return Debug(
        req = req,
        query = this,
        search = req.search ?: "",
        searchConditions = if (req.search == null) "" else this.renderSearch(req.search).condition.toString(),
        partition = req.partition ?: "",
        partitionConditions = if (req.partition == null) "" else this.renderSearch(req.partition).condition.toString(),
        sql = req.transform(this).data(db).query().sql,
      )
    }
  }
}


