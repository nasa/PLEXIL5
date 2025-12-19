This directory contains PLEXIL plan TakeImage1 and its associated
model-checking files.  See README in parent directory for more info.

Expected behavior:

When loading `load0.maude` followed by `check.maude`, all properties
should pass.  Note that load0 uses a deterministic environment that is
scripted to enable a fully successful execution of the plan.

When loading `load1.maude` followed by `check.maude`, only liveness1'
and safety3' pass, and all the other properties fail.  This is because
the environment here (env0.maude) is non-deterministic in _all_ the
plan's Lookups.
