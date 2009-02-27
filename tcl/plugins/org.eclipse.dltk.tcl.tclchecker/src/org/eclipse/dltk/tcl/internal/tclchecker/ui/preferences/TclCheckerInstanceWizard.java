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

import org.eclipse.dltk.validators.configs.ValidatorInstance;
import org.eclipse.jface.wizard.Wizard;

public class TclCheckerInstanceWizard extends Wizard {

	/**
	 * @param environments
	 */
	public TclCheckerInstanceWizard(IValidatorDialogContext context,
			ValidatorInstance instance) {
		this.context = context;
		this.instance = instance;
		setWindowTitle("Add Validator");
	}

	private final IValidatorDialogContext context;
	private ValidatorWizardPage validatorPage;
	private final ValidatorInstance instance;

	@Override
	public void addPages() {
		// addPage(new ValidatorSelectTypePage(ValidatorSelectTypePage.class
		// .getName()));
		validatorPage = new ValidatorWizardPage(context,
				new TclCheckerInstanceBlock(), instance);
		addPage(validatorPage);
	}

	@Override
	public boolean performFinish() {
		return validatorPage.performFinish();
	}

	@Override
	public boolean canFinish() {
		// TODO call validation
		return getContainer().getCurrentPage() instanceof ValidatorWizardPage;
	}

}
