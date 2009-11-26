itcl::class A {
  method m1 {} {
  }
  proc m2 {} {
  }
  public proc m3 {} {
  } 
  protected proc m4 {} {
  }
  private proc m5 {} {
  }
}

body A::m1 {} {
    set x "m1"
}
body A::m2 {} {
  	set x "m2"
}
body A::m3 {} {
  	set x "m3"
}
body A::m4 {} {
  	set x "m4"
}
body A::m5 {} {
  	set x "m5"
}
