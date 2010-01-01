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

import junit.framework.TestSuite;

import org.eclipse.dltk.formatter.tests.ScriptedTest;
import org.eclipse.dltk.tcl.formatter.TclFormatterConstants;
import org.eclipse.dltk.ui.CodeFormatterConstants;

public class FullTest extends ScriptedTest {

	@SuppressWarnings("nls")
	public static TestSuite suite() {
		final FullTest tests = new FullTest();
		tests.setPreference(TclFormatterConstants.FORMATTER_TAB_CHAR,
				CodeFormatterConstants.TAB);
		tests.setPreference(TclFormatterConstants.FORMATTER_INDENTATION_SIZE,
				new Integer(1));
		tests.setPreference(TclFormatterConstants.INDENT_SCRIPT, true);
		tests.setPreference(TclFormatterConstants.INDENT_AFTER_BACKSLASH, true);
		tests.setPreference(TclFormatterConstants.LINES_FILE_AFTER_PACKAGE, 1);
		tests.setPreference(TclFormatterConstants.LINES_FILE_BETWEEN_PROC, 1);
		tests.setPreference(TclFormatterConstants.LINES_PRESERVE, 1);
		return tests.createScriptedSuite(FContext.CONTEXT,
				"scripts/full-tests.tcl");
	}

}
