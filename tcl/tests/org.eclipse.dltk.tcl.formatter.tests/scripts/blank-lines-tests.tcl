==== empty-lines-to-preserve-test
#comment

proc test {} {
set a 1



puts "hello"
}
==
#comment

proc test {} {
	set a 1

	puts "hello"
}
==== empty-line-between-proc-test
proc test {} {
puts "hello"
}
proc test2 {} {
puts " world"
}



proc test3 {} {
puts "!"
}
==
proc test {} {
	puts "hello"
}


proc test2 {} {
	puts " world"
}


proc test3 {} {
	puts "!"
}
==== empty-line-between-proc-test
proc test {} {
puts "hello"
}
proc test2 {} {
puts " world"
}
test a
==
proc test {} {
	puts "hello"
}


proc test2 {} {
	puts " world"
}


test a
==== empty-line-between-proc-with-comments-test
#comment1
proc test {} {
#comment2
puts "hello"
}
#comment3
proc test2 {} {
puts " world"
}


#comment4
proc test3 {} {
puts "!"
}
==
#comment1
proc test {} {
	#comment2
	puts "hello"
}


#comment3
proc test2 {} {
	puts " world"
}


#comment4
proc test3 {} {
	puts "!"
}
==== empty-line-between-proc-with-comments-test2
# comment1
namespace eval myNamespace {
# comment2
proc myproc { a { b 20 } } {
# comment3
puts "hello"
}
# comment4
proc myproc { a { b 20 } } {
puts "hello"
}
}
==
# comment1
namespace eval myNamespace {


	# comment2
	proc myproc { a { b 20 } } {
		# comment3
		puts "hello"
	}


	# comment4
	proc myproc { a { b 20 } } {
		puts "hello"
	}


}
==== empty-line-between-proc-with-comments-test3
# comment1

# comment2
proc myproc { a { b 20 } } {
# comment3
puts "hello"
}
# comment4

# comment5
proc myproc { a { b 20 } } {
puts "hello"
}
==
# comment1


# comment2
proc myproc { a { b 20 } } {
	# comment3
	puts "hello"
}


# comment4


# comment5
proc myproc { a { b 20 } } {
	puts "hello"
}
==== empty-line-after-package-require-simple-test
#pacage require test
package require Tk
proc test {} {
puts "hello"
}
==
#pacage require test
package require Tk


proc test {} {
	puts "hello"
}
==== empty-line-after-package-require-test
#pacage require test
package require Tk
package require Tk
package require Tk
package require Tk
set a 1
package require Tk
package require Tk
#comment
proc test2 {} {
puts "hello2"
}
==
#pacage require test
package require Tk
package require Tk
package require Tk
package require Tk

set a 1
package require Tk
package require Tk


#comment
proc test2 {} {
	puts "hello2"
}