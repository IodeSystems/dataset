DataSet
========================================

A Kotlin library providing a type-safe, DSL-based query builder that converts freeform user queries into JOOQ SQL conditions, designed with an aim for least surprise.

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**Version:** 9.0.0
**Requirements:** Kotlin 2.1+, Java 21+, JOOQ 3.19+

Example query language:
```clickhouse
this "and that", "or these and those and" someField:"is this"
```

## Features

- **Type-safe query building** - Compile-time verification of field types and operations
- **Natural language search parsing** - Convert user-friendly search queries to SQL using ANTLR4
- **Declarative field configuration** - Define searchable/orderable fields inline with SELECT statements
- **HTTP request integration** - Built-in support for pagination, ordering, and filtering from REST APIs
- **Flexible mapping** - Transform database records to DTOs, Maps, or any custom type
- **Automatic search error recovery** - Gracefully handles malformed search queries

## Quick Start

Given a defined dataset schema, users can input freeform query
language to produce filtered result sets from a provided
dataset.

All you have to do is:

1. Define a DataSet from a JOOQ table using `DataSet { }`
2. Configure fields as searchable/orderable inline
3. Send a JSON DataSet.Request to an endpoint
4. Render produced records and sizing information

No more custom SQL!

## Search Query Semantics

1. Unquoted terms are converted into tokens.
2. Spaced terms are considered AND conditions.
3. Commaed terms are considered OR conditions.
4. Terms can be grouped with parentheses.
5. Targets can be specialized with colons `target:value`
6. Negation is supported with `!` prefix

### Search Examples

| Query | Meaning |
|-------|---------|
| `john` | Simple term search |
| `john active` | john AND active |
| `john, jane` | john OR jane |
| `!inactive` | NOT inactive |
| `(john, jane) status:active` | (john OR jane) AND status:active |
| `email:john@example.com` | Targeted field search |


## Complete Example
Given the following query:
```clickhouse
from:"some name" attachment:true (content:(user_input), user_input)
```

It is expected to be converted into:
```SQL
WHERE 
    from:"some name"
    AND attachment:true
    AND (
        content:user_input
        OR any:user_input
    )
```

And given the schema:
```kotlin
val query = DataSet {
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
    search("is_x", open = true) { s ->
        if (s == "x") DSL.trueCondition()
        else null
    }

    db.select(
        field(EMAIL_ID) {
            primaryKey()
        },
        field(ATTACHMENT) {
            search(global = false) { f, s ->
                if (s.lowercase() == "true") f.isNull else null
            }
        },
        field(CONTENT) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
        },
        field(FROM) {
            orderable()
            search { f, s -> f.containsIgnoreCase(s) }
        },
        CREATED_AT
    ).from(EMAIL)
}
val request = DataSet.Request(
    showColumns = true, showCounts = true, search = "x", partition = ""
)
val response = request.response(db, query)
```

## Field Configuration Options

```kotlin
field(FIELD) {
    primaryKey()                          // Mark as primary key
    orderable()                           // Allow ordering (default ASC)
    orderable(Direction.DESC)             // Allow ordering with default direction
    search { f, s -> f.eq(s) }           // Searchable, global (open search)
    search(global = false) { f, s -> ... } // Searchable, targeted only
}
```

## Named Searches

Named searches are custom search functions that aren't tied to a specific field. They're perfect for complex logic like date ranges, price ranges, or multi-field conditions.

### Basic Named Search

```kotlin
val query = DataSet {
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
query.search("daysAgo:<30")   // Products created less than 30 days ago
query.search("daysAgo:>90")   // Products created more than 90 days ago
```

### Advanced Named Search Examples

```kotlin
val query = DataSet {
    // Price range search
    search("price") { s ->
        when {
            s.startsWith(">=") -> {
                s.drop(2).toBigDecimalOrNull()?.let { PRICE.greaterOrEqual(it) }
            }
            s.startsWith("<=") -> {
                s.drop(2).toBigDecimalOrNull()?.let { PRICE.lessOrEqual(it) }
            }
            s.startsWith(">") -> {
                s.drop(1).toBigDecimalOrNull()?.let { PRICE.greaterThan(it) }
            }
            s.startsWith("<") -> {
                s.drop(1).toBigDecimalOrNull()?.let { PRICE.lessThan(it) }
            }
            s.contains("..") -> {
                val parts = s.split("..")
                if (parts.size == 2) {
                    val min = parts[0].toBigDecimalOrNull()
                    val max = parts[1].toBigDecimalOrNull()
                    if (min != null && max != null) {
                        PRICE.between(min, max)
                    } else null
                } else null
            }
            else -> {
                s.toBigDecimalOrNull()?.let { PRICE.eq(it) }
            }
        }
    }

    // Status flag search
    search("active", open = false) { s ->
        when (s.lowercase()) {
            "true", "yes" -> STATUS.eq("ACTIVE")
            "false", "no" -> STATUS.eq("INACTIVE")
            else -> null
        }
    }

    // Open search (participates in global search)
    search("special", open = true) { s ->
        if (s == "x") DSL.trueCondition()
        else null
    }

    db.select(
        field(ID) { primaryKey() },
        field(NAME) { search { f, s -> f.containsIgnoreCase(s) } },
        STATUS,
        PRICE,
        CREATED_AT
    ).from(PRODUCT)
}

// Usage examples:
query.search("price:>=100")           // Products $100 or more
query.search("price:<50")             // Products under $50
query.search("price:10..99")          // Products between $10 and $99
query.search("active:true")           // Active products only
query.search("laptop daysAgo:<7")     // Recent laptops
```

### Named Search Parameters

- **`name`**: The search target name (e.g., `"daysAgo"` matches `daysAgo:<30`)
- **`open`**:
  - `true` (default): Participates in global/unqualified searches
  - `false`: Only matches when explicitly targeted (e.g., `active:true`)
- **Return `null`**: If the search string doesn't match, return `null` to ignore the condition

Named searches must be defined **before** the `db.select()` call.

## Lazy SQL (Runtime Conditions)

Sometimes you need conditions that are evaluated **at query execution time** rather than at DataSet creation time. This is essential for values that change per-request, like `currentUserId`, tenant IDs, or current timestamps.

### When Conditions Are Applied

**Static conditions** (evaluated once during DataSet construction):
```kotlin
val query = DataSet {
    val tenantId = getCurrentTenantId()  // ❌ Evaluated once at startup!
    db.select(
        field(USER.ID) { primaryKey() }
    ).from(USER)
}.where(USER.TENANT_ID.eq(tenantId))  // Wrong: uses startup tenantId
```

**Lazy SQL** (evaluated at query execution):
```kotlin
val query = DataSet {
    db.select(
        field(USER.ID) { primaryKey() }
    ).from(USER)
    .where(lazy {
        // ✅ Evaluated per-request!
        USER.TENANT_ID.eq(getCurrentTenantId())
    })
}
```

### Use Cases

**Multi-tenant applications:**
```kotlin
// DataSet created once at startup
val userQuery = DataSet {
    db.select(
        field(USER.ID) { primaryKey() },
        field(USER.NAME) { search { f, s -> f.containsIgnoreCase(s) } }
    ).from(USER)
    .where(lazy {
        USER.TENANT_ID.eq(RequestContext.currentTenantId())
    })
}

// Later, in request handler:
RequestContext.setTenantId("tenant-123")
val users = request.filter(db, userQuery)  // Only sees tenant-123 users
```

**User permissions:**
```kotlin
val productQuery = DataSet {
    db.select(
        field(PRODUCT.ID) { primaryKey() },
        field(PRODUCT.NAME) { search { f, s -> f.containsIgnoreCase(s) } }
    ).from(PRODUCT)
    .where(lazy {
        PRODUCT.OWNER_ID.eq(SecurityContext.currentUserId())
    })
}
```

**Complex conditional logic:**
```kotlin
val query = DataSet {
    db.select(
        field(PRODUCT.ID) { primaryKey() }
    ).from(PRODUCT)
    .where(lazy {
        val user = SecurityContext.currentUser()
        if (user.isAdmin) {
            DSL.trueCondition()  // Admins see everything
        } else {
            PRODUCT.OWNER_ID.eq(user.id)  // Users see only their products
        }
    })
}
```

### Why Lazy SQL?

- **Discoverable**: `lazy { }` is available in the DataSet DSL
- **Type-safe**: Full JOOQ type checking
- **Clear intent**: Obviously runtime-evaluated
- **Flexible**: Supports any JOOQ Condition

### Important Notes

- Lazy SQL is evaluated **every time** the query executes (not cached)
- Perfect for multi-tenant apps, user permissions, and time-based queries
- Uses JOOQ internal APIs (still functional, monitored for breaking changes)

## Mapping to DTOs

Transform query results into custom types:

```kotlin
data class UserDto(val id: Long, val email: String)

val query = DataSet {
    db.select(
        field(USER.ID) {
            primaryKey()
            search { f, s -> f.eq(s.toLongOrNull()) }
        },
        field(USER.EMAIL) {
            search { f, s -> f.containsIgnoreCase(s) }
        }
    ).from(USER)
}.map { record ->
    UserDto(
        id = record[USER.ID],
        email = record[USER.EMAIL]
    )
}

val users: List<UserDto> = query.data(db).fetch()
```

## Migration from v8.x

If you're upgrading from v8.x, here's what changed:

**Before (v8.x):**
```kotlin
// Old DataSetBuilder API
val query = DataSetBuilder.build {
    db.select(
        field(USER_ID) { primaryKey() }
    ).from(USER)
}

// Or old Fields API
val fields = DataSet.build {
    field(USER_ID)
}
val query = fields.toDataSet { sql -> sql.from(USER) }
```

**After (v9.0):**
```kotlin
// New unified API
val query = DataSet {
    db.select(
        field(USER_ID) { primaryKey() }
    ).from(USER)
}
```

**Key changes:**
- Single entry point: `DataSet { }` replaces all old builder patterns
- Field configuration uses 2-parameter lambda: `search { f, s -> ... }` instead of `search { s -> field.method(s) }`
- Named searches defined before select: `search("name") { ... }`
- Deprecated methods removed: `DataSet.forTable()`, `DataSet.build()`, `DataSetBuilder.build()`, `Fields.toDataSet()`

## HTTP Integration: `filter()` vs `response()`

DataSet.Request provides two methods for executing queries, each designed for different use cases:

### `request.filter()` - Get Data Only

Use `filter()` when you **only need the data** (no metadata, counts, or column information). Perfect for:
- Bulk operations (delete, update)
- Export operations
- Simple list endpoints that don't need pagination metadata
- Background jobs

```kotlin
val request = DataSet.Request(
    search = "status:active",
    page = 0,
    pageSize = 50
)

// Returns: List<T> - just the data
val data: List<Record> = request.filter(db, query)

// For unlimited results (bulk operations)
val allData = request.filter(db, query, unlimit = true)
```

**What `filter()` does:**
- ✅ Applies search, selection, ordering, pagination
- ✅ Returns data only
- ❌ No counts
- ❌ No column metadata
- ❌ No searchRendered

### `request.response()` - Get Full Response

Use `response()` when you need **complete metadata** for UI rendering. Perfect for:
- List/table views with pagination
- Endpoints that need total counts
- UIs that need column metadata (searchable, orderable, types)
- Search interfaces where you need to show corrected search

```kotlin
val request = DataSet.Request(
    search = "status:active",
    page = 0,
    pageSize = 50,
    showCounts = true,      // Include total counts
    showColumns = true      // Include column metadata
)

// Returns: Response<T> with data, counts, columns, searchRendered
val response: DataSet.Response<Record> = request.response(db, query)

// Access response components:
val data = response.data                    // List<T>
val totalCount = response.count?.inQuery    // Total matching records
val columns = response.columns              // Column metadata for UI
val correctedSearch = response.searchRendered // Show user what was searched
```

**What `response()` does:**
- ✅ Applies search, selection, ordering, pagination
- ✅ Returns data
- ✅ Returns counts (if `showCounts = true`)
- ✅ Returns column metadata (if `showColumns = true`)
- ✅ Returns corrected search string

### Quick Comparison

| Feature | `filter()` | `response()` |
|---------|-----------|----------|
| Returns data | ✅ | ✅ |
| Returns counts | ❌ | ✅ (optional) |
| Returns column metadata | ❌ | ✅ (optional) |
| Returns searchRendered | ❌ | ✅ |
| Unlimit option | ✅ | ❌ |
| Best for | Bulk ops, exports | UI tables, lists |

### Example Use Cases

**List Endpoint (use `response()`):**
```kotlin
@GetMapping("/api/products")
fun listProducts(@RequestBody request: DataSet.Request): DataSet.Response<ProductDto> {
    return request.response(db, productQuery)
}
```

**Delete Selection (use `filter()` with selection):**
```kotlin
@DeleteMapping("/api/products")
fun deleteProducts(@RequestBody request: DataSet.Request): Int {
    // Get selected records using selection
    val toDelete = request.filter(db, productQuery, unlimit = true)

    // Extract IDs and delete
    val ids = toDelete.map { it[PRODUCT.ID] }
    return db.deleteFrom(PRODUCT)
        .where(PRODUCT.ID.`in`(ids))
        .execute()
}
```

**Example requests:**

Delete specific products by ID:
```json
{
  "selection": {
    "include": true,
    "keys": [["1"], ["2"], ["5"]]
  }
}
```

Delete all except specific products:
```json
{
  "selection": {
    "include": false,
    "keys": [["3"], ["4"]]
  }
}
```

Delete all matching a search:
```json
{
  "search": "status:inactive"
}
```

**Export (use `filter()` with unlimit):**
```kotlin
@GetMapping("/api/products/export")
fun exportProducts(@RequestBody request: DataSet.Request): ByteArray {
    val allData = request.filter(db, productQuery, unlimit = true)
    return csvExporter.export(allData)
}
```

## Selection

Selection allows filtering by specific primary key values. Perfect for "delete selected rows" or "export selected rows" features in UIs.

### Selection Object

```kotlin
data class Selection(
    val include: Boolean,        // true = include only these; false = exclude these
    val keys: List<List<String>> // List of primary key value rows
)
```

### How Selection Works

Selection filters by primary key values:

1. **Single Primary Key**: Each key row has one value
   ```json
   {
     "selection": {
       "include": true,
       "keys": [["1"], ["2"], ["5"]]
     }
   }
   ```
   SQL: `WHERE ID IN (1, 2, 5)`

2. **Composite Primary Key**: Each key row has multiple values
   ```json
   {
     "selection": {
       "include": true,
       "keys": [
         ["user1", "tenant-a"],
         ["user2", "tenant-b"]
       ]
     }
   }
   ```
   SQL: `WHERE (USER_ID = 'user1' AND TENANT_ID = 'tenant-a') OR (USER_ID = 'user2' AND TENANT_ID = 'tenant-b')`

3. **Exclude Mode**: Invert the selection
   ```json
   {
     "selection": {
       "include": false,
       "keys": [["3"]]
     }
   }
   ```
   SQL: `WHERE NOT (ID = 3)`

### Combining Selection with Search

Selection can be combined with search for powerful filtering:

```json
{
  "search": "status:active",
  "selection": {
    "include": true,
    "keys": [["1"], ["2"], ["5"]]
  }
}
```

This finds records that are BOTH active AND have ID 1, 2, or 5.

### Primary Key Configuration

Selection requires fields to be marked as `primaryKey()`:

```kotlin
val query = DataSet {
    db.select(
        field(PRODUCT.ID) {
            primaryKey()  // Required for selection
            search { f, s -> f.eq(s.toLongOrNull()) }
        },
        field(PRODUCT.NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
        }
    ).from(PRODUCT)
}
```

For composite keys, mark all key fields:

```kotlin
val query = DataSet {
    db.select(
        field(USER.ID) {
            primaryKey()  // First part of composite key
            search { f, s -> f.eq(s) }
        },
        field(TENANT.ID) {
            primaryKey()  // Second part of composite key
            search { f, s -> f.eq(s) }
        },
        field(USER.NAME) {
            search { f, s -> f.containsIgnoreCase(s) }
        }
    ).from(USER)
}
```

**Key ordering:** The order of `keys` array values must match the order of `primaryKey()` fields in your DataSet definition.

### UI Integration Example

Typical frontend workflow:

1. User views paginated table with checkboxes
2. User selects specific rows (e.g., products with IDs 1, 5, 7)
3. User clicks "Delete Selected"
4. Frontend sends:
   ```json
   {
     "selection": {
       "include": true,
       "keys": [["1"], ["5"], ["7"]]
     }
   }
   ```
5. Backend deletes only those specific records

Or for "Select All" with exclusions:

1. User clicks "Select All" (selects all 100 products)
2. User unchecks 2 products (IDs 3 and 8)
3. User clicks "Delete Selected"
4. Frontend sends:
   ```json
   {
     "selection": {
       "include": false,
       "keys": [["3"], ["8"]]
     }
   }
   ```
5. Backend deletes all except IDs 3 and 8

## User Query Errors
------------------------------------
Because this is a regular language, and users may supply bad data,
there needs to be a method of rescuing parse errors.

Any time there is a parse error, the offending token is escaped.

This works out in the grammar to convert:

```clickhouse
from:((, (this is parser torture""\")
```

to

```clickhouse
from:(\(, \(this is parser torture\"\"\")
```

While the user may not end up rendering what they think,
the (last applied) search is returned in the dataset result.

In the case of partitioning or sub querying, we simply send back an empty string.

Make sure to update the user's search input with `searchRendered`
return value to ensure that the user knows what produced their result.

## Building from Source

```bash
./gradlew build
./gradlew test
```

## Links

- **Repository:** https://github.com/iodesystems/dataset
- **Issues:** https://github.com/iodesystems/dataset/issues
- **JOOQ Documentation:** https://www.jooq.org/doc/
- **ANTLR4 Documentation:** https://github.com/antlr/antlr4/blob/master/doc/index.md

## License

MIT License
