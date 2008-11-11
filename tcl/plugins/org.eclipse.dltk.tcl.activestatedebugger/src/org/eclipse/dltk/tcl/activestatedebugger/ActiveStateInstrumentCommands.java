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
package org.eclipse.dltk.tcl.activestatedebugger;

import org.eclipse.dltk.dbgp.DbgpBaseCommands;
import org.eclipse.dltk.dbgp.DbgpRequest;
import org.eclipse.dltk.dbgp.IDbgpCommunicator;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;

public class ActiveStateInstrumentCommands extends DbgpBaseCommands {

	private static final String INSTRUMENT_SET = "tcl_instrument_set"; //$NON-NLS-1$
	private static final String INSTRUMENT_INCLUDE = "tcl_instrument_include"; //$NON-NLS-1$
	private static final String INSTRUMENT_EXCLUDE = "tcl_instrument_exclude"; //$NON-NLS-1$

	private static final String FILE_OPTION = "-file"; //$NON-NLS-1$
	private static final String D_OPTION = "-d"; //$NON-NLS-1$

	public ActiveStateInstrumentCommands(IDbgpCommunicator communicator) {
		super(communicator);
	}

	public void instrumentSet(InstrumentationFeature feature, boolean value)
			throws DbgpException {
		DbgpRequest request = createRequest(INSTRUMENT_SET);
		request.addOption(D_OPTION, feature.getValue());
		request.addOption("-e", value ? "yes" : "no"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		communicate(request);
	}

	public void setErrorAction(ErrorAction errorAction) throws DbgpException {
		DbgpRequest request = createRequest(INSTRUMENT_SET);
		request.addOption(D_OPTION, "erroraction"); //$NON-NLS-1$
		request.addOption("-value", errorAction.getValue()); //$NON-NLS-1$
		communicate(request);
	}

	public void instrumentInclude(String pattern) throws DbgpException {
		DbgpRequest request = createRequest(INSTRUMENT_INCLUDE);
		request.addOption(FILE_OPTION, pattern);
		communicate(request);
	}

	public void instrumentExclude(String pattern) throws DbgpException {
		DbgpRequest request = createRequest(INSTRUMENT_EXCLUDE);
		request.addOption(FILE_OPTION, pattern);
		communicate(request);
	}
}
