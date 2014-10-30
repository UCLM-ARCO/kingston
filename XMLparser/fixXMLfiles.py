#!/usr/bin/python
# -*- coding: utf-8; mode: python -

import sys
import os
import errno

currentdir = os.getcwd()


def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else:
            raise


def create_symdirs():
    for item in os.listdir(currentdir + "/videos/actores"):
        dirpath = currentdir + "/videos/actores/" + item
        mkdir_p(dirpath + "/depth/xml_symlink")


def create_symlinks():
    actoresDir = os.path.join(currentdir, "videos/actores")
    for itemX in os.listdir(actoresDir):
        # dirs to use
        accionesDir = os.path.join(actoresDir, itemX)
        xmlDir = os.path.join(accionesDir, "depth/xml")
        linkDir = os.path.join(accionesDir, "depth/xml_symlink")
        # list and sort xml files
        xmlFiles = os.listdir(xmlDir)
        xmlFiles_sorted = sorted(xmlFiles, key=lambda n: int(n.split('_')[1]))
        # iterate through xml sorted files and create links
        i = 0
        for fileX in xmlFiles_sorted:
            xmlFile_name = os.path.join(xmlDir, fileX)
            linkFile_name = os.path.join(linkDir, "frame_" + str(i) + ".xml")
            os.symlink(xmlFile_name, linkFile_name)


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
    for itemX in actoresDir:
        xmlDir = os.path.join(actoresDir, itemX + "/depth/xml")
        fixXMLdir(xmlDir)


create_symlinks()
