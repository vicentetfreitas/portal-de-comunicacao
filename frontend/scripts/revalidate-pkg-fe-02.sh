#!/usr/bin/env bash
# PKG-FE-02 gate (BUILD-01): lint, typecheck, unit tests, build — no E2E
set -uo pipefail

REPO="$(cd "$(dirname "$0")/../.." && pwd)"
PKG_DIR="$REPO/construction/frontend/features/FT-EQUIPE/pkg-fe-02"
LOG="$PKG_DIR/evidence/build-verify-$(date +%Y-%m-%d).log"
mkdir -p "$PKG_DIR/evidence"

export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"

cd "$REPO/frontend" || exit 1

LINT_EXIT=0
TYPECHECK_EXIT=0
TEST_UNIT_EXIT=0
BUILD_EXIT=0

{
  echo "PKG-FE-02 evidence $(date -Iseconds)"
  echo "PWD: $(pwd)"
  echo ""

  echo "1 yarn lint:check"
  yarn lint:check 2>&1 || LINT_EXIT=$?
  echo "EXIT_LINT=$LINT_EXIT"
  echo ""

  echo "2 yarn typecheck"
  yarn typecheck 2>&1 || TYPECHECK_EXIT=$?
  echo "EXIT_TYPECHECK=$TYPECHECK_EXIT"
  echo ""

  echo "3 yarn test:unit"
  yarn test:unit 2>&1 || TEST_UNIT_EXIT=$?
  echo "EXIT_TEST_UNIT=$TEST_UNIT_EXIT"
  echo ""

  echo "4 yarn build"
  yarn build 2>&1 || BUILD_EXIT=$?
  echo "EXIT_BUILD=$BUILD_EXIT"
} > "$LOG" 2>&1

cat "$LOG"

test "${LINT_EXIT}${TYPECHECK_EXIT}${TEST_UNIT_EXIT}${BUILD_EXIT}" = 0000 || exit 1
