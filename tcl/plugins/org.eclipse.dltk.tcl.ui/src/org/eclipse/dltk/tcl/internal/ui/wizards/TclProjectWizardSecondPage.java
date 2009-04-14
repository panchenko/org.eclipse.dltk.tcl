/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.ui.wizards.BuildpathDetector;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.preferences.TclBuildPathsBlock;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;

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

	protected BuildpathDetector createBuildpathDetector(
			IProgressMonitor monitor, IDLTKLanguageToolkit toolkit)
			throws CoreException {
		TclBuildpathDetector detector = new TclBuildpathDetector(
				getCurrProject(), toolkit);

		TclProjectWizardFirstPage page = (TclProjectWizardFirstPage) this
				.getFirstPage();
		detector.setUseAnalysis(page.useAnalysis());
		detector.detectBuildpath(new SubProgressMonitor(monitor, 20));
		return detector;
	}

	protected void postConfigureProject() throws CoreException {
		final IProject project = getCurrProject();
		final IEnvironment environment = EnvironmentManager
				.getEnvironment(project);
		if (environment != null && !environment.isLocal()) {
			final IEclipsePreferences coreNode = new ProjectScope(project)
					.getNode(DLTKCore.PLUGIN_ID);
			coreNode.putBoolean(DLTKCore.INDEXER_ENABLED, false);
			coreNode.putBoolean(DLTKCore.BUILDER_ENABLED, false);
			try {
				coreNode.flush();
			} catch (BackingStoreException e) {
				TclUI.error(e);
			}
		}
	}

}
