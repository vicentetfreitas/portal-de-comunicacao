#!/usr/bin/env bash
# VAL-01 / ART-01 / BUILD-02 — Frontend PKG evidence runner (template central)
#
# Usage:
#   PKG_DIR=construction/.../pkg-fe-01 bash construction/templates/pkg-evidence-run-frontend.sh
#
# Modes:
#   (default)           → typecheck, yarn test, build (legado)
#   FULL_VALIDATION=1   → Gate PKG: lint, typecheck, test:unit, build (SEM e2e)
#   + E2E_VALIDATION=1  → Gate PKG + yarn test:e2e (somente PKG-FE-06 / closure)
#
# Ver: construction/16-frontend-validation-gates.md

set -uo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
REPO_ROOT="$(cd "$SCRIPT_DIR/../.." && pwd)"

if [ -z "${PKG_DIR:-}" ]; then
  echo "PKG_DIR is required (path to pkg-XX directory)" >&2
  exit 2
fi

if [[ "$PKG_DIR" != /* ]]; then
  PKG_DIR="$REPO_ROOT/$PKG_DIR"
fi

FRONTEND="${FRONTEND_ROOT:-$REPO_ROOT/frontend}"
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
  echo "E2E_VALIDATION: ${E2E_VALIDATION:-0}"
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

    if [ "${E2E_VALIDATION:-0}" = "1" ]; then
      echo "4 yarn test:e2e"
      if ! compgen -G "$HOME/.cache/ms-playwright/chromium-"* >/dev/null 2>&1; then
        echo "running yarn test:e2e:install"
        yarn test:e2e:install 2>&1 || true
      fi
      PLAYWRIGHT_SINGLE_WORKER=1 yarn test:e2e 2>&1 || TEST_E2E_EXIT=$?
      echo "EXIT_TEST_E2E=$TEST_E2E_EXIT"
      echo ""

      echo "5 yarn build"
      yarn build 2>&1 || BUILD_EXIT=$?
      echo "EXIT_BUILD=$BUILD_EXIT"
    else
      echo "4 yarn build"
      yarn build 2>&1 || BUILD_EXIT=$?
      echo "EXIT_BUILD=$BUILD_EXIT"
    fi
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
  if [ "${E2E_VALIDATION:-0}" = "1" ]; then
    test "${LINT_EXIT}${TYPECHECK_EXIT}${TEST_UNIT_EXIT}${TEST_E2E_EXIT}${BUILD_EXIT}" = 00000 || exit 1
  else
    test "${LINT_EXIT}${TYPECHECK_EXIT}${TEST_UNIT_EXIT}${BUILD_EXIT}" = 0000 || exit 1
  fi
else
  exit $(( TYPECHECK_EXIT || TEST_EXIT || BUILD_EXIT ))
fi
