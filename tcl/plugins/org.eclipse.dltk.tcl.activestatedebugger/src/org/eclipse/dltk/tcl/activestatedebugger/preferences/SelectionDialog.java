/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
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

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IParent;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptModel;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.internal.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.ModelElementLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class SelectionDialog extends SelectionStatusDialog implements
		ISelectionChangedListener {

	static class SelectionDialogInput {
		final IScriptProject project;

		/**
		 * @param project
		 */
		public SelectionDialogInput(IScriptProject project) {
			this.project = project;
		}

		/**
		 * @param parentProject
		 */
		public SelectionDialogInput(IProject project) {
			this.project = DLTKCore.create(project);
		}

	}

	private static class SelectionDialogComparator extends ViewerComparator {

		@Override
		public int category(Object element) {
			if (element instanceof IResource) {
				return 1;
			} else if (element instanceof IModelElement) {
				return 2;
			} else {
				return 0;
			}
		}

	}

	private static class SelectionDialogTreeContentProvider implements
			ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			try {
				if (parentElement instanceof IScriptProject) {
					final IProjectFragment[] fragments = ((IScriptProject) parentElement)
							.getProjectFragments();
					final List<IProjectFragment> result = new ArrayList<IProjectFragment>(
							fragments.length);
					for (IProjectFragment fragment : fragments) {
						if (!fragment.isExternal()) {
							result.add(fragment);
						}
					}
					return result.toArray();
				} else if (parentElement instanceof ISourceModule) {
					return new Object[0];
				} else if (parentElement instanceof IParent) {
					return ((IParent) parentElement).getChildren();
				} else if (parentElement instanceof IContainer) {
					return ((IContainer) parentElement).members();
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			if (element instanceof IResource) {
				return ((IResource) element).getParent();
			} else if (element instanceof IModelElement) {
				return ((IModelElement) element).getParent();
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			try {
				if (element instanceof ISourceModule) {
					return false;
				} else if (element instanceof IParent) {
					return ((IParent) element).hasChildren();
				} else if (element instanceof IContainer) {
					return true;
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof SelectionDialogInput) {
				final Set<IScriptProject> projects = new HashSet<IScriptProject>();
				final IScriptModel model = DLTKCore.create(ResourcesPlugin
						.getWorkspace().getRoot());
				final Set<IProjectFragment> libraries = new HashSet<IProjectFragment>();
				collectProjects(model, projects,
						((SelectionDialogInput) inputElement).project);
				for (IScriptProject project : projects) {
					try {
						for (IProjectFragment fragment : project
								.getProjectFragments()) {
							if (fragment.isExternal()
									&& !IBuildpathEntry.BUILTIN_EXTERNAL_ENTRY_STR
											.equals(fragment.getPath().segment(
													0))) {
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
				for (IScriptProject project : projects) {
					try {
						result.add(project.getUnderlyingResource());
					} catch (ModelException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				}
				for (IProjectFragment fragment : libraries) {
					result.add(fragment);
				}
				return result.toArray();
			}
			return new Object[0];
		}

		/**
		 * @param model
		 * @param projects
		 * @param project
		 */
		private void collectProjects(IScriptModel model,
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

		public void dispose() {
			// TODO Auto-generated method stub
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// TODO Auto-generated method stub
		}

	}

	private CheckboxTreeViewer fViewer;

	private List<ViewerFilter> fFilters;

	private Object fInput;

	private Set<Object> fExisting;
	private Object fFocusElement;

	public SelectionDialog(Shell parent) {
		super(parent);

		setSelectionResult(null);
		setStatusLineAboveButtons(true);

		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fExisting = null;
		fFocusElement = null;
		fFilters = null;
	}

	public void setExisting(Object[] existing) {
		fExisting = new HashSet<Object>();
		for (int i = 0; i < existing.length; i++) {
			fExisting.add(existing[i]);
		}
	}

	/**
	 * Sets the tree input.
	 * 
	 * @param input
	 *            the tree input.
	 */
	public void setInput(Object input) {
		fInput = input;
	}

	/**
	 * Adds a filter to the tree viewer.
	 * 
	 * @param filter
	 *            a filter.
	 */
	public void addFilter(ViewerFilter filter) {
		if (fFilters == null) {
			fFilters = new ArrayList<ViewerFilter>(4);
		}
		fFilters.add(filter);
	}

	/**
	 * Handles cancel button pressed event.
	 */
	protected void cancelPressed() {
		setSelectionResult(null);
		super.cancelPressed();
	}

	/*
	 * @see SelectionStatusDialog#computeResult()
	 */
	protected void computeResult() {
		Object[] checked = fViewer.getCheckedElements();
		if (fExisting == null) {
			if (checked.length == 0) {
				checked = null;
			}
		} else {
			List<Object> result = new ArrayList<Object>();
			for (int i = 0; i < checked.length; i++) {
				Object elem = checked[i];
				if (!fExisting.contains(elem)) {
					result.add(elem);
				}
			}
			if (!result.isEmpty()) {
				checked = result.toArray();
			} else {
				checked = null;
			}
		}
		setSelectionResult(checked);
	}

	private void access$superCreate() {
		super.create();
	}

	/*
	 * @see Window#create()
	 */
	public void create() {

		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				access$superCreate();

				fViewer.setCheckedElements(getInitialElementSelections()
						.toArray());

				fViewer.expandToLevel(2);
				if (fExisting != null) {
					for (Object item : fExisting) {
						fViewer.reveal(item);
					}
				}

				updateOKStatus();
			}
		});

	}

	/**
	 * Creates the tree viewer.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the tree viewer
	 */
	protected CheckboxTreeViewer createTreeViewer(Composite parent) {
		fViewer = new CheckboxTreeViewer(parent, SWT.BORDER);

		fViewer.setContentProvider(new SelectionDialogTreeContentProvider());
		fViewer.setLabelProvider(new ModelElementLabelProvider());
		fViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateOKStatus();
			}
		});

		fViewer.setComparator(new SelectionDialogComparator());
		if (fFilters != null) {
			for (ViewerFilter filter : fFilters)
				fViewer.addFilter(filter);
		}

		fViewer.setInput(fInput);

		return fViewer;
	}

	/**
	 * 
	 */
	protected void updateOKStatus() {
		computeResult();
		if (getResult() != null) {
			updateStatus(new StatusInfo());
		} else {
			updateStatus(new StatusInfo(IStatus.ERROR, "")); //$NON-NLS-1$
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		createMessageArea(composite);
		CheckboxTreeViewer treeViewer = createTreeViewer(composite);

		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = convertWidthInCharsToPixels(60);
		data.heightHint = convertHeightInCharsToPixels(18);

		Tree treeWidget = treeViewer.getTree();
		treeWidget.setLayoutData(data);
		treeWidget.setFont(composite.getFont());

		treeViewer.addSelectionChangedListener(this);
		if (fExisting != null) {
			Object[] existing = fExisting.toArray();
			treeViewer.setGrayedElements(existing);
			setInitialSelections(existing);
		}
		if (fFocusElement != null) {
			treeViewer.setSelection(new StructuredSelection(fFocusElement),
					true);
		}
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				forceExistingChecked(event);
			}
		});

		applyDialogFont(composite);
		return composite;
	}

	protected void forceExistingChecked(CheckStateChangedEvent event) {
		if (fExisting != null) {
			Object elem = event.getElement();
			if (fExisting.contains(elem)) {
				fViewer.setChecked(elem, true);
			}
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {

	}

	public void setInitialFocus(Object focusElement) {
		fFocusElement = focusElement;
	}

}
