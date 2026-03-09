package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;
import java.util.*;

final class StaticRedditSource implements RedditSource {

  private final List<RedditPayload> payloads;

  StaticRedditSource(List<RedditPayload> payloads) {
    this.payloads = payloads;
  }

  @Override
  public PullResult pull() {
    return new PullResult.Success(payloads);
  }
}
