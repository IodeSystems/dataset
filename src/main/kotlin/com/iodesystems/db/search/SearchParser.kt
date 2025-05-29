package com.iodesystems.db.search

import com.iodesystems.db.query.DataSetSearchLexer
import com.iodesystems.db.query.DataSetSearchParser
import com.iodesystems.db.query.DataSetSearchParser.*
import com.iodesystems.db.query.DataSetSearchParserBaseListener
import com.iodesystems.db.search.errors.InvalidSearchStringException
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException
import com.iodesystems.db.search.model.Conjunction
import com.iodesystems.db.search.model.Term
import com.iodesystems.db.search.model.TermValue
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTreeWalker

class SearchParser {
  class UnwantedTokenException(val position: Int) : Exception()
  data class ParseResult(
    val search: String, val terms: List<Term>
  )

  private fun parseInternal(search: String): ParseResult {
    val stream = CharStreams.fromString(search)
    val lexer = DataSetSearchLexer(stream)
    lexer.removeErrorListeners()
    lexer.addErrorListener(object : BaseErrorListener() {
      override fun syntaxError(
        recognizer: Recognizer<*, *>?,
        offendingSymbol: Any?,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException
      ) {
        throw UnwantedTokenException(charPositionInLine)
      }
    })
    val parser = DataSetSearchParser(CommonTokenStream(lexer))
    parser.errorHandler = object : DefaultErrorStrategy() {
      override fun reportUnwantedToken(recognizer: Parser) {
        throw UnwantedTokenException(recognizer.currentToken.stopIndex)
      }

      override fun reportError(recognizer: Parser, e: RecognitionException) {
        when (e) {
          is InputMismatchException -> {
            throw UnwantedTokenException(recognizer.currentToken.stopIndex)
          }

          is NoViableAltException -> {
            throw UnwantedTokenException(e.offendingToken.startIndex - 1)
          }

          else -> {
            throw SneakyInvalidSearchStringException(
              "Error parsing search at: " + e.offendingToken.text + " in $search", e
            )
          }
        }
      }
    }
    val walker = ParseTreeWalker()
    val listener = Listener()
    walker.walk(listener, parser.search())
    return ParseResult(search, listener.getTerms())
  }

  @Throws(InvalidSearchStringException::class)
  fun parse(search: String?): ParseResult {
    var searchString = (search ?: "").trim()
    var lastException: Exception? = null
    try {
      (1..1000).forEach { i ->
        try {
          return parseInternal(searchString)
        } catch (e: UnwantedTokenException) {
          searchString = searchString.take(e.position) + '\\' + searchString.drop(e.position)
          lastException = e
        }
      }
      throw InvalidSearchStringException(
        "Unwanted token recoveries exhausted:" + lastException?.message, lastException
      )
    } catch (e: SneakyInvalidSearchStringException) {
      throw InvalidSearchStringException(e.message, e)
    }
  }

  private inner class Listener : DataSetSearchParserBaseListener() {
    private val terms: MutableList<Term> = ArrayList()
    private var currentTerm: Term? = null
    private var currentTermValues: MutableList<TermValue>? = null
    fun getTerms(): List<Term> {
      return terms
    }

    override fun enterSimpleTerm(ctx: SimpleTermContext) {
      super.enterSimpleTerm(ctx)
      currentTermValues = mutableListOf()

      currentTerm =
        Term(
          currentTermValues!!,
          extractTarget(ctx.term().termTarget()), Conjunction.AND, ctx.term().NEGATE() != null
        )
      terms.add(currentTerm!!)
    }

    private fun extractTarget(termTarget: TermTargetContext?): String? {
      return termTarget?.text
    }

    private fun extractValue(value: TermValueContext): String? {
      value.ANY()?.let { return extractValue(it.text) }
      value.ESCAPED_CHAR()?.let { return extractValue(it.text) }
      value.STRING()?.let { return extractValue(it.text) }
      return null
    }

    private fun extractValue(value: String): String {
      var valueModified = value
      valueModified = valueModified.trim { it <= ' ' }
      return if (valueModified.isEmpty()) {
        valueModified
      } else if (valueModified.startsWith("(") && valueModified.endsWith(")") || valueModified.startsWith("\"") && valueModified.endsWith(
          "\""
        ) || valueModified.startsWith(
          "'"
        ) && valueModified.endsWith("'")
      ) {
        valueModified.substring(1, valueModified.length - 1)
      } else {
        valueModified.replace("\\'", "'").replace("\\ ", " ").replace("\\\"", "\"").replace("\\:", ":")
          .replace("\\(", "(")
          .replace("\\)", ")").replace("\\!", "!").replace("\\\\", "\\")
      }
    }

    override fun enterAndTerm(ctx: AndTermContext) {
      super.enterAndTerm(ctx)
      currentTermValues = mutableListOf()
      currentTerm = Term(
        currentTermValues!!,
        extractTarget(ctx.term().termTarget()),
        Conjunction.AND,
        ctx.term().NEGATE() != null
      )
      terms.add(currentTerm!!)
    }

    override fun enterOrTerm(ctx: OrTermContext) {
      super.enterOrTerm(ctx)
      currentTermValues = mutableListOf()
      currentTerm = Term(
        currentTermValues!!,
        extractTarget(ctx.term().termTarget()),
        Conjunction.OR,
        ctx.term().NEGATE() != null
      )
      terms.add(currentTerm!!)
    }

    private fun addTermValue(ctx: TermValueContext, conjunction: Conjunction = Conjunction.AND) {
      val value = extractValue(ctx)
      currentTermValues!!.add(
        if (value == null) {
          if (ctx.NEGATE() !== null) {
            TermValue(
              conjunction, ctx.text, false
            )
          } else {
            error("Cannot extract term value")
          }
        } else {
          TermValue(
            conjunction, value, ctx.NEGATE() != null
          )
        }
      )
    }

    override fun enterSimpleValue(ctx: SimpleValueContext) {
      super.enterSimpleValue(ctx)
      addTermValue(ctx.termValue())
    }

    override fun enterAndValue(ctx: AndValueContext) {
      super.enterAndValue(ctx)
      addTermValue(ctx.termValue())
    }

    override fun enterOrValue(ctx: OrValueContext) {
      super.enterOrValue(ctx)
      addTermValue(ctx.termValue(), Conjunction.OR)
    }

    override fun enterUnprotectedOrValue(ctx: UnprotectedOrValueContext) {
      super.enterUnprotectedOrValue(ctx)
      addTermValue(ctx.termValue(), Conjunction.OR)
    }
  }
}
