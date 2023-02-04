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
        val search: String,
        val terms: List<Term>
    )

    fun parseInternal(search: String): ParseResult {
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
        var searchString = search ?: ""
        var lastException: Exception? = null
        try {
            for (i in 1..1000) {
                try {
                    return parseInternal(searchString)
                } catch (e: UnwantedTokenException) {
                    searchString = searchString.take(e.position) + '\\' + searchString.drop(e.position)
                    lastException = e
                }
            }
            throw InvalidSearchStringException(
                "Unwanted token recoveries exhausted:" + lastException?.message,
                lastException
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
            currentTerm = Term(extractTarget(ctx.term().termTarget()), Conjunction.AND, currentTermValues!!)
            terms.add(currentTerm!!)
        }

        private fun extractTarget(termTarget: TermTargetContext?): String? {
            return termTarget?.text
        }

        private fun extractValue(value: TermValueContext): String {
            return extractValue(value.text)
        }

        private fun extractValue(value: String): String {
            var value = value
            value = value.trim { it <= ' ' }
            return if (value.length == 0) {
                value
            } else if (value.startsWith("(") && value.endsWith(")") || value.startsWith("\"") && value.endsWith("\"") || value.startsWith(
                    "'"
                ) && value.endsWith("'")
            ) {
                value.substring(1, value.length - 1)
            } else {
                value
            }
        }

        override fun enterAndTerm(ctx: AndTermContext) {
            super.enterAndTerm(ctx)
            currentTermValues = mutableListOf()
            currentTerm = Term(extractTarget(ctx.term().termTarget()), Conjunction.AND, currentTermValues!!)
            terms.add(currentTerm!!)
        }

        override fun enterOrTerm(ctx: OrTermContext) {
            super.enterOrTerm(ctx)
            currentTermValues = mutableListOf()
            currentTerm = Term(extractTarget(ctx.term().termTarget()), Conjunction.OR, currentTermValues!!)
            terms.add(currentTerm!!)
        }

        override fun enterSimpleValue(ctx: SimpleValueContext) {
            super.enterSimpleValue(ctx)
            currentTermValues!!.add(TermValue(Conjunction.AND, extractValue(ctx.termValue())))
        }

        override fun enterAndValue(ctx: AndValueContext) {
            super.enterAndValue(ctx)
            currentTermValues!!.add(TermValue(Conjunction.AND, extractValue(ctx.termValue())))
        }

        override fun enterOrValue(ctx: OrValueContext) {
            super.enterOrValue(ctx)
            currentTermValues!!.add(TermValue(Conjunction.OR, extractValue(ctx.termValue())))
        }

        override fun enterUnprotectedOrValue(ctx: UnprotectedOrValueContext) {
            super.enterUnprotectedOrValue(ctx)
            currentTermValues!!.add(TermValue(Conjunction.OR, extractValue(ctx.termValue())))
        }
    }
}
