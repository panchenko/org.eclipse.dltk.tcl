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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class TclCheckerInstanceDialog extends StatusDialog {

	private final CheckerInstance instance;
	private final EnvironmentContainer environments;
	private final DataBindingContext bindingContext;

	/**
	 * @param parent
	 * @param instance
	 */
	public TclCheckerInstanceDialog(Shell parent,
			EnvironmentContainer environments, CheckerInstance instance) {
		super(parent);
		this.environments = environments;
		this.instance = instance;
		this.bindingContext = new DataBindingContext();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite dialogArea = (Composite) super.createDialogArea(parent);
		final Composite content = new Composite(dialogArea, SWT.NONE);
		final GridLayout contentLayout = new GridLayout(2, false);
		content.setLayout(contentLayout);
		createEnvironment(content);
		createPath(content);
		createVersion(content);

		return dialogArea;
	}

	private void createEnvironment(Composite parent) {
		SWTFactory.createLabel(parent, "Environment", 1);
		SWTFactory.createText(parent, SWT.BORDER | SWT.READ_ONLY, 1,
				Util.EMPTY_STRING).setText(
				environments.getName(instance.getEnvironmentId()));
	}

	private void createPath(Composite parent) {

	}

	/**
	 * @param content
	 */
	private void createVersion(Composite parent) {
		SWTFactory.createLabel(parent, "Version", 1);
		Combo version = SWTFactory.createCombo(parent, SWT.BORDER
				| SWT.READ_ONLY, 1, new String[] {
				CheckerVersion.VERSION4.getLiteral(),
				CheckerVersion.VERSION5.getLiteral() });
	}

}
