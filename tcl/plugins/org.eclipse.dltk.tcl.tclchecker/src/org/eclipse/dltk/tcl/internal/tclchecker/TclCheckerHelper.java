/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.dltk.validators.core.CommandLine;
import org.eclipse.jface.preference.IPreferenceStore;

public final class TclCheckerHelper {

	// private static final String QUIET_OPTION = "-quiet";

	private static final String W1_OPTION = "-W1"; //$NON-NLS-1$

	private static final String W2_OPTION = "-W2"; //$NON-NLS-1$

	private static final String W3_OPTION = "-W3"; //$NON-NLS-1$

	private static final String PCX_OPTION = "-pcx"; //$NON-NLS-1$
	private static final String NO_PCX_OPTION = "-nopcx"; //$NON-NLS-1$

	private static final String SUPPRESS_OPTION = "-suppress"; //$NON-NLS-1$
	private static final String CHECK_OPTION = "-check"; //$NON-NLS-1$

	public static boolean buildCommandLine(IPreferenceStore store,
			CommandLine cmdLine, IEnvironment environment,
			IScriptProject project) {
		Map<IEnvironment, String> paths = getPaths(store);
		String path = paths.get(environment);
		if (path == null || path.length() == 0) {
			return false;
		}
		IFileHandle validatorFile = PlatformFileUtils
				.findAbsoluteOrEclipseRelativeFile(environment, new Path(path));
		cmdLine.add(validatorFile.toOSString());

		// cmdLine.add(QUIET_OPTION);

		if (project != null) {
			try {
				final IInterpreterInstall install = ScriptRuntime
						.getInterpreterInstall(project);
				if (install != null) {
					final String version = PackagesManager.getInstance()
							.getPackageVersion(install, "Tcl"); //$NON-NLS-1$
					if (version != null && version.length() != 0) {
						if (version.startsWith("8.")) { //$NON-NLS-1$
							int pos = version.indexOf('.', 2);
							if (pos < 0) {
								pos = version.length();
							}
							cmdLine.add("-use"); //$NON-NLS-1$
							cmdLine
									.add("\"Tcl" + version.substring(0, pos) + "\""); //$NON-NLS-1$ //$NON-NLS-2$
						}
					}
				}
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}

		int mode = store.getInt(TclCheckerConstants.PREF_MODE);

		if (mode == TclCheckerConstants.MODE_ERRORS) {
			cmdLine.add(W1_OPTION);
		} else if (mode == TclCheckerConstants.MODE_ERRORS_AND_USAGE_WARNINGS) {
			cmdLine.add(W2_OPTION);
		} else if (mode == TclCheckerConstants.MODE_ALL) {
			cmdLine.add(W3_OPTION);
		}

		// Suppress
		List<String> problems = TclCheckerProblemDescription
				.getProblemIdentifiers();
		for (String warningName : problems) {
			int processType = store.getInt(warningName);
			switch (processType) {
			case TclCheckerConstants.PROCESS_TYPE_CHECK:
				cmdLine.add(CHECK_OPTION);
				cmdLine.add(warningName);
				break;
			case TclCheckerConstants.PROCESS_TYPE_SUPPRESS:
				cmdLine.add(SUPPRESS_OPTION);
				cmdLine.add(warningName);
				break;
			}
		}

		boolean noPcx = store.getBoolean(TclCheckerConstants.PREF_NO_PCX);
		if (noPcx) {
			cmdLine.add(NO_PCX_OPTION);
		} else {
			// pcx paths
			Map<IEnvironment, List<String>> pcxPaths = getPcxPaths(store);
			if (pcxPaths.containsKey(environment)) {
				List<String> pcxPath = pcxPaths.get(environment);
				for (final String pcx : pcxPath) {
					IFileHandle handle = PlatformFileUtils
							.findAbsoluteOrEclipseRelativeFile(environment,
									new Path(pcx));
					cmdLine.add(PCX_OPTION);
					if (handle.exists()) {
						cmdLine.add(handle.toOSString());
					} else {
						cmdLine.add(pcx);
					}
				}
			}
		}
		String cliOptions = store.getString(TclCheckerConstants.CLI_OPTIONS);
		if (cliOptions != null && cliOptions.length() != 0) {
			cmdLine.add(new CommandLine(cliOptions));
		}
		if (TclCheckerConstants.VERSION_5.equals(store
				.getString(TclCheckerConstants.PREF_VERSION))) {
			cmdLine.add("-as"); //$NON-NLS-1$
			cmdLine.add("script"); //$NON-NLS-1$
		}
		return true;
	}

	public static Map<IEnvironment, List<String>> getPcxPaths(
			IPreferenceStore store) {
		Map<IEnvironment, List<String>> results = new HashMap<IEnvironment, List<String>>();
		IEnvironment[] environments = EnvironmentManager.getEnvironments();
		for (int i = 0; i < environments.length; i++) {
			results.put(environments[i], getPcxPathsFrom(store,
					TclCheckerConstants.PREF_PCX_PATH + ENV_PREFIX_SEPARATOR
							+ environments[i].getId()));
		}
		return results;
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

	public static void setPcxPaths(IPreferenceStore store,
			Map<IEnvironment, List<String>> paths) {
		for (Map.Entry<IEnvironment, List<String>> entry : paths.entrySet()) {
			IEnvironment environment = entry.getKey();
			setPcxPathsTo(store, TclCheckerConstants.PREF_PCX_PATH
					+ ENV_PREFIX_SEPARATOR + environment.getId(), entry
					.getValue());
		}
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

	public static Map<IEnvironment, String> getPaths(IPreferenceStore store) {
		String prefix = TclCheckerConstants.PREF_PATH;
		Map<IEnvironment, String> results = getEnvironmentValues(store, prefix);
		return results;
	}

	public static void setPaths(IPreferenceStore store,
			Map<IEnvironment, String> paths) {
		String prefix = TclCheckerConstants.PREF_PATH;
		setEnvironmentValues(store, paths, prefix);
	}

	public static boolean canExecuteTclChecker(IPreferenceStore store,
			IEnvironment environment) {
		Map<IEnvironment, String> paths = getPaths(store);
		if (paths.containsKey(environment)) {
			String path = paths.get(environment);
			if (path.length() != 0) {
				IFileHandle file = PlatformFileUtils
						.findAbsoluteOrEclipseRelativeFile(environment,
								new Path(path));
				if (file.exists()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Map<IEnvironment, String> getNoPCX(IPreferenceStore store) {
		return getEnvironmentValues(store, TclCheckerConstants.PREF_NO_PCX);
	}

	public static void setNoPCX(IPreferenceStore store,
			Map<IEnvironment, String> paths) {
		setEnvironmentValues(store, paths, TclCheckerConstants.PREF_NO_PCX);
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
}
