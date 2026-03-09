# Versioning

Canonical versioning policy lives in `friction-core`:

- [friction-core/docs/VERSIONING.md](https://github.com/idelstak/friction-core/blob/master/docs/VERSIONING.md)

This repo follows that policy for release labeling and pre-release staging.

## Core Dependency Pinning

`friction-adapters` consumes published `friction-core` packages in CI/release.

- Dependency source: GitHub Packages (`friction-core` package repo)
- Dependency auth in CI: `secrets.PACKAGES_TOKEN`
- Pin point: `pom.xml` property `friction.core.version`
- Rule: update `friction.core.version` only to published tags/versions
  available in GitHub Packages.

Do not use local jars or manual classpath wiring in CI.
