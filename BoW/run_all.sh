#! /usr/bin/perl -w
#!/bin/bash

echo "=================================" 
echo "Doing some configuration work"
echo "=================================" 

cd recognition_process
    rm -r ann nearest_neighbour histograms results learn_hist_norm_ho*
    mkdir ann
    mkdir nearest_neighbour
    mkdir histograms
    mkdir results
cd ..
cp  sources/stip-1.1-winlinux/bin/stipdet extracted_features/stipdet
chmod +x extracted_features/stipdet

echo "=================================" 
echo "Starting the video segmentation"
echo "=================================" 



cd videos/truth
echo Moving into `echo pwd`

for f in *_frame_segmented.txt; do
    echo Segmenting `echo $f | sed 's/_frame_segmented.txt//'`
    cd ../segmented_videos
## This line has to be commented whenever you want to reuse the extracted features
    perl build_segmented_videos.pl `echo $f | sed 's/_frame_segmented.txt//'`
done

cd ..


echo "================================" 
echo "Starting the feature extraction"
echo "================================" 


##Lets create a symbolic link to the feature extractor


echo `pwd`


for f in truth/*_frame_segmented.txt; do
    actor=`echo $f | sed 's/_frame_segmented.txt//'` 
    actor=`echo $actor | sed 's/truth\///'`
    echo Segmenting $actor
    cd ../extracted_features

## You can comment the next line whenever you want to reuse the extracted features    
   perl avi2stip.pl $actor

    echo "=========================================" 
    echo "Turning the stip feature files into .hof"
    echo "=========================================" 
    
    echo  stip2featuresdescriptors.pl $actor
    perl stip2featuresdescriptors.pl $actor
   
    echo "=========================================" 
    echo "Calculing look up table for the testing"
     echo "sequence: smallut.lut"
    echo "=========================================" 
   
    cd ../clustering
    pwd
    rm smallut.lut
    rm -r nearest_neighbour histograms
    mkdir nearest_neighbour
    mkdir histograms

    perl new_sequence_cluster_choice_k.pl hof 300 $actor
   
    echo "=========================================" 
    echo "Creating the learned histogram file for "
    echo "the testing sequence "
    echo "=========================================" 
    cd ../recognition_process


    perl all_rcg_same_file.pl hof 300 

    echo "=========================================" 
    echo "Compute the model given the trained data"
    echo "=========================================" 

    perl recognition_from_cluster_all.pl hof 0 400 300 $actor
done

