#! /usr/bin/perl -w
use strict;
use warnings;

my($line,@word,@splt,@lut,$i);
open(INPUT,"<$ARGV[0]") or die("open: $!");
@splt=split(/\//,$ARGV[0]);
@splt=split(/\./,$splt[-1]);

open(LUT,"<../clustering/smallut.lut") or die("open: $!");
$line = <LUT>;
close(LUT);
chomp($line);
@lut=split(/ /,$line);
open(OUTPUT,">$splt[0]_libsvm");
$i=1;
while( defined( $line = <INPUT> ) ){
	chomp($line);
	@word=split(/ /,$line);
	print(OUTPUT ++$lut[$i]);
	foreach my $j (1..scalar(@word)){
		print(OUTPUT " $j:$word[$j-1]");
	}
	print(OUTPUT "\n");
	$i+=2;
}
close(INPUT);
close(OUTPUT);
