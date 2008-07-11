/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.IOException;

import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.validators.core.AbstractValidatorType;
import org.eclipse.dltk.validators.core.IValidator;
import org.w3c.dom.Element;

public class TclCheckerType extends AbstractValidatorType {

	private static final String CHECKER_ID = "Tcl Checker"; //$NON-NLS-1$

	private TclCheckerImpl checker;

	public TclCheckerType() {
		this.checker = new TclCheckerImpl(CHECKER_ID, this);
		this.checker.setName(CHECKER_ID);
		this.validators.put(CHECKER_ID, checker);
	}

	public IValidator createValidator(String id) {
		// return new ScriptChecker(id, this);
		return null;
	}

	public IValidator createValidatorFrom(String id, Element validatorElement)
			throws IOException {
		// return new ScriptChecker(id, validatorElement, this);
		checker.loadInfo(validatorElement);
		return checker;
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

	public boolean isConfigurable() {
		return true;
	}

	public boolean isBuiltin() {
		return true;
	}
}
