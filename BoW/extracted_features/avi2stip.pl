#! /usr/bin/perl -w
use strict;
use warnings;

my (@files,@splt,$folder, $folder_features);

$folder="../videos/segmented_videos/$ARGV[0]";
$folder_features="$ARGV[0]";
@files = <$folder/*.avi>;

$folder_features.="_stip";
system("rm -r $folder_features");
system("mkdir $folder_features");
foreach my $i (0..$#files){
	@splt=split(/.avi/,$files[$i]);
	@splt=split(/\//,$splt[-1]);
	printf("stipdet -f $files[$i] -nplev 3 -plev0 0 -kparam 0.00000050 -thresh 0 -border 0 -dscr hoghof -szf 5 -o $folder_features/$splt[-1].stip -vis no\n");
	system("./stipdet -f $files[$i] -nplev 3 -plev0 0 -kparam 0.0001 -thresh 1.00e-11 -dscr hoghof  -szf 10.0  -o $folder_features/$splt[-1].stip");
}
