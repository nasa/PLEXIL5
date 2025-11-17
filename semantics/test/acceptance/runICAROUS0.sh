#!/usr/bin/env bash

TIMEOUT=${1:-timeout}
MD5=${2:-md5sum}
MAUDE=${3:-maude}
readlink=readlink

echo
echo "ICAROUS Set 0"
echo "-------------"

pushd icarous/set-0 > /dev/null
plePlan=`$readlink -f ${1:-TRAFFIC_RESOLUTION.ple}`
plxPlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.plx/'`
maudePlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.maude/'`
plexil2maudeCMD=${3:-"plx2maude"}
pstTestInput=${2:-"input.pst"}
psxTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.psx/'`
maudeTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.maude/'`
debugConfiguration=`$readlink -f ../../Debug.AcceptanceTest.cfg`
maudeDeps=`$readlink -f deps.maude`
maudeRun=`$readlink -f run.maude`

plexilc $plePlan
$plexil2maudeCMD $plxPlan > $maudePlan

for i in `ls | grep -E '^[0-9]+$'`; do
    pushd $i > /dev/null
    plexilc $pstTestInput > /dev/null
    psx2maude $psxTestInput > $maudeTestInput
    echo -n "Input [$i]: "
    test -f ../deps.maude || (echo "File ../deps.maude not found" && exit)
    test -f ../run.maude  || (echo "File ../run.maude not found"  && exit)
    test -f input.maude   || (echo "File input.maude not found"   && exit)
    plexiltest -p $plxPlan -s $psxTestInput -d $debugConfiguration > output.plexil 2>&1
    $MAUDE -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ../deps.maude input.maude ../run.maude > /dev/null 2> temp.output.maude
    plexilog-diff output.plexil temp.output.maude
    popd > /dev/null
done
popd > /dev/null