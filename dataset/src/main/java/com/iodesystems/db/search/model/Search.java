package com.iodesystems.db.search.model;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

public class Search {

  public static Condition isLong(String search, Field<Long> field) {
    try {
      return field.eq(Long.valueOf(search));
    } catch (IllegalArgumentException ignored) {
      return null;
    }
  }

  public static Condition equalsStringIgnoreCase(String search, Field<String> field) {
    return field.equalIgnoreCase(search);
  }

  public static Condition containsIgnoreCase(String search, Field<String> field) {
    return field.containsIgnoreCase(search);
  }

  public static Condition contains(String search, Field<String> field) {
    return field.contains(search);
  }

  public static Condition startsWithIgnoreCaseOrEmpty(String search, Field<String> field) {
    if (search.isEmpty()) {
      return DSL.trueCondition();
    } else {
      return field.lower().startsWith(search.toLowerCase());
    }
  }

  public static Condition startsWithIgnoreCase(String search, Field<String> field) {
    return field.lower().startsWith(search.toLowerCase());
  }

  public static Condition equalsString(String search, Field<String> field) {
    return field.startsWith(search);
  }

  public static <F> Condition eq(String s, Field<F> fField) {
    return fField.eq((F) s);
  }

  public static Condition isBool(String s, Field<Boolean> field) {
    s = s.toLowerCase();
    if (s.equals("y") || s.equals("yes") || s.equals("t") || s.equals("true") || s.equals("1")) {
      return field.eq(true);
    } else if (s.equals("n")
        || s.equals("no")
        || s.equals("f")
        || s.equals("false")
        || s.equals("0")) {
      return field.eq(false);
    }
    return null;
  }
}
