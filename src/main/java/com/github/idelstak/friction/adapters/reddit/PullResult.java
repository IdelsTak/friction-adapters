package com.github.idelstak.friction.adapters.reddit;

import java.util.*;

public sealed interface PullResult {

  record Success(List<RedditPayload> payloads) implements PullResult {
  }

  record Failure(PullFailure error) implements PullResult {
  }
}
