/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Sergey Kanshin)
 *******************************************************************************/
package org.eclipse.dltk.tcl.formatter.tests;

import org.eclipse.dltk.ui.formatter.FormatterException;

@SuppressWarnings("nls")
public class SimpleTests extends AbstractTclFormatterTest {

	public void test1() throws FormatterException {
		String input = joinLines("proc test {} {", "\t" + "set a 20", "}");
		String output = format(input);
		assertEquals(input, output);
	}

	public void test2() throws FormatterException {
		String input = joinLines("proc test {} {", "set a 10", "set b 20",
				"set c 30", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t" + "set a 10", "\t"
				+ "set b 20", "\t" + "set c 30", "}");
		assertEquals(expected, output);
	}

	public void test3() throws FormatterException {
		String input = joinLines("proc test {} {", "\t\t\t\t" + "set a 10",
				"\t\t\t" + "set b 20", "\t\t\t\t\t" + "set c 30", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t" + "set a 10", "\t"
				+ "set b 20", "\t" + "set c 30", "}");
		assertEquals(expected, output);
	}

	public void test4() throws FormatterException {
		String input = joinLines("proc test {} {", "set a 10", "set b 20",
				"if {$a < $b} {", "puts \"Hello World!\"", "}", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t" + "set a 10", "\t"
				+ "set b 20", "\t" + "if {$a < $b} {", "\t\t"
				+ "puts \"Hello World!\"", "\t" + "}", "}");
		assertEquals(expected, output);
	}

	public void test5() throws FormatterException {
		String input = joinLines("proc test {} {", "set a 10", "set b 20",
				"if {$a < $b} { puts \"Hello World!\" }", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t" + "set a 10", "\t"
				+ "set b 20", "\t" + "if {$a < $b} { puts \"Hello World!\" }",
				"}");
		assertEquals(expected, output);
	}

	public void test6() throws FormatterException {
		String input = joinLines("proc test {} {", "after 20 cancel", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t" + "after 20 cancel",
				"}");
		assertEquals(expected, output);
	}

	public void test7() throws FormatterException {
		String input = joinLines("proc test {} {", "set a 10; set b 20", "}");
		String output = format(input);
		String expected = joinLines("proc test {} {", "\t"
				+ "set a 10; set b 20", "}");
		assertEquals(expected, output);
	}

	

}
