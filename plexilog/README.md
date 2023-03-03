# PLEXIL Log Tools

The *PLEXIL Log Tools* provide commands to parse the *debugging* log output files of the *PLEXIL Executive* and the *Rewriting Logic PLEXIL Semantics Interpreter* into a Haskell data structure representing the sets/sequences of atomic, micro and macro steps.
The `plexilog` program parses the output of the *PLEXIL Executive* and prints the result to the console.
The `plexilog-maude` program parses the output of the *Rewriting Logic PLEXIL Semantics Interpreter* and prints the result to the console.
The `plexilog-diff` program compares the log outputs of a *PLEXIL Executive* run and a *Rewriting Logic PLEXIL Semantics Interpreter* run, and prints its differences to the console.

# Installation

1. Install the `ghc` Haskell compiler and the `cabal-install` installation tool.

2. Run the command `cabal v2-install` inside the folder containing the `plexil-log-tools.cabal` file.
   The `cabal-install` tool will then build and install `plexilog`, `plexilog-maude` and `plexilog-diff` in a predefined place that will be output to the terminal at the end of its execution:
   ```
   ...
   Symlinking 'plexilog-maude' to '<a-path>/plexilog-maude'
   Symlinking 'plexilog-diff' to '<a-path>/plexilog-diff'
   Symlinking 'plexilog' to '<a-path>/plexilog'
   ```

# Running

The `plexilog` command requires the *PLEXIL Executive*'s  log information in its standard input and prints its parsed data to the console:
```
plexilog < plexil-plan.log
```

The `plexilog-maude` command requires the *Rewriting Logic PLEXIL Semantics Interpreter* log information in its standard input and prints its parsed data to the console:
```
plexilog-maude < maude-plan.log
```

The `plexilog-diff` command requires the *PLEXIL Executive* and the *Rewriting Logic PLEXIL Semantics Interpreter* log files as arguments, and it prints the difference to the console:
```
plexilog-diff plexil-plan.log maude-plan.log
```
