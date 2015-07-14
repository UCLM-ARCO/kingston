#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import os
import sys
import errno

currentdir = os.getcwd()
truthDir = os.path.join(currentdir, "RecogidaDatos/truth")


def fixTruthFile(filetruth):
	filepath = os.path.join(truthDir, filetruth)
	
	with open(filepath, "r") as fr:
		data = fr.read()
		dataSplited = data.split('\n')
		fr.close()
		os.remove(filepath)
	
	with open(filepath, "w") as fw:
		initLine = dataSplited[0]
		initLineSplited = initLine.split(' ')
		dataAux = "" + initLineSplited[0]
		AfterAction = initLineSplited[2]
		
		for line in dataSplited:
			if line == '':
				break
				
			lineSplited = line.split(' ')
			CurrentInitFrame = lineSplited[0]
			CurrentEndFrame = lineSplited[1]
			CurrentAction = lineSplited[2]
			
			if CurrentAction == AfterAction :
				AfterEndFrame = CurrentEndFrame
			else:
				dataAux += " " + AfterEndFrame + " " + AfterAction + "\n" + CurrentInitFrame
				AfterAction = CurrentAction
				AfterEndFrame = CurrentEndFrame
			
		dataAux += " " + AfterEndFrame + " " + AfterAction
		fw.write(dataAux)
		fw.close()
		
for filetruth in os.listdir(truthDir):
	
	print "Fixing " + filetruth
	fixTruthFile(filetruth)
	
	
	
	
	
