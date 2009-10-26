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
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.StandardModelElementContentProvider2;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

public class InstrumentationContentProvider extends
		StandardModelElementContentProvider2 {

	public InstrumentationContentProvider() {
		super(false, false);
	}

	private boolean fIsFlatLayout = false;

	private Object input;

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.input = newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SelectionDialogInput) {
			final SelectionDialogInput input = (SelectionDialogInput) inputElement;
			final Set<IScriptProject> projects = input.collectProjects();
			final List<Object> result = new ArrayList<Object>();
			result.addAll(projects);
			final LibraryContainerElement libContainer = new LibraryContainerElement(
					input);
			if (libContainer.getChildren().length != 0) {
				result.add(libContainer);
			}
			final PackageContainerElement packageContainer = new PackageContainerElement(
					input);
			if (packageContainer.getChildren().length != 0) {
				result.add(packageContainer);
			}
			final SourceContainerElement sourceContainer = new SourceContainerElement(
					input);
			if (sourceContainer.getChildren().length != 0) {
				result.add(sourceContainer);
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
				&& LibraryContainerElement.isValid((IProjectFragment) element)) {
			return new LibraryContainerElement((SelectionDialogInput) input);
		}
		if (element instanceof PackageElement) {
			return new PackageContainerElement((SelectionDialogInput) input);
		}
		if (element instanceof SourceElement) {
			return new SourceContainerElement((SelectionDialogInput) input);
		}
		if (!fIsFlatLayout && element instanceof IScriptFolder) {
			return getHierarchicalPackageParent((IScriptFolder) element);
		}
		return super.internalGetParent(element);
	}

	@Override
	public Object[] getChildren(Object element) {
		if (element instanceof IWorkbenchAdapter) {
			return ((IWorkbenchAdapter) element).getChildren(element);
		} else {
			return super.getChildren(element);
		}
	}

	@Override
	protected Object[] getProjectFragmentContent(final IProjectFragment root)
			throws ModelException {
		if (fIsFlatLayout) {
			return super.getProjectFragmentContent(root);
		}
		// hierarchical package mode
		final ArrayList<Object> result = new ArrayList<Object>();
		getHierarchicalPackageChildren(root, null, result);
		if (fForeignResources && !isProjectProjectFragment(root)) {
			final Object[] nonJavaResources = root.getForeignResources();
			for (int i = 0; i < nonJavaResources.length; i++) {
				result.add(nonJavaResources[i]);
			}
		}
		return result.toArray();
	}

	@Override
	protected Object[] getScriptFolderContent(final IScriptFolder fragment)
			throws ModelException {
		if (fIsFlatLayout) {
			return super.getScriptFolderContent(fragment);
		}
		// hierarchical package mode
		ArrayList<Object> result = new ArrayList<Object>();
		getHierarchicalPackageChildren((IProjectFragment) fragment.getParent(),
				fragment, result);
		Object[] nonPackages = super.getScriptFolderContent(fragment);
		if (result.isEmpty()) {
			return nonPackages;
		}
		for (int i = 0; i < nonPackages.length; i++) {
			result.add(nonPackages[i]);
		}
		return result.toArray();
	}

	/**
	 * Returns the hierarchical packages inside a given fragment or root.
	 * 
	 * @param parent
	 *            The parent package fragment root
	 * @param fragment
	 *            The package to get the children for or 'null' to get the
	 *            children of the root.
	 * @param result
	 *            Collection where the resulting elements are added
	 * @throws JavaModelException
	 */
	@SuppressWarnings("unchecked")
	private void getHierarchicalPackageChildren(final IProjectFragment parent,
			final IScriptFolder fragment, final Collection result)
			throws ModelException {
		IModelElement[] children = parent.getChildren();
		List<IModelElement> newElements = new ArrayList<IModelElement>();
		if (fragment == null || fragment.isRootFolder()) {
			for (int i = 0; i < children.length; ++i) {
				if (children[i] instanceof IScriptFolder) {
					IScriptFolder scriptFolder = ((IScriptFolder) children[i]);
					if (scriptFolder.isRootFolder()) {
						IModelElement[] members = scriptFolder.getChildren();
						for (int j = 0; j < members.length; ++j) {
							newElements.add(members[j]);
						}
						continue;
					}
				}
				newElements.add(children[i]);
			}
			children = newElements
					.toArray(new IModelElement[newElements.size()]);
		}
		String prefix = fragment != null ? fragment.getElementName()
				+ IScriptFolder.PACKAGE_DELIMETER_STR : ""; //$NON-NLS-1$
		int prefixLen = prefix.length();
		for (int i = 0; i < children.length; i++) {
			if (children[i] instanceof IScriptFolder) {
				IScriptFolder curr = (IScriptFolder) children[i];
				String name = curr.getElementName();
				if (name.startsWith(prefix)
						&& name.length() > prefixLen
						&& name.indexOf(IScriptFolder.PACKAGE_DELIMITER,
								prefixLen) == -1) {
					// if (fFoldPackages) {
					// curr = ScriptExplorerContentProvider.getFolded(
					// children, curr);
					// }
					result.add(curr);
				}
				/*
				 * else if (fragment == null && curr.isRootFolder()) {
				 * result.add(curr); }
				 */
			} else {
				result.add(children[i]);
			}
		}
	}

	public Object getHierarchicalPackageParent(final IScriptFolder child) {
		String name = child.getElementName();
		IProjectFragment parent = (IProjectFragment) child.getParent();
		int index = name.lastIndexOf(IScriptFolder.PACKAGE_DELIMITER);
		if (index != -1) {
			String realParentName = name.substring(0, index);
			IScriptFolder element = parent.getScriptFolder(realParentName);
			if (element.exists()) {
				// try {
				// if (fFoldPackages
				// && ScriptExplorerContentProvider.isEmpty(element)
				// && ScriptExplorerContentProvider
				// .findSinglePackageChild(element, parent
				// .getChildren()) != null) {
				// return getHierarchicalPackageParent(element);
				// }
				// } catch (ModelException e) {
				// ignore
				// }
				return element;
			} else { // bug 65240
				IResource resource = element.getResource();
				if (resource != null) {
					return resource;
				}
			}
		}
		if (parent.getResource() instanceof IProject) {
			return parent.getScriptProject();
		}
		return parent;
	}

}
