// Generated from java-escape by ANTLR 4.11.1

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link DataSetSearch}.
 */
public interface DataSetSearchListener extends ParseTreeListener {
    /**
     * Enter a parse tree produced by {@link DataSetSearch#search}.
     *
     * @param ctx the parse tree
     */
    void enterSearch(DataSetSearch.SearchContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#search}.
     *
     * @param ctx the parse tree
     */
    void exitSearch(DataSetSearch.SearchContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#simpleTerm}.
     *
     * @param ctx the parse tree
     */
    void enterSimpleTerm(DataSetSearch.SimpleTermContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#simpleTerm}.
     *
     * @param ctx the parse tree
     */
    void exitSimpleTerm(DataSetSearch.SimpleTermContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#andTerm}.
     *
     * @param ctx the parse tree
     */
    void enterAndTerm(DataSetSearch.AndTermContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#andTerm}.
     *
     * @param ctx the parse tree
     */
    void exitAndTerm(DataSetSearch.AndTermContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#orTerm}.
     *
     * @param ctx the parse tree
     */
    void enterOrTerm(DataSetSearch.OrTermContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#orTerm}.
     *
     * @param ctx the parse tree
     */
    void exitOrTerm(DataSetSearch.OrTermContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#term}.
     *
     * @param ctx the parse tree
     */
    void enterTerm(DataSetSearch.TermContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#term}.
     *
     * @param ctx the parse tree
     */
    void exitTerm(DataSetSearch.TermContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#termTarget}.
     *
     * @param ctx the parse tree
     */
    void enterTermTarget(DataSetSearch.TermTargetContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#termTarget}.
     *
     * @param ctx the parse tree
     */
    void exitTermTarget(DataSetSearch.TermTargetContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#termValueGroup}.
     *
     * @param ctx the parse tree
     */
    void enterTermValueGroup(DataSetSearch.TermValueGroupContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#termValueGroup}.
     *
     * @param ctx the parse tree
     */
    void exitTermValueGroup(DataSetSearch.TermValueGroupContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#simpleValue}.
     *
     * @param ctx the parse tree
     */
    void enterSimpleValue(DataSetSearch.SimpleValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#simpleValue}.
     *
     * @param ctx the parse tree
     */
    void exitSimpleValue(DataSetSearch.SimpleValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#termValue}.
     *
     * @param ctx the parse tree
     */
    void enterTermValue(DataSetSearch.TermValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#termValue}.
     *
     * @param ctx the parse tree
     */
    void exitTermValue(DataSetSearch.TermValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#andValue}.
     *
     * @param ctx the parse tree
     */
    void enterAndValue(DataSetSearch.AndValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#andValue}.
     *
     * @param ctx the parse tree
     */
    void exitAndValue(DataSetSearch.AndValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#orValue}.
     *
     * @param ctx the parse tree
     */
    void enterOrValue(DataSetSearch.OrValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#orValue}.
     *
     * @param ctx the parse tree
     */
    void exitOrValue(DataSetSearch.OrValueContext ctx);

    /**
     * Enter a parse tree produced by {@link DataSetSearch#unprotectedOrValue}.
     *
     * @param ctx the parse tree
     */
    void enterUnprotectedOrValue(DataSetSearch.UnprotectedOrValueContext ctx);

    /**
     * Exit a parse tree produced by {@link DataSetSearch#unprotectedOrValue}.
     *
     * @param ctx the parse tree
     */
    void exitUnprotectedOrValue(DataSetSearch.UnprotectedOrValueContext ctx);
}
