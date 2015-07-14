#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import os
import sys
import errno

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
	mkdir_p(os.path.join(actorDir, "depth_symlink/xml_symlink"))
	mkdir_p(os.path.join(actorDir, "esqueleto_symlink"))
	mkdir_p(os.path.join(actorDir, "rgb_symlink"))

def fixNameImages(actorDir, type_image):
	imageDir = os.path.join(actorDir, type_image)
	imageLinkDir = os.path.join(actorDir, type_image + "_symlink")

	imageFiles = os.listdir(imageDir)

	if(type_image == "depth"):
		for element in imageFiles:
			if (os.path.isdir(os.path.join(imageDir, element))):
				imageFiles.remove(element)
				break

	imageFiles_sorted = sorted(imageFiles, key=lambda n: int(n.split('_')[1]))

	offset = int (imageFiles_sorted[0].split('_')[1])

	for image in imageFiles_sorted:
		actor = image.split('_')[0]
		iframe = int (image.split('_')[1])
		timestamp = image.split('_')[2]

		image_name = os.path.join(imageDir, image)
		linkImage_name = os.path.join(imageLinkDir, actor + "_%i_" % (iframe - offset) + timestamp + "_" + type_image + ".jpg")

		try:
			os.symlink(image_name, linkImage_name)
	    	except OSError as exc:
			if exc.errno == errno.EEXIST and os.path.isfile(xmlFile_name):
			    	pass
			else:
			    	raise

def fixNameXML(actorDir):
	xmlDir = os.path.join(actorDir, "depth/xml")
	xmlLinkDir = os.path.join(actorDir, "depth_symlink/xml_symlink")

	xmlFiles = os.listdir(xmlDir)
	xmlFiles_sorted = sorted(xmlFiles, key=lambda n: int(n.split('_')[1]))

	offset = int (xmlFiles_sorted[0].split('_')[1])

	for xml in xmlFiles_sorted:
		actor = xml.split('_')[0]
		iframe = int (xml.split('_')[1])
		timestamp = xml.split('_')[2]

		xml_name = os.path.join(xmlDir, xml)
		linkXML_name = os.path.join(xmlLinkDir, actor + "_%i_" % (iframe - offset) + timestamp + "_depth.xml")

		try:
			os.symlink(xml_name, linkXML_name)
	    	except OSError as exc:
			if exc.errno == errno.EEXIST and os.path.isfile(xmlFile_name):
			    	pass
			else:
			    	raise

def fixAnyXML(xmlLinkDir):
	xmlLinkFiles = os.listdir(xmlLinkDir)
	xmlLinkFiles_sorted = sorted(xmlLinkFiles, key=lambda n: int(n.split('_')[1]))
	anterior = -1

	for xmlLink in xmlLinkFiles_sorted:
		actor = xmlLink.split('_')[0]
		iframe = int (xmlLink.split('_')[1])
		timestamp = xmlLink.split('_')[2]

		if (iframe != anterior + 1):
			for i in range(anterior + 1, iframe):
				filepath = os.path.join(xmlLinkDir, actor + "_%i" %i + "_000000000_depth.xml")
				print "Nuevo XML: " + filepath
				with open(filepath, "w") as fw:
				    data = '<?﻿xml version="1.0" encoding="UTF-8"?><frame><timestamp>000000000</timestamp><frame_index>%i</frame_index><users/></frame>' %i
				    fw.write(data)

				fw.close()

		anterior = iframe

for dirActores in os.listdir(actoresDir):
	actorDir = os.path.join(actoresDir, dirActores)
	create_symdirs(actorDir)

	print "Fixing " + actorDir
	fixNameImages(actorDir, "rgb")
	fixNameImages(actorDir, "esqueleto")
	fixNameImages(actorDir, "depth")
	fixNameXML(actorDir)
	fixAnyXML(os.path.join(actoresDir, dirActores + "/depth_symlink/xml_symlink"))
