namespace eval pkgnamespace {
	proc myproc {a pkg1} {
		puts "myproc from pkg1"
	} 
	namespace export *
}
proc myglobalproc {pkg1 var2} {
	puts "pkg1"
}
package provide pkg1 1.0