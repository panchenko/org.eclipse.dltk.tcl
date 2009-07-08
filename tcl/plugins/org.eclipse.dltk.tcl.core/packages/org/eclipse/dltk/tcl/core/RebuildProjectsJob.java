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
package org.eclipse.dltk.tcl.core;

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
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.osgi.util.NLS;

class RebuildProjectsJob extends Job {

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
			TclPlugin.getDefault().getLog().log(
					new Status(IStatus.ERROR, TclPlugin.PLUGIN_ID, e
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
				TclPlugin.getDefault().getLog().log(
						new Status(IStatus.ERROR, TclPlugin.PLUGIN_ID, e
								.getMessage(), e));
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