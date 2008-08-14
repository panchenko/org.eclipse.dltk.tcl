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
package org.eclipse.dltk.tcl.internal.parser.raw;

import java.util.ArrayList;
import java.util.List;

public class TclScript extends TclElement {

	List<TclCommand> commands;

	public TclScript() {
		commands = new ArrayList<TclCommand>();
	}

	public void addCommand(TclCommand cmd) {
		commands.add(cmd);
	}

	public String getRawText() {
		// TODO
		return null;
	}

	public List<TclCommand> getCommands() {
		return commands;
	}

	public String toString() {
		return "TclScript" + commands; //$NON-NLS-1$
	}

}
