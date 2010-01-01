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

package org.eclipse.dltk.tcl.formatter.preferences;

import java.net.URL;

import org.eclipse.dltk.tcl.formatter.TclFormatterConstants;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 *
 */
public class TclFormatterCommentsPage extends FormatterModifyTabPage {

	public TclFormatterCommentsPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}
	
	/**
	 * @see org.eclipse.dltk.ui.formatter.FormatterModifyTabPage#createOptions(org.eclipse.dltk.ui.formatter.IFormatterControlManager, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		Group commentWrappingGroup = SWTFactory.createGroup(parent,
				"Comment formatting", 2, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(commentWrappingGroup,
				TclFormatterConstants.WRAP_COMMENTS,
				"Enable comment wrapping", 2);
		manager.createNumber(commentWrappingGroup,
				TclFormatterConstants.WRAP_COMMENTS_LENGTH,
				"Maximum line width for comments");
	}

	@Override
	protected URL getPreviewContent() {
		return getClass().getResource("wrapping-preview.tcl");
	}

}
