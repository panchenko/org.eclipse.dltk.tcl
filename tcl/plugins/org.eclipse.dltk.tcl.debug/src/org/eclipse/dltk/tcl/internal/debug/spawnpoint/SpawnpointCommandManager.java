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
package org.eclipse.dltk.tcl.internal.debug.spawnpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;

public class SpawnpointCommandManager {

	private static final String EXTENSION_POINT = TclDebugPlugin.PLUGIN_ID
			+ ".spawnpointCommand"; //$NON-NLS-1$

	private SpawnpointCommandManager() {
		// hidden constructor
	}

	private static Set<String> commands = null;

	/**
	 * Returns the names of the commands contributed by the extension points
	 * 
	 * @return
	 */
	public static Set<String> getContributedCommands() {
		if (commands == null) {
			final Set<String> names = new HashSet<String>();
			final IConfigurationElement[] elements = Platform
					.getExtensionRegistry().getConfigurationElementsFor(
							EXTENSION_POINT);
			for (IConfigurationElement element : elements) {
				final String name = element.getAttribute("name"); //$NON-NLS-1$
				if (isValidCommandName(name)) {
					names.add(name);
				}
			}
			commands = Collections.unmodifiableSet(names);
		}
		return commands;
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
		return decode(TclDebugPlugin.getDefault().getPluginPreferences()
				.getString(TclDebugConstants.PREF_SPAWNPOINT_COMMANDS));
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
		final Set<String> contributed = getContributedCommands();
		for (String commandName : contributed) {
			if (!commandNames.contains(commandName)) {
				commandNames.add(commandName);
				selected.add(commandName);
			}
		}
		Collections.sort(commandNames, new Comparator<String>() {
			public int compare(String o1, String o2) {
				boolean contributed1 = contributed.contains(o1);
				boolean contributed2 = contributed.contains(o2);
				if (contributed1 != contributed2) {
					return contributed1 ? -1 : +1;
				}
				return o1.compareToIgnoreCase(o2);
			}
		});
		return new SpawnpointCommands(commandNames, selected);
	}

	public static void saveToPreferences(SpawnpointCommands commandNames) {
		TclDebugPlugin.getDefault().getPluginPreferences().setValue(
				TclDebugConstants.PREF_SPAWNPOINT_COMMANDS,
				encode(commandNames));
	}

	public static String encode(SpawnpointCommands commandNames) {
		final Set<String> contributed = getContributedCommands();
		final StringBuffer buffer = new StringBuffer();
		for (String commandName : commandNames.getCommands()) {
			if (contributed.contains(commandName)
					&& commandNames.isSelected(commandName)) {
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
