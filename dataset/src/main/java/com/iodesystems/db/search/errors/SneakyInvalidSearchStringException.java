package com.iodesystems.db.search.errors;

public class SneakyInvalidSearchStringException extends RuntimeException {

  public SneakyInvalidSearchStringException(String message, Throwable cause) {
    super(message, cause);
  }

  public SneakyInvalidSearchStringException(String message) {
    super(message);
  }
}
