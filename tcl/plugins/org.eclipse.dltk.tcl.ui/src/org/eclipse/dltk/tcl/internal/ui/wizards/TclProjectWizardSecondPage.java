/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.wizards;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.preferences.TclBuildPathsBlock;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jface.preference.IPreferenceStore;

final class TclProjectWizardSecondPage extends ProjectWizardSecondPage {
	TclProjectWizardSecondPage(ProjectWizardFirstPage mainPage) {
		super(mainPage);
	}

	protected BuildpathsBlock createBuildpathBlock(
			IStatusChangeListener listener) {
		return new TclBuildPathsBlock(new BusyIndicatorRunnableContext(),
				listener, 0, useNewSourcePage(), null);
	}

	protected String getScriptNature() {
		return TclNature.NATURE_ID;
	}

	protected IPreferenceStore getPreferenceStore() {
		return TclUI.getDefault().getPreferenceStore();
	}

	protected void postConfigureProject() throws CoreException {
		final IProject project = getCurrProject();
		final IEnvironment environment = EnvironmentManager
				.getEnvironment(project);
		if (environment != null && !environment.isLocal()) {
			final Map options = new HashMap();
			options.put(DLTKCore.INDEXER_ENABLED, DLTKCore.DISABLED);
			options.put(DLTKCore.BUILDER_ENABLED, DLTKCore.DISABLED);
			DLTKCore.create(project).setOptions(options);
		}
	}

}
