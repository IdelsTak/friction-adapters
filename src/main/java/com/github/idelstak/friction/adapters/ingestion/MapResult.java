package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.core.observation.*;

public sealed interface MapResult {

  record Mapped(ObservationInput input) implements MapResult {
  }

  record Rejected(String reason) implements MapResult {
  }
}
