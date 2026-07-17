#!/usr/bin/env bash
# PKG-FE-06 gate (E2E-01): Gate PKG + Playwright E2E
set -uo pipefail

REPO="$(cd "$(dirname "$0")/../.." && pwd)"
export PKG_DIR="$REPO/construction/frontend/features/FT-EQUIPE/pkg-fe-06"
export FULL_VALIDATION=1
export E2E_VALIDATION=1

exec bash "$REPO/construction/templates/pkg-evidence-run-frontend.sh"
