/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblemDescription;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclCheckerPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public TclCheckerPreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		IPreferenceStore store = TclCheckerPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(TclCheckerConstants.PREF_VERSION,
				TclCheckerConstants.VERSION_5);
		store.setDefault(TclCheckerConstants.CLI_OPTIONS, Util.EMPTY_STRING);
		store.setDefault(TclCheckerConstants.PREF_MODE,
				TclCheckerConstants.MODE_DEFAULT);

		List<String> problems = TclCheckerProblemDescription
				.getProblemIdentifiers();
		for (String problemId : problems) {
			store.setDefault(problemId,
					TclCheckerConstants.PROCESS_TYPE_DEFAULT);
		}
		final String[] checkedByDefaultProblems = { "warnUndefinedUpvar", //$NON-NLS-1$
				"warnUndefinedVar", "warnUndefFunc", "warnUndefProc" }; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		for (String problemId : checkedByDefaultProblems) {
			store.setDefault(problemId, TclCheckerConstants.PROCESS_TYPE_CHECK);
		}
	}
}
