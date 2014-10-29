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
        else: raise

def create_symdirs():
    for item in os.listdir(currentdir + "/videos/actores"):
        dirpath = currentdir + "/videos/actores/" + item
        mkdir_p(dirpath + "/depth/xml_symlink")

def create_symlinks():
    actoresDir = os.path.join(currentdir, "videos/actores")
    for itemX in os.listdir(actoresDir):
        accionesDir = os.path.join(actoresDir, itemX)
        print(accionesDir)
        # xmlfiles_sorted = sorted(inXmlDir, key=lambda n: int(n.split('_')[1]))
        # print(xmlfiles_sorted[0])
        # print(xmlfiles_sorted[1])
        # print(xmlfiles_sorted[2])
        # print("hola")


create_symlinks()







# check the path parameter
# dirpath = sys.argv[1]
# pathlen = len(dirpath)
# if dirpath[pathlen-1] != '/':
#     dirpath += '/'

# for filename in os.listdir(dirpath):
#     filepath = dirpath+filename
#     fr = open(filepath, "r")

#     str = fr.read()
#     strSplited = str.split(' ')
#     strAux = '<?xml version="1.0" '
#     strAux += strSplited[2]

#     fr.close()
#     os.remove(filepath)

#     fw = open(filepath, "w")
#     fw.write(strAux)
#     fw.close()
