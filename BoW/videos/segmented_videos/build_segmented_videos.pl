#! /usr/bin/perl -w
use strict;
use warnings;

my (@truth,$start_frame, $end_frame, @frame_line, $duration, $aux, $video_name);

if($ARGV[0] ne  "clean"){
    open(INPUT,"<../truth/$ARGV[0]_frame_segmented.txt") or die("open: $!");
    @truth=<INPUT>;
    close(INPUT);
    system("rm -r $ARGV[0]");
    system("mkdir $ARGV[0]");
    #first we convert the mpeg video into a sequence of png images
    system("mkdir images");
    system("mkdir tmp");
    system("ffmpeg -i ../$ARGV[0].avi -r 25 images/$ARGV[0]_%d.jpeg");


    foreach my $i (0..$#truth){
	chomp($truth[$i]);
	@frame_line = split(/ /, $truth[$i]);
	$start_frame = $frame_line[0];
	$end_frame = $frame_line[1];
	for(my $j= $start_frame; $j < $end_frame; $j++){
	    $aux = $j - $start_frame;
	    system("mv images/$ARGV[0]_$j.jpeg  tmp/$ARGV[0]_$aux.jpeg");
	}

	$video_name = sprintf("$ARGV[0]/$ARGV[0]_%03d\_$frame_line[2].avi", $i);
	system("ffmpeg -i tmp/$ARGV[0]_%d.jpeg -s 390x291 $video_name");

	#printf("ffmpeg -i tmp/$ARGV[0]%%d.png/ -o  $ARGV[0]/$ARGV[0]_$i\_$frame_line[2].mpeg\n");
	system("rm tmp/*");


    }
    system("rm -r tmp");
    system("rm -r images");
    
}
else{
    system("rm -r actor*");
    system("rm -r images");
    system("rm -r tmp");
}

    

