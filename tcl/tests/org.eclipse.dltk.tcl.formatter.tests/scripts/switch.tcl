==== switch
		set a "A"
switch $a {
A {
puts "A"
}
B {
puts "B"
}
}
==
set a "A"
switch $a {
	A {
		puts "A"
	}
	B {
		puts "B"
	}
}
==== switch-hash-arg
		set a "#2"
switch $a {
A {
puts "A"
}
#1 {
puts "#1"
}
#2 {
puts "#2"
}
}
==
set a "#2"
switch $a {
	A {
		puts "A"
	}
	#1 {
		puts "#1"
	}
	#2 {
		puts "#2"
	}
}
