/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterEnvironmentVariablesBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.ExpandableBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersMessages;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ComboDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.InterpreterStandin;
import org.eclipse.dltk.launching.LibraryLocation;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.VariableMap;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.launching.StatusWithPackages;
import org.eclipse.dltk.tcl.internal.ui.GlobalVariableBlock;
import org.eclipse.dltk.tcl.ui.manpages.Documentation;
import org.eclipse.dltk.tcl.ui.manpages.InterpreterDocumentation;
import org.eclipse.dltk.tcl.ui.manpages.ManPageLoader;
import org.eclipse.dltk.tcl.ui.manpages.ManpagesFactory;
import org.eclipse.dltk.tcl.ui.manpages.ManpagesPackage;
import org.eclipse.dltk.tcl.ui.manpages.dialogs.ManPagesConfigurationDialog;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
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
	private ExpandableBlock globalsExpandableNode;
	private ExpandableBlock libraryExpandableNode;
	private ExpandableBlock environmentExpandableNode;

	protected Composite createEnvironmentVariablesBlockParent(Composite parent,
			int numColumns) {
		environmentExpandableNode = new ExpandableBlock(parent, 0);
		environmentExpandableNode
				.setText(InterpretersMessages.AddScriptInterpreterDialog_interpreterEnvironmentVariables);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		environmentExpandableNode.setLayoutData(gd);
		environmentExpandableNode.setExpanded(true);
		return environmentExpandableNode.getContent();
	}

	protected Composite createLibraryBlockParent(Composite parent,
			int numColumns) {
		libraryExpandableNode = new ExpandableBlock(parent, 0);
		libraryExpandableNode
				.setText("Custom library locations (addition to TCLLIBPATH):");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		libraryExpandableNode.setLayoutData(gd);
		return libraryExpandableNode.getContent();
	}

	private ComboDialogField documentationField;
	private List<Documentation> documentations;

	private Documentation findById(String docId) {
		if (docId != null) {
			for (Documentation documentation : documentations) {
				if (docId.equals(documentation.getId())) {
					return documentation;
				}
			}
		}
		return null;
	}

	@Override
	protected void createSimpleFields(Composite parent, int numColumns) {
		super.createSimpleFields(parent, numColumns);
		documentationField = new ComboDialogField(SWT.BORDER | SWT.READ_ONLY);
		documentationField.setLabelText("Manual Pages");
		loadDocumentations();
		documentationField.doFillIntoGrid(parent, numColumns - 1);
		final Button configureDocumentation = new Button(parent, SWT.PUSH);
		configureDocumentation.setText("Configure...");
		configureDocumentation.setLayoutData(StringButtonDialogField
				.gridDataForButton(configureDocumentation, 1));
		configureDocumentation.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				configureDocumentation();
			}
		});
	}

	protected void configureDocumentation() {
		final ManPagesConfigurationDialog dialog = new ManPagesConfigurationDialog(
				getShell());
		if (dialog.open() == Window.OK) {
			final int index = documentationField.getSelectionIndex();
			final String docId = index > 0 ? this.documentations.get(index - 1)
					.getId() : null;
			loadDocumentations();
			final Documentation doc = findById(docId);
			documentationField.selectItem(doc != null ? this.documentations
					.indexOf(doc) + 1 : 0);
		}
	}

	private void loadDocumentations() {
		documentations = new ArrayList<Documentation>(ManPageLoader.load()
				.getDocumentations());
		final String[] names = new String[documentations.size() + 1];
		names[0] = documentations.isEmpty() ? "(not configured)" : "(default)";
		Collections.sort(documentations, new Comparator<Documentation>() {
			public int compare(Documentation o1, Documentation o2) {
				return o1.getName().compareToIgnoreCase(o2.getName());
			}
		});
		for (int i = 0; i < documentations.size(); ++i) {
			names[i + 1] = documentations.get(i).getName();
		}
		documentationField.setItems(names);
	}

	@Override
	protected void createDialogBlocks(final Composite parent, int numColumns) {
		super.createDialogBlocks(parent, numColumns);
		globalsExpandableNode = new ExpandableBlock(parent, 0);
		globalsExpandableNode
				.setText(TclInterpreterMessages.AddTclInterpreterDialog_0);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		globalsExpandableNode.setLayoutData(gd);
		globals = new GlobalVariableBlock(this);
		globals.createControlsIn(globalsExpandableNode.getContent());
		globalsExpandableNode.setExpanded(true);
		// Available packages
		ExpandableBlock node2 = new ExpandableBlock(parent, 0);
		node2.setText("Available packages:");
		GridData gd2 = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd2.horizontalSpan = numColumns;
		node2.setLayoutData(gd2);
		packagesBlock = new AvailablePackagesBlock(this);
		packagesBlock.createIn(node2.getContent());
		node2.setExpanded(true);
	}

	@Override
	protected void updateValidateInterpreterLocation() {
		super.updateValidateInterpreterLocation();
		IStatus status = getInterpreterLocationStatus();
		if (status instanceof StatusWithPackages) {
			StatusWithPackages swp = (StatusWithPackages) status;
			packagesBlock.updatePackages(swp.getInterpreter());
		} else {
			// Make it empty if not detected.
			packagesBlock.updatePackages(null);
		}
	}

	@Override
	protected void initializeBounds() {
		super.initializeBounds();
		// Resize dialog height
		Shell shell = getShell();
		Point size = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		Point size2 = shell.getSize();
		if (size.y > size2.y) {
			shell.setSize(size2.x, size.y);
		}
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
			final InterpreterDocumentation documentation = (InterpreterDocumentation) install
					.findExtension(ManpagesPackage.Literals.INTERPRETER_DOCUMENTATION);
			final String docId = documentation != null ? documentation
					.getDocumentationId() : null;
			final Documentation doc = findById(docId);
			documentationField.selectItem(doc != null ? documentations
					.indexOf(doc) + 1 : 0);
			VariableMap variableMap = (VariableMap) install
					.findExtension(TclPackagesPackage.Literals.VARIABLE_MAP);
			if (variableMap != null) {
				globals.setValues(variableMap.getVariables());
			} else {
				globals.setValues(ECollections
						.<String, VariableValue> emptyEMap());
			}
		} else {
			documentationField.selectItem(0);
			globals.setValues(ECollections.<String, VariableValue> emptyEMap());
		}

		// Set initial expanding
		globalsExpandableNode.setExpanded(!globals.getValues().isEmpty());
		environmentExpandableNode.setExpanded(fEnvironmentVariablesBlock
				.getEnvironmentVariables().length > 0);
	}

	@Override
	protected void setFieldValuesToInterpreter(IInterpreterInstall install) {
		super.setFieldValuesToInterpreter(install);
		int index = documentationField.getSelectionIndex();
		if (index == 0) {
			install.replaceExtension(
					ManpagesPackage.Literals.INTERPRETER_DOCUMENTATION, null);
		} else {
			final Documentation doc = documentations.get(index - 1);
			final InterpreterDocumentation idoc = ManpagesFactory.eINSTANCE
					.createInterpreterDocumentation();
			idoc.setDocumentationId(doc.getId());
			install.replaceExtension(
					ManpagesPackage.Literals.INTERPRETER_DOCUMENTATION, idoc);
		}
		final EMap<String, VariableValue> newVars = globals.getValues();
		final EMap<String, VariableValue> oldVars = TclPackagesManager
				.getVariablesEMap(install);
		if (!GlobalVariableBlock.equalsEMap(newVars, oldVars)) {
			TclPackagesManager.setVariables(install, newVars);
		}
	}

	/**
	 * @since 2.0
	 */
	public InterpreterStandin getInterpreterStandin() {
		InterpreterStandin standin = new InterpreterStandin(
				fSelectedInterpreterType, "$fake$");
		IEnvironment selectedEnv = getEnvironment();
		String locationName = getInterpreterPath();
		final IFileHandle file;
		if (locationName.length() == 0) {
			file = null;
			return null;
		} else {
			file = PlatformFileUtils.findAbsoluteOrEclipseRelativeFile(
					selectedEnv, new Path(locationName));
			EnvironmentVariable[] environmentVariables = null;
			if (fEnvironmentVariablesBlock != null) {
				environmentVariables = fEnvironmentVariablesBlock
						.getEnvironmentVariables();
			}
			LibraryLocation[] locations = fLibraryBlock.getLibraryLocations();
			standin.setInstallLocation(file);
			standin.setLibraryLocations(locations);
			standin.setEnvironmentVariables(environmentVariables);
		}
		return standin;
	}
}
