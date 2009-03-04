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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.SelectionDialog.ProjectSelectionDialogInput;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.SelectionDialog.WorkspaceSelectionDialogInput;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class InstrumentationPatternList {

	private static class InstrumentPatternContentProvider implements
			ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof List) {
				return ((List<?>) inputElement).toArray();
			}
			return new Object[0];
		}

		public void dispose() {
			// empty
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// empty
		}

	}

	private final IProject parentProject;
	private final Shell shell;
	private final boolean includeMode;
	private final String title;

	private TableViewer fList;

	private Button fAddButton;
	private Button fEditButton;
	private Button fBrowseButton;
	private Button fRemoveButton;

	/**
	 * @param parent
	 * @param shell
	 * @param string
	 */
	public InstrumentationPatternList(IProject parentProject, Composite parent,
			String message, Shell shell, boolean includeMode) {
		this.parentProject = parentProject;
		this.shell = shell;
		this.includeMode = includeMode;
		this.title = message;
		createControl(parent, message);
	}

	private Control createControl(final Composite parent, String message) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		topLayout.marginHeight = 0;
		topLayout.marginWidth = 0;
		comp.setLayout(topLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);

		if (message != null) {
			showLabel(comp, message, 2);
		}

		fList = new TableViewer(comp, SWT.TOP | SWT.BORDER | SWT.MULTI);
		fList.setContentProvider(new InstrumentPatternContentProvider());
		fList.setLabelProvider(new InstrumentationPatternLabelProvider());
		fList.setComparator(new InstrumentationPatternComparator());
		fList.setInput(new ArrayList<Pattern>());
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 6;
		fList.getControl().setLayoutData(gd);
		fList.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				InstrumentationPatternList.this.selectionChanged(event
						.getSelection());
			}

		});
		// fList.addCheckStateListener(new ICheckStateListener() {
		//
		// public void checkStateChanged(CheckStateChangedEvent event) {
		// if (event.getElement() instanceof Pattern) {
		// ((Pattern) event.getElement()).setInclude(event
		// .getChecked());
		// }
		// }
		//
		// });
		Composite pathButtonComp = new Composite(comp, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL);
		pathButtonComp.setLayoutData(gd);
		pathButtonComp.setFont(font);
		fAddButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_pattern_AddButton);
		fAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				onAdd(null);
			}
		});
		fEditButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_pattern_EditButton);
		fEditButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onEdit();
			}
		});
		fBrowseButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_patternBrowseButton);
		fBrowseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onBrowse();
			}
		});
		fRemoveButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_pattern_RemoveButton);
		fRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final ISelection s = fList.getSelection();
				if (s instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) s;
					@SuppressWarnings("unchecked")
					final List<Pattern> model = (List<Pattern>) fList
							.getInput();
					for (Object item : sel.toArray()) {
						model.remove(item);
					}
					fList.refresh();
				}
			}
		});
		selectionChanged(fList.getSelection());
		return comp;
	}

	protected void showLabel(Composite comp, String message, int colSpan) {
		GridData gd;
		Label messageLabel = new Label(comp, SWT.WRAP);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.horizontalSpan = colSpan;
		messageLabel.setText(message);
		messageLabel.setLayoutData(gd);
	}

	protected void selectionChanged(ISelection selection) {
		fRemoveButton.setEnabled(!selection.isEmpty());
		fEditButton
				.setEnabled(selection instanceof IStructuredSelection
						&& ((IStructuredSelection) selection).size() == 1
						&& ((IStructuredSelection) selection).getFirstElement() instanceof GlobPattern);
	}

	protected void onAdd(final GlobPattern input) {
		PatternDialog dialog = new PatternDialog(shell, input != null ? input
				.getPath() : Util.EMPTY_STRING);
		dialog.setTitle(NLS.bind(input != null ? "Edit {0} Pattern"
				: "Add {0} Pattern", title));
		if (dialog.open() == Window.OK) {
			final String newGlob = dialog.getPattern();
			if (input != null && newGlob.equals(input.getPath())) {
				return;
			}
			@SuppressWarnings("unchecked")
			final List<Pattern> model = (List<Pattern>) fList.getInput();
			final Set<String> globs = new HashSet<String>();
			for (Pattern pattern : model) {
				if (pattern instanceof GlobPattern && pattern != input) {
					globs.add(pattern.getPath());
				}
			}
			if (globs.add(newGlob)) {
				if (input == null) {
					GlobPattern pattern = PreferencesFactory.eINSTANCE
							.createGlobPattern();
					pattern.setInclude(includeMode);
					pattern.setPath(newGlob);
					model.add(pattern);
					fList.add(pattern);
					// fList.setChecked(pattern, pattern.isInclude());
					fList.setSelection(new StructuredSelection(pattern));
				} else {
					input.setPath(newGlob);
					fList.refresh(input);
				}
			}
		}
	}

	protected void onEdit() {
		ISelection selection = fList.getSelection();
		if (selection != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1 && ss.getFirstElement() instanceof GlobPattern) {
				onAdd((GlobPattern) ss.getFirstElement());
			}
		}
	}

	protected void onBrowse() {
		@SuppressWarnings("unchecked")
		final List<Pattern> model = (List<Pattern>) fList.getInput();
		final Set<String> resourcePaths = new HashSet<String>();
		final Set<String> externalPaths = new HashSet<String>();
		for (Pattern pattern : model) {
			if (pattern instanceof WorkspacePattern) {
				resourcePaths.add(pattern.getPath());
			} else if (pattern instanceof ExternalPattern) {
				externalPaths.add(pattern.getPath());
			}
		}
		SelectionDialog dialog = new SelectionDialog(fList.getControl()
				.getShell());
		dialog
				.setTitle(PreferenceMessages.InstrumentationPatternList_SelectionDialog_Title);
		dialog
				.setInput(parentProject == null ? new WorkspaceSelectionDialogInput()
						: new ProjectSelectionDialogInput(parentProject));
		// TODO dialog.setExisting(new Object[0]);
		dialog.open();
		final Object[] result = dialog.getResult();
		if (result != null) {
			for (Object item : result) {
				if (item instanceof IResource) {
					final String path = ((IResource) item).getFullPath()
							.toString();
					if (resourcePaths.add(path)) {
						final WorkspacePattern pattern = PreferencesFactory.eINSTANCE
								.createWorkspacePattern();
						pattern.setInclude(includeMode);
						pattern.setPath(path);
						model.add(pattern);
						fList.refresh();
						// fList.setChecked(pattern, pattern.isInclude());
					}
				} else if (item instanceof IModelElement) {
					final IModelElement me = (IModelElement) item;
					final IProjectFragment fragment = (IProjectFragment) me
							.getAncestor(IModelElement.PROJECT_FRAGMENT);
					if (fragment.isExternal()) {
						final String path = EnvironmentPathUtils.getLocalPath(
								me.getPath()).toString();
						if (externalPaths.add(path)) {
							final ExternalPattern pattern = PreferencesFactory.eINSTANCE
									.createExternalPattern();
							pattern.setInclude(includeMode);
							pattern.setPath(path);
							model.add(pattern);
							fList.refresh();
							// fList.setChecked(pattern, pattern.isInclude());
						}
					}
				}
			}
		}
	}

	protected Button createPushButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData();
		button.setLayoutData(gd);
		// gd.widthHint = getButtonWidthHint(button);
		gd.horizontalAlignment = GridData.FILL;
		return button;
	}

	/**
	 * Returns a width hint for a button control.
	 */
	private int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter
				.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).x);
	}

	/**
	 * @param value
	 */
	public void setValue(List<Pattern> model) {
		// List<Pattern> model = PatternListIO.decode(value);
		fList.setInput(model);
		// for (Pattern pattern : model) {
		// fList.setChecked(pattern, pattern.isInclude());
		// }
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pattern> getValue() {
		return (List<Pattern>) fList.getInput();
	}

}
