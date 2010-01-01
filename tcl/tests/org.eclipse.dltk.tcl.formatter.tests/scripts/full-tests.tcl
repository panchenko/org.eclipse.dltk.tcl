==== full-test1
# $Header: /autons/CVSROOT/@ats3/lib/cisco1.0/ciscoHelp.tcl,v 3.2.4.1\
1999/12/16 16:30:50 rduhamel Exp $

if {[info exists __ciscoHelp_sourced]} {
return
} else {
set __ciscoHelp_sourced 1
}

#
#  Getopt proc arg parsing.
#
# _getopt is the global option parsing table.
#
global _getopt

# these routines access the parsing table.
#
proc _getopt_type {id option} {
global _getopt
lindex $_getopt($id,$option) 0
}

proc _getopt_var {id option} {
global _getopt
lindex $_getopt($id,$option) 1
}

proc _getopt_desc {id option} {
global _getopt
lindex $_getopt($id,$option) 2
}

proc _getopt_num_arg {id option} {
global _getopt
lindex $_getopt($id,$option) 3
}

# procs to make the getopts code more readable
#
proc _getopt_is_option option {
regexp -- {^(-|\+)} $option
}

proc _getopt_is_enable_opt option {
regexp -- {^\+} $option
}

proc _getopt_is_disable_opt option {
regexp -- {^-} $option
}

proc _getopt_option_exists {id option} {
global _getopt
info exists _getopt($id,$option)
}

proc _getopt_error msg {
error $msg
}

proc _getopt_msg {opt code} {
switch -glob -- $code {
args  	{ set msg "Invalid number of arguments." }
type  	{ set msg "Invalid type." }
notype 	{ set msg "Type procedure errored or was not found." }
proc  	{ set msg "Argument check fail." }
default   { set msg "Unknown error." }
}
return "Option \"$opt\": $msg"
}

# _getopt_process is called to process the actual arguments for each option.
# It expects to be passed the id of the options, the name of the options, and
# the actual arguments that are expected to match this option.
#
proc _getopt_process {id option args} {

# error if the user did not supply all the option's arguments
#
if {[llength $args] < [_getopt_num_arg $id $option]} {
_getopt_error [_getopt_msg $option args]
}

# type check any arguments to the option if a type (proc) is defined
#
set type_proc [_getopt_type $id $option]
if {([llength $args] > 0) && ![is_null $type_proc]} {
if {[catch [concat $type_proc $args] ret_val]} {
_getopt_error [_getopt_msg $option notype]
}
if {!$ret_val} {
_getopt_error [_getopt_msg $option type]
}
}

# if no arguments are expected, then one is synthesized to indicated
# if the option switch is being "enabled" (option begins with a leading
# "+", resulting in a value of "1"), or disabled (option begins with a
# "-", which is translated to a value of "0").
#
if {[_getopt_num_arg $id $option] < 1} {
set args [_getopt_is_enable_opt $option]
}

# save the option's argument value(s) for the caller by
# calling the name in the option record if it is a proc
#
set handler [_getopt_var $id $option]

if {![is_null [info command $handler]]} {
# use the caller's stackframe to allow side-effects
uplevel 1 $handler $id $option $args
return
}

# the handler must be a variable because it is not a proc, so set the
# variable to the option argument values in the caller's frame.
#
if {[llength $args] > 2} {
uplevel 1 [list set $handler $args]
} else {
uplevel 1 set $handler $args
}
return
}

# Getopt_add adds a new entry to the global getopt arg parsing
# table.  Each call to getopt_add describes how to parse one option.
# Online help is described later in this file because the help
# system is not defined yet.
#
proc getopt_add {id opt type var desc num_arg} {
global _getopt

if {![def_natural $num_arg]} {
_getopt_error "must declare a non-negative number of option arguments"
}
if {[is_null $var]} {
_getopt_error "must specify a variable or proc to handle option\
	  values"
}
set _getopt($id,$opt) [list $type $var $desc $num_arg]
}

# Getopt_parse accepts an id (a handle used to refer to groups of options,
# usually corresponding to the name of the proc that uses the options) and
# a list of actual arguments to be parsed according to the parsing table.
# Online help for this proc is defined later in this file because the help
# system isn't running yet.
#
proc getopt_parse {id argv} {
set rest_argv {}

while {[llength $argv] > 0} {
set s [shift argv]

# skip arguments that don't match option syntax
#
if {![_getopt_is_option $s]} {
lappend rest_argv $s
continue
}

# skip options without definitions
#
if {![_getopt_option_exists $id $s]} {
lappend rest_argv $s
continue
}

# process the option in the calling stack frame's variable scope
#
set num_args [_getopt_num_arg $id $s]

set opt_args [lrange $argv 0 [expr {$num_args - 1}]]
set ret_val [catch {uplevel _getopt_process $id $s $opt_args} errortext]

if {$ret_val} {
puts "\n Invalid Number of Arguments specified on the command-line \n Program exiting... \n"
}
# remove any args just processed for the next iteration
#
set argv [lrange $argv $num_args end]
}

# return remaining, non-options, arguments
#
return $rest_argv
}

# ====================================================================
#  Cisco online help system.
# ====================================================================

# array containing information about files
#
global _file_info

# array containing proc descriptions
#
global _proc_descr

# array containing global variable descriptions
#
global _global_descr

# a variable to "override" the current idea of what file is being
# currently evaluated.  This allows multiple help sections to be
# in one file of any name.  _current_filename is a FIFO stack.
#
global _current_filename

proc _curFileName {} {
global _current_filename

if {[info exists _current_filename] && [llength _current_filename] &&\
      [string length [lindex $_current_filename 0]]} {
return [lindex $_current_filename 0]
} else {
return [basename [info script]]
}
}

proc _overrideCurFileName libName {
global _current_filename

# Push the library name on top of the stack.

lvarpush _current_filename $libName 0
}

proc _resetCurFileName {{libName {}}} {
global _current_filename

if {[info exists _current_filename] && [llength _current_filename]} {
set libName [string trim $libName]

if {![string length $libName]} {

# If a library name is not given just pop the top of the
# stack.

lvarpop _current_filename

} elseif {[set index [lsearch -exact $_current_filename $libName]] >= 0} {

# Remove library name from the stack.

set _current_filename [lreplace $_current_filename $index $index]
}
}
}

proc structureVersion version {
global _file_info
set _file_info([_curFileName],structureVersion) $version
}

proc rcsId idString {
global _file_info
set _file_info([_curFileName],fileVersion) [lindex [split $idString] 2]
set _file_info([_curFileName],rcsId) $idString
}

proc rcsLog logString {
global _file_info
set _file_info([_curFileName],rcsLog) $logString
set _file_info([_curFileName],fileLog) $logString
}

proc fileVersion version {
global _file_info
set _file_info([_curFileName],fileVersion) $version
}

proc fileType type {
global _file_info
set _file_info([_curFileName],fileType) $type
}

proc fileLog log {
global _file_info
set _file_info([_curFileName],fileLog) $log
}

proc fileDescr descr {
global _file_info
set _file_info([_curFileName],fileDescr) $descr
}

proc globalDescr {globalName descr} {
global _file_info
global _namespace_info
global _global_descr

set cur_ns [_curNamespace]
_resolveNamespace cur_ns globalName
lappend _file_info([_curFileName],globals) $globalName
lappend _namespace_info(${cur_ns},global) $globalName
if {[string equal $cur_ns ""]} {
set _global_descr($globalName) $descr
} else {
set _global_descr(${cur_ns}::$globalName) $descr
}
}

proc procDescr {procName descr} {
global _file_info
global _namespace_info
global _proc_descr

set cur_ns [_curNamespace]
_resolveNamespace cur_ns procName
lappend _file_info([_curFileName],procs) $procName
lappend _namespace_info(${cur_ns},proc) $procName
if {[string equal $cur_ns ""]} {
set _proc_descr($procName) $descr
} else {
set _proc_descr(${cur_ns}::$procName) $descr
}
}

proc beginLibHelp libName {
_overrideCurFileName $libName
}

proc endLibHelp {{libName {}}} {
_resetCurFileName $libName
}

# additionalProcDescr when called will add the procedure name and 
# arguments to the procedure description.

proc additionalProcDescr {} {
global _proc_descr

foreach nsProcName [array names _proc_descr] {
set additional_descr "Procedure: $nsProcName"

if {[string length [info procs $nsProcName]]} {
append additional_descr " {"

foreach arg [info args $nsProcName] {
if {[info default $nsProcName $arg value]} {
append additional_descr " ?${arg}?"
} else {
append additional_descr " $arg"
}
}

append additional_descr " }\n"
}

set _proc_descr($nsProcName) $additional_descr$_proc_descr($nsProcName)
}
}

# getHelpLibList returns a list libraries with help available.

proc getHelpLibList {} {
global _file_info

set match_list {}

foreach key [array names _file_info] {
if {[regexp -- "^(.*),fileType\$" $key dummy filename]} {
if {[regexp -- "library|class" $_file_info($key)]} {
lappend match_list $filename
}
}
}

return $match_list
}

######################################################################
#
# Help system for namespaces
#
######################################################################

proc _curNamespace {} {
global _file_info
global _current_namespace

if {[info exists _file_info([_curFileName],namespaces)] &&
[info exists _current_namespace] &&
[string length $_current_namespace]} {
set cur_ns ::[string trimleft $_current_namespace ":"]
return $cur_ns
}
return
}

proc _resolveNamespace {_cur_ns _name} {
upvar 1 $_cur_ns cur_ns
upvar 1 $_name   name

set idx [string last "::" $name]
if {$idx != "-1"} {
set ns_part [string range $name 0 [expr {$idx-1}]]
set name [string range $name [expr {$idx+2}] end]
if {[string first "::" $ns_part] == 0} {
set cur_ns $ns_part
} else {
append cur_ns "::" $ns_part
}
}
}

proc variableDescr {variableName descr} {
global _file_info
global _namespace_info
global _variable_descr

set cur_ns [_curNamespace]
_resolveNamespace cur_ns variableName
lappend _file_info([_curFileName],variables) $variableName
lappend _namespace_info(${cur_ns},variable) $variableName
if {[string equal $cur_ns ""]} {
set _variable_descr($variableName) $descr
} else {
set _variable_descr(${cur_ns}::$variableName) $descr
}
}

proc namespaceDescr {namespaceName descr} {
global _file_info
global _namespace_descr
global _current_namespace

if {![string length $namespaceName] && [info exists _current_namespace]} {
unset _current_namespace
return
}
lappend _file_info([_curFileName],namespaces) $namespaceName
set _file_info([_curFileName],fileType) namespace
set _current_namespace $namespaceName
set _namespace_descr($namespaceName) $descr
}

######################################################################
#
# Help system for [incr Tcl]
#
######################################################################

proc classDescr {className descr} {
global _file_info
global _class_descr
global _current_class

if {![string length $className] && [info exists _current_class]} {
unset _current_class
return
}
lappend _file_info([_curFileName],classes) $className
set _file_info([_curFileName],fileType) class
set _current_class $className
set _class_descr($className) $descr
}

proc methodDescr {methodName descr} {
global _class_info
global _current_class
global _class_descr
global _method_descr

set cur_class {}
if {[info exists _current_class]} {
set cur_class $_current_class
}

# Resolve classname from method if possible
_resolveNamespace cur_ns methodName

if {![info exists _class_descr($cur_class)]} {
puts stderr "Cannot find current class, define classDescr first."
return
}
lappend _class_info(${cur_class},method) $methodName
set _method_descr(${cur_class}::${methodName}) $descr
}

proc publicDescr {publicName descr} {
global _class_info
global _current_class
global _public_descr

lappend _class_info($_current_class,public) $publicName
set _public_descr(${_current_class}::${publicName}) $descr
}

proc fileEnd {} {
# "info script" is null when this file is _not_ being evaluated
# by the source command, but instead by expect executing the file
# directly.  The interactive command returns 1 if stdout is a tty.
#
if {[is_null [info script]] && [interactive]} {
# Since this file is running at top level it's library name
# is {} to the help system.
#
set status [catch {help +verbose library {}} help_text]
puts stdout $help_text
exit $status
}
return
}

getopt_add require +force {} force "Always source required files" 0

proc require args {
global _file_info
global requireForce

set force 0
set args [getopt_parse require $args]
set libName [lindex $args 0]
set verSpec [lindex $args 1]

# add this require to the require tree
#
lappend _file_info([_curFileName],requires) [list [basename $libName]\
$verSpec]

# sometimes sourcing the file is mandatory
#
if {$force || ( ![catch {set requireForce}] && ($requireForce != 0) )} {
return [uplevel source $libName]
}

# otherwise, we only source if the file never has been before
#
foreach entry [array names _file_info] {
if {[regexp -- "^[basename $libName],.*\$" $entry]} {
return
}
}
return [uplevel source $libName]
}

getopt_add help +printer {} printer "Format help for a line printer, and\
output to stdout" 0
getopt_add help +verbose {} verbose "Display full descriptions even when\
multiple objects match" 0

proc cisco_help args {
global _file_info
global _proc_descr
global _global_descr
global _variable_descr
global _namespace_info
global _namespace_descr
global _class_descr
global _class_info
global _method_descr
global _public_descr

set printer 0
set verbose 0
set args [getopt_parse help $args]
set keyword [lindex $args 0]
set modifier [lindex $args 1]

switch -exact -- $keyword {
proc   -
method {
set retval {}
set match_list {}

# no search expression means list all procs
#
if {[is_null $modifier]} {
set modifier {.*}
}

# test $modifier to make sure it is a valid regexp
#
if {[catch {regexp -- $modifier foobarbletch}]} {
error "\"$modifier\" is an invalid regular expression"
}

# find any matching procs
#
set match_list {}
if {$keyword == "proc"} {
set alist [array names _proc_descr]
} else {
if {![info exists _method_descr]} {
set alist ""
} else {
set alist [array names _method_descr]
}
}
foreach key $alist {
if {[regexp -- $modifier $key]} {
lappend match_list $key
}
}

# display the information from the resulting matches,
# taking care to not print full information unless a
# single match occured to avoid a deluge.
#
switch -glob -- [llength $match_list] {
	0 {return "no help for procedures matching: $modifier"}
1 {
append retval [lindex $match_list 0] \n
if { $keyword == "proc" } {
append retval $_proc_descr([lindex $match_list 0]) \n
} else {
append retval $_method_descr([lindex $match_list 0]) \n
}
return $retval
}
* {
set match_list [lsort $match_list]
foreach pname $match_list {
if {$verbose} {
if { $keyword == "proc" } {
append retval $_proc_descr($pname) \n
} else {
append retval $_method_descr($pname) \n
}
} else {
append retval [string trimleft $pname ":"] \n
}
}
return $retval
}
}
}
global   -
variable -
public   {
set retval {}
set match_list {}

# if there is no search modifier, default to .* (all globals)
#
if {[is_null $modifier]} {
set modifier {.*}
}

# test $modifier to make sure it is a valid regexp
#
if {[catch {regexp -- $modifier foobarbletch}]} {
error "\"$modifier\" is an invalid regular expression"
}

if {$keyword == "global"} {
if {![info exists _global_descr]} {
return "No `global' variable found"
}
set keys [array names _global_descr]
} elseif {$keyword == "variable"} {
if {![info exists _variable_descr]} {
return "No `variable' variable found"
}
set keys [array names _variable_descr]
} else {
if {![info exists _public_descr]} {
return "No `public' variable found"
}
set keys [array names _public_descr]
}

# find any matching globals/variables/publics
#
foreach key $keys {
if {[regexp -- $modifier $key]} {
lappend match_list $key
}
}

# display the information
#
if {[llength $match_list] < 1} {
return "no help for globals/variables/publics matching: $modifier"
}

if {$keyword == "global"} {
foreach gname [lsort $match_list] {
append retval [string trimleft $gname ":"] " -- "
append retval $_global_descr($gname) \n
}
} elseif {$keyword == "variable"} {
foreach gname [lsort $match_list] {
append retval [string trimleft $gname ":"] " -- "
append retval $_variable_descr($gname) \n
}
} else {
foreach gname [lsort $match_list] {
append retval [string trimleft $gname ":"] " -- "
append retval $_public_descr($gname)\n
}
}

return $retval
}
library {
set retval {}
set match_list {}

# $modifier == {}, list all libraries
#
if {[is_null $modifier]} {
set modifier {.*}
}

# test $modifier to make sure it is a valid regexp
#
if {[catch {regexp $modifier foobarbletch}]} {
error "\"$modifier\" is an invalid regular expression"
}

# find the names of all library files
#
foreach key [array names _file_info] {
if {[regexp "^($modifier),fileType\$" $key dummy\
filename]} {
if {[regexp "library|class" $_file_info($key)]} {
lappend match_list $filename
}
}
}

# display the information
#
if {[llength $match_list] < 1} {
return "no help for libraries matching: $modifier"
}
foreach lname [lsort $match_list] {
if {!$verbose} {
append retval "Library: $lname:\n"
if {[info exists _file_info($lname,classes)]} {
foreach class $_file_info($lname,classes) {
append retval [help class $class]
}
}
catch {append retval\
"$_file_info($lname,fileDescr)\n"}
if {[info exists _file_info($lname,procs)]} {
append retval "Procs:\n"
foreach pname [lsort $_file_info($lname,procs)] {
append retval "\t$pname\n"
}
}
} else {
if {$printer} {
append retval ""
}
append retval "\n[replicate = 78]\n\n"
append retval "Library: $lname:\n"

if {[info exists _file_info($lname,classes)]} {
foreach class $_file_info($lname,classes) {
if {$printer} {
append retval [help +printer +verbose\
class $class]
} else {
append retval [help +verbose class $class]
}
}
}

catch {append retval\
"$_file_info($lname,fileDescr)\n"}
catch {append retval\
"Requires: $_file_info($lname,requires)\n"}

if {[info exists _file_info($lname,globals)]} {
catch {append retval "Globals:\n"}
foreach gname\
[lsort $_file_info($lname,globals)] {
append retval "\n$gname"
catch {append retval $_global_descr($gname)\n}
}
}

if {[info exists _file_info($lname,procs)]} {
foreach pname [lsort $_file_info($lname,procs)] {
append retval "\n[replicate - 78]\n"
catch {append retval $_proc_descr($pname)}
}
}
}
}

append retval \n
return $retval
}
namespace {
set retval {}
set match_list {}

if {![info exists _namespace_descr]} {
return "No namespace defined"
}

# $modifier == {}, list all libraries
#
if {[is_null $modifier]} {
set modifier {.*}
}

# test $modifier to make sure it is a valid regexp
#
if {[catch {regexp $modifier foobarbletch}]} {
error "\"$modifier\" is an invalid regular expression"
	}

	# find the names of all namespaces
#
foreach key [array names _namespace_descr] {
if {[regexp "^$modifier\$" $key dummy filename]} {
lappend match_list $key
}
}

# display the information
#
if {[llength $match_list] < 1} {
return "no help for libraries matching: $modifier"
}
foreach lname [lsort $match_list] {
if {!$verbose} {
append retval "Namespace: $lname:\n"
if {[info exists _namespace_info($lname,variable)]} {
append retval "Variable:\n"
foreach pname\
[lsort $_namespace_info($lname,variable)] {
append retval "\t[string trimleft $pname ":"]\n"
}
}
if {[info exists _namespace_info($lname,proc)]} {
append retval "Proc:\n"
foreach pname\
[lsort $_namespace_info($lname,proc)] {
append retval "\t[string trimleft $pname ":"]\n"
}
}
} else {
append retval "\nNamespace: $lname:\n"
append retval "$_namespace_descr($lname)"
if {[info exists _namespace_info($lname,variable)]} {
append retval "\nPublic:\n"
foreach pname\
[lsort $_namespace_info($lname,variable)] {
append retval \t[string trimleft $pname ":"]
append retval " -- "
append retval\
$_variable_descr(${lname}::${pname})\n
}
}
if {[info exists _namespace_info($lname,proc)]} {
foreach pname\
[lsort $_namespace_info($lname,proc)] {
append retval "\n[replicate - 78]\n"
catch {append retval\
$_proc_descr(${lname}::${pname})}
}
}
}
}

append retval \n
return $retval
}
class {
set retval {}
set match_list {}

if {![info exists _class_descr]} {
return "No class defined"
}

# $modifier == {}, list all libraries
#
if {[is_null $modifier]} {
set modifier {.*}
}

# test $modifier to make sure it is a valid regexp
#
if {[catch {regexp $modifier foobarbletch}]} {
error "\"$modifier\" is an invalid regular expression"
}

# find the names of all classes
#
foreach key [array names _class_descr] {
if {[regexp "^$modifier\$" $key dummy filename]} {
lappend match_list $key
}
}

# display the information
#
if {[llength $match_list] < 1} {
return "no help for libraries matching: $modifier"
}
foreach lname [lsort $match_list] {
if {!$verbose} {
append retval "Class: $lname:\n"
if {[info exists _class_info($lname,public)]} {
append retval "Public:\n"
foreach pname\
[lsort $_class_info($lname,public)] {
append retval "\t$pname\n"
}
}
if {[info exists _class_info($lname,method)]} {
append retval "Method:\n"
foreach pname\
[lsort $_class_info($lname,method)] {
append retval "\t$pname\n"
}
}
} else {
append retval "\nClass: $lname:\n"
append retval "$_class_descr($lname)"
if {[info exists _class_info($lname,public)]} {
append retval "\nPublic:\n"
foreach pname\
[lsort $_class_info($lname,public)] {
append retval \t$pname " -- "
append retval\
			  $_public_descr(${lname}::${pname})\n
}
}
if {[info exists _class_info($lname,method)]} {
foreach pname\
[lsort $_class_info($lname,method)] {
append retval "\n[replicate - 78]\n"
catch {append retval\
$_method_descr(${lname}::${pname})}
}
}
}
}

append retval \n
return $retval
}
help {
puts $_proc_descr(::cisco_help)
}
service {
}
default {
if {[string length $keyword]} {
puts "\ncisco_help: unknown keyword \"$keyword\""
}

puts [cisco_help +verbose library cisco_help]
}
}
}
==
# $Header: /autons/CVSROOT/@ats3/lib/cisco1.0/ciscoHelp.tcl,v 3.2.4.1\
1999/12/16 16:30:50 rduhamel Exp $

if {[info exists __ciscoHelp_sourced]} {
	return
} else {
	set __ciscoHelp_sourced 1
}

#
#  Getopt proc arg parsing.
#
# _getopt is the global option parsing table.
#
global _getopt

# these routines access the parsing table.
#
proc _getopt_type {id option} {
	global _getopt
	lindex $_getopt($id,$option) 0
}

proc _getopt_var {id option} {
	global _getopt
	lindex $_getopt($id,$option) 1
}

proc _getopt_desc {id option} {
	global _getopt
	lindex $_getopt($id,$option) 2
}

proc _getopt_num_arg {id option} {
	global _getopt
	lindex $_getopt($id,$option) 3
}

# procs to make the getopts code more readable
#
proc _getopt_is_option option {
	regexp -- {^(-|\+)} $option
}

proc _getopt_is_enable_opt option {
	regexp -- {^\+} $option
}

proc _getopt_is_disable_opt option {
	regexp -- {^-} $option
}

proc _getopt_option_exists {id option} {
	global _getopt
	info exists _getopt($id,$option)
}

proc _getopt_error msg {
	error $msg
}

proc _getopt_msg {opt code} {
	switch -glob -- $code {
		args  	{ set msg "Invalid number of arguments." }
		type  	{ set msg "Invalid type." }
		notype 	{ set msg "Type procedure errored or was not found." }
		proc  	{ set msg "Argument check fail." }
		default   { set msg "Unknown error." }
	}
	return "Option \"$opt\": $msg"
}

# _getopt_process is called to process the actual arguments for each option.
# It expects to be passed the id of the options, the name of the options, and
# the actual arguments that are expected to match this option.
#
proc _getopt_process {id option args} {

	# error if the user did not supply all the option's arguments
	#
	if {[llength $args] < [_getopt_num_arg $id $option]} {
		_getopt_error [_getopt_msg $option args]
	}

	# type check any arguments to the option if a type (proc) is defined
	#
	set type_proc [_getopt_type $id $option]
	if {([llength $args] > 0) && ![is_null $type_proc]} {
		if {[catch [concat $type_proc $args] ret_val]} {
			_getopt_error [_getopt_msg $option notype]
		}
		if {!$ret_val} {
			_getopt_error [_getopt_msg $option type]
		}
	}

	# if no arguments are expected, then one is synthesized to indicated
	# if the option switch is being "enabled" (option begins with a leading
	# "+", resulting in a value of "1"), or disabled (option begins with a
	# "-", which is translated to a value of "0").
	#
	if {[_getopt_num_arg $id $option] < 1} {
		set args [_getopt_is_enable_opt $option]
	}

	# save the option's argument value(s) for the caller by
	# calling the name in the option record if it is a proc
	#
	set handler [_getopt_var $id $option]

	if {![is_null [info command $handler]]} {
		# use the caller's stackframe to allow side-effects
		uplevel 1 $handler $id $option $args
		return
	}

	# the handler must be a variable because it is not a proc, so set the
	# variable to the option argument values in the caller's frame.
	#
	if {[llength $args] > 2} {
		uplevel 1 [list set $handler $args]
	} else {
		uplevel 1 set $handler $args
	}
	return
}

# Getopt_add adds a new entry to the global getopt arg parsing
# table.  Each call to getopt_add describes how to parse one option.
# Online help is described later in this file because the help
# system is not defined yet.
#
proc getopt_add {id opt type var desc num_arg} {
	global _getopt

	if {![def_natural $num_arg]} {
		_getopt_error "must declare a non-negative number of option arguments"
	}
	if {[is_null $var]} {
		_getopt_error "must specify a variable or proc to handle option\
	  values"
	}
	set _getopt($id,$opt) [list $type $var $desc $num_arg]
}

# Getopt_parse accepts an id (a handle used to refer to groups of options,
# usually corresponding to the name of the proc that uses the options) and
# a list of actual arguments to be parsed according to the parsing table.
# Online help for this proc is defined later in this file because the help
# system isn't running yet.
#
proc getopt_parse {id argv} {
	set rest_argv {}

	while {[llength $argv] > 0} {
		set s [shift argv]

		# skip arguments that don't match option syntax
		#
		if {![_getopt_is_option $s]} {
			lappend rest_argv $s
			continue
		}

		# skip options without definitions
		#
		if {![_getopt_option_exists $id $s]} {
			lappend rest_argv $s
			continue
		}

		# process the option in the calling stack frame's variable scope
		#
		set num_args [_getopt_num_arg $id $s]

		set opt_args [lrange $argv 0 [expr {$num_args - 1}]]
		set ret_val [catch {uplevel _getopt_process $id $s $opt_args} errortext]

		if {$ret_val} {
			puts "\n Invalid Number of Arguments specified on the command-line \n Program exiting... \n"
		}
		# remove any args just processed for the next iteration
		#
		set argv [lrange $argv $num_args end]
	}

	# return remaining, non-options, arguments
	#
	return $rest_argv
}

# ====================================================================
#  Cisco online help system.
# ====================================================================

# array containing information about files
#
global _file_info

# array containing proc descriptions
#
global _proc_descr

# array containing global variable descriptions
#
global _global_descr

# a variable to "override" the current idea of what file is being
# currently evaluated.  This allows multiple help sections to be
# in one file of any name.  _current_filename is a FIFO stack.
#
global _current_filename

proc _curFileName {} {
	global _current_filename

	if {[info exists _current_filename] && [llength _current_filename] &&\
		[string length [lindex $_current_filename 0]]} {
		return [lindex $_current_filename 0]
	} else {
		return [basename [info script]]
	}
}

proc _overrideCurFileName libName {
	global _current_filename

	# Push the library name on top of the stack.

	lvarpush _current_filename $libName 0
}

proc _resetCurFileName {{libName {}}} {
	global _current_filename

	if {[info exists _current_filename] && [llength _current_filename]} {
		set libName [string trim $libName]

		if {![string length $libName]} {

			# If a library name is not given just pop the top of the
			# stack.

			lvarpop _current_filename

		} elseif {[set index [lsearch -exact $_current_filename $libName]] >= 0} {

			# Remove library name from the stack.

			set _current_filename [lreplace $_current_filename $index $index]
		}
	}
}

proc structureVersion version {
	global _file_info
	set _file_info([_curFileName],structureVersion) $version
}

proc rcsId idString {
	global _file_info
	set _file_info([_curFileName],fileVersion) [lindex [split $idString] 2]
	set _file_info([_curFileName],rcsId) $idString
}

proc rcsLog logString {
	global _file_info
	set _file_info([_curFileName],rcsLog) $logString
	set _file_info([_curFileName],fileLog) $logString
}

proc fileVersion version {
	global _file_info
	set _file_info([_curFileName],fileVersion) $version
}

proc fileType type {
	global _file_info
	set _file_info([_curFileName],fileType) $type
}

proc fileLog log {
	global _file_info
	set _file_info([_curFileName],fileLog) $log
}

proc fileDescr descr {
	global _file_info
	set _file_info([_curFileName],fileDescr) $descr
}

proc globalDescr {globalName descr} {
	global _file_info
	global _namespace_info
	global _global_descr

	set cur_ns [_curNamespace]
	_resolveNamespace cur_ns globalName
	lappend _file_info([_curFileName],globals) $globalName
	lappend _namespace_info(${cur_ns},global) $globalName
	if {[string equal $cur_ns ""]} {
		set _global_descr($globalName) $descr
	} else {
		set _global_descr(${cur_ns}::$globalName) $descr
	}
}

proc procDescr {procName descr} {
	global _file_info
	global _namespace_info
	global _proc_descr

	set cur_ns [_curNamespace]
	_resolveNamespace cur_ns procName
	lappend _file_info([_curFileName],procs) $procName
	lappend _namespace_info(${cur_ns},proc) $procName
	if {[string equal $cur_ns ""]} {
		set _proc_descr($procName) $descr
	} else {
		set _proc_descr(${cur_ns}::$procName) $descr
	}
}

proc beginLibHelp libName {
	_overrideCurFileName $libName
}

proc endLibHelp {{libName {}}} {
	_resetCurFileName $libName
}

# additionalProcDescr when called will add the procedure name and
# arguments to the procedure description.

proc additionalProcDescr {} {
	global _proc_descr

	foreach nsProcName [array names _proc_descr] {
		set additional_descr "Procedure: $nsProcName"

		if {[string length [info procs $nsProcName]]} {
			append additional_descr " {"

			foreach arg [info args $nsProcName] {
				if {[info default $nsProcName $arg value]} {
					append additional_descr " ?${arg}?"
				} else {
					append additional_descr " $arg"
				}
			}

			append additional_descr " }\n"
		}

		set _proc_descr($nsProcName) $additional_descr$_proc_descr($nsProcName)
	}
}

# getHelpLibList returns a list libraries with help available.

proc getHelpLibList {} {
	global _file_info

	set match_list {}

	foreach key [array names _file_info] {
		if {[regexp -- "^(.*),fileType\$" $key dummy filename]} {
			if {[regexp -- "library|class" $_file_info($key)]} {
				lappend match_list $filename
			}
		}
	}

	return $match_list
}

######################################################################
#
# Help system for namespaces
#
######################################################################

proc _curNamespace {} {
	global _file_info
	global _current_namespace

	if {[info exists _file_info([_curFileName],namespaces)] &&
		[info exists _current_namespace] &&
		[string length $_current_namespace]} {
		set cur_ns ::[string trimleft $_current_namespace ":"]
		return $cur_ns
	}
	return
}

proc _resolveNamespace {_cur_ns _name} {
	upvar 1 $_cur_ns cur_ns
	upvar 1 $_name   name

	set idx [string last "::" $name]
	if {$idx != "-1"} {
		set ns_part [string range $name 0 [expr {$idx-1}]]
		set name [string range $name [expr {$idx+2}] end]
		if {[string first "::" $ns_part] == 0} {
			set cur_ns $ns_part
		} else {
			append cur_ns "::" $ns_part
		}
	}
}

proc variableDescr {variableName descr} {
	global _file_info
	global _namespace_info
	global _variable_descr

	set cur_ns [_curNamespace]
	_resolveNamespace cur_ns variableName
	lappend _file_info([_curFileName],variables) $variableName
	lappend _namespace_info(${cur_ns},variable) $variableName
	if {[string equal $cur_ns ""]} {
		set _variable_descr($variableName) $descr
	} else {
		set _variable_descr(${cur_ns}::$variableName) $descr
	}
}

proc namespaceDescr {namespaceName descr} {
	global _file_info
	global _namespace_descr
	global _current_namespace

	if {![string length $namespaceName] && [info exists _current_namespace]} {
		unset _current_namespace
		return
	}
	lappend _file_info([_curFileName],namespaces) $namespaceName
	set _file_info([_curFileName],fileType) namespace
	set _current_namespace $namespaceName
	set _namespace_descr($namespaceName) $descr
}

######################################################################
#
# Help system for [incr Tcl]
#
######################################################################

proc classDescr {className descr} {
	global _file_info
	global _class_descr
	global _current_class

	if {![string length $className] && [info exists _current_class]} {
		unset _current_class
		return
	}
	lappend _file_info([_curFileName],classes) $className
	set _file_info([_curFileName],fileType) class
	set _current_class $className
	set _class_descr($className) $descr
}

proc methodDescr {methodName descr} {
	global _class_info
	global _current_class
	global _class_descr
	global _method_descr

	set cur_class {}
	if {[info exists _current_class]} {
		set cur_class $_current_class
	}

	# Resolve classname from method if possible
	_resolveNamespace cur_ns methodName

	if {![info exists _class_descr($cur_class)]} {
		puts stderr "Cannot find current class, define classDescr first."
		return
	}
	lappend _class_info(${cur_class},method) $methodName
	set _method_descr(${cur_class}::${methodName}) $descr
}

proc publicDescr {publicName descr} {
	global _class_info
	global _current_class
	global _public_descr

	lappend _class_info($_current_class,public) $publicName
	set _public_descr(${_current_class}::${publicName}) $descr
}

proc fileEnd {} {
	# "info script" is null when this file is _not_ being evaluated
	# by the source command, but instead by expect executing the file
	# directly.  The interactive command returns 1 if stdout is a tty.
	#
	if {[is_null [info script]] && [interactive]} {
		# Since this file is running at top level it's library name
		# is {} to the help system.
		#
		set status [catch {help +verbose library {}} help_text]
		puts stdout $help_text
		exit $status
	}
	return
}

getopt_add require +force {} force "Always source required files" 0

proc require args {
	global _file_info
	global requireForce

	set force 0
	set args [getopt_parse require $args]
	set libName [lindex $args 0]
	set verSpec [lindex $args 1]

	# add this require to the require tree
	#
	lappend _file_info([_curFileName],requires) [list [basename $libName]\
		$verSpec]

	# sometimes sourcing the file is mandatory
	#
	if {$force || ( ![catch {set requireForce}] && ($requireForce != 0) )} {
		return [uplevel source $libName]
	}

	# otherwise, we only source if the file never has been before
	#
	foreach entry [array names _file_info] {
		if {[regexp -- "^[basename $libName],.*\$" $entry]} {
			return
		}
	}
	return [uplevel source $libName]
}

getopt_add help +printer {} printer "Format help for a line printer, and\
output to stdout" 0
getopt_add help +verbose {} verbose "Display full descriptions even when\
multiple objects match" 0

proc cisco_help args {
	global _file_info
	global _proc_descr
	global _global_descr
	global _variable_descr
	global _namespace_info
	global _namespace_descr
	global _class_descr
	global _class_info
	global _method_descr
	global _public_descr

	set printer 0
	set verbose 0
	set args [getopt_parse help $args]
	set keyword [lindex $args 0]
	set modifier [lindex $args 1]

	switch -exact -- $keyword {
		proc   -
		method {
			set retval {}
			set match_list {}

			# no search expression means list all procs
			#
			if {[is_null $modifier]} {
				set modifier {.*}
			}

			# test $modifier to make sure it is a valid regexp
			#
			if {[catch {regexp -- $modifier foobarbletch}]} {
				error "\"$modifier\" is an invalid regular expression"
			}

			# find any matching procs
			#
			set match_list {}
			if {$keyword == "proc"} {
				set alist [array names _proc_descr]
			} else {
				if {![info exists _method_descr]} {
					set alist ""
				} else {
					set alist [array names _method_descr]
				}
			}
			foreach key $alist {
				if {[regexp -- $modifier $key]} {
					lappend match_list $key
				}
			}

			# display the information from the resulting matches,
			# taking care to not print full information unless a
			# single match occured to avoid a deluge.
			#
			switch -glob -- [llength $match_list] {
				0 {return "no help for procedures matching: $modifier"}
				1 {
					append retval [lindex $match_list 0] \n
					if { $keyword == "proc" } {
						append retval $_proc_descr([lindex $match_list 0]) \n
					} else {
						append retval $_method_descr([lindex $match_list 0]) \n
					}
					return $retval
				}
				* {
					set match_list [lsort $match_list]
					foreach pname $match_list {
						if {$verbose} {
							if { $keyword == "proc" } {
								append retval $_proc_descr($pname) \n
							} else {
								append retval $_method_descr($pname) \n
							}
						} else {
							append retval [string trimleft $pname ":"] \n
						}
					}
					return $retval
				}
			}
		}
		global   -
		variable -
		public   {
			set retval {}
			set match_list {}

			# if there is no search modifier, default to .* (all globals)
			#
			if {[is_null $modifier]} {
				set modifier {.*}
			}

			# test $modifier to make sure it is a valid regexp
			#
			if {[catch {regexp -- $modifier foobarbletch}]} {
				error "\"$modifier\" is an invalid regular expression"
			}

			if {$keyword == "global"} {
				if {![info exists _global_descr]} {
					return "No `global' variable found"
				}
				set keys [array names _global_descr]
			} elseif {$keyword == "variable"} {
				if {![info exists _variable_descr]} {
					return "No `variable' variable found"
				}
				set keys [array names _variable_descr]
			} else {
				if {![info exists _public_descr]} {
					return "No `public' variable found"
				}
				set keys [array names _public_descr]
			}

			# find any matching globals/variables/publics
			#
			foreach key $keys {
				if {[regexp -- $modifier $key]} {
					lappend match_list $key
				}
			}

			# display the information
			#
			if {[llength $match_list] < 1} {
				return "no help for globals/variables/publics matching: $modifier"
			}

			if {$keyword == "global"} {
				foreach gname [lsort $match_list] {
					append retval [string trimleft $gname ":"] " -- "
					append retval $_global_descr($gname) \n
				}
			} elseif {$keyword == "variable"} {
				foreach gname [lsort $match_list] {
					append retval [string trimleft $gname ":"] " -- "
					append retval $_variable_descr($gname) \n
				}
			} else {
				foreach gname [lsort $match_list] {
					append retval [string trimleft $gname ":"] " -- "
					append retval $_public_descr($gname)\n
				}
			}

			return $retval
		}
		library {
			set retval {}
			set match_list {}

			# $modifier == {}, list all libraries
			#
			if {[is_null $modifier]} {
				set modifier {.*}
			}

			# test $modifier to make sure it is a valid regexp
			#
			if {[catch {regexp $modifier foobarbletch}]} {
				error "\"$modifier\" is an invalid regular expression"
			}

			# find the names of all library files
			#
			foreach key [array names _file_info] {
				if {[regexp "^($modifier),fileType\$" $key dummy\
							filename]} {
					if {[regexp "library|class" $_file_info($key)]} {
						lappend match_list $filename
					}
				}
			}

			# display the information
			#
			if {[llength $match_list] < 1} {
				return "no help for libraries matching: $modifier"
			}
			foreach lname [lsort $match_list] {
				if {!$verbose} {
					append retval "Library: $lname:\n"
					if {[info exists _file_info($lname,classes)]} {
						foreach class $_file_info($lname,classes) {
							append retval [help class $class]
						}
					}
					catch {append retval\
							"$_file_info($lname,fileDescr)\n"}
					if {[info exists _file_info($lname,procs)]} {
						append retval "Procs:\n"
						foreach pname [lsort $_file_info($lname,procs)] {
							append retval "\t$pname\n"
						}
					}
				} else {
					if {$printer} {
						append retval ""
					}
					append retval "\n[replicate = 78]\n\n"
					append retval "Library: $lname:\n"

					if {[info exists _file_info($lname,classes)]} {
						foreach class $_file_info($lname,classes) {
							if {$printer} {
								append retval [help +printer +verbose\
									class $class]
							} else {
								append retval [help +verbose class $class]
							}
						}
					}

					catch {append retval\
							"$_file_info($lname,fileDescr)\n"}
					catch {append retval\
							"Requires: $_file_info($lname,requires)\n"}

					if {[info exists _file_info($lname,globals)]} {
						catch {append retval "Globals:\n"}
						foreach gname\
							[lsort $_file_info($lname,globals)] {
							append retval "\n$gname"
							catch {append retval $_global_descr($gname)\n}
						}
					}

					if {[info exists _file_info($lname,procs)]} {
						foreach pname [lsort $_file_info($lname,procs)] {
							append retval "\n[replicate - 78]\n"
							catch {append retval $_proc_descr($pname)}
						}
					}
				}
			}

			append retval \n
			return $retval
		}
		namespace {
			set retval {}
			set match_list {}

			if {![info exists _namespace_descr]} {
				return "No namespace defined"
			}

			# $modifier == {}, list all libraries
			#
			if {[is_null $modifier]} {
				set modifier {.*}
			}

			# test $modifier to make sure it is a valid regexp
			#
			if {[catch {regexp $modifier foobarbletch}]} {
				error "\"$modifier\" is an invalid regular expression"
			}

			# find the names of all namespaces
			#
			foreach key [array names _namespace_descr] {
				if {[regexp "^$modifier\$" $key dummy filename]} {
					lappend match_list $key
				}
			}

			# display the information
			#
			if {[llength $match_list] < 1} {
				return "no help for libraries matching: $modifier"
			}
			foreach lname [lsort $match_list] {
				if {!$verbose} {
					append retval "Namespace: $lname:\n"
					if {[info exists _namespace_info($lname,variable)]} {
						append retval "Variable:\n"
						foreach pname\
							[lsort $_namespace_info($lname,variable)] {
							append retval "\t[string trimleft $pname ":"]\n"
						}
					}
					if {[info exists _namespace_info($lname,proc)]} {
						append retval "Proc:\n"
						foreach pname\
							[lsort $_namespace_info($lname,proc)] {
							append retval "\t[string trimleft $pname ":"]\n"
						}
					}
				} else {
					append retval "\nNamespace: $lname:\n"
					append retval "$_namespace_descr($lname)"
					if {[info exists _namespace_info($lname,variable)]} {
						append retval "\nPublic:\n"
						foreach pname\
							[lsort $_namespace_info($lname,variable)] {
							append retval \t[string trimleft $pname ":"]
							append retval " -- "
							append retval\
								$_variable_descr(${lname}::${pname})\n
						}
					}
					if {[info exists _namespace_info($lname,proc)]} {
						foreach pname\
							[lsort $_namespace_info($lname,proc)] {
							append retval "\n[replicate - 78]\n"
							catch {append retval\
									$_proc_descr(${lname}::${pname})}
						}
					}
				}
			}

			append retval \n
			return $retval
		}
		class {
			set retval {}
			set match_list {}

			if {![info exists _class_descr]} {
				return "No class defined"
			}

			# $modifier == {}, list all libraries
			#
			if {[is_null $modifier]} {
				set modifier {.*}
			}

			# test $modifier to make sure it is a valid regexp
			#
			if {[catch {regexp $modifier foobarbletch}]} {
				error "\"$modifier\" is an invalid regular expression"
			}

			# find the names of all classes
			#
			foreach key [array names _class_descr] {
				if {[regexp "^$modifier\$" $key dummy filename]} {
					lappend match_list $key
				}
			}

			# display the information
			#
			if {[llength $match_list] < 1} {
				return "no help for libraries matching: $modifier"
			}
			foreach lname [lsort $match_list] {
				if {!$verbose} {
					append retval "Class: $lname:\n"
					if {[info exists _class_info($lname,public)]} {
						append retval "Public:\n"
						foreach pname\
							[lsort $_class_info($lname,public)] {
							append retval "\t$pname\n"
						}
					}
					if {[info exists _class_info($lname,method)]} {
						append retval "Method:\n"
						foreach pname\
							[lsort $_class_info($lname,method)] {
							append retval "\t$pname\n"
						}
					}
				} else {
					append retval "\nClass: $lname:\n"
					append retval "$_class_descr($lname)"
					if {[info exists _class_info($lname,public)]} {
						append retval "\nPublic:\n"
						foreach pname\
							[lsort $_class_info($lname,public)] {
							append retval \t$pname " -- "
							append retval\
								$_public_descr(${lname}::${pname})\n
						}
					}
					if {[info exists _class_info($lname,method)]} {
						foreach pname\
							[lsort $_class_info($lname,method)] {
							append retval "\n[replicate - 78]\n"
							catch {append retval\
									$_method_descr(${lname}::${pname})}
						}
					}
				}
			}

			append retval \n
			return $retval
		}
		help {
			puts $_proc_descr(::cisco_help)
		}
		service {
		}
		default {
			if {[string length $keyword]} {
				puts "\ncisco_help: unknown keyword \"$keyword\""
			}

			puts [cisco_help +verbose library cisco_help]
		}
	}
}