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
- Required checks in branch protection should include CI and version-label validation.

## Notes

- Script is non-destructive and fast-fails on first error.
- If `.github/workflows` does not exist yet, script exits successfully with a skip message.
- `act` dry-run validates workflow wiring, but final parity check should run on GitHub-hosted runners.
