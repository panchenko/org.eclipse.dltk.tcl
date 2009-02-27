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
package org.eclipse.dltk.tcl.internal.tclchecker;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.validators.core.AbstractValidator;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidatorType;

public class TclCheckerOptional extends AbstractValidator {

	private final IEnvironment environment;
	private final CheckerEnvironmentInstance environmentInstance;
	private final CheckerConfig config;

	/**
	 * @param environmentInstance
	 * @param id
	 * @param name
	 * @param type
	 */
	protected TclCheckerOptional(IEnvironment environment,
			CheckerEnvironmentInstance environmentInstance,
			CheckerConfig config, IValidatorType type) {
		super(TclCheckerType.CHECKER_ID, environmentInstance.getInstance()
				.getName()
				+ " (" //$NON-NLS-1$
				+ config.getName() + ")", type); //$NON-NLS-1$
		this.environment = environment;
		this.environmentInstance = environmentInstance;
		this.config = config;
	}

	@SuppressWarnings("unchecked")
	public Object getValidator(IScriptProject project, Class validatorType) {
		if (ISourceModuleValidator.class.equals(validatorType)) {
			// TODO project specific settings?
			return new TclChecker(environmentInstance, config, environment);
		}
		return null;
	}

	public boolean isValidatorValid(IScriptProject project) {
		return TclCheckerHelper.canExecuteTclChecker(environmentInstance,
				environment);
	}

}
