/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.IProjectWizardInitializer;
import org.eclipse.dltk.utils.LazyExtensionManager;

class TclProjectWizardInitializerManager extends
		LazyExtensionManager<IProjectWizardInitializer> {

	static final String ATTR_NATURE = "nature"; //$NON-NLS-1$

	/**
	 * @param extensionPoint
	 */
	public TclProjectWizardInitializerManager() {
		super(DLTKUIPlugin.PLUGIN_ID + ".projectWizardInitializer"); //$NON-NLS-1$
	}

	@Override
	protected boolean isValidDescriptor(
			Descriptor<IProjectWizardInitializer> descriptor) {
		final String natureId = descriptor.getAttribute(ATTR_NATURE);
		return natureId == null || TclNature.NATURE_ID.equals(natureId);
	}

}
