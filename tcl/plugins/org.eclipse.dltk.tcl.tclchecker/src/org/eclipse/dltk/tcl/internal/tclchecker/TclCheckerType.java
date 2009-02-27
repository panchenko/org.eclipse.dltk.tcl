/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils.ValidatorInstanceRef;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils.ValidatorInstanceResponse;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.validators.core.AbstractValidatorType;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidator;

public class TclCheckerType extends AbstractValidatorType {

	public static final String TYPE_ID = "org.eclipse.dltk.tclchecker"; //$NON-NLS-1$

	public static final String CHECKER_ID = "Tcl Checker"; //$NON-NLS-1$

	private final TclCheckerImpl checker;

	@SuppressWarnings("unchecked")
	public TclCheckerType() {
		this.checker = new TclCheckerImpl(CHECKER_ID, this);
		this.checker.setName(CHECKER_ID);
		this.validators.put(CHECKER_ID, checker);
	}

	public IValidator createValidator(String id) {
		throw new UnsupportedOperationException();
	}

	public String getID() {
		return TYPE_ID;
	}

	public String getName() {
		return CHECKER_ID;
	}

	public String getNature() {
		return TclNature.NATURE_ID;
	}

	public boolean isBuiltin() {
		return true;
	}

	@Override
	public boolean isConfigurable() {
		return false;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class validatorType) {
		return ISourceModuleValidator.class.equals(validatorType);
	}

	@Override
	public IValidator[] getAllValidators(IEnvironment environment) {
		final ValidatorInstanceResponse response = TclCheckerConfigUtils
				.getConfiguration(environment, TclCheckerConfigUtils.ALL);
		final List<IValidator> result = new ArrayList<IValidator>();
		for (ValidatorInstanceRef pair : response.instances) {
			result.add(new TclCheckerOptional(environment,
					pair.environmentInstance, pair.config, this));
			for (CheckerConfig config : pair.environmentInstance.getInstance()
					.getConfigs()) {
				if (config != pair.config) {
					result.add(new TclCheckerOptional(environment,
							pair.environmentInstance, config, this));
				}
			}
			for (CheckerConfig config : response.getCommonConfigurations()) {
				if (config != pair.config) {
					result.add(new TclCheckerOptional(environment,
							pair.environmentInstance, config, this));
				}
			}
		}
		return result.toArray(new IValidator[result.size()]);
	}
}
