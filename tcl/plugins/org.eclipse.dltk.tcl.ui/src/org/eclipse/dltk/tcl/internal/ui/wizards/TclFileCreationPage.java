/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.ui.TclCodeTemplateArea;
import org.eclipse.dltk.ui.text.templates.ICodeTemplateArea;
import org.eclipse.dltk.ui.wizards.NewSourceModulePage;

public class TclFileCreationPage extends NewSourceModulePage {

	@Override
	protected String getRequiredNature() {
		return TclNature.NATURE_ID;
	}

	@Override
	protected String getPageDescription() {
		return "This wizard creates a new Tcl file.";
	}

	@Override
	protected String getPageTitle() {
		return "Create new Tcl file";
	}

	private final ICodeTemplateArea codeTemplateArea = new TclCodeTemplateArea();

	/*
	 * @see NewSourceModulePage#getCodeTemplateArea()
	 */
	@Override
	protected ICodeTemplateArea getTemplateArea() {
		return codeTemplateArea;
	}

	/*
	 * @see NewSourceModulePage#getCodeTemplateContextTypes()
	 */
	@Override
	protected String[] getCodeTemplateContextTypeIds() {
		return new String[] { "org.eclipse.dltk.tcl.text.template.type.tcl" }; //$NON-NLS-1$
	}

	/*
	 * @see NewSourceModulePage#getDefaultCodeTemplateId()
	 */
	@Override
	protected String getDefaultCodeTemplateId() {
		return "org.eclipse.dltk.tcl.text.templates.tcl"; //$NON-NLS-1$
	}

	/**
	 * @return the name of the template used in the previous dialog invocation.
	 */
	@Override
	protected String getLastUsedTemplateName() {
		return null;
	}

}
