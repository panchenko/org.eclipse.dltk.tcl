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
import org.eclipse.dltk.compiler.util.Util;
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
import org.eclipse.jface.viewers.StructuredSelection;
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
			if (element instanceof Package) {
				String packageName = ((Package) element).name;
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
				return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_PACKAGE);
			} else if (element instanceof PackageCategory) {
				return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_LIBRARY);
			} else {
				return null;
			}
		}

		@Override
		public String getText(Object element) {
			if (element instanceof Package) {
				return ((Package) element).name;
			} else if (element instanceof PackageCategory) {
				return ((PackageCategory) element).category;
			}
			return super.getText(element);
		}

	}

	private static class PackageComparator extends ViewerComparator {

		@Override
		public int category(Object element) {
			if (element instanceof PackageCategory) {
				final PackageCategory category = (PackageCategory) element;
				return category.readOnly ? 2 : 1;
			}
			return super.category(element);
		}
	}

	private static class PackagesContentProvider implements
			ITreeContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof PackageCategory) {
				return ((PackageCategory) parentElement).getPackages();
			} else {
				return Util.EMPTY_ARRAY;
			}
		}

		public Object getParent(Object element) {
			if (element instanceof Package) {
				return ((Package) element).parent;
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			if (element instanceof PackageCategory) {
				return !((PackageCategory) element).packages.isEmpty();
			}
			return false;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof PackageInput) {
				return ((PackageInput) inputElement).getCategories();
			}
			return Util.EMPTY_ARRAY;
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
		addButton
				.setText(TclInterpreterMessages.TclInterpreterComboBlock_buttonAdd);
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
		addAllButton
				.setText(TclInterpreterMessages.TclInterpreterComboBlock_buttonAddAll);
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
		remove
				.setText(TclInterpreterMessages.TclInterpreterComboBlock_buttonRemove);
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

		this.fElements.setContentProvider(new PackagesContentProvider());
		this.fElements.setLabelProvider(new PackagesLabelProvider());
		if (editablePackages) {
			this.fElements
					.addSelectionChangedListener(new ISelectionChangedListener() {
						public void selectionChanged(SelectionChangedEvent event) {
							remove.setEnabled(canRemove(event.getSelection()));
						}
					});
		}
		this.fElements.setComparator(new PackageComparator());
		showPackages();

		this.addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PROPERTY_INTERPRETER)) {
					refreshView();
				}
			}
		});
	}

	private void showPackages() {
		if (fElements != null) {
			PackageInput input = new PackageInput();
			input
					.addCategory(
							TclInterpreterMessages.TclInterpreterComboBlock_CategoryManual,
							this.packages, false);
			input
					.addCategory(
							TclInterpreterMessages.TclInterpreterComboBlock_CategoryAutomatic,
							this.autoPackages, true);
			fElements.setInput(input);
			fElements.expandToLevel(2);
		}
	}

	private static class PackageInput {
		private final List<PackageCategory> categories = new ArrayList<PackageCategory>();

		public void addCategory(String category, Set<String> packagesSet,
				boolean readOnly) {
			categories
					.add(new PackageCategory(category, packagesSet, readOnly));
		}

		public PackageCategory[] getCategories() {
			return categories.toArray(new PackageCategory[categories.size()]);
		}

		/**
		 * @param categoryName
		 * @return
		 */
		public PackageCategory get(String categoryName) {
			for (PackageCategory category : categories) {
				if (categoryName.equals(category.category)) {
					return category;
				}
			}
			throw new IllegalArgumentException(categoryName);
		}
	}

	private static class PackageCategory {
		final String category;
		final Set<String> packages;
		final boolean readOnly;

		public PackageCategory(String category, Set<String> packagesSet,
				boolean readOnly) {
			this.category = category;
			this.readOnly = readOnly;
			this.packages = packagesSet;
		}

		public Package[] getPackages() {
			final Package[] result = new Package[packages.size()];
			int index = 0;
			for (String packageName : this.packages) {
				result[index++] = new Package(this, packageName);
			}
			return result;
		}

		@Override
		public int hashCode() {
			return category.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PackageCategory) {
				PackageCategory other = (PackageCategory) obj;
				return category.equals(other.category);
			}
			return false;
		}

	}

	private static class Package {
		final PackageCategory parent;
		final String name;

		public Package(PackageCategory parent, String name) {
			this.parent = parent;
			this.name = name;
		}

		@Override
		public int hashCode() {
			return name.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof Package) {
				final Package other = (Package) obj;
				return name.equals(other.name) && parent.equals(other.parent);
			}
			return false;
		}
	}

	/**
	 * @since 2.0
	 */
	protected boolean canRemove(ISelection selection) {
		if (!selection.isEmpty()) {
			if (selection instanceof IStructuredSelection) {
				final IStructuredSelection ss = (IStructuredSelection) selection;
				for (@SuppressWarnings("unchecked")
				Iterator iterator = ss.iterator(); iterator.hasNext();) {
					final Object pkg = iterator.next();
					if (pkg instanceof Package) {
						if (((Package) pkg).parent.readOnly) {
							return false;
						}
						if (!this.packages.contains(((Package) pkg).name)) {
							return false;
						}
					} else {
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
				final Object pkg = iterator.next();
				if (pkg instanceof Package && !((Package) pkg).parent.readOnly) {
					if (this.packages.remove(((Package) pkg).name)) {
						++updates;
					}
				}
			}
			if (updates != 0) {
				refreshView();
			}
		}
	}

	private void refreshView() {
		refreshView(null);
	}

	private void refreshView(final String selection) {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (fElements.getControl().isDisposed())
					return;
				fElements.refresh();
				if (selection != null) {
					PackageCategory category = ((PackageInput) fElements
							.getInput())
							.get(TclInterpreterMessages.TclInterpreterComboBlock_CategoryManual);
					fElements.setSelection(new StructuredSelection(new Package(
							category, selection)));
				}
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
			dialog
					.setTitle(TclInterpreterMessages.TclInterpreterComboBlock_title);
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
				refreshView((String) (result.length != 0 ? result[0] : null));
			}
		} else {
			MessageBox box = new MessageBox(this.fElements.getControl()
					.getShell(), SWT.OK | SWT.ICON_WARNING
					| SWT.APPLICATION_MODAL);
			box
					.setText(TclInterpreterMessages.TclInterpreterComboBlock_errorTitle);
			box
					.setMessage(TclInterpreterMessages.TclInterpreterComboBlock_errorMessage);
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
			box
					.setText(TclInterpreterMessages.TclInterpreterComboBlock_errorTitle);
			box
					.setText(TclInterpreterMessages.TclInterpreterComboBlock_errorMessage);
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
