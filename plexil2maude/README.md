# plexil2maude

The `plexil2maude` repository provides the commands `plx2maude`
and `psx2maude`.

The `plx2maude` program translates **PLEXIL**'s *PLX* files (**core
PLEXIL** plan's *XML* plan representation) into a **Maude**'s
**PLEXIL** *plan* theory that provides a representation of the plan
that can be analyzed with the [Rewriting Logic PLEXIL semantics in
Maude](https://gitlab.larc.nasa.gov/plexil/plexil-maude-semantics).

Similarly, the `psx2maude` program translates **PLEXILScript** *PSX*
files into its **Maude** representation.

# Installation

1. Install the `ghc` Haskell compiler and the `cabal-install`
   installation tool.

2. Run the command `cabal v2-install` inside the folder containing the
   `plexil2maude.cabal` file.  The `cabal-install` tool will then
   build and install `plx2maude` and `psx2maude` in a predefined place
   that will be output to the terminal at the end of its execution:
   ```
   ...
   Symlinking 'plx2maude' to '<a-path>/plx2maude'
   Symlinking 'psx2maude' to '<a-path>/psx2maude'
   ```

NOTE: If either executable already exists, invoke the build command
with the `--overwrite-policy=always` option:
```
cabal --overwrite-policy=always v2-install
```

# Running

The `plexil2maude` command is very simple.
It requires one argument which is the **PLEXIL**'s `.plx` file to translate.
```
plexil2maude plan.plx
```
If necessary, use the full path `<a-path>/plexil2maude` provided by
`cabal-install`'s output during the installation.  The translation is
output to the terminal and should be stored in a file to be further
analyzed, e.g.:
```
plexil2maude plan.plx > plan.maude
```
