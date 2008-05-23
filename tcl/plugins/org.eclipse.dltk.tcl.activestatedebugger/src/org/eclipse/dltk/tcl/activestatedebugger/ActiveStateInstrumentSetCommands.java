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

import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.dbgp.internal.DbgpRequest;
import org.eclipse.dltk.dbgp.internal.commands.DbgpBaseCommands;
import org.eclipse.dltk.dbgp.internal.commands.IDbgpCommunicator;
import org.w3c.dom.Element;

public class ActiveStateInstrumentSetCommands extends DbgpBaseCommands {
	public ActiveStateInstrumentSetCommands(IDbgpCommunicator communicator) {
		super(communicator);
	}

	public void setInstrumentSet(String set) throws DbgpException {
		DbgpRequest request = createRequest("instrument_set"); //$NON-NLS-1$
		request.addOption("-d", set); //$NON-NLS-1$
		request.addOption("-e", "1"); //$NON-NLS-1$
		Element element = communicate(request);
	}
}
