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
public class TclFormatterBlankLinesPage extends FormatterModifyTabPage {

	public TclFormatterBlankLinesPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}
	
	/**
	 * @see org.eclipse.dltk.ui.formatter.FormatterModifyTabPage#createOptions(org.eclipse.dltk.ui.formatter.IFormatterControlManager, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		Group emptyLinesGroup = SWTFactory.createGroup(parent,
				"Blank lines in source file", 2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(emptyLinesGroup,
				TclFormatterConstants.LINES_FILE_AFTER_PACKAGE,
				"After package directives");
		manager.createNumber(emptyLinesGroup,
				TclFormatterConstants.LINES_FILE_BETWEEN_PROC,
				"Between procedures");
		//
		Group preserveGroup = SWTFactory.createGroup(parent,
				"Existing blank lines", 2, 1, GridData.FILL_HORIZONTAL);
		manager.createNumber(preserveGroup,
				TclFormatterConstants.LINES_PRESERVE,
				"Number of empty lines to preserve");
	}

	@Override
	protected URL getPreviewContent() {
		return getClass().getResource("blank-lines-preview.tcl");
	}

}
