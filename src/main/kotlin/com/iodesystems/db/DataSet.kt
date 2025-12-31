package com.iodesystems.db

import com.iodesystems.db.search.SearchConditionFactory
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.util.StringUtil.camelToTitleCase
import com.iodesystems.db.util.StringUtil.isCamelCase
import com.iodesystems.db.util.StringUtil.isSnakeCase
import com.iodesystems.db.util.StringUtil.snakeToCamelCase
import com.iodesystems.db.util.StringUtil.snakeToTitleCase
import com.iodesystems.db.util.StringUtil.titleCase
import org.jooq.*
import org.jooq.impl.DSL
import kotlin.sequences.Sequence
import kotlin.streams.asSequence
import java.util.*

data class DataSet<T : Select<R>, R : Record, M>(
  val table: T,
  val mapper: (List<R>) -> List<M>,
  val mapperBatchSize: Int = 1000,
  val conditions: List<Condition> = emptyList(),
  val offset: Int = 0,
  val limit: Int = -1,
  val order: List<SortField<*>> = emptyList(),
  val fields: Map<String, FieldConfiguration<*>> = emptyMap(),
  val searches: Map<String, (query: String) -> Condition?> = emptyMap(),
  val openSearches: List<String> = emptyList(),
  val lastSearchCorrected: String? = null,
  val searchConditionFactory: SearchConditionFactory = SearchConditionFactory()
) : SearchConditionFactory.SearchConditionContext {

  private val fieldsByNameLower by lazy {
    fields.mapKeys {
      it.key.lowercase().replace("_", "")
    }
  }
  private val searchesByNameLower by lazy {
    searches.mapKeys {
      it.key.lowercase().replace("_", "")
    }
  }
  private val openSearchProviders = fields.values.asSequence()
    .filter { it.open && it.search != null }
    .map { fc ->
      val fn = fc.search!!
      SearchConditionFactory.SearchProvider { q -> fn(q) }
    }.toList() + openSearches.mapNotNull { name ->
    val fn = searches[name]
    fn?.let { f -> SearchConditionFactory.SearchProvider { q -> f(q) } }
  }

  override fun getSearchProvider(name: String): SearchConditionFactory.SearchProvider? {
    val normalizedName = name.lowercase().replace("_", "")
    // First check named/targeted searches
    searchesByNameLower[normalizedName]?.let { fn ->
      return SearchConditionFactory.SearchProvider { q -> fn(q) }
    }
    // Then check fields by name
    val field = fieldsByNameLower[normalizedName]
    val fieldSearch = field?.search
    return fieldSearch?.let { f ->
      SearchConditionFactory.SearchProvider { q -> f(q) }
    }
  }

  override fun getOpenSearchProviders(): Collection<SearchConditionFactory.SearchProvider> {
    return openSearchProviders
  }

  fun <F> fieldConfiguration(field: Field<F>, alias: String? = null): FieldConfiguration<F>? {
    val name = alias ?: fieldName(field.name).name.lowercase()
    return fieldsByNameLower[name]?.let {
      @Suppress("UNCHECKED_CAST")
      it as FieldConfiguration<F>
    }
  }

  fun <F> field(field: Field<F>, alias: String? = null): Field<F>? {
    return fieldConfiguration(field, alias)?.field
  }

  enum class Nulls {
    FIRST, LAST
  }

  data class SearchRendered(
    val search: String, val condition: Condition?
  )

  data class Data<T : Select<R>, R : Record, M>(
    val db: DSLContext, val query: DataSet<T, R, M>
  ) {

    fun field(name: String): FieldConfiguration<*>? {
      return query.fieldsByNameLower[name.lowercase()]
    }

    fun field(field: Field<*>): FieldConfiguration<*>? {
      return query.fields[field.name]
    }

    fun search(search: String): Data<T, R, M> {
      return copy(db = db, query = query.search(search))
    }

    fun query(): Select<R> {
      val withConditions = if (query.conditions.isNotEmpty()) {
        // Merge conditions if we have them
        val existingConditions = query.table.`$where`()
        if (existingConditions != null) {
          query.table.`$where`(DSL.and(query.conditions + existingConditions))
        } else {
          query.table.`$where`(DSL.and(query.conditions))
        }
      } else {
        query.table
      }
      val tableOrdered = if (query.order.isNotEmpty()) {
        withConditions.`$orderBy`(query.order)
      } else {
        withConditions
      }
      val tableWithOffset = if (query.offset > 0) {
        tableOrdered.`$offset`(DSL.`val`(query.offset))
      } else {
        tableOrdered
      }
      val tableWithLimit = if (query.limit > -1) {
        tableWithOffset.`$limit`(DSL.`val`(query.limit))
      } else {
        tableWithOffset
      }
      tableWithLimit.attach(db.configuration())
      return tableWithLimit
    }

    internal fun result(): Result<R> {
      return query().fetch()
    }

    override fun toString(): String {
      return query().toString()
    }

    fun first(): M? {
      val result = copy(query = query.one()).fetch()
      return if (result.isEmpty()) {
        null
      } else result[0]
    }

    fun page(page: Int, pageSize: Int): List<M> {
      return copy(query = query.page(page, pageSize)).fetch()
    }


    fun fetch(): List<M> {
      val result: Result<R> = result()
      return result.asSequence()
        .chunked(query.mapperBatchSize, query.mapper)
        .flatten()
        .toList()
    }

    fun stream(): Sequence<M> {
      return query()
        .stream()
        .asSequence()
        .chunked(query.mapperBatchSize, query.mapper)
        .flatten()
    }

    fun count(): Long {
      return db.selectCount().from(query()).fetchOne(0, Long::class.java)!!
    }

    fun empty(): Boolean {
      return copy(query = query.one()).count() != 1L
    }

    fun order(field: String, sortOrder: SortOrder, nulls: Nulls? = null): Data<T, R, M> {
      return copy(query = query.order(field, sortOrder, nulls))
    }

    fun clearOrder(): Data<T, R, M> {
      return copy(query = query.clearOrder())

    }
  }

  fun one(): DataSet<T, R, M> {
    return copy(limit = 1)
  }


  fun page(page: Int, pageSize: Int): DataSet<T, R, M> {
    return copy(offset = page * pageSize, limit = pageSize)
  }

  fun search(query: String): DataSet<T, R, M> {
    val rendered = renderSearch(query)
    if (rendered.condition == null) {
      return this
    }
    return copy(
      conditions = conditions + rendered.condition,
      lastSearchCorrected = rendered.search
    )
  }

  fun where(condition: Condition): DataSet<T, R, M> {
    return copy(
      conditions = conditions + condition
    )
  }

  fun renderSearch(search: String): SearchRendered {
    return searchConditionFactory.search(search, this)
  }

  private fun mergeCondition(
    termsCondition: Condition?, termCondition: Condition?, conjunction: Conjunction = Conjunction.OR
  ): Condition? {
    if (termCondition == null) return termsCondition
    if (termsCondition == null) return termCondition
    if (conjunction == Conjunction.AND) return DSL.and(termsCondition, termCondition)
    return DSL.or(termsCondition, termCondition)
  }

  fun clearOrder(): DataSet<T, R, M> {
    return copy(order = emptyList())
  }

  fun order(field: String, sortOrder: SortOrder?, nulls: Nulls? = null): DataSet<T, R, M> {
    val fieldConfiguration = fields[field] ?: return this

    return order(fieldConfiguration.field, sortOrder, fieldConfiguration.orderNulls ?: nulls)
  }

  fun order(field: Field<*>, order: SortOrder?, nulls: Nulls? = null): DataSet<T, R, M> {
    return copy(
      order = this.order + field.sort(order)
        .let { if (nulls == null) it else if (nulls == Nulls.FIRST) it.nullsFirst() else it.nullsLast() })
  }

  fun data(db: DSLContext): Data<T, R, M> {
    return Data(db, this)
  }

  fun offset(offset: Int): DataSet<T, R, M> {
    return copy(offset = offset)
  }

  fun limit(limit: Int): DataSet<T, R, M> {
    return copy(limit = limit)
  }

  fun <U> map(converter: (M) -> U): DataSet<T, R, U> {
    return mapBatch { it.map(converter) }
  }

  fun <U> mapBatch(converter: (List<M>) -> List<U>): DataSet<T, R, U> {
    val newMapper: (List<R>) -> List<U> = { record -> converter(mapper(record)) }
    return DataSet(
      table = table,
      mapper = newMapper,
      conditions = conditions,
      offset = offset,
      limit = limit,
      order = order,
      fields = fields,
      searches = searches,
      openSearches = openSearches,
      lastSearchCorrected = lastSearchCorrected,
      searchConditionFactory = searchConditionFactory,
    )
  }

  data class FieldConfiguration<T>(
    val field: Field<T>,
    val name: String = field.name,
    val title: String = name,
    val external: Boolean,
    val orderable: Boolean = false,
    val direction: Order.Direction? = null,
    val search: ((String) -> Condition?)? = null,
    val primaryKey: Boolean = false,
    val open: Boolean = true,
    val orderNulls: Nulls? = null,
    val mapper: Mapper<T, Any>? = null,
  ) {

    data class Mapper<A, B>(
      val type: Class<B>,
      val mapper: (A, Record) -> B
    )

    data class Builder<T>(
      val field: Field<T>,
      var name: String = field.name,
      var title: String = name,
      private var orderable: Boolean = false,
      private var direction: Order.Direction? = null,
      private var search: ((String) -> Condition?)? = null,
      var primaryKey: Boolean = false,
      private var open: Boolean = true,
      private var orderNulls: Nulls? = null,
      private var mapper: Mapper<T, Any>? = null,
      var external: Boolean = false
    ) {

      fun orderable(direction: Order.Direction? = null, nulls: Nulls? = null) {
        this.direction = direction
        orderable = true
        orderNulls = nulls
      }

      fun search(open: Boolean? = null, fn: (s: String) -> Condition?) {
        if (open != null) this.open = open
        search = fn
      }

      fun primaryKey() {
        primaryKey = true
      }

      fun <B> map(type: Class<B>, fn: (T, record: Record) -> B) {
        @Suppress("UNCHECKED_CAST")
        this.mapper = Mapper(type, fn) as Mapper<T, Any>
      }

      fun build(): FieldConfiguration<T> {
        return FieldConfiguration(
          field = field,
          name = name,
          title = title,
          orderable = orderable,
          search = search,
          primaryKey = primaryKey,
          open = open,
          direction = direction,
          orderNulls = orderNulls,
          external = external,
        )
      }
    }
  }

  data class Builder<T : Select<R>, R : Record, M>(
    var query: DataSet<T, R, M>,
    var doDetectFields: Boolean = false,
  ) {


    fun <T> field(
      field: Field<T>,
      init: (FieldConfiguration.Builder<T>.(field: Field<T>) -> Unit)? = null
    ) {
      // Ensure that the field is scoped to the outside table
      val builder = FieldConfiguration.Builder(field)

      val name = fieldName(field.name)
      builder.name = name.name
      builder.title = name.title

      init?.let { builder.it(field) }
      val built = builder.build()
      query = query.copy(
        fields = query.fields + Pair(built.name, built),
        order = query.order + (if (built.direction == null) emptyList() else listOf(
          built.field.sort(
            when (built.direction) {
              Order.Direction.ASC -> SortOrder.ASC
              Order.Direction.DESC -> SortOrder.DESC
            }
          )
        ))
      )
    }

    fun search(
      name: String,
      open: Boolean = false,
      search: (query: String) -> Condition?
    ) {
      query = query.copy(
        searches = query.searches + Pair(name, search),
        openSearches = if (open) query.openSearches + name else query.openSearches
      )
    }

    fun fields(vararg fields: Field<T>) {
      fields.map {
        field(it)
      }
    }

    fun autoDetectFields(
      db: DSLContext,
    ) {
      db.selectFrom(query.table).limit(0).fetch().recordType().fields()
        .map { field ->
          val existing = query.fields.values.firstOrNull {
            it.field.name == field.name
          }
          if (existing == null) {
            val unqualifiedName = DSL.name(field.name)
            val unqualifiedField = field.`as`(unqualifiedName)
            field(unqualifiedField) {
              external = true
            }
          }
        }
    }
  }

  // ========================================
  // HTTP Request/Response nested classes
  // ========================================

  data class Selection(
    val include: Boolean,
    val keys: List<List<String>>
  )

  data class Request(
    val search: String? = null,
    val partition: String? = null,
    val ordering: List<Order>? = null,
    val page: Int? = null,
    val pageSize: Int? = null,
    val showCounts: Boolean? = null,
    val showColumns: Boolean? = null,
    val selection: Selection? = null
  ) {
    /**
     * Apply all filters and fetch data.
     *
     * @param db Database context
     * @param query DataSet query builder
     * @param unlimit If true, returns all matching rows (for bulk operations)
     * @return Filtered data
     */
    fun <T> filter(
      db: DSLContext,
      query: DataSet<*, *, T>,
      unlimit: Boolean = false
    ): List<T> {
      return Companion.filter(db, query, this, unlimit)
    }

    /**
     * Apply all filters and return full response with counts and columns.
     *
     * @param db Database context
     * @param query DataSet query builder
     * @return Response with data, counts, columns
     */
    fun <T> query(
      db: DSLContext,
      query: DataSet<*, *, T>
    ): Response<T> {
      return Companion.query(db, query, this)
    }

    /**
     * @deprecated Use filter() or query() instead. toResponse() does not apply selection.
     */
    @Deprecated("Use filter() or query() instead", ReplaceWith("query(db, dataSet)"))
    fun <T> toResponse(
      db: DSLContext,
      dataSet: DataSet<*, *, T>
    ): Response<T> {
      return Response.fromRequest(db, dataSet, this)
    }

    fun <T : Select<R>, R : Record, M> transform(
      query: DataSet<T, R, M>,
      transform: EnumSet<RequestTransform> = EnumSet.allOf(RequestTransform::class.java),
      unlimit: Boolean = false
    ): DataSet<T, R, M> {
      return query
        .let { queryUnfiltered ->
          // Apply selection keys
          if (selection != null && transform.contains(RequestTransform.SELECTION)) {
            // Build key set
            val fields = query.fields.entries.mapNotNull { f ->
              if (f.value.primaryKey) {
                val field = f.value.field
                f.value.search ?: { s -> field.cast(String::class.java).eq(s) }
              } else {
                null
              }
            }
            // Convert keys to rows
            val condition = DSL.or(
              selection.keys.map { keyRow ->
                DSL.and(
                  keyRow.mapIndexed { index, key ->
                    fields[index](key)
                  }
                )
              }).let {
              // Apply inversion
              if (selection.include) {
                it
              } else {
                it.not()
              }
            }
            queryUnfiltered.where(condition)
          } else {
            queryUnfiltered
          }
        }
        .let {
          // Apply partition
          if (!partition.isNullOrEmpty() && transform.contains(RequestTransform.PARTITION)) {
            it.search(partition)
          } else {
            it
          }
        }
        .let {
          // Apply search
          if (!search.isNullOrEmpty() && transform.contains(RequestTransform.SEARCH)) {
            it.search(search)
          } else {
            it
          }
        }
        .let {
          // Apply ordering
          if (!ordering.isNullOrEmpty() && transform.contains(RequestTransform.ORDERING)) {
            it.clearOrder()
              .let { dataSet ->
                for (ordering in ordering) {
                  dataSet.order(
                    ordering.field,
                    if (Order.Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
                  )
                }
                dataSet
              }
          } else {
            it
          }
        }.let {
          // Apply limit and offset
          if (unlimit) {
            it.page(1, -1)
          } else if (pageSize != null && page != null) {
            it.page(page, pageSize)
          } else {
            it
          }
        }
    }
  }

  data class Response<T>(
    val data: List<T>,
    val count: Count? = null,
    val columns: List<Column>? = null,
    val searchRendered: String? = null
  ) {
    data class Count(
      val inPartition: Long,
      val inQuery: Long,
    )

    companion object {

      fun <T> fromRequest(
        db: DSLContext,
        query: DataSet<*, *, T>,
        request: Request,
        defaultPageSize: Int = 50,
      ): Response<T> {
        var dataSet = query.data(db)

        // Apply partition first
        val partition = request.partition
        if (!partition.isNullOrEmpty()) {
          dataSet = dataSet.search(partition)
        }

        // Maybe fetch counts?
        var count: Count? = null
        if (request.showCounts == true) {
          if (request.search.isNullOrEmpty()) {
            val inPartition = dataSet.count()
            count = Count(inPartition, inPartition)
          } else {
            val inPartition = dataSet.count()
            if (inPartition == 0L) {
              count = Count(0, 0)
            } else {
              dataSet = dataSet.search(request.search)
              val inQuery = dataSet.count()
              count = Count(inPartition, inQuery)
            }
          }
        } else {
          if (!request.search.isNullOrEmpty()) {
            dataSet = dataSet.search(request.search)
          }
        }

        val shouldQuery = request.showCounts != true || (count?.inQuery ?: 0) > 0

        // Apply ordering
        if (!request.ordering.isNullOrEmpty()) {
          dataSet = dataSet.clearOrder()
          for (ordering in request.ordering) {
            dataSet = dataSet.order(
              ordering.field,
              if (Order.Direction.ASC == ordering.order) SortOrder.ASC else SortOrder.DESC
            )
          }
        }
        val page = request.page ?: 0
        val pageSize = request.pageSize ?: defaultPageSize
        val data = if (shouldQuery) {
          dataSet.page(page, pageSize)
        } else {
          emptyList()
        }
        // Do we even want these columns?
        val columns = if (request.showColumns == true) {
          dataSet.query.fields.values.map { field ->
            val order = dataSet.query.order.find { it.name == field.field.name }?.order
            Column(
              name = field.name,
              title = field.title,
              type = field.field.dataType.type.simpleName,
              searchable = field.search != null,
              orderable = field.orderable,
              sortDirection = when (order) {
                null -> null
                SortOrder.ASC -> Order.Direction.ASC
                else -> Order.Direction.DESC
              },
              primaryKey = field.primaryKey,
            )
          }
        } else {
          null
        }

        return Response(
          count = count,
          columns = columns,
          data = data,
          // Only return the search if we applied a search here.
          // We ignore partitioning and sub-searches and only pay attention to the request searching
          searchRendered = if (request.search.isNullOrEmpty()) "" else dataSet.query.lastSearchCorrected
        )
      }
    }

    data class Column(
      val name: String,
      val title: String,
      val searchable: Boolean,
      val orderable: Boolean,
      val sortDirection: Order.Direction?,
      val primaryKey: Boolean,
      val type: String?,
    ) {
      override fun toString(): String {
        return name
      }
    }
  }

  data class Order(
    val field: String,
    val order: Direction
  ) {
    enum class Direction {
      ASC, DESC
    }
  }

  enum class RequestTransform {
    SEARCH, PARTITION, ORDERING, SELECTION
  }

  companion object {

    data class FieldName(
      val title: String,
      val name: String,
    )

    fun fieldName(name: String): FieldName {
      return if (name.isCamelCase()) {
        FieldName(name.camelToTitleCase(), name)
      } else if (name.isSnakeCase()) {
        FieldName(name.snakeToTitleCase(), name.snakeToCamelCase())
      } else {
        FieldName(name.titleCase(), name)
      }
    }

    /**
     * Creates a DataSet using a DSL-based builder pattern.
     *
     * This is the preferred way to construct DataSet instances. It provides
     * a clean, declarative syntax for configuring fields inline with your SELECT statement.
     *
     * Example:
     * ```kotlin
     * val query = DataSet {
     *   db.select(
     *     field(USER_ID) {
     *       primaryKey()
     *       search { f, s -> f.eq(s.toLongOrNull()) }
     *     },
     *     field(EMAIL) {
     *       orderable()
     *       search { f, s -> f.containsIgnoreCase(s) }
     *     }
     *   ).from(USER)
     * }
     * ```
     *
     * @param block Builder function that receives a DataSetContext and returns a JOOQ Select query
     * @return Configured DataSet instance ready for execution
     */
    operator fun <T : Record> invoke(
      block: DataSetContext.() -> Select<T>
    ): DataSet<Select<T>, T, T> {
      val context = DataSetContext()
      val query = block(context)
      return context.buildDataSet(query)
    }

    /**
     * Apply all request filters (search, selection, ordering, pagination) and fetch data.
     *
     * Use this when you need the actual data with all filters applied.
     * For bulk operations (delete, update), use unlimit=true to get all matching rows.
     *
     * @param db Database context
     * @param query DataSet query builder
     * @param request Request with search, selection, ordering, pagination
     * @param unlimit If true, ignores pagination and returns all matching rows
     * @return Filtered data
     */
    fun <T> filter(
      db: DSLContext,
      query: DataSet<*, *, T>,
      request: Request,
      unlimit: Boolean = false
    ): List<T> {
      val transformed = request.transform(query, unlimit = unlimit)
      return transformed.data(db).page(0, -1)
    }

    /**
     * Apply filters and return full response with counts and column metadata.
     *
     * Use this for list/table views that need pagination, counts, and column info.
     * Selection is applied if present in request.
     *
     * @param db Database context
     * @param query DataSet query builder
     * @param request Request with all filter options
     * @return Response with data, counts, columns
     */
    fun <T> query(
      db: DSLContext,
      query: DataSet<*, *, T>,
      request: Request
    ): Response<T> {
      // Apply selection/search/ordering first
      val transformed = request.transform(query, unlimit = false)
      var dataSet = transformed.data(db)

      // Apply partition first
      val partition = request.partition
      if (!partition.isNullOrEmpty()) {
        dataSet = dataSet.search(partition)
      }

      // Maybe fetch counts?
      var count: Response.Count? = null
      if (request.showCounts == true) {
        // Count after selection is applied
        val inPartition = dataSet.count()
        count = Response.Count(inPartition, inPartition)
      }

      val shouldQuery = request.showCounts != true || (count?.inQuery ?: 0) > 0
      val page = request.page ?: 0
      val pageSize = request.pageSize ?: 50

      val data = if (shouldQuery) {
        dataSet.page(page, pageSize)
      } else {
        emptyList()
      }

      // Do we even want these columns?
      val columns = if (request.showColumns == true) {
        transformed.fields.values.map { field ->
          val order = transformed.order.find { it.name == field.field.name }?.order
          Response.Column(
            name = field.name,
            title = field.title,
            type = field.field.dataType.type.simpleName,
            searchable = field.search != null,
            orderable = field.orderable,
            sortDirection = when (order) {
              null -> null
              SortOrder.ASC -> Order.Direction.ASC
              else -> Order.Direction.DESC
            },
            primaryKey = field.primaryKey,
          )
        }
      } else {
        null
      }

      return Response(
        count = count,
        columns = columns,
        data = data,
        searchRendered = if (request.search.isNullOrEmpty()) "" else transformed.lastSearchCorrected
      )
    }

    // Helper methods for building DataSets using Fields
    fun build(init: (com.iodesystems.db.query.Fields<Record>.(com.iodesystems.db.query.Fields<Record>) -> Unit)): com.iodesystems.db.query.Fields<Record> {
      return build(
        Record::class.java,
        init
      )
    }

    fun buildForMaps(init: (com.iodesystems.db.query.Fields<Map<String, *>>.(com.iodesystems.db.query.Fields<Map<String, *>>) -> Unit)): com.iodesystems.db.query.Fields<Map<String, *>> {
      @Suppress("UNCHECKED_CAST")
      return build(Map::class.java as Class<Map<String, *>>, init)
    }

    fun <T> build(
      mappedType: Class<T>,
      init: (com.iodesystems.db.query.Fields<T>.(com.iodesystems.db.query.Fields<T>) -> Unit)
    ): com.iodesystems.db.query.Fields<T> {
      return com.iodesystems.db.query.Fields(
        mappedType = mappedType,
        customMapper = null,
        fields = mutableListOf(),
        searches = mutableListOf(),
        init = init
      )
    }

  }
}

/**
 * Context for building DataSet instances using the DSL.
 *
 * Provides methods for configuring fields and searches in a fluent, declarative way.
 */
class DataSetContext {
  private val fieldConfigs = mutableListOf<DataSetFieldConfig<*>>()
  private val searches = mutableListOf<DataSetSearch>()

  /**
   * Register a named search that can be targeted in search queries.
   *
   * @param name The name to use when targeting this search (e.g., "status" allows "status:active")
   * @param open If true, this search is included in global/open searches (default: true)
   * @param search Function that converts a search string into a JOOQ Condition
   */
  fun search(name: String, open: Boolean = true, search: (query: String) -> Condition?) {
    searches.add(DataSetSearch(name, open, search))
  }

  /**
   * Configure a field with search, ordering, and other options.
   *
   * Example:
   * ```kotlin
   * field(USER_ID) {
   *   primaryKey()
   *   search { f, s -> f.eq(s.toLongOrNull()) }
   * }
   * ```
   *
   * @param jooqField The JOOQ field to configure
   * @param config Configuration block for field options
   * @return The same JOOQ field (for use in SELECT clause)
   */
  fun <F> field(jooqField: Field<F>, config: (DataSetFieldConfigBuilder<F>.() -> Unit) = {}): Field<F> {
    val configBuilder = DataSetFieldConfigBuilder(jooqField)
    config(configBuilder)
    fieldConfigs.add(configBuilder.build())
    return jooqField
  }

  internal fun <T : Record> buildDataSet(query: Select<T>): DataSet<Select<T>, T, T> {
    // Build field configurations map
    val fields = mutableMapOf<String, DataSet.FieldConfiguration<*>>()
    val defaultOrdering = mutableListOf<SortField<*>>()

    fieldConfigs.forEach { config ->
      @Suppress("UNCHECKED_CAST")
      val fieldConfig = DataSet.FieldConfiguration(
        field = config.field as Field<Any>,
        name = config.field.name,
        title = config.field.name,
        external = true,
        orderable = config.isOrderable,
        direction = config.orderDirection,
        search = config.searchFunction?.let { searchFn ->
          { searchString: String ->
            @Suppress("UNCHECKED_CAST")
            (searchFn as (Field<Any>, String) -> Condition)(config.field as Field<Any>, searchString)
          }
        },
        primaryKey = config.isPrimaryKey,
        open = config.globalSearch
      )
      fields[config.field.name] = fieldConfig

      // Apply default ordering for orderable fields
      if (config.isOrderable) {
        val sortOrder = when (config.orderDirection) {
          DataSet.Order.Direction.DESC -> SortOrder.DESC
          else -> SortOrder.ASC
        }
        defaultOrdering.add(config.field.sort(sortOrder))
      }
    }

    // Build named searches map
    val searchesMap = mutableMapOf<String, (query: String) -> Condition?>()
    searches.forEach { search ->
      searchesMap[search.name] = search.search
    }

    // Get list of open search field names
    val openSearches = searches.filter { it.open }.map { it.name }

    // Create DataSet directly
    return DataSet(
      table = query,
      mapper = { it }, // Identity mapper for Record -> Record
      fields = fields,
      searches = searchesMap,
      openSearches = openSearches,
      order = defaultOrdering
    )
  }
}

/**
 * Configuration data for a field in DataSet.
 */
data class DataSetFieldConfig<F>(
  val field: Field<F>,
  val isPrimaryKey: Boolean = false,
  val isOrderable: Boolean = false,
  val orderDirection: DataSet.Order.Direction? = null,
  val searchFunction: ((Field<F>, String) -> Condition?)? = null,
  val globalSearch: Boolean = true
)

/**
 * Builder for configuring field options in DataSet.
 */
class DataSetFieldConfigBuilder<F>(private val field: Field<F>) {
  private var isPrimaryKey = false
  private var isOrderable = false
  private var orderDirection: DataSet.Order.Direction? = null
  private var searchFunction: ((Field<F>, String) -> Condition?)? = null
  private var globalSearch = true

  /**
   * Mark this field as the primary key.
   */
  fun primaryKey() {
    isPrimaryKey = true
  }

  /**
   * Allow ordering by this field.
   *
   * @param direction Default sort direction (ASC or DESC)
   */
  fun orderable(direction: DataSet.Order.Direction = DataSet.Order.Direction.ASC) {
    isOrderable = true
    orderDirection = direction
  }

  /**
   * Make this field searchable.
   *
   * @param global If true, this field is searched in global/open searches (default: true)
   * @param fn Function that takes the field and search string and returns a Condition
   */
  fun search(global: Boolean = true, fn: (Field<F>, String) -> Condition?) {
    globalSearch = global
    searchFunction = fn
  }

  internal fun build(): DataSetFieldConfig<F> {
    return DataSetFieldConfig(field, isPrimaryKey, isOrderable, orderDirection, searchFunction, globalSearch)
  }
}

/**
 * Configuration data for a named search in DataSet.
 */
data class DataSetSearch(
  val name: String,
  val open: Boolean,
  val search: (query: String) -> Condition?
)
