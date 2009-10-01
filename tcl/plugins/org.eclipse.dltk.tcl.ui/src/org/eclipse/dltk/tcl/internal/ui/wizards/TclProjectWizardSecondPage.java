/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;

final class TclProjectWizardSecondPage extends ProjectWizardSecondPage {
	TclProjectWizardSecondPage(ProjectWizardFirstPage mainPage) {
		super(mainPage);
	}

	@Override
	protected void postConfigureProject(IProgressMonitor monitor)
			throws CoreException, InterruptedException {
		final IProject project = getCurrProject();
		((TclProjectCreationWizard) getWizard()).fFirstPage.postConfigure(
				monitor, project);
		monitor.done();
	}

	protected IProject acquireProject() {
		changeToNewProject();
		return getCurrProject();
	}

	protected void releaseProject() {
		removeProject();
	}

}
