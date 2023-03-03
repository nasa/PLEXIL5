#!/usr/bin/env bash

echo "Compiling PLEXIL plan..."
plexilc TRAFFIC_RESOLUTION.ple

for i in `ls | grep -E '[0-9]+'`; do
    echo -n "  Preparing test $i... "
    pushd $i > /dev/null
    plexilc input.pst
    plexiltest -p ../TRAFFIC_RESOLUTION.plx -s input.psx -d ../../Debug.AcceptanceTest.cfg > output.plexil
    echo "DONE"
    popd > /dev/null
done


