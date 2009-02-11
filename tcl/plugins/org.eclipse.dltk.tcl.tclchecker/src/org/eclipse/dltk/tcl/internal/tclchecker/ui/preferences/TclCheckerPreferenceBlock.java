package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConstants;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.FeatureChange;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
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

		public void customButtonPressed(ListDialogField field, int index) {
			if (index == IDX_ADD) {
				doAdd(field);
			} else if (index == IDX_EDIT) {
				doEdit(field);
			} else if (index == IDX_REMOVE) {
				doRemove(field);
			}
		}

		private void doAdd(ListDialogField field) {
			final ConfigInstance instance = editConfiguration(null);
			if (instance != null) {
				resource.getContents().add(instance);
				saveResource();
				field.addElement(instance);
			}
		}

		private void doEdit(ListDialogField field) {
			List selection = field.getSelectedElements();
			if (canEdit(selection)) {
				final ConfigInstance instance = (ConfigInstance) selection
						.get(0);
				if (editConfiguration(instance) != null) {
					saveResource();
					field.refresh();
					field.selectElements(new StructuredSelection(selection));
				}
			}
		}

		private void doRemove(ListDialogField field) {
			List selection = field.getSelectedElements();
			if (canRemove(selection)) {
				resource.getContents().removeAll(selection);
				saveResource();
				field.removeElements(selection);
			}
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canEdit(List selection) {
			return selection.size() == 1;
		}

		/**
		 * @param selection
		 * @return
		 */
		private boolean canRemove(List selection) {
			return !selection.isEmpty();
		}

		public void doubleClicked(ListDialogField field) {
			customButtonPressed(field, IDX_EDIT);
		}

		public void selectionChanged(ListDialogField field) {
			List selection = field.getSelectedElements();
			field.enableButton(IDX_EDIT, canEdit(selection));
			field.enableButton(IDX_REMOVE, canRemove(selection));
		}

	}

	private static class TclCheckerConfigurationLabelProvider extends
			LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ConfigInstance) {
				final ConfigInstance config = (ConfigInstance) element;
				switch (columnIndex) {
				case 0:
					return config.getName();
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
	}

	private static final int IDX_ADD = 0;
	private static final int IDX_EDIT = 1;
	private static final int IDX_COPY = 2;
	private static final int IDX_REMOVE = 3;

	private static final String[] CONFIGURATION_BUTTONS = {
			PreferencesMessages.TclChecker_button_Add,
			PreferencesMessages.TclChecker_button_Edit,
			PreferencesMessages.TclChecker_button_Copy,
			PreferencesMessages.TclChecker_button_Remove };

	private ListDialogField configurationField;

	@Override
	protected Control createOptionsBlock(Composite parent) {
		CTabFolder folder = new CTabFolder(parent, SWT.NONE);
		CTabItem tabConfigs = new CTabItem(folder, SWT.NONE);
		tabConfigs.setText(PreferencesMessages.TclChecker_tab_Configurations);
		configurationField = new ListDialogField(
				new TclCheckerConfigurationListAdapter(),
				CONFIGURATION_BUTTONS,
				new TclCheckerConfigurationLabelProvider());
		configurationField
				.setTableColumns(new ListDialogField.ColumnsDescription(
						new String[] {
								PreferencesMessages.TclChecker_column_ConfigurationName,
								PreferencesMessages.TclChecker_column_ConfigurationType },
						true));
		configurationField
				.setViewerSorter(new TclCheckerConfigurationViewerSorter());
		Composite configurationComposite = new Composite(folder, SWT.NONE);
		configurationComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		configurationComposite.setLayout(new GridLayout(3, false));
		configurationField.doFillIntoGrid(configurationComposite, 3);
		((GridData) configurationField.getListControl(configurationComposite)
				.getLayoutData()).grabExcessHorizontalSpace = true;
		tabConfigs.setControl(configurationComposite);

		// TabItem tabInstances = new TabItem(folder, SWT.NONE);
		// tabInstances.setText("Instances");

		// TODO Auto-generated method stub

		folder.setSelection(folder.getItem(0));
		return folder;
	}

	protected ConfigInstance editConfiguration(final ConfigInstance input) {
		final ConfigInstance workingCopy;
		if (input != null) {
			workingCopy = (ConfigInstance) EcoreUtil.copy(input);
		} else {
			workingCopy = ConfigsFactory.eINSTANCE.createConfigInstance();
		}
		final ChangeRecorder changeRecorder = input != null ? new ChangeRecorder(
				workingCopy)
				: null;
		final TclCheckerConfigurationDialog dialog = new TclCheckerConfigurationDialog(
				getShell(), workingCopy);
		dialog
				.setTitle(input == null ? PreferencesMessages.TclChecker_add_Configuration_Title
						: PreferencesMessages.TclChecker_edit_Configuration_Title);
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

	private Resource resource;

	private static final String ENCODING = "UTF-8"; //$NON-NLS-1$

	private void initValues() {
		loadResource();
		initConfigurations();
	}

	private void loadResource() {
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ConfigsPackage.eNS_URI,
				ConfigsPackage.eINSTANCE);
		this.resource = resourceSet.createResource(URI
				.createURI(ConfigsPackage.eNS_URI));
		final String value = getString(KEY_CONFIGURATION);
		if (value != null && value.length() != 0) {
			try {
				resource.load(new URIConverter.ReadableInputStream(value,
						ENCODING), null);
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		if (!resource.isLoaded()) {
			resource.getContents().clear();
		}
	}

	protected void saveResource() {
		try {
			final StringWriter writer = new StringWriter();
			resource.save(new URIConverter.WriteableOutputStream(writer,
					ENCODING), null);
			setString(KEY_CONFIGURATION, writer.toString());
		} catch (IOException e) {
			// FIXME show error
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private void initConfigurations() {
		final List<ConfigInstance> instances = new ArrayList<ConfigInstance>();
		for (EObject object : resource.getContents()) {
			if (object instanceof ConfigInstance) {
				instances.add((ConfigInstance) object);
			}
		}
		configurationField.setElements(instances);
		if (!instances.isEmpty()) {
			configurationField.selectFirstElement();
		} else {
			configurationField.selectElements(new StructuredSelection(
					Collections.EMPTY_LIST));
		}
	}

}
