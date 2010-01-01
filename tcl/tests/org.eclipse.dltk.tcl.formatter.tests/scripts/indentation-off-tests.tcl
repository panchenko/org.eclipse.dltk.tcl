==== simple-indentation-test
proc test {} {
puts "hello"
}
==
proc test {} {
puts "hello"
}
==== indentation-script-with-{}
proc test {} {
set a 1; set b2
if {$a < $b} {
puts "hello"
}
}
==
proc test {} {
set a 1; set b2
if {$a < $b} {
puts "hello"
}
}
==== indentation-after-\
test -arg1 \
-arg2 -arg3 \
-arg4
==
test -arg1 \
-arg2 -arg3 \
-arg4
==== indentation-after-\-for-substitution
set b [button $tb.tb[incr i] \
-text $icon -image \
-relief flat -overrelief raised]
==
set b [button $tb.tb[incr i] \
-text $icon -image \
-relief flat -overrelief raised]
==== indentation-comment-test
#comment1
proc test {} {
#comment2
puts "hello"; #comment3
#comment4 }
==
#comment1
proc test {} {
#comment2
puts "hello"; #comment3
#comment4 }