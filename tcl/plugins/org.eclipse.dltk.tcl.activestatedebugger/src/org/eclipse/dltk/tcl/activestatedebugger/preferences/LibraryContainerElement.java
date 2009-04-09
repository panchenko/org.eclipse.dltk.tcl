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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationUtils;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.model.IWorkbenchAdapter;

class LibraryContainerElement implements IAdaptable, IWorkbenchAdapter {

	private final SelectionDialogInput input;

	/**
	 * @param input
	 */
	public LibraryContainerElement(SelectionDialogInput input) {
		this.input = input;
	}

	@Override
	public int hashCode() {
		return input.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LibraryContainerElement) {
			return input.equals(((LibraryContainerElement) obj).input);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IWorkbenchAdapter.class)
			return this;
		return null;
	}

	public Object[] getChildren(Object o) {
		final Set<IScriptProject> projects = input.collectProjects();
		final Set<IProjectFragment> libraries = InstrumentationUtils
				.collectExternalFragments(projects);
		return libraries.toArray();
	}

	public ImageDescriptor getImageDescriptor(Object object) {
		return DLTKPluginImages.DESC_OBJS_LIBRARY;
	}

	public String getLabel(Object o) {
		return PreferenceMessages.InstrumentationLabelProvider_Libraries;
	}

	public Object getParent(Object o) {
		return null;
	}

}
