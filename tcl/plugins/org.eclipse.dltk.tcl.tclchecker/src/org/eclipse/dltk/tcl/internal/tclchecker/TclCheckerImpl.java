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
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils.InstanceConfigPair;
import org.eclipse.dltk.validators.core.AbstractValidator;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidatorType;

public class TclCheckerImpl extends AbstractValidator {

	protected TclCheckerImpl(String id, IValidatorType type) {
		super(id, null, type);
	}

	@Override
	public boolean isAutomatic(IScriptProject project) {
		final InstanceConfigPair pair = TclCheckerConfigUtils
				.getConfiguration(project);
		return pair != null && pair.instance.isAutomatic();
	}

	public boolean isValidatorValid(IScriptProject project) {
		final InstanceConfigPair pair = TclCheckerConfigUtils
				.getConfiguration(project);
		return pair != null
				&& TclCheckerHelper.canExecuteTclChecker(pair.instance,
						getEnvrironment(project));
	}

	@SuppressWarnings("unchecked")
	public Object getValidator(IScriptProject project, Class validatorType) {
		if (ISourceModuleValidator.class.equals(validatorType)) {
			final InstanceConfigPair pair = TclCheckerConfigUtils
					.getConfiguration(project);
			if (pair != null) {
				return new TclChecker(pair.instance, pair.config,
						getEnvrironment(project));
			}
		}
		return null;
	}
}
