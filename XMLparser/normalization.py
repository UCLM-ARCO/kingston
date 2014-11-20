#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import numpy as np
import scipy
import scipy.spatial as sp
import time


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
    op2 = -2*(XY)
    res = op1 + op2
    euclideanDistanceSum = np.power(res,0.5).sum()

    return euclideanDistanceSum


def euclideanDistanceByPoint(x, y):
    return np.linalg.norm(x-y)


#TEST TIMES

# test time using own function
def timeMatrix(sequences):
    allJoints = []
    frames = sequences[0]

    tStart = time.time()
    for iFrame in frames:
        joints = iFrame.getJoints()
        allJoints += joints

    allJoints = np.array(allJoints)
    dist = euclideanDistanceByMatrix(allJoints, np.array([3,3,3]))
    tEnd = time.time() - tStart

    return dist, tEnd


# test time using numpy.linalg.norm() funcion -> calculating euclidean distance
# point by point through a loop
def timePointByPoint(sequences):
    allJoints = []
    dist = 0
    frames = sequences[0]

    tStart = time.time()
    for iFrame in frames:
        joints = iFrame.getJoints()
        allJoints += joints

    allJoints = np.array(allJoints)

    for iJoint in allJoints:
        dist += np.linalg.norm(iJoint-np.array([3,3,3]))
    tEnd = time.time() - tStart

    return dist, tEnd


# test time using scipy.spatial.distance.cdist() -> adding all points to a matrix
# and using cdist() to calc the sum of all distances between matrix and origin
# BEST RESULTS
def timeCDist(sequences):
    allJoints = []
    frames = sequences[0]

    tStart = time.time()
    for iFrame in frames:
        joints = iFrame.getJoints()
        allJoints += joints

    allJoints = scipy.matrix(allJoints)

    dist = sp.distance.cdist(allJoints, scipy.matrix([3,3,3])).sum()
    tEnd = time.time() - tStart

    return dist, tEnd
