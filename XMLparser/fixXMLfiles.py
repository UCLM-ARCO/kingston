#!/usr/bin/python
# -*- coding: utf-8; mode: python -

if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    exit()


import os
import errno

currentdir = os.getcwd()


def fixXMLdir(path):
    for fileX in os.listdir(path):
        filepath = os.path.join(path, fileX)
        fr = open(filepath, "r")

        data = fr.read()
        dataSplited = data.split(' ')
        dataAux = '<?xml version="1.0" '
        dataAux += dataSplited[2]

        fr.close()
        os.remove(filepath)

        fw = open(filepath, "w")
        fw.write(dataAux)
        fw.close()


def fixXMLfiles():
    actoresDir = os.path.join(currentdir, "videos/actores")
    for itemX in os.listdir(actoresDir):
        xmlDir = os.path.join(actoresDir, itemX + "/depth/xml")
        fixXMLdir(xmlDir)
