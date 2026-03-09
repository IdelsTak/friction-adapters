package com.github.idelstak.friction.adapters.diagnostics;

public sealed interface Severity {

  record Info() implements Severity {
  }

  record Warn() implements Severity {
  }

  record Error() implements Severity {
  }
}
