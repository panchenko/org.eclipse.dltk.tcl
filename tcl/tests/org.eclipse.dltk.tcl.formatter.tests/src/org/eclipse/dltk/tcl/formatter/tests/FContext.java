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
package org.eclipse.dltk.tcl.formatter.tests;

import java.util.Map;

import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.formatter.tests.ScriptedTest.IScriptedTestContext;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;
import org.osgi.framework.Bundle;

public class FContext implements IScriptedTestContext {

	static final IScriptedTestContext CONTEXT = new FContext();

	public String validateOptionValue(String name, String value) {
		return value;
	}

	public String validateOptionName(String name) {
		return name;
	}

	public Bundle getResourceBundle() {
		return Platform.getBundle("org.eclipse.dltk.tcl.formatter.tests"); //$NON-NLS-1$
	}

	public String getCharset() {
		return "ISO-8859-1"; //$NON-NLS-1$
	}

	public IScriptFormatter createFormatter(Map<String, Object> preferences) {
		if (preferences != null) {
			final Map<String, Object> prefs = TestTclFormatter
					.createTestingPreferences();
			prefs.putAll(preferences);
			return new TestTclFormatter(Util.LINE_SEPARATOR, prefs);
		} else {
			return new TestTclFormatter();
		}
	}

}
