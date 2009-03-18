package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.dialogs.PropertyPage;

public class TclEnvironmentPropertyPage extends PropertyPage {

	private Combo fEnvironments;
	private IEnvironment[] environments;

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
		} catch (CoreException e) {
			// TODO
			return false;
		}
		return super.performOk();
	}

	private String getCurrentEnvironment(final IProject project) {
		return EnvironmentManager.getEnvironmentId(project, false);
	}
}
