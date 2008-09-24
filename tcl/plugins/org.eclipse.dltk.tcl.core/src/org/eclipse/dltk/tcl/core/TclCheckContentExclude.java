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
package org.eclipse.dltk.tcl.core;

import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Preferences.IPropertyChangeListener;
import org.eclipse.core.runtime.Preferences.PropertyChangeEvent;
import org.eclipse.dltk.compiler.CharOperation;

public class TclCheckContentExclude implements IPropertyChangeListener {

	private char[][] cachedPatterns = null;

	public boolean isExcluded(String name) {
		char[][] patterns;
		synchronized (this) {
			if (cachedPatterns == null) {
				final Preferences prefs = TclPlugin.getDefault()
						.getPluginPreferences();
				initPatterns(prefs
						.getString(TclCorePreferences.CHECK_CONTENT_EXCLUDES));
				prefs.addPropertyChangeListener(this);
			}
			patterns = cachedPatterns;
		}
		if (patterns.length != 0) {
			for (int i = 0; i < patterns.length; ++i) {
				final char[] pattern = patterns[i];
				if (match(pattern, name)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param string
	 */
	private void initPatterns(String value) {
		cachedPatterns = CharOperation.splitOn(
				TclCorePreferences.CHECK_CONTENT_EXCLUDE_SEPARATOR, value
						.toCharArray());
		if (cachedPatterns == null) {
			cachedPatterns = CharOperation.NO_CHAR_CHAR;
		}
	}

	private static final boolean match(char[] pattern, String name) {
		final int patternEnd = pattern.length;
		final int nameEnd = name.length();
		int iPattern = 0; // patternStart;
		int iName = 0; // nameStart

		/* check first segment */
		char patternChar = 0;
		while ((iPattern < patternEnd)
				&& (patternChar = pattern[iPattern]) != '*') {
			if (iName == nameEnd)
				return false;
			if (patternChar != Character.toLowerCase(name.charAt(iName))
					&& patternChar != '?') {
				return false;
			}
			iName++;
			iPattern++;
		}
		/* check sequence of star+segment */
		int segmentStart;
		if (patternChar == '*') {
			segmentStart = ++iPattern; // skip star
		} else {
			segmentStart = 0; // force iName check
		}
		int prefixStart = iName;
		checkSegment: while (iName < nameEnd) {
			if (iPattern == patternEnd) {
				iPattern = segmentStart; // mismatch - restart current
				// segment
				iName = ++prefixStart;
				continue checkSegment;
			}
			/* segment is ending */
			if ((patternChar = pattern[iPattern]) == '*') {
				segmentStart = ++iPattern; // skip start
				if (segmentStart == patternEnd) {
					return true;
				}
				prefixStart = iName;
				continue checkSegment;
			}
			/* check current name character */
			if (Character.toLowerCase(name.charAt(iName)) != patternChar
					&& patternChar != '?') {
				iPattern = segmentStart; // mismatch - restart current
				// segment
				iName = ++prefixStart;
				continue checkSegment;
			}
			iName++;
			iPattern++;
		}

		return (segmentStart == patternEnd)
				|| (iName == nameEnd && iPattern == patternEnd)
				|| (iPattern == patternEnd - 1 && pattern[iPattern] == '*');
	}

	public void propertyChange(PropertyChangeEvent event) {
		if (TclCorePreferences.CHECK_CONTENT_EXCLUDES.equals(event
				.getProperty())) {
			synchronized (this) {
				initPatterns((String) event.getNewValue());
			}
		}
	}
}
