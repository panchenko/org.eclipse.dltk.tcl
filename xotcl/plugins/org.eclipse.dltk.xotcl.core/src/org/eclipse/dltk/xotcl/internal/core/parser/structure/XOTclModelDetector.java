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
import org.eclipse.dltk.tcl.core.ITclCommandDetector.CommandInfo;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuilderDetector;
import org.eclipse.dltk.tcl.structure.TclModelBuilderUtil;
import org.eclipse.dltk.xotcl.internal.core.XOTclKeywords;
import org.eclipse.dltk.xotcl.internal.core.parser.XOTclCommandDetector.XOTclGlobalClassParameter;

public class XOTclModelDetector extends TclModelBuilderUtil implements
		ITclModelBuilderDetector {

	private static final String CLASS_PREFIX = "#Class#"; //$NON-NLS-1$
	private static final String OBJECT_PREFIX = "#Object#"; //$NON-NLS-1$
	static final String CREATE = "create"; //$NON-NLS-1$
	static final String INSTPROC = "instproc"; //$NON-NLS-1$
	static final String PROC = "proc"; //$NON-NLS-1$

	private static final String CLASS_CREATE = CLASS_PREFIX + CREATE;
	private static final String CLASS_NEW_INSTANCE = CLASS_PREFIX
			+ "$newInstance"; //$NON-NLS-1$
	//	private static final String CLASS_PROC_CALL = CLASS_PREFIX + "$ProcCall"; //$NON-NLS-1$

	private static final String OBJECT_CREATE = OBJECT_PREFIX + CREATE;

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
			// XOTclClassAttribute type = findXOTclType("Class");
			// if (type != null) {
			// context.addAttribute(type);
			// }
			if (contains(value, XOTclKeywords.XOTclCommandClassArgs)) {
				return CLASS_PREFIX + value;
			}
			String info = checkCreateType(context, command, commandName,
					subcmd, value);
			if (info != null) {
				return info;
			}
			if (INTERPRET_CLASS_UNKNOWN_AS_CREATE) {
				return CLASS_CREATE;
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
			// XOTclClassAttribute type = findXOTclType("Object");
			// if (type != null) {
			// context.addAttribute(type);
			// }
			if (contains(value, XOTclKeywords.XOTclCommandObjectArgs)) {
				return OBJECT_PREFIX + value;
			}
			String info = checkCreateType(context, command, commandName,
					subcmd, value);
			if (info != null) {
				return info;
			}
			// // Else unknown command or create command.
			if (INTERPRET_OBJECT_UNKNOWN_AS_CREATE) {
				return OBJECT_CREATE;
			}
		}
		return null;
	}

	private String checkCreateType(ITclModelBuildContext context,
			TclCommand command, String commandName, TclArgument arg,
			String value) {
		if (value.equals(INSTPROC) || value.equals(PROC) || value.equals("set")) {
			// context.addAttribute(new XOTclClassAttribute(commandName, command
			// .getName()));
			String info = checkCommands(value);
			if (info != null) {
				return info;
			}
		}
		return null;
	}

	private String checkCommands(String value) {
		if (contains(value, XOTclKeywords.XOTclCommandClassArgs)) {
			return CLASS_PREFIX + value;
		}
		if (contains(value, XOTclKeywords.XOTclCommandObjectArgs)) {
			return OBJECT_PREFIX + value;
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
		final TclArgument subcmd = command.getArguments().get(0);
		if (isSymbol(subcmd)) {
			final XOTclNames names = XOTclNames.get(context);
			if (names != null) {
				final XOTclType type = names.resolve(commandName);
				if (type != null) {
					type.saveTo(context);
					return check(subcmd);
				}
			}
		}
		// externally defined class?
		if (commandName.length() >= 3) {
			if (commandName.startsWith("::")) {
				commandName = commandName.substring(2);
			}
			if (commandName.indexOf("::") > 0
					|| Character.isUpperCase(commandName.charAt(0))) {
				if (asSymbol(subcmd).equals(CREATE)) {
					new XOTclType(commandName, null).saveTo(context);
					return CLASS_NEW_INSTANCE;
				}
			}
		}

		return null;
	}

	/**
	 * @param subcmd
	 */
	private String check(TclArgument subcmd) {
		String value = asSymbol(subcmd);
		if (!CREATE.equals(value)) {
			String info = checkCommands(value);
			if (info != null) {
				return info;
			}
		} else {
			return CLASS_NEW_INSTANCE;
		}
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
