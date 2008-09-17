/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclPlugin;

public class TclCorePreferenceInitializer extends AbstractPreferenceInitializer {

	public TclCorePreferenceInitializer() {
	}

	public void initializeDefaultPreferences() {
		// Todo Tags
		Preferences preferences = TclPlugin.getDefault().getPluginPreferences();
		TodoTaskPreferences.initializeDefaultValues(preferences);
		// Check content
		preferences.setDefault(
				TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_LOCAL, true);
		preferences.setDefault(
				TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_REMOTE, false);
		preferences.setDefault(
				TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_LOCAL, true);
		preferences.setDefault(
				TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_REMOTE, false);
	}

}
