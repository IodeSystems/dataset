package com.iodesystems.db

import com.iodesystems.db.http.DataSet
import com.iodesystems.db.query.TypedQuery
import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.search.model.Term
import junit.framework.TestCase.assertEquals
import org.h2.jdbcx.JdbcConnectionPool
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.expect

class TypedQueryTest {

  @Test
  fun testSearchParserConformance() {
    val searchParser = SearchParser()
    searchParser.parse("A")
    searchParser.parse(" A ")
    searchParser.parse(" A B")
    searchParser.parse(" A B ")
    searchParser.parse(" A B, ")
    searchParser.parse(" A B ,")
    searchParser.parse(" A B , C")
    searchParser.parse(" A B , C D ")
    searchParser.parse(" A B , C D E:F")
    searchParser.parse(" A B , C D E: F")
    searchParser.parse(" A B , C D E: F G")
    searchParser.parse(" A B , C D E: F G H:I,J")
    searchParser.parse(" A B , C D E: F G H:I,J \"M N\" O:()")
    searchParser.parse(" A B , C D E: F G H:I,J \"M N\" O:(P Q,R)")
  }

  @Test
  fun testEmptyNegation() {
    val searchParser = SearchParser()
    searchParser.parse("""!""").apply {
      assertEquals("!", search)
    }
    searchParser.parse("""! """).apply {
      assertEquals("! ", search)
    }
    searchParser.parse(""" ! """).apply {
      assertEquals(" ! ", search)
    }
    searchParser.parse(""" ! !! ! !!""").apply {
      assertEquals(" ! !\\! ! !\\!", search)
    }
  }

  @Test
  fun testSearchParserEscapeConformance() {
    val searchParser = SearchParser()
    searchParser.parse("""\A""")
    searchParser.parse("""A\:B""")
    searchParser.parse("""\A\::(\(a,\  \,b\))""")
  }

  @Test
  fun testSearchParserEscapeStrings() {
    val searchParser = SearchParser()
    val result = searchParser.parse("""a\ b\ c d""")
    expect("a b c") {
      result.terms.first().values.first().value
    }
    expect("d") {
      result.terms.last().values.first().value
    }
  }

  @Test
  fun testSearchParser() {
    val searchParser = SearchParser()
    Assert.assertEquals(listOf(Term("A")), searchParser.parse("A").terms)
    Assert.assertEquals(listOf(Term("A"), Term("B")), searchParser.parse("A B").terms)
    Assert.assertEquals(listOf(Term("A"), Term("B")), searchParser.parse("A B").terms)
    Assert.assertEquals(
      listOf(Term("A"), Term("B"), Term(Conjunction.OR, "C")), searchParser.parse("A B , C").terms
    )
    Assert.assertEquals(
      listOf(
        Term("A"),
        Term("B"),
        Term(Conjunction.OR, "C"),
        Term("target", Conjunction.AND, "Y")
      ), searchParser.parse("A B , C target:Y").terms
    )
  }

  @Test
  fun testBadSearch() {
    val result = SearchParser().parse(":")
    assertEquals(1, result.terms.size)
    assertEquals(":", result.terms[0].values[0].value)
  }

  @Test
  fun testEscapeRescape() {
    val result = SearchParser().parse("Sean O'Conner")
    assertEquals("Sean", result.terms[0].values[0].value)
    assertEquals("O'Conner", result.terms[1].values[0].value)
    val result2 = SearchParser().parse("Sean O\\'Conner")
    assertEquals("Sean", result2.terms[0].values[0].value)
    assertEquals("O'Conner", result2.terms[1].values[0].value)
  }

  @Test
  fun testNegation() {
    val result = SearchParser().parse("A !B")
    assertEquals("A", result.terms[0].values[0].value)
    assertEquals("B", result.terms[1].values[0].value)
    assertEquals(false, result.terms[0].values[0].negated)
    assertEquals(true, result.terms[1].values[0].negated)
  }

  @Test
  fun testNegationWithEscaping() {
    SearchParser().parse("""A:(!!a, \!b c!, !d\\!)""").apply {
      assertEquals(1, terms.size)
      terms[0].apply {
        assertEquals(4, values.size)
        assertEquals("A", target)

        assertEquals("!a", values[0].value)
        assertEquals(true, values[0].negated)

        assertEquals("!b", values[1].value)
        assertEquals(false, values[1].negated)

        assertEquals("c!", values[2].value)
        assertEquals(false, values[2].negated)

        assertEquals("d\\!", values[3].value)
        assertEquals(true, values[3].negated)
      }
    }
  }

  @Test
  fun testNegationConditions() {
    val setup = setup()
    @Suppress("SqlResolve")
    setup.db.execute(
      """
      INSERT INTO "EMAIL" (
        EMAIL_ID,
        CONTENT,
        FROM_EMAIL
      ) VALUES
       (1,'A', 'x'),
       (2,'B', 'y'),
       (3,'C', 'z'),
       (4,'AB', 'xx')
    """.trimIndent()
    )
    DataSet.Request(search = "A !B", showCounts = true).let { request ->
      request.toResponse(setup.db, setup.query)
        .let {
          assertEquals(1L, it.count?.inQuery)
          assertEquals(1, it.data.size)
          assertEquals("A", it.data[0].get("CONTENT"))
        }
    }

    assertEquals(
      2L,
      DataSet.Response.fromRequest(
        setup.db, setup.query,
        DataSet.Request(search = "A !C", showCounts = true)
      ).count?.inQuery
    )

    DataSet.Request(search = "!z", showCounts = true).let { req ->
      req.toResponse(setup.db, setup.query).let {
        assertEquals(3L, it.count?.inQuery)
        assertEquals(3, it.data.size)
      }

    }
  }

  data class Setup<R : Record, M>(
    val db: DefaultDSLContext,
    val query: TypedQuery<Select<R>, R, M>,
    val queries: MutableList<String>
  )

  fun <T : Record, M> setup(setup: (DSLContext) -> TypedQuery<Select<T>, T, M>): Setup<T, M> {
    val queries = mutableListOf<String>()
    val config = DefaultConfiguration().apply {
      set(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"))
      set(SQLDialect.H2)
      set(ExecuteListener.onExecuteEnd {
        queries.add(it.query().toString())
      })
    }
    val db = DefaultDSLContext(config)
    return Setup(db, setup(db), queries)
  }

  fun setup(): Setup<Record, Record> {
    return setup { db ->
      val EMAIL = DSL.table("EMAIL")
      DataSet.build {
        val EMAIL_ID = field(DSL.field("EMAIL_ID", Int::class.java))
        val CONTENT = field(DSL.field("CONTENT", String::class.java)) { f ->
          search { s ->
            f.containsIgnoreCase(s)
          }
        }
        val FROM = field(DSL.field("FROM_EMAIL", String::class.java)) { f ->
          search { s ->
            f.eq(s)
          }
        }
        val ATTACHMENT = field(DSL.field("ATTACHMENT", String::class.java)) { f ->
          search { s ->
            if (s.lowercase() == "true") {
              f.isNull
            } else {
              null
            }
          }
        }
        val CREATED_AT = field(DSL.field("CREATED_AT", OffsetDateTime::class.java))

        search("is_x", open = true) { s ->
          if (s == "x") DSL.trueCondition()
          else null
        }
        search("daysAgo") { s ->
          if (s.startsWith("<")) {
            val ltDaysAgo = s.drop(1).toLongOrNull()
            if (ltDaysAgo == null) null
            else CREATED_AT.lessOrEqual(OffsetDateTime.now().minusDays(ltDaysAgo))
          } else if (s.startsWith(">")) {
            val gtDaysAgo = s.drop(1).toLongOrNull()
            if (gtDaysAgo == null) null
            else CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(gtDaysAgo))
          } else {
            val daysAgo = s.toLongOrNull()
            if (daysAgo == null) null
            else CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(daysAgo))
          }
        }

        db.createTable(EMAIL)
          .column(EMAIL_ID)
          .column(CONTENT)
          .column(FROM)
          .column(ATTACHMENT)
          .column(CREATED_AT)
          .execute()
      }.toTypedQuery { sql ->
        sql.from(EMAIL)
      }
    }
  }


  @Test
  fun testLazyInternalCondition() {
    data class TestRecord(val field1: String)
    val (db, query, queries) = setup { db ->
      val table = DSL.table(DSL.name("tbl1"))
      val field = DSL.field(DSL.name(table.name, "field1"), String::class.java)
      db.createTable(table).column(field).execute()
      val fields = DataSet.build(TestRecord::class.java) {
        field(field) { f ->
          search { s ->
            f.eq(s)
          }
        }
      }
      fields.toTypedQuery { sql ->
        sql.from(table)
      }
    }
    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "x"
      )
    )
    assertEquals(
      """
        select "tbl1"."field1"
        from "tbl1"
        where "tbl1"."field1" = 'x'
        fetch next 50 rows only
      """.trimIndent(), queries.last()
    )
  }

  @Test
  fun testFieldQualifiedNames() {
    val (db, query, queries) = setup { db ->
      val table = DSL.table(DSL.name("tbl1"))
      val field = DSL.field(DSL.name(table.name, "field1"), String::class.java)
      db.createTable(table).column(field).execute()
      val table2 = DSL.table(DSL.name("tbl2"))
      val field2 = DSL.field(DSL.name(table2.name, "field2"), String::class.java)
      // Qualify it
      db.createTable(table2).column(field2).execute()
      DataSet.forTable(
        table.join(table2).on(field.eq(field2))
      ) {
        field(field) { f ->
          search { s ->
            f.eq(s)
          }
        }
        field(field2) { f ->
          search { s ->
            f.eq(s)
          }
        }
        autoDetectFields(db)
      }

    }
    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "x"
      )
    )
    assertEquals(
      """
        select *
        from "tbl1"
          join "tbl2"
            on "tbl1"."field1" = "tbl2"."field2"
        where (
          "tbl1"."field1" = 'x'
          or "tbl2"."field2" = 'x'
        )
        fetch next 50 rows only
      """.trimIndent(), queries.last()
    )
  }

  @Test
  fun testExample() {
    val setup = setup()
    val db = setup.db
    val query = setup.query
    val queries = setup.queries

    DataSet.Response.fromRequest(db, query, DataSet.Request(search = "x"))
    assertEquals(
      """
      select
        EMAIL_ID,
        CONTENT,
        FROM_EMAIL,
        ATTACHMENT,
        CREATED_AT
      from EMAIL
      where (
        lower(CONTENT) like ('%' || replace(
          replace(
            replace(
              lower('x'),
              '!',
              '!!'
            ),
            '%',
            '!%'
          ),
          '_',
          '!_'
        ) || '%') escape '!'
        or FROM_EMAIL = 'x'
        or true
      )
      fetch next 50 rows only
      """.trimIndent(), queries.last()
    )

    DataSet.Request(
      search = "from_email:who,content:why"
    ).let { req ->
      req.toResponse(db, query).let { rsp ->
        assertEquals(
          """
          select
            EMAIL_ID,
            CONTENT,
            FROM_EMAIL,
            ATTACHMENT,
            CREATED_AT
          from EMAIL
          where (
            FROM_EMAIL = 'who'
            or lower(CONTENT) like ('%' || replace(
              replace(
                replace(
                  lower('why'),
                  '!',
                  '!!'
                ),
                '%',
                '!%'
              ),
              '_',
              '!_'
            ) || '%') escape '!'
          )
          fetch next 50 rows only
          """.trimIndent(), queries.last()
        )
      }
    }


    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "from_email:who("
      )
    )
    assertEquals(
      """
      select
        EMAIL_ID,
        CONTENT,
        FROM_EMAIL,
        ATTACHMENT,
        CREATED_AT
      from EMAIL
      where FROM_EMAIL = 'who('
      fetch next 50 rows only
      """.trimIndent(), queries.last()
    )

    val result = DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = """from_email:((, (this is parser torture""\")"""
      )
    )
    assertEquals(
      """from_email:(\(, \(this is parser torture\"\"\")""",
      result.searchRendered
    )
    assertEquals(
      """
      select
        EMAIL_ID,
        CONTENT,
        FROM_EMAIL,
        ATTACHMENT,
        CREATED_AT
      from EMAIL
      where (
        (
          FROM_EMAIL = '('
          or FROM_EMAIL = '(this'
        )
        and FROM_EMAIL = 'is'
        and FROM_EMAIL = 'parser'
        and FROM_EMAIL = 'torture""${'"'}'
      )
      fetch next 50 rows only
      """.trimIndent(), queries.last()
    )
  }

  @Test
  fun testSearching() {

    val queries = mutableListOf<String>()
    val config = DefaultConfiguration().apply {
      set(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"))
      set(SQLDialect.H2)
      set(ExecuteListener.onExecuteEnd {
        queries.add(it.query().toString())
      })
    }
    val db = DefaultDSLContext(config)
    val TABLE = DSL.table("TEST_TABLE")
    val TABLE_ID = DSL.field("ID", Int::class.java)
    val TABLE_NAME = DSL.field("NAME", String::class.java)
    val TABLE_CREATED_AT = DSL.field("CREATED_AT", LocalDateTime::class.java)
    val AUTODETECT = DSL.field("AUTO_DETECT", LocalDateTime::class.java)
    db.createTable(TABLE).column(TABLE_ID).column(TABLE_NAME).column(TABLE_CREATED_AT).column(AUTODETECT).execute()
    db.insertInto(TABLE)
      .set(TABLE_ID, 1)
      .set(TABLE_NAME, "DERP")
      .set(TABLE_CREATED_AT, LocalDateTime.now())
      .newRecord()
      .set(TABLE_ID, 2)
      .set(TABLE_NAME, "DERPY DOO")
      .set(TABLE_CREATED_AT, LocalDateTime.now().plusDays(1))
      .newRecord().set(TABLE_ID, 3)
      .set(TABLE_NAME, "HERPY")
      .set(TABLE_CREATED_AT, LocalDateTime.now().minusDays(1))
      .newRecord()
      .set(TABLE_ID, 4)
      .set(TABLE_NAME, "DERPY BOOBER 1")
      .set(TABLE_CREATED_AT, LocalDateTime.now())
      .execute()

    val query = DataSet.forTable(DSL.table("(SELECT * FROM TEST_TABLE)"), { r -> r.intoMap() }) {
      field(TABLE_ID) { field ->
        orderable()
        search {
          val num = it.toIntOrNull()
          if (num == null) {
            null
          } else {
            field.eq(num)
          }
        }
      }
      field(TABLE_NAME) { f ->
        search { f.contains(it) }
      }
      field(TABLE_CREATED_AT) { f ->
        search { s ->
          if (s.lowercase() == "today") {
            DSL.trunc(f).cast(LocalDate::class.java).eq(LocalDate.now())
          } else {
            null
          }
        }
      }
      search("testSearch") { s ->
        DSL.value("abc").eq(s)
      }
      autoDetectFields(db)
    }
    val data = query.data(db)

    val req = DataSet.Request(
      showColumns = true, showCounts = true, search = "testSearch:x", partition = "mane:DERP"
    )
    val rsp = DataSet.Response.fromRequest(db, query, req)
    val counts = rsp.count
    assertNotNull(counts)
    assertEquals(0, counts.inQuery)
    assertEquals(3, counts.inPartition)
    val columns = rsp.columns
    assertNotNull(columns)
    assertEquals(columns.size, 4)
    assertEquals("ID", columns[0].name)
    assertEquals("NAME", columns[1].name)
    assertEquals("CREATED_AT", columns[2].name)
    assertEquals("AUTO_DETECT", columns[3].name)

    val rsp2 = DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        partition = "mane:DERP", pageSize = 1
      )
    )
    assertNull(rsp2.columns)
    assertNull(rsp2.count)
    assertEquals(rsp2.data.size, 1)

    // Test searching fields
    assertEquals(0, data.search("asdf").count())
    assertEquals(4, data.search("E").count())
    assertEquals(2, data.search("OO").count())
    data.search("DERPY,HERPY").count().let {
      assertEquals(3, it)
    }

    assertEquals(3, data.search("DERPY , HERPY").count())
    assertEquals(3, data.search(" DERPY , HERPY ").count())
    assertEquals(1, data.search("DERPY DOO").count())
    assertEquals(2, data.search("1").count())

    // Test targeted search
    assertEquals(0, data.search("abc").count())
    assertEquals(0, data.search("testSearch:1").count())
    assertEquals(4, data.search("testSearch:abc").count())

    // Test targeted search on fields
    assertEquals(1, data.search("id:1").count())
    assertEquals(3, data.search("id:(1,2,3)").count())
    assertEquals(0, data.search("id:(1 3)").count())
    assertEquals(2, data.search("today").count())
    assertEquals(2, data.search("createdAt:today").count())
    assertEquals(1, data.search("createdAt:today ID:1").count())
    assertEquals(3, data.search("createdAt:today, ID:3").count())
  }
}
