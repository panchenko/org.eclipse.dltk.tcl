/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.validators;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.internal.validators.TclCheckBuildParticipant;
import org.eclipse.dltk.validators.core.AbstractBuildParticipantType;
import org.eclipse.dltk.validators.core.IBuildParticipant;

public class TclChecksValidatorType extends AbstractBuildParticipantType {

	private static final String ID = "org.eclipse.dltk.tcl.validators.checks"; //$NON-NLS-1$
	private static final String NAME = "Tcl checks"; //$NON-NLS-1$

	public TclChecksValidatorType() {
		super(ID, NAME);
	}

	protected IBuildParticipant createBuildParticipant(IScriptProject project)
			throws CoreException {
		return new TclCheckBuildParticipant();
	}

	public String getNature() {
		return TclNature.NATURE_ID;
	}
}
