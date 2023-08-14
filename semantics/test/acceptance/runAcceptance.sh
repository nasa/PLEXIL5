#!/usr/bin/env bash

MAUDE=${1:-maude}

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

# Acceptance Tests
# ----------------
# - Each acceptance test `<testName>` should be located in a subfolder of
#   `semantics/test/acceptance/tests`
# - The acceptance test subfolder `<testName>` must contain both a
#   `<testName>.ple` and a `<testName>.pst` file with the plan and script.
echo
echo "ACCEPTANCE TESTS"
echo "----------------"
pushd ./tests > /dev/null
for i in `ls`; do
    pushd $i > /dev/null
    test -f $i.ple || msgAndExit "File $i.ple not found"
    test -f $i.pst || msgAndExit "File $i.pst not found"
    rm -f $i.plx
    rm -f $i.psx
    plexilc $i.ple
    plexilc $i.pst
    test -f $i.plx || msgAndExit "Error producing file $i.plx"
    test -f $i.psx || msgAndExit "Error producing file $i.psx"
    rm -f $i.maude
    rm -f input.maude
    plx2maude $i.plx > $i.maude
    psx2maude $i.psx > input.maude
    test -f $i.maude    || msgAndExit "Error producing file $i.maude"
    test -f input.maude || msgAndExit "Error producing file input.maude"
    echo -n "Input [$i]: "

    # create run.maude
    rm -f run.maude
    rm -f output.maude
    rm -f output.plexil
    printf "load ../../../../src/plexilite.maude\nload $i.maude\nload input.maude\nset print attribute on .\nset print color on .\nmod PLAN-SIMULATION is\n    protecting $i-PLAN .\n    protecting INPUT .\nendm\nrew run(compile(rootNode,input)) .\nq" >> run.maude

    plexiltest -p $i.plx -s $i.psx -d ../../../../benchmark/Debug.AcceptanceTest.cfg > output.plexil 2>&1
    $MAUDE -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr run.maude > /dev/null 2> output.maude
    plexilog-diff output.plexil output.maude
    popd > /dev/null
done
