#!/usr/bin/python
# -*- coding: utf-8; mode: python -

import os

currentdir = os.getcwd()


def fixdir(path):
    for fileX in os.listdir(path):
        filepath = os.path.join(path, fileX)
        with open(filepath, "r") as fr:
            data = fr.read()
            dataSplited = data.split(' ')
            dataAux = '<?xml version="1.0" '
            dataAux += dataSplited[2]

        fr.close()
        os.remove(filepath)

        with open(filepath, "w") as fw:
            fw.write(dataAux)

        fw.close()


def fixfiles():
    actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
    for itemX in os.listdir(actoresDir):
        xmlDir = os.path.join(actoresDir, "%s/depth_symlink/xml_symlink" % itemX)
        fixdir(xmlDir)


if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    exit()
