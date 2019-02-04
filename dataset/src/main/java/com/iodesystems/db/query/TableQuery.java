package com.iodesystems.db.query;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Table;

public class TableQuery<TABLE extends Table<RECORD>, RECORD extends Record>
    extends TypedQuery<TABLE, RECORD, RECORD> {

  public TableQuery(DSLContext db, TABLE table) {
    super(db, table, r -> r);
  }

  public TableQuery(TypedQuery<TABLE, RECORD, RECORD> clone) {
    super(clone);
  }
}
