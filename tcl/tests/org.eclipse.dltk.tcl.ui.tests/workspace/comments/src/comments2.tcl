#classA_before
class A
#p1_before
A instproc p1 {} {
#p1_inside
}


#classB_before
class B
B instproc p2 {} {
#p2_inside
}


#p3_before

	#whitespace-separated comment
	
A instproc p3 {} {

}



#p4_before
#another line of comment
proc p4 {} {

}



#eof_comment
