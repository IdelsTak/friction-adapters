package com.github.idelstak.friction.adapters.diagnostics;

import java.util.*;

public final class InMemoryDiagnosticSink implements DiagnosticSink, DiagnosticFeed {

  private final List<DiagnosticEvent> events;

  public InMemoryDiagnosticSink() {
    events = new ArrayList<>();
  }

  @Override
  public void emit(DiagnosticEvent event) {
    events.add(event);
  }

  @Override
  public List<DiagnosticEvent> events() {
    return List.copyOf(events);
  }
}
