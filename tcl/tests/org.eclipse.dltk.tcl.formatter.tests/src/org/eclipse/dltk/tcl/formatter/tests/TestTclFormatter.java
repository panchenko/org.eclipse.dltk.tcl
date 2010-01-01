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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.formatter.TclFormatter;
import org.eclipse.dltk.tcl.formatter.TclFormatterConstants;
import org.eclipse.dltk.ui.CodeFormatterConstants;

public class TestTclFormatter extends TclFormatter {

	public TestTclFormatter() {
		super(Util.LINE_SEPARATOR, createTestingPreferences());
	}

	public TestTclFormatter(String lineDelimiter,
			Map<String, Object> preferences) {
		super(lineDelimiter, preferences);
	}

	@Override
	protected boolean isValidation() {
		return false;
	}

	public static Map<String, Object> createTestingPreferences() {
		final Map<String, Object> result = new HashMap<String, Object>();
		for (int i = 0; i < INDENTING.length; ++i) {
			result.put(INDENTING[i], Boolean.TRUE);
		}
		for (int i = 0; i < BLANK_LINES.length; ++i) {
			result.put(BLANK_LINES[i], -1);
		}
		result.put(TclFormatterConstants.LINES_PRESERVE, 1);
		result.put(TclFormatterConstants.FORMATTER_TAB_CHAR,
				CodeFormatterConstants.TAB);
		result.put(TclFormatterConstants.FORMATTER_INDENTATION_SIZE, 1);
		return result;
	}

}
