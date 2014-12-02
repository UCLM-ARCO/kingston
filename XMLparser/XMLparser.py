#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import xml.etree.ElementTree as ET
import os
import sys
import errno
import frame as fr
from fixDataset import symlinks
import numpy as np

currentdir = os.getcwd()


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


def errorCase(action, j, step):
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


def getSequencesPerAction(itemXdir, action):
    dicTmp = {}
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

    fd.close()
    return dicTmp, i


def getJointsPerAction(sequences):
    joints = []
    for iSequence in sequences.keys():
        for iFrame in sequences[iSequence]:
            joints += iFrame.getJoints()

    return np.array(joints)


def parse(actions, step):
    truthDir = os.path.join(currentdir, "RecogidaDatos/truth")
    actoresDir = os.path.join(currentdir, "RecogidaDatos/videos/actores")
    truthFiles = os.listdir(truthDir)         # all truth files
    sequences = os.listdir(actoresDir)        # 'acciones[1-11]' and 'actor[1-15]' dirs

    # select training or testing files
    if step == "training":
        guidedActions = sorted(truthFiles)[0:33]  # 'accionesX' truth files
        esqueleton = guidedActions[1:32:3]        # 'accionesX_Esqueleto' truth files

        esqueletonSorted = sorted(esqueleton,
                                  key = lambda x: (len(x), x.split('_')[0]))

        sequencesSorted = sorted(sequences,
                                 key = lambda x: (len(x), x.split('([0-9])')))[15:]  # 'acciones[1-11]' dirs sorted by name

    elif step == "testing":
        unguidedActions = sorted(truthFiles)[33:]  # 'actorX' truth files
        esqueleton = unguidedActions[1:45:3]       # 'actorX_Esqueleto' truth files

        esqueletonSorted = sorted(esqueleton,
                                  key = lambda x: (len(x), x.split('_')[0]))

        sequencesSorted = sorted(sequences,
                                 key = lambda x: (len(x), x.split('([0-9])')))[:15]  # 'actor[1-15]' dirs sorted by name

    else:
        print("Wrong step parameter, must be 'training' or 'testing'")
        sys.exit()

    # loop to parse dataset action per action
    for action in range(0, 15):

        actions[action] = {}     # init dictionary of each action
        j = 0                    # variable to realize the matching between "actores/" and "acciones/, actor/" folders
        sequence = 0             # sequences counter

        for itemX in esqueletonSorted:
            itemXdir = os.path.join(truthDir, itemX)

            dicTmp, i = getSequencesPerAction(itemXdir, action)

            if not i == 0:

                sequenceXMLDir = os.path.join(actoresDir, sequencesSorted[j] + "/depth/xml_symlink")
                sequenceXMLFiles = sorted(os.listdir(sequenceXMLDir),
                                          key = lambda x: int((x.split('_')[1]).split('.')[0]))

                if not errorCase(action, j, step):

                    for x in range(len(dicTmp)):
                        startFrame = int(dicTmp[x][0])
                        endFrame = int(dicTmp[x][1])

                        actions[action][sequence] = []

                        for y in range(startFrame, endFrame + 1):
                            frameDir = os.path.join(sequenceXMLDir, sequenceXMLFiles[y])
                            existFrame, frame = getFrameFromXML(frameDir)
                            if existFrame:
                                actions[action][sequence] += [frame]
                            else:
                                pass

                        sequence += 1

            j += 1


def createActionFiles(actions, step):
    if step == "training" or step == "testing":
        filedir = os.path.join(currentdir, "RecogidaDatos/actionsFiles/" + step)
        symlinks.mkdir_p(filedir)

        for iAction in actions.keys():
            filename = os.path.join(filedir, "action" + str(iAction) + ".txt")
            joints = getJointsPerAction(actions[iAction])

            try:
                fd = open(filename, "w")
            except OSError as exc:
                if exc.errno == errno.EEXIST and os.path.isfile(filename):
                    pass
                else:
                    raise

            for iJoint in joints:
                fd.write("%s " % iJoint[0])
                fd.write("%s " % iJoint[1])
                fd.write("%s " % iJoint[2])

            fd.close()

    else:
        print("Wrong step parameter, must be 'training' or 'testing'")
        sys.exit()


if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    sys.exit()
