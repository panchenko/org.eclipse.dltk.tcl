/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.ui.tests.swtbot.complex.EditorTests;
import org.eclipse.dltk.ui.tests.swtbot.complex.InterpreterTests;
import org.eclipse.dltk.ui.tests.swtbot.complex.ProjectContentTests;
import org.eclipse.dltk.ui.tests.swtbot.complex.ProjectTests;

public class AllTests {

	public static Test suite() {
		EnvironmentManager.getEnvironments();
		TestSuite suite = new TestSuite("SWTBot tests for "
				+ Activator.PLUGIN_ID);
		// $JUnit-BEGIN$

		suite.addTestSuite(ProjectTests.class);
		suite.addTestSuite(ProjectContentTests.class);
		suite.addTestSuite(EditorTests.class);
		suite.addTestSuite(InterpreterTests.class);
		// suite.addTestSuite(LaunchTests.class);

		// suite.addTest(new
		// ProjectContentTests("testCreateFolderByContextMenu"));
		// suite.addTest(new ProjectTests("testCreateTclProject006"));

		// suite.addTest(new InterpreterTests("testRemoveFromProject"));
		// suite.addTest(new InterpreterTests("testRemoveInterpreters"));
		// suite.addTest(new ProjectTests("testCreateTclProject005"));
		// suite.addTest(new ProjectTests("testCreateTclProject006"));

		// $JUnit-END$
		return suite;
	}
}
