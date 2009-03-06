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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.StandardModelElementContentProvider2;
import org.eclipse.jface.viewers.Viewer;

public class ContentProvider extends StandardModelElementContentProvider2 {

	public ContentProvider() {
		super(false, false);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SelectionDialogInput) {
			final Set<IScriptProject> projects = ((SelectionDialogInput) inputElement)
					.collectProjects();
			final Set<IProjectFragment> libraries = new HashSet<IProjectFragment>();
			for (IScriptProject project : projects) {
				try {
					for (IProjectFragment fragment : project
							.getProjectFragments()) {
						if (fragment.isExternal() && !fragment.isBuiltin()) {
							libraries.add(fragment);
						}
					}
				} catch (ModelException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
			final List<Object> result = new ArrayList<Object>();
			result.addAll(projects);
			for (IProjectFragment fragment : libraries) {
				result.add(fragment);
			}
			return result.toArray();
		}
		return NO_CHILDREN;
	}

	@Override
	protected boolean isValidProjectFragment(IProjectFragment root) {
		return !root.isExternal();
	}

	@Override
	protected Object internalGetParent(Object element) {
		if (element instanceof IScriptProject) {
			return null;
		}
		if (element instanceof IProjectFragment
				&& ((IProjectFragment) element).isExternal()) {
			return null;
		}
		return super.internalGetParent(element);
	}

}
