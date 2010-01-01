#
# Indentation
#
namespace eval myNamespace {
# Script indentation
proc myproc { a { b 20 } } {
upvar 1 $a array
s = [string length $a]
puts $array
set t 2.4
set s "Alfa"
# Indentation after backslash
return [expr \
{ s + b }]
}
}

proc myproc2 {} {
puts "Hello World"
}
button .stop -text "STOP" \
-command {stop}
