package com.iodesystems.db.search;

import com.iodesystems.db.query.DataSetSearchBaseListener;
import com.iodesystems.db.query.DataSetSearchLexer;
import com.iodesystems.db.query.DataSetSearchParser;
import com.iodesystems.db.query.DataSetSearchParser.AndTermContext;
import com.iodesystems.db.query.DataSetSearchParser.AndValueContext;
import com.iodesystems.db.query.DataSetSearchParser.OrTermContext;
import com.iodesystems.db.query.DataSetSearchParser.OrValueContext;
import com.iodesystems.db.query.DataSetSearchParser.SimpleTermContext;
import com.iodesystems.db.query.DataSetSearchParser.SimpleValueContext;
import com.iodesystems.db.query.DataSetSearchParser.TermTargetContext;
import com.iodesystems.db.query.DataSetSearchParser.TermValueContext;
import com.iodesystems.db.query.DataSetSearchParser.UnprotectedOrValueContext;
import com.iodesystems.db.search.errors.InvalidSearchStringException;
import com.iodesystems.db.search.errors.SneakyInvalidSearchStringException;
import com.iodesystems.db.search.model.Conjunction;
import com.iodesystems.db.search.model.Term;
import com.iodesystems.db.search.model.TermValue;
import java.util.ArrayList;
import java.util.List;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CodePointCharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class SearchParser {

  public List<Term> parse(String search) throws InvalidSearchStringException {
    try {
      CodePointCharStream stream = CharStreams.fromString(search);
      DataSetSearchLexer lexer = new DataSetSearchLexer(stream);
      lexer.addErrorListener(
          new BaseErrorListener() {

            @Override
            public void syntaxError(
                Recognizer<?, ?> recognizer,
                Object offendingSymbol,
                int line,
                int charPositionInLine,
                String msg,
                RecognitionException e) {
              throw new SneakyInvalidSearchStringException(
                  "Error lexing search at: " + e.getOffendingToken().getText(), e);
            }
          });
      DataSetSearchParser parser = new DataSetSearchParser(new CommonTokenStream(lexer));
      parser.setErrorHandler(
          new DefaultErrorStrategy() {

            @Override
            protected void reportUnwantedToken(Parser recognizer) {
              throw new SneakyInvalidSearchStringException(
                  "Unrecognized search part:" + recognizer.getCurrentToken().getText());
            }

            @Override
            public void reportError(Parser recognizer, RecognitionException e) {
              throw new SneakyInvalidSearchStringException(
                  "Error parsing search at: " + e.getOffendingToken().getText(), e);
            }
          });
      ParseTreeWalker walker = new ParseTreeWalker();
      Listener listener = new Listener();
      walker.walk(listener, parser.search());
      return listener.getTerms();
    } catch (SneakyInvalidSearchStringException e) {
      throw new InvalidSearchStringException(e.getMessage(), e);
    }
  }

  private class Listener extends DataSetSearchBaseListener {

    private List<Term> terms = new ArrayList<>();
    private Term currentTerm;
    private List<TermValue> currentTermValues;

    public List<Term> getTerms() {
      return terms;
    }

    @Override
    public void enterSimpleTerm(SimpleTermContext ctx) {
      super.enterSimpleTerm(ctx);
      currentTermValues = new ArrayList<>();
      currentTerm =
          new Term(extractTarget(ctx.term().termTarget()), Conjunction.AND, currentTermValues);
      terms.add(currentTerm);
    }

    private String extractTarget(TermTargetContext termTarget) {
      return termTarget == null ? null : termTarget.getText();
    }

    private String extractValue(TermValueContext value) {
      return extractValue(value.getText());
    }

    private String extractValue(String value) {
      value = value.trim();
      if (value.length() == 0) {
        return value;
      } else if ((value.startsWith("(") && value.endsWith(")"))
          || (value.startsWith("\"") && value.endsWith("\""))
          || (value.startsWith("'") && value.endsWith("'"))) {
        return value.substring(1, value.length() - 1);
      } else {
        return value;
      }
    }

    @Override
    public void enterAndTerm(AndTermContext ctx) {
      super.enterAndTerm(ctx);
      currentTermValues = new ArrayList<>();
      currentTerm =
          new Term(extractTarget(ctx.term().termTarget()), Conjunction.AND, currentTermValues);
      terms.add(currentTerm);
    }

    @Override
    public void enterOrTerm(OrTermContext ctx) {
      super.enterOrTerm(ctx);
      currentTermValues = new ArrayList<>();
      currentTerm =
          new Term(extractTarget(ctx.term().termTarget()), Conjunction.OR, currentTermValues);
      terms.add(currentTerm);
    }

    @Override
    public void enterSimpleValue(SimpleValueContext ctx) {
      super.enterSimpleValue(ctx);
      currentTermValues.add(new TermValue(Conjunction.AND, extractValue(ctx.termValue())));
    }

    @Override
    public void enterAndValue(AndValueContext ctx) {
      super.enterAndValue(ctx);
      currentTermValues.add(new TermValue(Conjunction.AND, extractValue(ctx.termValue())));
    }

    @Override
    public void enterOrValue(OrValueContext ctx) {
      super.enterOrValue(ctx);
      currentTermValues.add(new TermValue(Conjunction.OR, extractValue(ctx.termValue())));
    }

    @Override
    public void enterUnprotectedOrValue(UnprotectedOrValueContext ctx) {
      super.enterUnprotectedOrValue(ctx);
      currentTermValues.add(new TermValue(Conjunction.OR, extractValue(ctx.termValue())));
    }
  }
}
