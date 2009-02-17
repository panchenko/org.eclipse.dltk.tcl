/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.tclchecker;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;

/**
 * Represents the system or project-specific TclChecker preferences.
 */
public interface ITclCheckerPreferences {

	/**
	 * Returns list of TclChecker configurations.
	 * 
	 * @return
	 */
	List<CheckerConfig> getConfigurations();

	/**
	 * Creates new configuration
	 * 
	 * @return
	 */
	CheckerConfig newConfiguration();

	/**
	 * Removes the specified configuration
	 * 
	 * @param config
	 * @return <code>true</code> if the specified <code>config</code> was
	 *         removed from preferences, <code>false</code> otherwise.
	 */
	boolean removeConfiguration(CheckerConfig config);

	/**
	 * Returns the environment specific configuration. For system preferences
	 * <code>environmentId</code> is required, but for project-specific
	 * preferences <code>null</code> could be used instead.
	 * 
	 * @param environmentId
	 * @return
	 */
	CheckerInstance getEnvironment(String environmentId);

	/**
	 * Saves changes in this object to the permanent storage
	 * 
	 * @throws CoreException
	 */
	void save() throws CoreException;

	/**
	 * Completely deletes TclChecker configuration
	 */
	void delete();

}
