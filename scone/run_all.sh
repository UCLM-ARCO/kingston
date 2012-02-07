#!/bin/bash

## VARIABLE DEFINITIONS
PATH_TO_SCONE_SERVER=/home/mjsantof/repo/mariajose.santofimia/tesis/CMU/Scone/server/scone-server-release-1.0
PATH_TO_PARSER=/home/mjsantof/repo/mariajose.santofimia/kingston/scone/parser

echo "=================================" 
echo "    STARTING the Scone Server"
echo "=================================" 


cd $PATH_TO_SCONE_SERVER
#rm scone-server.pid
#./start-server 5000 localhost


echo "=================================" 
echo " PARSING the recognized actions"
echo "=================================" 
cd $PATH_TO_PARSER

export CLASSPATH=$CLASSPATH:/home/mjsantof/repo/mariajose.santofimia/kingston/scone/apache-log4j-1.2.16/log4j-1.2.16.jar:/home/mjsantof/repo/mariajose.santofimia/kingston/scone

javac ActionParsing.java
cd ..
java parser.ActionParsing /home/mjsantof/repo/mariajose.santofimia/kingston/BoW/recognition_process/results


echo "=================================" 
echo "    STOPING the Scone Server"
echo "=================================" 

cd $PATH_TO_SCONE_SERVER
#./stop-server 