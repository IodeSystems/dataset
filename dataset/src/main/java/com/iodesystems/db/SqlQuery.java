package com.iodesystems.db;

import com.iodesystems.db.query.TypedQuery;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.Table;
import org.jooq.impl.DSL;

public class SqlQuery<T> extends TypedQuery<Table<Record>, Record, T> {

  public SqlQuery(DSLContext db, String sql, RecordMapper<Record, T> mapper) {
    super(db, DSL.table("(" + sql + ")"), mapper);
  }

  public SqlQuery(DSLContext db, Table<Record> table, Class<T> mapTo) {
    super(db, table, mapTo);
  }
}
