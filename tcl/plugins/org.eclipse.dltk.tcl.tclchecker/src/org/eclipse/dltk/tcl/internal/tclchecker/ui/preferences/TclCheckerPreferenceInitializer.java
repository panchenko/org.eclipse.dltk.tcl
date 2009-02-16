/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclCheckerPreferenceInitializer extends
		AbstractPreferenceInitializer {

	public TclCheckerPreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		final IPreferenceStore store = TclCheckerPlugin.getDefault()
				.getPreferenceStore();
		store.setDefault(TclCheckerConstants.PREF_CONFIGURATION,
				Util.EMPTY_STRING);
	}
}
