package require Itcl

itcl::class a {
  constructor {} {
    puts "A-constuctor"
  }
}

proc fooProc {a b c} {
  ::a aInstance
  puts "a=$a" 
}

yes 1 2 3
