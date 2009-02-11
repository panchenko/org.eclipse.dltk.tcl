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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class TclCheckerInstanceDialog extends StatusDialog {

	/**
	 * @param parent
	 */
	public TclCheckerInstanceDialog(Shell parent) {
		super(parent);
		// TODO Auto-generated constructor stub
	}

	private Combo version;

	/**
	 * @param content
	 */
	private void initVersion(Composite parent) {
		SWTFactory.createLabel(parent, "Version", 1);
		version = SWTFactory.createCombo(parent, SWT.BORDER | SWT.READ_ONLY, 1,
				new String[] { CheckerVersion.VERSION4.getLiteral(),
						CheckerVersion.VERSION5.getLiteral() });
	}

}
