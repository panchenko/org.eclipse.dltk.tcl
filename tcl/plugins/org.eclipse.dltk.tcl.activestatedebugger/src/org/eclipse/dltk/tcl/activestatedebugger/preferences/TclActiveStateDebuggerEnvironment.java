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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Map;

import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.ITclActiveStateDebuggerEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclActiveStateDebuggerEnvironment implements
		ITclActiveStateDebuggerEnvironment {

	private final IEnvironment environment;

	/**
	 * @param environment
	 */
	public TclActiveStateDebuggerEnvironment(IEnvironment environment) {
		this.environment = environment;
	}

	public String getDebuggerPath() {
		return getEnviromentPath(TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY);
	}

	public String getLoggingPath() {
		return getEnviromentPath(TclActiveStateDebuggerConstants.LOG_FILE_NAME);
	}

	public String getPDXPath() {
		return getEnviromentPath(TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY);
	}

	public void setDebuggerPath(String path) {
		setEnvironmentPath(
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY, path);
	}

	public void setLoggingPath(String path) {
		setEnvironmentPath(TclActiveStateDebuggerConstants.LOG_FILE_NAME, path);
	}

	public void setPDXPath(String path) {
		setEnvironmentPath(
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY,
				path);
	}

	/**
	 * @param key
	 * @return
	 */
	private String getEnviromentPath(String key) {
		final IPreferenceStore store = TclActiveStateDebuggerPlugin
				.getDefault().getPreferenceStore();
		final Map<?, ?> paths = EnvironmentPathUtils.decodePaths(store
				.getString(key));
		return (String) paths.get(environment);
	}

	/**
	 * @param key
	 * @param path
	 */
	private void setEnvironmentPath(String key, String path) {
		final IPreferenceStore store = TclActiveStateDebuggerPlugin
				.getDefault().getPreferenceStore();
		@SuppressWarnings("unchecked")
		final Map<IEnvironment, String> paths = EnvironmentPathUtils
				.decodePaths(store.getString(key));
		final String oldPath = paths.get(environment);
		if (path != null) {
			if (path.equals(oldPath)) {
				return;
			}
			paths.put(environment, path);
		} else {
			if (oldPath == null) {
				return;
			}
			paths.remove(environment);
		}
		store.setValue(key, EnvironmentPathUtils.encodePaths(paths));
	}

}
