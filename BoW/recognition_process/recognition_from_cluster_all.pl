#! /usr/bin/perl -w
use strict;
use warnings;

# SAV: removed copying of executables now in the path (we cannot assume this file system allows executables)

my (@file,$line,@splt,$i);
my @months = qw(Jan Feb Mar Apr May Jun Jul Aug Sep Oct Nov Dec);
my @weekDays = qw(Sun Mon Tue Wed Thu Fri Sat Sun);
my ($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings, $year, $theTime);

@file = <learn_hist_norm_$ARGV[0]_*_libsvm>;

$i=0;
while($i<scalar(@file)){
	@splt=split(/_/,$file[$i]);
	if( !($splt[4] =~ m/t/) ){
		($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
		$year = 1900 + $yearOffset;
		$theTime = "$hour:$minute:$second, $weekDays[$dayOfWeek] $months[$month] $dayOfMonth, $year";
		print("start of recognition process for descriptor $ARGV[0], kernel $ARGV[1], cost $ARGV[2] and k=$splt[4] at $theTime\n");

		system("/usr/local/sbin/bsvm-train -t $ARGV[1] -c $ARGV[2]  $file[$i] results/$file[$i]_model_$ARGV[1]_$ARGV[2]");
		#system("/usr/local/sbin/bsvm-predict learn_hist_norm_$ARGV[0]_$splt[4]t_libsvm results/$file[$i]_model_$ARGV[1]_$ARGV[2] results/classified_result_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4] >results/accuracy_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4]");
		#system("/tmp/bsvm-2.06_bk/bsvm-predict learn_hist_norm_$ARGV[0]_$splt[4]t_libsvm results/$file[$i]_model_$ARGV[1]_$ARGV[2] results/classified_result_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4] >results/accuracy_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4]");		
		print("../../bsvm-2.06/bsvm-predict learn_hist_norm_$ARGV[0]_$splt[4]t_libsvm results/$file[$i]_model_$ARGV[1]_$ARGV[2] results/classified_result_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4] > results/accuracy_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4]");
		system("../../bsvm-2.06/bsvm-predict learn_hist_norm_$ARGV[0]_$splt[4]t_libsvm results/$file[$i]_model_$ARGV[1]_$ARGV[2] results/classified_result_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4] > results/accuracy_$ARGV[1]_$ARGV[2]_$ARGV[0]_$splt[4]_$ARGV[4]");

	}
	$i++;
}
($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
$year = 1900 + $yearOffset;
$theTime = "$hour:$minute:$second, $weekDays[$dayOfWeek] $months[$month] $dayOfMonth, $year";
print("end of recognition process at $theTime\n");
