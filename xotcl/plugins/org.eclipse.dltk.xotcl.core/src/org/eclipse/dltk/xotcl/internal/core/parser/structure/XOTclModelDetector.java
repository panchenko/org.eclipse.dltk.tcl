/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.xotcl.internal.core.parser.structure;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuilderDetector;

public class XOTclModelDetector implements ITclModelBuilderDetector {

	public String detect(String commandName, TclCommand command,
			ITclModelBuildContext context) {
		if (commandName == null) {
			return null;
		}
		commandName = normalize(commandName);
		if (CLASS.equals(commandName)) {
			return checkClass(command, context);
		} else if (OBJECT.equals(commandName)) {
			return checkObject(command, context);
		} else {
			return checkInstanceOperations(command, context);
		}
	}

	/**
	 * @param command
	 * @param context
	 * @return
	 */
	private String checkClass(TclCommand command, ITclModelBuildContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param command
	 * @param context
	 * @return
	 */
	private String checkObject(TclCommand command, ITclModelBuildContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param command
	 * @param context
	 * @return
	 */
	private String checkInstanceOperations(TclCommand command,
			ITclModelBuildContext context) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("nls")
	private static final String[] NAMESPACES = new String[] { "::xotcl::",
			"xotcl::" };

	private static final String CLASS = "Class"; //$NON-NLS-1$
	private static final String OBJECT = "Object"; //$NON-NLS-1$

	/**
	 * Remove XOTcl namespaces from the specified command name
	 * 
	 * @param commandName
	 * @return command name without namespace
	 */
	private String normalize(String commandName) {
		for (String namespace : NAMESPACES) {
			if (commandName.startsWith(namespace)) {
				return commandName.substring(namespace.length());
			}
		}
		return commandName;
	}
}
