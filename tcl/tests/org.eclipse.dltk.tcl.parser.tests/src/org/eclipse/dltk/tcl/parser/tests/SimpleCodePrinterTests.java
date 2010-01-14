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

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;

public class SimpleCodePrinterTests extends TestCase {
	NamespaceScopeProcessor processor;

	public void test001() throws Exception {
		outCheck("after 10 {puts alpha}", "after 10 {puts alpha}");
	}

	public void test002() throws Exception {
		outCheck("source $arg/beta.tcl", "source $arg/beta.tcl");
	}

	public void test003() throws Exception {
		outCheck("source {$arg/beta 2.tcl}", "source {$arg/beta 2.tcl}");
	}

	public void test004() throws Exception {
		outCheck("source \"$arg/beta 2.tcl\"", "source \"$arg/beta 2.tcl\"");
	}

	public void test005() throws Exception {
		outCheck("source [file join $dir alfa.tcl]",
				"source [file join $dir alfa.tcl]");
	}

	public void test006() throws Exception {
		outCheck("file delete $path(gorp.file)", "file delete $path(gorp.file)");
	}

	public void test007() throws Exception {
		outCheck("file delete $path(gorp.file)\n",
				"file delete $path(gorp.file)");
	}

	public void test008() throws Exception {
		outCheck("file delete $path($result,$str)",
				"file delete $path($result,$str)");
	}

	public void test009() throws Exception {
		outCheck("file delete $path($result,$str)\n",
				"file delete $path($result,$str)");
	}

	public void test010() throws Exception {
		outCheck(
				"proc hello2 {name2} {\n\t" + "puts \"Hello, $name2\"\n" + "}",
				"proc hello2 {name2} {  puts \"Hello, $name2\" }");
	}

	public void test011() throws Exception {
		outCheck("if {$DEF(cancel) == $caller} {$caller} else {.$caller}",
				"if {$DEF(cancel) == $caller} {$caller} else {.$caller}");
	}

	public void test012() throws Exception {
		String s = "if {     $DEF(cancel)      ==      $caller     } {$caller} else {.$caller}";
		outCheck(s, s);
	}

	public void test013() throws Exception {
		String s = "if      {     $DEF(         cancel             )      ==      $caller     } {      $caller    } else {.$caller}";
		outCheck(s, s);
	}

	public void test014() throws Exception {
		String s = "proc alfa {    a    {   bbbb  }  {c {    d   }   } {    }";
		outCheck(s, s);
	}

	public void test015() throws Exception {
		String s = "set a [    alfa]";
		outCheck(s, s);
	}

	public void test016() throws Exception {
		String s = "set a [    alfa      ]";
		outCheck(s, s);
	}

	public void test017() throws Exception {
		String s = "set a       [    alfa                 ]";
		outCheck(s, s);
	}

	private void outCheck(String source, String expected) throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
		TclParser parser = new TclParser("8.4");
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		String actual = SimpleCodePrinter.getCommandsString(module);
		TestCase.assertEquals(expected, actual);
	}
}
