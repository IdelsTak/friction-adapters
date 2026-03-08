# friction-adapters Docs

This repo keeps adapter-specific documentation only. Shared architecture/product docs are canonical in `friction-core`.

## Canonical Shared Docs

- [Product and Domain Overview](https://github.com/idelstak/friction-core/blob/master/docs/friction-technical-and-conceptual-overview.md)
- [Design Decisions / ADR Source](https://github.com/idelstak/friction-core/blob/master/docs/DECISIONS.md)
- [SPI Contracts](https://github.com/idelstak/friction-core/blob/master/docs/SPI.md)
- [Read Model Contracts](https://github.com/idelstak/friction-core/blob/master/docs/READ_MODELS.md)
- [Repo Boundaries](https://github.com/idelstak/friction-core/blob/master/docs/REPO_BOUNDARIES.md)
- [Versioning Policy](https://github.com/idelstak/friction-core/blob/master/docs/VERSIONING.md)

## Adapter-Specific Docs to Add/Keep

Add adapter-owned docs in this folder for:

- Source-specific ingestion behavior (Reddit now, others later)
- Data mapping and normalization rules
- Persistence adapter behavior and constraints
- Operational runbooks (retries, backoff, error handling, rate limits)

## Adapter-Specific Docs

- [Reddit Ingestion Mapping and Runbook](./REDDIT_INGESTION_RUNBOOK.md)
