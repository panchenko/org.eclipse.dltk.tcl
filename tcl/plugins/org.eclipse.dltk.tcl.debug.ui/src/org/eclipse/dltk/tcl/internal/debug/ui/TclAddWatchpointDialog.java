/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TclAddWatchpointDialog extends StatusDialog {

	/**
	 * @param parentShell
	 */
	protected TclAddWatchpointDialog(Shell parentShell) {
		super(parentShell);
		setTitle(Messages.TclAddWatchpointDialog_title);
	}

	private String expression = Util.EMPTY_STRING;
	private Text expressionField;

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		Composite inner = SWTFactory.createComposite(composite, composite
				.getFont(), 2, 1, GridData.FILL_BOTH);
		SWTFactory.createLabel(inner,
				Messages.TclAddWatchpointDialog_expressionLabel, 1);
		expressionField = SWTFactory.createText(inner, SWT.BORDER, 1,
				expression);
		((GridData) expressionField.getLayoutData()).widthHint = convertWidthInCharsToPixels(20);
		return composite;
	}

	protected void okPressed() {
		final String s = expressionField.getText().trim();
		if (s.length() == 0) {
			MessageBox msg = new MessageBox(getShell(), SWT.ICON_WARNING);
			msg.setText(getShell().getText());
			msg.setMessage(Messages.TclAddWatchpointDialog_watchpointError);
			msg.open();
			return;
		}
		expression = s;
		super.okPressed();
	}

	public void setExpression(String value) {
		this.expression = value;
	}

	public String getExpression() {
		return expression;
	}

}
