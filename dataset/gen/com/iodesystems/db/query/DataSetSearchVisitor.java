// Generated from com/iodesystems/db/query/DataSetSearch.g4 by ANTLR 4.7.1
package com.iodesystems.db.query;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DataSetSearchParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DataSetSearchVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#search}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSearch(DataSetSearchParser.SearchContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#simpleTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleTerm(DataSetSearchParser.SimpleTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#andTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndTerm(DataSetSearchParser.AndTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#orTerm}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrTerm(DataSetSearchParser.OrTermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTerm(DataSetSearchParser.TermContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#termTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermTarget(DataSetSearchParser.TermTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#termValueGroup}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermValueGroup(DataSetSearchParser.TermValueGroupContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#simpleValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleValue(DataSetSearchParser.SimpleValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#termValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermValue(DataSetSearchParser.TermValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#andValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndValue(DataSetSearchParser.AndValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#orValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrValue(DataSetSearchParser.OrValueContext ctx);
	/**
	 * Visit a parse tree produced by {@link DataSetSearchParser#unprotectedOrValue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnprotectedOrValue(DataSetSearchParser.UnprotectedOrValueContext ctx);
}