DataSet
========================================

A simple query language parser that converts user queries
to SQL conditions (using Antlr4 and JOOQ) with an aim
for least surprise.


Example query language:
```clickhouse
this "and that", "or these and those and" someField:"is this"
```

Overview
----------------------------------------
Given a defined dataset schema, users can input freeform query
language to produce filtered result sets from a provided
dataset.

All you have to do is:

1. Define a TypedQuery from a JOOQ table
2. Configure fields as searchable/orderable
3. Send a JSON DataSet.Request to an endpoint
4. Render produced records and sizing information

No more custom SQL!

Semantics
----------------------------------------

1. Unquoted terms are converted into tokens.
2. Spaced terms are considered AND conditions.
3. Commaed terms are considered OR conditions.
4. Terms can be grouped with parentheses.
5. Targets can be specialized with colons `target:value`


Example
----------------------------------------
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
val query = TypedQuery {
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
val req = DataSet.Request(
    showColumns = true, showCounts = true, search = "x", partition = ""
)
val rsp = DataSet.query(db, query, req)
```

User Query Errors
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

