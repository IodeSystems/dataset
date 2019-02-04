package com.iodesystems.db.logic;

public interface Converter<FROM, TO> {

  TO from(FROM from);
}
