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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageElement;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesElement;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesSourceModule;

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

	public static Set<IProjectFragment> collectExternalFragments(
			final Set<IScriptProject> projects) {
		final Set<IProjectFragment> libraries = new HashSet<IProjectFragment>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (fragment.isExternal() && !fragment.isBuiltin()
							&& !(fragment instanceof TclPackageFragment)
							&& !(fragment instanceof TclSourcesFragment)) {
						libraries.add(fragment);
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return libraries;
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

	/**
	 * @since 1.1
	 */
	public static Set<TclPackageElement> collectPackages(
			Set<IScriptProject> projects) {
		final Set<TclPackageElement> packages = new HashSet<TclPackageElement>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (!(fragment instanceof TclPackageFragment)) {
						continue;
					}
					for (IModelElement element : fragment.getChildren()) {
						if (element instanceof TclPackageElement) {
							packages.add((TclPackageElement) element);
						}
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return packages;
	}

	/**
	 * @since 1.1
	 */
	public static Set<TclSourcesSourceModule> collectSources(
			Set<IScriptProject> projects) {
		final Set<TclSourcesSourceModule> sources = new HashSet<TclSourcesSourceModule>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (!(fragment instanceof TclSourcesFragment)) {
						continue;
					}
					for (IModelElement element : fragment.getChildren()) {
						if (element instanceof TclSourcesElement) {
							for (IModelElement ee : ((IParent) element)
									.getChildren()) {
								if (ee instanceof TclSourcesSourceModule) {
									sources.add((TclSourcesSourceModule) ee);
								}
							}
						}
					}
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		return sources;
	}

}
