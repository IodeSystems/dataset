package com.iodesystems.db.query;

import org.jooq.Condition;

public interface ConditionProvider<T> {

  Condition conditions(T table);
}
