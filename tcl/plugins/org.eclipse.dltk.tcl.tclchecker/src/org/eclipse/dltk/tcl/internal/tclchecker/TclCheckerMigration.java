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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.SystemTclCheckerPreferences;
import org.eclipse.dltk.tcl.tclchecker.ITclCheckerPreferences;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerMode;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;
import org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage;
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

	private static final int VERSION_EMF = 1;

	public static void migratePreferences() {
		final IPreferenceStore store = TclCheckerPlugin.getDefault()
				.getPreferenceStore();
		if (store.getInt(TclCheckerConstants.PREF_VERSION) == VERSION_EMF) {
			return;
		}
		if (!DLTKCore.DEBUG) {
			final String newConfiguration = store
					.getString(TclCheckerConstants.PREF_CONFIGURATION);
			if (newConfiguration != null && newConfiguration.length() != 0) {
				return;
			}
		}
		final Set<String> removeKeys = new HashSet<String>();
		try {
			final ITclCheckerPreferences preferences = new SystemTclCheckerPreferences() {
				@Override
				protected String readConfiguration() {
					return Util.EMPTY_STRING;
				}
			};
			migrate(store, preferences, removeKeys);
			preferences.save();
		} catch (Exception e) {
			TclCheckerPlugin.error("TclChecker preferences upgrade problem", e); //$NON-NLS-1$
		}
		store.setValue(TclCheckerConstants.PREF_VERSION, VERSION_EMF);
		for (String key : removeKeys) {
			store.setToDefault(key);
		}
	}

	private static void migrate(IPreferenceStore store,
			ITclCheckerPreferences preferences, final Set<String> removeKeys) {
		final String cliOptions = store.getString(CLI_OPTIONS);
		removeKeys.add(CLI_OPTIONS);
		final String version = store.getString(PREF_VERSION);
		removeKeys.add(PREF_VERSION);
		final Set<String> environmentIds = new HashSet<String>();
		for (Map.Entry<IEnvironment, String> entry : getPaths(store).entrySet()) {
			final String envId = entry.getKey().getId();
			if (entry.getValue() != null && entry.getValue().length() != 0) {
				environmentIds.add(envId);
				final CheckerInstance instance = preferences
						.getEnvironment(envId);
				instance.setExecutablePath(entry.getValue());
				instance.setAutomatic(entry.getKey().isLocal());
				if (cliOptions != null && cliOptions.length() != 0) {
					instance.setCommandLineOptions(cliOptions);
				}
				if (VERSION_5.equals(version)) {
					instance.setVersion(CheckerVersion.VERSION5);
				} else {
					instance.setVersion(CheckerVersion.VERSION4);
				}
			}
			removeKeys.add(PREF_PATH + ENV_PREFIX_SEPARATOR + envId);
		}
		for (Map.Entry<IEnvironment, List<String>> entry : getPcxPaths(store)
				.entrySet()) {
			final String envId = entry.getKey().getId();
			if (environmentIds.contains(envId) && !entry.getValue().isEmpty()) {
				preferences.getEnvironment(envId).getPcxFileFolders().addAll(
						entry.getValue());
			}
			removeKeys.add(PREF_PCX_PATH + ENV_PREFIX_SEPARATOR + envId);
		}
		for (Map.Entry<IEnvironment, String> entry : getNoPCX(store).entrySet()) {
			final String envId = entry.getKey().getId();
			if (environmentIds.contains(envId)) {
				preferences.getEnvironment(envId).setUsePcxFiles(
						!Boolean.valueOf(entry.getValue()).booleanValue());
			}
			removeKeys.add(PREF_NO_PCX + ENV_PREFIX_SEPARATOR + envId);
		}
		final CheckerConfig config = preferences.newConfiguration();
		config.setName("Workspace configuration"); //$NON-NLS-1$
		switch (store.getInt(PREF_MODE)) {
		case MODE_NONE:
			config.setMode(CheckerMode.W0);
			break;
		case MODE_ERRORS:
			config.setMode(CheckerMode.W1);
			break;
		case MODE_ERRORS_AND_USAGE_WARNINGS:
			config.setMode(CheckerMode.W2);
			break;
		case MODE_ERRORS_AND_WARNINGS_EXCEPT_UPGRADE:
			config.setMode(CheckerMode.W3);
			break;
		case MODE_ALL:
			config.setMode(CheckerMode.W4);
			break;
		}
		removeKeys.add(PREF_MODE);
		config.setSummary(store.getBoolean(PREF_SUMMARY));
		removeKeys.add(PREF_SUMMARY);
		config.setUseTclVer(store.getBoolean(PREF_USE_TCL_VER));
		removeKeys.add(PREF_USE_TCL_VER);

		final Map<String, Integer> oldDefaults = new HashMap<String, Integer>();
		oldDefaults.put("warnUndefinedUpvar", PROCESS_TYPE_CHECK); //$NON-NLS-1$
		oldDefaults.put("warnUndefinedVar", PROCESS_TYPE_CHECK); //$NON-NLS-1$
		oldDefaults.put("warnUndefFunc", PROCESS_TYPE_CHECK); //$NON-NLS-1$
		oldDefaults.put("warnUndefProc", PROCESS_TYPE_CHECK); //$NON-NLS-1$
		for (String messageId : TclCheckerProblemDescription
				.getProblemIdentifiers()) {
			int action = store.getInt(messageId);
			if (action == PROCESS_TYPE_DEFAULT && !store.isDefault(messageId)
					&& oldDefaults.containsKey(messageId)) {
				action = oldDefaults.get(messageId);
			}
			if (action == PROCESS_TYPE_CHECK) {
				config.getMessageStates().put(messageId, MessageState.CHECK);
			} else if (action == PROCESS_TYPE_SUPPRESS) {
				config.getMessageStates().put(messageId, MessageState.SUPPRESS);
			}
			removeKeys.add(messageId);
		}
		for (String messageId : TclCheckerProblemDescription
				.getAltProblemIdentifiers()) {
			int action = store.getInt(messageId);
			if (action == PROCESS_TYPE_DEFAULT && !store.isDefault(messageId)
					&& oldDefaults.containsKey(messageId)) {
				action = oldDefaults.get(messageId);
			}
			final CheckerMessage message = TclCheckerProblemDescription
					.getProblem(messageId, false);
			Assert.isNotNull(message);
			if (action == PROCESS_TYPE_CHECK) {
				config.getMessageStates().put(message.getMessageId(),
						MessageState.CHECK);
			} else if (action == PROCESS_TYPE_SUPPRESS) {
				config.getMessageStates().put(message.getMessageId(),
						MessageState.SUPPRESS);
			}
			removeKeys.add(messageId);
		}
		config.setIndividualMessageStates(!config.getMessageStates().isEmpty());
		for (String envId : environmentIds) {
			final CheckerInstance instance = preferences.getEnvironment(envId);
			instance.setConfiguration(config);
		}
	}
}
