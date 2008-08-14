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
package org.eclipse.dltk.tcl.parser.definitions;

import java.util.List;

import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Command;

public class BuiltinCommandUtils {
	public static String getNamespaceEvalName(TclCommand command,
			Command definition) {
		List<TclArgument> list = command.getArguments();
		TclArgument argument = list.get(0);
		if (argument instanceof StringArgument) {
			if ("eval".equals(((StringArgument) argument).getValue())) {
				TclArgument namespaceName = list.get(1);
				if( namespaceName instanceof StringArgument ) {
					return ((StringArgument)namespaceName).getValue();
				}
			}
		}
		return null;
	}
}
