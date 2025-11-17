#!/bin/bash

CHECK=NO

while [[ $# -gt 0 ]]; do
  case $1 in
    -c|--check)
      CHECK=YES
      shift
      ;;
    *)
      break
      ;;
  esac
done

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}

function run {
  clear
  printf "\nTESTS\n-----\n"
  $TIMEOUT -v -k 0 5 $MAUDE -batch -always-advise -no-banner test/runSuite.maude
}

function check {
  if grep -q "AllTestsPassed" "$1"; then
      exit 0
  else
      exit 1
  fi
}

if [ "$CHECK" == "YES" ]; then
  TEMP_FILE=$(mktemp)
  run | tee $TEMP_FILE
  check $TEMP_FILE
else
  run
  while true; do
    sum1=$($MD5 `find ./src ./test -name "*.maude" -not -name ".*"`)
    sleep 2
    sum2=$($MD5 `find ./src ./test -name "*.maude" -not -name ".*"`)
    if [ "$sum1" != "$sum2" ];
    then
        run
    fi
  done
fi
