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
package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.TclUILanguageToolkit;
import org.eclipse.dltk.ui.preferences.CodeTemplatesPreferencePage;

public class TclCodeTemplatesPreferencePage extends CodeTemplatesPreferencePage {

	public static final String PREF_ID = "org.eclipse.dltk.tcl.preferencePage.CodeTemplatePage"; //$NON-NLS-1$
	public static final String PROP_ID = "org.eclipse.dltk.tcl.propertyPage.CodeTemplatePage"; //$NON-NLS-1$

	public TclCodeTemplatesPreferencePage() {
		super(TclUILanguageToolkit.getInstance(), TclUI.getDefault()
				.getCodeTemplateAccess());
		setPreferenceStore(TclUI.getDefault().getPreferenceStore());
	}

	/*
	 * @see PropertyAndPreferencePage#getPreferencePageId()
	 */
	protected String getPreferencePageId() {
		return PREF_ID;
	}

	/*
	 * @see PropertyAndPreferencePage#getPropertyPageId()
	 */
	protected String getPropertyPageId() {
		return PROP_ID;
	}

}
