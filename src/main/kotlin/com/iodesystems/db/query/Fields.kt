package com.iodesystems.db.query

import com.iodesystems.db.DataSet
import com.iodesystems.db.DataSet.Order.Direction
import com.iodesystems.db.util.StringUtil.isCamelCase
import com.iodesystems.db.util.StringUtil.isSnakeCase
import com.iodesystems.db.util.StringUtil.snakeToCamelCase
import org.jooq.*
import org.jooq.impl.DSL
import org.jooq.impl.TableImpl
import kotlin.reflect.full.isSubclassOf

typealias ConfiguredField<T> = Pair<Field<T>, DataSet.FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit>

class Fields<T>(
  private val mappedType: Class<T>,
  var customMapper: ((Record) -> T)? = null,
  val fields: MutableList<Field<*>> = mutableListOf(),
  private val searches: MutableList<Search> = mutableListOf(),
  init: (Fields<T>.(Fields<T>) -> Unit),
) {

  private val configuredFields = mutableMapOf<String, ConfiguredField<*>>()

  data class Search(
    val name: String,
    val open: Boolean,
    val search: (query: String) -> Condition?
  )

  init {
    init(this)
  }

  fun fields(collection: Collection<Field<*>>) {
    fields(collection.toTypedArray())
  }

  fun fields(fields: Array<Field<*>>) {
    fields.forEach {
      field(it)
    }
  }

  fun fields(table: TableImpl<*>) {
    fields(table.fields())
  }

  fun <T> field(
    field: Field<T>, alias: String, init: (DataSet.FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit) = {}
  ): Field<T> {
    return field(field.`as`(alias), init)
  }

  fun <T> field(
    field: Field<T>, init: (DataSet.FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit) = {}
  ): Field<T> {
    val existing = configuredFields[field.name]
    if (existing != null) {
      fields.remove(existing.first)
    }
    fields.add(field as Field<*>)
    val builder: ConfiguredField<T> = Pair(
      field, init
    )
    @Suppress("UNCHECKED_CAST")
    configuredFields[field.name] = builder as ConfiguredField<*>
    return field
  }

  fun search(
    name: String,
    open: Boolean = true,
    search: (query: String) -> Condition?
  ) {
    searches.add(Search(name, open, search))
  }

  fun <R : Record, TABLE : Select<R>> toDataSet(
    block: (sql: SelectFromStep<Record>) -> Select<R>
  ): DataSet<Select<R>, R, T> {
    val query = block(DSL.select(fields))

    // Build field configurations and default ordering
    val fieldConfigs = mutableMapOf<String, DataSet.FieldConfiguration<*>>()
    val defaultOrdering = mutableListOf<SortField<*>>()

    configuredFields.values.forEach { (field, init) ->
      val builder = DataSet.FieldConfiguration.Builder<Any?>(field as Field<Any?>)
      @Suppress("UNCHECKED_CAST")
      (init as (DataSet.FieldConfiguration.Builder<Any?>).(Field<Any?>) -> Unit)(builder, field as Field<Any?>)
      val config = builder.build()
      fieldConfigs[field.name] = config

      // Apply default ordering for orderable fields
      if (config.orderable) {
        val sortOrder = when (config.direction) {
          Direction.DESC -> SortOrder.DESC
          else -> SortOrder.ASC
        }
        defaultOrdering.add(field.sort(sortOrder))
      }
    }

    // Build search map and openSearches list
    val searchMap = mutableMapOf<String, (String) -> Condition?>()
    val openSearchList = mutableListOf<String>()
    searches.forEach { search ->
      searchMap[search.name] = search.search
      if (search.open) {
        openSearchList.add(search.name)
      }
    }

    @Suppress("UNCHECKED_CAST")
    return DataSet(
      table = query,
      mapper = { it.map(mapper as (R) -> T) },
      fields = fieldConfigs,
      searches = searchMap,
      openSearches = openSearchList,
      order = defaultOrdering
    )
  }

  @Deprecated("Use toDataSet instead", ReplaceWith("toDataSet(block)"))
  fun <R : Record, TABLE : Select<R>> toTypedQuery(
    block: (sql: SelectFromStep<Record>) -> Select<R>
  ): DataSet<Select<R>, R, T> {
    return toDataSet(block)
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
    // Check for map type
    if (mappedType == Map::class.java) {
      return@lazy { record: Record ->
        record.intoMap()
      }
    }

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
          However, only ${fields.size} fields are defined
          """.trimIndent()
      )
    }
    // Allow conversion of snake_case to camelCase
    val fieldsByCamelCaseName = fields.associateBy {
      if (it.name.isCamelCase()) it.name
      else if (it.name.isSnakeCase()) it.name.snakeToCamelCase()
      else it.name
    }
    val constructorFieldsByName = constructorFields.associateBy { it.name }
    val foundFields = mutableSetOf<String>()
    val foundConstructorFields = mutableSetOf<String>()
    // field errors
    fieldsByCamelCaseName.forEach { (name, field) ->
      val constructorField = constructorFieldsByName[name]
      if (constructorField == null) {
        errors.add("Constructor is missing configured field $name")
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
      val mappedField = fieldsByCamelCaseName[name]
      if (mappedField == null) {
        errors.add("Constructor is missing field $name")
        return@forEach
      }
      foundConstructorFields.add(name)
    }

    if (constructorFields.size != fields.size) {
      errors.add(
        "Constructor has ${constructorFields.size} fields but only ${fields.size} fields are defined"
      )
    }

    if (errors.isEmpty()) {
      { record: Record ->
        val args = constructorFields.map { field ->
          val mappedField = fieldsByCamelCaseName[field.name]
          record[mappedField]
        }
        @Suppress("UNCHECKED_CAST")
        constructor.newInstance(*args.toTypedArray()) as T
      }
    } else {
      throw IllegalArgumentException(
        "Could not create mapper for type $mappedType\n" +
            errors.joinToString("\n\n")
      )
    }
  }
}
