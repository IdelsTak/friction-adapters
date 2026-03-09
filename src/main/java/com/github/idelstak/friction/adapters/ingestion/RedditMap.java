package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;
import com.github.idelstak.friction.core.observation.*;

public final class RedditMap implements PayloadMap {

  @Override
  public MapResult map(RedditPayload payload) {
    var source = new ObservationSource(payload.meta().externalId().trim(), payload.meta().type().trim(), payload.meta().subreddit().trim());
    var provenance = new ObservationProvenance(payload.text().permalink().trim(), payload.text().createdAt());
    var input = new ObservationInput(payload.meta().externalId().trim(), source, provenance, payload.text().body().trim());
    return new MapResult.Mapped(input);
  }
}
