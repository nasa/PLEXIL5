#!/usr/bin/env bash

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}
MAUDE=${3:-maude}

pushd ../../benchmark/icarous/ > /dev/null
for i in `ls | grep -E '^[0-9]+$'`; do
    pushd $i > /dev/null
    echo -n "Input [$i]: "
    test -f ../deps.maude || (echo "File ../deps.maude not found" && exit)
    test -f ../run.maude  || (echo "File ../run.maude not found"  && exit)
    test -f input.maude   || (echo "File input.maude not found"   && exit)
    $MAUDE -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ../deps.maude input.maude ../run.maude > /dev/null 2> temp.output.maude
    plexilog-diff output.plexil temp.output.maude
    popd > /dev/null
done
popd > /dev/null

readlink=greadlink

# cognition tests
echo
echo "COGNITION BENCHMARK"
echo "-------------------"
pushd ../../benchmark/icarous/cognition > /dev/null
plePlan=`$readlink -f ${1:-TrafficConflictHandler.ple}`
plxPlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.plx/'`
maudePlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.maude/'`
plexil2maudeCMD=${3:-"plx2maude"}
pstTestInput=${2:-"input.pst"}
psxTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.psx/'`
maudeTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.maude/'`
debugConfiguration=`$readlink -f ../../Debug.AcceptanceTest.cfg`
maudeDeps=`$readlink -f deps.maude`
maudeRun=`$readlink -f run.maude`

# echo "____________________________________________________________"
# echo "Compiling PLEXIL plan $plePlan into $plxPlan ..."
plexilc $plePlan
# echo "DONE"
# echo ""

# echo "____________________________________________________________"
# echo "Compiling PLEXIL plan $plxPlan into $maudePlan ..."
$plexil2maudeCMD $plxPlan > $maudePlan
# echo "DONE"
# echo ""

    test -f $plePlan            || (echo "File plePlan:$plePlan not found" && exit)
    test -f $plxPlan            || (echo "File plxPlan:$plxPlan not found" && exit)
    test -f $maudePlan          || (echo "File maudePlan:$maudePlan not found" && exit)
    test -f $debugConfiguration || (echo "File debugConfiguration:$maudePlan not found" && exit)

for i in `ls tests | grep -E '^[0-9]+$' | sort -n`; do
    pushd tests/$i > /dev/null
    echo -n "Input [$i]: "

# echo "____________________________________________________________"
# echo "Compiling testinput $pstTestInput into $psxTestInput ..."
plexilc $pstTestInput > /dev/null
# echo "DONE"
# echo ""

psx2maude $psxTestInput > $maudeTestInput

    test -f $pstTestInput   || (echo "File pstTestInput:$pstTestInput not found" && exit)
    test -f $psxTestInput   || (echo "File psxTestInput:$psxTestInput not found" && exit)
    test -f $maudeDeps      || (echo "File maudeDeps:$maudeDeps not found" && exit)
    test -f $maudeRun       || (echo "File maudeRun:$maudeRun not found" && exit)
    test -f $maudeTestInput || (echo "File maudeTestInput:$maudeTestInput not found" && exit)

# echo "____________________________________________________________"
# echo "Executing test with $plxPlan and $psxTestInput ..."
plexiltest -p $plxPlan -s $psxTestInput -d $debugConfiguration > output.plexil 2>&1
# echo "DONE"
# echo ""

# echo "____________________________________________________________"
# echo "Executing test with $maudePlan and $maudeTestInput ..."
$MAUDE -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr $maudeDeps $maudeTestInput $maudeRun > /dev/null 2> output.maude
# echo "DONE"
# echo ""

# echo "____________________________________________________________"
# echo "Comparing execution of $plxPlan againts $maudePlan ..."
plexilog-diff output.plexil output.maude
# echo "DONE"
# echo ""

    #  $MAUDE -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ../../../deps.maude input.maude ../../../run.maude > /dev/null 2> temp.output.maude
    # plexilog-diff output.plexil temp.output.maude
    popd > /dev/null
done
popd > /dev/null
