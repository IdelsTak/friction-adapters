package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.diagnostics.*;
import com.github.idelstak.friction.adapters.reddit.*;
import com.github.idelstak.friction.core.pipeline.*;
import java.time.*;

public final class RedditIngestion implements Ingestion {

  private final RedditSource source;
  private final PayloadMap map;
  private final FrictionPipeline pipeline;
  private final DiagnosticSink diagnostics;

  public RedditIngestion(RedditSource source, PayloadMap map, FrictionPipeline pipeline, DiagnosticSink diagnostics) {
    this.source = source;
    this.map = map;
    this.pipeline = pipeline;
    this.diagnostics = diagnostics;
  }

  @Override
  public IngestionReport run() {
    return switch (source.pull()) {
      case PullResult.Failure failure -> fail(failure);
      case PullResult.Success success -> ingest(success);
    };
  }

  private IngestionReport fail(PullResult.Failure failure) {
    diagnostics.emit(new DiagnosticEvent("reddit.pull.failed", new Severity.Error(), failure.error().message(), new DiagnosticContext(kind(failure.error()), Instant.now())));
    return new IngestionReport(0, 0, 0, 0).fail();
  }

  private IngestionReport ingest(PullResult.Success success) {
    var report = new IngestionReport(0, 0, 0, 0);
    for (var payload : success.payloads()) {
      report = update(report, payload);
    }
    return report;
  }

  private IngestionReport update(IngestionReport report, RedditPayload payload) {
    return switch (map.map(payload)) {
      case MapResult.Rejected rejected -> reject(report, payload, rejected);
      case MapResult.Mapped mapped -> accept(report, payload, mapped);
    };
  }

  private IngestionReport reject(IngestionReport report, RedditPayload payload, MapResult.Rejected rejected) {
    diagnostics.emit(new DiagnosticEvent("reddit.payload.rejected", new Severity.Error(), rejected.reason(), new DiagnosticContext(payload.meta().externalId(), Instant.now())));
    return report.reject();
  }

  private IngestionReport accept(IngestionReport report, RedditPayload payload, MapResult.Mapped mapped) {
    return switch (pipeline.ingest(mapped.input()).outcome()) {
      case IngestOutcome.Accepted _ -> report.accept();
      case IngestOutcome.DuplicateDetected _ -> report.duplicate();
      case IngestOutcome.ObservationRejected rejected -> rejectInput(report, payload, rejected);
    };
  }

  private IngestionReport rejectInput(IngestionReport report, RedditPayload payload, IngestOutcome.ObservationRejected rejected) {
    diagnostics.emit(new DiagnosticEvent("reddit.input.rejected", new Severity.Error(), rejected.reason(), new DiagnosticContext(payload.meta().externalId(), Instant.now())));
    return report.reject();
  }

  private String kind(PullFailure error) {
    return switch (error) {
      case PullFailure.Transient _ -> "transient";
      case PullFailure.Terminal _ -> "terminal";
    };
  }
}
