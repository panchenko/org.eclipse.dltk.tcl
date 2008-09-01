/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.ui.dialogs.StatusInfo;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.dltk.validators.core.IValidatorType;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class PathDialog extends StatusDialog {

	private IValidatorType fSelectedValidatorType;

	private StringDialogField fPath;
	private IEnvironment environment;
	private IStatus[] fStati;
	private int fPrevIndex = -1;

	public PathDialog(Shell shell, IEnvironment environment) {
		super(shell);
		this.environment = environment;
		setShellStyle(getShellStyle() | SWT.RESIZE);
		// fRequestor= requestor;
		fStati = new IStatus[5];
		for (int i = 0; i < fStati.length; i++) {
			fStati[i] = new StatusInfo();
		}
	}

	/**
	 * @see Windows#configureShell
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		// PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell,
		// IScriptDebugHelpContextIds.EDIT_ValidatorEnvironment_DIALOG);
	}

	protected void createDialogFields() {

		fPath = new StringDialogField();
		fPath.setLabelText("Path");
	}

	protected void createFieldListeners() {

		fPath.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				setPatternStatus(validate());
				updateStatusLine();
			}
		});
	}

	protected String getPattern() {
		return fPath.getText();
	}

	private void createLabel(Composite parent, String content, int columns) {
		Label l = new Label(parent, SWT.None);
		l.setText(content);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalSpan = columns;
		l.setLayoutData(gd);
	}

	protected Control createDialogArea(Composite ancestor) {
		createDialogFields();
		Composite parent = (Composite) super.createDialogArea(ancestor);
		((GridLayout) parent.getLayout()).numColumns = 3;

		fPath.doFillIntoGrid(parent, 3);
		((GridData) fPath.getTextControl(null).getLayoutData()).grabExcessHorizontalSpace = true;

		initializeFields();
		createFieldListeners();
		applyDialogFont(parent);
		return parent;
	}

	public void create() {
		super.create();
		fPath.setFocus();
	}

	private void initializeFields() {
		fPath.setText(""); //$NON-NLS-1$
		setPatternStatus(validate());
		updateStatusLine();
	}

	private IStatus validate() {
		String path = fPath.getText();
		StatusInfo info = new StatusInfo();
		if (path.isEmpty()) {
			info.setError("Path is empty");
			return info;
		}
		IFileHandle file = PlatformFileUtils.findAbsoluteOrEclipseRelativeFile(
				this.environment, new Path(path));
		if (!file.exists()) {
			info.setError("File does not exists");
		} else if (!file.isDirectory()) {
			info.setError("File is not a directory");
		}
		return info;
	}

	public void updateStatusLine() {
		IStatus max = null;
		for (int i = 0; i < fStati.length; i++) {
			IStatus curr = fStati[i];
			if (curr.matches(IStatus.ERROR)) {
				updateStatus(curr);
				return;
			}
			if (max == null || curr.getSeverity() > max.getSeverity()) {
				max = curr;
			}
		}
		updateStatus(max);
	}

	protected void okPressed() {
		doOkPressed();
		super.okPressed();
	}

	private void doOkPressed() {
	}

	private void setPatternStatus(IStatus status) {
		fStati[0] = status;
	}

	protected IStatus getSystemLibraryStatus() {
		return fStati[3];
	}

	public void setSystemLibraryStatus(IStatus status) {
		fStati[3] = status;
	}

	/**
	 * Updates the status of the ok button to reflect the given status.
	 * Subclasses may override this method to update additional buttons.
	 * 
	 * @param status
	 *            the status.
	 */
	protected void updateButtonsEnableState(IStatus status) {
		Button ok = getButton(IDialogConstants.OK_ID);
		if (ok != null && !ok.isDisposed())
			ok.setEnabled(status.getSeverity() == IStatus.OK);
	}

	/**
	 * @see org.eclipse.jface.dialogs.Dialog#setButtonLayoutData(org.eclipse.swt.widgets.Button)
	 */
	public void setButtonLayoutData(Button button) {
		super.setButtonLayoutData(button);
	}

	/**
	 * Returns the name of the section that this dialog stores its settings in
	 * 
	 * @return String
	 */
	protected String getDialogSettingsSectionName() {
		return "ADD_PATH_TCLCHECKER_DIALOG"; //$NON-NLS-1$
	}

	public String getPath() {
		return fPath.getText();
	}
}
