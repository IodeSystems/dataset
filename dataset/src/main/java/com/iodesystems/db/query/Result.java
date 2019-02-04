package com.iodesystems.db.query;

import com.iodesystems.db.logic.Handler;

public interface Result<M> {

  void stream(Handler<M> handler);
}
