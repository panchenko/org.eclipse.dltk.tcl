/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterEnvironmentVariablesBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackagesPackage;
import org.eclipse.dltk.tcl.core.packages.VariableMap;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.tcl.internal.debug.ui.TclDebugUIPlugin;
import org.eclipse.dltk.tcl.internal.ui.GlobalVariableBlock;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
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

	@Override
	protected void createDialogBlocks(Composite parent, int numColumns) {
		super.createDialogBlocks(parent, numColumns);
		Label l = new Label(parent, SWT.NONE);
		l.setText(TclInterpreterMessages.AddTclInterpreterDialog_0);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numColumns;
		l.setLayoutData(gd);

		globals = new GlobalVariableBlock(this);
		globals.createControlsIn(parent);
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
		// final EMap<String, VariableValue> newVars = (EMap<String,
		// VariableValue>) value;
		final EMap<String, VariableValue> oldVars = TclPackagesManager
				.getVariablesEMap(install);
		if (!GlobalVariableBlock.equalsEMap(newVars, oldVars)) {
			TclPackagesManager.setVariables(install, newVars);
			// FIXME new RebuildProjectsJob(install).schedule();
		}
	}

	private static class RebuildProjectsJob extends Job {

		private final IInterpreterInstall install;

		public RebuildProjectsJob(IInterpreterInstall install) {
			super(
					NLS
							.bind(
									TclInterpreterMessages.AddTclInterpreterDialog_RebuildJobName,
									install.getName()));
			this.install = install;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			final SubMonitor subMonitor = SubMonitor.convert(monitor, 100);
			final IScriptProject[] projects;
			try {
				final IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
						.getRoot();
				projects = DLTKCore.create(root).getScriptProjects(
						TclNature.NATURE_ID);
			} catch (ModelException e) {
				TclDebugUIPlugin.getDefault().getLog().log(
						new Status(IStatus.ERROR, TclDebugUIPlugin.PLUGIN_ID, e
								.getMessage(), e));
				return e.getStatus();
			}
			subMonitor.worked(20);
			final SubMonitor buildingMonitor = subMonitor.newChild(80);
			buildingMonitor
					.beginTask(
							TclInterpreterMessages.AddTclInterpreterDialog_RebuildProjectsTaskName,
							projects.length);
			for (int i = 0; i < projects.length; ++i) {
				final IScriptProject project = projects[i];
				try {
					bulidProject(project, buildingMonitor.newChild(1));
				} catch (CoreException e) {
					TclDebugUIPlugin.getDefault().getLog().log(
							new Status(IStatus.ERROR,
									TclDebugUIPlugin.PLUGIN_ID, e.getMessage(),
									e));
				}
			}
			subMonitor.done();
			return Status.OK_STATUS;
		}

		private void bulidProject(final IScriptProject project,
				SubMonitor monitor) throws CoreException {
			final IInterpreterInstall projectInterpreterInstall = ScriptRuntime
					.getInterpreterInstall(project);
			if (projectInterpreterInstall != null
					&& projectInterpreterInstall.equals(install)) {
				monitor
						.setTaskName(NLS
								.bind(
										TclInterpreterMessages.AddTclInterpreterDialog_RebuildProjectTaskName,
										project.getElementName()));
				project.getProject().build(
						IncrementalProjectBuilder.FULL_BUILD, monitor);
			}
		}
	}

}
