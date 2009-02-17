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

import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.ui.environment.EnvironmentContainer;
import org.eclipse.dltk.ui.environment.IEnvironmentUI;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TclCheckerInstanceDialog extends StatusDialog {

	private final CheckerInstance instance;
	private final EnvironmentContainer environments;
	private final List<CheckerConfig> configs;

	/**
	 * @param parent
	 * @param instance
	 */
	public TclCheckerInstanceDialog(Shell parent,
			EnvironmentContainer environments, List<CheckerConfig> configs,
			CheckerInstance instance) {
		super(parent);
		this.environments = environments;
		this.configs = configs;
		this.instance = instance;
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}

	private Combo versionField;
	private Text environmentField;
	private Text pathField;
	private Text cliField;
	private Button noPCX;
	private ListViewer pcxList;
	private Button pcxAdd;
	private Button pcxBrowse;
	private Button pcxRemove;
	private Button automaticField;
	private Combo configField;

	protected IEnvironment getEnvironment() {
		return environments.get(instance.getEnvironmentId());
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite dialogArea = (Composite) super.createDialogArea(parent);
		final Composite content = new Composite(dialogArea, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		final GridLayout contentLayout = new GridLayout(3, false);
		contentLayout.marginWidth = 0;
		contentLayout.marginHeight = 0;
		content.setLayout(contentLayout);
		createEnvironment(content);
		createPath(content);
		createVersion(content);
		createCommandLineOptions(content);
		createAuto(content);
		createConfig(content);
		createPCXGroup(content);
		return dialogArea;
	}

	private void createEnvironment(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_Environment, 1);
		environmentField = SWTFactory.createText(parent, SWT.BORDER
				| SWT.READ_ONLY, 2, Util.EMPTY_STRING);
	}

	/**
	 * @param content
	 */
	private void createVersion(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_Version, 1);
		versionField = SWTFactory.createCombo(parent, SWT.BORDER
				| SWT.READ_ONLY, 2, new String[] {
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
				updateStatus();
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
					final String path = ui.selectFile(getShell(),
							IEnvironmentUI.EXECUTABLE);
					if (path != null) {
						pathField.setText(path);
						updateStatus();
					}
				}
			}
		});
	}

	private void createCommandLineOptions(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_CommandLineOptions, 1);
		cliField = SWTFactory.createText(parent, SWT.BORDER, 2,
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
				updatePcxGroupEnablement(noPCX.getSelection(), pcxList
						.getSelection());
				updateStatus();
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
				final PathDialog pathDialog = new PathDialog(pcxAdd.getShell(),
						getEnvironment());
				pathDialog
						.setTitle(Messages.TclCheckerInstanceDialog_pcxAddTitle);
				if (pathDialog.open() == Window.OK) {
					final String path = pathDialog.getPath();
					if (path != null) {
						instance.getPcxFileFolders().add(path);
						pcxList.add(path);
						updateStatus();
					}
				}
			}
		});
		pcxBrowse = SWTFactory.createPushButtonNoLayoutData(buttons,
				Messages.TclCheckerInstanceDialog_pcxBrowse);
		pcxBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final IEnvironment environment = getEnvironment();
				if (environment != null) {
					final IEnvironmentUI ui = (IEnvironmentUI) environment
							.getAdapter(IEnvironmentUI.class);
					final String path = ui.selectFolder(pcxBrowse.getShell());
					if (path != null) {
						instance.getPcxFileFolders().add(path);
						pcxList.add(path);
						updateStatus();
					}
				}
			}
		});
		pcxRemove = SWTFactory.createPushButtonNoLayoutData(buttons,
				Messages.TclCheckerInstanceDialog_pcxRemove);
		pcxRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final ISelection selection = pcxList.getSelection();
				if (selection instanceof IStructuredSelection) {
					for (Iterator<?> i = ((IStructuredSelection) selection)
							.iterator(); i.hasNext();) {
						final String path = (String) i.next();
						instance.getPcxFileFolders().remove(path);
						pcxList.remove(path);
						updateStatus();
					}
				}
			}
		});
	}

	private void createAuto(Composite parent) {
		SWTFactory.createHorizontalSpacer(parent, 1);
		automaticField = SWTFactory.createCheckButton(parent,
				Messages.TclCheckerInstanceDialog_automaticCheckbox, 2);
	}

	private void createConfig(Composite parent) {
		SWTFactory.createLabel(parent,
				Messages.TclCheckerInstanceDialog_Configuration, 1);
		String[] configNames = new String[configs.size() + 1];
		configNames[0] = Messages.TclCheckerInstanceDialog_None;
		for (int i = 0; i < configs.size(); ++i) {
			configNames[i + 1] = configs.get(i).getName();
		}
		configField = SWTFactory.createCombo(parent,
				SWT.BORDER | SWT.READ_ONLY, 2, configNames);
	}

	/**
	 * 
	 */
	protected void updateStatus() {
		// TODO Auto-generated method stub

	}

	private void updatePcxGroupEnablement(boolean noPCX, ISelection selection) {
		pcxAdd.setEnabled(!noPCX);
		pcxBrowse.setEnabled(!noPCX);
		pcxRemove.setEnabled(!noPCX && !selection.isEmpty());
		pcxList.getControl().setEnabled(!noPCX);
	}

	@Override
	public void create() {
		super.create();
		environmentField.setText(environments.getName(instance
				.getEnvironmentId()));
		pathField.setText(instance.getExecutablePath() != null ? instance
				.getExecutablePath() : Util.EMPTY_STRING);
		if (instance.getVersion() == CheckerVersion.VERSION5) {
			versionField.select(1);
		} else {
			versionField.select(0);
		}
		cliField.setText(instance.getCommandLineOptions() != null ? instance
				.getCommandLineOptions() : Util.EMPTY_STRING);
		pcxList.setInput(instance.getPcxFileFolders());
		noPCX.setSelection(!instance.isUsePcxFiles());
		automaticField.setSelection(instance.isAutomatic());
		int configIndex = configs.indexOf(instance.getConfiguration());
		configField.select(configIndex >= 0 ? configIndex + 1 : 0);
		updatePcxGroupEnablement(!instance.isUsePcxFiles(), pcxList
				.getSelection());
	}

	@Override
	protected void okPressed() {
		instance.setExecutablePath(pathField.getText());
		instance
				.setVersion(versionField.getSelectionIndex() == 1 ? CheckerVersion.VERSION5
						: CheckerVersion.VERSION4);
		instance.setCommandLineOptions(cliField.getText());
		instance.setUsePcxFiles(!noPCX.getSelection());
		instance.setAutomatic(automaticField.getSelection());
		int configIndex = configField.getSelectionIndex();
		instance.setConfiguration(configIndex > 0 ? configs
				.get(configIndex - 1) : null);
		super.okPressed();
	}
}
