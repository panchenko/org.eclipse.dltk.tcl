package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.CheckedListDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.IEnvironmentPredicate;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.SingleEnvironmentPredicate;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.dltk.ui.environment.EnvironmentContainer;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ColumnLayoutData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclCheckerPreferenceBlock extends AbstractOptionsBlock {

	private static final PreferenceKey KEY_CONFIGURATION = new PreferenceKey(
			TclCheckerPlugin.PLUGIN_ID, TclCheckerConstants.PREF_CONFIGURATION);

	private static final PreferenceKey[] KEYS = new PreferenceKey[] { KEY_CONFIGURATION };

	public TclCheckerPreferenceBlock(IStatusChangeListener context,
			IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, KEYS, container);
	}

	private class TclCheckerConfigurationListAdapter implements IListAdapter {

		private static final int IDX_ADD = 0;
		private static final int IDX_EDIT = 1;
		private static final int IDX_COPY = 2;
		private static final int IDX_REMOVE = 3;
		private static final int IDX_IMPORT = 5;
		private static final int IDX_EXPORT = 6;

		public void customButtonPressed(ListDialogField field, int index) {
			if (index == IDX_ADD) {
				doAdd(field);
			} else if (index == IDX_EDIT) {
				doEdit(field);
			} else if (index == IDX_REMOVE) {
				doRemove(field);
			} else if (index == IDX_EXPORT) {
				doExport(field);
			} else if (index == IDX_IMPORT) {
				doImport();
			}
		}

		private void doAdd(ListDialogField field) {
			final CheckerConfig instance = editConfiguration(null);
			if (instance != null) {
				resource.getContents().add(instance);
				saveResource();
				field.addElement(instance);
			}
		}

		private void doEdit(ListDialogField field) {
			List<?> selection = field.getSelectedElements();
			if (canEdit(selection)) {
				final CheckerConfig instance = (CheckerConfig) selection.get(0);
				if (editConfiguration(instance) != null) {
					saveResource();
					field.refresh();
				}
			}
		}

		private void doRemove(ListDialogField field) {
			List<?> selection = field.getSelectedElements();
			if (canRemove(selection)) {
				resource.getContents().removeAll(selection);
				saveResource();
				field.removeElements(selection);
			}
		}

		private static final String FILTER_EXTENSIONS = "*.xml"; //$NON-NLS-1$

		private void doExport(ListDialogField field) {
			final List<?> selection = field.getSelectedElements();
			if (selection.size() == 1) {
				final FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
				dialog.setText(Messages.TclChecker_export_Title);
				dialog.setOverwrite(true);
				dialog.setFilterExtensions(new String[] { FILTER_EXTENSIONS });
				final String exportPath = dialog.open();
				if (exportPath != null) {
					final Resource resource = new XMIResourceImpl();
					resource.getContents().add(
							EcoreUtil.copy((EObject) selection.get(0)));
					try {
						final FileWriter writer = new FileWriter(exportPath);
						try {
							resource.save(
									new URIConverter.WriteableOutputStream(
											writer,
											TclCheckerConfigUtils.ENCODING),
									null);
						} finally {
							try {
								writer.close();
							} catch (IOException e) {
								// ignore
							}
						}
					} catch (IOException e) {
						ErrorDialog.openError(getShell(),
								Messages.TclChecker_export_ErrorTitle, e
										.getMessage(), new Status(
										IStatus.ERROR,
										TclCheckerPlugin.PLUGIN_ID, e
												.getMessage(), e));
					}
				}
			}
		}

		private void doImport() {
			final FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
			dialog.setText(Messages.TclChecker_import_Title);
			dialog.setFilterExtensions(new String[] { FILTER_EXTENSIONS });
			final String importPath = dialog.open();
			if (importPath != null) {
				try {
					final Resource importResource = new XMIResourceImpl(URI
							.createFileURI(importPath));
					importResource.load(null);
					int importedCount = 0;
					for (EObject object : importResource.getContents()) {
						if (object instanceof CheckerConfig) {
							resource.getContents().add(EcoreUtil.copy(object));
							++importedCount;
						}
					}
					if (importedCount != 0) {
						initConfigurations();
					}
				} catch (Exception e) {
					ErrorDialog.openError(getShell(),
							Messages.TclChecker_import_ErrorTitle, e
									.getMessage(), new Status(IStatus.ERROR,
									TclCheckerPlugin.PLUGIN_ID, e.getMessage(),
									e));
				}
			}
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canEdit(List<?> selection) {
			return selection.size() == 1
					&& !((CheckerConfig) selection.get(0)).isReadOnly();
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canRemove(List<?> selection) {
			if (selection.isEmpty()) {
				return false;
			}
			for (Iterator<?> i = selection.iterator(); i.hasNext();) {
				CheckerConfig instance = (CheckerConfig) i.next();
				if (instance.isReadOnly()) {
					return false;
				}
			}
			return true;
		}

		public void doubleClicked(ListDialogField field) {
			customButtonPressed(field, IDX_EDIT);
		}

		public void selectionChanged(ListDialogField field) {
			List<?> selection = field.getSelectedElements();
			field.enableButton(IDX_EDIT, canEdit(selection));
			field.enableButton(IDX_REMOVE, canRemove(selection));
			field.enableButton(IDX_COPY, false); // TODO
			field.enableButton(IDX_EXPORT, selection.size() == 1);
		}

	}

	private static class TclCheckerConfigurationLabelProvider extends
			LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof CheckerConfig) {
				final CheckerConfig config = (CheckerConfig) element;
				switch (columnIndex) {
				case 0:
					return config.getName();
				case 1:
					if (config.isReadOnly()) {
						return Messages.TclCheckerPreferenceBlock_BuiltIn;
					} else {
						return Messages.TclCheckerPreferenceBlock_UserDefined;
					}
				}
			}
			return Util.EMPTY_STRING;
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

	}

	private static class TclCheckerConfigurationViewerSorter extends
			ViewerSorter {

		private final int BUILTIN_CATEGORY = 0;
		private final int USER_CATEGORY = 1;

		@Override
		public int category(Object element) {
			if (element instanceof CheckerConfig) {
				return ((CheckerConfig) element).isReadOnly() ? BUILTIN_CATEGORY
						: USER_CATEGORY;
			}
			return super.category(element);
		}
	}

	private class TclCheckerInstanceListAdapter implements IListAdapter {

		private static final int IDX_ADD = 0;
		private static final int IDX_EDIT = 1;
		private static final int IDX_REMOVE = 2;

		public void customButtonPressed(ListDialogField field, int index) {
			switch (index) {
			case IDX_ADD:
				doAdd(field);
				break;
			case IDX_EDIT:
				doEdit(field.getSelectedElements());
				break;
			case IDX_REMOVE:
				doRemove(field.getSelectedElements());
				break;
			}
		}

		private void doAdd(ListDialogField field) {
			final CheckerInstance instance = editInstance(null);
			if (instance != null) {
				resource.getContents().add(instance);
				saveResource();
				field.addElement(instance);
			}
		}

		/**
		 * @param selection
		 */
		private void doEdit(List<?> selection) {
			if (canEdit(selection)) {
				final CheckerInstance instance = (CheckerInstance) selection
						.get(0);
				if (editInstance(instance) != null) {
					instanceField.setGrayedWithoutUpdate(instance,
							!canBeAutomatic(instance));
					instanceField.setChecked(instance, canBeAutomatic(instance)
							&& instance.isAutomatic());
					instanceField.getTableViewer().refresh();
					saveResource();
				}
			}
		}

		/**
		 * @param selection
		 */
		private void doRemove(List<?> selection) {
			resource.getContents().removeAll(selection);
			saveResource();
			instanceField.removeElements(selection);
		}

		public void doubleClicked(ListDialogField field) {
			customButtonPressed(field, IDX_EDIT);
		}

		public void selectionChanged(ListDialogField field) {
			final List<?> selection = field.getSelectedElements();
			field.enableButton(IDX_EDIT, canEdit(selection));
			field.enableButton(IDX_REMOVE, canRemove(selection));
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canRemove(List<?> selection) {
			return !selection.isEmpty();
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canEdit(List<?> selection) {
			return selection.size() == 1;
		}

	}

	private class TclCheckerInstanceLabelProvider extends LabelProvider
			implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) element;
				switch (columnIndex) {
				case 0:
					return environments.getName(instance.getEnvironmentId());
				}
			}
			return Util.EMPTY_STRING;
		}

		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

	}

	private class TclCheckerInstanceViewerSorter extends ViewerSorter {

		private static final int LOCAL_CATEGORY = -3;
		private static final int REMOTE_CATEGORY = -2;
		private static final int UNKNOWN_CATEGORY = -1;

		@Override
		public int category(Object element) {
			if (element instanceof CheckerInstance) {
				final IEnvironment environment = environments
						.get(((CheckerInstance) element).getEnvironmentId());
				if (environment != null) {
					return environment.isLocal() ? LOCAL_CATEGORY
							: REMOTE_CATEGORY;
				} else {
					return UNKNOWN_CATEGORY;
				}
			}
			return super.category(element);
		}
	}

	private class TclCheckerInstanceFieldListener implements
			ICheckStateListener {

		/*
		 * @see ICheckStateListener#checkStateChanged(CheckStateChangedEvent)
		 */
		public void checkStateChanged(CheckStateChangedEvent event) {
			CheckerInstance instance = (CheckerInstance) event.getElement();
			if (canBeAutomatic(instance)) {
				if (instance.isAutomatic() != event.getChecked()) {
					instance.setAutomatic(event.getChecked());
					saveResource();
				}
			}
		}

	}

	private static boolean canBeAutomatic(CheckerInstance instance) {
		return instance.getExecutablePath() != null
				&& instance.getExecutablePath().length() != 0;
	}

	private static final String[] CONFIGURATION_BUTTONS = {
			Messages.TclChecker_button_Add, Messages.TclChecker_button_Edit,
			Messages.TclChecker_button_Copy, Messages.TclChecker_button_Remove,
			null, Messages.TclChecker_button_Import,
			Messages.TclChecker_button_Export };

	private static final String[] INSTANCE_BUTTONS = {
			Messages.TclChecker_button_Add, Messages.TclChecker_button_Edit,
			Messages.TclChecker_button_Remove };

	private ListDialogField configurationField;
	private CheckedListDialogField instanceField;
	private EnvironmentContainer environments = new EnvironmentContainer();

	@Override
	protected Control createOptionsBlock(Composite parent) {
		TabFolder folder = new TabFolder(parent, SWT.NONE);
		TabItem tabInstances = new TabItem(folder, SWT.NONE);
		tabInstances.setText(Messages.TclChecker_tab_Instances);
		tabInstances.setControl(createInstanceField(folder));

		TabItem tabConfigs = new TabItem(folder, SWT.NONE);
		tabConfigs.setText(Messages.TclChecker_tab_Configurations);
		tabConfigs.setControl(createConfigurationField(folder));

		folder.setSelection(folder.getItem(0));
		environments.addChangeListener(new Runnable() {
			public void run() {
				instanceField.getTableViewer().refresh();
			}
		});
		return folder;
	}

	/**
	 * @param folder
	 * @return
	 */
	private Composite createInstanceField(Composite folder) {
		instanceField = new CheckedListDialogField(
				new TclCheckerInstanceListAdapter(), INSTANCE_BUTTONS,
				new TclCheckerInstanceLabelProvider());
		instanceField.setUseLabel(false);
		instanceField.setListGrabExcessHorizontalSpace(true);
		instanceField
				.addCheckStateListener(new TclCheckerInstanceFieldListener());
		instanceField.setTableColumns(new ListDialogField.ColumnsDescription(
				new String[] {
						Messages.TclChecker_column_InstanceEnvironmentName,
						Messages.TclChecker_column_InstanceConfigurationName },
				true));
		instanceField.setViewerSorter(new TclCheckerInstanceViewerSorter());
		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(3, false));
		SWTFactory.createLabel(composite,
				Messages.TclCheckerPreferenceBlock_environmentTabDescription,
				3, SWT.WRAP);
		instanceField.doFillIntoGrid(composite, 3);
		return composite;
	}

	private Composite createConfigurationField(Composite folder) {
		configurationField = new ListDialogField(
				new TclCheckerConfigurationListAdapter(),
				CONFIGURATION_BUTTONS,
				new TclCheckerConfigurationLabelProvider());
		configurationField.setUseLabel(false);
		configurationField.setListGrabExcessHorizontalSpace(true);
		configurationField
				.setTableColumns(new ListDialogField.ColumnsDescription(
						new ColumnLayoutData[] { new ColumnWeightData(60),
								new ColumnWeightData(40) }, new String[] {
								Messages.TclChecker_column_ConfigurationName,
								Messages.TclChecker_column_ConfigurationType },
						true));
		configurationField
				.setViewerSorter(new TclCheckerConfigurationViewerSorter());
		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		composite.setLayout(new GridLayout(3, false));
		configurationField.doFillIntoGrid(composite, 3);
		return composite;
	}

	protected CheckerConfig editConfiguration(final CheckerConfig input) {
		final CheckerConfig workingCopy;
		if (input != null) {
			workingCopy = (CheckerConfig) EcoreUtil.copy(input);
		} else {
			workingCopy = ConfigsFactory.eINSTANCE.createCheckerConfig();
		}
		final ChangeRecorder changeRecorder = input != null ? new ChangeRecorder(
				workingCopy)
				: null;
		final TclCheckerConfigurationDialog dialog = new TclCheckerConfigurationDialog(
				getShell(), workingCopy);
		dialog
				.setTitle(input == null ? Messages.TclChecker_add_Configuration_Title
						: Messages.TclChecker_edit_Configuration_Title);
		if (dialog.open() == Window.OK) {
			if (input != null) {
				final ChangeDescription changeDescription = changeRecorder
						.endRecording();
				if (changeDescription != null) {
					changeDescription.applyAndReverse();
					final List<FeatureChange> featureChanges = changeDescription
							.getObjectChanges().get(workingCopy);
					if (featureChanges != null) {
						for (FeatureChange featureChange : featureChanges) {
							featureChange.apply(input);
						}
					}
				}
				return input;
			} else {
				return workingCopy;
			}
		}
		return null;
	}

	protected CheckerInstance editInstance(final CheckerInstance input) {
		final CheckerInstance workingCopy;
		if (input != null) {
			workingCopy = (CheckerInstance) EcoreUtil.copy(input);
		} else {
			workingCopy = ConfigsFactory.eINSTANCE.createCheckerInstance();
			// FIXME workingCopy.setEnvironmentId(input.environmentId);
		}
		final ChangeRecorder changeRecorder = input != null ? new ChangeRecorder(
				workingCopy)
				: null;
		final TclCheckerInstanceDialog dialog = new TclCheckerInstanceDialog(
				getShell(), environments, workingCopy);
		dialog.setTitle(Messages.TclChecker_edit_Environment_Title);
		if (dialog.open() == Window.OK) {
			if (input != null) {
				final ChangeDescription changeDescription = changeRecorder
						.endRecording();
				if (changeDescription != null) {
					changeDescription.applyAndReverse();
					final List<FeatureChange> featureChanges = changeDescription
							.getObjectChanges().get(workingCopy);
					if (featureChanges != null) {
						for (FeatureChange featureChange : featureChanges) {
							featureChange.apply(input);
						}
					}
				}
				return input;
			} else {
				return workingCopy;
			}
		}
		return null;
	}

	@Override
	protected void initialize() {
		super.initialize();
		initValues();
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		initValues();
	}

	@Override
	public void dispose() {
		environments.dispose();
		super.dispose();
	}

	private Resource resource;
	private List<Resource> contributedResources;

	private void initValues() {
		environments.initialize();
		loadResource();
		initConfigurations();
		initInstances();
	}

	private void loadResource() {
		this.resource = TclCheckerConfigUtils
				.loadConfiguration(getString(KEY_CONFIGURATION));
		if (contributedResources == null) {
			contributedResources = TclCheckerConfigUtils
					.loadContributedConfigurations(this.resource
							.getResourceSet());
		}
	}

	protected void saveResource() {
		try {
			setString(KEY_CONFIGURATION, TclCheckerConfigUtils
					.saveConfiguration(resource));
		} catch (IOException e) {
			// FIXME show error
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private void initConfigurations() {
		final List<CheckerConfig> instances = collectConfugurations();
		configurationField.setElements(instances);
		if (!instances.isEmpty()) {
			configurationField.selectFirstElement();
		} else {
			configurationField.selectElements(StructuredSelection.EMPTY);
		}
	}

	private List<CheckerConfig> collectConfugurations() {
		final List<CheckerConfig> instances = new ArrayList<CheckerConfig>();
		TclCheckerConfigUtils.collectConfigurations(instances, resource);
		for (Resource r : contributedResources) {
			TclCheckerConfigUtils.collectConfigurations(instances, r);
		}
		return instances;
	}

	private static class AllEnvironments implements IEnvironmentPredicate {
		public boolean evaluate(String environmentId) {
			return true;
		}
	}

	private IEnvironmentPredicate buildEnvironmentPredicate() {
		if (isProjectPreferencePage()) {
			final IProject project = getProject();
			if (project != null) {
				final String environmentId = EnvironmentManager
						.getEnvironmentId(project);
				if (environmentId != null) {
					return new SingleEnvironmentPredicate(environmentId);
				}
			}
		}
		return new AllEnvironments();
	}

	private void initInstances() {
		final IEnvironmentPredicate ePredicate = buildEnvironmentPredicate();
		final List<CheckerInstance> handles = new ArrayList<CheckerInstance>();
		final List<CheckerInstance> selected = new ArrayList<CheckerInstance>();
		final List<CheckerInstance> grayed = new ArrayList<CheckerInstance>();
		for (EObject object : resource.getContents()) {
			if (object instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) object;
				if (ePredicate.evaluate(instance.getEnvironmentId())) {
					handles.add(instance);
					if (!canBeAutomatic(instance)) {
						grayed.add(instance);
					} else if (instance.isAutomatic()) {
						selected.add(instance);
					}
				}
			}
		}
		instanceField.setElements(handles);
		instanceField.setCheckedElements(selected);
		instanceField.setGrayedElements(grayed);
		if (!handles.isEmpty()) {
			instanceField.selectFirstElement();
		} else {
			instanceField.selectElements(StructuredSelection.EMPTY);
		}
	}
}
