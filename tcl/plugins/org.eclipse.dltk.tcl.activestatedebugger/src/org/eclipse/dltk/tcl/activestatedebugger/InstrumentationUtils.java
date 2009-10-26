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
package org.eclipse.dltk.tcl.activestatedebugger;

import java.util.Set;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode;

public class InstrumentationUtils {

	/**
	 * @param model
	 * @param projects
	 * @param project
	 */
	public static void collectProjects(IScriptModel model,
			Set<IScriptProject> projects, IScriptProject project) {
		if (projects.add(project)) {
			final IBuildpathEntry[] entries;
			try {
				entries = project.getResolvedBuildpath(true);
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
				return;
			}
			for (IBuildpathEntry entry : entries) {
				if (entry.getEntryKind() == IBuildpathEntry.BPE_PROJECT) {
					collectProjects(model, projects, model
							.getScriptProject(entry.getPath().segment(0)));
				}
			}
		}
	}

	public static void collectProjects(Set<IScriptProject> projects,
			IScriptProject project) {
		final IScriptModel model = DLTKCore.create(getWorkspaceRoot());
		collectProjects(model, projects, project);
	}

	public static InstrumentationMode getMode(InstrumentationConfig config) {
		if (config != null) {
			return config.getMode();
		} else {
			return InstrumentationMode.SOURCES;
		}
	}

	protected static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

}
