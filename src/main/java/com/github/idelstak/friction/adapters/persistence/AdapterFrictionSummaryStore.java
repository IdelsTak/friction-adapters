package com.github.idelstak.friction.adapters.persistence;

import com.github.idelstak.friction.core.readmodel.*;
import java.util.*;

public final class AdapterFrictionSummaryStore implements FrictionSummaryStore {

  private final InMemoryProjectionState state;

  public AdapterFrictionSummaryStore(InMemoryProjectionState state) {
    this.state = state;
  }

  @Override
  public FrictionSummaryStore save(FrictionSummary summary) {
    state.saveSummary(summary);
    return this;
  }

  @Override
  public Optional<FrictionSummary> find(String frictionId) {
    return state.findSummary(frictionId);
  }

  @Override
  public List<FrictionSummary> top(int limit) {
    return state.top(limit);
  }

  @Override
  public FrictionSummaryStore clear() {
    state.clear();
    return this;
  }
}
