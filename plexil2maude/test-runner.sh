#!/bin/bash

# This script translates a program to Maude and then tries to load that  for the Plexil to Maude translator.
# It is intended to be run from the top-level directory plexil2maude.

# The script takes one argument, the name of the file to test.

rm -f parsedTests/$1.maude

cabal run plx2maude -- ~/Documents/plexil/test/TestExec-regression-test/plans/$1.plx >> parsedTests/$1.maude

sed -i '1d' parsedTests/$1.maude

maude ../semantics/src/plexilite.maude parsedTests/$1.maude