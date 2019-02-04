package com.iodesystems.db.search;

import org.jooq.Condition;

public interface SearchConditionProvider {

  Condition search(String search);
}
