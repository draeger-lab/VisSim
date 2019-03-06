# VisSim
Visual Simulation Module for InSilico

#### Description
VisSim provides an intuitive Graphical User Interface for dynamic model simulation of models encoded in the Systems Biology Markup Language (SBML). For this intent it contains the Systems Biology Simulation Core Library, which provides needed functionality for sufficient simulation process. VisSim runs on all platforms that provide a standard Java Virtual Machine. 

## File structure
VisSim structure:
```
 |- org.insilico.vissim.core           -> Contains OSGi Bundle capable to handle user-platform interaction
 |- org.insilico.vissim.feature        -> Corresponding feature for e4 platform
 |- org.insilico.vissim.sbscl          -> Helper-Project for simulation execution
```
## Getting started
It is desired to include all provided components manually with help of inSilico platform tools.
 * Install `org.insilico.vissim.core` OSGi plugin (Install -> Install Bundle)
 * Install `org.insilico.vissim.feature` feature project (Install -> Install Feature)

## Troubleshooting

Place to report a bug: https://github.com/draeger-lab/VisSim/issues

#### Licensing terms
This software is distributed under MIT license.

