#!/usr/bin/env bash
# VAL-01 / ART-01 — Frontend PKG evidence runner (template central)
#
# Usage:
#   PKG_DIR=construction/.../pkg-fe-06 bash construction/templates/pkg-evidence-run-frontend.sh
#   FULL_VALIDATION=1  → lint:check, test:unit, test:e2e, build (frontend Features fechamento)
#   (default)          → typecheck, yarn test, build

set -uo pipefail

if [ -z "${PKG_DIR:-}" ]; then
  echo "PKG_DIR is required (path to pkg-XX directory)" >&2
  exit 2
fi

FRONTEND="${FRONTEND_ROOT:-/home/projects/portal-de-comunicacao/frontend}"
LOG="${PKG_DIR}/evidence/build-verify-$(date +%Y-%m-%d).log"
mkdir -p "${PKG_DIR}/evidence"

export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"

cd "$FRONTEND" || exit 1

LINT_EXIT=0
TYPECHECK_EXIT=0
TEST_EXIT=0
TEST_UNIT_EXIT=0
TEST_E2E_EXIT=0
BUILD_EXIT=0

{
  echo "PKG evidence $(date -Iseconds)"
  echo "PKG_DIR: $PKG_DIR"
  echo "PWD: $(pwd)"
  echo "FULL_VALIDATION: ${FULL_VALIDATION:-0}"
  echo ""

  if [ "${FULL_VALIDATION:-0}" = "1" ]; then
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

    echo "4 yarn test:e2e"
    if ! compgen -G "$HOME/.cache/ms-playwright/chromium-"* >/dev/null 2>&1; then
      echo "running yarn test:e2e:install"
      yarn test:e2e:install 2>&1 || true
    fi
    yarn test:e2e 2>&1 || TEST_E2E_EXIT=$?
    echo "EXIT_TEST_E2E=$TEST_E2E_EXIT"
    echo ""

    echo "5 yarn build"
    yarn build 2>&1 || BUILD_EXIT=$?
    echo "EXIT_BUILD=$BUILD_EXIT"
  else
    echo "1 yarn typecheck"
    yarn typecheck 2>&1 || TYPECHECK_EXIT=$?
    echo "EXIT_TYPECHECK=$TYPECHECK_EXIT"
    echo ""

    echo "2 yarn test"
    yarn test 2>&1 || TEST_EXIT=$?
    echo "EXIT_TEST=$TEST_EXIT"
    echo ""

    echo "3 yarn build"
    yarn build 2>&1 || BUILD_EXIT=$?
    echo "EXIT_BUILD=$BUILD_EXIT"
  fi
} > "$LOG" 2>&1

cat "$LOG"

if [ "${FULL_VALIDATION:-0}" = "1" ]; then
  test "${LINT_EXIT}${TYPECHECK_EXIT}${TEST_UNIT_EXIT}${TEST_E2E_EXIT}${BUILD_EXIT}" = 00000 || exit 1
else
  exit $(( TYPECHECK_EXIT || TEST_EXIT || BUILD_EXIT ))
fi
