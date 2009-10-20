/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterEnvironmentVariablesBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersMessages;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.VariableMap;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.ui.GlobalVariableBlock;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class AddTclInterpreterDialog extends AddScriptInterpreterDialog {
	public AddTclInterpreterDialog(IAddInterpreterDialogRequestor requestor,
			Shell shell, IInterpreterInstallType[] interpreterInstallTypes,
			IInterpreterInstall editedInterpreter) {
		super(requestor, shell, interpreterInstallTypes, editedInterpreter);
	}

	@Override
	protected AbstractInterpreterLibraryBlock createLibraryBlock(
			AddScriptInterpreterDialog dialog) {
		return new TclInterpreterLibraryBlock(dialog);
	}

	@Override
	protected AbstractInterpreterEnvironmentVariablesBlock createEnvironmentVariablesBlock() {
		return new TclInterpreterEnvironmentVariablesBlock(this);
	}

	private GlobalVariableBlock globals;
	private AvailablePackagesBlock packagesBlock;

	protected Composite createEnvironmentVariablesBlockParent(Composite parent,
			int numColumns) {
		ExpandableBlock block = new ExpandableBlock(parent, 0);
		block
				.setText(InterpretersMessages.AddScriptInterpreterDialog_interpreterEnvironmentVariables);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		block.setLayoutData(gd);
		block.setExpanded(true);
		return block.getContent();
	}

	protected Composite createLibraryBlockParent(Composite parent,
			int numColumns) {
		ExpandableBlock block = new ExpandableBlock(parent, 0);
		block
				.setText(InterpretersMessages.AddInterpreterDialog_Interpreter_system_libraries__1);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		block.setLayoutData(gd);
		return block.getContent();
	}

	@Override
	protected void createDialogBlocks(final Composite parent, int numColumns) {
		super.createDialogBlocks(parent, numColumns);
		ExpandableBlock node = new ExpandableBlock(parent, 0);
		node.setText(TclInterpreterMessages.AddTclInterpreterDialog_0);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		node.setLayoutData(gd);
		globals = new GlobalVariableBlock(this);
		globals.createControlsIn(node.getContent());
		node.setExpanded(true);
		// Available packages
		ExpandableBlock node2 = new ExpandableBlock(parent, 0);
		node2.setText("Available packages:");
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd2.horizontalSpan = numColumns;
		node2.setLayoutData(gd2);
		packagesBlock = new AvailablePackagesBlock();
		packagesBlock.createIn(node2.getContent());
		node2.setExpanded(true);
	}

	@Override
	protected void updateValidateInterpreterLocation() {
		// TODO Auto-generated method stub
		super.updateValidateInterpreterLocation();
		IStatus status = getInterpreterLocationStatus();
		if (status instanceof StatusWithPackages) {
			StatusWithPackages swp = (StatusWithPackages) status;
			packagesBlock.updatePackages(swp.getPackages());
		}
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		// Resize dialog height
		Shell shell = getShell();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point size2 = shell.getSize();
		shell.setSize(size2.x, size.y);
	}

	@Override
	protected boolean useInterpreterArgs() {
		return false;
	}

	@Override
	protected boolean isRediscoverSupported() {
		return false;
	}

	@Override
	protected void okPressed() {
		super.okPressed();
		// Remove all information for packages infrastructure for this
		// interpreter.
		IInterpreterInstall install = getLastInterpreterInstall();
		if (install != null) {
			IContentCache coreCache = ModelManager.getModelManager()
					.getCoreCache();
			coreCache.clearCacheEntryAttributes(install.getInstallLocation());
			TclPackagesManager.removeInterpreterInfo(install);
		}
	}

	@Override
	protected String getDialogSettingsSectionName() {
		return "ADD_TCL_SCRIPT_INTERPRETER_DIALOG_SECTION"; //$NON-NLS-1$
	}

	@Override
	protected void initializeFields(IInterpreterInstall install) {
		super.initializeFields(install);
		if (install != null) {
			VariableMap variableMap = (VariableMap) install
					.findExtension(TclPackagesPackage.Literals.VARIABLE_MAP);
			if (variableMap != null) {
				globals.setValues(variableMap.getVariables());
			} else {
				globals.setValues(ECollections
						.<String, VariableValue> emptyEMap());
			}
		} else {
			globals.setValues(ECollections.<String, VariableValue> emptyEMap());
		}
	}

	@Override
	protected void setFieldValuesToInterpreter(IInterpreterInstall install) {
		super.setFieldValuesToInterpreter(install);
		final EMap<String, VariableValue> newVars = globals.getValues();
		final EMap<String, VariableValue> oldVars = TclPackagesManager
				.getVariablesEMap(install);
		if (!GlobalVariableBlock.equalsEMap(newVars, oldVars)) {
			TclPackagesManager.setVariables(install, newVars);
		}
	}
}
