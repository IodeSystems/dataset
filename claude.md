# DataSet Library - Claude Context

## Project Overview

**DataSet** is a Kotlin library that provides a type-safe, DSL-based query builder for converting freeform user queries into JOOQ SQL conditions. It emphasizes "least surprise" behavior and declarative configuration.

**Repository:** iODESystems/dataset
**Current Version:** 8.0.9-SNAPSHOT
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

**TypedQuery** (`src/main/kotlin/com/iodesystems/db/query/TypedQuery.kt`)
- Core data structure representing a configured query
- Holds table, mapper, conditions, ordering, field configurations
- Immutable with copy-based mutation methods

**DataSetBuilder** (`src/main/kotlin/com/iodesystems/db/http/DataSetBuilder.kt`)
- **Preferred API pattern** for constructing TypedQuery instances
- Provides DSL for inline field configuration within SELECT statements
- Returns TypedQuery directly without intermediate steps

**SearchParser** (`src/main/kotlin/com/iodesystems/db/search/SearchParser.kt`)
- ANTLR4-based parser for freeform search queries
- Supports: quoted strings, AND/OR logic, grouping, negation, field targeting
- Converts natural language searches to structured Term lists

**DataSet.query() and filter()** (`src/main/kotlin/com/iodesystems/db/http/DataSet.kt`)
- HTTP integration layer
- Converts Request objects (with search, ordering, pagination) into database queries
- Returns Response objects with data, counts, and column metadata

### Directory Structure

```
src/main/kotlin/com/iodesystems/db/
├── http/
│   ├── DataSet.kt               # HTTP request/response handling
│   ├── DataSetBuilder.kt        # Preferred API entry point
│   └── DataSetBuilderContext.kt # DSL context for builder
├── query/
│   ├── TypedQuery.kt            # Core query data structure
│   ├── Fields.kt                # Field configuration builder (legacy)
│   └── FieldConfigBuilder.kt    # Field configuration DSL
├── search/
│   ├── SearchParser.kt          # ANTLR4 search parser
│   ├── SearchConditionFactory.kt # Converts parsed search to SQL conditions
│   └── DynamicCondition.kt      # Lazy condition evaluation
├── jooq/
│   └── JooqExtensions.kt        # JOOQ utility extensions
└── util/
    ├── StringUtil.kt            # String manipulation helpers
    └── DebugUtil.kt             # Debug logging utilities

src/main/antlr4/
├── SearchLexer.g4               # Search query lexer grammar
└── SearchParser.g4              # Search query parser grammar

src/test/kotlin/com/iodesystems/db/
├── TypedQueryTest.kt            # Query parsing and execution tests
├── DataSetBuilderTest.kt        # Builder API tests
└── TestUtils.kt                 # Test infrastructure (H2, mocks)
```

---

## Current Refactoring Initiative

### Problem Statement

Over time, the library has evolved multiple API patterns for constructing TypedQuery instances:

1. **TypedQuery.forTable()** - Original companion factory method
2. **DataSet.forTable()** - HTTP package wrapper (3 overloads)
3. **Fields.toTypedQuery()** - Fluent builder pattern
4. **DataSetBuilder.build()** - Current preferred pattern

**Issues:**
- Too many entry points cause confusion
- Not discoverable (users don't know which to use)
- Code duplication across layers
- Maintenance burden for multiple patterns

**User's Usage Pattern:**
The maintainer uses **exclusively DataSetBuilder.build()** and wants to make this the only pattern.

---

## Refactoring Goals (v9.0)

### 1. Single Entry Point API

**Remove these methods:**
- `TypedQuery.forTable()`, `forTableMaps()`, `forTableRecords()`
- `DataSet.forTable()` (all 3 overloads)
- `DataSet.build()` and `buildForMaps()`
- `Fields.toTypedQuery()`

**Create new primary API:**
```kotlin
TypedQuery {
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

**Implementation approach:**
- Add `operator fun invoke()` to TypedQuery companion object
- Migrate DataSetBuilder logic into TypedQuery companion
- Remove DataSetBuilder object entirely (or keep as deprecated alias)

### 2. Clean Up Unused Code

**Confirmed unused:**
- `Map.partition()` extension in TypedQuery.kt:459-470
- Deprecated `DataSet.toResponse()` method (already marked deprecated)

**Needs audit:**
- StringUtil extensions (`camelToSnakeCase`, `lowerCaseFirstLetter`, `toEnumOrNull`)
- DynamicCondition (uses deprecated JOOQ APIs)
- Duplicate response building logic in DataSet.kt

### 3. Improve Test Coverage

**Current state:**
- TypedQueryTest.kt: 17 tests using OLD API (DataSet.forTable)
- DataSetBuilderTest.kt: 5 tests using NEW API (DataSetBuilder.build)

**Goals:**
- Port all tests to new API
- Expand coverage to 30+ tests
- Add error handling tests
- Add integration tests with DataSet.query() and filter()
- Test edge cases: complex joins, subqueries, field qualification

### 4. Improve Discoverability

**Issues:**
- No clear "getting started" path
- Multiple entry points confuse new users
- Builder pattern hidden in companion object

**Solutions:**
- Single, obvious entry point: `TypedQuery { ... }`
- Comprehensive KDoc with examples
- Updated README with quick start
- Migration guide for v8.x users

---

## Implementation Phases

### Phase 1: Non-Breaking Addition (v8.x)
1. Add `TypedQuery.invoke()` operator as new preferred API
2. Keep all existing APIs intact
3. Add `@Deprecated` annotations with migration hints
4. Update documentation to show new pattern first

### Phase 2: Test Migration (v8.x)
1. Create new test file with new API
2. Port all TypedQueryTest cases
3. Expand coverage for edge cases
4. Add integration tests

### Phase 3: Breaking Cleanup (v9.0)
1. Remove all deprecated methods
2. Remove truly unused utilities
3. Simplify internal architecture (remove Fields if possible)
4. Update README and create migration guide
5. Publish with major version bump

---

## Key Design Decisions

### Why Keep Type Parameters?

TypedQuery maintains three type parameters:
```kotlin
TypedQuery<T : Select<R>, R : Record, M>
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
fun search(query: String): TypedQuery<T, R, M> = copy(...)
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

1. Add property to `FieldConfiguration` data class (TypedQuery.kt)
2. Add builder method to `FieldConfiguration.Builder`
3. Add builder method to `FieldConfigBuilder` (DataSetBuilderContext.kt)
4. Update `buildTypedQuery()` to handle new property
5. Add test in DataSetBuilderTest.kt

### Adding a New Search Feature

1. Update ANTLR4 grammar files (SearchLexer.g4, SearchParser.g4)
2. Run Gradle task to regenerate parser: `./gradlew generateGrammarSource`
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

**Old Pattern 1: DataSet.forTable**
```kotlin
DataSet.forTable(table) {
  field(FIELD_A) { f ->
    search { s -> f.containsIgnoreCase(s) }
  }
  autoDetectFields(db)
}
```

**Old Pattern 2: TypedQuery.forTable**
```kotlin
TypedQuery.forTable(DSL.selectFrom(table), { it }) {
  field(FIELD_A) {
    orderable()
  }
}
```

**Old Pattern 3: DataSetBuilder.build (current preferred)**
```kotlin
DataSetBuilder.build {
  db.select(
    field(USER_ID) { primaryKey() }
  ).from(USER)
}
```

### After (v9.0)

**New Pattern: TypedQuery { }**
```kotlin
TypedQuery {
  db.select(
    field(USER_ID) {
      primaryKey()
      search { f, s -> f.eq(s.toLongOrNull()) }
    }
  ).from(USER)
}
```

**Key Changes:**
1. Use `TypedQuery { ... }` instead of `DataSetBuilder.build { ... }`
2. Use `TypedQuery { ... }` instead of `DataSet.forTable(...) { ... }`
3. Use `TypedQuery { ... }` instead of `TypedQuery.forTable(...) { ... }`
4. Field configuration syntax remains the same
5. Search syntax remains the same

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

### 1. DynamicCondition Uses Deprecated JOOQ APIs
**File:** `src/main/kotlin/com/iodesystems/db/search/DynamicCondition.kt`
**Status:** Still functional but relies on `@Deprecated` JOOQ interfaces
**Impact:** May break in future JOOQ versions
**Action:** Monitor JOOQ releases, consider alternative implementation

### 2. Multiple Response Building Paths
**Files:** `DataSet.query()` vs `Response.fromRequest()`
**Status:** Duplicate logic with subtle differences
**Impact:** Maintenance burden, potential for bugs
**Action:** Consolidate in v9.0 refactoring

### 3. Limited Support for Subqueries
**Status:** Works but not extensively tested
**Impact:** May have edge cases with field qualification
**Action:** Add comprehensive subquery tests

### 4. Auto-Detect Fields Requires DSLContext
**Method:** `TypedQuery.Builder.autoDetectFields(db)`
**Status:** Convenient but requires database connection during setup
**Impact:** Not usable in purely declarative scenarios
**Action:** Consider alternative field detection mechanism

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
2. **Streaming API** - Large result set streaming with backpressure
3. **Query explain** - Debug what SQL will be generated without executing
4. **Field validators** - Type-safe validation rules on field configuration

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

### Create a TypedQuery (Current v8.x)
```kotlin
val query = DataSetBuilder.build {
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
```kotlin
val db: DSLContext = // ... JOOQ context
val request = DataSet.Request(
  search = "john@email.com",
  page = 0,
  pageSize = 50
)

val response = DataSet.query(db, query, request)
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
  primaryKey()                          // Mark as primary key
  orderable()                           // Allow ordering by this field
  orderable(Direction.DESC)             // Default sort direction
  search { f, s -> f.eq(s) }           // Searchable, open (global search)
  search(global = false) { f, s -> ... } // Searchable, not open (targeted only)
}
```

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
