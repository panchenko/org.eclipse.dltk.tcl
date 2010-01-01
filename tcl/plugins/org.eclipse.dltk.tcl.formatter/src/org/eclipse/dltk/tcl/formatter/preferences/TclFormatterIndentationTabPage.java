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
import org.eclipse.dltk.ui.formatter.FormatterIndentationGroup;
import org.eclipse.dltk.ui.formatter.FormatterModifyTabPage;
import org.eclipse.dltk.ui.formatter.IFormatterControlManager;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialog;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class TclFormatterIndentationTabPage extends FormatterModifyTabPage {

	public TclFormatterIndentationTabPage(IFormatterModifyDialog dialog) {
		super(dialog);
	}

	/**
	 * @see FormatterModifyTabPage#createOptions(IFormatterControlManager,Composite)
	 */
	@Override
	protected void createOptions(IFormatterControlManager manager,
			Composite parent) {
		new FormatterIndentationGroup(manager, parent);
		//
		Group indentBlocks = SWTFactory.createGroup(parent,
				"Indent within blocks", 1, 1, GridData.FILL_HORIZONTAL);
		manager.createCheckbox(indentBlocks,
				TclFormatterConstants.INDENT_SCRIPT,
				"Statements within script body");
		manager.createCheckbox(indentBlocks,
				TclFormatterConstants.INDENT_AFTER_BACKSLASH,
				"Statements after back slash");
	}

	@Override
	protected URL getPreviewContent() {
		return getClass().getResource("indentation-preview.tcl"); //$NON-NLS-1$
	}

}
