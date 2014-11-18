#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import XMLparser as xmlp
import fixDataset as fixd

# fix dataset
fixd.fixfiles()
fixd.create_symdirs()
fixd.create_symlinks()

# parse dataset
actions = {}
xmlp.parser(actions)
