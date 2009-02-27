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

import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ValidatorSelectTypePage extends WizardPage {

	/**
	 * @param pageName
	 */
	protected ValidatorSelectTypePage(String pageName) {
		super(pageName);
		setTitle("Select Validator Type");
		setDescription("Select Validator Type");
		// TODO Auto-generated constructor stub
	}

	/*
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		Composite control = SWTFactory.createComposite(parent,
				parent.getFont(), 2, 1, GridData.FILL_BOTH);

		Label label = SWTFactory.createLabel(control, "Validator Type", 1);
		((GridData) label.getLayoutData()).widthHint = new PixelConverter(
				parent).convertWidthInCharsToPixels(20);
		SWTFactory.createCombo(control, SWT.BORDER | SWT.READ_ONLY, 1,
				new String[] { "TclChecker" });
		setControl(control);
	}

}
