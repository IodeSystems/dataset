package com.iodesystems.db.search.model;

import java.util.Objects;

public class TermValue {
  private final Conjunction conjunction;
  private final String value;

  public TermValue(Conjunction conjunction, String value) {
    this.conjunction = conjunction;
    this.value = value;
  }

  public Conjunction getConjunction() {
    return conjunction;
  }

  public String getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TermValue termValue = (TermValue) o;
    return getConjunction() == termValue.getConjunction()
        && Objects.equals(getValue(), termValue.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getConjunction(), getValue());
  }
}
