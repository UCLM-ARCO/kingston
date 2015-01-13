# Description

This repository includes project content for humans actions recognition forward a number of people with a video sequence. This analysis is supported by the common sense knowledge, which improves the detection of actions and the future prediction. 

This project is based in other project developed previosly. In this project, some improvements are add for increased success rate.

# Estructure

It content is divided into several folders, which can be differentiated into two groups.
One side are those that refer to the algorithmic part of the system, while on the other side are the reasoning part of the system. 

The folders of reasoning part of the system are: Multilevel_activities and scone. 

- Multilevel_activities folder: this folder is compose by cshar-components folder and cshar.lisp file. This file has the statements to load the specified knowledge in the knowledge base Scone. 

- Cshar-components folder: this folder has all files about modeling knowledge referring movements, actions, events and list of expected actions. These are developed in LIPS terms.

- scone folder: this folder contains knowledge modeling of previous project. It is the base for the new knowledge in new terms of Scone. 

# New knowledge load in Scone

For load knowledge in Scone, initially is necessary to copy the cshar-components folder an cshar.lisp file into the knowledge base Scone. 

Later, it is start and load Scone, opening the emacs tool and starting the slime tool. The next step is introduce the path of the pathToFiles file (this is the file for load the several modules of knowledge). Now, the Scone system is ready to work.