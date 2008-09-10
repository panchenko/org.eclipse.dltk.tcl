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

package org.eclipse.dltk.tcl.internal.validators.checks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.IScopeProcessor;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class CommandRedefinitionCheck implements ITclCheck {
	public CommandRedefinitionCheck() {
	}

	public void checkCommands(final List<TclCommand> tclCommands,
			final ITclErrorReporter reporter, Map<String, String> options,
			IScriptProject project, final CodeModel codeModel) {
		final IScopeProcessor processor = DefinitionManager.getInstance().createProcessor();
		TclParserUtils.traverse(tclCommands, new TclVisitor() {
			Map<String, Integer> userCommands = new HashMap<String, Integer>();

			@Override
			public boolean visit(TclCommand tclCommand) {
				processor.processCommand(tclCommand);
				if (tclCommand == null || tclCommand.getDefinition() == null
						|| !tclCommand.isMatched()) {
					return true;
				}
				if ("proc".equals(tclCommand.getDefinition().getName())) {
					TclArgument nameArgument = tclCommand.getArguments().get(0);
					if (nameArgument instanceof StringArgument) {
						String current = ((StringArgument) nameArgument)
								.getValue();
						current = processor.getQualifiedName(current);
						Command[] definitions = processor
								.getCommandDefinition(current);
						if (definitions != null && definitions.length != 0)
							reporter.report(
									ICheckKinds.BUILTIN_COMMAND_REDEFINITION,
									"Built-in command redefinition", null,
									nameArgument.getStart(), nameArgument
											.getEnd(),
									ITclErrorReporter.WARNING);
						Set<String> names = userCommands.keySet();
						for (String name : names) {
							if (current.startsWith("::")) {
								current = current.substring(2);
							}
							if (name.equals(current))
								reporter.report(
										ICheckKinds.USER_COMMAND_REDEFINITION,
										"A procedure of the same name already defined on line "
												+ userCommands.get(name), null,
										nameArgument.getStart(), nameArgument
												.getEnd(),
										ITclErrorReporter.WARNING);
						}
						int start = tclCommand.getStart();
						userCommands.put(current, codeModel.getLineNumber(
								start, start + 1));
					}
				}
				return true;
			}

			@Override
			public void endVisit(TclCommand command) {
				processor.endProcessCommand();
			}
		});
	}
}
