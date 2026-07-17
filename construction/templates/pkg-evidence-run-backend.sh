#!/usr/bin/env bash
# VAL-01 / ART-01 — Backend PKG evidence runner (template central)
#
# Usage:
#   PKG_DIR=construction/features/FT-SINGULAR/pkg-01 bash construction/templates/pkg-evidence-run-backend.sh

set -uo pipefail

if [ -z "${PKG_DIR:-}" ]; then
  echo "PKG_DIR is required (path to pkg-XX directory)" >&2
  exit 2
fi

REPO_ROOT="${REPO_ROOT:-/home/projects/portal-de-comunicacao}"
LOG="${PKG_DIR}/evidence/build-verify-$(date +%Y-%m-%d).log"
mkdir -p "${PKG_DIR}/evidence"
MVN_MODULE="${MVN_MODULE:-backend}"
MVN_TEST_SCOPE="${MVN_TEST_SCOPE:--pl ${MVN_MODULE} -am test}"

cd "$REPO_ROOT" || exit 1

{
  echo "PKG evidence $(date -Iseconds)"
  echo "PKG_DIR: $PKG_DIR"
  echo "PWD: $(pwd)"
  echo "JAVA: $(java -version 2>&1 | head -1)"
  echo ""

  echo "1 mvn ${MVN_TEST_SCOPE}"
  TEST_EXIT=0
  mvn $MVN_TEST_SCOPE 2>&1 || TEST_EXIT=$?
  echo "EXIT_MVN_TEST=$TEST_EXIT"
  echo ""

  echo "2 mvn compile -pl ${MVN_MODULE}"
  COMPILE_EXIT=0
  mvn compile -pl "$MVN_MODULE" 2>&1 || COMPILE_EXIT=$?
  echo "EXIT_MVN_COMPILE=$COMPILE_EXIT"
} > "$LOG" 2>&1

cat "$LOG"
exit $(( TEST_EXIT || COMPILE_EXIT ))
