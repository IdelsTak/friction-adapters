package com.github.idelstak.friction.adapters.reddit;

import java.time.*;

public final class ThreadPause implements Pause {

  @Override
  public void sleep(Duration duration) {
    try {
      Thread.sleep(duration.toMillis());
    } catch (InterruptedException interrupted) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("sleep interrupted", interrupted);
    }
  }
}
