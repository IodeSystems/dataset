package com.iodesystems.db.query;

import org.jooq.Field;

public interface FieldExtractor<T, U> {

  Field<U> fieldFrom(T table);
}
