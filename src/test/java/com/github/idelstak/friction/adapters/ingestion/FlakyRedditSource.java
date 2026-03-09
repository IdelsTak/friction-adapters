package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;
import java.util.*;

final class FlakyRedditSource implements RedditSource {

  private final int failures;
  private final RedditPayload payload;
  private int calls;

  FlakyRedditSource(int failures, RedditPayload payload) {
    this.failures = failures;
    this.payload = payload;
    this.calls = 0;
  }

  @Override
  public PullResult pull() {
    calls++;
    if (calls <= failures) {
      return new PullResult.Failure(new PullFailure.Transient("timeout", Optional.empty()));
    }
    return new PullResult.Success(List.of(payload));
  }

  int calls() {
    return calls;
  }
}
