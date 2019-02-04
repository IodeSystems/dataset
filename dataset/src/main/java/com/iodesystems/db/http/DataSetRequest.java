package com.iodesystems.db.http;

import java.util.ArrayList;
import java.util.List;

public class DataSetRequest {

  private String search;
  private String partition;
  private int page;
  private int pageSize;

  public void setPartition(String partition) {
    this.partition = partition;
  }

  public void setOrdering(List<Ordering> ordering) {
    this.ordering = ordering;
  }

  private List<Ordering> ordering = new ArrayList<>();

  public List<Ordering> getOrdering() {
    return ordering;
  }

  public String getSearch() {
    return search;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public String getPartition() {
    return partition;
  }

  public int getPage() {
    return page;
  }

  public void setPage(int page) {
    this.page = page;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
}
