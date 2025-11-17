PLEXIL-V
========

The Plan Execution Interchange Language ([PLEXIL](http://plexil.sourceforge.net)) is an open source synchronous language developed by [NASA](https://www.nasa.gov) for commanding and monitoring autonomous systems.

PLEXIL Formal Interactive Verification Environment (PLEXIL-V) is a tool that contains a formal executable semantics of PLEXIL specified as a rewriting logic specification in the [Maude](http://maude.cs.illinois.edu/) system.

PLEXIL-V provides an LTL model-checker of PLEXIL plans and a framework to model PLEXIL execution environments for verification.


### Current Release

PLEXIL-V v0.6.0 (October-2025)


### Requirements

* **Glasgow Haskell Compiler** (v9.6.7): The recommended way is to use [GHCup](https://www.haskell.org/ghcup/).

* **The Maude System**: Binary releases for different platforms can be found [here](https://github.com/maude-lang/Maude/releases).


## First steps

PLEXIL-V consists of different components:
- `plexil2maude` translator from PLEXIL XML format to PLEXIL-V internal format.
- `plexilog` log processing tools to compare PLEXIL-V against PLEXIL executive runs.
- `plexil` git submodule with the official PLEXIL executive version that is supported by PLEXIL-V.
- `semantics` the rewriting logic specification in Maude that implements the formal semantics and the auxiliary tools needed for model-checking and modeling execution environments.
- `examples` for showcasing how to verify properties of PLEXIL plans.


### Compile `plexil2maude` and `plexilog`

From the root of the repository, where the `cabal.project` file is located, install everything with cabal:
```
$ cabal install --overwrite-policy=always all
```
This will compile and install the tools located in the `plexil2maude` and `plexilog` folders.
Immediately after, confirm that the compiled tools are accessible from the command line by running:
```
$ plx2maude
$ plexilog-diff
```
Otherwise, add the folder containing those commands to the `PATH`.


### Compile the PLEXIL interpreter **(optional)**

To install the official PLEXIL executive, checkout the `plexil` submodule of this repository:
```
$ git submodule update --init --recursive
```
Then follow the instructions located in the [official repository](https://github.com/plexil-group/plexil).

If you run into issues while compiling, the minimal set of tools needed for PLEXIL-V can be obtained by running the following commands from the `plexil` folder:
```
$ make ipc
$ make tools -j
```

After adding the `plexil/scripts` folder to the `PATH` environment variable, the following commands should be available:
```
$ plexilc
$ plexiltest
```


### Semantics

The semantics is inside the `semantics/src/` folder.
The file that needs to be imported to access all the semantics is the `semantics/src/plexil-v.maude` file:
```
$ maude semantics/src/plexil-v.maude
```
The `PLEXIL-V` module should be visible.
```
Maude> show module PLEXIL-V .
```


### Examples

The `examples/` folder, contains some examples of PLEXIL plans with properties to verify.

For example, the `TakeImage0` example contains a PLEXIL plan controlling a camera that can move to point at its target before taking a photo, if necessary.

Two environment model files are provided to model the environment of execution of the plan.
`env0.maude` models an environment in which the camera is always pointing at its target, while `env1.maude` models that the camera can be moved away from the target at any execution step.

The example properties to verify are contained in `properties.maude`.

Some Maude scripts are provided to automate the loading and execution of the models:
  - `loadX.maude` scripts load the `TakeImage0` plan and the environment `envX.maude`.
  - `check.maude` contains the commands to execute to verify the different properties.
Before executing any of the previous scripts the PLEXIL-V main module `plexil-v.maude` has to be loaded into Maude.

To verify the properties in `env0.maude` we would run the following commands:
```
$ cd examples/TakeImage0
$ make                     # compiles the plans
$ maude ../../semantics/src/plexil-v.maude load0.maude check.maude
```
Then we can confirm that all the properties hold:
```
...
==========================================
reduce in M-CHECK : plexilModelCheck(init, correctness1, transitionCounterexample) .
rewrites: 6872 in 1ms cpu (1ms real) (4032863 rewrites/second)
result Bool: true
==========================================
...
```
However, if we try to verify them over `env-1.maude`:
```
$ cd examples/TakeImage
$ maude ../../semantics/src/plexil-v.maude load1.maude check.maude
```
some of them fail:
```
...
==========================================
reduce in M-CHECK : plexilModelCheck(init, correctness1, transitionCounterexample) .
rewrites: 8316 in 2ms cpu (2ms real) (3977044 rewrites/second)
result TransitionCounterexample:
Start
-->
Input(stateLookup('PointedAtTarget, nilarg, val(true)))
-->
Input(commandAck('camera_capture_image, val(50) val(1), CommandSuccess) stateLookup('PointedAtTarget, nilarg, val(false)))
-->
Loop{

Input(stateLookup('PointedAtTarget, nilarg, val(true)))
--> Input(stateLookup('PointedAtTarget, nilarg, val(false)))
}
==========================================
...
```
Note that a counterexample is provided for each of the failures.

The example property `correctness1`, defined as `hasStatus('TakeImage, executing) U hasStatus('TakeImage, finished)`, states that the root node of the plan remains in the `EXECUTING` status until it eventually reaches the `FINISHED` state.

This property clearly holds when the camera is always pointing at its target (as in `env0.maude`) but not if it can move (as in `env1.maude`), since in the latter case, an invariant condition in the plan is violated, preventing the root node from finishing.

### License

The code in this repository is released under NASA's Open Source Agreement.
See the directory [`LICENSES`](LICENSES); see also the copyright notice at the end of this file.

### Authors

* [Cesar A. Mu&ntilde;oz](http://shemesh.larc.nasa.gov/people/cam) (cesar.a.munoz@nasa.gov), NASA Langley Research Center, USA.
* Marco A. Feliu, Analytical Mechanics Associates supporting NASA Langley Research Center, USA.

### Contributors

* Camilo Rocha, Pontificia Universidad Javeriana de Cali, Colombia.
* Hector Cadavid, Escuela Colombiana de Ingenier&iacute;a, Colombia.

### Copyright Notice

Copyright 2016 United States Government as represented by the Administrator of the National Aeronautics and Space Administration. All Rights Reserved.

No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER, CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.  FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE, AND DISTRIBUTES IT "AS IS."

Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT.  IF RECIPIENT'S USE OF THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES, EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW.  RECIPIENT'S SOLE REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL TERMINATION OF THIS AGREEMENT.
