/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.internal.environment.LocalEnvironment;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.IInterpreterComboBlockContext;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.launching.ScriptRuntime.DefaultInterpreterEntry;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * This class is used on Interpreter tab in Launch Configurations and
 * AbstractInterpreterContainerWizardPage. Configured packages are saved only on
 * the later one. On Interpreter tab packages could not be modified.
 */
public class TclInterpreterComboBlock extends AbstractInterpreterComboBlock {
	/**
	 * @since 2.0
	 */
	protected TclInterpreterComboBlock(IInterpreterComboBlockContext context) {
		super(context);
	}

	private Set<String> packages = new HashSet<String>();
	private Set<String> autoPackages = new HashSet<String>();

	public class PackagesLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			if (element instanceof String) {
				String packageName = (String) element;
				IInterpreterInstall install = getInterpreter();
				if (install == null) {
					install = ScriptRuntime
							.getDefaultInterpreterInstall(new DefaultInterpreterEntry(
									TclNature.NATURE_ID,
									LocalEnvironment.ENVIRONMENT_ID));
				}
				if (install != null) {
					final Set<String> names = TclPackagesManager
							.getPackageInfosAsString(install);

					if (!names.contains(packageName)) {
						return DLTKPluginImages
								.get(DLTKPluginImages.IMG_OBJS_ERROR);
					}
				}
			}

			return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_PACKAGE);
		}

		@Override
		public String getText(Object element) {
			if (element instanceof String) {
				final String pkg = (String) element;
				if (isAutoPackage(pkg)) {
					return pkg + " [auto]";
				}
				return pkg;
			}
			return super.getText(element);
		}

		protected boolean isAutoPackage(final String pkg) {
			return false;
		}

	}

	private static class PackagesContentProvider implements
			ITreeContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		@SuppressWarnings("unchecked")
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Set) {
				return getElements(parentElement);
			}
			return CharOperation.NO_STRINGS;
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object element) {
			return false;
		}

		@SuppressWarnings("unchecked")
		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Set) {
				return ((Set) inputElement).toArray();
			}
			return CharOperation.NO_STRINGS;
		}
	}

	private TreeViewer fElements;
	private IScriptProject scriptProject;
	private Button addButton;
	private Button addAllButton;
	private Button remove;

	@Override
	public void createControl(Composite ancestor) {
		super.createControl(ancestor);
		// use Composite created in super to place additional controls.
		final Composite mainComposite = (Composite) getControl();
		Composite composite = new Composite(mainComposite, SWT.NONE);
		GridData compositeData = new GridData(SWT.FILL, SWT.FILL, true, true);
		compositeData.horizontalSpan = ((GridLayout) mainComposite.getLayout()).numColumns;
		composite.setLayoutData(compositeData);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.marginWidth = 0;
		composite.setLayout(gridLayout);

		this.fElements = new TreeViewer(composite);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = new PixelConverter(ancestor)
				.convertHeightInCharsToPixels(8);

		this.fElements.getTree().setLayoutData(data);

		Composite buttons = new Composite(composite, SWT.NONE);
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));

		GridLayout buttonsLayout = new GridLayout(1, true);
		buttonsLayout.marginLeft = buttonsLayout.marginWidth;
		buttonsLayout.marginWidth = 0;
		buttons.setLayout(buttonsLayout);

		final boolean editablePackages = getContext().getMode() == IInterpreterComboBlockContext.M_BUILDPATH;

		addButton = new Button(buttons, SWT.PUSH);
		addButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		addButton.setText("Add");
		if (editablePackages) {
			addButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					addPackage();
				}
			});
		} else {
			addButton.setEnabled(false);
		}
		addAllButton = new Button(buttons, SWT.PUSH);
		addAllButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				false));
		addAllButton.setText("Add all");
		if (editablePackages) {
			addAllButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					addAllPackages();
				}
			});
		} else {
			addAllButton.setEnabled(false);
		}
		remove = new Button(buttons, SWT.PUSH);
		remove.setText("Remove");
		remove.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		if (editablePackages) {
			remove.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					removePackage();
				}
			});
			remove.setEnabled(false); // disable initially
		} else {
			remove.setEnabled(false);
		}

		// setTitle("Packages");
		// setMessage("Package dependencies list");
		// this.setDescription("Package dependencies list");

		this.fElements.setContentProvider(new PackagesContentProvider());
		this.fElements.setLabelProvider(new PackagesLabelProvider() {
			@Override
			protected boolean isAutoPackage(String pkg) {
				return !packages.contains(pkg) && autoPackages.contains(pkg);
			}
		});
		if (editablePackages) {
			this.fElements
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							remove.setEnabled(canRemove(event.getSelection()));
						}
					});
		}
		this.fElements.setComparator(new ViewerComparator());
		showPackages();

		this.addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PROPERTY_INTERPRETER)) {
					refreshView();
				}
			}
		});
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void showPackages() {
		if (fElements != null) {
			fElements.setInput(new CombinedSet<String>(this.packages,
					this.autoPackages));
		}
	}

	protected boolean canRemove(ISelection selection) {
		if (!selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				for (@SuppressWarnings("unchecked")
				Iterator<String> iterator = ss.iterator(); iterator.hasNext();) {
					final String pkg = iterator.next();
					if (!this.packages.contains(pkg)) {
						return false;
					}
				}
				return true;
			}
		}
		return false;
	}

	protected void removePackage() {
		final ISelection selection = this.fElements.getSelection();
		if (selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			int updates = 0;
			for (@SuppressWarnings("unchecked")
			Iterator<String> iterator = ss.iterator(); iterator.hasNext();) {
				final String pkg = iterator.next();
				if (this.packages.remove(pkg)) {
					++updates;
				}
			}
			if (updates != 0) {
				refreshView();
			}
		}
	}

	private void refreshView() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fElements.getControl().isDisposed())
					return;
				fElements.refresh();
			}
		});
	}

	protected void addPackage() {
		IInterpreterInstall install = this.getInterpreter();
		if (install == null) {
			install = ScriptRuntime
					.getDefaultInterpreterInstall(new DefaultInterpreterEntry(
							TclNature.NATURE_ID,
							LocalEnvironment.ENVIRONMENT_ID));
		}
		if (install != null) {
			Set<String> packages = TclPackagesManager
					.getPackageInfosAsString(install);
			final List<String> names = new ArrayList<String>();
			names.addAll(packages);
			Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
			ListDialog dialog = new ListDialog(this.fElements.getControl()
					.getShell());
			dialog.setTitle("Add Packages");
			dialog.setContentProvider(new IStructuredContentProvider() {
				public Object[] getElements(Object inputElement) {
					return names.toArray();
				}

				public void dispose() {
				}

				public void inputChanged(Viewer viewer, Object oldInput,
						Object newInput) {
				}
			});
			dialog.setLabelProvider(new PackagesLabelProvider());
			dialog.setInput(names);
			if (dialog.open() == ListDialog.OK) {
				Object[] result = dialog.getResult();
				for (int i = 0; i < result.length; i++) {
					String pkg = (String) result[i];
					this.packages.add(pkg);
				}
				refreshView();
			}
		} else {
			MessageBox box = new MessageBox(this.fElements.getControl()
					.getShell(), SWT.OK | SWT.ICON_WARNING
					| SWT.APPLICATION_MODAL);
			box.setText("Packages");
			box.setMessage("Project interpreter could not be found...");
			box.open();
		}
	}

	protected void addAllPackages() {
		IInterpreterInstall install = null;
		try {
			install = ScriptRuntime.getInterpreterInstall(this.scriptProject);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		if (install != null) {
			Set<String> packages = TclPackagesManager
					.getPackageInfosAsString(install);
			this.packages.addAll(packages);

			refreshView();
		} else {
			MessageBox box = new MessageBox(this.fElements.getControl()
					.getShell(), SWT.OK | SWT.ICON_INFORMATION
					| SWT.APPLICATION_MODAL);
			box.setText("Packages");
			box.setText("Project interpreter could not be found...");
			box.open();
		}
	}

	/**
	 * @since 2.0
	 */
	public void initialize(IScriptProject project) {
		this.scriptProject = project;
		final Set<String> set = new HashSet<String>();
		final Set<String> autoSet = new HashSet<String>();
		InterpreterContainerHelper.getInterpreterContainerDependencies(project,
				set, autoSet);
		this.packages.clear();
		this.packages.addAll(set);
		this.autoPackages.clear();
		this.autoPackages.addAll(autoSet);
		showPackages();
	}

	@Override
	public IBuildpathEntry getEntry() {
		IBuildpathEntry createPackagesContainer = InterpreterContainerHelper
				.createPackagesContainer(this.packages, this.autoPackages,
						getInterpreterPath());
		return createPackagesContainer;
	}
}
