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
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclCheckerMigration {

	public static void setPaths(IPreferenceStore store,
			Map<IEnvironment, String> paths) {
		setEnvironmentValues(store, paths, PREF_PATH);
	}

	public static Map<IEnvironment, String> getPaths(IPreferenceStore store) {
		return getEnvironmentValues(store, PREF_PATH);
	}

	public static Map<IEnvironment, String> getNoPCX(IPreferenceStore store) {
		return getEnvironmentValues(store, PREF_NO_PCX);
	}

	public static void setNoPCX(IPreferenceStore store,
			Map<IEnvironment, String> paths) {
		setEnvironmentValues(store, paths, PREF_NO_PCX);
	}

	private static final String ENV_PREFIX_SEPARATOR = "."; //$NON-NLS-1$

	private static Map<IEnvironment, String> getEnvironmentValues(
			IPreferenceStore store, String prefix) {
		Map<IEnvironment, String> results = new HashMap<IEnvironment, String>();
		IEnvironment[] environments = EnvironmentManager.getEnvironments();
		for (int i = 0; i < environments.length; i++) {
			final IEnvironment environment = environments[i];
			results.put(environment, store.getString(prefix
					+ ENV_PREFIX_SEPARATOR + environment.getId()));
		}
		return results;
	}

	private static void setEnvironmentValues(IPreferenceStore store,
			Map<IEnvironment, String> paths, String prefix) {
		for (Map.Entry<IEnvironment, String> entry : paths.entrySet()) {
			store.setValue(prefix + ENV_PREFIX_SEPARATOR
					+ entry.getKey().getId(), entry.getValue());
		}
	}

	public static void setPcxPaths(IPreferenceStore store,
			Map<IEnvironment, List<String>> paths) {
		for (Map.Entry<IEnvironment, List<String>> entry : paths.entrySet()) {
			IEnvironment environment = entry.getKey();
			setPcxPathsTo(store, PREF_PCX_PATH + ENV_PREFIX_SEPARATOR
					+ environment.getId(), entry.getValue());
		}
	}

	private static List<String> getPcxPathsFrom(IPreferenceStore store,
			String key) {
		final List<String> values = new ArrayList<String>();
		final String[] parts = TextUtils.split(store.getString(key),
				File.pathSeparatorChar);
		for (int i = 0; i < parts.length; ++i) {
			final String part = parts[i];
			if (part.length() != 0) {
				values.add(part);
			}
		}
		return values;
	}

	private static void setPcxPathsTo(IPreferenceStore store, String key,
			List<String> paths) {
		final StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (String path : paths) {
			if (!first) {
				buffer.append(File.pathSeparator);
			} else {
				first = false;
			}
			buffer.append(path);
		}
		store.setValue(key, buffer.toString());
	}

	public static Map<IEnvironment, List<String>> getPcxPaths(
			IPreferenceStore store) {
		Map<IEnvironment, List<String>> results = new HashMap<IEnvironment, List<String>>();
		IEnvironment[] environments = EnvironmentManager.getEnvironments();
		for (int i = 0; i < environments.length; i++) {
			results.put(environments[i], getPcxPathsFrom(store, PREF_PCX_PATH
					+ ENV_PREFIX_SEPARATOR + environments[i].getId()));
		}
		return results;
	}

	@Deprecated
	public static final String PREF_PATH = "tclchecker.path"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_MODE = "tclchecker.mode"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_SUMMARY = "tclchecker.summary"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_USE_TCL_VER = "tclchecker.use_tcl_ver"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_PCX_PATH = "tclchecker.pcx.path"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_NO_PCX = "tclchecker.no_pcx"; //$NON-NLS-1$
	@Deprecated
	public static final String PREF_VERSION = "tclchecker.version"; //$NON-NLS-1$
	@Deprecated
	public static final String CLI_OPTIONS = "tclchecker.cli.options"; //$NON-NLS-1$
	@Deprecated
	public static final String VERSION_4 = "4"; //$NON-NLS-1$
	@Deprecated
	public static final String VERSION_5 = "5"; //$NON-NLS-1$
	@Deprecated
	public static final int MODE_NONE = -1;
	@Deprecated
	public static final int MODE_ERRORS = 0;
	@Deprecated
	public static final int MODE_ERRORS_AND_USAGE_WARNINGS = 1;
	@Deprecated
	public static final int MODE_ERRORS_AND_WARNINGS_EXCEPT_UPGRADE = 2;
	@Deprecated
	public static final int MODE_ALL = 3;
	@Deprecated
	public static final int MODE_DEFAULT = MODE_ERRORS_AND_WARNINGS_EXCEPT_UPGRADE;
	@Deprecated
	public static final int PROCESS_TYPE_DEFAULT = 0;
	@Deprecated
	public static final int PROCESS_TYPE_SUPPRESS = 1;
	@Deprecated
	public static final int PROCESS_TYPE_CHECK = 2;

}
