#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

# if __name__ == "__main__":
#     print "This module is only to be used as an import in the main program."
#     exit()


import xml.etree.ElementTree as ET
import os
import fixXMLfiles
import symlinks

currentdir = os.getcwd()


def getJoints(xmlpath):
    tree = ET.parse(xmlpath)
    root = tree.getroot()
    joints = root[2][0][5]
    coordinatesList = ()

    for joint in joints:
        if joint.tag != "center_of_mass" and joint.tag != "bounding_box":
            coordinates = (joint[0][0].text, joint[0][1].text, joint[0][2].text)
            coordinatesList += coordinates

    return coordinatesList


truthDir = os.path.join(currentdir, "RecogidaDatos/truth")
truthFiles = os.listdir(truthDir)
guidedActions = sorted(truthFiles)[0:33]
esqueleton = guidedActions[1:32:3]
esqueletonSorted = sorted(esqueleton, key = lambda x: (len(x), x.split('_')[0]))

dic = {}

# basic example of code for action 1
for itemX in esqueletonSorted:
    itemXdir = os.path.join(truthDir, itemX)

    fd = open(itemXdir, "r")

    i = 0
    while True:
        line = fd.readline()
        if not line:
            break
        else:
            action = int(line.split()[2])
            if action == 1: # action 1: mirar el reloj
                dic[i] = (line.split()[0], line.split()[1])
                i += 1


    fd.close()

    for x in range(len(dic)):
        startFrame = dic[x][0]
        endFrame = dic[x][1]
        print(startFrame, endFrame)
        # iterar entre inicio y fin sacando las listas de lox XML



    break #quitar luego

fixXMLfiles.fixfiles()
symlinks.create_symdirs()
symlinks.create_symlinks()
