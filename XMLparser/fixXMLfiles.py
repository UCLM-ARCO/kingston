#!/usr/bin/python
# -*- coding: utf-8; mode: python -

import sys
import os
import xml.etree.ElementTree as ET

# check the path parameter
dirpath = sys.argv[1]
pathlen = len(dirpath)
if dirpath[pathlen-1] != '/':
    dirpath += '/'

for filename in os.listdir(dirpath):
    filepath = dirpath+filename
    fr = open(filepath, "r")

    str = fr.read()
    strSplited = str.split(' ')
    strAux = '<?xml version="1.0" '
    strAux += strSplited[2]

    fr.close()
    os.remove(filepath)

    fw = open(filepath, "w")
    fw.write(strAux)
    fw.close()
