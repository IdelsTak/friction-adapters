package com.github.idelstak.friction.adapters.diagnostics;

public record DiagnosticEvent(String code, Severity severity, String message, DiagnosticContext context) {
}
