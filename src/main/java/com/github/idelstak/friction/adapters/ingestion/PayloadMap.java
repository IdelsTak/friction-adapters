package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.reddit.*;

public interface PayloadMap {

  MapResult map(RedditPayload payload);
}
