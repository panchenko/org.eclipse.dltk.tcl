/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.formatter.ScriptFormatterManager;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.EditorConfigurationBlock;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class TclEditorPreferencesPage extends
		AbstractConfigurationBlockPreferencePage {

	@Override
	protected String getHelpId() {
		return Util.EMPTY_STRING;
	}

	@Override
	protected void setDescription() {
		String description = org.eclipse.dltk.tcl.internal.ui.preferences.TclPreferencesMessages.TCLEditorPreferencePage_general;
		setDescription(description);
	}

	@Override
	protected Label createDescriptionLabel(Composite parent) {
		return null;
	}

	@Override
	protected void setPreferenceStore() {
		setPreferenceStore(TclUI.getDefault().getPreferenceStore());
	}

	@Override
	protected IPreferenceConfigurationBlock createConfigurationBlock(
			OverlayPreferenceStore overlayPreferenceStore) {
		int flags = EditorConfigurationBlock.FLAG_EDITOR_SMART_NAVIGATION;
		if (!ScriptFormatterManager.hasFormatterFor(TclNature.NATURE_ID)) {
			flags |= EditorConfigurationBlock.FLAG_TAB_POLICY;
		}
		return new EditorConfigurationBlock(this, overlayPreferenceStore, flags);
	}
}
