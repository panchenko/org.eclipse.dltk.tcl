/**
 * 
 */
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jface.wizard.IWizardPage;

final class TclProjectWizardSecondPage extends ProjectWizardSecondPage {
	TclProjectWizardSecondPage(ProjectWizardFirstPage firstPage) {
		super(firstPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.WizardPage#getPreviousPage()
	 */

	@Override
	public IWizardPage getPreviousPage() {
		// for now always go the 1st page
		return getWizard().getPage(ProjectWizardFirstPage.PAGE_NAME);
	}

}
