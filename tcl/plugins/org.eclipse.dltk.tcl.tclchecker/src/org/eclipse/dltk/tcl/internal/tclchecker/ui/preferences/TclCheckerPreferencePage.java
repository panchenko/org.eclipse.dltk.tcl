package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclCheckerPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	public TclCheckerPreferencePage() {
		noDefaultAndApplyButton();
	}

	@Override
	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new TclCheckerPreferenceBlock(newStatusChangedListener, project,
				container);
	}

	@Override
	protected String getHelpId() {
		return null;
	}

	@Override
	protected String getProjectHelpId() {
		return null;
	}

	@Override
	protected void setDescription() {
		// empty
	}

	@Override
	protected void setPreferenceStore() {
		// empty
	}

	@Override
	protected String getPreferencePageId() {
		return "org.eclipse.dltk.tcl.tclchecker.configuration"; //$NON-NLS-1$
	}

	@Override
	protected String getPropertyPageId() {
		return "org.eclipse.dltk.tcl.propertypage.tclchecker"; //$NON-NLS-1$
	}

}
