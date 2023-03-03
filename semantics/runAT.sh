#!/usr/bin/env zsh

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}

function cmd {
  pushd test/acceptance > /dev/null
  clear
  echo
  echo "DEBUGGING"
  echo "---------"
  $TIMEOUT -v -k 0 20 ./runDiff.sh
  popd > /dev/null
}

cmd
while true;
do
    sum1=$($MD5 `find src -name "*.maude" -not -name ".*"`)
    sleep 2
    sum2=$($MD5 `find src -name "*.maude" -not -name ".*"`)
    if [ "$sum1" != "$sum2" ];
    then
        cmd
    fi
done

