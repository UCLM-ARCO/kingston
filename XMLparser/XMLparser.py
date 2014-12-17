#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import frame as fr
from fixDataset import symlinks
import os
import sys
import numpy as np

try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET


currentdir = os.getcwd()
truthDir = os.path.join(currentdir, "RecogidaDatos/truth")
actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
truthFiles = os.listdir(truthDir)                             # all truth files
accionesX_truthFiles = sorted(truthFiles)[0:33]               # 'accionesX' truth files
accionesXEsqueleto_truthFiles = accionesX_truthFiles[1:32:3]  # 'accionesX_Esqueleto' truth files
actorX_truthFiles = sorted(truthFiles)[33:]                   # 'actorX' truth files
actorXEsqueleto_truthFiles = actorX_truthFiles[1:45:3]        # 'actorX_Esqueleto' truth files


def getFrameFromXML(xmlpath):
    tree = ET.parse(xmlpath)
    root = tree.getroot()
    existFrame = False

    if not len(root[2]) == 1:
        frame = fr.Frame()
    else:
        existFrame = True
        timestamp = root[0].text
        index = root[1].text
        joints = root[2][0][5]

        for joint in joints:
            if joint.tag == "joint_head":
                joint_head = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_neck":
                joint_neck = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_shoulder":
                joint_left_shoulder = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "join_right_shoulder":
                joint_right_shoulder = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_elbow":
                joint_left_elbow = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_right_elbow":
                joint_right_elbow = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_hand":
                joint_left_hand = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_right_hand":
                joint_right_hand = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_torso":
                joint_torso = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_hip":
                joint_left_hip = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_right_hip":
                joint_right_hip = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_knee":
                joint_left_knee = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_right_knee":
                joint_right_knee = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_left_foot":
                joint_left_foot = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            elif joint.tag == "joint_right_foot":
                joint_right_foot = [joint[0][0].text, joint[0][1].text, joint[0][2].text]
            else:
                pass

        frame = fr.Frame(timestamp, index, joint_head, joint_neck, joint_left_shoulder,
                         joint_right_shoulder, joint_left_elbow, joint_right_elbow,
                         joint_left_hand, joint_right_hand, joint_torso, joint_left_hip,
                         joint_right_hip, joint_left_knee, joint_right_knee, joint_left_foot, joint_right_foot)

    return existFrame, frame


def errorCase1(action, j, step):
    if step == "training":
        if ((action == 11 and j == 9) or (action == 12 and j == 9)
            or (action == 13 and j == 6) or (action == 13 and j == 7)
            or (action == 13 and j == 9) or (action == 14 and j == 0)
            or (action == 14 and j == 1) or (action == 14 and j == 2)
            or (action == 14 and j == 3) or (action == 14 and j == 5)
            or (action == 14 and j == 6) or (action == 14 and j == 7)
            or (action == 14 and j == 8) or (action == 14 and j == 9)):
            return True
        else:
            return False
    else:
        if ((action == 5 and j == 6) or (action == 6 and j == 9)
            or (action == 7 and j == 2) or (action == 7 and j == 9)):
            return True
        else:
            return False


def errorCase2(actor, sequence):
    if ((actor == 3 and sequence == 114) or (actor == 7 and sequence == 59)
        or (actor == 10 and sequence == 44)):
        return True
    else:
        return False


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
                                          key = lambda x: int((x.split('_')[1]).split('.')[0]))

                if not errorCase1(action, j, step):

                    for x in range(len(dicTmp)):
                        startFrame = int(dicTmp[x][0])
                        endFrame = int(dicTmp[x][1])

                        actions[action][sequence] = []
                        generateListOfFramesBySequence(startFrame, endFrame, actions, sequenceXMLDir, sequenceXMLFiles, action, sequence)
                        sequence += 1

            j += 1


def createTrainingFiles(actions):
    filedir = os.path.join(currentdir, "RecogidaDatos/actionsFiles/training")
    symlinks.mkdir_p(filedir)

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
        symlinks.mkdir_p(filedir)

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
                                  key = lambda x: (len(x), x.split('([0-9])')))[15:]  # 'acciones[1-11]' dirs sorted by name

    loopThroughActions(actions, accionesXEsqueleto_truthFilesSorted, accionesX_dirsSorted, "training")


def parseTestingActionsForSequenceRecognition(actions):
    actorXEsqueleto_truthFilesSorted = sorted(actorXEsqueleto_truthFiles,
                                              key = lambda x: (len(x), x.split('_')[0]))

    actorX_dirsSorted = sorted(os.listdir(actoresDir),
                               key = lambda x: (len(x), x.split('([0-9])')))[:15]  # 'actor[1-15]' dirs sorted by name

    loopThroughActions(actions, actorXEsqueleto_truthFilesSorted, actorX_dirsSorted, "testing")


def parseTestingActionsForContinuousRecognition(actions):
    actorCount = 0
    actorX_dirsSorted = sorted(os.listdir(actoresDir),
                               key = lambda x: (len(x), x.split('([0-9])')))[:15]

    # loop to parse dataset actor per actor
    for itemX in actorX_dirsSorted:
        actions[actorCount + 1] = {}
        sequence = 0
        truthFile_name = os.path.join(truthDir, "%sEsqueleto_frame_segmented.txt" % itemX)

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
                                              key = lambda x: int((x.split('_')[1]).split('.')[0]))

                    actions[actorCount + 1][sequence] = []
                    if not errorCase2(actorCount + 1, sequence):
                        generateListOfFramesBySequence(startFrame, endFrame, actions, sequenceXMLDir, sequenceXMLFiles, actorCount + 1, sequence)
                        sequence += 1

        fd.close()
        actorCount += 1


if __name__ == "__main__":
    print ("This module is only to be used as an import in the main program.")
    sys.exit()
