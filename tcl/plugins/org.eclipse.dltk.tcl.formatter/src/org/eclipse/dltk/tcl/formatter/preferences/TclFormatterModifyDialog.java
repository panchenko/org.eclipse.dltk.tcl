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

import org.eclipse.dltk.ui.formatter.FormatterModifyDialog;
import org.eclipse.dltk.ui.formatter.IFormatterModifyDialogOwner;
import org.eclipse.dltk.ui.formatter.IScriptFormatterFactory;

public class TclFormatterModifyDialog extends FormatterModifyDialog {

	public TclFormatterModifyDialog(IFormatterModifyDialogOwner dialogOwner,
			IScriptFormatterFactory formatterFactory) {
		super(dialogOwner, formatterFactory);
		setTitle("Tcl Formatter");
	}
	
	@Override
	protected void addPages() {
		addTabPage("Indentation", new TclFormatterIndentationTabPage(this));
		addTabPage("Blank Lines", new TclFormatterBlankLinesPage(this));
		addTabPage("Comments", new TclFormatterCommentsPage(this));
	}
}
