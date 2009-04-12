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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.ITclActiveStateDebuggerEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.utils.TextUtils;
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

	public boolean isLoggingEnabled() {
		return getEnvironmentBoolean(
				TclActiveStateDebuggerConstants.LOG_ENABLE_KEY, true);
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

	public void setLoggingEnabled(boolean value) {
		setEnvironmentBoolean(TclActiveStateDebuggerConstants.LOG_ENABLE_KEY,
				value);
	}

	public void setLoggingPath(String path) {
		setEnvironmentPath(TclActiveStateDebuggerConstants.LOG_FILE_NAME, path);
	}

	public void setPDXPath(String path) {
		setEnvironmentPath(
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY,
				path);
	}

	private static IPreferenceStore getPreferenceStore() {
		return TclActiveStateDebuggerPlugin.getDefault().getPreferenceStore();
	}

	private static final char ENVIRONMENT_SEPARATOR = ';';
	private static final char VALUE_SEPARATOR = '=';

	public static Map<String, Boolean> decodeBooleans(String value) {
		final Map<String, Boolean> result = new HashMap<String, Boolean>();
		final String[] parts = TextUtils.split(value, ENVIRONMENT_SEPARATOR);
		if (parts != null) {
			for (String part : parts) {
				int pos = part.indexOf(VALUE_SEPARATOR);
				if (pos > 0) {
					final String environmentId = part.substring(0, pos);
					final String environmentValue = part.substring(pos + 1);
					result
							.put(environmentId, Boolean
									.valueOf(environmentValue));
				}
			}
		}
		return result;
	}

	public static String encodeBooleans(Map<String, Boolean> value) {
		final StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, Boolean> entry : value.entrySet()) {
			if (sb.length() != 0) {
				sb.append(ENVIRONMENT_SEPARATOR);
			}
			sb.append(entry.getKey());
			sb.append(VALUE_SEPARATOR);
			sb.append(entry.getValue().toString());
		}
		return sb.toString();
	}

	/**
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	private boolean getEnvironmentBoolean(String key, boolean defaultValue) {
		final IPreferenceStore store = getPreferenceStore();
		final Map<String, Boolean> values = decodeBooleans(store.getString(key));
		Boolean value = values.get(key);
		if (value == null) {
			value = Boolean.valueOf(defaultValue);
		}
		return value.booleanValue();
	}

	/**
	 * @param key
	 * @param value
	 */
	private void setEnvironmentBoolean(String key, boolean value) {
		final IPreferenceStore store = getPreferenceStore();
		final Map<String, Boolean> values = decodeBooleans(store.getString(key));
		values.put(environment.getId(), value);
		store.setValue(key, encodeBooleans(values));
	}

	/**
	 * @param key
	 * @return
	 */
	private String getEnviromentPath(String key) {
		final IPreferenceStore store = getPreferenceStore();
		final Map<?, ?> paths = EnvironmentPathUtils.decodePaths(store
				.getString(key));
		return (String) paths.get(environment);
	}

	/**
	 * @param key
	 * @param path
	 */
	private void setEnvironmentPath(String key, String path) {
		final IPreferenceStore store = getPreferenceStore();
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
