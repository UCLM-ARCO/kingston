#! /usr/bin/perl -w
use strict;
use warnings;

my (@files,@splt,$folder,$folder2, $folder_features);

$folder="$ARGV[0]_stip";
$folder_features="$ARGV[0]";

@files = <$folder/*.stip>;



$folder_features.="_feature_descriptors";
system("rm -r $folder_features");
system("mkdir $folder_features");


foreach my $i (0..$#files){ #get the last index of the array
    
	@splt=split(/.stip/,$files[$i]);
	@splt=split(/\//,$splt[-1]);
	
	
	printf("perl stip2hof.pl $files[$i] $folder_features/$ARGV[0].hof\n");
	system("perl stip2hof.pl $files[$i] $folder_features/$splt[-1]_.hof");
	#system("cp $folder/$splt[-1].hof $folder2/$splt[-1].hofrcg");
}
