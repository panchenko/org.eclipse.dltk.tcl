==== between code
puts 1


puts 2
==
puts 1

puts 2
==== inside strings
set a {1


2}
==
set a {1


2}
==== mixed
set a {1


2}


puts "=${a}="
==
set a {1


2}

puts "=${a}="
