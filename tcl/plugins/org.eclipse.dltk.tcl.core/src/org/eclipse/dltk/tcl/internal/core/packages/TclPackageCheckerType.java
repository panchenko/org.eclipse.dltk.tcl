/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.packages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.validators.core.AbstractBuildParticipantType;
import org.eclipse.dltk.validators.core.IBuildParticipant;

public class TclPackageCheckerType extends AbstractBuildParticipantType {

	private static final String ID = "org.eclipse.dltk.tcl.packageChecker"; //$NON-NLS-1$
	private static final String NAME = "Tcl Package Checker"; //$NON-NLS-1$

	/**
	 * @param id
	 * @param name
	 */
	public TclPackageCheckerType() {
		super(ID, NAME);
	}

	protected IBuildParticipant createBuildParticipant(IScriptProject project)
			throws CoreException {
		try {
			return new TclCheckBuilder(project);
		} catch (IllegalStateException e) {
			return null;
		}
	}

	public String getNature() {
		return TclNature.NATURE_ID;
	}

}
