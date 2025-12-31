# DataSet Library - Claude Context

## Project Overview

**DataSet** is a Kotlin library that provides a type-safe, DSL-based query builder for converting freeform user queries into JOOQ SQL conditions. It emphasizes "least surprise" behavior and declarative configuration.

**Repository:** iODESystems/dataset
**Current Version:** 9.0.0-SNAPSHOT
**Target JVM:** Java 21
**License:** MIT
**Key Dependencies:** JOOQ, ANTLR4, Kotlin stdlib

---

## Project Purpose

The library solves the problem of building flexible, searchable database queries with:
- Type-safe field configuration
- Declarative search targeting
- Natural language-style query parsing (via ANTLR4)
- Automatic pagination and ordering
- HTTP request integration for REST APIs

**Example Use Case:**
A user types `"john@email.com status:active created:<30daysAgo"` in a search box, and the library converts this into SQL:
```sql
WHERE email LIKE '%john@email.com%'
  AND status = 'active'
  AND created_at > NOW() - INTERVAL 30 DAY
```

---

## Current Architecture

### Core Components

**DataSet** (`src/main/kotlin/com/iodesystems/db/DataSet.kt`)
- Core data class representing a configured query
- Holds table, mapper, conditions, ordering, field configurations
- Immutable with copy-based mutation methods
- **Primary API entry point:** `DataSet { }` operator function

**DataSetContext** (`src/main/kotlin/com/iodesystems/db/DataSet.kt`)
- DSL context for building DataSet instances
- Provides `field()` and `search()` functions for configuration
- Used internally by `DataSet { }` operator

**SearchParser** (`src/main/kotlin/com/iodesystems/db/search/SearchParser.kt`)
- ANTLR4-based parser for freeform search queries
- Supports: quoted strings, AND/OR logic, grouping, negation, field targeting
- Converts natural language searches to structured Term lists

**Request.response() and Request.filter()** (`src/main/kotlin/com/iodesystems/db/DataSet.kt`)
- HTTP integration layer (Request methods)
- Converts Request objects (with search, ordering, pagination) into database queries
- Returns Response objects with data, counts, and column metadata

**Fields** (`src/main/kotlin/com/iodesystems/db/query/Fields.kt`)
- Legacy builder API (still supported for backward compatibility)
- Used by some existing code but not recommended for new code

### Directory Structure

```
src/main/kotlin/com/iodesystems/db/
├── DataSet.kt                   # Core data class + primary API entry point
├── query/
│   └── Fields.kt                # Legacy field configuration builder
├── search/
│   ├── SearchParser.kt          # ANTLR4 search parser
│   ├── SearchConditionFactory.kt # Converts parsed search to SQL conditions
│   ├── model/
│   │   ├── Term.kt              # Search term model
│   │   ├── TermValue.kt         # Search term value model
│   │   └── Conjunction.kt       # AND/OR conjunction type
│   └── errors/
│       ├── InvalidSearchStringException.kt
│       └── SneakyInvalidSearchStringException.kt
├── jooq/
│   └── LazySql.kt               # Lazy SQL evaluation
└── util/
    ├── StringUtil.kt            # String manipulation helpers
    └── Debug.kt                 # Debug logging utilities

src/main/antlr4/
├── DataSetSearchLexer.g4        # Search query lexer grammar
└── DataSetSearchParser.g4       # Search query parser grammar

src/test/kotlin/com/iodesystems/db/
├── TypedQueryTest.kt            # Query parsing and execution tests
├── DataSetBuilderTest.kt        # Builder API tests (v9 API)
├── TypedQueryInvokeTest.kt      # DataSet invoke API tests
└── TestUtils.kt                 # Test infrastructure (H2, mocks)
```

---

## v9.0 Refactoring - COMPLETED ✅

### What Changed

The v9.0 refactoring consolidated multiple API patterns into a single, unified entry point.

**Removed:**
- `DataSetBuilder.build()` - Was the preferred v8.x pattern
- `DataSet.forTable()` - Legacy HTTP package wrapper
- `TypedQuery.forTable()` - Never existed, was planned but not implemented
- `Fields.toTypedQuery()` - Legacy fluent builder pattern (still exists via `Fields.toDataSet()`)

**New Primary API:**
```kotlin
DataSet {
  db.select(
    field(USER_ID) {
      primaryKey()
      search { f, s -> f.eq(s.toLongOrNull()) }
    },
    field(FIRST_NAME) {
      orderable()
      search { f, s -> f.containsIgnoreCase(s) }
    }
  ).from(USER)
}
```

**Implementation:**
- Added `operator fun invoke()` to DataSet companion object
- Created `DataSetContext` class for DSL building
- Migrated all builder logic into DataSet companion
- Kept `Fields` class for backward compatibility but not recommended

### Code Cleanup Completed

**Removed/Deprecated:**
- `Request.toResponse()` method - Deprecated in favor of `DataSet.Response.fromRequest()`
- Old builder patterns consolidated

**Still Present (for compatibility):**
- `LazySql` - Uses deprecated JOOQ APIs but still functional
- StringUtil extensions - All still in use

### Test Migration Completed ✅

**Current state:**
- TypedQueryTest.kt: 17 tests using v9.0 API (`DataSet { }`)
- DataSetBuilderTest.kt: 5 tests using v9.0 API
- TypedQueryInvokeTest.kt: 26 comprehensive integration tests
- All tests passing with `BUILD SUCCESSFUL`

**Coverage:**
- ✅ All public API methods tested
- ✅ All search parser features tested
- ✅ Error handling and edge cases
- ✅ Complex joins and field qualification
- ✅ Integration tests with request.response() and request.filter()
- ✅ HTTP Request/Response integration

### Documentation Updated ✅

- ✅ README.md updated with v9.0 API examples
- ✅ Migration guide added to README
- ✅ CLAUDE.md updated to reflect completed refactoring
- ✅ All examples use new `DataSet { }` syntax

---

## Key Design Decisions

### Why Keep Type Parameters?

DataSet maintains three type parameters:
```kotlin
DataSet<T : Select<R>, R : Record, M>
```

- **T**: The JOOQ Select type (preserves query builder type)
- **R**: The Record type (what JOOQ returns from DB)
- **M**: The mapped type (what the user gets back)

This allows:
- Type-safe field configuration
- Flexible mapping (Record -> DTO, Record -> Map, etc.)
- Compile-time verification of field types

### Why Immutable with Copy-Based Mutation?

```kotlin
fun search(query: String): DataSet<T, R, M> = copy(...)
```

Benefits:
- Thread-safe
- Easier to reason about
- Supports functional composition
- Aligns with Kotlin best practices

### Why ANTLR4 for Search Parsing?

The search grammar is complex:
- Quoted strings with escape sequences
- AND/OR logic (space vs comma)
- Grouping with parentheses
- Field targeting with colons (`field:"value"`)
- Negation with `!`

ANTLR4 provides:
- Robust parsing with error recovery
- Clear grammar definition
- Maintainable parsing logic
- Better error messages

---

## Common Development Tasks

### Adding a New Field Configuration Option

1. Add property to `FieldConfiguration` data class (DataSet.kt)
2. Add builder method to `FieldConfiguration.Builder` (DataSet.kt)
3. Add builder method to `DataSetFieldConfigBuilder` (DataSet.kt)
4. Update `DataSetContext.buildDataSet()` to handle new property
5. Add test in TypedQueryInvokeTest.kt or DataSetBuilderTest.kt

### Adding a New Search Feature

1. Update ANTLR4 grammar files (DataSetSearchLexer.g4, DataSetSearchParser.g4)
2. Run Gradle task to regenerate parser: `./gradlew generateLexer generateParser`
3. Update `SearchParser.parse()` to handle new syntax
4. Update `SearchConditionFactory.search()` to generate conditions
5. Add tests in TypedQueryTest.kt

### Running Tests

```bash
./gradlew test
```

Tests use H2 in-memory database, no external setup required.

### Building the Project

```bash
./gradlew build
```

### Publishing a Release

```bash
./bin/release <version>
```

See release-conventions.gradle.kts for publishing configuration.

---

## Migration Guide (v8.x -> v9.0)

### Before (v8.x)

**Old Pattern 1: DataSetBuilder.build (was preferred in v8.x)**
```kotlin
DataSetBuilder.build {
  db.select(
    field(USER_ID) { primaryKey() }
  ).from(USER)
}
```

**Old Pattern 2: Fields.toDataSet**
```kotlin
val fields = DataSet.build {
  field(USER_ID)
  field(EMAIL) { f ->
    search { s -> f.containsIgnoreCase(s) }
  }
}
val query = fields.toDataSet { sql -> sql.from(USER) }
```

### After (v9.0)

**New Pattern: DataSet { }**
```kotlin
DataSet {
  db.select(
    field(USER_ID) {
      primaryKey()
      search { f, s -> f.eq(s.toLongOrNull()) }
    },
    field(EMAIL) {
      search { f, s -> f.containsIgnoreCase(s) }
    }
  ).from(USER)
}
```

**Key Changes:**
1. Use `DataSet { ... }` instead of `DataSetBuilder.build { ... }`
2. Use `DataSet { ... }` instead of `Fields.toDataSet { ... }`
3. Field configuration lambda now receives both field and search string: `search { f, s -> ... }`
4. Named searches are defined before the select: `search("name") { ... }`
5. HTTP methods: Use `request.response()` and `request.filter()` instead of companion methods
6. Request.query() renamed to Request.response()

---

## Coding Conventions

### Package Organization

- `http/` - HTTP integration, request/response models
- `query/` - Core query building logic
- `search/` - Search parsing and condition generation
- `jooq/` - JOOQ-specific utilities and extensions
- `util/` - General-purpose utilities

### Naming Conventions

- Use camelCase for variables and functions
- Use PascalCase for classes and objects
- Use snake_case for database column names (converted via StringUtil)
- Field configuration properties: descriptive nouns (`orderable`, `primaryKey`)
- Builder methods: verbs or property setters (`orderable()`, `search { }`)

### DSL Design Principles

1. **Receiver lambdas** for scoped configuration:
   ```kotlin
   field(FIELD_A) {  // 'this' is FieldConfigBuilder
     orderable()
   }
   ```

2. **Fluent chaining** for query modification:
   ```kotlin
   query.search("text").page(1, 50).order("created", DESC)
   ```

3. **Declarative over imperative:**
   ```kotlin
   field(USER_ID) { primaryKey() }  // Good
   field(USER_ID).apply { primaryKey = true }  // Avoid
   ```

---

## Testing Strategy

### Unit Tests
- **TypedQueryTest.kt**: Search parsing, condition generation, query building
- **DataSetBuilderTest.kt**: Builder API, field configuration, mapping

### Integration Tests
- Use H2 in-memory database
- Test full request-to-response pipeline
- Verify SQL generation matches expectations

### Test Utilities
- **TestUtils.kt**: Provides `setup()` function
- Captures executed SQL for verification
- Provides `SimpleMeta` for standard test schema

### Test Coverage Goals
- All public API methods
- All search parser features
- Error handling and edge cases
- Complex joins and subqueries
- Field qualification in multi-table queries

---

## Known Issues and Limitations

### 1. LazySql Uses Deprecated JOOQ APIs
**File:** `src/main/kotlin/com/iodesystems/db/jooq/LazySql.kt`
**Status:** Still functional but relies on `@Deprecated` JOOQ interfaces
**Impact:** May break in future JOOQ versions
**Action:** Monitor JOOQ releases, consider alternative implementation

### 2. JOOQ Type Parameters Can Be Complex
**Issue:** JOOQ returns specific Record types (Record3, Record5, etc.) not generic Record
**Status:** Working as intended, but can cause type compatibility issues
**Impact:** May need type casts when using generic Setup or test utilities
**Workaround:** Use `.mapBatch { it.map { record -> record.intoMap() } }` or explicit type casts

### 3. Limited Support for Subqueries
**Status:** Works but not extensively tested
**Impact:** May have edge cases with field qualification
**Action:** Add comprehensive subquery tests

### 4. Legacy Fields API Still Present
**Status:** `Fields` class and `DataSet.build()` methods still exist for backward compatibility
**Impact:** Maintenance burden, potential confusion for new users
**Recommendation:** Use `DataSet { }` for all new code

---

## Future Enhancements (Post-v9.0)

### Potential Features
1. **GraphQL integration** - Convert GraphQL queries to DataSet queries
2. **Aggregation support** - Built-in COUNT, SUM, AVG, etc.
3. **Computed fields** - Virtual fields calculated from multiple columns
4. **Field-level permissions** - Hide/show fields based on user roles
5. **Query caching** - Cache parsed search queries
6. **Bulk operations** - UPDATE/DELETE support with search syntax
7. **Export formats** - CSV, JSON, Excel export from query results

### API Improvements
1. **Coroutine support** - Async/suspend query execution
2. **Streaming API** - Enhanced large result set streaming with backpressure
3. **Query explain** - Debug what SQL will be generated without executing
4. **Field validators** - Type-safe validation rules on field configuration
5. **Auto-detect fields v2** - Better field detection without requiring database connection

---

## Dependencies and Versions

### Core Dependencies
```kotlin
org.jooq:jooq:3.19.x           // SQL DSL and query generation
org.antlr:antlr4:4.13.x        // Search query parsing
org.jetbrains.kotlin:kotlin-stdlib:2.1.x
```

### Test Dependencies
```kotlin
com.h2database:h2:2.x          // In-memory test database
org.junit.jupiter:junit-jupiter:5.x
```

### Build Plugins
```kotlin
org.jetbrains.kotlin.jvm:2.1.x
com.gradle.plugin-publish:1.x
org.jlleitschuh.gradle.ktlint:12.x
```

---

## Release Process

### Version Numbering
- **MAJOR**: Breaking API changes (e.g., 8.x -> 9.0)
- **MINOR**: New features, backward compatible (e.g., 8.0 -> 8.1)
- **PATCH**: Bug fixes, backward compatible (e.g., 8.0.9 -> 8.0.10)

### Release Steps
1. Update version in `build.gradle.kts`
2. Update CHANGELOG.md
3. Run `./gradlew test` (ensure all tests pass)
4. Run `./gradlew build`
5. Run `./bin/release <version>`
6. Create GitHub release with tag
7. Publish to Maven Central

### Release Script
```bash
./bin/release 9.0.0
```

Handles:
- Version bumping
- Git tagging
- Publishing to Maven Central

---

## Resources

### Documentation
- README.md - Quick start and usage examples
- PROJECT_ANALYSIS.md - Detailed architecture analysis
- CHANGELOG.md - Version history and breaking changes

### External Resources
- JOOQ Documentation: https://www.jooq.org/doc/
- ANTLR4 Documentation: https://github.com/antlr/antlr4/blob/master/doc/index.md
- Kotlin DSL Guide: https://kotlinlang.org/docs/type-safe-builders.html

### Related Projects
- iODESystems organization: https://github.com/iodesystems

---

## Quick Reference

### Create a DataSet (v9.0)
```kotlin
val query = DataSet {
  search("daysAgo") { s ->
    // Custom named search
    s.toLongOrNull()?.let {
      CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(it))
    }
  }

  db.select(
    field(USER_ID) {
      primaryKey()
      search { f, s -> f.eq(s.toLongOrNull()) }
    },
    field(EMAIL) {
      orderable()
      search { f, s -> f.containsIgnoreCase(s) }
    }
  ).from(USER)
}.map { record ->
  UserDto(
    id = record[USER_ID],
    email = record[EMAIL]
  )
}
```

### Execute a Query

**Programmatic API:**
```kotlin
val data = query
  .search("john@email.com")
  .page(page = 0, pageSize = 50)
  .data(db)
  .fetch()
```

**HTTP Request API:**
```kotlin
val db: DSLContext = // ... JOOQ context
val request = DataSet.Request(
  search = "john@email.com",
  page = 0,
  pageSize = 50,
  showColumns = true,
  showCounts = true
)

val response = request.response(db, query)
// response.data: List<UserDto>
// response.count: Count?
// response.columns: List<Column>?
```

### Search Syntax Examples
```
Simple:             john@email.com
AND logic:          john status:active
OR logic:           john, jane
Negation:           !inactive
Grouping:           (john, jane) status:active
Quoted:             "john doe"
Escaped:            "quote: \"value\""
```

### Field Configuration Options
```kotlin
field(FIELD) {
  primaryKey()                           // Mark as primary key
  orderable()                            // Allow ordering by this field
  orderable(Direction.DESC)              // Default sort direction
  search { f, s -> f.eq(s) }            // Searchable, global (open search)
  search(global = false) { f, s -> ... } // Searchable, targeted only
}
```

### Named Search Configuration

Named searches are custom search functions not tied to a specific field. Perfect for date ranges, price ranges, and complex logic.

**Basic Example:**
```kotlin
DataSet {
  search("daysAgo") { s ->
    when {
      s.startsWith("<") -> {
        s.drop(1).toLongOrNull()?.let { days ->
          CREATED_AT.lessOrEqual(OffsetDateTime.now().minusDays(days))
        }
      }
      s.startsWith(">") -> {
        s.drop(1).toLongOrNull()?.let { days ->
          CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(days))
        }
      }
      else -> {
        s.toLongOrNull()?.let { days ->
          CREATED_AT.greaterOrEqual(OffsetDateTime.now().minusDays(days))
        }
      }
    }
  }

  db.select(
    field(ID) { primaryKey() },
    field(NAME) { search { f, s -> f.containsIgnoreCase(s) } },
    CREATED_AT
  ).from(PRODUCT)
}

// Usage:
query.search("daysAgo:<30")   // Created less than 30 days ago
query.search("daysAgo:>90")   // Created more than 90 days ago
```

**Advanced Examples:**
```kotlin
DataSet {
  // Price range search
  search("price") { s ->
    when {
      s.startsWith(">=") -> s.drop(2).toBigDecimalOrNull()?.let { PRICE.greaterOrEqual(it) }
      s.startsWith("<=") -> s.drop(2).toBigDecimalOrNull()?.let { PRICE.lessOrEqual(it) }
      s.contains("..") -> {
        val parts = s.split("..")
        if (parts.size == 2) {
          val min = parts[0].toBigDecimalOrNull()
          val max = parts[1].toBigDecimalOrNull()
          if (min != null && max != null) PRICE.between(min, max) else null
        } else null
      }
      else -> s.toBigDecimalOrNull()?.let { PRICE.eq(it) }
    }
  }

  // Status flag search (targeted only)
  search("active", open = false) { s ->
    when (s.lowercase()) {
      "true", "yes" -> STATUS.eq("ACTIVE")
      "false", "no" -> STATUS.eq("INACTIVE")
      else -> null
    }
  }

  db.select(...).from(...)
}

// Usage:
query.search("price:>=100")      // $100 or more
query.search("price:10..99")     // Between $10 and $99
query.search("active:true")      // Active only
```

**Parameters:**
- `name`: Search target name
- `open = true`: Participates in global search; `false`: targeted only
- Return `null` to ignore unmatched strings

### Lazy SQL (Runtime Conditions)

For conditions that must be evaluated **at query execution time** (not at DataSet creation):

**Use Case:** Multi-tenant apps, currentUserId, request-scoped permissions

```kotlin
val query = DataSet {
  db.select(
    field(USER.ID) { primaryKey() }
  ).from(USER)
  .where(lazy {
    USER.TENANT_ID.eq(RequestContext.currentTenantId())
  })
}
```

**Static vs Lazy:**
- Static: `.where(USER.TENANT_ID.eq(123))` - evaluated once at DataSet creation
- Lazy: `.where(lazy { USER.TENANT_ID.eq(getCurrentTenantId()) })` - evaluated each query execution

**Common patterns:**
- Tenant isolation: `.where(lazy { TENANT_ID.eq(context.tenantId) })`
- User permissions: `.where(lazy { OWNER_ID.eq(context.userId) })`
- Complex logic: `.where(lazy { if (user.isAdmin) DSL.trueCondition() else OWNER_ID.eq(user.id) })`

**Why lazy SQL?**
- Discoverable in DataSet DSL (no imports needed)
- Type-safe and clearer intent
- Flexible - supports any JOOQ Condition

**Note:** Uses deprecated JOOQ APIs (still works, monitored for breaking changes)

### HTTP Integration: `filter()` vs `response()`

DataSet.Request provides two methods for different use cases:

**`request.filter()` - Data Only**
```kotlin
// Returns: List<T>
val data = request.filter(db, query)

// With unlimit for bulk operations
val allData = request.filter(db, query, unlimit = true)
```

- ✅ Returns data only
- ❌ No counts, columns, searchRendered
- ✅ Has `unlimit` parameter
- **Use for:** Bulk operations, exports, simple lists

**`request.response()` - Full Response**
```kotlin
// Returns: Response<T> with data, counts, columns, searchRendered
val response = request.response(db, query)
```

- ✅ Returns data, counts (optional), columns (optional), searchRendered
- ❌ No unlimit parameter
- **Use for:** UI tables, paginated lists, search interfaces

**Comparison:**

| Feature | `filter()` | `response()` |
|---------|-----------|----------|
| Returns data | ✅ | ✅ |
| Returns counts | ❌ | ✅ (optional) |
| Returns columns | ❌ | ✅ (optional) |
| Returns searchRendered | ❌ | ✅ |
| Unlimit option | ✅ | ❌ |
| Best for | Bulk ops, exports | UI tables, lists |

**Example: Delete Selection**
```kotlin
@DeleteMapping("/api/products")
fun deleteProducts(@RequestBody request: DataSet.Request): Int {
    // Get all matching records (ignoring pagination)
    val toDelete = request.filter(db, productQuery, unlimit = true)

    val ids = toDelete.map { it[PRODUCT.ID] }
    return db.deleteFrom(PRODUCT)
        .where(PRODUCT.ID.`in`(ids))
        .execute()
}
```

### Selection

Selection filters by specific primary key values. Perfect for "delete selected rows" features.

**Selection Object:**
```kotlin
data class Selection(
    val include: Boolean,        // true = include only; false = exclude
    val keys: List<List<String>> // Primary key value rows
)
```

**Examples:**

Single primary key:
```json
{
  "selection": {
    "include": true,
    "keys": [["1"], ["2"], ["5"]]
  }
}
```
→ SQL: `WHERE ID IN (1, 2, 5)`

Composite primary key:
```json
{
  "selection": {
    "include": true,
    "keys": [["user1", "tenant-a"], ["user2", "tenant-b"]]
  }
}
```
→ SQL: `WHERE (USER_ID='user1' AND TENANT_ID='tenant-a') OR (USER_ID='user2' AND TENANT_ID='tenant-b')`

Exclude mode:
```json
{
  "selection": {
    "include": false,
    "keys": [["3"]]
  }
}
```
→ SQL: `WHERE NOT (ID = 3)`

**Primary Key Configuration:**
```kotlin
DataSet {
  db.select(
    field(PRODUCT.ID) {
      primaryKey()  // Required for selection
      search { f, s -> f.eq(s.toLongOrNull()) }
    }
  ).from(PRODUCT)
}
```

For composite keys, mark all key fields as `primaryKey()`. Key order in `keys` array must match field order.

**Combining with Search:**
```json
{
  "search": "status:active",
  "selection": {
    "include": true,
    "keys": [["1"], ["2"]]
  }
}
```
→ Records that are active AND have ID 1 or 2

---

## Important Notes for Claude

### When Working on This Codebase

1. **Always read existing code** before making changes
2. **Run tests** after any modification: `./gradlew test`
3. **Maintain backward compatibility** in v8.x
4. **Update tests** when changing behavior
5. **Follow Kotlin conventions** (immutability, data classes, sealed classes)
6. **Preserve type safety** - don't use `Any` or unchecked casts unless necessary

### Before Committing

1. Run `./gradlew ktlintFormat` to format code
2. Run `./gradlew test` to ensure tests pass
3. Update relevant documentation (README, CHANGELOG, or this file)
4. If adding a new public API, add KDoc documentation

### Common Pitfalls

1. **Don't break JOOQ query builder chain** - Always return the same query type
2. **Don't mutate TypedQuery** - Use copy() for modifications
3. **Don't forget batch mapper size** - Large result sets need batching
4. **Don't ignore field qualification** - Joins require qualified field names
5. **Don't skip parser regeneration** - After grammar changes, run `./gradlew generateGrammarSource`

---

## Contact and Maintenance

**Maintainer:** nthalk
**Issues:** https://github.com/iodesystems/dataset/issues
**License:** MIT
