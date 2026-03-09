package com.github.idelstak.friction.adapters.persistence;

import com.github.idelstak.friction.core.readmodel.*;
import java.util.*;

public final class InMemoryProjectionState {

  private final Map<String, FrictionSummary> summaries = new HashMap<>();
  private final Map<String, ObservationDetail> detailsByObservation = new HashMap<>();
  private final Map<String, List<String>> frictionToObservationIds = new HashMap<>();

  public synchronized void saveSummary(FrictionSummary summary) {
    var current = summaries.get(summary.frictionId());
    if (current == null || summary.activity().prevalence() >= current.activity().prevalence()) {
      summaries.put(summary.frictionId(), summary);
    }
  }

  public synchronized Optional<FrictionSummary> findSummary(String frictionId) {
    return Optional.ofNullable(summaries.get(frictionId));
  }

  public synchronized List<FrictionSummary> top(int limit) {
    var ordered = new ArrayList<>(summaries.values());
    ordered.sort(Comparator.<FrictionSummary>comparingInt(one -> one.activity().prevalence()).reversed());
    return ordered.subList(0, Math.min(limit, ordered.size()));
  }

  public synchronized void saveDetail(ObservationDetail detail) {
    if (detailsByObservation.containsKey(detail.observationId())) {
      return;
    }
    detailsByObservation.put(detail.observationId(), detail);
    frictionToObservationIds.computeIfAbsent(detail.frictionId(), _ -> new ArrayList<>()).add(detail.observationId());
  }

  public synchronized List<ObservationDetail> byFriction(String frictionId) {
    var ids = frictionToObservationIds.getOrDefault(frictionId, List.of());
    var details = new ArrayList<ObservationDetail>();
    for (var id : ids) {
      var detail = detailsByObservation.get(id);
      if (detail != null) {
        details.add(detail);
      }
    }
    return details;
  }

  public synchronized void clear() {
    summaries.clear();
    detailsByObservation.clear();
    frictionToObservationIds.clear();
  }
}
