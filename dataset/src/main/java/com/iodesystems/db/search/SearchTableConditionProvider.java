package com.iodesystems.db.search;

import org.jooq.Condition;
import org.jooq.Table;

public interface SearchTableConditionProvider<T extends Table<?>> {

  Condition search(String search, T table);
}
