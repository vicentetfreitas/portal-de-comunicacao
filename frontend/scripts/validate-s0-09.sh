#!/usr/bin/env bash
set -uo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
REPORT="$ROOT/reports/pkg-fe-s0-09-validation.log"
mkdir -p "$ROOT/reports"

{
  echo "=== PKG-FE-S0-09 validation ==="
  echo "Started: $(date -Iseconds)"
  echo "Node: $(node -v 2>/dev/null || echo unknown)"
  echo

  cd "$ROOT"

  run_step() {
    local name="$1"
    shift
    echo "===== $name ====="
    set +e
    "$@"
    local ec=$?
    set -e
    echo "EXIT_CODE_${name}=$ec"
    echo
    return $ec
  }

  run_step yarn_typecheck yarn typecheck
  TC=$?

  run_step yarn_test_unit yarn test:unit
  TU=$?

  run_step yarn_test_e2e_install yarn test:e2e:install
  TEI=$?

  run_step yarn_test_e2e yarn test:e2e
  TE=$?

  run_step yarn_build yarn build
  TB=$?

  echo "===== SUMMARY ====="
  echo "yarn_typecheck=$TC"
  echo "yarn_test_unit=$TU"
  echo "yarn_test_e2e_install=$TEI"
  echo "yarn_test_e2e=$TE"
  echo "yarn_build=$TB"

  if [ "$TC" -eq 0 ] && [ "$TU" -eq 0 ] && [ "$TEI" -eq 0 ] && [ "$TE" -eq 0 ] && [ "$TB" -eq 0 ]; then
    echo "OVERALL=PASS"
    exit 0
  fi

  echo "OVERALL=FAIL"
  exit 1
} 2>&1 | tee "$REPORT"
