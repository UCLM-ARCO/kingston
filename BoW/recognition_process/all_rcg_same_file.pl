#! /usr/bin/perl -w
use strict;
use warnings;

my (@files,$content,$fold);


@files = <../clustering/histograms/*_$ARGV[1].$ARGV[0]hist>;

if(scalar(@files)>0){
	open(OUTPUT,">learn_hist_norm_$ARGV[0]_$ARGV[1]t");
	printf("file name: learn_hist_norm_$ARGV[0]_$ARGV[1]t\n");
	foreach my $i (0..$#files){
		open(INPUT,"<$files[$i]") or die("open: $!");
		$content = <INPUT>;
		close(INPUT);
		print(OUTPUT $content);
		if($i<$#files){
			print(OUTPUT "\n");
		}
	}
	close(OUTPUT);
	system("perl norm_hist_smallut2libsvm.pl learn_hist_norm_$ARGV[0]_$ARGV[1]t");
	#system("rm -r learn_hist_norm_$ARGV[0]_$ARGV[1]t");
	system("perl norm_hist_biglut2libsvm.pl ../clustering/learn_hist_norm_$ARGV[0]_$ARGV[1].hist");
}
