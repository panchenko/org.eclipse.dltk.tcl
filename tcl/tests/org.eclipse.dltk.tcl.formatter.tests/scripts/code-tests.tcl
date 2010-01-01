==== simple-code
proc test {} {
puts "Hello World!"
}
==
proc test {} {
	puts "Hello World!"
}
==== script-one-line
proc test {} {
set s 1
if [ $s == 1 ] { puts "true" } else { puts "false" }
}
==
proc test {} {
	set s 1
	if [ $s == 1 ] { puts "true" } else { puts "false" }
}
==== commands-one-line
proc test {} {
set a 1; set b 2; set c 3
}
==
proc test {} {
	set a 1; set b 2; set c 3
}
==== script-without-begin-end
proc test {} {
after 20 cancel
}
==
proc test {} {
	after 20 cancel
}
==== comments-test1
#comment
proc test {} {
#comment
set a 1
}
==
#comment
proc test {} {
	#comment
	set a 1
}
==== comments-test2
#comment
proc test {} {
                           #comment
set s 1
                        if [ $s == 1 ] {
#comment
puts "true"
#comment
 } else { #comment 
 puts "false" }
#comment
}
#comment
==
#comment
proc test {} {
	#comment
	set s 1
	if [ $s == 1 ] {
		#comment
		puts "true"
		#comment
	} else { #comment
		puts "false" }
	#comment
}
#comment