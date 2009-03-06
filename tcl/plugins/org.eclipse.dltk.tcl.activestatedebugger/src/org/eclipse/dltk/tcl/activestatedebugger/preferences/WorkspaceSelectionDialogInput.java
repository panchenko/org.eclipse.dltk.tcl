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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.core.TclNature;

class WorkspaceSelectionDialogInput extends SelectionDialogInput {

	@Override
	public Set<IScriptProject> collectProjects() {
		final Set<IScriptProject> projects = new HashSet<IScriptProject>();
		final IScriptModel model = DLTKCore.create(ResourcesPlugin
				.getWorkspace().getRoot());
		try {
			for (IScriptProject project : model
					.getScriptProjects(TclNature.NATURE_ID)) {
				collectProjects(model, projects, project);
			}
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return projects;
	}
}