/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;

final class TclProjectWizardSecondPage extends ProjectWizardSecondPage {
	TclProjectWizardSecondPage(ProjectWizardFirstPage mainPage) {
		super(mainPage);
	}

	@Override
	protected void postConfigureProject(IProgressMonitor monitor)
			throws CoreException, InterruptedException {
		// final IProject project = getCurrProject();
		// final IEnvironment environment = EnvironmentManager
		// .getEnvironment(project);
		// if (environment != null && !environment.isLocal()) {
		// final Map options = new HashMap();
		// options.put(DLTKCore.INDEXER_ENABLED, DLTKCore.DISABLED);
		// options.put(DLTKCore.BUILDER_ENABLED, DLTKCore.DISABLED);
		// DLTKCore.create(project).setOptions(options);
		// }
		final IProject project = getCurrProject();
		((TclProjectCreationWizard) getWizard()).fFirstPage.postConfigure(
				monitor, project);
		monitor.done();
	}

	protected IProject acquireProject() {
		changeToNewProject();
		try {
			configureEnvironment(new NullProgressMonitor());
		} catch (CoreException e) {
			DLTKUIPlugin.log(e);
		}
		return getCurrProject();
	}

	protected void releaseProject() {
		removeProject();
	}

}
