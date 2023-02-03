// Generated from java-escape by ANTLR 4.11.1

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DataSetSearch}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 *            operations with no return type.
 */
public interface DataSetSearchVisitor<T> extends ParseTreeVisitor<T> {
    /**
     * Visit a parse tree produced by {@link DataSetSearch#search}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSearch(DataSetSearch.SearchContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#simpleTerm}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSimpleTerm(DataSetSearch.SimpleTermContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#andTerm}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAndTerm(DataSetSearch.AndTermContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#orTerm}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrTerm(DataSetSearch.OrTermContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#term}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTerm(DataSetSearch.TermContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#termTarget}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTermTarget(DataSetSearch.TermTargetContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#termValueGroup}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTermValueGroup(DataSetSearch.TermValueGroupContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#simpleValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitSimpleValue(DataSetSearch.SimpleValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#termValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitTermValue(DataSetSearch.TermValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#andValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitAndValue(DataSetSearch.AndValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#orValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitOrValue(DataSetSearch.OrValueContext ctx);

    /**
     * Visit a parse tree produced by {@link DataSetSearch#unprotectedOrValue}.
     *
     * @param ctx the parse tree
     * @return the visitor result
     */
    T visitUnprotectedOrValue(DataSetSearch.UnprotectedOrValueContext ctx);
}
