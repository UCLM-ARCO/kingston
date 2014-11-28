#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import XMLparser as xmlp
from fixDataset import fixXMLfiles
from fixDataset import symlinks
import normalization as nm

# fix dataset
print("Fixing DATASET.....")
fixXMLfiles.fixfiles()
symlinks.create_symdirs()
symlinks.create_symlinks()
print("Fixed!")

# parse dataset
print("Parsing DATASET.....")
trainingActions = {}
xmlp.parse(trainingActions, "training")
testingActions = {}
xmlp.parse(testingActions, "testing")
print("Parsed!")

# normalize dataset
print("Normalizing DATASET.....")
nm.normalize(trainingActions)
nm.normalize(testingActions)
print("Normalized!")

# obtain action files
xmlp.createActionFiles(trainingActions, "training")
xmlp.createActionFiles(testingActions, "testing")
