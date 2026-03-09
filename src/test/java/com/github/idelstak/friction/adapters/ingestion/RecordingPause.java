package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;
import java.time.*;
import java.util.*;

final class RecordingPause implements Pause {

  private final List<Duration> sleeps;

  RecordingPause() {
    this.sleeps = new ArrayList<>();
  }

  @Override
  public void sleep(Duration duration) {
    sleeps.add(duration);
  }

  List<Duration> sleeps() {
    return List.copyOf(sleeps);
  }
}
