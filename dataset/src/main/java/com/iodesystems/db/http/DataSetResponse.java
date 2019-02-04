package com.iodesystems.db.http;

import com.iodesystems.db.query.Column;
import com.iodesystems.db.query.TypedQuery;
import java.util.List;
import org.jooq.SortOrder;

public class DataSetResponse<T> {

  private Long recordsFiltered;
  private Long recordsTotal;
  private List<T> data;
  private List<Column> columns;

  public static <T> DataSetResponse<T> fromRequest(
      TypedQuery<?, ?, T> dataSet, DataSetRequest request) {

    if (request.getPartition() != null && !request.getPartition().isEmpty()) {
      dataSet = dataSet.search(request.getPartition());
    }

    DataSetResponse<T> response = new DataSetResponse<>();
    response.setRecordsTotal(dataSet.count());
    response.setRecordsFiltered(response.getRecordsTotal());

    if (request.getSearch() != null && !request.getSearch().isEmpty()) {
      dataSet = dataSet.search(request.getSearch());
      response.setRecordsFiltered(dataSet.count());
    }

    if (request.getOrdering() != null) {
      for (Ordering ordering : request.getOrdering()) {
        dataSet =
            dataSet.order(
                ordering.getColumn(),
                "asc".equalsIgnoreCase(ordering.getSort()) ? SortOrder.ASC : SortOrder.DESC);
      }
    }

    response.setData(
        dataSet.page(request.getPage(), request.getPageSize() <= 0 ? 50 : request.getPageSize()));

    response.columns = dataSet.getColumns();

    return response;
  }

  public Long getRecordsFiltered() {
    return recordsFiltered;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void setRecordsFiltered(Long recordsFiltered) {
    this.recordsFiltered = recordsFiltered;
  }

  public Long getRecordsTotal() {
    return recordsTotal;
  }

  public void setRecordsTotal(Long recordsTotal) {
    this.recordsTotal = recordsTotal;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }
}
