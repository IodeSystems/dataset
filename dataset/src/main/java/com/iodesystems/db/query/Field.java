package com.iodesystems.db.query;

import com.iodesystems.db.search.SearchConditionProvider;
import com.iodesystems.db.search.SearchFieldConditionProvider;

public class Field<F> {

  private final String name;
  private final org.jooq.Field<F> field;
  private final SearchConditionProvider search;
  private final boolean orderable;

  public Field(
      String name,
      org.jooq.Field<F> field,
      SearchFieldConditionProvider<F, org.jooq.Field<F>> search,
      boolean orderable) {
    this.name = name;
    this.field = field;
    this.orderable = orderable;
    if (search == null) {
      this.search = null;
    } else {
      this.search = s -> search.search(s, field);
    }
  }

  public String getName() {
    return name;
  }

  public org.jooq.Field<F> getField() {
    return field;
  }

  public SearchConditionProvider getSearch() {
    return search;
  }

  public boolean isOrderable() {
    return orderable;
  }
}
