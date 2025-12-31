package com.iodesystems.db

import com.iodesystems.db.TestUtils.setup
import com.iodesystems.db.DataSet
import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.search.model.Term
import junit.framework.TestCase
import junit.framework.TestCase.*
import org.h2.jdbcx.JdbcConnectionPool
import org.jooq.ExecuteListener
import org.jooq.SQLDialect
import org.jooq.impl.DSL
import org.jooq.impl.DefaultConfiguration
import org.jooq.impl.DefaultDSLContext
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

class TypedQueryTest {

  @Test
  fun testGroupNegation() {
    val searchParser = SearchParser()
    val result = searchParser.parse("! ( a , b )")
    TestCase.assertEquals(1, result.terms.size)
    // Ensure negated
    TestCase.assertEquals(true, result.terms[0].negated)
  }

  @Test
  fun testLastNegation() {
    val (db, query, queries) = setup { db ->
      val table = DSL.table(DSL.name("tbl1"))
      val field = DSL.field(DSL.name(table.name, "field1"), String::class.java)
      val field2 = DSL.field(DSL.name(table.name, "field2"), String::class.java)
      db.createTable(table)
        .column(field)
        .column(field2)
        .execute()
      DataSet {
        db.select(
          field(field) {
            search { f, s ->
              f.eq(s)
            }
          },
          field(field2) {
            search { f, s ->
              f.eq(s)
            }
          }
        ).from(table)
      }
    }

    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "A !B"
      )
    ).let { result ->
      TestCase.assertEquals(
        "A !B",
        result.searchRendered
      )
      TestCase.assertEquals(
        """
        select "tbl1"."field1", "tbl1"."field2"
        from "tbl1"
        where (
          (
            "tbl1"."field1" = 'A'
            or "tbl1"."field2" = 'A'
          )
          and not (
            "tbl1"."field1" = 'B'
            or "tbl1"."field2" = 'B'
          )
        )
        fetch next 50 rows only
      """.trimIndent(),
        queries.last()
      )
    }
  }

  @Test
  fun testGroupNegationSearch() {
    val (db, query, queries) = setup { db ->
      val table = DSL.table(DSL.name("tbl1"))
      val field = DSL.field(DSL.name(table.name, "field1"), String::class.java)
      val field2 = DSL.field(DSL.name(table.name, "field2"), String::class.java)
      db.createTable(table)
        .column(field)
        .column(field2)
        .execute()
      DataSet {
        db.select(
          field(field) {
            search { f, s ->
              f.eq(s)
            }
          },
          field(field2) {
            search { f, s ->
              f.eq(s)
            }
          }
        ).from(table)
      }
    }
    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = """!(a,b c)"""
      )
    ).let { result ->
      TestCase.assertEquals(
        """!(a,b c)""",
        result.searchRendered
      )
      TestCase.assertEquals(
        """
        select "tbl1"."field1", "tbl1"."field2"
        from "tbl1"
        where not (
          (
            "tbl1"."field1" = 'a'
            or "tbl1"."field2" = 'a'
            or "tbl1"."field1" = 'b'
            or "tbl1"."field2" = 'b'
          )
          and (
            "tbl1"."field1" = 'c'
            or "tbl1"."field2" = 'c'
          )
        )
        fetch next 50 rows only
      """.trimIndent(),
        queries.last()
      )
    }

  }

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
      TestCase.assertEquals("\\!", search)
    }
    searchParser.parse("""! """).apply {
      TestCase.assertEquals("\\!", search)
    }
    searchParser.parse(""" ! """).apply {
      TestCase.assertEquals("\\!", search)
    }
    searchParser.parse(""" ! !! ! !!""").apply {
      TestCase.assertEquals("! !\\! ! !\\!", search)
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
    assertEquals("a b c", result.terms.first().values.first().value)
    assertEquals("d", result.terms.last().values.first().value)
  }

  @Test
  fun testSearchParser() {
    val searchParser = SearchParser()
    Assert.assertEquals(listOf(Term.simple("A")), searchParser.parse("A").terms)
    Assert.assertEquals(listOf(Term.simple("A"), Term.simple("B")), searchParser.parse("A B").terms)
    Assert.assertEquals(listOf(Term.simple("A"), Term.simple("B")), searchParser.parse("A B").terms)
    Assert.assertEquals(
      listOf(
        Term.simple("A"),
        Term.simple("B"),
        Term.simple("C", Conjunction.OR)
      ).toString(),
      searchParser.parse("A B , C").terms.toString()
    )
    Assert.assertEquals(
      listOf(
        Term.simple("A"),
        Term.simple("B"),
        Term.simple("C", Conjunction.OR),
        Term.simple(value = "Y", conjunction = Conjunction.AND, target = "target")
      ).toString(), searchParser.parse("A B , C target:Y").terms.toString()
    )
  }

  @Test
  fun testBadSearch() {
    val result = SearchParser().parse(":")
    TestCase.assertEquals(1, result.terms.size)
    TestCase.assertEquals(":", result.terms[0].values[0].value)
  }

  @Test
  fun testEscapeRescape() {
    val result = SearchParser().parse("Sean O'Conner")
    TestCase.assertEquals("Sean", result.terms[0].values[0].value)
    TestCase.assertEquals("O'Conner", result.terms[1].values[0].value)
    val result2 = SearchParser().parse("Sean O\\'Conner")
    TestCase.assertEquals("Sean", result2.terms[0].values[0].value)
    TestCase.assertEquals("O'Conner", result2.terms[1].values[0].value)
  }

  @Test
  fun testNegation() {

    SearchParser().parse("A a:!(!B,C)").let { result ->
      // Assert search parsed properly
      TestCase.assertEquals("A a:!(!B,C)", result.search)

      TestCase.assertEquals(true, result.terms[1].negated)
      TestCase.assertEquals(true, result.terms[1].values[0].negated)
      TestCase.assertEquals(false, result.terms[1].values[1].negated)
    }

    SearchParser().parse("A !B").let { result ->
      TestCase.assertEquals("A", result.terms[0].values[0].value)
      TestCase.assertEquals("B", result.terms[1].values[0].value)
      TestCase.assertEquals(false, result.terms[0].values[0].negated)
      // The parent term takes the negation
      TestCase.assertEquals(true, result.terms[1].negated)
      TestCase.assertEquals(false, result.terms[1].values[0].negated)
    }
  }

  @Test
  fun testNegationWithEscaping() {
    SearchParser().parse("""A:(!!a, \!b c!, !d\\!)""").apply {
      TestCase.assertEquals(1, terms.size)
      terms[0].apply {
        TestCase.assertEquals(4, values.size)
        TestCase.assertEquals("A", target)

        TestCase.assertEquals("!a", values[0].value)
        TestCase.assertEquals(true, values[0].negated)

        TestCase.assertEquals("!b", values[1].value)
        TestCase.assertEquals(false, values[1].negated)

        TestCase.assertEquals("c!", values[2].value)
        TestCase.assertEquals(false, values[2].negated)

        TestCase.assertEquals("d\\!", values[3].value)
        TestCase.assertEquals(true, values[3].negated)
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
          TestCase.assertEquals(1, it.data.size)
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
        TestCase.assertEquals(3, it.data.size)
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
      fields.toDataSet { sql ->
        sql.from(table)
      }
    }
    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "x"
      )
    )
    TestCase.assertEquals(
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
      DataSet {
        db.select(
          field(field) {
            search { f, s ->
              f.eq(s)
            }
          },
          field(field2) {
            search { f, s ->
              f.eq(s)
            }
          }
        ).from(table.join(table2).on(field.eq(field2)))
      }
    }
    DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        search = "x"
      )
    )
    TestCase.assertEquals(
      """
        select "tbl1"."field1", "tbl2"."field2"
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
    TestCase.assertEquals(
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
      req.toResponse(db, query).let {
        TestCase.assertEquals(
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
    TestCase.assertEquals(
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
    TestCase.assertEquals(
      """from_email:(\(, \(this is parser torture\"\"\")""",
      result.searchRendered
    )
    TestCase.assertEquals(
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

    val query = DataSet {
      search("testSearch", open = false) { s ->
        DSL.value("abc").eq(s)
      }
      db.select(
        field(TABLE_ID) {
          orderable()
          search { field, s ->
            val num = s.toIntOrNull()
            if (num == null) {
              null
            } else {
              field.eq(num)
            }
          }
        },
        field(TABLE_NAME) {
          search { f, s -> f.contains(s) }
        },
        field(TABLE_CREATED_AT) {
          search { f, s ->
            if (s.lowercase() == "today") {
              DSL.trunc(f).cast(LocalDate::class.java).eq(LocalDate.now())
            } else {
              null
            }
          }
        },
        field(AUTODETECT) {}
      ).from(DSL.table("(SELECT * FROM TEST_TABLE)"))
    }.mapBatch { it.map { it.intoMap() } }
    val data = query.data(db)

    val req = DataSet.Request(
      showColumns = true, showCounts = true, search = "testSearch:x", partition = "mane:DERP"
    )
    val rsp = DataSet.Response.fromRequest(db, query, req)
    val counts = rsp.count
    assertNotNull(counts!!)
    TestCase.assertEquals(0, counts.inQuery)
    TestCase.assertEquals(3, counts.inPartition)
    val columns = rsp.columns
    assertNotNull(columns!!)
    TestCase.assertEquals(columns.size, 4)
    TestCase.assertEquals("ID", columns[0].name)
    TestCase.assertEquals("NAME", columns[1].name)
    TestCase.assertEquals("CREATED_AT", columns[2].name)
    TestCase.assertEquals("AUTO_DETECT", columns[3].name)

    val rsp2 = DataSet.Response.fromRequest(
      db, query, DataSet.Request(
        partition = "mane:DERP", pageSize = 1
      )
    )
    assertNull(rsp2.columns)
    assertNull(rsp2.count)
    TestCase.assertEquals(rsp2.data.size, 1)

    // Test searching fields
    TestCase.assertEquals(0, data.search("asdf").count())
    TestCase.assertEquals(4, data.search("E").count())
    TestCase.assertEquals(2, data.search("OO").count())
    data.search("DERPY,HERPY").count().let {
      TestCase.assertEquals(3, it)
    }

    TestCase.assertEquals(3, data.search("DERPY , HERPY").count())
    TestCase.assertEquals(3, data.search(" DERPY , HERPY ").count())
    TestCase.assertEquals(1, data.search("DERPY DOO").count())
    TestCase.assertEquals(2, data.search("1").count())

    // Test targeted search
    TestCase.assertEquals(0, data.search("abc").count())
    TestCase.assertEquals(0, data.search("testSearch:1").count())
    TestCase.assertEquals(4, data.search("testSearch:abc").count())

    // Test targeted search on fields
    TestCase.assertEquals(1, data.search("id:1").count())
    TestCase.assertEquals(3, data.search("id:(1,2,3)").count())
    TestCase.assertEquals(0, data.search("id:(1 3)").count())
    TestCase.assertEquals(2, data.search("today").count())
    TestCase.assertEquals(2, data.search("createdAt:today").count())
    TestCase.assertEquals(1, data.search("createdAt:today ID:1").count())
    TestCase.assertEquals(3, data.search("createdAt:today, ID:3").count())
  }
}
