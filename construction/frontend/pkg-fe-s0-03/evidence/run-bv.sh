#!/usr/bin/env bash
# VAL-01 — PKG-FE-S0-03 evidence runner (from construction/templates/pkg-evidence-run-frontend.sh)

set -uo pipefail

FRONTEND="${FRONTEND_ROOT:-/home/projects/portal-de-comunicacao/frontend}"
PKG_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LOG="${PKG_DIR}/evidence/build-verify-$(date +%Y-%m-%d).log"

export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"

cd "$FRONTEND" || exit 1

{
  echo "PKG-FE-S0-03 evidence $(date -Iseconds)"
  echo "PWD: $(pwd)"
  echo "NODE: $(node -v 2>&1)"
  echo "YARN: $(yarn -v 2>&1)"
  echo ""

  echo "1 yarn typecheck"
  TYPECHECK_EXIT=0
  yarn typecheck 2>&1 || TYPECHECK_EXIT=$?
  echo "EXIT_TYPECHECK=$TYPECHECK_EXIT"
  echo ""

  echo "2 yarn test"
  TEST_EXIT=0
  yarn test 2>&1 || TEST_EXIT=$?
  echo "EXIT_TEST=$TEST_EXIT"
  echo ""

  echo "3 yarn build"
  BUILD_EXIT=0
  yarn build 2>&1 || BUILD_EXIT=$?
  echo "EXIT_BUILD=$BUILD_EXIT"
} > "$LOG" 2>&1

cat "$LOG"
exit $(( TYPECHECK_EXIT || TEST_EXIT || BUILD_EXIT ))
