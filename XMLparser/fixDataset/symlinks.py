#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

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
    actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
    for item in os.listdir(actoresDir):
        dirpath = os.path.join(actoresDir, item)
        mkdir_p("%s/depth/xml_symlink" % dirpath)


def create_symlinks():
    actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
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
            linkFile_name = os.path.join(linkDir, "frame_%s.xml" % i)

            try:
                os.symlink(xmlFile_name, linkFile_name)
                i += 1
            except OSError as exc:
                if exc.errno == errno.EEXIST and os.path.isfile(xmlFile_name):
                    pass
                else:
                    raise


if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    exit()
