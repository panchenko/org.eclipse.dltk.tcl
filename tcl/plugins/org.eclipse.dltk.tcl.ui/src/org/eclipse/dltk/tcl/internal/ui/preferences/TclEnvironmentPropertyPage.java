package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
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

public class TclEnvironmentPropertyPage extends PropertyPage {

	private static class ProjectBuildJob extends Job {

		private final IProject project;

		/**
		 * @param project
		 */
		public ProjectBuildJob(IProject project) {
			super(
					NLS
							.bind(
									TclPreferencesMessages.TclEnvironmentPropertyPage_BuildingJobName,
									project.getName()));
			this.project = project;
		}

		protected IStatus run(IProgressMonitor monitor) {
			try {
				project.build(IncrementalProjectBuilder.FULL_BUILD, monitor);
				return Status.OK_STATUS;
			} catch (CoreException e) {
				return e.getStatus();
			}
		}
	}

	private Combo fEnvironments;
	private IEnvironment[] environments;

	private Button indexerEnabled;
	private Button builderEnabled;

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

		final IEclipsePreferences coreNode = new ProjectScope(project)
				.getNode(DLTKCore.PLUGIN_ID);

		indexerEnabled = new Button(composite, SWT.CHECK);
		final GridData indexerData = new GridData(GridData.FILL_HORIZONTAL);
		indexerData.horizontalSpan = 2;
		indexerEnabled.setLayoutData(indexerData);
		indexerEnabled
				.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_IndexerEnabled);
		indexerEnabled.setSelection(coreNode.getBoolean(
				DLTKCore.INDEXER_ENABLED, true));

		builderEnabled = new Button(composite, SWT.CHECK);
		final GridData builderData = new GridData(GridData.FILL_HORIZONTAL);
		builderData.horizontalSpan = 2;
		builderEnabled.setLayoutData(indexerData);
		builderEnabled
				.setText(TclPreferencesMessages.TclEnvironmentPropertyPage_BuilderEnabled);
		builderEnabled.setSelection(coreNode.getBoolean(
				DLTKCore.BUILDER_ENABLED, true));

		return composite;
	}

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
			final IEclipsePreferences coreNode = new ProjectScope(project)
					.getNode(DLTKCore.PLUGIN_ID);
			if (indexerEnabled.getSelection()) {
				coreNode.remove(DLTKCore.INDEXER_ENABLED);
			} else {
				coreNode.putBoolean(DLTKCore.INDEXER_ENABLED, false);
			}
			if (builderEnabled.getSelection()) {
				final boolean wasEnabled = coreNode.getBoolean(
						DLTKCore.BUILDER_ENABLED, true);
				coreNode.remove(DLTKCore.BUILDER_ENABLED);
				if (!wasEnabled) {
					final Job job = new ProjectBuildJob(project);
					job.schedule(500);
				}
			} else {
				coreNode.putBoolean(DLTKCore.BUILDER_ENABLED, false);
			}
			coreNode.flush();
		} catch (Exception e) {
			// TODO
			return false;
		}
		return super.performOk();
	}

	private String getCurrentEnvironment(final IProject project) {
		return EnvironmentManager.getEnvironmentId(project, false);
	}
}
