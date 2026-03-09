package com.github.idelstak.friction.adapters.diagnostics;

public interface DiagnosticSink {

  void emit(DiagnosticEvent event);
}
