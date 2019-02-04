package com.iodesystems.db.query.configuration;

import com.iodesystems.db.query.TypedQuery;
import com.iodesystems.db.search.SearchFieldConditionProvider;
import org.jooq.Field;

public class FieldConfiguration<T> {

  private final TypedQuery query;
  private final String name;
  private final Field<T> field;
  private boolean orderable;
  private SearchFieldConditionProvider<T, Field<T>> search;

  public FieldConfiguration(TypedQuery query, String name, Field<T> field, boolean addField) {
    this.query = query;
    this.name = name;
    this.field = field;
    if (addField) {
      addField();
    }
  }

  public FieldConfiguration(TypedQuery query, String name, Field<T> field) {
    this.query = query;
    this.name = name;
    this.field = field;
    addField();
  }

  public FieldConfiguration(
      TypedQuery query, String name, com.iodesystems.db.query.Field<T> field) {
    this.query = query;
    this.name = name;
    this.field = field.getField();
  }

  private void addField() {
    query.field(name, new com.iodesystems.db.query.Field<T>(name, field, search, orderable));
  }

  public FieldConfiguration<T> orderable(boolean orderable) {
    this.orderable = orderable;
    addField();
    return this;
  }

  public FieldConfiguration<T> search(
      SearchFieldConditionProvider<T, Field<T>> searchFieldConditionProvider) {
    this.search = searchFieldConditionProvider;
    addField();
    return this;
  }
}
