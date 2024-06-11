package com.iodesystems.db.query

import com.iodesystems.db.query.TypedQuery.FieldConfiguration
import com.iodesystems.db.util.StringUtil.lowerCaseFirstLetter
import com.iodesystems.db.util.StringUtil.snakeToCamel
import org.jooq.*
import org.jooq.impl.DSL
import kotlin.reflect.full.isSubclassOf

class Fields<T>(
  private val mappedType: Class<T>,
  var customMapper: ((Record) -> T)? = null,
  private val fields: MutableList<Field<*>> = mutableListOf(),
  private val searches: MutableList<Search> = mutableListOf(),
  init: (Fields<T>.() -> Unit),
) : List<Field<*>> by fields {


  data class Search(
    val name: String,
    val open: Boolean,
    val search: (query: String) -> Condition?
  )

  private val builders = mutableListOf<Pair<Field<*>, (FieldConfiguration.Builder<*>.(field: Field<*>) -> Unit)>>()

  init {
    init()
  }

  fun <T> field(
    field: Field<T>, alias: String, init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit) = {}
  ): Field<T> {
    return field(field.`as`(alias), init)
  }

  fun <T> field(
    field: Field<T>, init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit) = {}
  ): Field<T> {
    val nonQualified = field.`as`(field.name)
    fields.add(nonQualified as Field<*>)
    @Suppress("UNCHECKED_CAST") builders.add(
      Pair(
        nonQualified, init
      ) as Pair<Field<*>, (FieldConfiguration.Builder<*>.(field: Field<*>) -> Unit)>
    )
    return field
  }

  fun search(
    name: String,
    open: Boolean = true,
    search: (query: String) -> Condition?
  ) {
    searches.add(Search(name, open, search))
  }

  fun <R : Record> toTypedQuery(
    block: (sql: SelectFromStep<Record>) -> TableLike<R>
  ): TypedQuery<Table<R>, R, T> {
    val table = block(DSL.select(fields)).asTable("query")
    @Suppress("UNCHECKED_CAST")
    return TypedQuery.forTable<R, T>(table, mapper as (Record) -> T) {
      val config = this
      builders.forEach { (field, init) ->
        config.field(field) {
          init(field)
        }
      }
      searches.forEach { search ->
        config.search(
          search.name,
          open = search.open
        ) { query, _ ->
          search.search(query)
        }
      }
    }
  }

  private val mapper by lazy {
    if (customMapper != null) {
      return@lazy customMapper
    }

    // If our type is a in the POJO dir, we can use the generated mapper
    if (mappedType.name.contains(".tables.pojos.")) {
      return@lazy { it.into(mappedType) }
    }

    if (Record::class.java.isAssignableFrom(mappedType)) {
      return@lazy { it }
    }

    // If our type is a hash map, we can use the generated mapper
    if (mappedType == HashMap::class.java) {
      return@lazy { it.intoMap() }
    }

    // Otherwise, we expect a data class with a constructor that matches our fields
    // Validate that mappedType is a data class has a constructor with our fields
    val errors = mutableListOf<String>()
    val constructor = mappedType.declaredConstructors.firstOrNull()
    if (constructor == null) {
      errors.add("Type $mappedType does not have a constructor")
      throw IllegalArgumentException(errors.joinToString("\n\n"))
    }

    val constructorFields = constructor.parameters
    if (constructorFields.isEmpty()) {
      errors.add("Type $mappedType constructor has no fields")
    }
    if (constructorFields.size != fields.size) {
      errors.add(
        """
          Type $mappedType constructor has ${constructorFields.size} fields
          However, it has ${fields.size} fields
          """.trimIndent()
      )
    }
    // Allow conversion of snake_case to camelCase
    val fieldsByName = fields.associateBy { it.name.snakeToCamel().lowerCaseFirstLetter() }
    val constructorFieldsByName = constructorFields.associateBy { it.name }
    val foundFields = mutableSetOf<String>()
    val foundConstructorFields = mutableSetOf<String>()
    // field errors
    fieldsByName.forEach { (name, field) ->
      val constructorField = constructorFieldsByName[name]
      if (constructorField == null) {
        errors.add("Constructor field is missing field $name")
        return@forEach
      }
      foundFields.add(name)
      // Check assignability
      if (!field.type.kotlin.isSubclassOf(constructorField.type.kotlin)) {
        errors.add(
          """
            Constructor field $name is not assignable from field ${field.type.simpleName}, it is ${constructorField.type.simpleName}
            """.trimIndent()
        )
      }
    }
    // constructor errors
    constructorFieldsByName.forEach { (name, _) ->
      val mappedField = fieldsByName[name]
      if (mappedField == null) {
        errors.add("Type $mappedType is missing field $name")
        return@forEach
      }
      foundConstructorFields.add(name)
    }

    if (foundConstructorFields.size != constructorFieldsByName.size) {
      val missingFields = (constructorFieldsByName.keys - foundConstructorFields).joinToString(", ")
      errors.add("Type $mappedType is missing fields $missingFields")
    }

    if (errors.isEmpty()) {
      { record: Record ->
        val args = constructorFields.map { field ->
          val mappedField = fieldsByName[field.name]
          record[mappedField] as Any
        }
        @Suppress("UNCHECKED_CAST")
        constructor.newInstance(*args.toTypedArray()) as T
      }
    } else {
      throw IllegalArgumentException(errors.joinToString("\n\n"))
    }
  }
}
