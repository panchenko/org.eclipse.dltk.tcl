# Commands covered: expr
#
# This file contains the original set of tests for Tcl's expr command.
# Since the expr command is now compiled, a new set of tests covering
# the new implementation are in the files "parseExpr.test" and
# "compExpr.test". Sourcing this file into Tcl runs the tests and generates
# output for errors. No output means no errors were found.
#
# Copyright (c) 1991-1994 The Regents of the University of California.
# Copyright (c) 1994-1997 Sun Microsystems, Inc.
# Copyright (c) 1998-2000 by Scriptics Corporation.
#
# See the file "license.terms" for information on usage and redistribution
# of this file, and for a DISCLAIMER OF ALL WARRANTIES.
#
# RCS: @(#) $Id: expr-old.tcl,v 1.1 2007/02/22 08:40:26 asobolev Exp $

if {[lsearch [namespace children] ::tcltest] == -1} {
    package require tcltest 2.1
    namespace import -force ::tcltest::*	
}

if {([catch {expr T1()} msg] == 1) && ($msg == {unknown math function "T1"})} {
    set gotT1 0
    puts "This application hasn't been compiled with the \"T1\" and"
    puts "\"T2\" math functions, so I'll skip some of the expr tests."
} else {
    set gotT1 1
}

# First, test all of the integer operators individually.

test expr-old-1.1 {integer operators} {expr -4} -4
test expr-old-1.2 {integer operators} {expr -(1+4)} -5
test expr-old-1.3 {integer operators} {expr ~3} -4
test expr-old-1.4 {integer operators} {expr !2} 0
test expr-old-1.5 {integer operators} {expr !0} 1
test expr-old-1.6 {integer operators} {expr 4*6} 24
test expr-old-1.7 {integer operators} {expr 36/12} 3
test expr-old-1.8 {integer operators} {expr 27/4} 6
test expr-old-1.9 {integer operators} {expr 27%4} 3
test expr-old-1.10 {integer operators} {expr 2+2} 4
test expr-old-1.11 {integer operators} {expr 2-6} -4
test expr-old-1.12 {integer operators} {expr 1<<3} 8
test expr-old-1.13 {integer operators} {expr 0xff>>2} 63
test expr-old-1.14 {integer operators} {expr -1>>2} -1
test expr-old-1.15 {integer operators} {expr 3>2} 1
test expr-old-1.16 {integer operators} {expr 2>2} 0
test expr-old-1.17 {integer operators} {expr 1>2} 0
test expr-old-1.18 {integer operators} {expr 3<2} 0
test expr-old-1.19 {integer operators} {expr 2<2} 0
test expr-old-1.20 {integer operators} {expr 1<2} 1
test expr-old-1.21 {integer operators} {expr 3>=2} 1
test expr-old-1.22 {integer operators} {expr 2>=2} 1
test expr-old-1.23 {integer operators} {expr 1>=2} 0
test expr-old-1.24 {integer operators} {expr 3<=2} 0
test expr-old-1.25 {integer operators} {expr 2<=2} 1
test expr-old-1.26 {integer operators} {expr 1<=2} 1
test expr-old-1.27 {integer operators} {expr 3==2} 0
test expr-old-1.28 {integer operators} {expr 2==2} 1
test expr-old-1.29 {integer operators} {expr 3!=2} 1
test expr-old-1.30 {integer operators} {expr 2!=2} 0
test expr-old-1.31 {integer operators} {expr 7&0x13} 3
test expr-old-1.32 {integer operators} {expr 7^0x13} 20
test expr-old-1.33 {integer operators} {expr 7|0x13} 23
test expr-old-1.34 {integer operators} {expr 0&&1} 0
test expr-old-1.35 {integer operators} {expr 0&&0} 0
test expr-old-1.36 {integer operators} {expr 1&&3} 1
test expr-old-1.37 {integer operators} {expr 0||1} 1
test expr-old-1.38 {integer operators} {expr 3||0} 1
test expr-old-1.39 {integer operators} {expr 0||0} 0
test expr-old-1.40 {integer operators} {expr 3>2?44:66} 44
test expr-old-1.41 {integer operators} {expr 2>3?44:66} 66
test expr-old-1.42 {integer operators} {expr 36/5} 7
test expr-old-1.43 {integer operators} {expr 36%5} 1
test expr-old-1.44 {integer operators} {expr -36/5} -8
test expr-old-1.45 {integer operators} {expr -36%5} 4
test expr-old-1.46 {integer operators} {expr 36/-5} -8
test expr-old-1.47 {integer operators} {expr 36%-5} -4
test expr-old-1.48 {integer operators} {expr -36/-5} 7
test expr-old-1.49 {integer operators} {expr -36%-5} -1
test expr-old-1.50 {integer operators} {expr +36} 36
test expr-old-1.51 {integer operators} {expr +--++36} 36
test expr-old-1.52 {integer operators} {expr +36%+5} 1
test expr-old-1.53 {integer operators} {
    catch {unset x}
    set x yes
    list [expr {1 && $x}] [expr {$x && 1}] \
         [expr {0 || $x}] [expr {$x || 0}]
} {1 1 1 1}

# Check the floating-point operators individually, along with
# automatic conversion to integers where needed.

test expr-old-2.1 {floating-point operators} {expr -4.2} -4.2
test expr-old-2.2 {floating-point operators} {expr -(1.1+4.2)} -5.3
test expr-old-2.3 {floating-point operators} {expr +5.7} 5.7
test expr-old-2.4 {floating-point operators} {expr +--+-62.0} -62.0
test expr-old-2.5 {floating-point operators} {expr !2.1} 0
test expr-old-2.6 {floating-point operators} {expr !0.0} 1
test expr-old-2.7 {floating-point operators} {expr 4.2*6.3} 26.46
test expr-old-2.8 {floating-point operators} {expr 36.0/12.0} 3.0
test expr-old-2.9 {floating-point operators} {expr 27/4.0} 6.75
test expr-old-2.10 {floating-point operators} {expr 2.3+2.1} 4.4
test expr-old-2.11 {floating-point operators} {expr 2.3-6.5} -4.2
test expr-old-2.12 {floating-point operators} {expr 3.1>2.1} 1
test expr-old-2.13 {floating-point operators} {expr {2.1 > 2.1}} 0
test expr-old-2.14 {floating-point operators} {expr 1.23>2.34e+1} 0
test expr-old-2.15 {floating-point operators} {expr 3.45<2.34} 0
test expr-old-2.16 {floating-point operators} {expr 0.002e3<--200e-2} 0
test expr-old-2.17 {floating-point operators} {expr 1.1<2.1} 1
test expr-old-2.18 {floating-point operators} {expr 3.1>=2.2} 1
test expr-old-2.19 {floating-point operators} {expr 2.345>=2.345} 1
test expr-old-2.20 {floating-point operators} {expr 1.1>=2.2} 0
test expr-old-2.21 {floating-point operators} {expr 3.0<=2.0} 0
test expr-old-2.22 {floating-point operators} {expr 2.2<=2.2} 1
test expr-old-2.23 {floating-point operators} {expr 2.2<=2.2001} 1
test expr-old-2.24 {floating-point operators} {expr 3.2==2.2} 0
test expr-old-2.25 {floating-point operators} {expr 2.2==2.2} 1
test expr-old-2.26 {floating-point operators} {expr 3.2!=2.2} 1
test expr-old-2.27 {floating-point operators} {expr 2.2!=2.2} 0
test expr-old-2.28 {floating-point operators} {expr 0.0&&0.0} 0
test expr-old-2.29 {floating-point operators} {expr 0.0&&1.3} 0
test expr-old-2.30 {floating-point operators} {expr 1.3&&0.0} 0
test expr-old-2.31 {floating-point operators} {expr 1.3&&3.3} 1
test expr-old-2.32 {floating-point operators} {expr 0.0||0.0} 0
test expr-old-2.33 {floating-point operators} {expr 0.0||1.3} 1
test expr-old-2.34 {floating-point operators} {expr 1.3||0.0} 1
test expr-old-2.35 {floating-point operators} {expr 3.3||0.0} 1
test expr-old-2.36 {floating-point operators} {expr 3.3>2.3?44.3:66.3} 44.3
test expr-old-2.37 {floating-point operators} {expr 2.3>3.3?44.3:66.3} 66.3
test expr-old-2.38 {floating-point operators} {
    list [catch {expr 028.1 + 09.2} msg] $msg
} {0 37.3}

# Operators that aren't legal on floating-point numbers

test expr-old-3.1 {illegal floating-point operations} {
    list [catch {expr ~4.0} msg] $msg
} {1 {can't use floating-point value as operand of "~"}}
test expr-old-3.2 {illegal floating-point operations} {
    list [catch {expr 27%4.0} msg] $msg
} {1 {can't use floating-point value as operand of "%"}}
test expr-old-3.3 {illegal floating-point operations} {
    list [catch {expr 27.0%4} msg] $msg
} {1 {can't use floating-point value as operand of "%"}}
test expr-old-3.4 {illegal floating-point operations} {
    list [catch {expr 1.0<<3} msg] $msg
} {1 {can't use floating-point value as operand of "<<"}}
test expr-old-3.5 {illegal floating-point operations} {
    list [catch {expr 3<<1.0} msg] $msg
} {1 {can't use floating-point value as operand of "<<"}}
test expr-old-3.6 {illegal floating-point operations} {
    list [catch {expr 24.0>>3} msg] $msg
} {1 {can't use floating-point value as operand of ">>"}}
test expr-old-3.7 {illegal floating-point operations} {
    list [catch {expr 24>>3.0} msg] $msg
} {1 {can't use floating-point value as operand of ">>"}}
test expr-old-3.8 {illegal floating-point operations} {
    list [catch {expr 24&3.0} msg] $msg
} {1 {can't use floating-point value as operand of "&"}}
test expr-old-3.9 {illegal floating-point operations} {
    list [catch {expr 24.0|3} msg] $msg
} {1 {can't use floating-point value as operand of "|"}}
test expr-old-3.10 {illegal floating-point operations} {
    list [catch {expr 24.0^3} msg] $msg
} {1 {can't use floating-point value as operand of "^"}}

# Check the string operators individually.

test expr-old-4.1 {string operators} {expr {"abc" > "def"}} 0
test expr-old-4.2 {string operators} {expr {"def" > "def"}} 0
test expr-old-4.3 {string operators} {expr {"g" > "def"}} 1
test expr-old-4.4 {string operators} {expr {"abc" < "abd"}} 1
test expr-old-4.5 {string operators} {expr {"abd" < "abd"}} 0
test expr-old-4.6 {string operators} {expr {"abe" < "abd"}} 0
test expr-old-4.7 {string operators} {expr {"abc" >= "def"}} 0
test expr-old-4.8 {string operators} {expr {"def" >= "def"}} 1
test expr-old-4.9 {string operators} {expr {"g" >= "def"}} 1
test expr-old-4.10 {string operators} {expr {"abc" <= "abd"}} 1
test expr-old-4.11 {string operators} {expr {"abd" <= "abd"}} 1
test expr-old-4.12 {string operators} {expr {"abe" <= "abd"}} 0
test expr-old-4.13 {string operators} {expr {"abc" == "abd"}} 0
test expr-old-4.14 {string operators} {expr {"abd" == "abd"}} 1
test expr-old-4.15 {string operators} {expr {"abc" != "abd"}} 1
test expr-old-4.16 {string operators} {expr {"abd" != "abd"}} 0
test expr-old-4.17 {string operators} {expr {"0y" < "0x12"}} 0
test expr-old-4.18 {string operators} {expr {"." < " "}} 0
test expr-old-4.19 {string operators} {expr {"abc" eq "abd"}} 0
test expr-old-4.20 {string operators} {expr {"abd" eq "abd"}} 1
test expr-old-4.21 {string operators} {expr {"abc" ne "abd"}} 1
test expr-old-4.22 {string operators} {expr {"abd" ne "abd"}} 0
test expr-old-4.23 {string operators} {expr {"" eq "abd"}} 0
test expr-old-4.24 {string operators} {expr {"" eq ""}} 1
test expr-old-4.25 {string operators} {expr {"abd" ne ""}} 1
test expr-old-4.26 {string operators} {expr {"" ne ""}} 0
test expr-old-4.27 {string operators} {expr {"longerstring" eq "shorter"}} 0
test expr-old-4.28 {string operators} {expr {"longerstring" ne "shorter"}} 1

# The following tests are non-portable because on some systems "+"
# and "-" can be parsed as numbers.

test expr-old-4.29 {string operators} {nonPortable} {expr {"0" == "+"}} 0
test expr-old-4.30 {string operators} {nonPortable} {expr {"0" == "-"}} 0
test expr-old-4.31 {string operators} {expr {1?"foo":"bar"}} foo
test expr-old-4.32 {string operators} {expr {0?"foo":"bar"}} bar

# Operators that aren't legal on string operands.

test expr-old-5.1 {illegal string operations} {
    list [catch {expr {-"a"}} msg] $msg
} {1 {can't use non-numeric string as operand of "-"}}
test expr-old-5.2 {illegal string operations} {
    list [catch {expr {+"a"}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-5.3 {illegal string operations} {
    list [catch {expr {~"a"}} msg] $msg
} {1 {can't use non-numeric string as operand of "~"}}
test expr-old-5.4 {illegal string operations} {
    list [catch {expr {!"a"}} msg] $msg
} {1 {can't use non-numeric string as operand of "!"}}
test expr-old-5.5 {illegal string operations} {
    list [catch {expr {"a"*"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "*"}}
test expr-old-5.6 {illegal string operations} {
    list [catch {expr {"a"/"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "/"}}
test expr-old-5.7 {illegal string operations} {
    list [catch {expr {"a"%"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "%"}}
test expr-old-5.8 {illegal string operations} {
    list [catch {expr {"a"+"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-5.9 {illegal string operations} {
    list [catch {expr {"a"-"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "-"}}
test expr-old-5.10 {illegal string operations} {
    list [catch {expr {"a"<<"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "<<"}}
test expr-old-5.11 {illegal string operations} {
    list [catch {expr {"a">>"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of ">>"}}
test expr-old-5.12 {illegal string operations} {
    list [catch {expr {"a"&"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "&"}}
test expr-old-5.13 {illegal string operations} {
    list [catch {expr {"a"^"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "^"}}
test expr-old-5.14 {illegal string operations} {
    list [catch {expr {"a"|"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "|"}}
test expr-old-5.15 {illegal string operations} {
    list [catch {expr {"a"&&"b"}} msg] $msg
} {1 {expected boolean value but got "a"}}
test expr-old-5.16 {illegal string operations} {
    list [catch {expr {"a"||"b"}} msg] $msg
} {1 {expected boolean value but got "a"}}
test expr-old-5.17 {illegal string operations} {
    list [catch {expr {"a"?4:2}} msg] $msg
} {1 {expected boolean value but got "a"}}

# Check precedence pairwise.

test expr-old-6.1 {precedence checks} {expr -~3} 4
test expr-old-6.2 {precedence checks} {expr -!3} 0
test expr-old-6.3 {precedence checks} {expr -~0} 1

test expr-old-7.1 {precedence checks} {expr 2*4/6} 1
test expr-old-7.2 {precedence checks} {expr 24/6*3} 12
test expr-old-7.3 {precedence checks} {expr 24/6/2} 2

test expr-old-8.1 {precedence checks} {expr -2+4} 2
test expr-old-8.2 {precedence checks} {expr -2-4} -6
test expr-old-8.3 {precedence checks} {expr +2-4} -2

test expr-old-9.1 {precedence checks} {expr 2*3+4} 10
test expr-old-9.2 {precedence checks} {expr 8/2+4} 8
test expr-old-9.3 {precedence checks} {expr 8%3+4} 6
test expr-old-9.4 {precedence checks} {expr 2*3-1} 5
test expr-old-9.5 {precedence checks} {expr 8/2-1} 3
test expr-old-9.6 {precedence checks} {expr 8%3-1} 1

test expr-old-10.1 {precedence checks} {expr 6-3-2} 1

test expr-old-11.1 {precedence checks} {expr 7+1>>2} 2
test expr-old-11.2 {precedence checks} {expr 7+1<<2} 32
test expr-old-11.3 {precedence checks} {expr 7>>3-2} 3
test expr-old-11.4 {precedence checks} {expr 7<<3-2} 14

test expr-old-12.1 {precedence checks} {expr 6>>1>4} 0
test expr-old-12.2 {precedence checks} {expr 6>>1<2} 0
test expr-old-12.3 {precedence checks} {expr 6>>1>=3} 1
test expr-old-12.4 {precedence checks} {expr 6>>1<=2} 0
test expr-old-12.5 {precedence checks} {expr 6<<1>5} 1
test expr-old-12.6 {precedence checks} {expr 6<<1<5} 0
test expr-old-12.7 {precedence checks} {expr 5<=6<<1} 1
test expr-old-12.8 {precedence checks} {expr 5>=6<<1} 0

test expr-old-13.1 {precedence checks} {expr 2<3<4} 1
test expr-old-13.2 {precedence checks} {expr 0<4>2} 0
test expr-old-13.3 {precedence checks} {expr 4>2<1} 0
test expr-old-13.4 {precedence checks} {expr 4>3>2} 0
test expr-old-13.5 {precedence checks} {expr 4>3>=2} 0
test expr-old-13.6 {precedence checks} {expr 4>=3>2} 0
test expr-old-13.7 {precedence checks} {expr 4>=3>=2} 0
test expr-old-13.8 {precedence checks} {expr 0<=4>=2} 0
test expr-old-13.9 {precedence checks} {expr 4>=2<=0} 0
test expr-old-13.10 {precedence checks} {expr 2<=3<=4} 1

test expr-old-14.1 {precedence checks} {expr 1==4>3} 1
test expr-old-14.2 {precedence checks} {expr 0!=4>3} 1
test expr-old-14.3 {precedence checks} {expr 1==3<4} 1
test expr-old-14.4 {precedence checks} {expr 0!=3<4} 1
test expr-old-14.5 {precedence checks} {expr 1==4>=3} 1
test expr-old-14.6 {precedence checks} {expr 0!=4>=3} 1
test expr-old-14.7 {precedence checks} {expr 1==3<=4} 1
test expr-old-14.8 {precedence checks} {expr 0!=3<=4} 1
test expr-old-14.9 {precedence checks} {expr 1eq4>3} 1
test expr-old-14.10 {precedence checks} {expr 0ne4>3} 1
test expr-old-14.11 {precedence checks} {expr 1eq3<4} 1
test expr-old-14.12 {precedence checks} {expr 0ne3<4} 1
test expr-old-14.13 {precedence checks} {expr 1eq4>=3} 1
test expr-old-14.14 {precedence checks} {expr 0ne4>=3} 1
test expr-old-14.15 {precedence checks} {expr 1eq3<=4} 1
test expr-old-14.16 {precedence checks} {expr 0ne3<=4} 1

test expr-old-15.1 {precedence checks} {expr 1==3==3} 0
test expr-old-15.2 {precedence checks} {expr 3==3!=2} 1
test expr-old-15.3 {precedence checks} {expr 2!=3==3} 0
test expr-old-15.4 {precedence checks} {expr 2!=1!=1} 0
test expr-old-15.5 {precedence checks} {expr 1eq3eq3} 0
test expr-old-15.6 {precedence checks} {expr 3eq3ne2} 1
test expr-old-15.7 {precedence checks} {expr 2ne3eq3} 0
test expr-old-15.8 {precedence checks} {expr 2ne1ne1} 0

test expr-old-16.1 {precedence checks} {expr 2&3eq2} 0
test expr-old-16.2 {precedence checks} {expr 1&3ne3} 0
test expr-old-16.3 {precedence checks} {expr 2&3eq2} 0
test expr-old-16.4 {precedence checks} {expr 1&3ne3} 0

test expr-old-17.1 {precedence checks} {expr 7&3^0x10} 19
test expr-old-17.2 {precedence checks} {expr 7^0x10&3} 7

test expr-old-18.1 {precedence checks} {expr 7^0x10|3} 23
test expr-old-18.2 {precedence checks} {expr 7|0x10^3} 23

test expr-old-19.1 {precedence checks} {expr 7|3&&1} 1
test expr-old-19.2 {precedence checks} {expr 1&&3|7} 1
test expr-old-19.3 {precedence checks} {expr 0&&1||1} 1
test expr-old-19.4 {precedence checks} {expr 1||1&&0} 1

test expr-old-20.1 {precedence checks} {expr 1||0?3:4} 3
test expr-old-20.2 {precedence checks} {expr 1?0:4||1} 0
test expr-old-20.3 {precedence checks} {expr 1?2:0?3:4} 2
test expr-old-20.4 {precedence checks} {expr 0?2:0?3:4} 4
test expr-old-20.5 {precedence checks} {expr 1?2?3:4:0} 3
test expr-old-20.6 {precedence checks} {expr 0?2?3:4:0} 0

# Parentheses.

test expr-old-21.1 {parenthesization} {expr (2+4)*6} 36
test expr-old-21.2 {parenthesization} {expr (1?0:4)||1} 1
test expr-old-21.3 {parenthesization} {expr +(3-4)} -1

# Embedded commands and variable names.

set a 16 
test expr-old-22.1 {embedded variables} {expr {2*$a}} 32 
test expr-old-22.2 {embedded variables} {
    set x -5
    set y 10
    expr {$x + $y}
} {5} 
test expr-old-22.3 {embedded variables} {
    set x "  -5"
    set y "  +10"
    expr {$x + $y}
} {5}
test expr-old-22.4 {embedded commands and variables} {expr {[set a] - 14}} 2
test expr-old-22.5 {embedded commands and variables} {
    list [catch {expr {12 - [bad_command_name]}} msg] $msg
} {1 {invalid command name "bad_command_name"}}

# Double-quotes and things inside them.

test expr-old-23.1 {double quotes} {expr {"abc"}} abc
test expr-old-23.2 {double quotes} {
    set a 189
    expr {"$a.bc"}
} 189.bc
test expr-old-23.3 {double quotes} {
    set b2 xyx
    expr {"$b2$b2$b2.[set b2].[set b2]"}
} xyxxyxxyx.xyx.xyx
test expr-old-23.4 {double quotes} {expr {"11\}\}22"}} 11}}22
test expr-old-23.5 {double quotes} {expr {"\*bc"}} {*bc}
test expr-old-23.6 {double quotes} {
    catch {unset bogus__}
    list [catch {expr {"$bogus__"}} msg] $msg
} {1 {can't read "bogus__": no such variable}}
test expr-old-23.7 {double quotes} {
    list [catch {expr {"a[error Testing]bc"}} msg] $msg
} {1 Testing}
test expr-old-23.8 {double quotes} {
    list [catch {expr {"12398712938788234-1298379" != ""}} msg] $msg
} {0 1}

# Numbers in various bases.

test expr-old-24.1 {numbers in different bases} {expr 0x20} 32
test expr-old-24.2 {numbers in different bases} {expr 015} 13

# Conversions between various data types.

test expr-old-25.1 {type conversions} {expr 2+2.5} 4.5
test expr-old-25.2 {type conversions} {expr 2.5+2} 4.5
test expr-old-25.3 {type conversions} {expr 2-2.5} -0.5
test expr-old-25.4 {type conversions} {expr 2/2.5} 0.8
test expr-old-25.5 {type conversions} {expr 2>2.5} 0
test expr-old-25.6 {type conversions} {expr 2.5>2} 1
test expr-old-25.7 {type conversions} {expr 2<2.5} 1
test expr-old-25.8 {type conversions} {expr 2>=2.5} 0
test expr-old-25.9 {type conversions} {expr 2<=2.5} 1
test expr-old-25.10 {type conversions} {expr 2==2.5} 0
test expr-old-25.11 {type conversions} {expr 2!=2.5} 1
test expr-old-25.12 {type conversions} {expr 2>"ab"} 0
test expr-old-25.13 {type conversions} {expr {2>" "}} 1
test expr-old-25.14 {type conversions} {expr {"24.1a" > 24.1}} 1
test expr-old-25.15 {type conversions} {expr {24.1 > "24.1a"}} 0
test expr-old-25.16 {type conversions} {expr 2+2.5} 4.5
test expr-old-25.17 {type conversions} {expr 2+2.5} 4.5
test expr-old-25.18 {type conversions} {expr 2.0e2} 200.0
test expr-old-25.19 {type conversions} {eformat} {expr 2.0e15} 2e+15
test expr-old-25.20 {type conversions} {expr 10.0} 10.0

# Various error conditions.

test expr-old-26.1 {error conditions} {
    list [catch {expr 2+"a"} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-26.2 {error conditions} {
    list [catch {expr 2+4*} msg] $msg
} {1 {syntax error in expression "2+4*": premature end of expression}}
test expr-old-26.3 {error conditions} {
    list [catch {expr 2+4*(} msg] $msg
} {1 {syntax error in expression "2+4*(": premature end of expression}}
catch {unset _non_existent_}
test expr-old-26.4 {error conditions} {
    list [catch {expr 2+$_non_existent_} msg] $msg
} {1 {can't read "_non_existent_": no such variable}}
set a xx
test expr-old-26.5 {error conditions} {
    list [catch {expr {2+$a}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-26.6 {error conditions} {
    list [catch {expr {2+[set a]}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-26.7 {error conditions} {
    list [catch {expr {2+(4}} msg] $msg
} {1 {syntax error in expression "2+(4": looking for close parenthesis}}
test expr-old-26.8 {error conditions} {
    list [catch {expr 2/0} msg] $msg $errorCode
} {1 {divide by zero} {ARITH DIVZERO {divide by zero}}}
test expr-old-26.9 {error conditions} {
    list [catch {expr 2%0} msg] $msg $errorCode
} {1 {divide by zero} {ARITH DIVZERO {divide by zero}}}
test expr-old-26.10 {error conditions} {
    list [catch {expr 2.0/0.0} msg] $msg $errorCode
} {1 {divide by zero} {ARITH DIVZERO {divide by zero}}}
test expr-old-26.11 {error conditions} {
    list [catch {expr 2#} msg] $msg
} {1 {syntax error in expression "2#": extra tokens at end of expression}}
test expr-old-26.12 {error conditions} {
    list [catch {expr a.b} msg] $msg
} {1 {syntax error in expression "a.b": variable references require preceding $}}
test expr-old-26.13 {error conditions} {
    list [catch {expr {"a"/"b"}} msg] $msg
} {1 {can't use non-numeric string as operand of "/"}}
test expr-old-26.14 {error conditions} {
    list [catch {expr 2:3} msg] $msg
} {1 {syntax error in expression "2:3": extra tokens at end of expression}}
test expr-old-26.15 {error conditions} {
    list [catch {expr a@b} msg] $msg
} {1 {syntax error in expression "a@b": variable references require preceding $}}
test expr-old-26.16 {error conditions} {
    list [catch {expr a[b} msg] $msg
} {1 {missing close-bracket}}
test expr-old-26.17 {error conditions} {
    list [catch {expr a`b} msg] $msg
} {1 {syntax error in expression "a`b": variable references require preceding $}}
test expr-old-26.18 {error conditions} {
    list [catch {expr \"a\"\{b} msg] $msg
} {1 syntax\ error\ in\ expression\ \"\"a\"\{b\":\ extra\ tokens\ at\ end\ of\ expression}
test expr-old-26.19 {error conditions} {
    list [catch {expr a} msg] $msg
} {1 {syntax error in expression "a": variable references require preceding $}}
test expr-old-26.20 {error conditions} {
    list [catch expr msg] $msg
} {1 {wrong # args: should be "expr arg ?arg ...?"}}

# Cancelled evaluation.

test expr-old-27.1 {cancelled evaluation} {
    set a 1
    expr {0&&[set a 2]}
    set a
} 1
test expr-old-27.2 {cancelled evaluation} {
    set a 1
    expr {1||[set a 2]}
    set a
} 1
test expr-old-27.3 {cancelled evaluation} {
    set a 1
    expr {0?[set a 2]:1}
    set a
} 1
test expr-old-27.4 {cancelled evaluation} {
    set a 1
    expr {1?2:[set a 2]}
    set a
} 1
catch {unset x}
test expr-old-27.5 {cancelled evaluation} {
    list [catch {expr {[info exists x] && $x}} msg] $msg
} {0 0}
test expr-old-27.6 {cancelled evaluation} {
    list [catch {expr {0 && [concat $x]}} msg] $msg
} {0 0}
test expr-old-27.7 {cancelled evaluation} {
    set one 1
    list [catch {expr {1 || 1/$one}} msg] $msg
} {0 1}
test expr-old-27.8 {cancelled evaluation} {
    list [catch {expr {1 || -"string"}} msg] $msg
} {0 1}
test expr-old-27.9 {cancelled evaluation} {
    list [catch {expr {1 || ("string" * ("x" && "y"))}} msg] $msg
} {0 1}
test expr-old-27.10 {cancelled evaluation} {
    set x -1.0
    list [catch {expr {($x > 0) ? round(log($x)) : 0}} msg] $msg
} {0 0}
test expr-old-27.11 {cancelled evaluation} {
    list [catch {expr {0 && foo}} msg] $msg
} {1 {syntax error in expression "0 && foo": variable references require preceding $}}
test expr-old-27.12 {cancelled evaluation} {
    list [catch {expr {0 ? 1 : foo}} msg] $msg
} {1 {syntax error in expression "0 ? 1 : foo": variable references require preceding $}}

# Tcl_ExprBool as used in "if" statements

test expr-old-28.1 {Tcl_ExprBoolean usage} {
    set a 1
    if {2} {set a 2}
    set a
} 2
test expr-old-28.2 {Tcl_ExprBoolean usage} {
    set a 1
    if {0} {set a 2}
    set a
} 1
test expr-old-28.3 {Tcl_ExprBoolean usage} {
    set a 1
    if {1.2} {set a 2}
    set a
} 2
test expr-old-28.4 {Tcl_ExprBoolean usage} {
    set a 1
    if {-1.1} {set a 2}
    set a
} 2
test expr-old-28.5 {Tcl_ExprBoolean usage} {
    set a 1
    if {0.0} {set a 2}
    set a
} 1
test expr-old-28.6 {Tcl_ExprBoolean usage} {
    set a 1
    if {"YES"} {set a 2}
    set a
} 2
test expr-old-28.7 {Tcl_ExprBoolean usage} {
    set a 1
    if {"no"} {set a 2}
    set a
} 1
test expr-old-28.8 {Tcl_ExprBoolean usage} {
    set a 1
    if {"true"} {set a 2}
    set a
} 2
test expr-old-28.9 {Tcl_ExprBoolean usage} {
    set a 1
    if {"fAlse"} {set a 2}
    set a
} 1
test expr-old-28.10 {Tcl_ExprBoolean usage} {
    set a 1
    if {"on"} {set a 2}
    set a
} 2
test expr-old-28.11 {Tcl_ExprBoolean usage} {
    set a 1
    if {"Off"} {set a 2}
    set a
} 1
test expr-old-28.12 {Tcl_ExprBool usage} {
    list [catch {if {"abc"} {}} msg] $msg
} {1 {expected boolean value but got "abc"}}
test expr-old-28.13 {Tcl_ExprBool usage} {
    list [catch {if {"ogle"} {}} msg] $msg
} {1 {expected boolean value but got "ogle"}}
test expr-old-28.14 {Tcl_ExprBool usage} {
    list [catch {if {"o"} {}} msg] $msg
} {1 {expected boolean value but got "o"}}

# Operands enclosed in braces

test expr-old-29.1 {braces} {expr {{abc}}} abc
test expr-old-29.2 {braces} {expr {{00010}}} 8
test expr-old-29.3 {braces} {expr {{3.1200000}}} 3.12
test expr-old-29.4 {braces} {expr {{a{b}{1 {2 3}}c}}} "a{b}{1 {2 3}}c"
test expr-old-29.5 {braces} {
    list [catch {expr "\{abc"} msg] $msg
} {1 {missing close-brace}}

# Very long values

test expr-old-30.1 {long values} {
    set a "0000 1111 2222 3333 4444"
    set a "$a | $a | $a | $a | $a"
    set a "$a || $a || $a || $a || $a"
    expr {$a}
} {0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 || 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 || 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 || 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 || 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444 | 0000 1111 2222 3333 4444}
test expr-old-30.2 {long values} {
    set a "000000000000000000000000000000"
    set a "$a$a$a$a$a$a$a$a$a$a$a$a$a$a$a$a${a}5"
    expr $a
} 5

# Expressions spanning multiple arguments

test expr-old-31.1 {multiple arguments to expr command} {
    expr 4 + ( 6 *12) -3
} 73
test expr-old-31.2 {multiple arguments to expr command} {
    list [catch {expr 2 + (3 + 4} msg] $msg
} {1 {syntax error in expression "2 + (3 + 4": looking for close parenthesis}}
test expr-old-31.3 {multiple arguments to expr command} {
    list [catch {expr 2 + 3 +} msg] $msg
} {1 {syntax error in expression "2 + 3 +": premature end of expression}}
test expr-old-31.4 {multiple arguments to expr command} {
    list [catch {expr 2 + 3 )} msg] $msg
} {1 {syntax error in expression "2 + 3 )": extra tokens at end of expression}}

# Math functions

test expr-old-32.1 {math functions in expressions} {
    format %.6g [expr acos(0.5)]
} {1.0472}
test expr-old-32.2 {math functions in expressions} {
    format %.6g [expr asin(0.5)]
} {0.523599}
test expr-old-32.3 {math functions in expressions} {
    format %.6g [expr atan(1.0)]
} {0.785398}
test expr-old-32.4 {math functions in expressions} {
    format %.6g [expr atan2(2.0, 2.0)]
} {0.785398}
test expr-old-32.5 {math functions in expressions} {
    format %.6g [expr ceil(1.999)]
} {2}
test expr-old-32.6 {math functions in expressions} {
    format %.6g [expr cos(.1)]
} {0.995004}
test expr-old-32.7 {math functions in expressions} {
    format %.6g [expr cosh(.1)]
} {1.005}
test expr-old-32.8 {math functions in expressions} {
    format %.6g [expr exp(1.0)]
} {2.71828}
test expr-old-32.9 {math functions in expressions} {
    format %.6g [expr floor(2.000)]
} {2}
test expr-old-32.10 {math functions in expressions} {
    format %.6g [expr floor(2.001)]
} {2}
test expr-old-32.11 {math functions in expressions} {
    format %.6g [expr fmod(7.3, 3.2)]
} {0.9}
test expr-old-32.12 {math functions in expressions} {
    format %.6g [expr hypot(3.0, 4.0)]
} {5}
test expr-old-32.13 {math functions in expressions} {
    format %.6g [expr log(2.8)]
} {1.02962}
test expr-old-32.14 {math functions in expressions} {
    format %.6g [expr log10(2.8)]
} {0.447158}
test expr-old-32.15 {math functions in expressions} {
    format %.6g [expr pow(2.1, 3.1)]
} {9.97424}
test expr-old-32.16 {math functions in expressions} {
    format %.6g [expr sin(.1)]
} {0.0998334}
test expr-old-32.17 {math functions in expressions} {
    format %.6g [expr sinh(.1)]
} {0.100167}
test expr-old-32.18 {math functions in expressions} {
    format %.6g [expr sqrt(2.0)]
} {1.41421}
test expr-old-32.19 {math functions in expressions} {
    format %.6g [expr tan(0.8)]
} {1.02964}
test expr-old-32.20 {math functions in expressions} {
    format %.6g [expr tanh(0.8)]
} {0.664037}
test expr-old-32.21 {math functions in expressions} {
    format %.6g [expr abs(-1.8)]
} {1.8}
test expr-old-32.22 {math functions in expressions} {
    expr abs(10.0)
} {10.0}
test expr-old-32.23 {math functions in expressions} {
    format %.6g [expr abs(-4)]
} {4}
test expr-old-32.24 {math functions in expressions} {
    format %.6g [expr abs(66)]
} {66}

# The following test is different for 32-bit versus 64-bit architectures.

if {0x80000000 > 0} {
    test expr-old-32.25 {math functions in expressions} {nonPortable} {
	list [catch {expr abs(0x8000000000000000)} msg] $msg
    } {1 {integer value too large to represent}}
} else {
    test expr-old-32.25 {math functions in expressions} {nonPortable} {
	list [catch {expr abs(0x80000000)} msg] $msg
    } {1 {integer value too large to represent}}
}

test expr-old-32.26 {math functions in expressions} {
    expr double(1)
} {1.0}
test expr-old-32.27 {math functions in expressions} {
    expr double(1.1)
} {1.1}
test expr-old-32.28 {math functions in expressions} {
    expr int(1)
} {1}
test expr-old-32.29 {math functions in expressions} {
    expr int(1.4)
} {1}
test expr-old-32.30 {math functions in expressions} {
    expr int(1.6)
} {1}
test expr-old-32.31 {math functions in expressions} {
    expr int(-1.4)
} {-1}
test expr-old-32.32 {math functions in expressions} {
    expr int(-1.6)
} {-1}
test expr-old-32.33 {math functions in expressions} {
    list [catch {expr int(1e60)} msg] $msg
} {1 {integer value too large to represent}}
test expr-old-32.34 {math functions in expressions} {
    list [catch {expr int(-1e60)} msg] $msg
} {1 {integer value too large to represent}}
test expr-old-32.35 {math functions in expressions} {
    expr round(1.49)
} {1}
test expr-old-32.36 {math functions in expressions} {
    expr round(1.51)
} {2}
test expr-old-32.37 {math functions in expressions} {
    expr round(-1.49)
} {-1}
test expr-old-32.38 {math functions in expressions} {
    expr round(-1.51)
} {-2}
test expr-old-32.39 {math functions in expressions} {
    list [catch {expr round(1e60)} msg] $msg
} {1 {integer value too large to represent}}
test expr-old-32.40 {math functions in expressions} {
    list [catch {expr round(-1e60)} msg] $msg
} {1 {integer value too large to represent}}
test expr-old-32.41 {math functions in expressions} {
    list [catch {expr pow(1.0 + 3.0 - 2, .8 * 5)} msg] $msg
} {0 16.0}
test expr-old-32.42 {math functions in expressions} {
    list [catch {expr hypot(5*.8,3)} msg] $msg
} {0 5.0}
if $gotT1 {
    test expr-old-32.43 {math functions in expressions} {
	expr 2*T1()
    } 246
    test expr-old-32.44 {math functions in expressions} {
	expr T2()*3
    } 1035
}
test expr-old-32.45 {math functions in expressions} {
    expr (0 <= rand()) && (rand() < 1)
} {1}
test expr-old-32.46 {math functions in expressions} {
    list [catch {expr rand(24)} msg] $msg
} {1 {too many arguments for math function}}
test expr-old-32.47 {math functions in expressions} {
    list [catch {expr srand()} msg] $msg
} {1 {too few arguments for math function}}
test expr-old-32.48 {math functions in expressions} {
    list [catch {expr srand(3.79)} msg] $msg
} {1 {can't use floating-point value as argument to srand}}
test expr-old-32.49 {math functions in expressions} {
    list [catch {expr srand("")} msg] $msg
} {1 {argument to math function didn't have numeric value}}
test expr-old-32.50 {math functions in expressions} {
    set result [expr round(srand(12345) * 1000)]
    for {set i 0} {$i < 10} {incr i} {
	lappend result [expr round(rand() * 1000)]
    }
    set result
} {97 834 948 36 12 51 766 585 914 784 333}
test expr-old-32.51 {math functions in expressions} {
    list [catch {expr {srand([lindex "6ty" 0])}} msg] $msg
} {1 {argument to math function didn't have numeric value}}
test expr-old-32.52 {math functions in expressions} {
    expr {srand(1<<37) < 1}
} {1}
test expr-old-32.53 {math functions in expressions} {
    expr {srand((1<<31) - 1) > 0}
} {1}

test expr-old-33.1 {conversions and fancy args to math functions} {
    expr hypot ( 3 , 4 )
} 5.0
test expr-old-33.2 {conversions and fancy args to math functions} {
    expr hypot ( (2.0+1.0) , 4 )
} 5.0
test expr-old-33.3 {conversions and fancy args to math functions} {
    expr hypot ( 3 , (3.0 + 1.0) )
} 5.0
test expr-old-33.4 {conversions and fancy args to math functions} {
    format %.6g [expr cos(acos(0.1))]
} 0.1

test expr-old-34.1 {errors in math functions} {
    list [catch {expr func_2(1.0)} msg] $msg
} {1 {unknown math function "func_2"}}
test expr-old-34.2 {errors in math functions} {
    list [catch {expr func|(1.0)} msg] $msg
} {1 {syntax error in expression "func|(1.0)": variable references require preceding $}}
test expr-old-34.3 {errors in math functions} {
    list [catch {expr {hypot("a b", 2.0)}} msg] $msg
} {1 {argument to math function didn't have numeric value}}
test expr-old-34.4 {errors in math functions} {
    list [catch {expr hypot(1.0 2.0)} msg] $msg
} {1 {syntax error in expression "hypot(1.0 2.0)": missing close parenthesis at end of function call}}
test expr-old-34.5 {errors in math functions} {
    list [catch {expr hypot(1.0, 2.0} msg] $msg
} {1 {syntax error in expression "hypot(1.0, 2.0": missing close parenthesis at end of function call}}
test expr-old-34.6 {errors in math functions} {
    list [catch {expr hypot(1.0 ,} msg] $msg
} {1 {syntax error in expression "hypot(1.0 ,": premature end of expression}}
test expr-old-34.7 {errors in math functions} {
    list [catch {expr hypot(1.0)} msg] $msg
} {1 {too few arguments for math function}}
test expr-old-34.8 {errors in math functions} {
    list [catch {expr hypot(1.0, 2.0, 3.0)} msg] $msg
} {1 {too many arguments for math function}}
test expr-old-34.9 {errors in math functions} {
    list [catch {expr acos(-2.0)} msg] $msg $errorCode
} {1 {domain error: argument not in valid range} {ARITH DOMAIN {domain error: argument not in valid range}}}
test expr-old-34.10 {errors in math functions} {nonPortable} {
    list [catch {expr pow(-3, 1000001)} msg] $msg $errorCode
} {1 {floating-point value too large to represent} {ARITH OVERFLOW {floating-point value too large to represent}}}
test expr-old-34.11 {errors in math functions} {
    list [catch {expr pow(3, 1000001)} msg] $msg $errorCode
} {1 {floating-point value too large to represent} {ARITH OVERFLOW {floating-point value too large to represent}}}
test expr-old-34.12 {errors in math functions} {
    list [catch {expr -14.0*exp(100000)} msg] $msg $errorCode
} {1 {floating-point value too large to represent} {ARITH OVERFLOW {floating-point value too large to represent}}}
test expr-old-34.13 {errors in math functions} {
    list [catch {expr int(1.0e30)} msg] $msg $errorCode
} {1 {integer value too large to represent} {ARITH IOVERFLOW {integer value too large to represent}}}
test expr-old-34.14 {errors in math functions} {
    list [catch {expr int(-1.0e30)} msg] $msg $errorCode
} {1 {integer value too large to represent} {ARITH IOVERFLOW {integer value too large to represent}}}
test expr-old-34.15 {errors in math functions} {
    list [catch {expr round(1.0e30)} msg] $msg $errorCode
} {1 {integer value too large to represent} {ARITH IOVERFLOW {integer value too large to represent}}}
test expr-old-34.16 {errors in math functions} {
    list [catch {expr round(-1.0e30)} msg] $msg $errorCode
} {1 {integer value too large to represent} {ARITH IOVERFLOW {integer value too large to represent}}}
if $gotT1 {
    test expr-old-34.17 {errors in math functions} {
	list [catch {expr T1(4)} msg] $msg
    } {1 {too many arguments for math function}}
}

test expr-old-36.1 {ExprLooksLikeInt procedure} -body {
    expr 0289
} -returnCodes error -match glob -result {*invalid octal number*}
test expr-old-36.2 {ExprLooksLikeInt procedure} {
    set x 0289
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use invalid octal number as operand of "+"}}
test expr-old-36.3 {ExprLooksLikeInt procedure} {
    list [catch {expr 0289.1} msg] $msg
} {0 289.1}
test expr-old-36.4 {ExprLooksLikeInt procedure} {
    set x 0289.1
    list [catch {expr {$x+1}} msg] $msg
} {0 290.1}
test expr-old-36.5 {ExprLooksLikeInt procedure} {
    set x {  +22}
    list [catch {expr {$x+1}} msg] $msg
} {0 23}
test expr-old-36.6 {ExprLooksLikeInt procedure} {
    set x {	-22}
    list [catch {expr {$x+1}} msg] $msg
} {0 -21}
test expr-old-36.7 {ExprLooksLikeInt procedure} {nonPortable unixOnly} {
    list [catch {expr nan} msg] $msg
} {1 {domain error: argument not in valid range}}
test expr-old-36.8 {ExprLooksLikeInt procedure} {
    list [catch {expr 78e1} msg] $msg
} {0 780.0}
test expr-old-36.9 {ExprLooksLikeInt procedure} {
    list [catch {expr 24E1} msg] $msg
} {0 240.0}
test expr-old-36.10 {ExprLooksLikeInt procedure} {nonPortable unixOnly} {
    list [catch {expr 78e} msg] $msg
} {1 {syntax error in expression "78e"}}

# test for [Bug #542588]
test expr-old-36.11 {ExprLooksLikeInt procedure} {
    # define a "too large integer"; this one works also for 64bit arith
    set x 665802003400000000000000
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use integer value too large to represent as operand of "+"}}

# tests for [Bug #587140]
test expr-old-36.12 {ExprLooksLikeInt procedure} {
    set x "10;"
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-36.13 {ExprLooksLikeInt procedure} {
    set x " +"
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use non-numeric string as operand of "+"}}
test expr-old-36.14 {ExprLooksLikeInt procedure} {
    set x "123456789012345678901234567890 "
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use integer value too large to represent as operand of "+"}}
test expr-old-36.15 {ExprLooksLikeInt procedure} {
    set x "099 "
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use invalid octal number as operand of "+"}}
test expr-old-36.16 {ExprLooksLikeInt procedure} {
    set x " 0xffffffffffffffffffffffffffffffffffffff  "
    list [catch {expr {$x+1}} msg] $msg
} {1 {can't use integer value too large to represent as operand of "+"}}

if {[info commands testexprlong] == {}} {
    puts "This application hasn't been compiled with the \"testexprlong\""
    puts "command, so I can't test Tcl_ExprLong etc."
} else {
test expr-old-37.1 {Check that Tcl_ExprLong doesn't modify interpreter result if no error} {
    testexprlong
} {This is a result: 5}
}

if {[info commands testexprstring] == {}} {
    puts "This application hasn't been compiled with the \"testexprstring\""
    puts "command, so I can't test Tcl_ExprString etc."
} else {
test expr-old-38.1 {Verify Tcl_ExprString's basic operation} {
    list [testexprstring "1+4"] [testexprstring "2*3+4.2"] \
        [catch {testexprstring "1+"} msg] $msg
} {5 10.2 1 {syntax error in expression "1+": premature end of expression}}
}

#
# Test for bug #908375: rounding numbers that do not fit in a
# long but do fit in a wide
#

test expr-old-39.1 {Rounding with wide result} {
    set x 1.0e10
    set y [expr $x + 0.1]
    catch {
	set x [list [expr {$x == round($y)}] [expr $x == -round(-$y)]]
    }
    set x
} {1 1}
unset -nocomplain x y

# Special test for Pentium arithmetic bug of 1994:

if {(4195835.0 - (4195835.0/3145727.0)*3145727.0) == 256.0} {
    puts "Warning: this machine contains a defective Pentium processor"
    puts "that performs arithmetic incorrectly.  I recommend that you"
    puts "call Intel customer service immediately at 1-800-628-8686"
    puts "to request a replacement processor."
}

# cleanup
::tcltest::cleanupTests
return
