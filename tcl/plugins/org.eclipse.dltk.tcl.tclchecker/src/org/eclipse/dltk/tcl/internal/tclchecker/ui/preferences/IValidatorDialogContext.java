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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.dltk.tcl.internal.tclchecker.impl.IEnvironmentPredicate;
import org.eclipse.dltk.ui.environment.EnvironmentContainer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.widgets.Shell;

public interface IValidatorDialogContext {

	EnvironmentContainer getEnvironments();

	IEnvironmentPredicate getEnvironmentPredicate();

	boolean isNew();

	/**
	 * @return
	 */
	Shell getShell();

	IShellProvider getShellProvider();

	void setShellProvider(IShellProvider shellProvider);

	IValidationHandler getValidationHandler();
	
	/**
	 * @param validationHandler
	 */
	void setValidationHandler(IValidationHandler validationHandler);

}
