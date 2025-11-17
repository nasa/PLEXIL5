#!/usr/bin/env bash

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}

msgAndExit () {
    echo "$*"
    exit 1
}

if (! [ -d ./tests ]) || [ -z "$(find ./tests -type d -mindepth 1 -maxdepth 1)" ] ; then
    echo
    echo "No extra acceptance tests found"
    echo
    exit
fi

RUN_TEST="$(realpath ./runTest.sh)"
test -f $RUN_TEST || msgAndExit "File $RUN_TEST not found"

# Acceptance Tests
# ----------------
# - Each acceptance test `<testName>` should be located in a subfolder of
#   `semantics/test/acceptance/tests`
# - The acceptance test subfolder `<testName>` must contain both a
#   `<testName>.ple` and a `<testName>.pst` file with the plan and script.
echo
echo "Acceptance Tests"
echo "----------------"
pushd ./tests > /dev/null
for i in `ls`; do
    pushd $i > /dev/null
    $TIMEOUT -v -k 0 20 $RUN_TEST $i.ple
    popd > /dev/null
done
