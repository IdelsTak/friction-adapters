package com.github.idelstak.friction.adapters.persistence;

import com.github.idelstak.friction.core.readmodel.*;
import java.util.*;

public final class AdapterObservationDetailStore implements ObservationDetailStore {

  private final InMemoryProjectionState state;

  public AdapterObservationDetailStore(InMemoryProjectionState state) {
    this.state = state;
  }

  @Override
  public ObservationDetailStore save(ObservationDetail detail) {
    state.saveDetail(detail);
    return this;
  }

  @Override
  public List<ObservationDetail> byFriction(String frictionId) {
    return state.byFriction(frictionId);
  }

  @Override
  public ObservationDetailStore clear() {
    state.clear();
    return this;
  }
}
