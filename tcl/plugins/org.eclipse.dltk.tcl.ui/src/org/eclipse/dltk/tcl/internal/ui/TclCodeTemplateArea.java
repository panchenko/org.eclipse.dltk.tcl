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
package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.dltk.ui.text.templates.ICodeTemplateAccess;
import org.eclipse.dltk.ui.text.templates.ICodeTemplateArea;

public class TclCodeTemplateArea implements ICodeTemplateArea {

	private static final String PREF_ID = "org.eclipse.dltk.tcl.preferences.code.templates"; //$NON-NLS-1$
	private static final String PROP_ID = "org.eclipse.dltk.tcl.propertyPage.CodeTemplatePage"; //$NON-NLS-1$

	/*
	 * @see ICodeTemplateArea#getCodeTemplateAccess()
	 */
	public ICodeTemplateAccess getTemplateAccess() {
		return TclUI.getDefault().getCodeTemplateAccess();
	}

	/*
	 * @see ICodeTemplateArea#getCodeTemplatePreferencePageId()
	 */
	public String getTemplatePreferencePageId() {
		return PREF_ID;
	}

	/*
	 * @see ICodeTemplateArea#getCodeTemplatePropertyPageId()
	 */
	public String getTemplatePropertyPageId() {
		return PROP_ID;
	}

}
