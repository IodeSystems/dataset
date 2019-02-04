package com.iodesystems.db.search.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Term {

  private final String target;
  private final List<TermValue> values;
  private final Conjunction conjunction;

  public Term(String target, Conjunction conjunction, String value, String... extra) {
    this(target, conjunction, convert(value, extra));
  }

  public Term(Conjunction conjunction, String value, String... extra) {
    this(null, conjunction, convert(value, extra));
  }

  public Term(String value, String... extra) {
    this(null, Conjunction.AND, convert(value, extra));
  }

  public Term(String target, Conjunction conjunction, List<TermValue> values) {
    this.target = target;
    this.conjunction = conjunction;
    this.values = values;
  }

  private static List<TermValue> convert(String value, String... extra) {
    ArrayList<TermValue> termValues = new ArrayList<>();
    termValues.add(new TermValue(Conjunction.AND, value));
    for (String s : extra) {
      termValues.add(new TermValue(Conjunction.AND, s));
    }

    return termValues;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Term term = (Term) o;
    return Objects.equals(getTarget(), term.getTarget())
        && Objects.equals(getValues(), term.getValues())
        && getConjunction() == term.getConjunction();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getTarget(), getValues(), getConjunction());
  }

  public Conjunction getConjunction() {
    return conjunction;
  }

  public String getTarget() {
    return target;
  }

  public List<TermValue> getValues() {
    return values;
  }
}
