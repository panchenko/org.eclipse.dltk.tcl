/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.validators.core.AbstractValidator;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidatorType;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclCheckerImpl extends AbstractValidator {

	protected TclCheckerImpl(String id, IValidatorType type) {
		super(id, null, type);
	}

	public boolean isValidatorValid(IScriptProject project) {
		return TclCheckerHelper.canExecuteTclChecker(getPreferenceStore(),
				getEnvrironment(project));
	}

	@SuppressWarnings("unchecked")
	public Object getValidator(IScriptProject project, Class validatorType) {
		if (ISourceModuleValidator.class.equals(validatorType)) {
			return new TclChecker(getPreferenceStore(),
					getEnvrironment(project));
		}
		return null;
	}

	private IPreferenceStore getPreferenceStore() {
		return TclCheckerPlugin.getDefault().getPreferenceStore();
	}

}
