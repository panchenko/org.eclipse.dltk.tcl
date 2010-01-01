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
==== indentation-after-\-for-comments
#comment1 \
comment2 \
comment3
==
#comment1 \
comment2 \
comment3
==== indentation-after-\-for-string-test1
proc test {} {
puts { A \
B
}
}
==
proc test {} {
   puts { A \
B
}
}
==== indentation-after-\-for-string-test2
proc test {} {
puts " A \
B
"
}
==
proc test {} {
   puts " A \
B
"
}
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
==== indentation-comment-test2
# comment1
proc test {} {
# comment2
if {1 < 2} {
# comment3
} else {
# comment4
}
}
==
# comment1
proc test {} {
   # comment2
   if {1 < 2} {
      # comment3
   } else {
      # comment4
   }
}
==== indentation-string-test
proc test {} {
puts "
A
"
}
==
proc test {} {
   puts "
A
"
}
==== switch-indentation-test1
switch -- $arg {
A {
puts "A"
}
B
{
puts "B"
}
}
==
switch -- $arg {
   A {
      puts "A"
   }
   B
   {
      puts "B"
   }
}