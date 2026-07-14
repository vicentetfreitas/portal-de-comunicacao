#!/usr/bin/env bash
# One-time migration of scattered runtime artifacts into backend/runtime/
set -euo pipefail

ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT"

mkdir -p runtime/logs runtime/reports/surefire-legacy runtime/dumps runtime/coverage

for f in \
  pkg08-verify2.log pkg08-verify.log pkg07-verify.log \
  mvn-full.log step1.log step2.log mvn-test-out.log \
  maven-test-report.txt pkg07-test.out pkg08-exit.code \
  .pkg08-tests-line.txt geh-test-result.log
do
  if [[ -f "$f" ]]; then
    mv "$f" runtime/logs/
    echo "MOVED $f -> runtime/logs/"
  fi
done

if [[ -d target/surefire-reports ]] && [[ -n "$(ls -A target/surefire-reports 2>/dev/null)" ]]; then
  mv target/surefire-reports/* runtime/reports/surefire-legacy/
  rmdir target/surefire-reports 2>/dev/null || true
  echo "MOVED target/surefire-reports/* -> runtime/reports/surefire-legacy/"
fi
