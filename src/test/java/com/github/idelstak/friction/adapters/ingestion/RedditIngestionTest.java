package com.github.idelstak.friction.adapters.ingestion;

import com.github.idelstak.friction.adapters.diagnostics.*;
import com.github.idelstak.friction.adapters.persistence.*;
import com.github.idelstak.friction.adapters.reddit.*;
import com.github.idelstak.friction.core.pipeline.*;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

final class RedditIngestionTest {

  @Test
  @DisplayName("ingestion accepts one valid payload")
  void acceptsOnePayload() {
    var report = ingest(List.of(payload("  ɓuild fails in CI  ", Optional.of(Instant.now()))));
    assertEquals(1, report.accepted(), "ingestion did not accept one valid payload");
  }

  @Test
  @DisplayName("ingestion marks duplicate payloads")
  void marksDuplicate() {
    var one = payload("dup café body", Optional.of(Instant.now()));
    var report = ingest(List.of(one, one));
    assertEquals(1, report.duplicates(), "ingestion did not mark one duplicate payload");
  }

  @Test
  @DisplayName("ingestion rejects malformed payload")
  void rejectsMalformed() {
    var report = ingest(List.of(payload("valid", Optional.empty())));
    assertEquals(1, report.rejected(), "ingestion did not reject malformed payload");
  }

  @Test
  @DisplayName("ingestion reports source failure on terminal pull failure")
  void reportsSourceFailure() {
    var report = ingest(new FailureRedditSource(new PullFailure.Terminal("bad auth", Optional.empty())));
    assertEquals(1, report.sourceFailures(), "ingestion did not report one source failure");
  }

  @Test
  @DisplayName("retry source retries until success")
  void retriesUntilSuccess() {
    var flaky = new FlakyRedditSource(2, payload("network résume", Optional.of(Instant.now())));
    var report = ingest(new RetryingRedditSource(flaky, new RetryPolicy(3, Duration.ofMillis(1)), new RecordingPause(), new InMemoryDiagnosticSink()));
    assertEquals(1, report.accepted(), "retry source did not accept payload after transient failures");
  }

  @Test
  @DisplayName("retry source performs expected retry calls")
  void retriesExpectedCalls() {
    var flaky = new FlakyRedditSource(2, payload("network résumé", Optional.of(Instant.now())));
    ingest(new RetryingRedditSource(flaky, new RetryPolicy(3, Duration.ofMillis(1)), new RecordingPause(), new InMemoryDiagnosticSink()));
    assertEquals(3, flaky.calls(), "retry source did not call source exactly three times");
  }

  private IngestionReport ingest(List<RedditPayload> payloads) {
    return ingest(new StaticRedditSource(payloads));
  }

  private IngestionReport ingest(RedditSource source) {
    var state = new InMemoryProjectionState();
    var summaries = new AdapterFrictionSummaryStore(state);
    var details = new AdapterObservationDetailStore(state);
    var pipeline = new FrictionPipeline(summaries, details, 0.85);
    var diagnostics = new InMemoryDiagnosticSink();
    var map = new ValidatingMap(new RedditMap());
    var ingestion = new RedditIngestion(source, map, pipeline, diagnostics);
    return ingestion.run();
  }

  private RedditPayload payload(String body, Optional<Instant> time) {
    var seed = UUID.randomUUID().toString();
    var meta = new RedditMeta("obs-" + seed, "post", "java");
    var text = new RedditText("https://reddit.com/r/java/" + seed, body, Optional.of("autør"), time);
    return new RedditPayload(meta, text);
  }
}
