package com.iodesystems.db

import com.iodesystems.db.DataSet
import org.h2.jdbcx.JdbcConnectionPool
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import java.time.OffsetDateTime

object TestUtils {

  data class Setup<R : Record, M, T>(
    val db: DefaultDSLContext,
    val query: DataSet<Select<R>, R, M>,
    val queries: MutableList<String>,
    val meta: () -> T
  )

  fun <T : Record, M> setup(
    block: (DSLContext) -> DataSet<Select<T>, T, M>
  ): Setup<T, M, Unit> {
    return setup(Unit, block)
  }

  fun <T : Record, M, Q> setup(
    meta: Q,
    block: (DSLContext) -> DataSet<Select<T>, T, M>
  ): Setup<T, M, Q> {
    val queries = mutableListOf<String>()
    val config = DefaultConfiguration().apply {
      set(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"))
      set(SQLDialect.H2)
      set(ExecuteListener.onExecuteEnd {
        queries.add(it.query().toString())
      })
    }
    val db = DefaultDSLContext(config)
    return Setup(db, block(db), queries, meta = { meta })
  }

  object SimpleMeta {
    val EMAIL = DSL.table("EMAIL")
    val EMAIL_ID = DSL.field("EMAIL_ID", Long::class.java)
    val CONTENT = DSL.field("CONTENT", String::class.java)
    val FROM = DSL.field("FROM_EMAIL", String::class.java)
    val ATTACHMENT = DSL.field("ATTACHMENT", String::class.java)
    val CREATED_AT = DSL.field("CREATED_AT", OffsetDateTime::class.java)
    val AUTODETECT = DSL.field("AUTO_DETECT", OffsetDateTime::class.java)

    fun create(db: DSLContext) {
      db.createTable(EMAIL)
        .column(EMAIL_ID)
        .column(CONTENT)
        .column(FROM)
        .column(ATTACHMENT)
        .column(CREATED_AT)
        .execute()
    }
  }

  fun setup(): Setup<Record, Map<String, *>, SimpleMeta> {
    @Suppress("UNCHECKED_CAST")
    return setup(SimpleMeta) { db ->
      SimpleMeta.create(db)
      DataSet {
        search("is_x", open = true) { s ->
          if (s == "x") DSL.trueCondition()
          else null
        }
        search("daysAgo") { s ->
          if (s.startsWith("<")) {
            val ltDaysAgo = s.drop(1).toLongOrNull()
            if (ltDaysAgo == null) null
            else SimpleMeta.CREATED_AT.lessOrEqual(OffsetDateTime.now().minusDays(ltDaysAgo))
          } else if (s.startsWith(">")) {
            val gtDaysAgo = s.drop(1).toLongOrNull()
            if (gtDaysAgo == null) null
            else SimpleMeta.CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(gtDaysAgo))
          } else {
            val daysAgo = s.toLongOrNull()
            if (daysAgo == null) null
            else SimpleMeta.CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(daysAgo))
          }
        }

        db.select(
          field(SimpleMeta.EMAIL_ID),
          field(SimpleMeta.CONTENT) {
            search { f, s ->
              f.containsIgnoreCase(s)
            }
          },
          field(SimpleMeta.FROM) {
            search { f, s ->
              f.eq(s)
            }
          },
          field(SimpleMeta.ATTACHMENT) {
            search { f, s ->
              if (s.lowercase() == "true") {
                f.isNull
              } else {
                null
              }
            }
          },
          SimpleMeta.CREATED_AT
        ).from(SimpleMeta.EMAIL)
      }.mapBatch { records -> records.map { it.intoMap() } }
    } as Setup<Record, Map<String, *>, SimpleMeta>
  }
}
