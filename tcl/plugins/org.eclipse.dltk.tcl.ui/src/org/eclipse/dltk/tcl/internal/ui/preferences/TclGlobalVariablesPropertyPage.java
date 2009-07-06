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
package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.ui.GlobalVariableBlock;
import org.eclipse.dltk.ui.dialogs.IStatusDialog;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.PropertyPage;

public class TclGlobalVariablesPropertyPage extends PropertyPage {

	private class StatusDialogAdapter implements IStatusDialog {

		public Shell getShell() {
			return TclGlobalVariablesPropertyPage.this.getShell();
		}

		public void setButtonLayoutData(Button button) {
			TclGlobalVariablesPropertyPage.this.setButtonLayoutData(button);
		}

		public void updateStatusLine() {
			// TODO Auto-generated method stub
		}

	}

	private final GlobalVariableBlock variableBlock;

	public TclGlobalVariablesPropertyPage() {
		this.variableBlock = new GlobalVariableBlock(new StatusDialogAdapter());
	}

	@Override
	protected Control createContents(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		variableBlock.createControlsIn(composite);
		final IProject project = (IProject) getElement().getAdapter(
				IProject.class);
		variableBlock.setValues(TclPackagesManager.getVariablesEMap(project
				.getName()));
		return composite;
	}

	@Override
	public boolean performOk() {
		final IProject project = (IProject) getElement().getAdapter(
				IProject.class);
		final EMap<String, VariableValue> oldVars = TclPackagesManager
				.getVariablesEMap(project.getName());
		final EMap<String, VariableValue> newVars = variableBlock.getValues();
		if (!GlobalVariableBlock.equalsEMap(newVars, oldVars)) {
			TclPackagesManager.setVariables(project.getName(), newVars);
			new ProjectBuildJob(project).schedule(500);
		}
		return super.performOk();
	}

}
