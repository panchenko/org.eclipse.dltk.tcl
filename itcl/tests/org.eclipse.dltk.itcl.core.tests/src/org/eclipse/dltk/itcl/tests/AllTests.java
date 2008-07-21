/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.itcl.tests;

import org.eclipse.dltk.itcl.tests.internal.ITclParseTests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("org.eclipse.dltk.itcl.tests"); //$NON-NLS-1$
		// $JUnit-BEGIN$
		suite.addTest(ITclParseTests.suite());
		// $JUnit-END$
		return suite;
	}

}
