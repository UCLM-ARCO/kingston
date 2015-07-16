#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import numpy as np
import scipy
import scipy.spatial as sp
import time


def obtainFrames(sequences):
    allFrames = []
    extendFrames = allFrames.extend
    for iSequence in sequences.keys():
        extendFrames(sequences[iSequence])

    return allFrames


def normalize(actions):
    jointsPerFrame = 25
    for action in actions.keys():
        if not action == 0:
            framesInAction = obtainFrames(actions[action])

            for frame in framesInAction:
                origin = np.array([0, 0, 0])

                # calculate center of mass
                joints = frame.getJoints()
                for joint in joints:
                    origin[0] += joint[0]
                    origin[1] += joint[1]
                    origin[2] += joint[2]

                origin[0] /= jointsPerFrame
                origin[1] /= jointsPerFrame
                origin[2] /= jointsPerFrame

                # calculate normalization distance
                jointsMatrix = scipy.matrix(joints)
                originMatrix = scipy.matrix(origin)
                normDistance = sp.distance.cdist(jointsMatrix, originMatrix).sum()
                normDistance /= jointsPerFrame

                # normalize to translation
                joints = np.array(joints)
                for joint in joints:
                    joint[0] = float((joint[0] - origin[0]) / normDistance)
                    joint[1] = float((joint[1] - origin[1]) / normDistance)
                    joint[2] = float((joint[2] - origin[2]) / normDistance)

                frame.updateJointsValue(joints)

                # rotation
                angle = np.arctan(((frame.joint_ShoulderLeft[2] + frame.joint_HipLeft[2]) / 2 -
                                   (frame.joint_ShoulderRight[2] + frame.joint_HipRight[2]) / 2) /
                                  ((frame.joint_ShoulderLeft[0] + frame.joint_HipLeft[0]) / 2 -
                                   (frame.joint_ShoulderRight[0] + frame.joint_HipRight[0]) / 2))

                joints = frame.getJoints()
                for joint in joints:
                    a = joint[0] - frame.joint_Neck[0]
                    b = joint[2] - frame.joint_Neck[2]
                    joint[0] = a * np.cos(angle) + b * np.sin(angle)
                    joint[2] = -a * np.sin(angle) + b * np.cos(angle)

                frame.updateJointsValue(joints)


def euclideanDistanceByMatrix(x, y):
    # to obtain the sum of each euclidean distance between the points
    # in X and the point Y. We need to calc: sqrt(x2+y2-2xy) and get the
    # sum of all of his elements

    X = np.matrix(x)
    Y = np.matrix(y)

    Xsum = np.sum(np.power(X, 2), 1)
    Ysum = np.sum(np.power(Y, 2), 1)
    XY = np.dot(X, Y.T)

    op1 = Xsum + Ysum
    op2 = -2 * (XY)
    res = op1 + op2
    euclideanDistanceSum = np.power(res, 0.5).sum()

    return euclideanDistanceSum


#TEST TIMES

# test time using own function
def timeMatrix(sequences):
    allJoints = []
    extendJoints = allJoints.extend

    tStart = time.time()
    for iSequence in sequences:
        for iFrame in sequences[iSequence]:
            extendJoints(iFrame.getJoints())

    allJoints = np.array(allJoints)
    dist = euclideanDistanceByMatrix(allJoints, np.array([3, 3, 3]))
    tEnd = time.time() - tStart

    return dist, tEnd


# test time using numpy.linalg.norm() funcion -> calculating euclidean distance
# point by point through a loop
def timePointByPoint(sequences):
    allJoints = []
    extendJoints = allJoints.extend
    dist = 0

    tStart = time.time()
    for iSequence in sequences:
        for iFrame in sequences[iSequence]:
            extendJoints(iFrame.getJoints())

    allJoints = np.array(allJoints)

    for iJoint in allJoints:
        dist += np.linalg.norm(iJoint - np.array([3, 3, 3]))
    tEnd = time.time() - tStart

    return dist, tEnd


# test time using scipy.spatial.distance.cdist() -> adding all points to a matrix
# and using cdist() to calc the sum of all distances between matrix and origin
# BEST RESULTS
def timeCDist(sequences):
    allJoints = []
    extendJoints = allJoints.extend

    tStart = time.time()
    for iSequence in sequences:
        for iFrame in sequences[iSequence]:
            extendJoints(iFrame.getJoints())

    allJoints = scipy.matrix(allJoints)

    dist = sp.distance.cdist(allJoints, scipy.matrix([3, 3, 3])).sum()
    tEnd = time.time() - tStart

    return dist, tEnd


if __name__ == "__main__":
    print "This module is only to be used as an import in the main program."
    exit()
