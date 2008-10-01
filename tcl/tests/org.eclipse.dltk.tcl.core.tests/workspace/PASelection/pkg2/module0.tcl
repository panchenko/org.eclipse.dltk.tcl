namespace eval pkgnamespace {
	proc myproc {a pkg2} {
		puts "myproc from pkg2"
	} 
	namespace export *
}
proc myglobalproc {pkg2 var2} {
	puts "pkg2"
}
package provide pkg2 1.0