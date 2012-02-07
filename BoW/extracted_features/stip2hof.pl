#! /usr/bin/perl -w
use strict;
use warnings;

open(INPUT,"<$ARGV[0]") or die("open: $!");
open (OUTPUT,">$ARGV[1]");
my($line,@word,@w);
while( defined( $line = <INPUT> ) ){
	if(substr($line,0,1)ne"#"){
		@word = split(/	/,$line);
		@w = @word[79..$#word];
		print( OUTPUT join(" ",@w));
	}
}
close(INPUT);
close(OUTPUT);
