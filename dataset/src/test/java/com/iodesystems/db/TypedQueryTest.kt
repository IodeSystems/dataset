package com.iodesystems.db

import com.iodesystems.db.http.DataSet
import com.iodesystems.db.query.TypedQuery
import com.iodesystems.db.search.SearchParser
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.search.model.Term
import com.iodesystems.fn.Fn
import junit.framework.TestCase.assertEquals
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
import java.time.OffsetDateTime
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
    fun testSearchParserEscapeConformance() {
        val searchParser = SearchParser()
        searchParser.parse("""\A""")
        searchParser.parse("""A\:B""")
        searchParser.parse("""\A\::(\(a,\  \,b\))""")
    }

    @Test
    @Throws(InvalidSearchStringException::class)
    fun testSearchParser() {
        val searchParser = SearchParser()
        Assert.assertEquals(Fn.list(Term("A")), searchParser.parse("A").terms)
        Assert.assertEquals(Fn.list(Term("A"), Term("B")), searchParser.parse("A B").terms)
        Assert.assertEquals(Fn.list(Term("A"), Term("B")), searchParser.parse("A B").terms)
        Assert.assertEquals(
            Fn.list(Term("A"), Term("B"), Term(Conjunction.OR, "C")), searchParser.parse("A B , C").terms
        )
        Assert.assertEquals(
            Fn.list(
                Term("A"), Term("B"), Term(Conjunction.OR, "C"), Term("target", Conjunction.AND, "Y")
            ), searchParser.parse("A B , C target:Y").terms
        )
    }

    @Test
    fun testBadSearch() {
        val result = SearchParser().parse(":")
        assertEquals(1, result.terms.size)
        assertEquals("\\:", result.terms[0].values[0].value)
    }

    @Test
    fun testExample() {

        val queries = mutableListOf<String>()
        val config = DefaultConfiguration().apply {
            set(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"))
            set(SQLDialect.H2)
            set(ExecuteListener.onExecuteEnd {
                queries.add(it.query().toString())
            })
        }

        val db = DefaultDSLContext(config)
        val EMAIL = DSL.table("EMAIL")
        val EMAIL_ID = DSL.field("EMAIL_ID", Int::class.java)
        val CONTENT = DSL.field("CONTENT", String::class.java)
        val FROM = DSL.field("FROM_", String::class.java)
        val ATTACHMENT = DSL.field("ATTACHMENT", String::class.java)
        val CREATED_AT = DSL.field("CREATED_AT", OffsetDateTime::class.java)
        db.createTable(EMAIL).column(EMAIL_ID).column(CONTENT).column(FROM).column(ATTACHMENT).column(CREATED_AT)
            .execute()

        val query = DataSet.forTable(EMAIL) {
            search("daysAgo") { s, _ ->
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
            search("is_x", open = true) { s, _ ->
                if (s == "x") DSL.trueCondition()
                else null
            }
            field(ATTACHMENT) { f ->
                search = { s ->
                    if (s.lowercase() == "true") {
                        f.isNull
                    } else {
                        null
                    }
                }
            }
            field(CONTENT) { f ->
                search = { s ->
                    f.containsIgnoreCase(s)
                }
            }
            field(FROM) { f ->
                search = { s ->
                    f.eq(s)
                }
            }
            autoDetectFields(db)
        }


        DataSet.Response.fromRequest(
            db, query, DataSet.Request(
                search = "x"
            )
        )
        assertEquals(
            """
            select *
            from EMAIL
            where (
              CONTENT ilike (('%' || replace(
                replace(
                  replace('x', '!', '!!'),
                  '%',
                  '!%'
                ),
                '_',
                '!_'
              )) || '%') escape '!'
              or FROM_ = 'x'
              or true
            )
            limit 50
            """.trimIndent(), queries.last()
        )

        DataSet.Response.fromRequest(
            db, query, DataSet.Request(
                search = "from:who,content:why"
            )
        )
        assertEquals(
            """
            select *
            from EMAIL
            where (
              FROM_ = 'who'
              or CONTENT ilike (('%' || replace(
                replace(
                  replace('why', '!', '!!'),
                  '%',
                  '!%'
                ),
                '_',
                '!_'
              )) || '%') escape '!'
            )
            limit 50
            """.trimIndent(), queries.last()
        )

        DataSet.Response.fromRequest(
            db, query, DataSet.Request(
                search = "from:who("
            )
        )
        assertEquals(
            """
            select *
            from EMAIL
            where FROM_ = 'who\('
            limit 50
            """.trimIndent(), queries.last()
        )

        val result = DataSet.Response.fromRequest(
            db, query, DataSet.Request(
                search = """
                    from:((, (this is parser torture""\")
                """.trimIndent()
            )
        )
        assertEquals(
            """from:(\(, \(this is parser torture\"\"\")""",
            result.searchRendered
        )

        assertEquals(
            """
            select *
            from EMAIL
            where (
              (
                FROM_ = '\('
                or FROM_ = '\(this'
              )
              and FROM_ = 'is'
              and FROM_ = 'parser'
              and FROM_ = 'torture\"\"\"'
            )
            limit 50
            """.trimIndent(), queries.last()
        )

        println()
    }

    @Test
    fun testSearching() {
        val db = DefaultDSLContext(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"), SQLDialect.H2)
        val TABLE = DSL.table("TEST_TABLE")
        val TABLE_ID = DSL.field("ID", Int::class.java)
        val TABLE_NAME = DSL.field("NAME", String::class.java)
        val TABLE_CREATED_AT = DSL.field("CREATED_AT", LocalDateTime::class.java)
        val AUTODETECT = DSL.field("AUTO_DETECT", LocalDateTime::class.java)
        db.createTable(TABLE).column(TABLE_ID).column(TABLE_NAME).column(TABLE_CREATED_AT).column(AUTODETECT).execute()
        db.insertInto(TABLE).set(TABLE_ID, 1).set(TABLE_NAME, "DERP").set(TABLE_CREATED_AT, LocalDateTime.now())
            .newRecord().set(TABLE_ID, 2).set(TABLE_NAME, "DERPY DOO")
            .set(TABLE_CREATED_AT, LocalDateTime.now().plusDays(1)).newRecord().set(TABLE_ID, 3)
            .set(TABLE_NAME, "HERPY").set(TABLE_CREATED_AT, LocalDateTime.now().minusDays(1)).newRecord()
            .set(TABLE_ID, 4).set(TABLE_NAME, "DERPY BOOBER 1").set(TABLE_CREATED_AT, LocalDateTime.now()).execute()

        val query = TypedQuery.forTable(DSL.table("(SELECT * FROM TEST_TABLE)"), { r -> r.intoMap() }) {
            field(TABLE_ID) { field ->
                orderable = true
                search = {
                    val num = it.toIntOrNull()
                    if (num == null) {
                        null
                    } else {
                        field.eq(num)
                    }
                }
            }
            field(TABLE_NAME) { f ->
                search = { f.contains(it) }
            }
            field(TABLE_CREATED_AT) { f ->
                search = { s ->
                    if (s.lowercase() == "today") {
                        DSL.trunc(f).cast(LocalDate::class.java).eq(LocalDate.now())
                    } else {
                        null
                    }
                }
            }
            search("testSearch") { s, _ ->
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
        assertEquals(counts.inQuery, 0)
        assertEquals(counts.inPartition, 3)
        val columns = rsp.columns
        assertNotNull(columns)
        assertEquals(columns.size, 4)
        assertEquals(columns[0].name, "id")
        assertEquals(columns[1].name, "name")
        assertEquals(columns[2].name, "createdAt")
        assertEquals(columns[3].name, "autoDetect")

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
        assertEquals(3, data.search("DERPY,HERPY").count())
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
