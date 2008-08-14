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

package org.eclipse.dltk.tcl.internal.validators;

import java.util.Map;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.tcl.internal.validators.ChecksExtensionManager.TclCheckInfo;

public class CheckPreferenceManager {
	private Preferences preferences;

	public CheckPreferenceManager(Preferences pluginPreferences) {
		this.preferences = pluginPreferences;
	}

	public boolean isEnabled(TclCheckInfo info) {
		return true;
	}

	public Map<String, String> getOptions(TclCheckInfo info) {
		return null;
	}
}
