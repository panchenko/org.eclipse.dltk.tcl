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
package org.eclipse.dltk.tcl.internal.tclchecker.impl;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;

public class SystemTclCheckerPreferences extends AbstractTclCheckerPreferences {

	public SystemTclCheckerPreferences() {
		initialize();
	}

	/*
	 * @see AbstractTclCheckerPreferences#readConfiguration()
	 */
	@Override
	protected String readConfiguration() {
		return TclCheckerPlugin.getDefault().getPluginPreferences().getString(
				TclCheckerConstants.PREF_CONFIGURATION);
	}

	/*
	 * @see AbstractTclCheckerPreferences#writeConfiguration(String)
	 */
	@Override
	protected void writeConfiguration(String value) {
		TclCheckerPlugin.getDefault().getPluginPreferences().setValue(
				TclCheckerConstants.PREF_CONFIGURATION, value);
		TclCheckerPlugin.getDefault().savePluginPreferences();
	}

	/*
	 * @see AbstractTclCheckerPreferences#createEnvironmentPredicate(String)
	 */
	@Override
	protected ISingleEnvironmentPredicate createEnvironmentPredicate(
			String environmentId) {
		Assert.isNotNull(environmentId);
		Assert.isLegal(environmentId.length() != 0);
		return new SingleEnvironmentPredicate(environmentId);
	}

	/*
	 * @see ITclCheckerPreferences#delete()
	 */
	public void delete() {
		writeConfiguration(Util.EMPTY_STRING);
	}

}
