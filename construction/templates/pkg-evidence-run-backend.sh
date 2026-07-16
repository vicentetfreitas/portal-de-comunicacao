#!/usr/bin/env bash
# VAL-01 — Backend PKG evidence runner (template)
# Usage: copy to pkg-XX/evidence/run-bv.sh, adjust MVN_MODULE / MVN_TEST_SCOPE.

set -uo pipefail

REPO_ROOT="${REPO_ROOT:-/home/projects/portal-de-comunicacao}"
PKG_DIR="$(cd "$(dirname "$0")/.." && pwd)"
LOG="${PKG_DIR}/evidence/build-verify-$(date +%Y-%m-%d).log"
MVN_MODULE="${MVN_MODULE:-backend}"
MVN_TEST_SCOPE="${MVN_TEST_SCOPE:--pl ${MVN_MODULE} -am test}"

cd "$REPO_ROOT" || exit 1

{
  echo "PKG evidence $(date -Iseconds)"
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
