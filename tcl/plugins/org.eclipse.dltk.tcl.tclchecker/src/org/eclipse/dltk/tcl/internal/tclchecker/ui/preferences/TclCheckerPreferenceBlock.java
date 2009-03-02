package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.IEnvironmentPredicate;
import org.eclipse.dltk.tcl.internal.tclchecker.impl.SingleEnvironmentPredicate;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.environment.EnvironmentContainer;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.validators.configs.ValidatorConfig;
import org.eclipse.dltk.validators.configs.ValidatorEnvironmentInstance;
import org.eclipse.dltk.validators.configs.ValidatorInstance;
import org.eclipse.dltk.validators.core.ValidatorRuntime;
import org.eclipse.dltk.validators.internal.core.ValidatorsCore;
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
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclCheckerPreferenceBlock extends AbstractOptionsBlock {

	private static final PreferenceKey KEY_CONFIGURATION = new PreferenceKey(
			ValidatorsCore.PLUGIN_ID, ValidatorRuntime.PREF_CONFIGURATION);

	private static final PreferenceKey[] KEYS = new PreferenceKey[] { KEY_CONFIGURATION };

	public TclCheckerPreferenceBlock(IStatusChangeListener context,
			IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, KEYS, container);
	}

	private static class ValidatorViewerSorter extends ViewerSorter {

		private static final int INSTANCE_CATEGORY = -1;

		private static final int BUILTIN_CONFIG_CATEGORY = -2;
		private static final int USER_CONFIG_CATEGORY = -1;

		@Override
		public int category(Object element) {
			if (element instanceof ValidatorConfig) {
				return ((ValidatorConfig) element).isReadOnly() ? BUILTIN_CONFIG_CATEGORY
						: USER_CONFIG_CATEGORY;
			} else if (element instanceof ValidatorInstance) {
				return INSTANCE_CATEGORY;
			}
			return super.category(element);
		}

	}

	private static class ViewerSorterComparatorWrapper implements
			Comparator<Object> {

		/**
		 * @param viewerl
		 */
		public ViewerSorterComparatorWrapper(StructuredViewer viewer) {
			this.viewer = viewer;
			this.sorter = viewer.getSorter() != null ? viewer.getSorter()
					: new ValidatorViewerSorter();
		}

		private final StructuredViewer viewer;
		private final ViewerSorter sorter;

		public int compare(Object o1, Object o2) {
			return sorter.compare(viewer, o1, o2);
		}

	}

	private class TclCheckerInstanceLabelProvider extends LabelProvider {

		@Override
		public String getText(Object element) {
			if (element instanceof ValidatorInstance) {
				final ValidatorInstance instance = (ValidatorInstance) element;
				final String name = instance.getName();
				if (name != null && name.length() != 0) {
					return name;
				} else {
					return Messages.TclChecker_name;
				}
			} else if (element instanceof ValidatorConfig) {
				return getConfigName((ValidatorConfig) element);
			} else if (element instanceof ValidatorConfigRef) {
				return getConfigName(((ValidatorConfigRef) element).config);
			} else {
				return Util.EMPTY_STRING;
			}
		}

		private String getConfigName(ValidatorConfig config) {
			if (config.isReadOnly()) {
				return config.getName()
						+ Messages.TclCheckerPreferenceBlock_BuiltIn;
			} else {
				return config.getName();
			}
		}
	}

	private class ValidatorInput {

		public ValidatorInstance[] getInstances() {
			final List<ValidatorInstance> instances = new ArrayList<ValidatorInstance>();
			for (EObject object : resource.getContents()) {
				if (object instanceof ValidatorInstance) {
					// TODO check nature or validatorType
					instances.add((ValidatorInstance) object);
				}
			}
			return instances.toArray(new ValidatorInstance[instances.size()]);
		}

	}

	private class ValidatorContentProvider implements ITreeContentProvider {

		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof ValidatorInstance) {
				return getConfigsOf((ValidatorInstance) parentElement);
			}
			return new Object[0];
		}

		public Object getParent(Object element) {
			if (element instanceof ValidatorConfig) {
				return ((ValidatorConfig) element).eContainer();
			} else if (element instanceof ValidatorConfigRef) {
				return ((ValidatorConfigRef) element).instance;
			}
			return null;
		}

		public boolean hasChildren(Object element) {
			// TODO improve
			return element instanceof ValidatorInstance;
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof ValidatorInput) {
				return ((ValidatorInput) inputElement).getInstances();
			}
			return new Object[0];
		}

		public void dispose() {
			// NOP
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// NOP
		}

	}

	private static class ValidatorConfigRef {

		public ValidatorConfigRef(ValidatorInstance instance,
				ValidatorConfig config) {
			this.instance = instance;
			this.config = config;
		}

		final ValidatorInstance instance;
		final ValidatorConfig config;

		@Override
		public int hashCode() {
			return instance.hashCode() ^ config.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ValidatorConfigRef) {
				final ValidatorConfigRef other = (ValidatorConfigRef) obj;
				return config == other.config && instance == other.instance;
			}
			return false;
		}

	}

	private CheckboxTreeViewer viewer;
	private Button[] fButtonControls;
	private EnvironmentContainer environments = new EnvironmentContainer();

	private static final int IDX_ADD_VALIDATOR = 0;
	private static final int IDX_ADD_CONFIG = 1;
	private static final int IDX_EDIT = 2;
	private static final int IDX_COPY = 3;
	private static final int IDX_REMOVE = 4;
	private static final int IDX_IMPORT = 6;
	private static final int IDX_EXPORT = 7;

	@Override
	protected Control createOptionsBlock(Composite parent) {
		Composite folder = SWTFactory.createComposite(parent, parent.getFont(),
				2, 1, GridData.FILL_BOTH);
		viewer = new CheckboxTreeViewer(folder, SWT.BORDER | SWT.SINGLE);
		viewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		viewer.setLabelProvider(new TclCheckerInstanceLabelProvider());
		viewer.setSorter(new ValidatorViewerSorter());
		viewer.setContentProvider(new ValidatorContentProvider());
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				TclCheckerPreferenceBlock.this
						.selectionChanged(convertSelection(event.getSelection()));
			}
		});
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				customButtonPressed(IDX_EDIT);
			}
		});
		viewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				TclCheckerPreferenceBlock.this.checkStateChanged(event
						.getElement(), event.getChecked());
			}
		});

		Composite buttonBox = SWTFactory.createComposite(folder, folder
				.getFont(), 1, 1, GridData.FILL_VERTICAL);
		String[] buttonLabels = { Messages.TclChecker_button_Add_Validator,
				Messages.TclChecker_button_Add_Configuration,
				Messages.TclChecker_button_Edit,
				Messages.TclChecker_button_Copy,
				Messages.TclChecker_button_Remove, null,
				Messages.TclChecker_button_Import,
				Messages.TclChecker_button_Export };
		fButtonControls = new Button[buttonLabels.length];
		for (int i = 0; i < buttonLabels.length; i++) {
			String label = buttonLabels[i];
			if (label != null) {
				final int buttonIndex = i;
				fButtonControls[i] = createButton(buttonBox, label, null);
				fButtonControls[i].setEnabled(true);
				fButtonControls[i].addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						customButtonPressed(buttonIndex);
					}
				});
			} else {
				fButtonControls[i] = null;
				createSeparator(buttonBox);
			}
		}

		environments.addChangeListener(new Runnable() {
			public void run() {
				viewer.refresh();
			}
		});
		return folder;
	}

	/**
	 * @param element
	 * @param checked
	 */
	protected void checkStateChanged(Object element, boolean checked) {
		if (element instanceof ValidatorInstance) {
			final ValidatorInstance instance = (ValidatorInstance) element;
			instance.setAutomatic(checked);
			for (ValidatorEnvironmentInstance environmentInstance : instance
					.getValidatorEnvironments()) {
				environmentInstance.setAutomatic(checked);
			}
			// TODO disable other tclcheckers if any
		} else if (element instanceof ValidatorConfig) {
			ValidatorConfig config = (ValidatorConfig) element;
			if (config.eContainer() != null) {
				selectFavoriteConfig((ValidatorInstance) config.eContainer(),
						config, element);
			}
		} else if (element instanceof ValidatorConfigRef) {
			ValidatorConfigRef item = (ValidatorConfigRef) element;
			selectFavoriteConfig(item.instance, item.config, element);
		}
	}

	/**
	 * @param container
	 * @param config
	 * @param element
	 */
	private void selectFavoriteConfig(ValidatorInstance instance,
			ValidatorConfig config, Object element) {
		instance.setValidatorFavoriteConfig(config);
		viewer.setChecked(element, true);
		for (Object checked : viewer.getCheckedElements()) {
			if (checked != element) {
				if (checked instanceof ValidatorConfig) {
					final ValidatorConfig c = (ValidatorConfig) checked;
					if (c.eContainer() == instance) {
						viewer.setChecked(c, false);
					}
				} else if (checked instanceof ValidatorConfigRef) {
					final ValidatorConfigRef ref = (ValidatorConfigRef) checked;
					if (ref.instance == instance) {
						viewer.setChecked(checked, false);
					}
				}
			}
		}
	}

	protected Button createButton(Composite parent, String label,
			SelectionListener listener) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		button.setText(label);
		if (listener != null) {
			button.addSelectionListener(listener);
		}
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.widthHint = SWTFactory.getButtonWidthHint(button);

		button.setLayoutData(gd);

		return button;
	}

	private Label createSeparator(Composite parent) {
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setFont(parent.getFont());
		separator.setVisible(false);
		GridData gd = new GridData();
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.BEGINNING;
		gd.verticalIndent = 4;
		separator.setLayoutData(gd);
		return separator;
	}

	private void enableButton(int buttonIndex, boolean enabled) {
		fButtonControls[buttonIndex].setEnabled(enabled);
	}

	private ValidatorInstance convertToValidatorInstance(List<?> selection) {
		if (selection.size() == 1) {
			final Object obj = selection.get(0);
			if (obj instanceof ValidatorInstance) {
				return (ValidatorInstance) obj;
			} else if (obj instanceof ValidatorConfig) {
				return (ValidatorInstance) ((ValidatorConfig) obj).eContainer();
			} else if (obj instanceof ValidatorConfigRef) {
				return ((ValidatorConfigRef) obj).instance;
			}
		}
		return null;
	}

	private ValidatorConfig convertToValidatorConfig(List<?> selection) {
		if (selection.size() == 1) {
			return convertToValidatorConfig(selection.get(0));
		}
		return null;
	}

	private ValidatorConfig convertToValidatorConfig(final Object obj) {
		if (obj instanceof ValidatorConfig) {
			return (ValidatorConfig) obj;
		} else if (obj instanceof ValidatorConfigRef) {
			return ((ValidatorConfigRef) obj).config;
		} else {
			return null;
		}
	}

	/**
	 * @param selection
	 * @return
	 */
	private boolean canEdit(List<?> selection) {
		if (selection.size() == 1) {
			final Object obj = selection.get(0);
			if (obj instanceof ValidatorConfig) {
				return !((ValidatorConfig) obj).isReadOnly();
			} else if (obj instanceof ValidatorConfigRef) {
				return !((ValidatorConfigRef) obj).config.isReadOnly();
			} else if (obj instanceof ValidatorInstance) {
				return true;
			}
		}
		return false;
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
			final Object obj = i.next();
			if (obj instanceof ValidatorConfig) {
				if (((ValidatorConfig) obj).isReadOnly()) {
					return false;
				}
			} else if (obj instanceof ValidatorConfigRef) {
				return false;
			}
		}
		return true;
	}

	protected void selectionChanged(List<?> selection) {
		final ValidatorInstance instance = convertToValidatorInstance(selection);
		enableButton(IDX_ADD_CONFIG, instance != null);
		enableButton(IDX_EDIT, canEdit(selection));
		enableButton(IDX_COPY, false);
		enableButton(IDX_REMOVE, canRemove(selection));
		enableButton(IDX_IMPORT, instance != null);
		enableButton(IDX_EXPORT, convertToValidatorConfig(selection) != null);
	}

	protected void customButtonPressed(int button) {
		switch (button) {
		case IDX_ADD_VALIDATOR:
			doAddValidator();
			break;
		case IDX_ADD_CONFIG: {
			final ValidatorInstance instance = convertToValidatorInstance(getSelection());
			if (instance != null) {
				doAddConfig(instance);
			}
			break;
		}
		case IDX_EDIT: {
			final List<?> selection = getSelection();
			if (canEdit(selection)) {
				final Object obj = selection.get(0);
				if (obj instanceof CheckerInstance) {
					if (editInstance((CheckerInstance) obj) != null) {
						saveResource();
						viewer.refresh(obj);
					}
				} else if (obj instanceof CheckerConfig) {
					if (editConfiguration((CheckerConfig) obj) != null) {
						saveResource();
						viewer.refresh(obj);
					}
				}
			}
			break;
		}
		case IDX_REMOVE: {
			final List<?> selection = getSelection();
			if (canRemove(selection)) {
				final List<EObject> removed = new ArrayList<EObject>();
				for (Object obj : selection) {
					if (obj instanceof EObject) {
						EcoreUtil.remove((EObject) obj);
						removed.add((EObject) obj);
						viewer.remove(obj);
					}
				}
				for (ValidatorInstance instance : new ValidatorInput()
						.getInstances()) {
					if (instance.getValidatorFavoriteConfig() != null
							&& removed.contains(instance
									.getValidatorFavoriteConfig())) {
						final Object[] configs = getConfigsOf(instance);
						if (configs.length != 0) {
							Arrays.sort(configs,
									new ViewerSorterComparatorWrapper(viewer));
							ValidatorConfig config = convertToValidatorConfig(configs[0]);
							if (config != null) {
								instance.setValidatorFavoriteConfig(config);
								viewer.setChecked(configs[0], true);
							}
						} else {
							instance.setValidatorFavoriteConfig(null);
						}
					}
				}
				saveResource();
			}
			break;
		}
		case IDX_COPY:
			// TODO copy
			break;
		case IDX_IMPORT: {
			final ValidatorInstance instance = convertToValidatorInstance(getSelection());
			if (instance != null) {
				doImport(instance);
			}
			break;
		}
		case IDX_EXPORT: {
			final ValidatorConfig config = convertToValidatorConfig(getSelection());
			if (config != null) {
				doExport(config);
			}
		}
			break;
		}
	}

	private void doAddValidator() {
		final CheckerInstance instance = editInstance(null);
		if (instance != null) {
			resource.getContents().add(instance);
			saveResource();
			viewer.refresh();
		}
	}

	private void doAddConfig(ValidatorInstance instance) {
		final CheckerConfig config = editConfiguration(null);
		if (instance != null) {
			instance.getValidatorConfigs().add(config);
			saveResource();
			viewer.add(instance, config);
		}
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
			workingCopy = input;
		} else {
			workingCopy = ConfigsFactory.eINSTANCE.createCheckerInstance();
			workingCopy.setId(UUID.randomUUID().toString());
		}
		boolean result = false;
		final ChangeRecorder changeRecorder = input != null ? new ChangeRecorder(
				workingCopy)
				: null;
		try {
			final IValidatorDialogContext context = new ValidatorDialogContext(
					buildEnvironmentPredicate(), environments, input == null);
			if (input != null) {
				final TclCheckerInstanceDialog dialog = new TclCheckerInstanceDialog(
						getShell(), context, workingCopy);
				dialog.setTitle(Messages.TclChecker_edit_Instance_Title);
				result = dialog.open() == Window.OK;
			} else {
				TclCheckerInstanceWizard wizard = new TclCheckerInstanceWizard(
						context, workingCopy);
				WizardDialog wd = new WizardDialog(getShell(), wizard);
				PixelConverter converter = new PixelConverter(getShell());
				wd.setMinimumPageSize(
						converter.convertWidthInCharsToPixels(70), converter
								.convertHeightInCharsToPixels(20));
				result = wd.open() == Window.OK;
			}
			if (result) {
				return workingCopy;
			}
		} finally {
			if (changeRecorder != null) {
				if (!result) {
					changeRecorder.endRecording().apply();
				}
				changeRecorder.dispose();
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
	public boolean performOk() {
		saveResource();
		return super.performOk();
	}

	@Override
	public boolean performApply() {
		saveResource();
		return super.performApply();
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
		final ValidatorInput input = new ValidatorInput();
		final List<Object> checked = new ArrayList<Object>();
		for (ValidatorInstance instance : input.getInstances()) {
			if (instance.isAutomatic()) {
				checked.add(instance);
			}
			final ValidatorConfig favorite = instance
					.getValidatorFavoriteConfig();
			if (favorite != null) {
				if (instance.getValidatorConfigs().contains(favorite)) {
					checked.add(favorite);
				} else {
					checked.add(new ValidatorConfigRef(instance, favorite));
				}
			}
		}
		viewer.setInput(input);
		viewer.expandAll();
		viewer.setCheckedElements(checked.toArray());
		selectionChanged(convertSelection(viewer.getSelection()));
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
		statusChanged(StatusInfo.OK_STATUS);
		try {
			setString(KEY_CONFIGURATION, TclCheckerConfigUtils
					.saveConfiguration(resource));
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			statusChanged(new Status(IStatus.ERROR, TclCheckerPlugin.PLUGIN_ID,
					e.getMessage()));
		}
	}

	private List<CheckerConfig> collectConfigurations() {
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

	private List<?> getSelection() {
		return convertSelection(viewer.getSelection());
	}

	private List<?> convertSelection(ISelection selection) {
		if (selection != null && !selection.isEmpty()
				&& selection instanceof IStructuredSelection) {
			return ((IStructuredSelection) selection).toList();
		} else {
			return Collections.emptyList();
		}
	}

	private static final String FILTER_EXTENSIONS = "*.xml"; //$NON-NLS-1$

	private void doImport(ValidatorInstance instance) {
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
					if (object instanceof ValidatorConfig) {
						instance.getValidatorConfigs().add(
								(ValidatorConfig) EcoreUtil.copy(object));
						++importedCount;
					}
				}
				if (importedCount != 0) {
					viewer.refresh();
				}
			} catch (Exception e) {
				ErrorDialog.openError(getShell(),
						Messages.TclChecker_import_ErrorTitle, e.getMessage(),
						new Status(IStatus.ERROR, TclCheckerPlugin.PLUGIN_ID, e
								.getMessage(), e));
			}
		}
	}

	private void doExport(ValidatorConfig config) {
		final FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
		dialog.setText(Messages.TclChecker_export_Title);
		dialog.setOverwrite(true);
		dialog.setFilterExtensions(new String[] { FILTER_EXTENSIONS });
		final String exportPath = dialog.open();
		if (exportPath != null) {
			final Resource resource = new XMIResourceImpl();
			resource.getContents().add(EcoreUtil.copy(config));
			try {
				final FileWriter writer = new FileWriter(exportPath);
				try {
					resource.save(new URIConverter.WriteableOutputStream(
							writer, TclCheckerConfigUtils.ENCODING), null);
				} finally {
					try {
						writer.close();
					} catch (IOException e) {
						// ignore
					}
				}
			} catch (IOException e) {
				ErrorDialog.openError(getShell(),
						Messages.TclChecker_export_ErrorTitle, e.getMessage(),
						new Status(IStatus.ERROR, TclCheckerPlugin.PLUGIN_ID, e
								.getMessage(), e));
			}
		}
	}

	protected Object[] getConfigsOf(final ValidatorInstance instance) {
		final List<Object> children = new ArrayList<Object>();
		children.addAll(instance.getValidatorConfigs());
		final List<CheckerConfig> configs = collectConfigurations();
		for (CheckerConfig config : configs) {
			children.add(new ValidatorConfigRef(instance, config));
		}
		return children.toArray();
	}

}
