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

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.dbgp.IDbgpFeature;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.model.IScriptDebugThreadConfigurator;
import org.eclipse.dltk.internal.debug.core.model.ScriptThread;
import org.eclipse.dltk.internal.debug.core.model.operations.DbgpDebugger;

public class TclActiveStateDebugThreadConfigurator implements
		IScriptDebugThreadConfigurator {
	private boolean initialized = false;

	public void configureThread(DbgpDebugger engine, ScriptThread scriptThread) {
		if (initialized) {
			return;
		}
		initialized = true;
		try {
			IDbgpFeature feature = engine.getFeature("instrument_set");
			if (feature.isSupported()) {
				ActiveStateInstrumentSetCommands commands = new ActiveStateInstrumentSetCommands(
						engine.getSession().getCommunicator());
				commands.setInstrumentSet("itcl");
			}
		} catch (DbgpException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}
}
