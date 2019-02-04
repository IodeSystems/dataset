package com.iodesystems.db.search.model;

import com.iodesystems.db.search.SearchConditionProvider;

public class TargetedSearch {

  private final String name;
  private final SearchConditionProvider searchConditionProvider;

  public TargetedSearch(String name, SearchConditionProvider searchConditionProvider) {
    this.name = name;
    this.searchConditionProvider = searchConditionProvider;
  }

  public String getName() {
    return name;
  }

  public SearchConditionProvider getSearchConditionProvider() {
    return searchConditionProvider;
  }
}
