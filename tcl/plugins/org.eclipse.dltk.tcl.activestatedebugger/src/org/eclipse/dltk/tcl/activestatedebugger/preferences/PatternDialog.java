/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class PatternDialog extends StatusDialog {

	private StringDialogField fPattern;
	private final String initialValue;

	public PatternDialog(Shell shell, String initialValue) {
		super(shell);
		this.initialValue = initialValue;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	protected void createDialogFields() {
		fPattern = new StringDialogField();
		fPattern.setLabelText(PreferenceMessages.PatternDialog_patternLabel);
	}

	protected void createFieldListeners() {
		fPattern.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				updateStatus(validate());
			}
		});
	}

	protected Control createDialogArea(Composite ancestor) {
		createDialogFields();
		Composite parent = (Composite) super.createDialogArea(ancestor);
		((GridLayout) parent.getLayout()).numColumns = 3;

		fPattern.doFillIntoGrid(parent, 3);
		final GridData pathGridData = (GridData) fPattern.getTextControl(null)
				.getLayoutData();
		pathGridData.grabExcessHorizontalSpace = true;
		pathGridData.widthHint = new PixelConverter(ancestor)
				.convertWidthInCharsToPixels(64);

		initializeFields();
		createFieldListeners();
		applyDialogFont(parent);
		return parent;
	}

	public void create() {
		super.create();
		fPattern.setFocus();
	}

	private void initializeFields() {
		fPattern.setText(initialValue);
		updateStatus(validate());
	}

	private IStatus validate() {
		String pattern = fPattern.getText().trim();
		StatusInfo info = new StatusInfo();
		if (pattern.length() == 0) {
			info.setError(PreferenceMessages.PatternDialog_ErrorPatternIsEmpty);
		}
		return info;
	}

	public String getPattern() {
		return fPattern.getText().trim();
	}
}
