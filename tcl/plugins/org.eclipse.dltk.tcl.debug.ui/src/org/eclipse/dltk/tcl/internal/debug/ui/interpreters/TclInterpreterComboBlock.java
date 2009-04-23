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
import org.eclipse.dltk.debug.ui.launchConfigurations.IMainLaunchConfigurationTabListenerManager;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.launching.ScriptRuntime.DefaultInterpreterEntry;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.internal.packages.TclPackagesManager;
import org.eclipse.dltk.ui.DLTKPluginImages;
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
 * FIXME this class is used on Interpreter tab in Launch Configurations and
 * AbstractInterpreterContainerWizardPage. Configured packages are saved only on
 * the later on via getEntry() method. On Interpreter tab packages could be
 * modified but value is not saved anywhere.
 */
public class TclInterpreterComboBlock extends AbstractInterpreterComboBlock {
	protected TclInterpreterComboBlock(
			IMainLaunchConfigurationTabListenerManager tab) {
		super(tab);
	}

	private Set<String> packages = new HashSet<String>();

	public class PackagesLabelProvider extends LabelProvider {

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
						return DLTKPluginImages.DESC_OBJS_ERROR.createImage();
					}
				}
			}

			return DLTKPluginImages.get(DLTKPluginImages.IMG_OBJS_PACKAGE);
		}

		public String getText(Object element) {
			if (element instanceof String) {
				return (String) element;
			}
			return super.getText(element);
		}

	}

	private class PackagesContentProvider implements ITreeContentProvider {

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

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

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof Set) {
				return packages.toArray();
			}
			return CharOperation.NO_STRINGS;
		}
	}

	private TreeViewer fElements;
	private IScriptProject scriptProject;
	private Button addButton;

	protected void showInterpreterPreferencePage() {
		showPrefPage(TclInterpreterPreferencePage.PAGE_ID);
	}

	protected String getCurrentLanguageNature() {
		return TclNature.NATURE_ID;
	}

	public void createControl(Composite ancestor) {
		super.createControl(ancestor);
		// use Composite created in super to place additional controls.
		final Composite mainComposite = (Composite) getControl();
		Composite composite = new Composite(mainComposite, SWT.NONE);
		GridData compositeData = new GridData(GridData.FILL, SWT.FILL, true,
				true);
		compositeData.horizontalSpan = ((GridLayout) mainComposite.getLayout()).numColumns;
		composite.setLayoutData(compositeData);
		GridLayout gridLayout = new GridLayout(2, false);
		composite.setLayout(gridLayout);

		this.fElements = new TreeViewer(composite);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);

		this.fElements.getTree().setLayoutData(data);

		Composite buttons = new Composite(composite, SWT.NONE);
		GridData data2 = new GridData(SWT.FILL, SWT.FILL, false, false);
		buttons.setLayoutData(data2);

		GridLayout gridLayout2 = new GridLayout(1, true);
		buttons.setLayout(gridLayout2);

		addButton = new Button(buttons, SWT.PUSH);
		data2 = new GridData(SWT.FILL, SWT.FILL, false, false);
		addButton.setLayoutData(data2);
		addButton.setText("Add");
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				addPackage();
			}
		});
		final Button remove = new Button(buttons, SWT.PUSH);
		remove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				removePackage();
			}
		});
		remove.setText("Remove");
		remove.setLayoutData(data2);

		// setTitle("Packages");
		// setMessage("Package dependencies list");
		// this.setDescription("Package dependencies list");

		this.fElements.setContentProvider(new PackagesContentProvider());
		this.fElements.setLabelProvider(new PackagesLabelProvider());
		this.fElements.setInput(this.packages);
		this.fElements
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						ISelection selection = event.getSelection();
						if (selection instanceof IStructuredSelection) {
							IStructuredSelection sel = (IStructuredSelection) selection;
							remove.setEnabled(!sel.isEmpty());
						}
					}
				});
		this.fElements.setComparator(new ViewerComparator() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {

				if (e1 instanceof String && e2 instanceof String) {
					return ((String) e1).compareToIgnoreCase((String) e2);
				}
				return super.compare(viewer, e1, e2);
			}
		});
		remove.setEnabled(false);

		this.addPropertyChangeListener(new IPropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent event) {
				if (event.getProperty().equals(PROPERTY_INTERPRETER)) {
					refreshView();
				}
			}
		});
	}

	protected void removePackage() {
		ISelection selection = this.fElements.getSelection();
		if (selection instanceof IStructuredSelection) {
			IStructuredSelection sel = (IStructuredSelection) selection;
			boolean update = false;
			for (Iterator iterator = sel.iterator(); iterator.hasNext();) {
				String pkg = (String) iterator.next();
				boolean res = this.packages.remove(pkg);
				if (res) {
					update = res;
				}
			}
			if (update) {
				refreshView();
			}
		}
	}

	private void refreshView() {
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				fElements.refresh();
				IInterpreterInstall install = getInterpreter();
				if (install == null) {
					try {
						install = ScriptRuntime
								.getInterpreterInstall(scriptProject);
					} catch (CoreException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					}
				}
				addButton.setEnabled(install != null);
			}
		});
	}

	protected void addPackage() {
		IInterpreterInstall install = null;
		install = this.getInterpreter();
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
			Collections.sort(names);
			ListDialog dialog = new ListDialog(this.fElements.getControl()
					.getShell());
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

	public void initialize(IScriptProject project,
			IBuildpathEntry[] currentEntries) {
		this.scriptProject = project;
		Set set = InterpreterContainerHelper
				.getInterpreterContainerDependencies(project);
		this.packages.addAll(set);
	}

	public IBuildpathEntry getEntry() {
		IBuildpathEntry createPackagesContainer = InterpreterContainerHelper
				.createPackagesContainer(this.packages, getInterpreterPath());
		return createPackagesContainer;
	}
}
