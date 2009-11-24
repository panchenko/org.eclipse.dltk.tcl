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

import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.ITclCommandDetector.CommandInfo;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuilderDetector;
import org.eclipse.dltk.tcl.structure.TclModelBuilderUtil;
import org.eclipse.dltk.xotcl.internal.core.XOTclKeywords;

public class XOTclModelDetector extends TclModelBuilderUtil implements
		ITclModelBuilderDetector {

	private static final boolean INTERPRET_CLASS_UNKNOWN_AS_CREATE = true;
	private static final boolean INTERPRET_OBJECT_UNKNOWN_AS_CREATE = true;

	public String detect(String commandName, TclCommand command,
			ITclModelBuildContext context) {
		if (commandName == null) {
			return null;
		}
		commandName = normalize(commandName);
		if (CLASS.equals(commandName)) {
			return checkClass(command, commandName, context);
		} else if (OBJECT.equals(commandName)) {
			return checkObject(command, commandName, context);
		} else {
			return checkInstanceOperations(command, commandName, context);
		}
	}

	/**
	 * @param command
	 * @param context
	 * @return
	 */
	private String checkClass(TclCommand command, String commandName,
			ITclModelBuildContext context) {
		if (command.getArguments().isEmpty()) {
			return null;
		}
		TclArgument subcmd = command.getArguments().get(0);
		if (isSymbol(subcmd)) {
			String value = asSymbol(subcmd);
			XOTclClassAttribute type = findXOTclType("Class");
			if (type != null) {
				context.addAttribute(type);
			}
			if (contains(value, XOTclKeywords.XOTclCommandClassArgs)) {
				return "#Class#" + value; //$NON-NLS-1$
			}
			String info = checkCreateType(context, command, commandName,
					subcmd, value);
			if (info != null) {
				return info;
			}
			if (INTERPRET_CLASS_UNKNOWN_AS_CREATE) {
				return "#Class#create"; //$NON-NLS-1$
			}
		}
		return null;
	}

	private String checkObject(TclCommand command, String commandName,
			ITclModelBuildContext context) {
		if (command.getArguments().isEmpty()) {
			return null;
		}
		TclArgument subcmd = command.getArguments().get(0);
		if (isSymbol(subcmd)) {
			String value = asSymbol(subcmd);
			XOTclClassAttribute type = findXOTclType("Object");
			if (type != null) {
				context.addAttribute(type);
			}
			if (contains(value, XOTclKeywords.XOTclCommandObjectArgs)) {
				return "#Object#" + value;
			}
			String info = checkCreateType(context, command, commandName,
					subcmd, value);
			if (info != null) {
				return info;
			}
			// // Else unknown command or create command.
			if (INTERPRET_OBJECT_UNKNOWN_AS_CREATE) {
				return "#Object#create";
			}
		}
		return null;
	}

	private String checkCreateType(ITclModelBuildContext context,
			TclCommand command, String commandName, TclArgument arg,
			String value) {
		if (value.equals("instproc") || value.equals("proc")
				|| value.equals("set")) {
			context.addAttribute(new XOTclClassAttribute(commandName, command
					.getName()));
			String info = checkCommands(value);
			if (info != null) {
				return info;
			}
		}
		return null;
	}

	private String checkCommands(String value) {
		if (contains(value, XOTclKeywords.XOTclCommandClassArgs)) {
			return "#Class#" + value;
		}
		if (contains(value, XOTclKeywords.XOTclCommandObjectArgs)) {
			return "#Object#" + value;
		}
		return null;
	}

	private boolean contains(String value, String[] commands) {
		for (int q = 0; q < commands.length; q++) {
			if (value.equals(commands[q])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param command
	 * @param commandName
	 * @param context
	 * @return
	 */
	private String checkInstanceOperations(TclCommand command,
			String commandName, ITclModelBuildContext context) {
		if (command.getArguments().isEmpty()) {
			return null;
		}
		TclArgument subcmd = command.getArguments().get(0);
		if (quickTestName(TclParseUtil.tclSplit(commandName))) {
			XOTclClassAttribute type = findXOTclType(commandName);
			if (type != null && isSymbol(subcmd)) {
				context.addAttribute(type);
				return check(subcmd);
			}
		}

		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @param subcmd
	 */
	private String check(TclArgument subcmd) {
		String value = asSymbol(subcmd);
		if (!"create".equals(value)) {
			String info = checkCommands(value);
			if (info != null) {
				return info;
			}
		} else {
			return "#Class#$newInstance";
		}
		return "#Class#$ProcCall";
	}

	/**
	 * @param string
	 * @return
	 */
	private XOTclClassAttribute findXOTclType(String string) {
		// TODO implement it
		return null;
	}

	/**
	 * @param parts
	 * @return
	 */
	private boolean quickTestName(String[] parts) {
		// TODO Auto-generated method stub
		return true;
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
