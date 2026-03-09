# Workflow Development and Local Preflight

Use this guide to validate GitHub Actions workflows locally before pushing.

## Required Tools

- `actionlint`
- `yamllint`
- `act`

## Installation

### Arch Linux (`pacman`)

```bash
sudo pacman -S --needed actionlint yamllint act
```

### actionlint

- Project: `https://github.com/rhysd/actionlint`
- Example (Go): `go install github.com/rhysd/actionlint/cmd/actionlint@latest`

### yamllint

- Example (pipx): `pipx install yamllint`
- Alternative: `pip install yamllint`

### act

- Project: `https://github.com/nektos/act`
- Install using package manager or official release binary

## Run Local Preflight

From repo root:

```bash
scripts/check-workflows.sh
```

What it does:

1. Verifies required tools are installed.
2. Runs `actionlint`.
3. Runs `yamllint .github/workflows`.
4. Runs `act` dry-run smoke checks for key workflows if present:
   - `ci.yml`
   - `release.yml`
   - `publish.yml`

## Version Label and Required Check Alignment

Ensure workflow checks align with repo release policy:

- PRs should include exactly one impact label:
  - `version:major` or `version:minor` or `version:patch`
- Optional one pre-release label:
  - `version:alpha` or `version:beta` or `version:rc`
- Stable PR check names from `.github/workflows/ci.yml`:
  - `build-and-test`
  - `version-label-check`
- Branch protection should require both checks.

## Dependency Resolution in CI

`build-and-test` resolves `friction-core` from GitHub Packages.

- `pom.xml` uses repository id `github` for `friction-core` packages.
- `ci.yml` configures `setup-java` server credentials for `github`.
- `ci.yml` build/test step uses `secrets.PACKAGES_TOKEN` for package access.
- CI must not rely on local/manual jars for `friction-core`.

## Publish Reference

`docs/PUBLISH_RUNBOOK.md` is the only source of truth for publish behavior.
Use its template directly and avoid local variations.

## Release and Publish Workflows

- `release.yml` (push to `master`):
  - resolves semver bump from PR labels
  - bumps `pom.xml` version
  - creates git tag + GitHub Release
  - generates release notes from current PR only in format:
    - `# Changelog`
    - `#<PR_NUMBER> <PR_TITLE>`
    - cleaned PR body
- `publish.yml` (`workflow_run` on successful `Release`):
  - behavior is defined only by `docs/PUBLISH_RUNBOOK.md` template

## Permissions Baseline

- `ci.yml`: `contents: read`, `packages: read`
- `release.yml`: `contents: write`, `pull-requests: read`
- `publish.yml` permissions and auth wiring are defined only in
  `docs/PUBLISH_RUNBOOK.md`.

## Notes

- Script is non-destructive and fast-fails on first error.
- If `.github/workflows` does not exist yet, script exits successfully with a skip message.
- `act` dry-run validates workflow wiring, but final parity check should run on GitHub-hosted runners.
