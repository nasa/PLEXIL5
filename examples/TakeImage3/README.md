This directory contains PLEXIL plan TakeImage3 and its associated
model-checking files (see parent directory's README for more info).

Expected behavior:

When loading `load0` and then `check`, all properties should pass.

When loading `load1` and then `check`, the following properties should fail:
 - liveness3
 - timepoint1
