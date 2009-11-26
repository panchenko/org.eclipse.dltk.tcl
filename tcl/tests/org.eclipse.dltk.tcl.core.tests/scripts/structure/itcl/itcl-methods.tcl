itcl::class A {
  method m1 {} {
    set x "m1"
  }
  proc m2 {} {
  	set x "m2"
  }
  public proc m3 {} {
  	set x "m3"
  } 
  protected proc m4 {} {
  	set x "m4"
  }
  private proc m5 {} {
  	set x "m5"
  }
}
