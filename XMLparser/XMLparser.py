#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    exit()


import xml.etree.ElementTree as ET
import os
import fixXMLfiles
import symlinks

currentdir = os.getcwd()
joints = {}


def getJointsFromXML(xmlpath):
    tree = ET.parse(xmlpath)
    root = tree.getroot()

    if not len(root[2]) == 1:
        return ()
    else:
        joints = root[2][0][5]
        coordinatesList = ()
        for joint in joints:
            if joint.tag != "center_of_mass" and joint.tag != "bounding_box":
                coordinates = (joint[0][0].text, joint[0][1].text, joint[0][2].text)
                coordinatesList += coordinates

        return coordinatesList


def errorCase(action, j):
    if (action == 11 and j == 9) or (action == 12 and j == 9) or (action == 13 and j == 6) or (action == 13 and j == 7) or (action == 13 and j == 9) or (action == 14 and j == 0) or (action == 14 and j == 1) or (action == 14 and j == 2) or (action == 14 and j == 3) or (action == 14 and j == 5) or (action == 14 and j == 6) or (action == 14 and j == 7) or (action == 14 and j == 8) or (action == 14 and j == 9):
        return True
    else:
        return False


def parser(joints):
    truthDir = os.path.join(currentdir, "RecogidaDatos/truth")
    actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")

    truthFiles = os.listdir(truthDir)         # all truth files
    guidedActions = sorted(truthFiles)[0:33]  # 'accionesX' truth files
    esqueleton = guidedActions[1:32:3]        # 'accionesX_Esqueleto' truth files

    esqueletonSorted = sorted(esqueleton,
                              key = lambda x: (len(x), x.split('_')[0]))

    sequences = os.listdir(actoresDir)        # 'acciones[1-11]' and 'actor[1-15]' dirs
    sequencesSorted = sorted(sequences,
                             key = lambda x: (len(x), x.split('([0-9])')))[15:] # 'acciones[1-11]' dirs sorted

    for action in range(0, 15):

        print("- Procesando accion:" + str(action) + "...")

        joints[action] = ()     # inicializar el diccionario de la accion en cuestion
        j = 0                   # variable para referenciar dentro de "actores/" a que secuencia "accionesX/" accedemos

        for itemX in esqueletonSorted:
            dicTmp = {}         # inicializar el diccionario temporal de cada accion
            itemXdir = os.path.join(truthDir, itemX)

            print("----- Abriendo: " + itemXdir + "...")
            fd = open(itemXdir, "r")

            i = 0
            while True:
                line = fd.readline()
                if not line:
                    break
                else:
                    actionAux = int(line.split()[2])
                    if actionAux == action:
                        dicTmp[i] = (line.split()[0], line.split()[1])
                        i += 1

            print("----- Cerrando: " + itemXdir + "...")
            fd.close()

            if not i == 0:

                sequenceXMLDir = os.path.join(actoresDir, sequencesSorted[j] + "/depth/xml_symlink")
                sequenceXMLFiles = sorted(os.listdir(sequenceXMLDir),
                                          key = lambda x: int((x.split('_')[1]).split('.')[0]))

                print("--------- Xml's dentro de acciones" + str(j+1) + " procesandose.")

                # if que se salte los errores del dataset, en funcion de j y action
		if not errorCase(action, j):
                    for x in range(len(dicTmp)):
                        startFrame = int(dicTmp[x][0])
                        endFrame = int(dicTmp[x][1])

                        for y in range(startFrame, endFrame+1):
                            frameDir = os.path.join(sequenceXMLDir, sequenceXMLFiles[y])
                            joints[action] += getJointsFromXML(frameDir)

                print("--------- Xml's dentro de acciones" + str(j+1) + " procesados.")

            j += 1
