# Persistence Adapter Behavior

This document defines adapter-specific persistence expectations in `friction-adapters` for in-memory and future DB-backed implementations.

## Scope

- Applies to concrete persistence adapters only.
- Complements canonical read-model contracts in `friction-core`.
- Defines operational behavior at the persistence boundary.

## Consistency Expectations (Write/Read)

### In-Memory (MVP)

- Writes are applied synchronously within a single process.
- Reads after successful write in the same process must reflect latest state.
- State is process-local and non-durable across restarts.

### DB-Backed (Future)

- Writes are acknowledged only after durable commit.
- Read consistency target:
  - primary-path reads should be read-your-write where storage mode allows
  - eventually-consistent replicas must be documented if used
- Transaction boundaries must be explicit for multi-record projection updates.

## Idempotency for Retried Writes

- Persistence writes for projection updates must be idempotent by entity identity and version/token when available.
- Retried identical write requests must not create duplicate rows/documents.
- Upsert semantics are preferred over blind insert where identity collisions are possible.
- Duplicate write detection should emit diagnostic signals rather than silent divergence.

## Ordering Assumptions and Conflict Handling

- Event/application order should be preserved per friction identity where possible.
- Out-of-order updates must be handled by:
  - version comparison or sequence checks
  - rejecting stale writes with explicit reason
- Conflict policy must be explicit:
  - preferred: last valid version wins only with monotonic version checks
  - otherwise: reject and surface conflict for replay/repair

## Failure and Retry Semantics at Persistence Boundary

- Classify failures into:
  - transient (connection timeout, lock timeout, temporary unavailability)
  - terminal (schema mismatch, constraint violation, serialization incompatibility)
- Retry policy:
  - retry transient failures with bounded attempts and backoff
  - do not retry terminal failures automatically
- On retry exhaustion:
  - emit failure signal with context
  - avoid partial state mutation for multi-step updates

## Observability and Diagnostic Signals

Capture and expose at least:

- write success/failure counts by operation type
- retry counts and retry exhaustion events
- conflict/stale-write rejection counts
- latency percentiles for read/write operations
- queue/backlog depth where async persistence is used

Required failure context in logs/events:

- adapter name and operation type
- entity identity (friction/observation/read-model id)
- reason category (transient/terminal/conflict)
- attempt count and correlation/request id when available

## Implementation Notes

- Keep persistence concerns in adapter layer; do not leak storage-specific details into core contracts.
- Keep behavior deterministic and testable for retry, conflict, and idempotency paths.

## Non-Goals

- This document does not redefine `friction-core` read-model interfaces.
- This document does not prescribe a specific database technology.
