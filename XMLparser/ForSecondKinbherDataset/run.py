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
print("--- parsing training actions")
trainingActions = {}
xmlp.parseTrainingActions(trainingActions)
print("--- parsing testing actions for sequence recognition")
testingSeqActions = {}
xmlp.parseTestingActionsForSequenceRecognition(testingSeqActions)
print("--- parsing testing actions for continuous recognition")
testingConActions = {}
xmlp.parseTestingActionsForContinuousRecognition(testingConActions)
print("Parsed!")

# normalize dataset
print("Normalizing DATASET.....")
nm.normalize(trainingActions)
nm.normalize(testingSeqActions)
nm.normalize(testingConActions)
print("Normalized!")

# obtain action files
xmlp.createTrainingFiles(trainingActions)
xmlp.createTestingFiles(testingSeqActions, "sequence")
xmlp.createTestingFiles(testingConActions, "continuous")
