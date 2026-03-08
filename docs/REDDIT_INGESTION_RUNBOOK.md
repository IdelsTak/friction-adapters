# Reddit Ingestion Mapping and Runbook

This document defines adapter-specific behavior for Reddit ingestion in `friction-adapters`.

## Scope

- Applies to the Reddit source adapter implementation only.
- Complements canonical contracts in `friction-core` without redefining them.

## Ingestion Flow Overview

1. Resolve source configuration (`subreddit`, filters, polling interval, auth if required).
2. Pull Reddit posts/comments for configured source scope.
3. Normalize payload into adapter-level intermediate shape.
4. Validate required fields and reject malformed payloads.
5. Map normalized payload to `IngestionRecord` contract fields.
6. Emit mapped records to downstream pipeline.
7. Emit failure/duplicate metadata for projection and monitoring paths.

## Payload Mapping and Normalization Rules

- Normalize textual fields:
  - trim surrounding whitespace
  - preserve original content semantics
  - keep source permalink as canonical provenance URI
- Normalize timestamps to UTC instants.
- Preserve stable identifiers from Reddit payload for dedupe checks.
- Keep adapter metadata explicit (subreddit, author handle when present, post/comment type).
- Reject payloads missing mandatory mapping fields:
  - source identifier
  - content body or equivalent textual payload
  - timestamp/permalink required for provenance

## Rate Limit and Retry/Backoff Policy

- Respect Reddit API/client rate limits at adapter boundary.
- Use bounded retries for transient failures only.
- Recommended retry policy:
  - max attempts: 3
  - backoff: exponential with jitter
  - stop retrying on validation/schema errors
- Surface retry exhaustion as explicit failure events with context.

## Failure Modes and Handling Guidance

### Common Failure Modes

- Authentication/authorization failures
- Rate-limit responses
- Network timeout or temporary connectivity failures
- Payload schema drift or missing required fields
- Duplicate records detected by stable external IDs

### Handling Guidance

- Fail fast on malformed payloads and include field-level context.
- Retry only transient transport/rate-limit failures.
- Do not swallow exceptions; enrich and propagate with adapter context.
- Emit duplicate detection outcomes without reprocessing downstream.

## Operational Debugging Checklist

- Confirm source config values (subreddit, filters, polling interval).
- Validate auth/token presence and freshness.
- Check rate-limit telemetry and retry counters.
- Inspect recent ingestion failures by type (auth, network, schema, duplicate).
- Validate mapping outputs for a sample payload (timestamp, URI, content, IDs).
- Verify downstream sees expected `IngestionRecord` volume and ordering.

## Non-Goals

- This doc does not redefine `friction-core` SPI/event contracts.
- This doc does not define UI or projection-layer behavior.
