package com.iodesystems.db.query;

public class Order {

  private String field;
  private Direction order;

  public String getField() {
    return field;
  }

  public Direction getOrder() {
    return order;
  }

  public Order() {}

  public Order(String field, Direction order) {
    this.field = field;
    this.order = order;
  }

  public enum Direction {
    ASC,
    DESC
  }
}
