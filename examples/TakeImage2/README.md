This directory contains PLEXIL plan TakeImage2 and its associated
model-checking files.  See README in parent directory for more info.

Expected behavior:

When loading `load0.maude` followed by `check.maude`, all properties
should pass.

When loading `load1.maude` followed by `check.maude`, the following
properties should fail:
 - liveness2
 - safety2