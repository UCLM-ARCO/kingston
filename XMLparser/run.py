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
actions = {}
xmlp.parser(actions)
print("Parsed!")

# normalize dataset
print("Normalizing DATASET.....")
nm.normalize(actions)
print("Normalized!")
