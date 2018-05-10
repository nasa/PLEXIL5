PLEXIL5 
========
The Plan Execution Interchange Language ([PLEXIL](http://plexil.sourceforge.net)) is an open source
synchronous language developed by NASA for commanding and monitoring
autonomous systems. PLEXIL Formal Interactive Verification Environment
(PLEXIL5) is a tool that implements a formal executable semantics of
PLEXIL. PLEXIL5 provides a graphical interface that enable access to
formal verification techniques such as model-checking, symbolic
execution, theorem proving, and static analysis of plans. The
graphical environment supports formula editing and visualization of
counterexamples, interactive simulation of plans at different
granularity levels, and random initialization of external environment
variables.


PLEXIL’s operational semantics has been formally specified and key
meta-theoretical properties of the language, such as determinism and
compositionality, have been formally verified in the Prototype
Verification System (PVS). This formalization yields a formal
executable semantics of the language that serves as an efficient
formal interpreter and reference implementation of PLEXIL. This formal
semantics is at the core of the verification capabilities of the
PLEXIL5. The formal analysis capabilities offered by PLEXIL5 are based
on PLEXIL’s rewriting logic semantics written in Maude and verified in
PVS.  The graphical user interface has been developed in Java using
the model-view-controller pattern. The object oriented model
represents the hierarchical structure of plans, their execution
behavior, and the external environment. The view consists of several
classes that present the user with views of the tree-like-structure of
plans. The controller consists of a custom controller-facade class and
listener classes using and extending the Java framework.  PLEXIL5
supports a number of input formats defining plans. For this purpose,
the tool links a series of parsers and translators that internally

1.  generate the format supported by the rewriting logic semantics of the
language implemented in Maude and

2. construct an object oriented
plan model from Maude’s output.

 Java and Maude communicate as processes at the operating system’s
level with help of the Java/Maude Integration API, developed as part
of the PLEXIL5 framework.

### Current Release

PLEXIL5 v0.0 (May-31-2018) 

### License

The code in this repository is released under NASA's Open Source
Agreement.  See the directory [`LICENSES`](LICENSES); see also the copyright notice at the end of this file. 

### Authors

* [C&eacute;sar A. Mu&ntilde;oz](http://shemesh.larc.nasa.gov/people/cam) (cesar.a.munoz@nasa.gov), NASA Langley Research Center, USA. 
* Hector Cadavid, Escuela Colombiana de Ingenieria, Colombia.
* Camilo Rocha, Potificia Universidad Javeriana de Cali, Colombia.
* Marco Feli&uacute;, National Institute of Aerospace, USA. 

### Copyright Notice

Copyright 2016 United States Government as represented by the Administrator of the National Aeronautics and Space Administration. All Rights Reserved.

No Warranty: THE SUBJECT SOFTWARE IS PROVIDED "AS IS" WITHOUT ANY WARRANTY OF ANY KIND, EITHER EXPRESSED, IMPLIED, OR STATUTORY, INCLUDING, BUT NOT LIMITED TO, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL CONFORM TO SPECIFICATIONS, ANY IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR FREEDOM FROM INFRINGEMENT, ANY WARRANTY THAT THE SUBJECT SOFTWARE WILL BE ERROR FREE, OR ANY WARRANTY THAT DOCUMENTATION, IF PROVIDED, WILL CONFORM TO THE SUBJECT SOFTWARE. THIS AGREEMENT DOES NOT, IN ANY MANNER, CONSTITUTE AN ENDORSEMENT BY GOVERNMENT AGENCY OR ANY PRIOR RECIPIENT OF ANY RESULTS, RESULTING DESIGNS, HARDWARE, SOFTWARE PRODUCTS OR ANY OTHER APPLICATIONS RESULTING FROM USE OF THE SUBJECT SOFTWARE.  FURTHER, GOVERNMENT AGENCY DISCLAIMS ALL WARRANTIES AND LIABILITIES REGARDING THIRD-PARTY SOFTWARE, IF PRESENT IN THE ORIGINAL SOFTWARE, AND DISTRIBUTES IT "AS IS."

Waiver and Indemnity: RECIPIENT AGREES TO WAIVE ANY AND ALL CLAIMS AGAINST THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT.  IF RECIPIENT'S USE OF THE SUBJECT SOFTWARE RESULTS IN ANY LIABILITIES, DEMANDS, DAMAGES, EXPENSES OR LOSSES ARISING FROM SUCH USE, INCLUDING ANY DAMAGES FROM PRODUCTS BASED ON, OR RESULTING FROM, RECIPIENT'S USE OF THE SUBJECT SOFTWARE, RECIPIENT SHALL INDEMNIFY AND HOLD HARMLESS THE UNITED STATES GOVERNMENT, ITS CONTRACTORS AND SUBCONTRACTORS, AS WELL AS ANY PRIOR RECIPIENT, TO THE EXTENT PERMITTED BY LAW.  RECIPIENT'S SOLE REMEDY FOR ANY SUCH MATTER SHALL BE THE IMMEDIATE, UNILATERAL TERMINATION OF THIS AGREEMENT.
