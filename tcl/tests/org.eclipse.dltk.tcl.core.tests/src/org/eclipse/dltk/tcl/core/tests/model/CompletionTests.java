/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.tcl.core.tests.model;

import java.util.Arrays;

import junit.framework.Test;

import org.eclipse.dltk.codeassist.RelevanceConstants;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelCompletionTests;
import org.eclipse.dltk.core.tests.model.CompletionTestsRequestor;

public class CompletionTests extends AbstractModelCompletionTests {

	private static final int RELEVANCE = (RelevanceConstants.R_DEFAULT
			+ RelevanceConstants.R_INTERESTING + RelevanceConstants.R_CASE + RelevanceConstants.R_NON_RESTRICTED);

	public CompletionTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public void setUpSuite() throws Exception {
		this.PROJECT = this.setUpScriptProject("Completion");

		super.setUpSuite();
		// InternalDLTKLanguageManager.setPrefferedPriority(TclNature.NATURE_ID,
		// 0);
	}

	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
		// InternalDLTKLanguageManager.setPrefferedPriority(TclNature.NATURE_ID,
		// -1);
	}

	public static Test suite() {
		return new Suite(CompletionTests.class);
	}

	private String makeResult(String[] elements, String[] completions,
			int[] relevance) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < elements.length; ++i) {
			buffer.append("element:" + elements[i] + "    completion:"
					+ completions[i] + "    relevance:" + relevance[i]);

			if (i != elements.length - 1) {
				buffer.append("\n");
			}
		}
		return buffer.toString();
	}

	private String makeResult(String[] elements) {
		String[] completions = new String[elements.length];
		int[] relevance = new int[elements.length];
		for (int i = 0; i < elements.length; ++i) {
			completions[i] = elements[i];
			relevance[i] = RELEVANCE;
		}
		return this.makeResult(elements, completions, relevance);
	}

	private String makeResult(String[] elements, int[] relevance) {
		String[] completions = new String[elements.length];
		for (int i = 0; i < elements.length; ++i) {
			completions[i] = elements[i];
		}
		return this.makeResult(elements, completions, relevance);
	}

	/**
	 * Returns the location after the end of the line with the specified marker.
	 * This function is needed to compensate platform difference in line
	 * separators.
	 * 
	 * @param str
	 * @param marker
	 * @return
	 */
	private static int newLineAfter(String str, String marker) {
		int location = str.indexOf(marker);
		assertTrue(location >= 0);
		location += marker.length();
		while (location < str.length() && str.charAt(location) != '\r'
				&& str.charAt(location) != '\n') {
			++location;
		}
		if (location < str.length() && str.charAt(location) == '\r') {
			++location;
		}
		if (location < str.length() && str.charAt(location) == '\n') {
			++location;
		}
		return location;
	}

	public void testCompletion001() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"CompletionKeywordNamespace1.tcl");

		String str = cu.getSource();
		String completeBehind = "nam";
		int cursorLocation = str.lastIndexOf(completeBehind)
				+ completeBehind.length();
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "namespace",
				"namespace children", "namespace code", "namespace current",
				"namespace delete", "namespace eval", "namespace export",
				"namespace forget", "namespace import", "namespace inscope",
				"namespace parent", "namespace qualifiers", "namespace tail",
				"namespace which" }), requestor.getResults());
	}

	public void testCompletion002() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"CompletionKeywordNamespace1.tcl");

		String str = cu.getSource();
		String completeBehind = "pa";
		int cursorLocation = str.lastIndexOf(completeBehind)
				+ completeBehind.length();
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "package",
				"package provide", "package require", "part" }), requestor
				.getResults());

	}

	public void testCompletion003() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"Completion002.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "#2") + 10;
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "::a::c::fac()" },
				new String[] { "::a::c::fac" }, new int[] { 18 }), requestor
				.getResults());
	}

	public void testCompletion004() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"Completion002.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "#1") + 9;
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "::a::c::fac()",
				"::a::c::fbac()", "::a::c::feac()" }, new String[] {
				"::a::c::fac", "::a::c::fbac", "::a::c::feac" }, new int[] {
				18, 18, 18 }), requestor.getResults());

	}

	public void testCompletion005() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"Completion002.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "#3") + 6;
		cu.codeComplete(cursorLocation, requestor);

		String[] result1 = new String[] { "::a::f::faf", "::a::f::q::faf_q",
				"::a::f::q::fafq", "::a::f::q::t::fafqt", "::a::fa" };
		String[] result2 = new String[result1.length];
		for (int i = 0; i < result1.length; ++i) {
			result2[i] = result1[i];
			result1[i] += "()";
		}
		int[] relevance = new int[result1.length];
		Arrays.fill(relevance, RELEVANCE);
		assertEquals(this.makeResult(result1, result2, relevance), requestor
				.getResults());
	}

	public void testCompletion006() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"Completion002.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "#4") + 7;
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "::b::fb()" },
				new String[] { "::b::fb" }, new int[] { 22 }), requestor
				.getResults());

	}

	public void testCompletion007() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"completion003.tcl");

		String str = cu.getSource();
		String s = "puts $";
		int cursorLocation = str.indexOf(s) + s.length();
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "$::x", "$x" }, new int[] {
				18, 18 }), requestor.getResults());

	}

	public void testCompletion008() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"completion004.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "puts \"");
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "$::x", "$x" }, new int[] {
				18, 18 }), requestor.getResults());

	}

	public void testCompletion009() throws ModelException {
		CompletionTestsRequestor requestor = new CompletionTestsRequestor();
		ISourceModule cu = this.getSourceModule("Completion", "src",
				"Completion002.tcl");

		String str = cu.getSource();
		int cursorLocation = newLineAfter(str, "#5") + 16;
		cu.codeComplete(cursorLocation, requestor);

		assertEquals(this.makeResult(new String[] { "::a::c::fac()" },
				new String[] { "::a::c::fac" }, new int[] { 18 }), requestor
				.getResults());
	}
}
