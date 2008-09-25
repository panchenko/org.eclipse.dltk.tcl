/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.parser.tests;

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.emf.common.util.EList;

public class DefinitionTests extends TestCase {
	NamespaceScopeProcessor processor;

	public void test001() throws Exception {
		String source = "if { true } {" + "set a 4 5" + "} else {" + "set"
				+ "}";
		typedCheck(source, 2, 2, "8.4");
	}

	public void test002() throws Exception {
		String source = "puts [chan configured stdin -blocking]";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test003() throws Exception {
		String source = "eval return {\"lala\"}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test004() throws Exception {
		String source = "return";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test005() throws Exception {
		String source = "lrange $argv 2 $args_length";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test006() throws Exception {
		String source = "after 10000";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test007() throws Exception {
		// parser error - ats_rmserver
		String source = "catch [file delete -force [ file join \"/temp\" $rm1.$rm2.rm ] ] err";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test008() throws Exception {
		String source = "lrange $args 0 end-1";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test009() throws Exception {
		String source = "set retCode [catch $exec_cmd retVal]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test010() throws Exception {
		String source = "lindex $cmd_argv $idx";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test011() throws Exception {
		String source = "lsort $_cmd($opts)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test012() throws Exception {
		String source = "incr i -$length";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test013() throws Exception {
		String source = "string first \"\\{\" $retval";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test014() throws Exception {
		String source = "string last \"\\}\" $retval";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test015() throws Exception {
		String source = "string range $retval $start_str $end_str";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test016() throws Exception {
		String source = "if 0==[catch {exec mkfifo $f}] return";
		typedCheck(source, 0, 1, "8.4");
	}

	public void test017() throws Exception {
		String source = "proc h {} help";
		typedCheck(source, 1, 1, "8.4");
	}

	public void test018() throws Exception {
		String source = "close";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test019() throws Exception {
		String source = "lsort $images";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test020() throws Exception {
		String source = "catch domainname domainname";
		typedCheck(source, 1, 1, "8.4");
	}

	public void test021() throws Exception {
		String source = "foreach {name options} $rd break";
		typedCheck(source, 0, 1, "8.4");
	}

	public void test022() throws Exception {
		String source = "array set g {}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test023() throws Exception {
		String source = "array set o $options";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test024() throws Exception {
		String source = "catch \"rmdir $file\"";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test025() throws Exception {
		String source = "catch \"open $lockfile {RDWR}\" lock_handle";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test026() throws Exception {
		String source = "proc $name {subcmd args} \"apply $name \\$subcmd \\$args\"";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test027() throws Exception {
		String source = "array name options";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test028() throws Exception {
		String source = "lsearch $options dev*";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test029() throws Exception {
		String source = "info frame 1";
		typedCheck(source, 1, 0, "8.4");
		typedCheck(source, 0, 0, "8.5");
	}

	public void test030() throws Exception {
		String source = "after 300 SendData";
		typedCheck(source, 1, 1, "8.4");
	}

	public void test031() throws Exception {
		String source = "linsert $args 0 vfs::mkcl::Mount $mkfile $local";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test032() throws Exception {
		String source = "after $flush [list ::mk4vfs::periodicCommit $db]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test033() throws Exception {
		String source = "lindex $args 0";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test034() throws Exception {
		String source = "linsert $dst 0 interp alias $path [lindex $m 0] {}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test035() throws Exception {
		String source = "set value [[$_rep(root) select $xpath] text]";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test036() throws Exception {
		String source = "after 2000 [list Feedback $win {}]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test037() throws Exception {
		String source = "regexp -- {^(.+),.*$} $entry dummy_var";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test038() throws Exception {
		String source = "array set map {\n" + "0 2\n" + "1 3\n" + "}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test039() throws Exception {
		String source = "fconfigure stdin -block lala";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test040() throws Exception {
		String source = "package present -exact trofs 0.4.4";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test041() throws Exception {
		String source = "file attributes $f -group";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test042() throws Exception {
		String source = "fconfigure $fhandle -translation binary -encoding binary";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test043() throws Exception {
		String source = "fconfigure $data(sock2a) -sockname";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test044() throws Exception {
		String source = "subst -nocommands -novariables $val";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test045() throws Exception {
		String source = "array set typemap {Q {4 i} Y {2 s}}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test048() throws Exception {
		String source = "string trim $var";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test049() throws Exception {
		String source = "string equa $var1 $var2";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test050() throws Exception {
		String source = "set i [interp create [lindex $args 0]]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test050_2() throws Exception {
		String source = "interp create [lindex $args 0]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test050_3() throws Exception {
		String source = "lindex $args 0";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test051() throws Exception {
		String source = "regexp -- {^(.+),.*$} $entry dummy_var console_name";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test053() throws Exception {
		String source = "fconfigure $data(sock2) -translation $data(mode)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test054() throws Exception {
		String source = "return -code error -errorcode DATA lala";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test055() throws Exception {
		String source = "regexp {bench/(.*)$} $head -> format";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test056() throws Exception {
		String source = "fconfigure $sock -buffering line -blocking 1 -translation {auto crlf}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test058() throws Exception {
		String source = "list ::qweaa::accept $sock";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test059() throws Exception {
		String source = "return -errorcode DATA -code error lala";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test060() throws Exception {
		String source = "set file_list [glob -nocomplain -type f                  \\"
				+ "                  [file join $tcl_path bin"
				+ " tclsh]   \\"
				+ "                  [file join $tcl_path bin wish]    \\"
				+ "                  [file join $tcl_path bin expect]  \\"
				+ "                  [file join $tcl_path bin expectk] \\"
				+ "                  [file join $tcl_path bin tkcon]   \\"
				+ "                  [file join $tcl_path bin teacup]  \\"
				+ "                  [file join $tcl_path bin dtplite] \\"
				+ "                  [file join $tcl_path bin page]    \\"
				+ "                  [file join $tcl_path bin tcldocstrip] \\"
				+ "                  [file join / moto qwe bin nmicmpd]   \\"
				+ "                  [file join / moto qwe bin nmtrapd]]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test061() throws Exception {
		String source = "string first < $address";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test062() throws Exception {
		String source = "string last > $address";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test063() throws Exception {
		String source = "lindex $mem 0 0";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test064() throws Exception {
		String source = "fconfigure $ifh -eofchar {} -encoding binary -translation lf";
		// deprecated
		typedCheck(source, 0, 0, "8.4");
	}

	public void test065() throws Exception {
		String source = "linsert $optlist \"end-1\" \"or\"";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test066() throws Exception {
		String source = "string first $url $str";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test067() throws Exception {
		String source = "open $file $mode";
		// $mode is var not const
		typedCheck(source, 0, 0, "8.4");
	}

	public void test068() throws Exception {
		String source = "info procs ::dom::xpathFunc::*";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test069() throws Exception {
		String source = "string first = $x";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test070() throws Exception {
		String source = "trace add variable receiver {write unset} Track";
		// Unknown command:Track
		typedCheck(source, 1, 1, "8.4");
	}

	public void test071() throws Exception {
		String source = "trace vdelete var wu $data(listVarTraceCmd)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test072() throws Exception {
		String source = "trace variable var wu $data(listVarTraceCmd)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test073_FAILED() throws Exception {
		String source = "file array set destructive {"
				+ "atime 0       attributes 0  copy 1       delete 1      dirname 0"
				+ "executable 0  exists 0      extension 0  isdirectory 0 isfile 0"
				+ "join 0        lstat 0       mkdir 1      mtime 0       nativename 0"
				+ "owned 0       pathtype 0    readable 0   readlink 0    rename 1"
				+ "rootname 0    size 0        split 0      stat 0        tail 0"
				+ "type 0        volumes 0     writable 0}";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test074() throws Exception {
		String source = "lsearch -all -inline -glob -index 0 $state(names) $partial*";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test075() throws Exception {
		String source = "regsub \"${schema}:\" $type {}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test076() throws Exception {
		String source = "open $path \"r\"";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test077() throws Exception {
		String source = "file attributes $appfile -creator TKd4";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test078() throws Exception {
		String source = "file attributes $appfile -permissions +x}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test079() throws Exception {
		String source = "regexp \"compress\" $file_out";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test081() throws Exception {
		String source = "open $::globals(high) {WRONLY CREAT TRUNC}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test082() throws Exception {
		String source = "fconfigure $n(fh) -mode $speed,n,8,1 -handshake xonxoff -buffering line -translation crlf";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test083() throws Exception {
		String source = "open $logfile \"a+\"";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test084() throws Exception {
		String source = "safe::setLogCmd [namespace current]::itrace";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test085() throws Exception {
		String source = "safe::interpCreate $interp";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test086() throws Exception {
		String source = "safe::interpAddToAccessPath $slave $path";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test087() throws Exception {
		String source = "safe::interpDelete $impl(interp)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test088() throws Exception {
		String source = "package verbose 1";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test089() throws Exception {
		String source = "fconfigure $sock -blocking no -translation binary -buffering full";
		typedCheck(source, 0, 0, "8.4");
	}

	// 
	// public void test090() throws Exception {
	// String source = "trace \"Message $messageId finalized\"";
	// typedCheck(source, 0, 0, "8.4");
	// }

	public void test091() throws Exception {
		String source = "info namespace tail $name";
		typedCheck(source, 1, 0, "8.4");
		typedCheck(source, 1, 0, "8.5");
	}

	public void test092() throws Exception {
		String source = "file attributes $temp/$constructTag -group $fileStats(gid)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test093() throws Exception {
		String source = "file attributes $existingFile -readonly 0";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test094() throws Exception {
		String source = "lsort -index $sortcol -$order -$sortmode $records";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test095() throws Exception {
		String source = "source -rsrc itk:tclIndex";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test096() throws Exception {
		String source = "catch {string is . .} charclasses";
		typedCheck(source, 1, 1, "8.4");
	}

	public void test096_2() throws Exception {
		String source = "string is . .";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test097() throws Exception {
		String source = "registry set $key $value $data $mod";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test098() throws Exception {
		String source = "regexp {^(Date.+|Time.+)$} $class";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test099() throws Exception {
		String source = "open $outF { WRONLY CREAT APPEND }";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test100_FAILED() throws Exception {
		String source = "file {set fname $urlarray(path)}";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test101() throws Exception {
		String source = "unset a";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test102() throws Exception {
		String source = "unset a($b)";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test103() throws Exception {
		String source = "file delete $newSuiteFile";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test104() throws Exception {
		String source = "glob [file join $dir library *.tcl]";
		typedCheck(source, 0, 0, "8.4");
	}

	// xbz
	public void test105() throws Exception {
		String source = "info namespace all itcl";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test106() throws Exception {
		String source = "exec [file join $autotest install checkenv.tcl]";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test107() throws Exception {
		String source = "puts stdout \"+$value -- \" nonewline";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test108() throws Exception {
		String source = "regexp $compareTo $value";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test109() throws Exception {
		String source = "switch -exact -- $keyword {proc - method {set retval {}}}";
		typedCheck(source, 0, 1, "8.4");
	}

	public void test110() throws Exception {
		String source = "namespace {set retval {}}";
		typedCheck(source, 1, 0, "8.4");
	}

	public void test111() throws Exception {
		String source = "registry set $key $value $data $mod";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test112() throws Exception {
		String source = "switch $beta {Darwin - SunOS {puts alpha} SunOS {puts alpha}}";
		typedCheck(source, 0, 2, "8.4");
	}

	public void test113() throws Exception {
		String source = "switch $beta {Darwin - SunOS -}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test114() throws Exception {
		String source = "switch $beta {{Darwin} -}";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test115() throws Exception {
		String source = "switch $beta {Darwin {puts alpha} SunOS {puts alpha}}";
		typedCheck(source, 0, 2, "8.4");
	}

	public void test116() throws Exception {
		String source = "switch $os {Darwin - FreeBSD - Linux - OSF1 - SunOS {return 0} HP-UX {return 0}}";
		typedCheck(source, 0, 2, "8.4");
	}

	public void test117() throws Exception {
		String source = "switch -- $option {\"-a\" {} -starttime - -stoptime {} default {}}";
		typedCheck(source, 0, 3, "8.4");
	}

	public void test118() throws Exception {
		String source = "switch $a {Darwin - SunOS {puts 2} Windows -}";
		typedCheck(source, 0, 1, "8.4");
	}

	public void test119() throws Exception {
		String source = "switch -- $beta {Darwin - SunOS {puts alpha}}";
		typedCheck(source, 0, 1, "8.4");
	}

	public void test120() throws Exception {
		String source = "package require $args";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test121() throws Exception {
		String source = "package present $pkg";
		typedCheck(source, 0, 0, "8.4");
	}

	public void test122() throws Exception {
		String source = "socket $host daytime";
		typedCheck(source, 0, 0, "8.4");
	}

	private void typedCheck(String source, int errs, int code, String version)
			throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[2];
		System.out.println("%%%%%%%%%%%%%%%%Test:" + element.getMethodName());
		TclParser parser = new TclParser(version);
		TclErrorCollector errors = new TclErrorCollector();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, true);
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, module.size());
		TclCommand tclCommand = module.get(0);
		EList<TclArgument> args = tclCommand.getArguments();
		int scripts = 0;
		for (int i = 0; i < args.size(); i++) {
			if (args.get(i) instanceof Script) {
				scripts++;
				TestUtils.outCode(source, args.get(i).getStart(), args.get(i)
						.getEnd());
			}
			if (args.get(i) instanceof TclArgumentListImpl) {
				EList<TclArgument> innerArgs = ((TclArgumentList) args.get(i))
						.getArguments();
				for (int k = 0; k < innerArgs.size(); k++) {
					if (innerArgs.get(k) instanceof Script) {
						scripts++;
						TestUtils.outCode(source, innerArgs.get(i).getStart(),
								innerArgs.get(i).getEnd());
					}
				}
			}

		}
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		TestCase.assertEquals(code, scripts);
		TestCase.assertEquals(errs, errors.getCount());
	}
}
