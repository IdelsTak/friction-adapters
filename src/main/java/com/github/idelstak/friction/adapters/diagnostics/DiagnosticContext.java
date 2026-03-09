package com.github.idelstak.friction.adapters.diagnostics;

import java.time.*;

public record DiagnosticContext(String value, Instant at) {
}
