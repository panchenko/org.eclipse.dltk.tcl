namespace eval n1 {
	Class C0
	C0 instproc foo {} {return "::n1::C0::foo"}
	C0 proc bar {} {return "::n1::C0::bar"}
	C0 create c0
}
