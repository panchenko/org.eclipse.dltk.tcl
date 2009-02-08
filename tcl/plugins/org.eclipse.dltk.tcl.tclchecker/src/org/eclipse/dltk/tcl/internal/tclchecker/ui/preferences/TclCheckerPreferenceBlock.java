package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
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
				ConfigInstance instance = editConfiguration(field, null);
				if (instance != null) {
					field.addElement(instance);
				}
			}
		}

		public void doubleClicked(ListDialogField field) {
			customButtonPressed(field, IDX_EDIT);
		}

		public void selectionChanged(ListDialogField field) {
			// TODO Auto-generated method stub

		}

	}

	private static class TclCheckerConfigurationLabelProvider extends
			LabelProvider {

	}

	private static class TclCheckerConfigurationViewerSorter extends
			ViewerSorter {

		@Override
		public int compare(Viewer viewer, Object e1, Object e2) {
			ConfigInstance config1 = (ConfigInstance) e1;
			ConfigInstance config2 = (ConfigInstance) e2;
			return config1.getName().compareToIgnoreCase(config2.getName());
		}

	}

	private class TclCheckerConfigurationCheckStateListener implements
			ICheckStateListener {

		public void checkStateChanged(CheckStateChangedEvent event) {
			// TODO Auto-generated method stub

		}

	}

	private static final int IDX_ADD = 0;
	private static final int IDX_EDIT = 1;
	private static final int IDX_COPY = 2;

	private static final String[] CONFIGURATION_BUTTONS = { "Add...",
			"Edit...", "Copy..." };

	private ListDialogField configurationField;

	@Override
	protected Control createOptionsBlock(Composite parent) {
		CTabFolder folder = new CTabFolder(parent, SWT.NONE);
		CTabItem tabConfigs = new CTabItem(folder, SWT.NONE);
		tabConfigs.setText("Configurations");
		configurationField = new ListDialogField(
				new TclCheckerConfigurationListAdapter(),
				CONFIGURATION_BUTTONS,
				new TclCheckerConfigurationLabelProvider()) {

			@Override
			protected TableViewer createTableViewer(Composite parent) {
				Table table = new Table(parent, SWT.CHECK | getListStyle());
				table.setFont(parent.getFont());
				return new CheckboxTableViewer(table);
			}

		};
		configurationField
				.setTableColumns(new ListDialogField.ColumnsDescription(
						new String[] { "Name", "Type" }, true));
		configurationField
				.setViewerSorter(new TclCheckerConfigurationViewerSorter());
		Composite configurationComposite = new Composite(folder, SWT.NONE);
		configurationComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		configurationComposite.setLayout(new GridLayout(3, false));
		configurationField.doFillIntoGrid(configurationComposite, 3);
		((GridData) configurationField.getListControl(configurationComposite)
				.getLayoutData()).grabExcessHorizontalSpace = true;
		tabConfigs.setControl(configurationComposite);
		((CheckboxTableViewer) configurationField.getTableViewer())
				.addCheckStateListener(new TclCheckerConfigurationCheckStateListener());

		// TabItem tabInstances = new TabItem(folder, SWT.NONE);
		// tabInstances.setText("Instances");

		// TODO Auto-generated method stub

		folder.setSelection(folder.getItem(0));
		return folder;
	}

	protected ConfigInstance editConfiguration(ListDialogField field,
			ConfigInstance input) {
		TclCheckerConfigurationDialog dialog = new TclCheckerConfigurationDialog(
				getShell(), input);
		dialog.setTitle(input == null ? "Add TclChecker Configuration"
				: "Edit TclChecker Configuration");
		if (dialog.open() == Window.OK) {
			if (input == null) {
				input = ConfigsFactory.eINSTANCE.createConfigInstance();
			}
			dialog.setObjectData(input);
			return input;
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

	private static final String ENCODING = "UTF-8";

	private void initValues() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ConfigsPackage.eNS_URI,
				ConfigsPackage.eINSTANCE);
		Resource resource = resourceSet.createResource(URI
				.createURI(ConfigsPackage.eNS_URI));
		String value = getString(KEY_CONFIGURATION);
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
		final List<ConfigInstance> instances = new ArrayList<ConfigInstance>();
		for (EObject object : resource.getContents()) {
			if (object instanceof ConfigInstance) {
				instances.add((ConfigInstance) object);
			}
		}
		configurationField.setElements(instances);
	}

}
