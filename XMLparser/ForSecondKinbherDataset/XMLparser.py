#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import frame as fr
import os
import sys
import numpy as np

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET


def get_truth_files(truth_files, type_file, type_image):
        items = []
        for item in truth_files:
                if (type_file in item) and (type_image in item):
                        items.append(item)
        return items

currentdir = os.getcwd()
truthDir = os.path.join(currentdir, "RecogidaDatos/truth")
actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
truthFiles = os.listdir(truthDir)                             # all truth files

accionesXEsqueleto_truthFiles = get_truth_files(os.listdir(truthDir), "actions", "Skeleton")    # 'accionesX_Esqueleto' truth files

actorXEsqueleto_truthFiles = get_truth_files(os.listdir(truthDir), "actor", "Skeleton")         # 'actorX_Esqueleto' truth files


def getFrameFromXML(xmlpath):
    tree = ET.parse(xmlpath)
    root = tree.getroot()
    existFrame = False

    if len(root[1]) == 0:
        frame = fr.Frame()
    else:
        existFrame = True
        timestamp = root[0].text
        user_index = root[1][0][0].text
        joints = root[1][0][4]

        for joint in joints:
            if joint.tag == "joint_SpineBase":
                joint_SpineBase = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_SpineMid":
                joint_SpineMid = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_Neck":
                joint_Neck = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_Head":
                joint_Head = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ShoulderLeft":
                joint_ShoulderLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ElbowLeft":
                joint_ElbowLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_WristLeft":
                joint_WristLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HandLeft":
                joint_HandLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ShoulderRight":
                joint_ShoulderRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ElbowRight":
                joint_ElbowRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_WristRight":
                joint_WristRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HandRight":
                joint_HandRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HipLeft":
                joint_HipLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_KneeLeft":
                joint_KneeLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_AnkleLeft":
                joint_AnkleLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_FootLeft":
                joint_FootLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HipRight":
                joint_HipRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_KneeRight":
                joint_KneeRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_AnkleRight":
                joint_AnkleRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_FootRight":
                joint_FootRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_SpineShoulder":
                joint_SpineShoulder = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HandTipLeft":
                joint_HandTipLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ThumbLeft":
                joint_ThumbLeft = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_HandTipRight":
                joint_HandTipRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_ThumbRight":
                joint_ThumbRight = [joint[0][0].text, joint[0][1].text, joint[0][2].text] 
            else:
                pass

        frame = fr.Frame(timestamp, user_index, joint_SpineBase, joint_SpineMid, 
        joint_Neck, joint_Head, joint_ShoulderLeft, joint_ElbowLeft, joint_WristLeft, 
        joint_HandLeft, joint_ShoulderRight, joint_ElbowRight, joint_WristRight, 
        joint_HandRight, joint_HipLeft, joint_KneeLeft, joint_AnkleLeft, joint_FootLeft, 
        joint_HipRight, joint_KneeRight, joint_AnkleRight, joint_FootRight, joint_SpineShoulder, 
        joint_HandTipLeft, joint_ThumbLeft, joint_HandTipRight, joint_ThumbRight)

    return existFrame, frame

def getSequenceListing(itemXdir, action):
    dicTmp = {}
    i = 0
    with open(itemXdir, "r") as fd:
        while True:
            line = fd.readline()
            if not line:
                break
            else:
                actionAux = int(line.split()[2])
                if actionAux == action:
                    dicTmp[i] = (line.split()[0], line.split()[1])
                    i += 1

    fd.close()
    return dicTmp, i


def getJointsPerAction(sequences):
    joints = []
    extendJoints = joints.extend
    for iSequence in sequences.keys():
        for iFrame in sequences[iSequence]:
            extendJoints(iFrame.getJoints())

    return np.array(joints)


def getJointsPerSequence(sequence):
    joints = []
    extendJoints = joints.extend
    for iFrame in sequence:
        extendJoints(iFrame.getJoints())

    return np.array(joints)


def generateListOfFramesBySequence(startFrame, endFrame, actions, sequenceXMLDir, sequenceXMLFiles, actionIndex, sequenceIndex):
    appendIntoSequence = actions[actionIndex][sequenceIndex].append
    for i in range(startFrame, endFrame + 1):
        frameFile = os.path.join(sequenceXMLDir, sequenceXMLFiles[i])
        existFrame, frame = getFrameFromXML(frameFile)
        if existFrame:
            appendIntoSequence(frame)
        else:
            pass


def loopThroughActions(actions, esqueletonFiles, sequencesDirs, step):
    # loop to parse dataset action per action
    for action in range(0, 15):

        actions[action] = {}     # init dictionary of each action
        j = 0                    # variable to realize the matching between "actores/" and "acciones/, actor/" folders
        sequence = 0             # sequences counter

        for itemX in esqueletonFiles:
            itemXdir = os.path.join(truthDir, itemX)

            dicTmp, i = getSequenceListing(itemXdir, action)

            if not i == 0:

                sequenceXMLDir = os.path.join(actoresDir, "%s/depth/xml_symlink" % sequencesDirs[j])
                sequenceXMLFiles = sorted(os.listdir(sequenceXMLDir),
                                          key = lambda x: int(x.split('_')[1]))

                for x in range(len(dicTmp)):
                        startFrame = int(dicTmp[x][0])
                        endFrame = int(dicTmp[x][1])

                        actions[action][sequence] = []
                        generateListOfFramesBySequence(startFrame, endFrame, actions, sequenceXMLDir, sequenceXMLFiles, action, sequence)
                        sequence += 1

            j += 1


def createTrainingFiles(actions):
    filedir = os.path.join(currentdir, "RecogidaDatos/actionsFiles/training")
    mkdir_p(filedir)

    for iAction in actions.keys():
        for iSequence in actions[iAction].keys():
            filename = os.path.join(filedir, "action%ssequence%s.txt" % (iAction, iSequence))
            joints = getJointsPerSequence(actions[iAction][iSequence])

            with open(filename, "w") as fd:
                for iJoint in joints:
                    fd.write("%s " % iJoint[0])
                    fd.write("%s " % iJoint[1])
                    fd.write("%s " % iJoint[2])

            fd.close()


def createTestingFiles(actions, recognitionType):
    if recognitionType == "sequence" or recognitionType == "continuous":
        filedir = os.path.join(currentdir, "RecogidaDatos/actionsFiles/testing-%s" % (recognitionType))
        mkdir_p(filedir)

        for iAction in actions.keys():
            for iSequence in actions[iAction].keys():
                filename = os.path.join(filedir, "action%ssequence%s.txt" % (iAction, iSequence))
                joints = getJointsPerSequence(actions[iAction][iSequence])

                with open(filename, "w") as fd:
                    for iJoint in joints:
                        fd.write("%s " % iJoint[0])
                        fd.write("%s " % iJoint[1])
                        fd.write("%s " % iJoint[2])

                fd.close()

    else:
        print("Error! recognition type should be 'sequence' or 'continuous'")
        sys.exit()


def parseTrainingActions(actions):
    accionesXEsqueleto_truthFilesSorted = sorted(accionesXEsqueleto_truthFiles,
                                                 key = lambda x: (len(x), x.split('_')[0]))

    accionesX_dirsSorted = sorted(os.listdir(actoresDir),
                                  key = lambda x: (len(x), x.split('([0-9])')))[17:]  # 'acciones[1-18]' dirs sorted by name

    loopThroughActions(actions, accionesXEsqueleto_truthFilesSorted, accionesX_dirsSorted, "training")


def parseTestingActionsForSequenceRecognition(actions):
    actorXEsqueleto_truthFilesSorted = sorted(actorXEsqueleto_truthFiles,
                                              key = lambda x: (len(x), x.split('_')[0]))

    actorX_dirsSorted = sorted(os.listdir(actoresDir),
                               key = lambda x: (len(x), x.split('([0-9])')))[:17]  # 'actor[1-17]' dirs sorted by name

    loopThroughActions(actions, actorXEsqueleto_truthFilesSorted, actorX_dirsSorted, "testing")


def parseTestingActionsForContinuousRecognition(actions):
    actorCount = 0
    actorX_dirsSorted = sorted(os.listdir(actoresDir),
                               key = lambda x: (len(x), x.split('([0-9])')))[:17]

    # loop to parse dataset actor per actor
    for itemX in actorX_dirsSorted:
        actions[actorCount + 1] = {}
        sequence = 0
        truthFile_name = os.path.join(truthDir, "%sSkeleton_frame_segmented.txt" % itemX)

        with open(truthFile_name, "r") as fd:
            while True:
                line = fd.readline()
                if not line:
                    break
                else:
                    # en caso de error, evaluar actorCount y sequence
                    startFrame = int(line.split()[0])
                    endFrame = int(line.split()[1])

                    sequenceXMLDir = os.path.join(actoresDir, "%s/depth/xml_symlink" % actorX_dirsSorted[actorCount])                              
                    sequenceXMLFiles = sorted(os.listdir(sequenceXMLDir),
                                              key = lambda x: int(x.split('_')[1]))
                                          
                    actions[actorCount + 1][sequence] = []
                    generateListOfFramesBySequence(startFrame, endFrame, actions, sequenceXMLDir, sequenceXMLFiles, actorCount + 1, sequence)
                    sequence += 1

        fd.close()
        actorCount += 1

def mkdir_p(path):
    try:
        os.makedirs(path)
    except OSError as exc:
        if exc.errno == errno.EEXIST and os.path.isdir(path):
            pass
        else:
            raise

if __name__ == "__main__":
    print ("This module is only to be used as an import in the main program.")
    sys.exit()
