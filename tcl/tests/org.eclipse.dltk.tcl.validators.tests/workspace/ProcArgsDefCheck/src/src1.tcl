proc c0 {a0 {a1 d1}} {puts $a0}
proc cmd "arg0 {{arg1}} {{arg2} {def2}} args" {puts alpha}
proc cmd0 arg {puts alpha}
proc cmd1 {arg} {puts alpha}
proc cmd10 "arg" {puts alpha}
proc cmd11 $arg {puts alpha}
proc cmd2 {arg1 arg2} {puts alpha}
proc cmd3 {arg1 {arg2 def2}} {puts alpha}
proc cmd4 {arg1 {arg2 def2} args} {puts alpha}
proc cmd5 {{arg1} {{arg2}}} {puts alpha}
proc cmd6 {} {puts alpha}
proc cmd7 {arg0 {{arg1}}} {puts alpha}
proc cmd7 [arg0] {puts alpha}
proc cmd7 arg0$s {{arg1}}} {puts alpha}
proc cmd7 arg0[lala] {puts alpha}