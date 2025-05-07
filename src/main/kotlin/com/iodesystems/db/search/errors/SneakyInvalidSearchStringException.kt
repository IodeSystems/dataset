package com.iodesystems.db.search.errors

class SneakyInvalidSearchStringException : RuntimeException {
  constructor(message: String?, cause: Throwable?) : super(message, cause)
  constructor(message: String?) : super(message)
}
