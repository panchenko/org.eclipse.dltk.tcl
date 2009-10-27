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

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.tcl.internal.core.sources.TclSourcesFragment;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @since 2.0
 */
public class LibraryContainerElement extends WorkbenchAdaptable {

	/**
	 * @param input
	 */
	public LibraryContainerElement(SelectionDialogInput input) {
		super(input);
	}

	@Override
	public ContainerType getContainerType() {
		return ContainerType.LIBRARIES;
	}

	@Override
	public Object[] getChildren() {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<IProjectFragment> libraries = collectExternalFragments(projects);
		return libraries.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Libraries;
	}

	public static Set<IProjectFragment> collectExternalFragments(
			final Set<IScriptProject> projects) {
		final Set<IProjectFragment> libraries = new HashSet<IProjectFragment>();
		for (IScriptProject project : projects) {
			try {
				for (IProjectFragment fragment : project.getProjectFragments()) {
					if (LibraryContainerElement.isValid(fragment)) {
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

	public static boolean isValid(IProjectFragment fragment) {
		return fragment.isExternal() && !fragment.isBuiltin()
				&& !(fragment instanceof TclPackageFragment)
				&& !(fragment instanceof TclSourcesFragment);
	}

}
