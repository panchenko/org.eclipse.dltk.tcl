/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class PathDialog extends StatusDialog {

	private StringDialogField fPath;
	private IEnvironment environment;

	public PathDialog(Shell shell, IEnvironment environment) {
		super(shell);
		this.environment = environment;
		setShellStyle(getShellStyle() | SWT.RESIZE);
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
		fPath.setLabelText(Messages.PathDialog_PathLabel);
	}

	protected void createFieldListeners() {
		fPath.setDialogFieldListener(new IDialogFieldListener() {
			public void dialogFieldChanged(DialogField field) {
				updateStatus(validate());
			}
		});
	}

	protected Control createDialogArea(Composite ancestor) {
		createDialogFields();
		Composite parent = (Composite) super.createDialogArea(ancestor);
		((GridLayout) parent.getLayout()).numColumns = 3;

		fPath.doFillIntoGrid(parent, 3);
		final GridData pathGridData = (GridData) fPath.getTextControl(null)
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
		fPath.setFocus();
	}

	private void initializeFields() {
		fPath.setText(Util.EMPTY_STRING);
		updateStatus(validate());
	}

	private IStatus validate() {
		IPath path = new Path(fPath.getText());
		StatusInfo info = new StatusInfo();
		if (path.isEmpty()) {
			info.setError(Messages.PathDialog_error_EmptyPath);
		} else if (this.environment != null) {
			IFileHandle file = PlatformFileUtils
					.findAbsoluteOrEclipseRelativeFile(this.environment, path);
			if (!file.exists()) {
				info
						.setError(Messages.PathDialog_error_FileNotExists);
			} else if (!file.isDirectory()) {
				info
						.setError(Messages.PathDialog_error_NotDirectory);
			}
		}
		return info;
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
	 * Returns the name of the section that this dialog stores its settings in
	 * 
	 * @return String
	 */
	protected String getDialogSettingsSectionName() {
		// FIXME not used
		return "ADD_PATH_TCLCHECKER_DIALOG"; //$NON-NLS-1$
	}

	public String getPath() {
		return fPath.getText();
	}
}
