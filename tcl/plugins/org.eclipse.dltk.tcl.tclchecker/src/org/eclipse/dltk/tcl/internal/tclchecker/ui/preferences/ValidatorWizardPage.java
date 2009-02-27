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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class ValidatorWizardPage extends WizardPage implements IShellProvider,
		IValidationHandler {

	private final IValidatorDialogContext context;
	private final IValidatorEditBlock block;

	/**
	 * @param pageName
	 */
	public ValidatorWizardPage(IValidatorDialogContext context,
			IValidatorEditBlock block, Object instance) {
		super(ValidatorWizardPage.class.getName());
		this.context = context;
		this.block = block;
		this.context.setShellProvider(this);
		this.context.setValidationHandler(this);
		this.block.init(context, instance);
		setTitle("Configure TclChecker instance");
		setDescription("Configure TclChecker instance");
		// TODO Auto-generated constructor stub
	}

	public void createControl(Composite parent) {
		final Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		block.createControl(content);
		setControl(content);
	}

	public void validate(Object hint) {
		final IStatus status = this.block.isValid(hint);
		showStatus(status);
	}

	private void showStatus(final IStatus status) {
		if (status != null && status.matches(IStatus.ERROR)) {
			setMessage(null);
			setErrorMessage(status.getMessage());
		} else {
			setErrorMessage(null);
			setMessage(status != null ? status.getMessage() : null);
		}
	}

	private boolean initialized = false;

	@Override
	public void setVisible(boolean visible) {
		if (visible && !initialized) {
			initialized = true;
			block.initControls();
		}
		super.setVisible(visible);
	}

	/**
	 * @return
	 */
	public boolean performFinish() {
		final IStatus status = block.isValid(null);
		if (status == null || !status.matches(IStatus.ERROR)) {
			showStatus(null);
			block.saveValues();
			return true;
		} else {
			showStatus(status);
			return false;
		}
	}

}
