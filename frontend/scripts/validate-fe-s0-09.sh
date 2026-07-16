#!/usr/bin/env bash
set -uo pipefail

REPORT_DIR="$(cd "$(dirname "$0")/.." && pwd)/reports"
REPORT_FILE="${REPORT_DIR}/pkg-fe-s0-09-validation.log"
mkdir -p "$REPORT_DIR"

{
  echo "=== PKG-FE-S0-09 validation ==="
  echo "Started: $(date -Iseconds)"
  export NVM_DIR="${NVM_DIR:-$HOME/.nvm}"
  # shellcheck source=/dev/null
  [ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
  nvm use 22
  echo "Node: $(node -v)"

  run_step() {
    local name="$1"
    shift
    echo ""
    echo "===== ${name} ====="
  set +e
    "$@"
    local code=$?
  set -e
    echo "EXIT_CODE_${name}=${code}"
    return "$code"
  }

  cd "$(dirname "$0")/.."

  FAILED=0
  run_step yarn_install yarn install --frozen-lockfile || FAILED=1
  run_step yarn_typecheck yarn typecheck || FAILED=1
  run_step yarn_test_unit yarn test:unit || FAILED=1
  run_step yarn_test_e2e_install yarn test:e2e:install || FAILED=1
  run_step yarn_test_e2e yarn test:e2e || FAILED=1
  run_step yarn_build yarn build || FAILED=1

  echo ""
  echo "=== DONE ==="
  echo "PIPELINE_FAILED=${FAILED}"
} 2>&1 | tee "$REPORT_FILE"

exit "${PIPESTATUS[0]}"
