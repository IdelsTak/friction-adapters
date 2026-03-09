package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;

public final class ValidatingMap implements PayloadMap {

  private final PayloadMap origin;

  public ValidatingMap(PayloadMap origin) {
    this.origin = origin;
  }

  @Override
  public MapResult map(RedditPayload payload) {
    if (blank(payload.meta().externalId())) {
      return new MapResult.Rejected("missing externalId");
    }
    if (blank(payload.meta().type())) {
      return new MapResult.Rejected("missing type");
    }
    if (blank(payload.meta().subreddit())) {
      return new MapResult.Rejected("missing subreddit");
    }
    if (blank(payload.text().permalink())) {
      return new MapResult.Rejected("missing permalink");
    }
    if (blank(payload.text().body())) {
      return new MapResult.Rejected("missing body");
    }
    if (payload.text().createdAt().isEmpty()) {
      return new MapResult.Rejected("missing timestamp");
    }
    return origin.map(payload);
  }

  private boolean blank(String text) {
    return text == null || text.isBlank();
  }
}
