#!/bin/bash

maude -no-wrap -no-banner -no-advise \
  deps.maude \
  TrafficConflictHandler.maude \
  model-check/$1/input.maude \
  runModelChecker.maude \
  <(echo "q .")
