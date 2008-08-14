proc cmd {arg0 {arg1 def1} arg2} {puts alpha}
proc cmd {arg0 args arg2} {puts alpha}
proc cmd {arg0 {arg1 def10 def11}} {puts alpha}
proc cmd {arg0 {args def}} {puts alpha}
proc cmd {arg0 {{{arg1}}}} {puts alpha}