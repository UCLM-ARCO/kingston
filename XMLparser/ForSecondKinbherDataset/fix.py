#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import os
import sys
import errno
import locale

locale.setlocale(locale.LC_ALL, "")
currentdir = os.getcwd()
actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")

def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else:
            raise

def create_symdirs(actorDir):
	mkdir_p(os.path.join(actorDir, "depth/xml_symlink"))

def fixNameXML(actorDir):
	xmlDir = os.path.join(actorDir, "depth/xml")
	xmlLinkDir = os.path.join(actorDir, "depth/xml_symlink")

	xmlFiles = os.listdir(xmlDir)
	xmlFiles_sorted = sorted(xmlFiles, key=lambda n: locale.atof(n.split('_')[1]))

    	index = 0

	for xml in xmlFiles_sorted:
		actor = xml.split('_')[0]
		timestamp = xml.split('_')[1]

		xml_name = os.path.join(xmlDir, xml)
		linkXML_name = os.path.join(xmlLinkDir, actor + "_%i_" % (index) + timestamp + "_depth.xml")

		try:
			os.symlink(xml_name, linkXML_name)
            		index = index + 1
	    	except OSError as exc:
			if exc.errno == errno.EEXIST and os.path.isfile(xmlFile_name):
			    	pass
			else:
			    	raise

for dirActores in os.listdir(actoresDir):
	actorDir = os.path.join(actoresDir, dirActores)
	create_symdirs(actorDir)

	print "Fixing " + actorDir
	fixNameXML(actorDir)
