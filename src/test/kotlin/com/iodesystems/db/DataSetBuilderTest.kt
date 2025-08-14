package com.iodesystems.db

import com.iodesystems.db.TestUtils.setup
import com.iodesystems.db.http.DataSetBuilder
import com.iodesystems.db.query.TypedQuery
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select
import org.jooq.impl.DSL
import org.junit.Test
import kotlin.random.Random
import kotlin.test.assertEquals

class DataSetBuilderTest {

  data class UserDto(
    val userId: Long,
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val isActive: Boolean
  )

  object Meta {
    val USER = DSL.table("TEST_USER")
    val USER_ID = DSL.field("USER_ID", Long::class.java)
    val FIRST_NAME = DSL.field("FIRST_NAME", String::class.java)
    val LAST_NAME = DSL.field("LAST_NAME", String::class.java)
    val EMAIL = DSL.field("EMAIL", String::class.java)
    val PHONE = DSL.field("PHONE", String::class.java)
    val IS_ACTIVE = DSL.field("IS_ACTIVE", Boolean::class.java)


    val CONTACT = DSL.table("CONTACT")
    val CONTACT_ID = DSL.field("CONTACT_ID", Long::class.java)
    val USER_ID_FK = DSL.field("USER_ID_FK", Long::class.java)

    fun setup(db: DSLContext) {

      db.createTable(CONTACT)
        .column(CONTACT_ID)
        .column(USER_ID_FK)
        .execute()

      db.createTable(USER)
        .column(USER_ID)
        .column(FIRST_NAME)
        .column(LAST_NAME)
        .column(EMAIL)
        .column(PHONE)
        .column(IS_ACTIVE)
        .execute()
      val seeded = Random(0)
      fun nextString(len: Int): String {
        val sb = StringBuilder(len)
        repeat(len) {
          sb.append((seeded.nextInt(26) + 26).toChar())
        }
        return sb.toString()
      }
      for (i in 1..15) {
        db.insertInto(USER)
          .values(
            i.toLong(),
            nextString(5),
            nextString(5).uppercase(),
            "email@${nextString(10)}.com",
            "555-12${seeded.nextInt()}",
            i % 2 == 0
          )
          .execute()
      }
    }
  }

  fun <T : Record, M> subject(
    block: (db: DSLContext) -> TypedQuery<Select<T>, T, M>
  ): TestUtils.Setup<T, M, Meta> {
    return setup(Meta) { db ->
      Meta.setup(db)
      block(db)
    }
  }

  @Test
  fun testDataSetBuilderApiExists() {
    val typedQuery = subject { db ->
      DataSetBuilder.build {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE) {
            search(global = false) { f, s -> f.eq(s.toBooleanStrictOrNull()) }
          }
        ).from(Meta.USER)
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID],
          firstName = record[Meta.FIRST_NAME],
          lastName = record[Meta.LAST_NAME],
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]
        )
      }
    }
    typedQuery.query.renderSearch("userId:100").condition.toString().let {
      assertEquals(
        it, """
        (
          USER_ID = 100
          or contains(
            lower(FIRST_NAME),
            lower('100')
          )
        )
      """.trimIndent()
      )
    }
    // Test query execution
    typedQuery.query.search("userId:1").let {
      it.data(typedQuery.db).result()
      typedQuery.queries.last().let { query ->
        // Verify the query contains expected components
        assert(query.contains("USER_ID"))
        assert(query.contains("FIRST_NAME"))
        assert(query.contains("TEST_USER"))
      }
    }
  }

  @Test
  fun testDataSetBuilderWithJoins() {
    val typedQuery = subject { db ->
      val contact = Meta.CONTACT
      val contactId = Meta.CONTACT_ID
      DataSetBuilder.build {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.EMAIL) {
            search { f, s -> f.containsIgnoreCase(s) }
          }
        )
          .from(Meta.USER)
          .leftJoin(contact).on(contactId.eq(Meta.USER_ID))
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID],
          firstName = record[Meta.FIRST_NAME],
          lastName = record[Meta.LAST_NAME],
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]
        )
      }
    }
    // Verify the join query works
    typedQuery.query.renderSearch("firstName:test").condition.toString().let {
      assertEquals(
        """
        (
          USER_ID = null
          or contains(
            lower(FIRST_NAME),
            lower('test')
          )
          or contains(
            lower(EMAIL),
            lower('test')
          )
        )
      """.trimIndent(), it
      )
    }
    // Test query execution
    typedQuery.query.search("userId:2").let {
      it.data(typedQuery.db).result()
      typedQuery.queries.last().let { query ->
        assertEquals("""
          select
            USER_ID,
            FIRST_NAME,
            EMAIL
          from TEST_USER
            left outer join CONTACT
              on CONTACT_ID = USER_ID
          where (
            USER_ID = 2
            or lower(FIRST_NAME) like ('%' || replace(
              replace(
                replace(
                  lower('2'),
                  '!',
                  '!!'
                ),
                '%',
                '!%'
              ),
              '_',
              '!_'
            ) || '%') escape '!'
            or lower(EMAIL) like ('%' || replace(
              replace(
                replace(
                  lower('2'),
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
          order by FIRST_NAME asc
        """.trimIndent(),query)
      }
    }
  }

  @Test
  fun testDataSetBuilderWithWhereClause() {
    val typedQuery = subject { db ->
      DataSetBuilder.build {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE) {
            search(global = false) { f, s -> f.eq(s.toBooleanStrictOrNull()) }
          }
        )
          .from(Meta.USER)
          .where(Meta.IS_ACTIVE.eq(true))
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID],
          firstName = record[Meta.FIRST_NAME],
          lastName = record[Meta.LAST_NAME],
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]
        )
      }
    }
    // Verify the where clause works
    typedQuery.query.search("userId:5").let {
      assertEquals(
        """
        (
          USER_ID = 5
          or contains(
            lower(FIRST_NAME),
            lower('5')
          )
        )""".trimIndent(),
        DSL.and(it.conditions).toString()
      )
    }
    typedQuery.query.search("userId:5").let {
      it.data(typedQuery.db).result()
      typedQuery.queries.last().let { query ->
        assertEquals(
          """
            select
              USER_ID,
              FIRST_NAME,
              IS_ACTIVE
            from TEST_USER
            where (
              (
                USER_ID = 5
                or lower(FIRST_NAME) like ('%' || replace(
                  replace(
                    replace(
                      lower('5'),
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
              and IS_ACTIVE = true
            )
            order by FIRST_NAME asc
          """.trimIndent(), query
        )
      }
    }
  }

  @Test
  fun testDataSetBuilderWithMapping() {
    val typedQuery = subject { db ->
      DataSetBuilder.build {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.LAST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.EMAIL) {
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.PHONE) {
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE) {
            search(global = false) { f, s -> f.eq(s.toBooleanStrictOrNull()) }
          }
        ).from(Meta.USER)
      }.map { record ->
        // Custom mapping from Record to UserDto
        UserDto(
          userId = record[Meta.USER_ID] ?: 0L,
          firstName = record[Meta.FIRST_NAME] ?: "Unknown",
          lastName = record[Meta.LAST_NAME] ?: "Unknown",
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE] ?: false
        )
      }
    }
    // Verify the mapping works with search
    typedQuery.query.renderSearch("firstName:john").condition.toString().let {
      assertEquals(
        """
        (
          USER_ID = null
          or contains(
            lower(FIRST_NAME),
            lower('john')
          )
          or contains(
            lower(LAST_NAME),
            lower('john')
          )
          or contains(
            lower(EMAIL),
            lower('john')
          )
          or contains(
            lower(PHONE),
            lower('john')
          )
        )
      """.trimIndent(), it
      )
    }
    // Test query execution and mapping
    typedQuery.query.search("userId:3").let {
      it.data(typedQuery.db).result()
      typedQuery.queries.last().let { query ->
        // Verify the query contains expected components
        assert(query.contains("USER_ID"))
        assert(query.contains("FIRST_NAME"))
        assert(query.contains("LAST_NAME"))
        assert(query.contains("EMAIL"))
        assert(query.contains("PHONE"))
        assert(query.contains("IS_ACTIVE"))
        assert(query.contains("TEST_USER"))
      }
    }
  }
}
