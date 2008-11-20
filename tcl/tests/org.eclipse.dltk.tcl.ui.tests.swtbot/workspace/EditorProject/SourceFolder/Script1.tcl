#
# WARNING: for correct work of tests declaration of procedures
# must be after call.
#

set globalVar 0

namespace eval ns1 {
namespace eval ns2 {

	set nsVar 0
	
	proc proc1 { param1 param2 } {
		upvar $param2 upVar
		set localVar 0

		puts $globalVar
		puts $localVar
		puts $param1
		puts $upVar

		ns1::proc2
		ns1::proc3
		globalProc
		proc4
	}

	proc proc4 { } {
	}

	proc1 1 $nsVar
}

proc proc3 { } {
}

}

proc ns1::proc2 { } {
}

proc globalProc { } {
}
