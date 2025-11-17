#!/usr/bin/env bash

plePlan=${1:-"TrafficConflictHandler.ple"}
plxPlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.plx/'`
maudePlan=`echo $plePlan | sed 's/\(.*\)\.ple/\1.maude/'`

plexil2maudeCMD=${3:-"plexil2maude"}

pstTestInput=${2:-"tests/0/input.pst"}
psxTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.psx/'`
maudeTestInput=`echo $pstTestInput | sed 's/\(.*\)\.pst/\1\.maude/'`

debugConfiguration=${3:-"../../Debug.AcceptanceTest.cfg"}

echo "____________________________________________________________"
echo "Compiling PLEXIL plan $plePlan into $plxPlan ..."
plexilc $plePlan
echo "DONE"
echo ""

echo "____________________________________________________________"
echo "Compiling PLEXIL plan $plxPlan into $maudePlan ..."
$plexil2maudeCMD $plxPlan > $maudePlan
echo "DONE"
echo ""

echo "____________________________________________________________"
echo "Compiling testinput $pstTestInput into $psxTestInput ..."
plexilc $pstTestInput
echo "DONE"
echo ""

echo "____________________________________________________________"
echo "Executing test with $plxPlan and $psxTestInput ..."
plexiltest -p $plxPlan -s $psxTestInput -d $debugConfiguration > output.plexil
echo "DONE"
echo ""

echo "____________________________________________________________"
echo "Executing test with $maudePlan and $maudeTestInput ..."
maude -no-ansi-color -no-wrap -no-banner -no-advise -print-to-stderr ./deps.maude $maudeTestInput ../run.maude > /dev/null 2> output.maude
echo "DONE"
echo ""

echo "____________________________________________________________"
echo "Comparing execution of $plxPlan againts $maudePlan ..."
plexilog-diff output.plexil output.maude
echo "DONE"
echo ""


# for i in `ls | grep -E '[0-9]+'`; do
#     echo -n "  Preparing test $i... "
#     pushd $i > /dev/null
#     plexilc input.pst
#     plexiltest -p ../TRAFFIC_RESOLUTION.plx -s input.psx -d ../../Debug.AcceptanceTest.cfg > output.plexil
#     echo "DONE"
#     popd > /dev/null
# done


