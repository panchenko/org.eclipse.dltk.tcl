package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.search.ProjectIndexerManager;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;
import org.osgi.service.prefs.BackingStoreException;

public class TclEnvironmentPropertyPage extends PropertyPage {

	private static class ProjectIndexJob extends Job {

		private final IProject project;

		public ProjectIndexJob(IProject project) {
			super(
					NLS
							.bind(
									TclPreferencesMessages.TclEnvironmentPropertyPage_IndexingJobName,
									project.getName()));
			this.project = project;
		}

		@Override
		protected IStatus run(IProgressMonitor monitor) {
			ProjectIndexerManager.indexProject(project);
			return Status.OK_STATUS;
		}
	}

	private Combo fEnvironments;
	private IEnvironment[] environments;

	private Button indexerEnabled;
	private Button builderEnabled;
	private Button localValidator;

	@Override
	protected Control createContents(Composite parent) {
		final Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		Label label = new Label(composite, SWT.NONE);
		label
				.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_environmentLabel);
		fEnvironments = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		fEnvironments.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true,
				false));
		environments = EnvironmentManager.getEnvironments();
		final String[] items = new String[environments.length + 1];
		items[0] = TclPreferencesMessages.TclEnvironmentPropertyPage_detectAutomatically;
		final IProject project = (IProject) getElement().getAdapter(
				IProject.class);
		final String envId = getCurrentEnvironment(project);
		int selection = 0;
		for (int i = 0; i < environments.length; i++) {
			final IEnvironment env = environments[i];
			items[i + 1] = env.getName();
			if (envId != null && envId.equals(env.getId())) {
				selection = i + 1;
			}
		}
		fEnvironments.setItems(items);
		fEnvironments.select(selection);

		final IScriptProject scriptProject = DLTKCore.create(project);

		indexerEnabled = new Button(composite, SWT.CHECK);
		final GridData indexerData = new GridData(GridData.FILL_HORIZONTAL);
		indexerData.horizontalSpan = 2;
		indexerEnabled.setLayoutData(indexerData);
		indexerEnabled
				.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_IndexerEnabled);
		indexerEnabled.setSelection(!DLTKCore.DISABLED.equals(scriptProject
				.getOption(DLTKCore.INDEXER_ENABLED, false)));

		builderEnabled = new Button(composite, SWT.CHECK);
		final GridData builderData = new GridData(GridData.FILL_HORIZONTAL);
		builderData.horizontalSpan = 2;
		builderEnabled.setLayoutData(builderData);
		builderEnabled
				.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_BuilderEnabled);
		builderEnabled.setSelection(!DLTKCore.DISABLED.equals(scriptProject
				.getOption(DLTKCore.BUILDER_ENABLED, false)));

		localValidator = new Button(composite, SWT.CHECK);
		final GridData localData = new GridData(GridData.FILL_HORIZONTAL);
		localData.horizontalSpan = 2;
		localValidator.setLayoutData(localData);
		localValidator.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_localTclChecker);
		localValidator.setSelection(DLTKCore.ENABLED.equals(new ProjectScope(
				project).getNode(TclPlugin.PLUGIN_ID).get(
				TclPlugin.PREF_LOCAL_VALIDATOR, DLTKCore.DISABLED)));
		return composite;
	}

	@Override
	public boolean performOk() {
		final IProject project = (IProject) getElement().getAdapter(
				IProject.class);
		final int selection = fEnvironments.getSelectionIndex();
		try {
			if (selection <= 0) {
				EnvironmentManager.setEnvironmentId(project, null);
			} else if (selection <= environments.length) {
				EnvironmentManager.setEnvironment(project,
						environments[selection - 1]);
			}
		} catch (CoreException e) {
			// TODO
			return false;
		}
		final IScriptProject scriptProject = DLTKCore.create(project);
		final boolean newIndexer = indexerEnabled.getSelection();
		final boolean wasIndexer = !DLTKCore.DISABLED.equals(scriptProject
				.getOption(DLTKCore.INDEXER_ENABLED, false));
		if (wasIndexer != newIndexer) {
			scriptProject.setOption(DLTKCore.INDEXER_ENABLED,
					newIndexer ? DLTKCore.ENABLED : DLTKCore.DISABLED);
			if (newIndexer) {
				new ProjectIndexJob(project).schedule(500);
			}
		}
		final boolean newBuilder = builderEnabled.getSelection();
		final boolean wasBuilder = !DLTKCore.DISABLED.equals(scriptProject
				.getOption(DLTKCore.BUILDER_ENABLED, false));
		if (wasBuilder != newBuilder) {
			scriptProject.setOption(DLTKCore.BUILDER_ENABLED,
					newBuilder ? DLTKCore.ENABLED : DLTKCore.DISABLED);
			if (!newBuilder) {
				new ProjectBuildJob(project).schedule(500);
			}
		}
		final IEclipsePreferences node = new ProjectScope(project)
				.getNode(TclPlugin.PLUGIN_ID);
		node.put(TclPlugin.PREF_LOCAL_VALIDATOR,
				localValidator.getSelection() ? DLTKCore.ENABLED
						: DLTKCore.DISABLED);
		try {
			node.flush();
		} catch (BackingStoreException e) {
			// TODO Auto-generated catch block
		}
		return super.performOk();
	}

	private String getCurrentEnvironment(final IProject project) {
		return EnvironmentManager.getEnvironmentId(project, false);
	}
}
