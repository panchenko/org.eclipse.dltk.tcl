/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.validators.core.AbstractValidatorType;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidator;

public class TclCheckerType extends AbstractValidatorType {

	private static final String CHECKER_ID = "Tcl Checker"; //$NON-NLS-1$

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
		return "org.eclipse.dltk.tclchecker"; //$NON-NLS-1$
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
}
