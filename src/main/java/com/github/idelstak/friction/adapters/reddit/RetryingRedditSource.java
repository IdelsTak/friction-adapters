package com.github.idelstak.friction.adapters.reddit;

import com.github.idelstak.friction.adapters.diagnostics.*;
import java.time.*;

public final class RetryingRedditSource implements RedditSource {

  private final RedditSource source;
  private final RetryPolicy policy;
  private final Pause pause;
  private final DiagnosticSink diagnostics;

  public RetryingRedditSource(RedditSource source, RetryPolicy policy, Pause pause, DiagnosticSink diagnostics) {
    this.source = source;
    this.policy = policy;
    this.pause = pause;
    this.diagnostics = diagnostics;
  }

  @Override
  public PullResult pull() {
    var current = source.pull();
    for (var attempt = 1; attempt < policy.maxAttempts(); attempt++) {
      if (!(current instanceof PullResult.Failure(PullFailure.Transient transientError))) {
        return current;
      }
      diagnostics.emit(new DiagnosticEvent("reddit.pull.retry", new Severity.Warn(), transientError.message(), new DiagnosticContext("attempt=" + attempt, Instant.now())));
      pause.sleep(policy.backoff(attempt));
      current = source.pull();
    }
    if (current instanceof PullResult.Failure(PullFailure.Transient exhausted)) {
      diagnostics.emit(new DiagnosticEvent("reddit.pull.retry.exhausted", new Severity.Error(), exhausted.message(), new DiagnosticContext("maxAttempts=" + policy.maxAttempts(), Instant.now())));
    }
    return current;
  }
}
