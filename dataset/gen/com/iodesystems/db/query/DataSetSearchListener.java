// Generated from com/iodesystems/db/query/DataSetSearch.g4 by ANTLR 4.7.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DataSetSearchParser}.
 */
public interface DataSetSearchListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#search}.
	 * @param ctx the parse tree
	 */
	void enterSearch(DataSetSearchParser.SearchContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#search}.
	 * @param ctx the parse tree
	 */
	void exitSearch(DataSetSearchParser.SearchContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#simpleTerm}.
	 * @param ctx the parse tree
	 */
	void enterSimpleTerm(DataSetSearchParser.SimpleTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#simpleTerm}.
	 * @param ctx the parse tree
	 */
	void exitSimpleTerm(DataSetSearchParser.SimpleTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#andTerm}.
	 * @param ctx the parse tree
	 */
	void enterAndTerm(DataSetSearchParser.AndTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#andTerm}.
	 * @param ctx the parse tree
	 */
	void exitAndTerm(DataSetSearchParser.AndTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#orTerm}.
	 * @param ctx the parse tree
	 */
	void enterOrTerm(DataSetSearchParser.OrTermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#orTerm}.
	 * @param ctx the parse tree
	 */
	void exitOrTerm(DataSetSearchParser.OrTermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTerm(DataSetSearchParser.TermContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTerm(DataSetSearchParser.TermContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#termTarget}.
	 * @param ctx the parse tree
	 */
	void enterTermTarget(DataSetSearchParser.TermTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#termTarget}.
	 * @param ctx the parse tree
	 */
	void exitTermTarget(DataSetSearchParser.TermTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#termValueGroup}.
	 * @param ctx the parse tree
	 */
	void enterTermValueGroup(DataSetSearchParser.TermValueGroupContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#termValueGroup}.
	 * @param ctx the parse tree
	 */
	void exitTermValueGroup(DataSetSearchParser.TermValueGroupContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#simpleValue}.
	 * @param ctx the parse tree
	 */
	void enterSimpleValue(DataSetSearchParser.SimpleValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#simpleValue}.
	 * @param ctx the parse tree
	 */
	void exitSimpleValue(DataSetSearchParser.SimpleValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#termValue}.
	 * @param ctx the parse tree
	 */
	void enterTermValue(DataSetSearchParser.TermValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#termValue}.
	 * @param ctx the parse tree
	 */
	void exitTermValue(DataSetSearchParser.TermValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#andValue}.
	 * @param ctx the parse tree
	 */
	void enterAndValue(DataSetSearchParser.AndValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#andValue}.
	 * @param ctx the parse tree
	 */
	void exitAndValue(DataSetSearchParser.AndValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#orValue}.
	 * @param ctx the parse tree
	 */
	void enterOrValue(DataSetSearchParser.OrValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#orValue}.
	 * @param ctx the parse tree
	 */
	void exitOrValue(DataSetSearchParser.OrValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link DataSetSearchParser#unprotectedOrValue}.
	 * @param ctx the parse tree
	 */
	void enterUnprotectedOrValue(DataSetSearchParser.UnprotectedOrValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link DataSetSearchParser#unprotectedOrValue}.
	 * @param ctx the parse tree
	 */
	void exitUnprotectedOrValue(DataSetSearchParser.UnprotectedOrValueContext ctx);
}