package com.iodesystems.db;

import static org.jooq.impl.DSL.field;
import static org.junit.Assert.assertEquals;

import com.iodesystems.db.search.SearchParser;
import com.iodesystems.db.search.errors.InvalidSearchStringException;
import com.iodesystems.db.search.model.Conjunction;
import com.iodesystems.db.search.model.Search;
import com.iodesystems.db.search.model.Term;
import com.iodesystems.fn.Fn;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.h2.jdbcx.JdbcConnectionPool;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultDSLContext;
import org.junit.Test;

public class TypedQueryTest {

  @Test
  public void testSearchParserConformance() throws InvalidSearchStringException {
    SearchParser searchParser = new SearchParser();
    searchParser.parse("A");
    searchParser.parse(" A ");
    searchParser.parse(" A B");
    searchParser.parse(" A B ");
    searchParser.parse(" A B, ");
    searchParser.parse(" A B ,");
    searchParser.parse(" A B , C");
    searchParser.parse(" A B , C D ");
    searchParser.parse(" A B , C D E:F");
    searchParser.parse(" A B , C D E: F");
    searchParser.parse(" A B , C D E: F G");
    searchParser.parse(" A B , C D E: F G H:I,J");
    searchParser.parse(" A B , C D E: F G H:I,J \"M N\" O:()");
    searchParser.parse(" A B , C D E: F G H:I,J \"M N\" O:(P Q,R)");
  }

  @Test
  public void testSearchParser() throws InvalidSearchStringException {
    SearchParser searchParser = new SearchParser();
    assertEquals(Fn.list(new Term("A")), searchParser.parse("A"));
    assertEquals(Fn.list(new Term("A"), new Term("B")), searchParser.parse("A B"));
    assertEquals(Fn.list(new Term("A"), new Term("B")), searchParser.parse("A B"));
    assertEquals(
        Fn.list(new Term("A"), new Term("B"), new Term(Conjunction.OR, "C")),
        searchParser.parse("A B , C"));
    assertEquals(
        Fn.list(
            new Term("A"),
            new Term("B"),
            new Term(Conjunction.OR, "C"),
            new Term("target", Conjunction.AND, "Y")),
        searchParser.parse("A B , C target:Y"));
  }

  @Test(expected = InvalidSearchStringException.class)
  public void testBadSearch() throws InvalidSearchStringException {
    new SearchParser().parse(":");
  }

  @Test
  public void testSearching() {
    DefaultDSLContext db =
        new DefaultDSLContext(JdbcConnectionPool.create("jdbc:h2:mem:", "sa", "sa"), SQLDialect.H2);
    Table<Record> TABLE = DSL.table("TEST_TABLE");
    Field<Integer> TABLE_ID = field("ID", Integer.class);
    Field<String> TABLE_NAME = field("NAME", String.class);
    Field<LocalDateTime> TABLE_CREATED_AT = field("CREATED_AT", LocalDateTime.class);
    db.createTable(TABLE).column(TABLE_ID).column(TABLE_NAME).column(TABLE_CREATED_AT).execute();

    db.insertInto(TABLE)
        .set(TABLE_ID, 1)
        .set(TABLE_NAME, "DERP")
        .set(TABLE_CREATED_AT, LocalDateTime.now())
        .newRecord()
        .set(TABLE_ID, 2)
        .set(TABLE_NAME, "DERPY DOO")
        .set(TABLE_CREATED_AT, LocalDateTime.now().plusDays(1))
        .newRecord()
        .set(TABLE_ID, 3)
        .set(TABLE_NAME, "HERPY")
        .set(TABLE_CREATED_AT, LocalDateTime.now().minusDays(1))
        .newRecord()
        .set(TABLE_ID, 4)
        .set(TABLE_NAME, "DERPY BOOBER 1")
        .set(TABLE_CREATED_AT, LocalDateTime.now())
        .execute();

    SqlQuery<Record> query =
        new SqlQuery<Record>(db, "SELECT * FROM TEST_TABLE", r -> r) {
          {
            field(TABLE_ID).orderable(true).search(Search::eq);
            field(TABLE_NAME).search(Search::contains);
            field(TABLE_CREATED_AT)
                .search(
                    (search, field) -> {
                      if ("today".equalsIgnoreCase(search)) {
                        return DSL.trunc(field).cast(LocalDate.class).eq(LocalDate.now());
                      } else {
                        return null;
                      }
                    });
            search("testSearch", (search, table) -> DSL.value("abc").eq(search));
          }
        };

    // Test searching fields
    assertEquals(0, query.search("asdf").count());
    assertEquals(4, query.search("E").count());
    assertEquals(2, query.search("OO").count());
    assertEquals(3, query.search("DERPY,HERPY").count());
    assertEquals(3, query.search("DERPY , HERPY").count());
    assertEquals(3, query.search(" DERPY , HERPY ").count());
    assertEquals(1, query.search("DERPY DOO").count());
    assertEquals(2, query.search("1").count());

    // Test targeted search
    assertEquals(0, query.search("abc").count());
    assertEquals(0, query.search("testSearch:1").count());
    assertEquals(4, query.search("testSearch:abc").count());

    // Test targeted search on fields
    assertEquals(1, query.search("ID:1").count());
    assertEquals(3, query.search("ID:(1,2,3)").count());
    assertEquals(0, query.search("ID:(1 3)").count());
    assertEquals(2, query.search("today").count());
    assertEquals(2, query.search("CREATED_AT:today").count());
    assertEquals(1, query.search("CREATED_AT:today ID:1").count());
    assertEquals(3, query.search("CREATED_AT:today, ID:3").count());
  }
}
