#!/usr/bin/env bash
set -euo pipefail
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && . "$NVM_DIR/nvm.sh"
cd /home/projects/portal-de-comunicacao/frontend
run() { echo "=== $1 ==="; shift; "$@"; ec=$?; echo "EXIT $1: $ec"; return $ec; }
EC_LINT=0; EC_TYPE=0; EC_TEST=0; EC_BUILD=0
run lint yarn lint || EC_LINT=$?
run typecheck yarn typecheck || EC_TYPE=$?
run test yarn test || EC_TEST=$?
run build yarn build || EC_BUILD=$?
echo "SUMMARY lint=$EC_LINT typecheck=$EC_TYPE test=$EC_TEST build=$EC_BUILD"
exit $(( EC_LINT || EC_TYPE || EC_TEST || EC_BUILD ))