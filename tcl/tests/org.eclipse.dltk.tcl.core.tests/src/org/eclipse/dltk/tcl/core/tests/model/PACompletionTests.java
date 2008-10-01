/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *

 *******************************************************************************/
package org.eclipse.dltk.tcl.core.tests.model;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;

import org.eclipse.dltk.core.CompletionProposal;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelCompletionTests;

public class PACompletionTests extends AbstractModelCompletionTests {

	private static final String PROJECT_NAME = "PACompletion";

	public PACompletionTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public void setUpSuite() throws Exception {
		this.PROJECT = this.setUpScriptProject(PROJECT_NAME);
		super.setUpSuite();
		waitUntilIndexesReady();
	}

	public void tearDownSuite() throws Exception {
		super.tearDownSuite();
	}

	public static Test suite() {
		return new Suite(PACompletionTests.class);
	}

	private void testDo(String expected, String module, String pattern,
			String project) throws ModelException {
		final List proposals = new ArrayList();
		CompletionRequestor requestor = new CompletionRequestor() {
			public void accept(CompletionProposal proposal) {
				proposals.add(proposal);
			}
		};
		ISourceModule cu = this.getSourceModule(project, "src", module);

		String str = cu.getSource();
		String completeBehind = pattern;
		int cursorLocation = str.lastIndexOf(completeBehind)
				+ completeBehind.length();
		cu.codeComplete(cursorLocation, requestor);
		TestCase.assertEquals(1, proposals.size());
		CompletionProposal proposal = (CompletionProposal) proposals.get(0);
		TestCase.assertEquals(expected, proposal.getModelElement()
				.getHandleIdentifier());
	}

	public void testCompletion001() throws ModelException {
		testDo("=PACompletion/pkg1<{module0.tcl~myglobalproc", "test.tcl",
				"myglobal", PROJECT_NAME);
	}

	public void testCompletion001b() throws ModelException {
		testDo("=PACompletion/pkg1<{module0.tcl[pkgnamespace~myproc",
				"test.tcl", "pkgnamespace::my", PROJECT_NAME);
	}

	public void testCompletion002() throws ModelException {
		testDo("=PACompletion/pkg2<{module0.tcl~myglobalproc", "test2.tcl",
				"myglobal", PROJECT_NAME);
	}

	public void testCompletion002b() throws ModelException {
		testDo("=PACompletion/pkg2<{module0.tcl[pkgnamespace~myproc",
				"test2.tcl", "pkgnamespace::my", PROJECT_NAME);
	}

	public void testCompletion003() throws ModelException {
		testDo("=PACompletion/pkg1<{module0.tcl~myglobalproc", "test3.tcl",
				"myglobal", PROJECT_NAME);
	}

	public void testCompletion003b() throws ModelException {
		testDo("=PACompletion/pkg1<{module0.tcl[pkgnamespace~myproc",
				"test3.tcl", "pkgnamespace::my", PROJECT_NAME);
	}

	public void testCompletion004() throws ModelException {
		testDo("=PACompletion/pkg2<{module0.tcl~myglobalproc", "test4.tcl",
				"myglobal", PROJECT_NAME);
	}

	public void testCompletion004b() throws ModelException {
		testDo("=PACompletion/pkg2<{module0.tcl[pkgnamespace~myproc",
				"test4.tcl", "pkgnamespace::my", PROJECT_NAME);
	}
}
