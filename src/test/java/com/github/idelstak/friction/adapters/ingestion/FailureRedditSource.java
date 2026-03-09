package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;

final class FailureRedditSource implements RedditSource {

  private final PullFailure failure;

  FailureRedditSource(PullFailure failure) {
    this.failure = failure;
  }

  @Override
  public PullResult pull() {
    return new PullResult.Failure(failure);
  }
}
