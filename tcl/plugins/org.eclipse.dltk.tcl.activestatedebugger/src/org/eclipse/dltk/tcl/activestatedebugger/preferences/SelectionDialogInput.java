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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;

abstract class SelectionDialogInput {

	public abstract Set<IScriptProject> collectProjects();

	/**
	 * @param model
	 * @param projects
	 * @param project
	 */
	protected void collectProjects(IScriptModel model,
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

}
