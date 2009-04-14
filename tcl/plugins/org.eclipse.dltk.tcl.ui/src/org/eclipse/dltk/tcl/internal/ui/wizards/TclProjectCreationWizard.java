/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.tcl.internal.ui.TclImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.IProjectWizardLastPage;
import org.eclipse.dltk.ui.wizards.NewElementWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

public class TclProjectCreationWizard extends NewElementWizard implements
		INewWizard, IExecutableExtension {

	public static final String ID_WIZARD = "org.eclipse.dltk.tcl.internal.ui.wizards.TclProjectWizard"; //$NON-NLS-1$

	private TclProjectWizardFirstPage fFirstPage;
	private TclProjectWizardSecondPage fSecondPage;

	private IConfigurationElement fConfigElement;

	public TclProjectCreationWizard() {
		setDefaultPageImageDescriptor(TclImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(TclWizardMessages.ProjectCreationWizard_title);
	}

	public void addPages() {
		super.addPages();
		fFirstPage = new TclProjectWizardFirstPage();
		addPage(fFirstPage);
		fSecondPage = new TclProjectWizardSecondPage(fFirstPage);
		addPage(fSecondPage);
	}

	protected void finishPage(IProgressMonitor monitor)
			throws InterruptedException, CoreException {
		getLastPage().performFinish(monitor); // use the full progress monitor
	}

	public boolean performFinish() {
		boolean res = super.performFinish();
		if (res) {
			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
			selectAndReveal(getLastPage().getScriptProject().getProject());
		}
		return res;
	}

	private IProjectWizardLastPage getLastPage() {
		return fSecondPage;
	}

	/*
	 * Stores the configuration element for the wizard. The config element will
	 * be used in <code>performFinish</code> to set the result perspective.
	 */
	public void setInitializationData(IConfigurationElement cfig,
			String propertyName, Object data) {
		fConfigElement = cfig;
	}

	public boolean performCancel() {
		getLastPage().performCancel();
		return super.performCancel();
	}

	public IModelElement getCreatedElement() {
		return DLTKCore.create(fFirstPage.getProjectHandle());
	}

	public IWizardPage getNextPage(IWizardPage page) {
		IWizardPage nextPage = super.getNextPage(page);
		while (nextPage != null && !isEnabledPage(nextPage)) {
			nextPage = super.getNextPage(nextPage);
		}
		return nextPage;
	}

	public IWizardPage getPreviousPage(IWizardPage page) {
		IWizardPage prevPage = super.getPreviousPage(page);
		while (prevPage != null && !isEnabledPage(prevPage)) {
			prevPage = super.getPreviousPage(prevPage);
		}
		return prevPage;
	}

	public boolean canFinish() {
		final IWizardPage[] pages = getPages();
		for (int i = 0; i < pages.length; ++i) {
			final IWizardPage page = pages[i];
			if (isEnabledPage(page)) {
				if (!page.isPageComplete()) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isEnabledPage(IWizardPage page) {
		return true;
	}
}
