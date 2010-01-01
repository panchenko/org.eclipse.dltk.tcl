#
# Blank lines
#
package require Tk
namespace eval myNamespace {
	# Comment for myproc1
	proc myproc1 {} {
		puts "Hello World!"
	}	
	# Comment for myproc2
	proc myproc2 {} {
		puts "Hello "
		# Many empty lines below:


		
		
		puts "World!"
	}
}
proc myproc3 {} {
puts "Hello World!"
}
button .start -text "START" -command {start}
button .stop -text "STOP" -command {stop}
	