###############################################################################
# Copyright (c) 2005, 2007 IBM Corporation and others.
# All rights reserved. This program and the accompanying materials
# are made available under the terms of the Eclipse Public License v1.0
# which accompanies this distribution, and is available at
# http://www.eclipse.org/legal/epl-v10.html
#

###############################################################################

# Parsing examples
# $xy  => no
# ^
# $xy  => all
#  ^
# $xy  => begins with "x"
#   ^
# $xy  => begins with "xy"
#    ^

# Match patterns
set varPattern {\$[_0-9a-zA-Z]*}
set procPattern {[_0-9a-zA-Z]+}

set strStdout ""
set evaluateError 0

proc log_puts {args} {
  #puts -nonewline "\["; foreach item $args { puts -nonewline $item }; puts "]"
}

proc err_puts {str} {
  global strStdout
  append strStdout $str
}

# Internal interpreter
proc my_gets {args} {

	switch -exact -- [llength $args] {
		1 {
			set arg0 [lindex $args 0]

			if {$arg0 eq "stdin"} {
				err_puts "Input from 'stdin' not supported\n"
			} else {
				return [evaluate "::__dltk__gets__ $arg0"]
			}
			
			return
		}

		2 {
			set arg0 [lindex $args 0]
			set arg1 [lindex $args 1]

			if {$arg0 eq "stdin"} {
				err_puts "Input from 'stdin' not supported\n"
			} else {
				return [evaluate "::__dltk__gets__ $arg0 $arg1"]
			}
			
			return
		}
	}

	err_puts "wrong # args: should be \"gets channelId ?varName?\"\n"
}

proc my_exit {args} {
	err_puts "'exit' command not supported, please close console from Eclipse\n"
	return 0
}

proc getOutput { {clear 1} } {
	global strStdout

	set output $strStdout

	if {$clear} {
		set strStdout ""
	}

	return $output
}


proc getInput { {clear 1} } {

}

interp create foo

proc evaluate {statement} {
        global foo
        global evaluateError 
        set evaluateError [catch { foo eval $statement } res]
        foo alias exit my_exit
        return $res
}

evaluate "rename gets ::__dltk__gets__"
evaluate "rename exit ::__dltk__exit__"

foo alias gets my_gets
foo alias exit my_exit

proc findProcs { {pattern "*"} } {
	return [evaluate "uplevel #0 info procs $pattern"]
}

proc findCommands { {pattern "*"} } {
	set cs [evaluate "uplevel #0 info commands $pattern"]	
	
	set commands {}
	foreach c $cs {
		if { $c eq "__dltk__puts__" || $c eq "__dltk__gets__" || $c eq "__dltk__exit__" } {
			continue
		}
	
		lappend commands $c
	}
	
	return $commands	

	#return $cs
}

proc findVars { {pattern "*"} } {
	return [evaluate "uplevel #0 info vars $pattern"]
}

proc hasProc {procName} {
	set procs [findProcs]
	set index [lsearch $procs $procName]

	if {$index == -1} {
		return 0
	}

	return 1
}

proc getProcArgs {procName} {
	return [evaluate "uplevel #0 info args $procName"]
}

proc getProcBody {procName} {
	return [evaluate "uplevel #0 info body $procName"]
}

proc hasVar {varName} {
	set vars [findVars]
	set index [lsearch $vars $varName]

	if {$index == -1} {
		return 0
	}

	return 1
}


# Sample print of command line
proc printCommandLine {line pos} {
	puts $line
	puts "[string repeat " " $pos]^"
}

# Location utility functions
proc printLocation {loc} {
	set first [lindex $loc 0]
	set last [lindex $loc 1]
	puts "\[$first, $last\]"
}

proc isLocation {loc} {
	if {[llength $loc]} {
		return 1
	} else {
		return 0
	}
}

# Routines for completion
proc matchPattern { line pos pattern } {
	set start 0

	while { [regexp -indices -start $start $pattern $line loc] } {
		set first [lindex $loc 0]
		set last [lindex $loc 1]

		#printLocation $loc

		if {$pos >= $first && $pos <= $last + 1} {
			return $loc
		}

		set start [expr {$last + 1}]
	}

	return {}
}

proc extractVarPrefix { loc line pos } {
	set first [lindex $loc 0]
	set last [lindex $loc 1]

	if {$first == $pos} {
		return 0
	}

	return [string range $line [expr {$first + 1}] [expr {$pos - 1}]]
}

proc extractVarName { loc line pos } {
	set first [lindex $loc 0]
	set last [lindex $loc 1]

	if {$first == $pos} {
		return ""
	}

	return [string range $line [expr {$first + 1}] $last]
}



proc extractProcPrefix { loc line pos } {
	set first [lindex $loc 0]
	set last [lindex $loc 1]

	if {$first == $pos} {
		return ""
	}

	return [string range $line $first [expr {$pos - 1}]]
}

proc extractProcName { loc line pos } {
	set first [lindex $loc 0]
	set last [lindex $loc 1]

	return [string range $line $first $last]
}


# XML generation for completion
proc makeConsoleNode {body} {
	set str "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>\n"
	append str "<console>$body</console>"
	return $str
}

proc makeInfoNode {id} {
	return "<info id=\"$id\"></info>"
}

proc makeShellNode {body} {
	return "<shell>$body</shell>"
}

proc makeCloseNode {} {
	return "<close/>"
}

proc makeDescriptionNode {body} {
	return "<description>$body</description>"
}

proc makeInterpreterNode {state stream body} {
	return "<interpreter state=\"$state\" stream=\"$stream\"><!\[CDATA\[$body\]\]></interpreter>"
}

proc makeCompletionNode {body} {
	return "<completion>$body</completion>"
}

proc makeCompletionCaseNode {display insert type} {
	return "<case display=\"$display\" insert=\"$insert\" type=\"$type\" />"
}

proc makeCompletion { commandLine cursorPos } {
	# Utility functions
	proc skipPrefix { prefixLen str } {
		set last [expr [string length $str] - 1]
		return [string range $str $prefixLen $last]
	}

	proc generateCompletions {prefix matches type} {
		set xml ""
		foreach match $matches {
			set insert [skipPrefix [string length $prefix] $match]

			if {$type == "proc"} {
				if {[hasProc $match]} {
					set args [getProcArgs $match]
					append match " \{$args\}"
				}
			}

			append xml [makeCompletionCaseNode $match $insert $type]
		}
		return $xml
	}

	proc completeProc { prefix } {
		set pattern $prefix
		append pattern "*"
		log_puts "Proc: |$pattern|"
		set commands [findCommands $pattern]
        return [generateCompletions $prefix $commands "proc"]

	}

	proc completeVar { prefix } {
		set pattern $prefix
		append pattern "*"
		log_puts "Var: |$pattern|"
		set vars [findVars $pattern]

                return [generateCompletions $prefix $vars "var"]
	}

	global procPattern varPattern

	set xml ""

	set loc [matchPattern $commandLine $cursorPos $varPattern]

	if {[isLocation $loc]} {
		#printLocation $loc
		set varNamePrefix [extractVarPrefix $loc $commandLine $cursorPos]
		if {$varNamePrefix != 0} {
			append xml [completeVar $varNamePrefix]
		} else {
			log_puts "No var name"
		}
	} else {
		set loc [matchPattern $commandLine $cursorPos $procPattern]
		if {[isLocation $loc]} {
			set procNamePrefix [extractProcPrefix $loc $commandLine $cursorPos]
			if {$procNamePrefix != 0} {
				append xml [completeProc $procNamePrefix]
			} else {
				log_puts "No proc name"
			}
		} else {
			log_puts "Generate default list"
			append xml [completeProc ""]
			append xml [completeVar ""]
		}
	}


	return [makeCompletionNode $xml]
}

proc makeDescription { commandLine cursorPos } {
	proc describeVar {name} {
		if {$name eq ""} {
			return ""
		}

		if {[hasVar $name]} {
			set value [evaluate "expr \$$name"]
			return  "$name = $value"
		} else {
			return ""
		}
	}

	proc describeProc {name} {
		if {[hasProc $name]} {
			set args [getProcArgs $name]
			#set body [getProcBody $name]
			return  "$name \{$args\}"
		} else {
			return ""
		}
	}

	global procPattern varPattern

	set xml ""

	set loc [matchPattern $commandLine $cursorPos $varPattern]

	if {[isLocation $loc]} {
		set name [extractVarName $loc $commandLine $cursorPos]
		set xml [describeVar $name]
	} else {
		set loc [matchPattern $commandLine $cursorPos $procPattern]
		if {[isLocation $loc]} {
			set name [extractProcName $loc $commandLine $cursorPos]
			set xml [describeProc $name]
		}
	}

	return [makeDescriptionNode $xml]
}


# Communication functions
proc connectToServer {host port} {
	set client [socket $host $port]
	fconfigure $client -translation binary
	return $client
}


proc disconnectFromServer {client} {
	close $client
}

proc sendResponse { out str } {
	proc makeFixedLength {val len} {
		set n [string length $val]
		set str [string repeat "0" [expr {$len - $n}]]
		append str $val
		return $str
	}

	set len [string length $str]
	set strLen [makeFixedLength $len 10]

	puts -nonewline $out $strLen
	puts -nonewline $out $str

	flush $out
}


# Shell handler
set closeFlag 0

proc shellHandler {input} {
	global closeFlag

	log_puts "Invoking shellHandler..."

	gets $input command

	log_puts "Command is $command"

	switch -exact -- $command {
		"close" {
			set closeFlag 1
			return [makeShellNode [makeCloseNode]]
		}

		"complete" {
			gets $input commandLine
			log_puts "Command line: $commandLine"
			gets $input cursorPosition
			log_puts "Position: $cursorPosition"

			return [makeShellNode [makeCompletion $commandLine $cursorPosition]]
		}

		"describe" {
			gets $input commandLine
			log_puts "Command line: $commandLine"
			gets $input cursorPosition
			log_puts "Position: $cursorPosition"

			return [makeShellNode [makeDescription $commandLine $cursorPosition]]
		}

		default {
			log_puts "Invalid shell command..."
		}
	}
}

# Interpreter handler
set command ""

proc interpreterHandler {input} {
        global command

        log_puts "Invoking interpreterHandler..."

        gets $input str

        append str "\n"
        append command $str

        if {[info complete $command]} {
                set res [evaluate $command]
                set command ""

                set output [getOutput]

                if {$output ne ""} {
                        set node [makeInterpreterNode "new" "stderr" $output]
                } elseif { $res ne "" } {
                        append res "\n"
                        global evaluateError
                        if $evaluateError {
                                set stream "stderr"
                        } else {
                                set stream "stdout"                      
                        }
                        set node [makeInterpreterNode "new" $stream $res]
                } else {
                        set node [makeInterpreterNode "new" "" ""]
                }

                return $node
        }

        return [makeInterpreterNode "continue" "" ""]
}

# Reading command line args
set host [lindex $argv 0]
set port [lindex $argv 1]
set id   [lindex $argv 2]

log_puts "Host: $host Port: $port ID: $id"

# Testing
#set in stdin
#set out stdout

# Connection
set server [connectToServer $host $port]
set in $server
set out $server

# Send initial information
sendResponse $out [makeConsoleNode [makeInfoNode $id]]

# Execute script with output
if {[llength $argv] > 3} {
	log_puts "Sourcing script..."

	set script [lindex $argv 3]
	if {$script != "--noscript" } {
		log_puts "Script args: "
		set scriptArgs {}
		for {set i 4} {$i < $argc} {incr i} {
			lappend scriptArgs [lindex $argv $i]
		}
		evaluate [list set argc [llength $scriptArgs]]
		evaluate [list set argv $scriptArgs]

		#puts "Script: $script"
		set res [evaluate [list source $script]]
	}
}
# END OF INITIAL RESPONSE - part of protocol for now
puts -nonewline $out "\0"
flush $out

# Handle commands and send responses
proc localHandler {} {
	global in
	global out
	global closeFlag

	gets $in command

	if {$command eq "shell"} {
		set body [shellHandler $in]
		sendResponse $out [makeConsoleNode $body]
	} elseif {$command eq "interpreter"} {
		set body [interpreterHandler $in]
		sendResponse $out [makeConsoleNode $body]
	}
	
	if {[eof $in]} {
        close $in
        exit 0
    }

	if {$closeFlag} {
		exit 0
	}
}

fileevent $in readable localHandler
vwait __forever__
