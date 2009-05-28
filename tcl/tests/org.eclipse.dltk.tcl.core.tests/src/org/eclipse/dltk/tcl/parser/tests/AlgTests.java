/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.tests;

import org.eclipse.dltk.tcl.core.TclParseUtil;

import junit.framework.TestCase;

public class AlgTests extends TestCase {

	public void test001() {
		String s = "";
		check(s);
	}

	public void test002() {
		String s = "a:b";
		check(s);
	}

	public void test003() {
		String s = "a::b";
		check(s);
	}

	public void test004() {
		String s = "::a:b";
		check(s);
	}

	public void test005() {
		String s = "a:b";
		check(s);
	}

	public void test006() {
		String s = "a:b::";
		check(s);
	}

	public void test007() {
		String s = "a::b::c";
		check(s);
	}

	public void test008() {
		String s = "a:::b";
		check(s);
	}

	public void test009() {
		String s = "a::b:::";
		check(s);
	}

	public void test00() {
		String s = ":::a::b";
		check(s);
	}

	private void check(String s) {
		String[] tclSplit = TclParseUtil.tclSplit(s);
		String[] split = s.split("::");
		TestCase.assertEquals(split.length, tclSplit.length);
		for (int i = 0; i < split.length; i++) {
			TestCase.assertEquals(split[i], tclSplit[i]);
		}
	}
}
