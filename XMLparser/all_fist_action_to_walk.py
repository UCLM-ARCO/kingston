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
                dataAux= ""
                
                if(int(initLineSplited[2]) != 7):
                        dataAux = "0 {} 7\n".format(int(initLineSplited[0]) - 1)
                
                for line in dataSplited:
                        if line == '':
				break
                        lineSplited = line.split(' ')
                        dataAux += "{} {} {}\n".format(lineSplited[0], lineSplited[1], lineSplited[2])
                
                fw.write(dataAux)
		fw.close()
		
for filetruth in os.listdir(truthDir):	
	print "Fixing " + filetruth
	fixTruthFile(filetruth)
	
	
	
	
	
