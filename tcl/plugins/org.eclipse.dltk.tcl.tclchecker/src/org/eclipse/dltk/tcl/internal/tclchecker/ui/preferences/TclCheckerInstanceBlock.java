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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.IEnvironmentPredicate;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.environment.EnvironmentContainer;
import org.eclipse.dltk.ui.environment.IEnvironmentUI;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

public class TclCheckerInstanceBlock extends AbstractValidatorEditBlock {

	private EnvironmentContainer environments;
	private IEnvironmentPredicate environmentPredicate;

	private CheckerInstance instance;
	private CheckerEnvironmentInstance environmentInstance;

	protected void doInit(IValidatorDialogContext context, Object object) {
		this.environments = context.getEnvironments();
		this.environmentPredicate = context.getEnvironmentPredicate();
		this.instance = (CheckerInstance) object;
	}

	private Text nameField;
	private Combo versionField;
	private TableViewer environmentField;
	private Text pathField;
	private Text cliField;
	private Button noPCX;
	private ListViewer pcxList;
	private Button pcxAdd;
	private Button pcxBrowse;
	private Button pcxRemove;

	public void createControl(Composite parent) {
		final GridLayout contentLayout = new GridLayout(2, false);
		contentLayout.marginWidth = 0;
		contentLayout.marginHeight = 0;
		parent.setLayout(contentLayout);

		final Composite nameComposite = new Composite(parent, SWT.NONE);
		final GridData nameLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		nameLayoutData.horizontalSpan = 2;
		nameComposite.setLayoutData(nameLayoutData);
		final GridLayout nameLayout = new GridLayout(2, false);
		nameLayout.marginWidth = 0;
		nameLayout.marginHeight = 0;
		nameComposite.setLayout(nameLayout);
		createName(nameComposite);
		createVersion(nameComposite);
		createCommandLineOptions(nameComposite);
		SWTFactory.createHorizontalSpacer(nameComposite, 2);

		final Composite hostList = new Composite(parent, SWT.NONE);
		final GridData hostListLayoutData = new GridData(GridData.FILL_VERTICAL);
		hostListLayoutData.widthHint = new PixelConverter(parent)
				.convertWidthInCharsToPixels(25);
		hostList.setLayoutData(hostListLayoutData);
		final GridLayout hostListLayout = new GridLayout(1, false);
		hostListLayout.marginWidth = 0;
		hostListLayout.marginHeight = 0;
		hostList.setLayout(hostListLayout);
		createEnvironment(hostList);

		final Composite right = new Composite(parent, SWT.NONE);
		right.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout rightLayout = new GridLayout(3, false);
		rightLayout.marginWidth = 0;
		rightLayout.marginHeight = 0;
		rightLayout.marginLeft = 5;
		right.setLayout(rightLayout);

		createPath(right);
		createPCXGroup(right);
	}

	private void createName(Composite parent) {
		SWTFactory.createLabel(parent, Messages.TclCheckerInstanceDialog_Name,
				1);
		nameField = SWTFactory.createText(parent, SWT.BORDER, 1,
				Util.EMPTY_STRING);
		nameField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
	}

	private void createEnvironment(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_Environment, 1);
		Composite environmentFieldParent = new Composite(parent, SWT.NONE);
		environmentFieldParent.setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumnLayout environmentTableLayout = new TableColumnLayout();
		environmentFieldParent.setLayout(environmentTableLayout);
		environmentField = new TableViewer(environmentFieldParent, SWT.BORDER);
		environmentTableLayout.setColumnData(new TableColumn(environmentField
				.getTable(), SWT.LEFT), new ColumnWeightData(100));
		environmentField.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof String) {
					return environments.getName((String) element);
				}
				return super.getText(element);
			}
		});
		environmentField.setContentProvider(new IStructuredContentProvider() {

			public void dispose() {
				// NOP
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				// NOP
			}

			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof String[]) {
					return (String[]) inputElement;
				}
				return new Object[0];
			}
		});
		environmentField
				.addSelectionChangedListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						if (isBusy())
							return;
						final ISelection selection = event.getSelection();
						if (selection != null && !selection.isEmpty()
								&& selection instanceof IStructuredSelection) {
							final IStructuredSelection ss = (IStructuredSelection) selection;
							if (ss.size() == 1) {
								environmentInstance = instance
										.getEnvironment((String) ss
												.getFirstElement());
								refreshEnvironmentFields();
								return;
							}
						}
						environmentInstance = null;
						refreshEnvironmentFields();
					}
				});
		environments.addChangeListener(new Runnable() {
			public void run() {
				++busy;
				try {
					final ISelection selection = environmentField
							.getSelection();
					final String[] ids = collectEnvironments();
					environmentField.setInput(ids);
					environmentField.setSelection(selection);
				} finally {
					--busy;
				}

			}
		});
	}

	private int busy = 0;

	private boolean isBusy() {
		return busy != 0;
	}

	private String[] collectEnvironments() {
		final List<String> ids = new ArrayList<String>();
		for (String envId : environments.getEnvironmentIds()) {
			if (environmentPredicate.evaluate(envId)) {
				ids.add(envId);
			}
		}
		for (CheckerEnvironmentInstance environmentInstance : instance
				.getEnvironments()) {
			final String envId = environmentInstance.getEnvironmentId();
			if (environmentPredicate.evaluate(envId) && !ids.contains(envId)) {
				ids.add(envId);
			}
		}
		return ids.toArray(new String[ids.size()]);
	}

	/**
	 * @param content
	 */
	private void createVersion(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_Version, 1);
		versionField = SWTFactory.createCombo(parent, SWT.BORDER
				| SWT.READ_ONLY, 1, new String[] {
				CheckerVersion.VERSION4.getDescription(),
				CheckerVersion.VERSION5.getDescription() });
	}

	private void createPath(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_ExecutablePath, 1);
		pathField = SWTFactory.createText(parent, SWT.BORDER, 1,
				Util.EMPTY_STRING, GridData.FILL_HORIZONTAL);
		((GridData) pathField.getLayoutData()).widthHint = new PixelConverter(
				parent).convertWidthInCharsToPixels(64);
		pathField.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (environmentInstance != null) {
					environmentInstance.setExecutablePath(pathField.getText());
				}
				validate();
			}
		});
		Button browse = SWTFactory.createPushButton(parent,
				Messages.TclCheckerInstanceDialog_Browse);
		browse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IEnvironment environment = getEnvironment();
				if (environment != null) {
					final IEnvironmentUI ui = (IEnvironmentUI) environment
							.getAdapter(IEnvironmentUI.class);
					final String path = ui.selectFile(getContext().getShell(),
							IEnvironmentUI.EXECUTABLE);
					if (path != null) {
						pathField.setText(path);
						if (environmentInstance != null) {
							environmentInstance.setExecutablePath(path);
						}
						validate();
					}
				}
			}
		});
	}

	private void createCommandLineOptions(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_CommandLineOptions, 1);
		cliField = SWTFactory.createText(parent, SWT.BORDER, 1,
				Util.EMPTY_STRING);
	}

	private void createPCXGroup(Composite parent) {
		Group pcxGroup = new Group(parent, SWT.NONE);
		pcxGroup.setText(Messages.TclChecker_pcxPath);
		GridData groupLayoutData = new GridData(GridData.FILL_BOTH);
		groupLayoutData.horizontalSpan = 3;
		pcxGroup.setLayoutData(groupLayoutData);
		pcxGroup.setLayout(new GridLayout(2, false));

		noPCX = SWTFactory.createCheckButton(pcxGroup,
				Messages.TclCheckerInstanceDialog_DisablePCX, 2);
		noPCX.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (environmentInstance != null) {
					environmentInstance.setUsePcxFiles(noPCX.getSelection());
				}
				updatePcxGroupEnablement(noPCX.getSelection(), pcxList
						.getSelection());
				validate();
			}
		});

		pcxList = new ListViewer(pcxGroup, SWT.BORDER);
		pcxList.getList().setLayoutData(new GridData(GridData.FILL_BOTH));
		pcxList.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof List) {
					return ((List<?>) inputElement).toArray();
				}
				return new Object[0];
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});
		pcxList.setLabelProvider(new LabelProvider());
		pcxList.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				updatePcxGroupEnablement(noPCX.getSelection(), event
						.getSelection());
			}
		});

		Composite buttons = new Composite(pcxGroup, SWT.NONE);
		RowLayout row = new RowLayout(SWT.VERTICAL);
		row.fill = true;
		buttons.setLayout(row);
		pcxAdd = SWTFactory.createPushButtonNoLayoutData(buttons,
				Messages.TclCheckerInstanceDialog_pcxAdd);
		pcxAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (environmentInstance == null) {
					return;
				}
				final PathDialog pathDialog = new PathDialog(pcxAdd.getShell(),
						getEnvironment());
				pathDialog
						.setTitle(Messages.TclCheckerInstanceDialog_pcxAddTitle);
				if (pathDialog.open() == Window.OK) {
					final String path = pathDialog.getPath();
					if (path != null) {
						environmentInstance.getPcxFileFolders().add(path);
						pcxList.add(path);
						validate();
					}
				}
			}
		});
		pcxBrowse = SWTFactory.createPushButtonNoLayoutData(buttons,
				Messages.TclCheckerInstanceDialog_pcxBrowse);
		pcxBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (environmentInstance == null) {
					return;
				}
				final IEnvironment environment = getEnvironment();
				if (environment != null) {
					final IEnvironmentUI ui = (IEnvironmentUI) environment
							.getAdapter(IEnvironmentUI.class);
					final String path = ui.selectFolder(pcxBrowse.getShell());
					if (path != null) {
						environmentInstance.getPcxFileFolders().add(path);
						pcxList.add(path);
						validate();
					}
				}
			}
		});
		pcxRemove = SWTFactory.createPushButtonNoLayoutData(buttons,
				Messages.TclCheckerInstanceDialog_pcxRemove);
		pcxRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (environmentInstance == null) {
					return;
				}
				final ISelection selection = pcxList.getSelection();
				if (selection instanceof IStructuredSelection) {
					for (Iterator<?> i = ((IStructuredSelection) selection)
							.iterator(); i.hasNext();) {
						final String path = (String) i.next();
						environmentInstance.getPcxFileFolders().remove(path);
						pcxList.remove(path);
						validate();
					}
				}
			}
		});
	}

	private void updatePcxGroupEnablement(boolean noPCX, ISelection selection) {
		pcxAdd.setEnabled(!noPCX);
		pcxBrowse.setEnabled(!noPCX);
		pcxRemove.setEnabled(!noPCX && !selection.isEmpty());
		pcxList.getControl().setEnabled(!noPCX);
	}

	private void refreshEnvironmentFields() {
		pathField.setEnabled(environmentInstance != null);
		pcxList.getList().setEnabled(environmentInstance != null);
		noPCX.setEnabled(environmentInstance != null);
		if (environmentInstance != null) {
			pathField.setText(StrUtils.toString(environmentInstance
					.getExecutablePath()));
			pcxList.setInput(environmentInstance.getPcxFileFolders());
			noPCX.setSelection(!environmentInstance.isUsePcxFiles());
			updatePcxGroupEnablement(!environmentInstance.isUsePcxFiles(),
					pcxList.getSelection());
		} else {
			updatePcxGroupEnablement(true, StructuredSelection.EMPTY);
		}
	}

	protected IEnvironment getEnvironment() {
		final ISelection selection = environmentField.getSelection();
		if (selection != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			final IStructuredSelection ss = (IStructuredSelection) selection;
			if (ss.size() == 1) {
				return environments.get((String) ss.getFirstElement());
			}
		}
		return null;
	}

	public void initControls() {
		nameField.setText(StrUtils.toString(instance.getName()));
		versionField.select(CheckerVersion.VALUES
				.indexOf(instance.getVersion()));
		cliField.setText(StrUtils.toString(instance.getCommandLineOptions()));
		++busy;
		try {
			final String[] ids = collectEnvironments();
			environmentField.setInput(ids);
			if (ids.length != 0) {
				environmentField.setSelection(new StructuredSelection(ids[0]));
				environmentInstance = instance.getEnvironment(ids[0]);
			} else {
				environmentField.setSelection(StructuredSelection.EMPTY);
				environmentInstance = null;
			}
		} finally {
			--busy;
		}
		refreshEnvironmentFields();
	}

	public IStatus isValid(Object hint) {
		final String name = nameField.getText();
		if (name == null || name.trim().length() == 0) {
			return new StatusInfo(IStatus.ERROR, "Enter validator name");
		}
		if (instance.getValidatorEnvironments().isEmpty()) {
			return new StatusInfo(IStatus.ERROR, "No environments configured");
		}
		final Map<ValidatorEnvironmentInstance, IStatus> environmentStatus = new HashMap<ValidatorEnvironmentInstance, IStatus>();
		int correctEnvironments = 0;
		for (ValidatorEnvironmentInstance eInstance : instance
				.getValidatorEnvironments()) {
			final IStatus status = validateEnvironment(eInstance);
			if (status != null) {
				environmentStatus.put(eInstance, status);
			} else {
				++correctEnvironments;
			}
		}
		if (correctEnvironments == 0) {
			return environmentStatus.get(environmentInstance);
		}
		return null;
	}

	private IStatus validateEnvironment(ValidatorEnvironmentInstance eInstance) {
		final String path = eInstance.getExecutablePath();
		if (path == null || path.trim().length() == 0) {
			return new StatusInfo(IStatus.ERROR, "Path not specified");
		}
		final IEnvironment environment = environments.get(eInstance
				.getEnvironmentId());
		if (environment != null && environment.isLocal()) {
			final Path pathObj = new Path(path);
			if (pathObj.isEmpty()) {
				return new StatusInfo(IStatus.ERROR, "Path is empty");
			}
			final IFileHandle file = PlatformFileUtils
					.findAbsoluteOrEclipseRelativeFile(environment, pathObj);
			if (file == null || !file.isFile()) {
				return new StatusInfo(IStatus.ERROR, NLS.bind(
						"File ''{0}'' not found", path));
			}
		}
		return null;
	}

	public void saveValues() {
		instance.setName(nameField.getText());
		int versionIndex = versionField.getSelectionIndex();
		instance.setVersion(CheckerVersion.values()[versionIndex]);
		instance.setCommandLineOptions(cliField.getText());
	}

}
