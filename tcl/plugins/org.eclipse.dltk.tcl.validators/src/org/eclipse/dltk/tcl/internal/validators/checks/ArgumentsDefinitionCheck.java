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

import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.ArgumentDefinition;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class ArgumentsDefinitionCheck implements ITclCheck {

	public ArgumentsDefinitionCheck() {
		// TODO Auto-generated constructor stub
	}

	public void checkCommands(List<TclCommand> commands,
			final ITclErrorReporter reporter, Map<String, String> options,
			IScriptProject project) {
		TclParserUtils.traverse(commands, new TclVisitor() {
			@Override
			public boolean visit(TclCommand tclCommand) {
				if (tclCommand == null || tclCommand.getDefinition() == null
						|| !tclCommand.isMatched()) {
					return true;
				}
				if ("proc".equals(tclCommand.getDefinition().getName())) {
					return check(tclCommand.getArguments().get(1));
				}
				if ("apply".equals(tclCommand.getDefinition().getName())) {
					return check(((TclArgumentList) tclCommand.getArguments()
							.get(0)).getArguments().get(0));
				}
				return true;
			}

			public boolean check(TclArgument tclArgument) {
				List<ArgumentDefinition> list = ArgumentDefinition
						.get(tclArgument);

				boolean wasArgs = false;
				boolean wasDef = false;
				for (int i = 0; i < list.size(); i++) {
					ArgumentDefinition definition = list.get(i);
					if (definition.getName() == null) {
						reporter.report(ICheckKinds.CHECK_BAD_ARG_DEFINITION,
								"Bad argument definition", null, definition
										.getArgument().getStart(), definition
										.getArgument().getEnd(),
								ITclErrorReporter.WARNING);
					} else {
						for (int j = 0; j < i; j++) {
							ArgumentDefinition other = list.get(j);
							if (definition.getName().equals(other.getName()))
								reporter.report(
										ICheckKinds.CHECK_SAME_ARG_NAME,
										"Argument with the same name "
												+ "has been declared", null,
										definition.getArgument().getStart(),
										definition.getArgument().getEnd(),
										ITclErrorReporter.WARNING);
						}
					}
					if ("args".equals(definition.getName())) {
						wasArgs = true;
						if (definition.isDefaulted()) {
							wasDef = true;
							reporter.report(ICheckKinds.CHECK_ARGS_DEFAULT,
									"\"args\" cannot be defaulted", null,
									definition.getArgument().getStart(),
									definition.getArgument().getEnd(),
									ITclErrorReporter.WARNING);
						}
					} else {
						if (wasArgs) {
							reporter.report(ICheckKinds.CHECK_ARG_AFTER_ARGS,
									"Argument specified after \"args\"", null,
									definition.getArgument().getStart(),
									definition.getArgument().getEnd(),
									ITclErrorReporter.WARNING);
						}
						if (definition.isDefaulted()) {
							wasDef = true;
						} else if (wasDef) {
							reporter.report(
									ICheckKinds.CHECK_NON_DEF_AFTER_DEF,
									"Non-default arg specified after default",
									null, definition.getArgument().getStart(),
									definition.getArgument().getEnd(),
									ITclErrorReporter.WARNING);
						}
					}

				}
				return true;
			}
		});
	}
}
