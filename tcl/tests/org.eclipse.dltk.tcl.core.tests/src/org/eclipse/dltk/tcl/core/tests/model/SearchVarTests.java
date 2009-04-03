/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.core.tests.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.tests.model.AbstractSingleProjectSearchTests;
import org.eclipse.dltk.core.tests.model.TestSearchResults;

public class SearchVarTests extends AbstractSingleProjectSearchTests {

	public SearchVarTests(String testName) {
		super(Activator.PLUGIN_ID, testName, "SearchVar");
	}

	public static Suite suite() {
		return new Suite(SearchVarTests.class);
	}

	public void testSearchVariable() throws CoreException {
		final TestSearchResults results = search("name", FIELD, REFERENCES);
		assertEquals(1, results.size());
		results.assertMethod("hello");
	}

	public void testSearchVariableSubstitution() throws CoreException {
		final TestSearchResults results = search("name2", FIELD, REFERENCES);
		assertEquals(1, results.size());
		results.assertMethod("hello2");
	}

}
