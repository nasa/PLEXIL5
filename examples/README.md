This directory contains examples for PLEXIL-V.  Each example directory
contains a PLEXIL plan, a Maude file containing properties to verify
in the plan, one or more environment definition files (PLEXIL and
Maude), and one or more convenience files for loading desired filesets
into Maude and running the checker.

Each directory contains a Makefile that compiles PLEXIL code into the
XML format read by the PLEXIL executive, as well as Maude to be loaded
into PLEXIL-V.

The file naming conventions as as follows.

properties.maude - properties for the plan
scriptN.pst      - deterministic environments (PLEXIL simulation scripts)
envN.pst         - nondeterministic environments
loadN.maude      - load file for the above
checkN.maude     - property checks

The TakeImageN directories are iterations of a simple plan that
illustrates image capture by an imagined satellite.

To build an example, type `make` in its directory.

To remove all build products of an example, type `make clean` in its
directory.

To run an example:

1. Change to its directory.

2. Start Maude with PLEXIL-V, e.g.
   `maude -no-wrap -no-banner -no-advise /path/to/plexil-v/semantics/src/plexil-v.maude`

3. Load the desired `load` file, e.g. `Maude> load load0`

4. Load the desired `check` file, e.g. `Maude> load check0`

To execute the plans using the production PLEXIL executive, you'll
need an installation of its `releases/plexil-6` git branch found at
https://github.com/plexil-group/plexil.
