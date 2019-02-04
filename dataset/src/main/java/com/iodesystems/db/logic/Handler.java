package com.iodesystems.db.logic;

public interface Handler<M> {

  void handle(M record);
}
