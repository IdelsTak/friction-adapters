package com.github.idelstak.friction.adapters.reddit;

import java.time.*;

public record RetryPolicy(int maxAttempts, Duration initialBackoff) {

  public Duration backoff(int attempt) {
    var factor = 1L << Math.max(0, attempt - 1);
    return initialBackoff.multipliedBy(factor);
  }
}
