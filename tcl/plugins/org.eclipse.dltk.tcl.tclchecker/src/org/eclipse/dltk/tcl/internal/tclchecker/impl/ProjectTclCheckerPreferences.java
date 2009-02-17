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
import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.core.PreferencesDelegate;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;

public class ProjectTclCheckerPreferences extends AbstractTclCheckerPreferences {

	private final IProject project;
	private final PreferencesDelegate delegate;

	/**
	 * @param project
	 */
	public ProjectTclCheckerPreferences(IProject project) {
		this.project = project;
		this.delegate = new PreferencesDelegate(project);
		initialize();
	}

	/*
	 * @see AbstractTclCheckerPreferences#readConfiguration()
	 */
	@Override
	protected String readConfiguration() {
		return delegate.getString(TclCheckerPlugin.PLUGIN_ID,
				TclCheckerConstants.PREF_CONFIGURATION);
	}

	/*
	 * @see AbstractTclCheckerPreferences#writeConfiguration(java.lang.String)
	 */
	@Override
	protected void writeConfiguration(String value) {
		delegate.setString(TclCheckerPlugin.PLUGIN_ID,
				TclCheckerConstants.PREF_CONFIGURATION, value);
	}

	/*
	 * @see org.eclipse.dltk.tcl.tclchecker.ITclCheckerPreferences#delete()
	 */
	public void delete() {
		delegate.setString(TclCheckerPlugin.PLUGIN_ID,
				TclCheckerConstants.PREF_CONFIGURATION, null);
	}

	/*
	 * @see AbstractTclCheckerPreferences#createEnvironmentPredicate(String)
	 */
	@Override
	protected ISingleEnvironmentPredicate createEnvironmentPredicate(
			String environmentId) {
		final String projectEnvironmentId = EnvironmentManager
				.getEnvironmentId(project);
		if (projectEnvironmentId == null) {
			throw new IllegalArgumentException(
					"Could not retrieve environmentId of " + project.getName() //$NON-NLS-1$
							+ " project"); //$NON-NLS-1$
		}
		if (environmentId != null && environmentId.length() != 0) {
			Assert.isLegal(environmentId.equals(projectEnvironmentId));
		}
		return new SingleEnvironmentPredicate(projectEnvironmentId);
	}

}
