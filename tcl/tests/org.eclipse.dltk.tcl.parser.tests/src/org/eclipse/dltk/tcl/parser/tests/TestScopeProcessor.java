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

package org.eclipse.dltk.tcl.parser.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.ISubstitutionManager;
import org.eclipse.dltk.tcl.parser.definitions.IScopeProcessor;
import org.eclipse.emf.common.util.EList;

public class TestScopeProcessor implements IScopeProcessor {
	private Map<String, Object> commands = new HashMap<String, Object>();

	public Command[] getCommandDefinition(String command) {
		Object cmd = commands.get(command);
		if (cmd != null && cmd instanceof Command) {
			return new Command[] { (Command) cmd };
		} else if (cmd != null && cmd instanceof List) {
			List<Command> lst = (List<Command>) this.commands.get(command);
			return lst.toArray(new Command[lst.size()]);
		}
		return new Command[0];
	}

	public void add(Scope command) {
		fillCommandsMap(command);
	}

	private void fillCommandsMap(Scope scope) {
		EList<Scope> children = scope.getChildren();
		for (Scope child : children) {
			fillCommandsMap(child);
		}
		if (scope instanceof Command) {
			Command command = (Command) scope;
			String key = command.getName();
			if (this.commands.containsKey(key)) {
				List<Command> commands = new ArrayList<Command>();
				commands.add(command);
				Object o = this.commands.get(key);
				if (o instanceof Command) {
					commands.add((Command) o);
				} else if (o instanceof List) {
					commands.addAll((List<Command>) o);
				}
				this.commands.put(key, commands);
			} else {
				this.commands.put(key, command);
			}
		}
	}

	public void endProcessCommand() {
	}

	public void processCommand(TclCommand command) {
	}

	public String getQualifiedName(String commandValue) {
		return commandValue;
	}

	public ISubstitutionManager getSubstitutionManager() {
		// TODO Auto-generated method stub
		return null;
	}
}
