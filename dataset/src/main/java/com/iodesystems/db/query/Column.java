package com.iodesystems.db.query;

import com.iodesystems.db.query.Order.Direction;

public class Column {

  private final String name;
  private final boolean searchable;
  private final boolean orderable;

  private final Direction sortDirection;

  public Column(String name, boolean searchable, boolean orderable, Direction sortDirection) {
    this.name = name;
    this.searchable = searchable;
    this.orderable = orderable;
    this.sortDirection = sortDirection;
  }

  public boolean isSearchable() {
    return searchable;
  }

  public boolean isOrderable() {
    return orderable;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }

  public Direction getSortDirection() {
    return sortDirection;
  }
}
