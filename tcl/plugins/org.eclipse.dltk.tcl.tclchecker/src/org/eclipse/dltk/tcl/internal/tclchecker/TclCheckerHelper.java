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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.jface.preference.IPreferenceStore;

public final class TclCheckerHelper {
	private static final String REGEX = "((?:\\w:)?[^:]+):(\\d+)\\s+\\((\\w+)\\)\\s+(.*)";

	// private static final String QUIET_OPTION = "-quiet";

	private static final String W1_OPTION = "-W1";

	private static final String W2_OPTION = "-W2";

	private static final String W3_OPTION = "-W3";

	private static final Object PCX_OPTION = "-pcx";
	private static final Object NO_PCX_OPTION = "-nopcx";

	private static final String SUPPRESS_OPTION = "-suppress";

	private static final Pattern pattern;

	static {
		pattern = Pattern.compile(REGEX);
	}

	public static String[] makeTclCheckerCmdLine(IPreferenceStore store,
			String path, IEnvironment environment) {
		List cmdLine = new ArrayList();

		passOriginalArguments(store, cmdLine, environment);

		cmdLine.add(path);

		return (String[]) cmdLine.toArray(new String[cmdLine.size()]);
	}

	public static boolean passOriginalArguments(IPreferenceStore store,
			List cmdLine, IEnvironment environment) {
		Map paths = getPaths(store);
		String path = (String) paths.get(environment);
		if (path == null || path.length() == 0) {
			return false;
		}
		IFileHandle validatorFile = PlatformFileUtils
				.findAbsoluteOrEclipseRelativeFile(environment, new Path(path));
		cmdLine.add(validatorFile.toOSString());

		// cmdLine.add(QUIET_OPTION);

		int mode = store.getInt(TclCheckerConstants.PREF_MODE);

		if (mode == TclCheckerConstants.MODE_ERRORS) {
			cmdLine.add(W1_OPTION);
		} else if (mode == TclCheckerConstants.MODE_ERRORS_AND_USAGE_WARNINGS) {
			cmdLine.add(W2_OPTION);
		} else if (mode == TclCheckerConstants.MODE_ALL) {
			cmdLine.add(W3_OPTION);
		}

		// Suppress
		List problems = TclCheckerProblemDescription.getProblemIdentifiers();
		Iterator it = problems.iterator();
		while (it.hasNext()) {
			String warningName = (String) it.next();
			if (store.getBoolean(warningName)) {
				cmdLine.add(SUPPRESS_OPTION);
				cmdLine.add(warningName);
			}
		}

		boolean noPcx = store.getBoolean(TclCheckerConstants.PREF_NO_PCX);
		if (noPcx) {
			cmdLine.add(NO_PCX_OPTION);
		} else {
			// pcx paths
			Map pcxPaths = getPcxPaths(store);
			if (pcxPaths.containsKey(environment)) {
				List pcxPath = (List) pcxPaths.get(environment);
				for (Iterator iterator = pcxPath.iterator(); iterator.hasNext();) {
					String pcx = (String) iterator.next();
					cmdLine.add(PCX_OPTION);
					cmdLine.add(pcx);
				}
			}
		}
		return true;
	}

	public static TclCheckerProblem parseProblem(String problem) {
		Matcher matcher = pattern.matcher(problem);

		if (!matcher.find())
			return null;

		String file = matcher.group(1);
		int lineNumber = Integer.parseInt(matcher.group(2));
		String messageID = matcher.group(3);
		String message = matcher.group(4);

		return new TclCheckerProblem(file, lineNumber, messageID, message);
	}

	public static Map getPcxPaths(IPreferenceStore store) {
		Map results = new HashMap();
		IEnvironment[] environments = EnvironmentManager.getEnvironments();
		for (int i = 0; i < environments.length; i++) {
			results.put(environments[i], getPcxPathsFrom(store,
					TclCheckerConstants.PREF_PCX_PATH + "/"
							+ environments[i].getId()));
		}
		return results;
	}

	private static List getPcxPathsFrom(IPreferenceStore store, String key) {
		String pcxPathsValue = store.getString(key);
		List values = new ArrayList();
		int start = 0;
		for (int i = 0; i < pcxPathsValue.length(); i++) {
			if (pcxPathsValue.charAt(i) == File.pathSeparatorChar && start < i) {
				String path = pcxPathsValue.substring(start, i);
				if (path.length() > 0) {
					values.add(path);
				}
				start = i + 1;
			}
		}
		if (start < pcxPathsValue.length()) {
			String path = pcxPathsValue
					.substring(start, pcxPathsValue.length());
			if (path.length() > 0) {
				values.add(path);
			}
		}
		return values;
	}

	public static void setPcxPaths(IPreferenceStore store, Map paths) {
		for (Iterator iterator = paths.keySet().iterator(); iterator.hasNext();) {
			IEnvironment environment = (IEnvironment) iterator.next();
			setPcxPathsTo(store, TclCheckerConstants.PREF_PCX_PATH + "/"
					+ environment.getId(), (List) paths.get(environment));
		}
	}

	private static void setPcxPathsTo(IPreferenceStore store, String key,
			List paths) {
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (Iterator iterator = paths.iterator(); iterator.hasNext();) {
			String path = (String) iterator.next();
			if (!first) {
				buffer.append(File.pathSeparator);
			} else {
				first = false;
			}
			buffer.append(path);
		}
		store.setValue(key, buffer.toString());
	}

	public static Map getPaths(IPreferenceStore store) {
		String prefix = TclCheckerConstants.PREF_PATH;
		Map results = getEnvironmentValues(store, prefix);
		return results;
	}

	public static void setPaths(IPreferenceStore store, Map paths) {
		String prefix = TclCheckerConstants.PREF_PATH;
		setEnvironmentValues(store, paths, prefix);
	}

	public static boolean canExecuteTclChecker(IPreferenceStore store,
			IEnvironment environment) {
		Map paths = getPaths(store);
		if (paths.containsKey(environment)) {
			String path = (String) paths.get(environment);
			if (path.length() != 0) {
				IFileHandle file = environment.getFile(new Path(path));
				if (file.exists()) {
					return true;
				}
			}
		}
		return false;
	}

	public static Map getNoPCX(IPreferenceStore store) {
		return getEnvironmentValues(store, TclCheckerConstants.PREF_NO_PCX);
	}

	public static void setNoPCX(IPreferenceStore store, Map paths) {
		setEnvironmentValues(store, paths, TclCheckerConstants.PREF_NO_PCX);
	}

	private static Map getEnvironmentValues(IPreferenceStore store,
			String prefix) {
		Map results = new HashMap();
		IEnvironment[] environments = EnvironmentManager.getEnvironments();
		for (int i = 0; i < environments.length; i++) {
			results.put(environments[i], store.getString(prefix + "."
					+ environments[i].getId()));
		}
		return results;
	}

	private static void setEnvironmentValues(IPreferenceStore store, Map paths,
			String prefix) {
		for (Iterator iterator = paths.keySet().iterator(); iterator.hasNext();) {
			IEnvironment environment = (IEnvironment) iterator.next();
			store.setValue(prefix + "." + environment.getId(), (String) paths
					.get(environment));
		}
	}
}
