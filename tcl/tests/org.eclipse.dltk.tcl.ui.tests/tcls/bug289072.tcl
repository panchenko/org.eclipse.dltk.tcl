#!/usr/bin/tclsh

namespace eval fakenamespace {

    proc fakeproc {} {
        set y {}
        puts $y
    }

    proc otherfake {} {
        set x []
    }
}

y
