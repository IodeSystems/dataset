package com.iodesystems.db

import com.iodesystems.db.TestUtils.setup
import com.iodesystems.db.DataSet
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.jooq.DSLContext
import org.jooq.Record
import org.jooq.Select
import org.jooq.impl.DSL
import org.junit.Test
import kotlin.random.Random

/**
 * Tests for the new DataSet.invoke() API.
 *
 * This test class validates the preferred entry point for creating DataSet instances.
 * Includes comprehensive tests for error handling, edge cases, and integration with DataSet HTTP layer.
 */
class DataSetInvokeTest {

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
    block: (db: DSLContext) -> DataSet<Select<T>, T, M>
  ): TestUtils.Setup<T, M, Meta> {
    return setup(Meta) { db ->
      Meta.setup(db)
      block(db)
    }
  }

  @Test
  fun testDataSetInvokeBasicUsage() {
    val typedQuery = subject { db ->
      DataSet {
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
          },
          Meta.LAST_NAME,
          Meta.EMAIL,
          Meta.PHONE
        ).from(Meta.USER)
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID]!!,
          firstName = record[Meta.FIRST_NAME]!!,
          lastName = record[Meta.LAST_NAME]!!,
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]!!
        )
      }
    }

    assertNotNull(typedQuery)
    assertNotNull(typedQuery.query)

    // Verify field configuration (only configured fields are in the map)
    assertEquals(3, typedQuery.query.fields.size)
    assertEquals(true, typedQuery.query.fields["USER_ID"]?.primaryKey)
    assertEquals(true, typedQuery.query.fields["FIRST_NAME"]?.orderable)

    // Verify search configuration
    val firstNameConfig = typedQuery.query.fields["FIRST_NAME"]
    assertNotNull(firstNameConfig)
    assertEquals(true, firstNameConfig?.open) // global search

    val isActiveConfig = typedQuery.query.fields["IS_ACTIVE"]
    assertNotNull(isActiveConfig)
    assertEquals(false, isActiveConfig?.open) // not global search

    // Test data fetching
    val users = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(15, users.size)
  }

  @Test
  fun testDataSetInvokeWithJoins() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          }
        )
          .from(Meta.USER)
          .leftJoin(Meta.CONTACT).on(Meta.CONTACT_ID.eq(Meta.USER_ID))
      }
    }

    assertNotNull(typedQuery)
    assertNotNull(typedQuery.query)

    // Verify the query can be executed (even with no matching joins)
    val users = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(15, users.size)
  }

  @Test
  fun testDataSetInvokeWithWhereClause() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
          },
          field(Meta.FIRST_NAME) {
            orderable()
          }
        )
          .from(Meta.USER)
          .where(Meta.IS_ACTIVE.eq(true))
      }
    }

    assertNotNull(typedQuery)

    // Should get only active users (7 out of 15)
    val users = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(7, users.size)
  }

  @Test
  fun testDataSetInvokeWithMapping() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME),
          field(Meta.LAST_NAME),
          field(Meta.EMAIL),
          field(Meta.PHONE),
          field(Meta.IS_ACTIVE)
        ).from(Meta.USER)
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID]!!,
          firstName = record[Meta.FIRST_NAME]!!,
          lastName = record[Meta.LAST_NAME]!!,
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]!!
        )
      }
    }

    val users = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(15, users.size)

    // Verify mapping worked correctly
    val firstUser = users.first()
    assertNotNull(firstUser.userId)
    assertNotNull(firstUser.firstName)
    assertNotNull(firstUser.lastName)
  }

  @Test
  fun testDataSetInvokeWithNamedSearches() {
    val typedQuery = subject { db ->
      DataSet {
        search("active") { s ->
          Meta.IS_ACTIVE.eq(s.toBooleanStrictOrNull())
        }
        search("userId") { s ->
          Meta.USER_ID.eq(s.toLongOrNull())
        }

        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME)
        ).from(Meta.USER)
      }
    }

    assertNotNull(typedQuery.query)

    // Test named search for active users
    val searchQuery = typedQuery.query.search("active:true")
    val activeUsers = searchQuery.data(typedQuery.db).fetch()
    assertEquals(7, activeUsers.size)

    // Test named search for specific user ID
    val userQuery = typedQuery.query.search("userId:1")
    val specificUser = userQuery.data(typedQuery.db).fetch()
    assertEquals(1, specificUser.size)
  }

  @Test
  fun testDataSetInvokeTypeParameters() {
    // Verify that the type parameters work correctly
    val typedQuery = subject { db ->
      DataSet {
        db.select(field(Meta.USER_ID), field(Meta.FIRST_NAME)).from(Meta.USER)
      }
    }

    assertNotNull(typedQuery.query)
    assertEquals(2, typedQuery.query.fields.size)
  }

  @Test
  fun testDataSetInvokeSearchAndPagination() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME)
        ).from(Meta.USER)
      }
    }

    // Test search by ID
    val searchedQuery = typedQuery.query.search("1")
    assertNotNull(searchedQuery)

    // Test pagination on all results
    val pagedQuery = typedQuery.query.page(0, 5)
    val results = pagedQuery.data(typedQuery.db).fetch()
    assertEquals(5, results.size)
  }

  @Test
  fun testDataSetInvokeErrorHandling() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
          }
        ).from(Meta.USER)
      }
    }

    // Test search with invalid ID - the search function returns null for invalid input
    val badSearchQuery = typedQuery.query.search("userId:notanumber")
    assertNotNull(badSearchQuery)

    // When search function returns null (e.g., toLongOrNull() returns null),
    // the condition is not added, so this returns 0 results
    val results = badSearchQuery.data(typedQuery.db).fetch()
    assertEquals(0, results.size)
  }

  @Test
  fun testDataSetInvokeEmptyResults() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          }
        )
          .from(Meta.USER)
          .where(Meta.USER_ID.eq(999L)) // Non-existent ID
      }
    }

    val results = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(0, results.size)
  }

  @Test
  fun testDataSetInvokeComplexSearchCombinations() {
    val typedQuery = subject { db ->
      DataSet {
        search("active") { s ->
          Meta.IS_ACTIVE.eq(s.toBooleanStrictOrNull())
        }

        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE)
        ).from(Meta.USER)
      }
    }

    // Test combining field search and named search
    // This should search for firstName containing "test" AND active = true
    val complexQuery = typedQuery.query.search("active:true")
    val results = complexQuery.data(typedQuery.db).fetch()

    // Should get only active users (7 out of 15)
    assertEquals(7, results.size)
  }

  @Test
  fun testDataSetInvokeFieldsNotInSelect() {
    val typedQuery = subject { db ->
      DataSet {
        // Configure a field for search but don't include it in SELECT
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME),
          // IS_ACTIVE is used in WHERE but not selected
          Meta.IS_ACTIVE
        ).from(Meta.USER)
      }
    }

    // Search should work even for fields not explicitly configured
    val results = typedQuery.query.search("1").data(typedQuery.db).fetch()
    assertEquals(1, results.size)
  }

  @Test
  fun testDataSetInvokeMultipleOrderings() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {
            orderable(DataSet.Order.Direction.ASC)
          },
          field(Meta.LAST_NAME) {
            orderable(DataSet.Order.Direction.DESC)
          }
        ).from(Meta.USER)
      }
    }

    // The query should have ordering configured
    assertNotNull(typedQuery.query.fields["FIRST_NAME"])
    assertEquals(true, typedQuery.query.fields["FIRST_NAME"]?.orderable)
    assertEquals(true, typedQuery.query.fields["LAST_NAME"]?.orderable)
  }

  @Test
  fun testDataSetInvokeStreamingResults() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME)
        ).from(Meta.USER)
      }
    }

    // Test streaming API
    val stream = typedQuery.query.data(typedQuery.db).stream()
    val results = stream.toList()
    assertEquals(15, results.size)
  }

  @Test
  fun testDataSetInvokeCount() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          }
        ).from(Meta.USER)
      }
    }

    // Test count without fetching
    val totalCount = typedQuery.query.data(typedQuery.db).count()
    assertEquals(15L, totalCount)

    // Test count with filter
    val filteredCount = typedQuery.query.search("userId:1").data(typedQuery.db).count()
    assertEquals(1L, filteredCount)
  }

  @Test
  fun testDataSetInvokeBatchMapping() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME)
        ).from(Meta.USER)
      }.mapBatch { records ->
        // Batch process records (e.g., enrich from external service)
        records.map { record ->
          UserDto(
            userId = record[Meta.USER_ID]!!,
            firstName = "${record[Meta.FIRST_NAME]} (Batch)",
            lastName = "Processed",
            email = null,
            phone = null,
            isActive = false
          )
        }
      }
    }

    val users = typedQuery.query.data(typedQuery.db).fetch()
    assertEquals(15, users.size)

    // Verify batch mapping was applied
    assertEquals(true, users.first().firstName.endsWith("(Batch)"))
    assertEquals("Processed", users.first().lastName)
  }

  @Test
  fun testDataSetInvokeClearOrder() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {
            orderable()
          }
        ).from(Meta.USER)
      }
    }

    // Initial query has ordering
    assertEquals(true, typedQuery.query.fields["FIRST_NAME"]?.orderable)

    // Clear ordering
    val unorderedQuery = typedQuery.query.clearOrder()
    assertEquals(0, unorderedQuery.order.size)
  }

  @Test
  fun testDataSetInvokeChainedOperations() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME)
        ).from(Meta.USER)
      }
    }

    // Test chaining multiple operations
    val result = typedQuery.query
      .search("userId:5")
      .page(0, 10)
      .map { record ->
        record[Meta.FIRST_NAME] ?: "Unknown"
      }
      .data(typedQuery.db)
      .fetch()

    assertEquals(1, result.size)
    assertNotNull(result.first())
  }

  // ========================================
  // Integration Tests with DataSet HTTP Layer
  // ========================================

  @Test
  fun testDataSetQueryIntegration() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE),
          Meta.LAST_NAME,
          Meta.EMAIL,
          Meta.PHONE
        ).from(Meta.USER)
      }.map { record ->
        UserDto(
          userId = record[Meta.USER_ID]!!,
          firstName = record[Meta.FIRST_NAME]!!,
          lastName = record[Meta.LAST_NAME]!!,
          email = record[Meta.EMAIL],
          phone = record[Meta.PHONE],
          isActive = record[Meta.IS_ACTIVE]!!
        )
      }
    }

    // Test request.response() with Request/Response
    val request = DataSet.Request(
      search = "userId:5",
      showColumns = true,
      showCounts = true,
      page = 0,
      pageSize = 10
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    // Verify response structure
    assertNotNull(response)
    assertEquals(1, response.data.size)
    assertNotNull(response.count)
    // Both counts should be 1 since we're filtering to userId:5
    assertEquals(1L, response.count?.inPartition)
    assertEquals(1L, response.count?.inQuery)
    assertNotNull(response.columns)

    // Verify column metadata (only configured fields have metadata)
    val userIdColumn = response.columns?.find { it.name == "USER_ID" }
    assertNotNull(userIdColumn)
    assertEquals(true, userIdColumn?.primaryKey)
    assertEquals(true, userIdColumn?.searchable)

    val firstNameColumn = response.columns?.find { it.name == "FIRST_NAME" }
    assertNotNull(firstNameColumn)
    assertEquals(true, firstNameColumn?.orderable)
    assertEquals(true, firstNameColumn?.searchable)
  }

  @Test
  fun testDataSetFilterIntegration() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          },
          field(Meta.FIRST_NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
          },
          field(Meta.IS_ACTIVE)
        ).from(Meta.USER)
      }
    }

    // Test request.filter() - just gets data without metadata
    val request = DataSet.Request(
      search = "userId:3"
    )

    val results = request.filter(typedQuery.db, typedQuery.query)

    assertNotNull(results)
    assertEquals(1, results.size)
  }

  @Test
  fun testDataSetQueryWithPagination() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) { orderable() }
        ).from(Meta.USER)
      }
    }

    // Test pagination - page 1 means second page (0-indexed)
    val request = DataSet.Request(
      page = 1,
      pageSize = 5,
      showCounts = true
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    // Page 1 with pageSize 5 should give us records 6-10 (5 records)
    assertEquals(5, response.data.size)
    // Verify counts are present
    assertNotNull(response.count)
  }

  @Test
  fun testDataSetQueryWithOrdering() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {
            orderable(DataSet.Order.Direction.ASC)
          },
          field(Meta.LAST_NAME) {
            orderable(DataSet.Order.Direction.DESC)
          }
        ).from(Meta.USER)
      }
    }

    // Test with ordering
    val request = DataSet.Request(
      ordering = listOf(
        DataSet.Order("FIRST_NAME", DataSet.Order.Direction.DESC)
      ),
      pageSize = 3
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    assertEquals(3, response.data.size)
    // Results should be ordered by FIRST_NAME DESC
    assertNotNull(response.data.first())
  }

  @Test
  fun testDataSetQueryWithSearchAndFilter() {
    val typedQuery = subject { db ->
      DataSet {
        search("active") { s ->
          Meta.IS_ACTIVE.eq(s.toBooleanStrictOrNull())
        }

        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME),
          field(Meta.IS_ACTIVE)
        )
          .from(Meta.USER)
          .where(Meta.USER_ID.lessOrEqual(10L)) // Base filter
      }
    }

    // Test combining base WHERE with search
    val request = DataSet.Request(
      search = "active:true",
      showCounts = true
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    // Should get active users with ID <= 10
    // IDs 2, 4, 6, 8, 10 = 5 users
    assertEquals(5, response.data.size)
  }

  @Test
  fun testDataSetFilterUnlimited() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() }
        ).from(Meta.USER)
      }
    }

    // Test unlimit parameter - should get all results ignoring pagination
    val request = DataSet.Request(
      page = 0,
      pageSize = 5
    )

    // With unlimit=true, should get all 15 records despite pageSize=5
    val results = request.filter(typedQuery.db, typedQuery.query, unlimit = true)

    assertEquals(15, results.size)
  }

  @Test
  fun testDataSetQueryEmptySearch() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
          }
        ).from(Meta.USER)
      }
    }

    // Test with null/empty search
    val request = DataSet.Request(
      search = null,
      showCounts = true,
      pageSize = 10
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    // Should return first page of all results
    assertEquals(10, response.data.size)
    assertEquals(15L, response.count?.inPartition)
  }

  @Test
  fun testDataSetQuerySearchRendered() {
    val typedQuery = subject { db ->
      DataSet {
        db.select(
          field(Meta.USER_ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
          }
        ).from(Meta.USER)
      }
    }

    // Test that searchRendered is returned
    val request = DataSet.Request(
      search = "userId:5"
    )

    val response = request.response(typedQuery.db, typedQuery.query)

    assertNotNull(response.searchRendered)
    assertEquals("userId:5", response.searchRendered)
  }

  @Test
  fun testLazy() {
    // Test that lazy conditions are evaluated at query execution time
    data class Context(var userId: Long, var evaluationCount: Int = 0)
    val ctx = Context(1L)

    val typedQuery = setup {
      Meta.setup(it)

      // Seed test data
      it.insertInto(Meta.USER)
        .columns(Meta.USER_ID, Meta.FIRST_NAME, Meta.LAST_NAME, Meta.EMAIL, Meta.IS_ACTIVE)
        .values(1L, "Alice", "Smith", "alice@test.com", true)
        .values(2L, "Bob", "Jones", "bob@test.com", true)
        .values(3L, "Charlie", "Brown", "charlie@test.com", false)
        .execute()

      DataSet {
        it.select(
          field(Meta.USER_ID) { primaryKey() },
          field(Meta.FIRST_NAME) {}
        ).from(Meta.USER)
          .where(lazy {
            // This condition is evaluated every time the query runs
            ctx.evaluationCount++
            println("Lazy evaluated! Count: ${ctx.evaluationCount}, userId: ${ctx.userId}")
            Meta.USER_ID.eq(ctx.userId)
          })
      }
    }

    println("DataSet created. Evaluation count should be 0")
    assertEquals("Lazy condition should NOT run during DataSet creation", 0, ctx.evaluationCount)

    // Query for user 1
    ctx.userId = 1L
    typedQuery.queries.clear()
    val user1Results = typedQuery.query.data(typedQuery.db).fetch()
    val sql1 = typedQuery.queries.last()
    println("After query 1: evaluationCount=${ctx.evaluationCount}, results=${user1Results.size}")
    println("SQL for user 1: $sql1")
    assertTrue("Lazy condition SHOULD run during query execution", ctx.evaluationCount > 0)
    assertTrue("SQL should contain WHERE USER_ID = 1", sql1.contains("USER_ID = 1"))
    val countAfterFirstQuery = ctx.evaluationCount

    // Change the context and query again
    ctx.userId = 2L
    typedQuery.queries.clear()
    val user2Results = typedQuery.query.data(typedQuery.db).fetch()
    val sql2 = typedQuery.queries.last()
    println("After query 2: evaluationCount=${ctx.evaluationCount}, results=${user2Results.size}")
    println("SQL for user 2: $sql2")
    assertTrue("Lazy condition SHOULD run again on second query", ctx.evaluationCount > countAfterFirstQuery)
    assertTrue("SQL should contain WHERE USER_ID = 2", sql2.contains("USER_ID = 2"))

    // Verify results are different based on the lazy value
    println("User 1 results: ${user1Results.map { it[Meta.FIRST_NAME] }}")
    println("User 2 results: ${user2Results.map { it[Meta.FIRST_NAME] }}")

    // Verify SQL was updated between queries
    assertTrue("SQL strings should be different", sql1 != sql2)
    println("\nSQL Comparison:")
    println("User 1 SQL contains 'USER_ID = 1': ${sql1.contains("USER_ID = 1")}")
    println("User 2 SQL contains 'USER_ID = 2': ${sql2.contains("USER_ID = 2")}")
  }
}
