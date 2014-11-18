#!/usr/bin/python
# -*- coding: utf-8; mode: python -*-

import numpy as np


class Frame(object):
    def __init__(self, timestamp=None, index=None, joint_head=None, joint_neck=None,
                 joint_left_shoulder=None, joint_right_shoulder=None, joint_left_elbow=None,
                 joint_right_elbow=None, joint_left_hand=None, joint_right_hand=None,
                 joint_torso=None, joint_left_hip=None, joint_right_hip=None,
                 joint_left_knee=None, joint_right_knee=None, joint_left_foot=None,
                 joint_right_foot=None, center_of_mass=None):

        self.timestamp = timestamp
        self.index = index
        self.joint_head = np.array(joint_head, dtype=np.float_)
        self.joint_neck = np.array(joint_neck, dtype=np.float_)
        self.joint_left_shoulder = np.array(joint_left_shoulder, dtype=np.float_)
        self.joint_right_shoulder = np.array(joint_right_shoulder, dtype=np.float_)
        self.joint_left_elbow = np.array(joint_left_elbow, dtype=np.float_)
        self.joint_right_elbow = np.array(joint_right_elbow, dtype=np.float_)
        self.joint_left_hand = np.array(joint_left_hand, dtype=np.float_)
        self.joint_right_hand = np.array(joint_right_hand, dtype=np.float_)
        self.joint_torso = np.array(joint_torso, dtype=np.float_)
        self.joint_left_hip = np.array(joint_left_hip, dtype=np.float_)
        self.joint_right_hip = np.array(joint_right_hip, dtype=np.float_)
        self.joint_left_knee = np.array(joint_left_knee, dtype=np.float_)
        self.joint_right_knee = np.array(joint_right_knee, dtype=np.float_)
        self.joint_left_foot = np.array(joint_left_foot, dtype=np.float_)
        self.joint_right_foot = np.array(joint_right_foot, dtype=np.float_)
        self.center_of_mass = np.array(center_of_mass, dtype=np.float_)


    def getJoints(self):
        return [self.joint_head, self.joint_neck, self.joint_left_shoulder,
                self.joint_right_shoulder, self.joint_left_elbow, self.joint_right_elbow,
                self.joint_left_hand, self.joint_right_hand, self.joint_torso,
                self.joint_left_hip, self.joint_right_hip, self.joint_left_knee,
                self.joint_right_knee, self.joint_left_foot, self.joint_right_foot]
