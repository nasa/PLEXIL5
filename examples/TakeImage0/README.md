This directory contains PLEXIL plan TakeImage0 and its associated
model-checking files.  See README in parent directory for more info.

Expected behavior:

When loading `load0.maude` followed by `check.maude`, all properties should hold.

When loading `load1.maude` followed by `check.maude`, all properties shoiuld hold.

When loading `load2.maude` followed by `check.maude`, the following
properties fail:
 - correctness1
 - correctness3
 - liveness2
 
