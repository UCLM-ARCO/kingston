#! /usr/bin/perl -w
use strict;
use warnings;

# SAV: removed copying of executables now in the path (we cannot assume this file system allows executables)

my (@descrpt,$j,$action,@content,$line,@splt,@splt2,$dim,@lut,$temp,$nb);
my @months = qw(Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec);
my @weekDays = qw(Sun Mon Tue Wed Thu Fri Sat Sun);
my ($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings, $year, $theTime);

print("start of recognition process for a new sequence with descriptor $ARGV[0] and k=$ARGV[1]\n");
($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
$year = 1900 + $yearOffset;
$theTime = "$hour:$minute:$second, $weekDays[$dayOfWeek] $months[$month] $dayOfMonth, $year";
print("$theTime\n");

# nearest neighbour
print("the nearest neighbour algorithm will be applied with the descriptors extracted from the new sequence \nand the clusters learned previously as input, and the association between a descriptor and a cluster\nwill be saved in the file ...nearest_cluster_$ARGV[1].$ARGV[0]pts\n");

@descrpt = <../extracted_features/$ARGV[2]_feature_descriptors/*.$ARGV[0]>;
open(INPUT,"<$descrpt[0]") or die("open: $!");
@content = <INPUT>;
close(INPUT);
$line = $content[0];
chomp($line);
@splt=split(/ /,$line);
$dim=scalar(@splt);
print("the dimension of the descriptor $ARGV[0] is $dim\n");
$nb=0;
open(OUTPUT,">smallut.lut");
foreach my $i (0..$#descrpt){
	open(INPUT,"<$descrpt[$i]") or die("open: $!");
	@content = <INPUT>;
	close(INPUT);
	$j=scalar(@content);
	$nb+=$j;

	$lut[$i]=$j;


	@splt=split(/.$ARGV[0]rcg/,$descrpt[$i]);
	@splt=split(/\//,$splt[-1]);
	@splt2=split(/_/,$splt[-1]);
	$action=$splt2[2]-1;
	print(OUTPUT "1 $action ");
#	system("cp /usr/local/sbin/ann_sample ann/ann_sample$ARGV[0]$ARGV[1]_$i");	
###	system("./ann/ann_sample$ARGV[0]$ARGV[1]_$i -d $dim -max $j -df clusters_$ARGV[0]_$ARGV[1].pts -qf $descrpt[$i] >nearest_neighbour/$splt[-1]nearest_cluster_$ARGV[1].$ARGV[0]pts");
	system("/usr/local/sbin/ann_sample -d $dim -max $j -df clusters_$ARGV[0]_$ARGV[1].pts -qf $descrpt[$i] >nearest_neighbour/$splt[-1]nearest_cluster_$ARGV[1].$ARGV[0]pts");
	print("nearest neighbour processed for $descrpt[$i] with $j descriptors\n");
}
close(OUTPUT);
print(join("","nearest neighbour processed ",scalar(@descrpt)," times for a total number of recognition descriptors of: $nb\n"));
($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
$year = 1900 + $yearOffset;
$theTime = "$hour:$minute:$second, $weekDays[$dayOfWeek] $months[$month] $dayOfMonth, $year";
print("$theTime\n");

#building histogram of clusters for each sequence
@descrpt = <nearest_neighbour/*_$ARGV[1].$ARGV[0]pts>;
my @hist=();
foreach my $i (0..$#descrpt){
	open(INPUT,"<$descrpt[$i]") or die("open: $!");
	@hist=(@hist,[(0)x$ARGV[1]]);
	while(defined($line=<INPUT>)){
		chomp($line);
		$hist[$i][$line]++;
	}
	close(INPUT);
}

#building normalized histogram of clusters for each sequence
@descrpt = <nearest_neighbour/*_$ARGV[1].$ARGV[0]pts>;
foreach my $i (0..$#descrpt){
	@splt=split(/.$ARGV[0]pts/,$descrpt[$i]);
	@splt=split(/\//,$splt[-1]);
	@splt=split(/nearest_cluster/,$splt[-1]);

	foreach $j (1..$ARGV[1]){
	    $hist[$i][$j-1]/=($lut[$i]+1); #mjsantof has add +1
	}
	open(OUTPUT,">histograms/$splt[0]_histogram_norm_$ARGV[1].$ARGV[0]hist");
	$temp=$hist[$i];
	print(OUTPUT join(" ",@$temp));
	close(OUTPUT);
}
print("normalized histogram built for every sequence\n");
($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
$year = 1900 + $yearOffset;
$theTime = "$hour:$minute:$second, $weekDays[$dayOfWeek] $months[$month] $dayOfMonth, $year";
print("$theTime\n");
