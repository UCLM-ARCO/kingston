#!/bin/bash

## VARIABLE DEFINITIONS
PATH_TO_SCONE_SERVER=/home/mjsantof/repo/mariajose.santofimia/tesis/CMU/Scone/server/scone-server-release-1.0
PATH_TO_PARSER=/home/mjsantof/repo/mariajose.santofimia/kingston/scone/parser

echo "=================================" 
echo "    STARTING the Scone Server"
echo "=================================" 


cd $PATH_TO_SCONE_SERVER
if [ -f scone-server.pid ]
    then
#    rm scone-server.pid
    echo "deleted scone-server.pid"
fi
#bash start-server 5000 localhost
#sleep 12

echo "=================================" 
echo " PARSING the recognized actions"
echo "=================================" 
cd $PATH_TO_PARSER

export CLASSPATH=$CLASSPATH:/home/mjsantof/repo/mariajose.santofimia/kingston/scone/apache-log4j-1.2.16/log4j-1.2.16.jar:/home/mjsantof/repo/mariajose.santofimia/kingston/scone

javac SconeFacade.java
javac ClassifiedResultFile.java
javac CSAR.java
javac Action.java
javac Believe.java
javac Estimation.java
javac Expectation.java


cd ..
#rm classificationResults/*
java parser.CSAR /home/mjsantof/repo/mariajose.santofimia/kingston/BoW/recognition_process/results


echo "=================================" 
echo "    STOPING the Scone Server"
echo "=================================" 

cd $PATH_TO_SCONE_SERVER
#bash stop-server 
