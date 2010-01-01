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
package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.dltk.tcl.core.TclConstants;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.text.SimpleTclSourceViewerConfiguration;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.texteditor.ITextEditor;

public class TclFormatterPreferencePage extends AbstractFormatterPreferencePage {

	public static final String PREFERENCE_PAGE_ID = "org.eclipse.dltk.tcl.preferences.formatter"; //$NON-NLS-1$

	private static final PreferenceKey FORMATTER = new PreferenceKey(
			TclUI.PLUGIN_ID, TclPreferenceConstants.FORMATTER_ID);

	@Override
	protected ScriptSourceViewerConfiguration createSimpleSourceViewerConfiguration(
			IColorManager colorManager, IPreferenceStore preferenceStore,
			ITextEditor editor, boolean configureFormatter) {
		return new SimpleTclSourceViewerConfiguration(colorManager,
				preferenceStore, editor, TclConstants.TCL_PARTITIONING,
				configureFormatter);
	}

	/**
	 * @see org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage#getDialogSettings()
	 */
	@Override
	protected IDialogSettings getDialogSettings() {
		return TclUI.getDefault().getDialogSettings();
	}

	/**
	 * @see org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage#getFormatterPreferenceKey()
	 */
	@Override
	protected PreferenceKey getFormatterPreferenceKey() {
		return FORMATTER;
	}

	/**
	 * @see org.eclipse.dltk.ui.formatter.AbstractFormatterPreferencePage#getNatureId()
	 */
	@Override
	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	/**
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#setPreferenceStore()
	 */
	@Override
	protected void setPreferenceStore() {
		setPreferenceStore(TclUI.getDefault().getPreferenceStore());
	}

	@Override
	protected String getPreferencePageId() {
		return PREFERENCE_PAGE_ID;
	}

	@Override
	protected String getPropertyPageId() {
		return "org.eclipse.dltk.tcl.propertyPage.formatter"; //$NON-NLS-1$
	}

}
