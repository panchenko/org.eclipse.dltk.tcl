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

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.core.PreferencesDelegate;
import org.eclipse.dltk.validators.core.ValidatorRuntime;
import org.eclipse.dltk.validators.internal.core.ValidatorsCore;

public class ProjectTclCheckerPreferences extends AbstractTclCheckerPreferences {

	private final PreferencesDelegate delegate;

	/**
	 * @param project
	 */
	public ProjectTclCheckerPreferences(IProject project) {
		this.delegate = new PreferencesDelegate(project);
		initialize();
	}

	/*
	 * @see AbstractTclCheckerPreferences#readConfiguration()
	 */
	@Override
	protected String readConfiguration() {
		return delegate.getString(ValidatorsCore.PLUGIN_ID,
				ValidatorRuntime.PREF_CONFIGURATION);
	}

	/*
	 * @see AbstractTclCheckerPreferences#writeConfiguration(java.lang.String)
	 */
	@Override
	protected void writeConfiguration(String value) {
		delegate.setString(ValidatorsCore.PLUGIN_ID,
				ValidatorRuntime.PREF_CONFIGURATION, value);
	}

	/*
	 * @see org.eclipse.dltk.tcl.tclchecker.ITclCheckerPreferences#delete()
	 */
	public void delete() {
		delegate.setString(ValidatorsCore.PLUGIN_ID,
				ValidatorRuntime.PREF_CONFIGURATION, null);
	}

}
