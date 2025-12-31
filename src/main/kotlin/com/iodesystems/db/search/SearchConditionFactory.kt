package com.iodesystems.db.search

import com.iodesystems.db.DataSet
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import org.jooq.Condition
import org.jooq.impl.DSL

/**
 * Factory responsible for producing jOOQ Conditions from a search string.
 * It encapsulates the SearchParser and uses a SearchConditionContext to resolve
 * targeted and open search providers.
 */
class SearchConditionFactory {
  private val searchParser = SearchParser()

  fun interface SearchProvider {
    fun search(query: String): Condition?
  }

  interface SearchConditionContext {
    fun getSearchProvider(name: String): SearchProvider?
    fun getOpenSearchProviders(): Collection<SearchProvider>
  }

  fun search(search: String, context: SearchConditionContext): DataSet.SearchRendered {
    val searchConditions = mutableListOf<Condition>()
    try {
      var termsCondition: Condition? = null
      val searchParsed = searchParser.parse(search)
      for (term in searchParsed.terms) {
        val conditionProvider = term.target?.lowercase()?.let { target ->
          context.getSearchProvider(target)
        }
        var termCondition: Condition? = null
        for (value in term.values) {
          if (conditionProvider != null) {
            val targetedSearchCondition = conditionProvider.search(value.value)
            termCondition = if (termCondition == null) {
              targetedSearchCondition
            } else if (value.conjunction == Conjunction.AND) {
              DSL.and(termCondition, targetedSearchCondition)
            } else {
              DSL.or(termCondition, targetedSearchCondition)
            }
            continue
          }

          // No targeted search, search all open field providers
          var fieldsCondition: Condition? = null
          for (openSearchProvider in context.getOpenSearchProviders()) {
            val searchCondition = openSearchProvider.search(value.value)
            fieldsCondition = fieldsCondition?.or(searchCondition) ?: searchCondition
          }

          // Negate the condition if necessary
          val maybeNegated = if (value.negated) fieldsCondition?.not() else fieldsCondition
          termCondition = if (maybeNegated == null) termCondition else if (termCondition == null) maybeNegated else if (value.conjunction == Conjunction.AND) DSL.and(termCondition, maybeNegated) else DSL.or(termCondition, maybeNegated)
        }
        if (term.negated) termCondition = termCondition?.not()
        termsCondition = if (termCondition == null) termsCondition else if (termsCondition == null) termCondition else if (term.conjunction == Conjunction.AND) DSL.and(termsCondition, termCondition) else DSL.or(termsCondition, termCondition)
      }
      if (termsCondition != null) {
        searchConditions.add(termsCondition)
      }
      return DataSet.SearchRendered(searchParsed.search, DSL.or(searchConditions))
    } catch (e: InvalidSearchStringException) {
      throw SneakyInvalidSearchStringException(
        "Invalid search while building conditions:" + e.message, e
      )
    }
  }
}
