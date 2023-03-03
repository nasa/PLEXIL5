#!/bin/bash

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}

function cmd {
  clear
  printf "\nTESTS\n-----\n"
  $TIMEOUT -v -k 0 5 $MAUDE -batch -always-advise -no-banner test/runSuite.maude
}
cmd
while true;
do
    sum1=$($MD5 `find ./src -name "*.maude" -not -name ".*"`)
    sleep 2
    sum2=$($MD5 `find ./src -name "*.maude" -not -name ".*"`)
    if [ "$sum1" != "$sum2" ];
    then
        cmd
    fi
done

