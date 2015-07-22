#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import numpy as np


class Frame(object):
    def __init__(self, timestamp=None, user_index=None, joint_SpineBase=None, 
                joint_SpineMid=None, joint_Neck=None, joint_Head=None, 
                joint_ShoulderLeft=None, joint_ElbowLeft=None, joint_WristLeft=None, 
                joint_HandLeft=None, joint_ShoulderRight=None, joint_ElbowRight=None, 
                joint_WristRight=None, joint_HandRight=None, joint_HipLeft=None, 
                joint_KneeLeft=None, joint_AnkleLeft=None, joint_FootLeft=None, 
                joint_HipRight=None, joint_KneeRight=None, joint_AnkleRight=None, 
                joint_FootRight=None, joint_SpineShoulder=None, joint_HandTipLeft=None, 
                joint_ThumbLeft=None, joint_HandTipRight=None, joint_ThumbRight=None):

        self.timestamp = timestamp
        self.user_index = user_index
        self.joint_SpineBase = np.array(joint_SpineBase, dtype=np.float_)
        self.joint_SpineMid = np.array(joint_SpineMid, dtype=np.float_)
        self.joint_Neck = np.array(joint_Neck, dtype=np.float_)
        self.joint_Head = np.array(joint_Head, dtype=np.float_)
        self.joint_ShoulderLeft = np.array(joint_ShoulderLeft, dtype=np.float_)
        self.joint_ElbowLeft = np.array(joint_ElbowLeft, dtype=np.float_)
        self.joint_WristLeft = np.array(joint_WristLeft, dtype=np.float_)
        self.joint_HandLeft = np.array(joint_HandLeft, dtype=np.float_)
        self.joint_ShoulderRight = np.array(joint_ShoulderRight, dtype=np.float_)
        self.joint_ElbowRight = np.array(joint_ElbowRight, dtype=np.float_)
        self.joint_WristRight = np.array(joint_WristRight, dtype=np.float_)
        self.joint_HandRight = np.array(joint_HandRight, dtype=np.float_)
        self.joint_HipLeft = np.array(joint_HipLeft, dtype=np.float_)
        self.joint_KneeLeft = np.array(joint_KneeLeft, dtype=np.float_)
        self.joint_AnkleLeft = np.array(joint_AnkleLeft, dtype=np.float_)
        self.joint_FootLeft = np.array(joint_FootLeft, dtype=np.float_)
        self.joint_HipRight = np.array(joint_HipRight, dtype=np.float_)
        self.joint_KneeRight = np.array(joint_KneeRight, dtype=np.float_)
        self.joint_AnkleRight = np.array(joint_AnkleRight, dtype=np.float_)
        self.joint_FootRight = np.array(joint_FootRight, dtype=np.float_)
        self.joint_SpineShoulder = np.array(joint_SpineShoulder, dtype=np.float_)
        self.joint_HandTipLeft = np.array(joint_HandTipLeft, dtype=np.float_)
        self.joint_ThumbLeft = np.array(joint_ThumbLeft, dtype=np.float_)
        self.joint_HandTipRight = np.array(joint_HandTipRight, dtype=np.float_)
        self.joint_ThumbRight = np.array(joint_ThumbRight, dtype=np.float_)

    def getJoints(self):
        return [self.joint_SpineBase, self.joint_SpineMid, self.joint_Neck, 
                self.joint_Head, self.joint_ShoulderLeft, self.joint_ElbowLeft, 
                self.joint_WristLeft, self.joint_HandLeft, self.joint_ShoulderRight, 
                self.joint_ElbowRight, self.joint_WristRight, self.joint_HandRight, 
                self.joint_HipLeft, self.joint_KneeLeft, self.joint_AnkleLeft, 
                self.joint_FootLeft, self.joint_HipRight, self.joint_KneeRight, 
                self.joint_AnkleRight, self.joint_FootRight, self.joint_SpineShoulder,
                self.joint_HandTipLeft, self.joint_ThumbLeft, self.joint_HandTipRight,
                self.joint_ThumbRight]

    def updateJointsValue(self, joints):
        self.joint_SpineBase = joints[0]
        self.joint_SpineMid = joints[1]
        self.joint_Neck = joints[2]
        self.joint_Head = joints[3]
        self.joint_ShoulderLeft = joints[4]
        self.joint_ElbowLeft = joints[5]
        self.joint_WristLeft = joints[6]
        self.joint_HandLeft = joints[7]
        self.joint_ShoulderRight = joints[8]
        self.joint_ElbowRight = joints[9]
        self.joint_WristRight = joints[10]
        self.joint_HandRight = joints[11]
        self.joint_HipLeft = joints[12]
        self.joint_KneeLeft = joints[13]
        self.joint_AnkleLeft = joints[14]
        self.joint_FootLeft = joints[15]
        self.joint_HipRight = joints[16]
        self.joint_KneeRight = joints[17]
        self.joint_AnkleRight = joints[18]
        self.joint_FootRight = joints[19]
        self.joint_SpineShoulder = joints[20]
        self.joint_HandTipLeft = joints[21]
        self.joint_ThumbLeft = joints[22]
        self.joint_HandTipRight = joints[23]
        self.joint_ThumbRight = joints[24]
