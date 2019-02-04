package com.iodesystems.db.search;

import org.jooq.Condition;
import org.jooq.Field;

public interface SearchFieldConditionProvider<K, T extends Field<K>> {

  Condition search(String search, T field);
}
