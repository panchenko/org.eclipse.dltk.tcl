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
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

/**
 * @since 2.0
 */
public class ProjectBuildJob extends Job {

	private final IProject project;
	private final int kind;

	public ProjectBuildJob(IProject project) {
		this(project, IncrementalProjectBuilder.FULL_BUILD);
	}

	public ProjectBuildJob(IProject project, int kind) {
		super(NLS.bind(TclPreferencesMessages.ProjectBuildJob_BuildingJobName,
				project.getName()));
		this.project = project;
		this.kind = kind;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			project.build(kind, monitor);
			return Status.OK_STATUS;
		} catch (CoreException e) {
			return e.getStatus();
		}
	}
}