/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterContainerWizardPage;
import org.eclipse.dltk.internal.debug.ui.interpreters.IInterpreterComboBlockContext;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.wizards.IBuildpathContainerPageExtension;

public class TclInterpreterContainerWizardPage extends
		AbstractInterpreterContainerWizardPage implements
		IBuildpathContainerPageExtension {

	@Override
	protected AbstractInterpreterComboBlock createInterpreterBlock(
			IInterpreterComboBlockContext context) {
		final TclInterpreterComboBlock block = new TclInterpreterComboBlock(
				context);
		block.initialize(getScriptProject());
		return block;
	}

	@Override
	public String getScriptNature() {
		return TclNature.NATURE_ID;
	}
}
