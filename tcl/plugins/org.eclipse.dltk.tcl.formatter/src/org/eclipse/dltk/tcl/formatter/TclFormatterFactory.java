/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Sergey Kanshin)
 *******************************************************************************/
package org.eclipse.dltk.tcl.formatter;

import java.net.URL;
import java.util.Map;

import org.eclipse.dltk.formatter.AbstractScriptFormatterFactory;
import org.eclipse.dltk.tcl.formatter.internal.TclFormatterPlugin;
import org.eclipse.dltk.tcl.formatter.preferences.TclFormatterModifyDialog;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialogOwner;
import org.eclipse.dltk.ui.formatter.IScriptFormatter;
import org.eclipse.dltk.ui.preferences.PreferenceKey;

public class TclFormatterFactory extends AbstractScriptFormatterFactory {

	private static final String[] KEYS = {
			TclFormatterConstants.FORMATTER_TAB_CHAR,
			TclFormatterConstants.FORMATTER_INDENTATION_SIZE,
			TclFormatterConstants.FORMATTER_TAB_SIZE,
			TclFormatterConstants.INDENT_SCRIPT,
			TclFormatterConstants.INDENT_AFTER_BACKSLASH,
			TclFormatterConstants.LINES_PRESERVE,
			TclFormatterConstants.LINES_FILE_AFTER_PACKAGE,
			TclFormatterConstants.LINES_FILE_BETWEEN_PROC,
			TclFormatterConstants.WRAP_COMMENTS,
			TclFormatterConstants.WRAP_COMMENTS_LENGTH };

	@SuppressWarnings("unchecked")
	public IScriptFormatter createFormatter(String lineDelimiter,
			Map preferences) {
		return new TclFormatter(lineDelimiter, preferences);
	}

	public PreferenceKey[] getPreferenceKeys() {
		final PreferenceKey[] result = new PreferenceKey[KEYS.length];
		for (int i = 0; i < KEYS.length; ++i) {
			final String key = KEYS[i];
			final String qualifier;
			if (TclFormatterConstants.FORMATTER_TAB_CHAR.equals(key)
					|| TclFormatterConstants.FORMATTER_INDENTATION_SIZE
							.equals(key)
					|| TclFormatterConstants.FORMATTER_TAB_SIZE.equals(key)) {
				qualifier = TclUI.PLUGIN_ID;
			} else {
				qualifier = TclFormatterPlugin.PLUGIN_ID;
			}
			result[i] = new PreferenceKey(qualifier, key);
		}
		return result;
	}

	public IFormatterModifyDialog createDialog(
			IFormatterModifyDialogOwner dialogOwner) {
		return new TclFormatterModifyDialog(dialogOwner, this);
	}

	@Override
	public URL getPreviewContent() {
		return getClass().getResource("preferences/formatterPreview.txt"); //$NON-NLS-1$
	}

	public PreferenceKey getActiveProfileKey() {
		return new PreferenceKey(TclFormatterPlugin.PLUGIN_ID,
				TclFormatterConstants.FORMATTER_PROFILES);
	}

	@Override
	public PreferenceKey getProfilesKey() {
		return new PreferenceKey(TclFormatterPlugin.PLUGIN_ID,
				TclFormatterConstants.FORMATTER_ACTIVE_PROFILE);
	}

}
