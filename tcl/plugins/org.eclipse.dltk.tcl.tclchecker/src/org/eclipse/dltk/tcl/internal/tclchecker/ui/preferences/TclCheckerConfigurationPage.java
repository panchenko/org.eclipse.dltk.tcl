/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.core.internal.environment.LocalEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerHelper;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblemDescription;
import org.eclipse.dltk.ui.environment.EnvironmentPathBlock;
import org.eclipse.dltk.ui.environment.IEnvironmentPathBlockListener;
import org.eclipse.dltk.ui.environment.IEnvironmentUI;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.dltk.validators.ui.ValidatorConfigurationPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;

public class TclCheckerConfigurationPage extends ValidatorConfigurationPage {

	private static final String[] processTypes = new String[] {
			PreferencesMessages.TclChecker_processType_default,
			PreferencesMessages.TclChecker_processType_suppress,
			PreferencesMessages.TclChecker_processType_check };

	EnvironmentPathBlock environmentPathBlock;
	private Map pcxPaths;

	private Button errorsMode;
	private Button errorsAndUsageWarningsMode;
	private Button allMode;

	private Table problemsTable;
	private TableViewer problemsTableViewer;

	private String message = "";
	private int messageType = IStatus.OK;

	private ListViewer lview;
	private Button noPCX;

	private SelectionAdapter noPCXSelectionListener;

	private Map noPCXValues;

	private Group pcxGroup;
	private Button pcxAdd;
	private Button pcxBrowse;
	private Button pcxRemove;

	private Combo comboVersion;

	public void createControl(Composite parent, int columns) {
		Composite c = new Composite(parent, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = columns;
		c.setLayoutData(gd);
		c.setLayout(new FillLayout());
		createContents(c);
	}

	public IStatus getStatus() {
		return new Status(messageType, TclCheckerPlugin.PLUGIN_ID, message);
	}

	protected void setModeSelection(int mode) {
		errorsMode.setSelection(mode == TclCheckerConstants.MODE_ERRORS);
		errorsAndUsageWarningsMode
				.setSelection(mode == TclCheckerConstants.MODE_ERRORS_AND_USAGE_WARNINGS);
		allMode.setSelection(mode == TclCheckerConstants.MODE_ALL);
	}

	protected int getModeSelection() {
		if (errorsMode.getSelection()) {
			return TclCheckerConstants.MODE_ERRORS;
		} else if (errorsAndUsageWarningsMode.getSelection()) {
			return TclCheckerConstants.MODE_ERRORS_AND_USAGE_WARNINGS;
		} else if (allMode.getSelection()) {
			return TclCheckerConstants.MODE_ALL;
		}

		return -1;
	}

	/**
	 * Check only for local environment.
	 */
	protected void validateTclCheckerPath() {
		Map envs = environmentPathBlock.getPaths();
		for (Iterator it = envs.keySet().iterator(); it.hasNext();) {
			IEnvironment env = (IEnvironment) it.next();
			if (!env.getId().equals(LocalEnvironment.ENVIRONMENT_ID)) {
				continue;
			}

			String txtPath = envs.get(env).toString();
			txtPath = txtPath.trim();

			if ("".equals(txtPath)) {
				/*
				 * setMessage(PreferencesMessages.TclChecker_pcxPath, env,
				 * PreferencesMessages.TclChecker_path_isempty, IStatus.INFO);
				 */
				continue;
			}

			IPath path = Path.fromPortableString(txtPath);
			IFileHandle file = PlatformFileUtils
					.findAbsoluteOrEclipseRelativeFile(env, path);

			if (file == null) {
				setMessage(PreferencesMessages.TclChecker_pcxPath, env,
						PreferencesMessages.TclChecker_path_isinvalid,
						IStatus.ERROR);
				continue;
			} else if (!file.isFile()) {
				setMessage(PreferencesMessages.TclChecker_pcxPath, env,
						PreferencesMessages.TclChecker_path_notexists,
						IStatus.ERROR);
				continue;
			} else if (!file.exists()) {
				setMessage(PreferencesMessages.TclChecker_pcxPath, env,
						PreferencesMessages.TclChecker_path_notexists,
						IStatus.ERROR);
				continue;
			} /*
			 * else if (txtPath.indexOf("tclchecker") == -1) {
			 * setMessage(PreferencesMessages.TclChecker_pcxPath, env,
			 * PreferencesMessages.TclChecker_path_notlookslike, IStatus.INFO);
			 * continue; }
			 */
		}
	}

	/**
	 * Validation only for local file system
	 */
	protected void validatePCXTclCheckerPath() {
		if (this.noPCX.getSelection()) {
			return;
		}
		Collection envs = pcxPaths.keySet();
		for (Iterator it = envs.iterator(); it.hasNext();) {
			IEnvironment env = (IEnvironment) it.next();
			if (!env.getId().equals(LocalEnvironment.ENVIRONMENT_ID)) {
				continue;
			}
			if (Boolean.valueOf((String) noPCXValues.get(env)).booleanValue()) {
				continue;
			}

			List txtPaths = (List) pcxPaths.get(env);
			for (int index = 0; index < txtPaths.size(); index++) {
				String txtPath = txtPaths.get(index).toString();
				txtPath = txtPath.trim();

				if ("".equals(txtPath)) {
					setMessage(PreferencesMessages.TclChecker_pcxPath, env,
							PreferencesMessages.TclChecker_path_isempty,
							IStatus.INFO);
					continue;
				}

				IPath path = Path.fromPortableString(txtPath);
				IFileHandle file = PlatformFileUtils
						.findAbsoluteOrEclipseRelativeFile(env, path);

				if (file == null) {
					setMessage(PreferencesMessages.TclChecker_pcxPath, env,
							PreferencesMessages.TclChecker_path_isinvalid + ":"
									+ path.toOSString(), IStatus.WARNING);
					continue;
				} else if (!file.isDirectory()) {
					setMessage(PreferencesMessages.TclChecker_pcxPath, env,
							PreferencesMessages.TclChecker_path_notexists + ":"
									+ path.toOSString(), IStatus.WARNING);
					continue;
				} else if (!file.exists()) {
					setMessage(PreferencesMessages.TclChecker_pcxPath, env,
							PreferencesMessages.TclChecker_path_notexists + ":"
									+ path.toOSString(), IStatus.WARNING);
					continue;
				}
			}
		}
	}

	private void setMessage(String group, IEnvironment env, String message,
			int type) {
		final String formattedMessage = MessageFormat.format(
				PreferencesMessages.TclChecker_path_msgPattern, new Object[] {
						group, env.getName(), message });
		setMessage(formattedMessage, type);
	}

	private void resetMessage() {
		this.message = "";
		this.messageType = IStatus.OK;
	}

	private void setMessage(String message, int type) {
		if (type > messageType) {
			this.message = message;
			this.messageType = type;
		}
	}

	protected void createModeGroup(Composite parent, Object data) {
		Group radioGroup = new Group(parent, SWT.NONE);
		radioGroup.setText(PreferencesMessages.TclChecker_mode);
		radioGroup.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		radioGroup.setLayout(layout);

		errorsMode = new Button(radioGroup, SWT.RADIO);
		errorsMode.setText(PreferencesMessages.TclChecker_mode_errors);

		errorsAndUsageWarningsMode = new Button(radioGroup, SWT.RADIO);
		errorsAndUsageWarningsMode
				.setText(PreferencesMessages.TclChecker_mode_errorsAndUsageWarnings);

		allMode = new Button(radioGroup, SWT.RADIO);
		allMode.setText(PreferencesMessages.TclChecker_mode_all);
	}

	protected void createPathGroup(final Composite parent, Object data) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(PreferencesMessages.TclChecker_path);
		group.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		group.setLayout(layout);
		GridData dt = new GridData(SWT.FILL, SWT.FILL, true, true);
		dt.horizontalSpan = 1;
		group.setLayoutData(dt);

		environmentPathBlock = new EnvironmentPathBlock();
		environmentPathBlock.createControl(group);
		environmentPathBlock.addListener(new IEnvironmentPathBlockListener() {
			public void valueChanged(Map paths) {
				validate();
			}
		});

		environmentPathBlock
				.addSelectionListener(new ISelectionChangedListener() {
					public void selectionChanged(SelectionChangedEvent event) {
						updatePCXGroup();
					}
				});
	}

	protected void editPDX() {

	}

	protected void createPCXPathGroup(final Composite parent, Object data) {
		pcxGroup = new Group(parent, SWT.NONE);
		pcxGroup.setText(PreferencesMessages.TclChecker_pcxPath);
		pcxGroup.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		pcxGroup.setLayout(layout);

		noPCX = new Button(pcxGroup, SWT.CHECK);
		noPCX.setText("Disable Using of PCX files");
		GridData noPCXDG = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		noPCXDG.horizontalSpan = 2;
		noPCX.setLayoutData(noPCXDG);

		final org.eclipse.swt.widgets.List list = new org.eclipse.swt.widgets.List(
				pcxGroup, SWT.BORDER);
		lview = new ListViewer(list);
		list.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		lview.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				IEnvironment environment = getEnvironment();
				if (inputElement instanceof Map) {
					if (environment != null) {
						return ((List) pcxPaths.get(environment)).toArray();
					}
				}
				return new Object[0];
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}
		});

		lview.setLabelProvider(new LabelProvider());

		Composite buttons = new Composite(pcxGroup, SWT.NONE);
		RowLayout row = new RowLayout(SWT.VERTICAL);
		row.fill = true;
		buttons.setLayout(row);
		pcxAdd = new Button(buttons, SWT.PUSH);
		pcxAdd.setText("Add");
		pcxAdd.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IEnvironment environment = getEnvironment();
				PathDialog pathDialog = new PathDialog(pcxAdd.getShell(),
						environment);
				if (pathDialog.open() == PathDialog.OK) {
					String path = pathDialog.getPath();
					if (path != null) {
						((List) pcxPaths.get(environment)).add(path);
					}
					updatePCX();
				}
			}
		});
		pcxBrowse = new Button(buttons, SWT.PUSH);
		pcxBrowse.setText("Browse...");
		pcxBrowse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IEnvironment environment = getEnvironment();
				IEnvironmentUI ui = (IEnvironmentUI) environment
						.getAdapter(IEnvironmentUI.class);
				String path = ui.selectFolder(pcxBrowse.getShell());
				if (path != null) {
					((List) pcxPaths.get(environment)).add(path);
				}
				updatePCX();
			}
		});
		pcxRemove = new Button(buttons, SWT.PUSH);
		pcxRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = lview.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) selection;
					for (Iterator i = ssel.iterator(); i.hasNext();) {
						String s = (String) i.next();
						IEnvironment environment = getEnvironment();
						((List) pcxPaths.get(environment)).remove(s);
					}
				}
				updatePCX();
			}
		});
		pcxRemove.setText("Remove");

		lview.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				ISelection selection = lview.getSelection();
				if (selection instanceof IStructuredSelection) {
					IStructuredSelection ssel = (IStructuredSelection) selection;
					boolean empty = ssel.isEmpty();
					pcxRemove.setEnabled(!empty);
				}
			}
		});
		noPCXSelectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				boolean selection = noPCX.getSelection();
				noPCXValues.put(getEnvironment(), new Boolean(selection)
						.toString());

				IStructuredSelection pathSelection = environmentPathBlock
						.getSelection();
				boolean enabled = !pathSelection.isEmpty();
				pcxAdd.setEnabled(!selection && enabled);
				pcxRemove.setEnabled(!selection && enabled);
				updatePCX();
			}
		};
		noPCX.addSelectionListener(noPCXSelectionListener);
	}

	protected IEnvironment getEnvironment() {
		IStructuredSelection selection = environmentPathBlock.getSelection();
		if (selection.isEmpty()) {
			return EnvironmentManager
					.getEnvironmentById(LocalEnvironment.ENVIRONMENT_ID);
		}
		return (IEnvironment) selection.getFirstElement();
	}

	protected void setProcessType(int type) {
		TableItem[] items = problemsTable.getItems();
		for (int i = 0; i < items.length; ++i) {
			ProblemItem item = (ProblemItem) items[i].getData();
			item.setProcessType(type);
		}
		problemsTableViewer.refresh();
	}

	protected void createSuppressProblemsGroup(Composite parent, Object data) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(PreferencesMessages.TclChecker_suppressProblems);
		group.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		group.setLayout(layout);

		// Table
		problemsTable = new Table(group, SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE
				| SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		problemsTable.setBounds(0, 0, 150, 200);
		problemsTable.setHeaderVisible(true);
		problemsTable.setLinesVisible(true);

		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true, 0, 0);
		tableData.heightHint = 100;
		problemsTable.setLayoutData(tableData);

		// Columns
		TableColumn problemsColumn = new TableColumn(problemsTable, SWT.LEFT);
		problemsColumn.setText(PreferencesMessages.TclChecker_problems_name);
		problemsColumn.setWidth(140);

		TableColumn typesColumn = new TableColumn(problemsTable, SWT.LEFT);
		typesColumn.setText(PreferencesMessages.TclChecker_problems_type);
		typesColumn.setWidth(70);

		TableColumn actionColumn = new TableColumn(problemsTable, SWT.LEFT);
		actionColumn.setText(PreferencesMessages.TclChecker_problems_action);
		actionColumn.setWidth(70);

		// TableViewer
		problemsTableViewer = new TableViewer(problemsTable);
		String[] propNames = new String[problemsTable.getColumnCount()];
		for (int index = 0; index < propNames.length; index++) {
			propNames[index] = Integer.toString(index);
		}
		problemsTableViewer.setColumnProperties(propNames);
		CellEditor[] editors = new CellEditor[problemsTable.getColumnCount()];
		editors[0] = null;
		editors[1] = null;
		editors[2] = new ComboBoxCellEditor(problemsTable, processTypes,
				SWT.READ_ONLY);
		problemsTableViewer.setCellEditors(editors);
		problemsTableViewer.setCellModifier(new ProblemsCellModifier(Arrays
				.asList(propNames)));

		// Items
		problemsTableViewer.setContentProvider(new ArrayContentProvider());
		problemsTableViewer.setLabelProvider(new ProblemsLabelProvider());

		List problems = TclCheckerProblemDescription.getProblemIdentifiers();
		Collections.sort(problems);
		problemsTableViewer.setInput(createProblemInput(problems));

		// Buttons composite
		Composite buttonsComposite = new Composite(group, SWT.NULL);
		RowLayout buttonsLayout = new RowLayout();
		buttonsLayout.type = SWT.VERTICAL;
		buttonsLayout.fill = true;
		buttonsComposite.setLayout(buttonsLayout);

		GridData buttonsData = new GridData();
		buttonsData.verticalAlignment = SWT.TOP;
		buttonsComposite.setLayoutData(buttonsData);

		Button defaultAll = new Button(buttonsComposite, SWT.PUSH);
		defaultAll
				.setText(PreferencesMessages.TclChecker_processType_defaultAll);
		defaultAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setProcessType(TclCheckerConstants.PROCESS_TYPE_DEFAULT);
			}
		});

		Button suppressAll = new Button(buttonsComposite, SWT.PUSH);
		suppressAll
				.setText(PreferencesMessages.TclChecker_processType_suppressAll);
		suppressAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setProcessType(TclCheckerConstants.PROCESS_TYPE_SUPPRESS);
			}
		});

		Button checkAll = new Button(buttonsComposite, SWT.PUSH);
		checkAll.setText(PreferencesMessages.TclChecker_processType_checkAll);
		checkAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setProcessType(TclCheckerConstants.PROCESS_TYPE_CHECK);
			}
		});
	}

	public Control createContents(Composite parent) {
		Composite top = new Composite(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		top.setLayout(layout);

		createVersionGroup(top, new GridData(GridData.FILL, SWT.NONE, true,
				false));
		createPathGroup(top, new GridData(GridData.FILL, SWT.NONE, true, false));
		Composite ctrl = new Composite(top, SWT.NONE);
		ctrl.setLayoutData(new GridData(GridData.FILL, SWT.NONE, true, false));
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginLeft = -5;
		gl.marginTop = -5;
		gl.marginRight = -5;
		ctrl.setLayout(gl);

		createModeGroup(ctrl, new GridData(SWT.DEFAULT, SWT.FILL, false, false));
		createPCXPathGroup(ctrl, new GridData(GridData.FILL, SWT.FILL, true,
				true));
		createSuppressProblemsGroup(top, new GridData(GridData.FILL,
				GridData.FILL, true, true));

		initializeValues();
		validate();

		return top;
	}

	/**
	 * @param top
	 * @param gridData
	 */
	private void createVersionGroup(Composite parent, GridData data) {
		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.makeColumnsEqualWidth = true;
		group.setLayout(layout);

		SWTFactory.createLabel(group, "Version", 1); //$NON-NLS-1$
		comboVersion = SWTFactory.createCombo(group, SWT.READ_ONLY, 1,
				new String[] { TclCheckerConstants.VERSION_4,
						TclCheckerConstants.VERSION_5 });
	}

	public void init(IWorkbench workbench) {

	}

	protected IPreferenceStore doGetPreferenceStore() {
		return TclCheckerPlugin.getDefault().getPreferenceStore();
	}

	public void initializeValues() {
		IPreferenceStore store = doGetPreferenceStore();

		// Version
		String version = store.getString(TclCheckerConstants.PREF_VERSION);
		if (TclCheckerConstants.VERSION_5.equals(version)) {
			comboVersion.select(1);
		} else {
			comboVersion.select(0);
		}

		// Path
		environmentPathBlock.setPaths(TclCheckerHelper.getPaths(store));
		this.pcxPaths = TclCheckerHelper.getPcxPaths(store);
		this.noPCXValues = TclCheckerHelper.getNoPCX(store);

		// noPCXSelectionListener.widgetSelected(null);

		lview.setInput(this.pcxPaths);
		updatePCXGroup();

		// Mode
		setModeSelection(store.getInt(TclCheckerConstants.PREF_MODE));

		// Problems
		TableItem[] items = problemsTable.getItems();
		for (int i = 0; i < items.length; ++i) {
			ProblemItem item = (ProblemItem) items[i].getData();
			item.setProcessType(loadProcessType(store, item.getProblemId()));
		}
		problemsTableViewer.refresh();
		// selectionChanged(null);
	}

	/**
	 * Use for compatibility with old preference store format.
	 * 
	 * @param store
	 * @return
	 */
	private int loadProcessType(IPreferenceStore store, String id) {
		String processType = store.getString(id);
		if ("true".equalsIgnoreCase(processType)) {
			return TclCheckerConstants.PROCESS_TYPE_SUPPRESS;
		} else if ("false".equalsIgnoreCase(processType)) {
			return TclCheckerConstants.PROCESS_TYPE_DEFAULT;
		}

		return store.getInt(id);
	}

	/**
	 * Get List<ProblemItem>
	 * 
	 * @param problems
	 * @return
	 */
	protected List createProblemInput(List problems) {
		List result = new ArrayList();
		for (int index = 0; index < problems.size(); index++) {
			String problem = (String) problems.get(index);
			result.add(new ProblemItem(problem,
					TclCheckerConstants.PROCESS_TYPE_DEFAULT));
		}
		return result;
	}

	protected void performDefaults() {
		setModeSelection(TclCheckerConstants.MODE_ALL);
		setProcessType(TclCheckerConstants.PROCESS_TYPE_DEFAULT);
	}

	public void applyChanges() {
		IPreferenceStore store = doGetPreferenceStore();

		// version
		final int versionIndex = comboVersion.getSelectionIndex();
		if (versionIndex >= 0 && versionIndex < comboVersion.getItemCount()) {
			store.setValue(TclCheckerConstants.PREF_VERSION, comboVersion
					.getItem(versionIndex));
		}

		// Path
		TclCheckerHelper.setPaths(store, environmentPathBlock.getPaths());
		TclCheckerHelper.setPcxPaths(store, pcxPaths);
		TclCheckerHelper.setNoPCX(store, noPCXValues);

		// Mode
		store.setValue(TclCheckerConstants.PREF_MODE, getModeSelection());

		// Problems
		TableItem[] items = problemsTable.getItems();
		for (int i = 0; i < items.length; ++i) {
			ProblemItem item = (ProblemItem) items[i].getData();
			store.setValue(item.getProblemId(), item.getProcessType());
		}
	}

	private void updatePCXGroup() {
		boolean isNoPCXChecked = Boolean.valueOf(
				(String) noPCXValues.get(getEnvironment())).booleanValue();
		noPCX.setSelection(isNoPCXChecked);
		pcxAdd.setEnabled(!isNoPCXChecked);
		pcxRemove.setEnabled(!isNoPCXChecked);

		updatePCX();
	}

	private void updatePCX() {
		Boolean value = Boolean.valueOf((String) noPCXValues
				.get(getEnvironment()));
		lview.getControl().setEnabled(!value.booleanValue());
		lview.refresh();
		validate();
	}

	protected void validate() {
		resetMessage();
		validateTclCheckerPath();
		validatePCXTclCheckerPath();

		updateStatus();
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Check/Suppress problem table item.
	//
	// ////////////////////////////////////////////////////////////////////////
	private static class ProblemItem {
		private String problemId;
		private int processType;

		public ProblemItem(String problemId, int processType) {
			super();
			this.problemId = problemId;
			this.processType = processType;
		}

		public String getProblemId() {
			return problemId;
		}

		public int getProcessType() {
			return processType;
		}

		public void setProcessType(int processType) {
			this.processType = processType;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Label provider for Check/Suppress problems table.
	//
	// ////////////////////////////////////////////////////////////////////////
	private static class ProblemsLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			ProblemItem problemItem = (ProblemItem) element;
			switch (columnIndex) {
			case 0:
				return problemItem.getProblemId();
			case 1:
				return getType(problemItem.getProblemId());
			case 2:
				return processTypes[problemItem.getProcessType()];
			}
			return null;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		private String getType(String problemId) {
			String type = TclCheckerProblemDescription
					.getProblemType(problemId);
			int category = TclCheckerProblemDescription
					.matchProblemCategory(type);

			if (TclCheckerProblemDescription.isError(category)) {
				return PreferencesMessages.TclChecker_error;
			} else if (TclCheckerProblemDescription.isWarning(category)) {
				return PreferencesMessages.TclChecker_warning;
			}
			return null;
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	//
	// Cell modifier for Check/Suppress problems table.
	//
	// ////////////////////////////////////////////////////////////////////////
	public class ProblemsCellModifier implements ICellModifier {

		/**
		 * List<String>
		 */
		private List properties;

		public ProblemsCellModifier(List properties) {
			super();
			this.properties = properties;
		}

		public boolean canModify(Object element, String property) {
			int columnIndex = properties.indexOf(property);
			return (columnIndex == 2);
		}

		/**
		 * Only for columnIndex == 2
		 */
		public Object getValue(Object element, String property) {
			ProblemItem item = (ProblemItem) element;
			return new Integer(item.getProcessType());
		}

		/**
		 * Only for columnIndex == 2
		 */
		public void modify(Object element, String property, Object value) {
			ProblemItem item = (ProblemItem) ((TableItem) element).getData();
			item.setProcessType(((Number) value).intValue());
			problemsTableViewer.update(item, null);
		}
	}
}
