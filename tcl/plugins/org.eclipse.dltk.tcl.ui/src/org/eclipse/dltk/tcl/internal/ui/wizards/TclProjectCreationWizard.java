/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.dltk.tcl.internal.ui.TclImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.ProjectWizard;

public class TclProjectCreationWizard extends ProjectWizard {

	public static final String ID_WIZARD = "org.eclipse.dltk.tcl.internal.ui.wizards.TclProjectWizard"; //$NON-NLS-1$

	protected TclProjectWizardFirstPage fFirstPage;
	protected TclProjectWizardSecondPage fSecondPage;

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

}
