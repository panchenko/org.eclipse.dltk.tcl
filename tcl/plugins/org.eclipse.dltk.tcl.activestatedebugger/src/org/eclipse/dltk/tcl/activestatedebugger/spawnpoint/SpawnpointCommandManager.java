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
package org.eclipse.dltk.tcl.activestatedebugger.spawnpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;

public class SpawnpointCommandManager {

	private static final String EXTENSION_POINT = TclDebugPlugin.PLUGIN_ID
			+ ".spawnpointCommand"; //$NON-NLS-1$

	private SpawnpointCommandManager() {
		// hidden constructor
	}

	private static Map<String, Boolean> commands = null;

	public static Set<String> getContributedCommands() {
		return getContributedCommandMap().keySet();
	}

	/**
	 * Returns the names of the commands contributed by the extension points
	 * 
	 * @return
	 */
	public static Map<String, Boolean> getContributedCommandMap() {
		if (commands == null) {
			final Map<String, Boolean> names = new HashMap<String, Boolean>();
			final IConfigurationElement[] elements = Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							EXTENSION_POINT);
			for (IConfigurationElement element : elements) {
				final String name = element.getAttribute("name"); //$NON-NLS-1$
				if (isValidCommandName(name)) {
					names.put(name, Boolean.valueOf(toBoolean(element
							.getAttribute("enabled"), true))); //$NON-NLS-1$
				}
			}
			commands = Collections.unmodifiableMap(names);
		}
		return commands;
	}

	private static boolean toBoolean(String value, boolean defaultValue) {
		if (value == null) {
			return defaultValue;
		} else {
			return Boolean.parseBoolean(value);
		}
	}

	private static final Pattern COMMAND_PATTERN = Pattern
			.compile("[\\w_]+(::[\\w_]+)*"); //$NON-NLS-1$

	/**
	 * @param name
	 * @return
	 */
	public static boolean isValidCommandName(String name) {
		return name != null && name.length() != 0
				&& COMMAND_PATTERN.matcher(name).matches();
	}

	private static final String COMMAND_SEPARATOR = ";"; //$NON-NLS-1$
	private static final char VALUE_SEPARATOR = '=';
	private static final String VALUE_FALSE = "0"; //$NON-NLS-1$

	public static SpawnpointCommands loadFromPreferences() {
		return decode(getPluginPreferences().getString(
				TclActiveStateDebuggerConstants.PREF_SPAWNPOINT_COMMANDS));
	}

	public static SpawnpointCommands decode(final String value) {
		final List<String> commandNames = new ArrayList<String>();
		final Set<String> selected = new HashSet<String>();
		if (value != null && value.length() != 0) {
			final StringTokenizer st = new StringTokenizer(value,
					COMMAND_SEPARATOR);
			while (st.hasMoreTokens()) {
				final String token = st.nextToken();
				final String commandName;
				final boolean enabled;
				final int pos = token.indexOf(VALUE_SEPARATOR);
				if (pos >= 0) {
					commandName = token.substring(0, pos);
					enabled = !VALUE_FALSE.equals(token.substring(pos + 1));
				} else {
					commandName = token;
					enabled = true;
				}
				if (isValidCommandName(commandName)) {
					commandNames.add(commandName);
					if (enabled) {
						selected.add(commandName);
					}
				}
			}
		}
		final Map<String, Boolean> contributed = getContributedCommandMap();
		for (Map.Entry<String, Boolean> commandName : contributed.entrySet()) {
			if (!commandNames.contains(commandName.getKey())) {
				commandNames.add(commandName.getKey());
				if (commandName.getValue().booleanValue()) {
					selected.add(commandName.getKey());
				}
			}
		}
		Collections.sort(commandNames, new Comparator<String>() {
			public int compare(String o1, String o2) {
				boolean contributed1 = contributed.containsKey(o1);
				boolean contributed2 = contributed.containsKey(o2);
				if (contributed1 != contributed2) {
					return contributed1 ? -1 : +1;
				}
				return o1.compareToIgnoreCase(o2);
			}
		});
		return new SpawnpointCommands(commandNames, selected);
	}

	public static void saveToPreferences(SpawnpointCommands commandNames) {
		getPluginPreferences().setValue(
				TclActiveStateDebuggerConstants.PREF_SPAWNPOINT_COMMANDS,
				encode(commandNames));
	}

	private static Preferences getPluginPreferences() {
		return TclActiveStateDebuggerPlugin.getDefault().getPluginPreferences();
	}

	public static String encode(SpawnpointCommands commandNames) {
		final Map<String, Boolean> contributed = getContributedCommandMap();
		final StringBuffer buffer = new StringBuffer();
		for (String commandName : commandNames.getCommands()) {
			final Boolean contributedEnabled = contributed.get(commandName);
			if (contributedEnabled != null
					&& commandNames.isSelected(commandName) == contributedEnabled
							.booleanValue()) {
				continue;
			}
			if (buffer.length() != 0) {
				buffer.append(COMMAND_SEPARATOR);
			}
			buffer.append(commandName);
			if (!commandNames.isSelected(commandName)) {
				buffer.append(VALUE_SEPARATOR);
				buffer.append(VALUE_FALSE);
			}
		}
		return buffer.toString();
	}
}
