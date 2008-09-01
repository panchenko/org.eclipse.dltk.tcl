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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class ProcArgsDefCheck implements ITclCheck {

	public ProcArgsDefCheck() {
		// TODO Auto-generated constructor stub
	}

	public void checkCommands(List<TclCommand> commands,
			final ITclErrorReporter reporter, Map<String, String> options,
			IScriptProject project) {

		TclParserUtils.traverse(commands, new TclVisitor() {
			@Override
			public boolean visit(TclCommand command) {
				Command definition = command.getDefinition();
				if (definition == null) {
					return true;
				}
				if (definition.getName().equals("proc")) {
					TclArgument arg = command.getArguments().get(1);

					if (arg instanceof StringArgument
							|| arg instanceof ComplexString
							|| arg instanceof Substitution
							|| arg instanceof VariableReference) {
						return true;
					} else if (arg instanceof TclArgumentList) {
						if (checkArgumentsList((TclArgumentList) arg)) {
							return true;
						} else {
							reporter.report(
									ICheckKinds.CHECK_BAD_ARG_DEFINITION,
									"Bad argument definition", arg.getStart(),
									arg.getEnd(), ITclErrorReporter.WARNING);
							return false;
						}
					}
				}
				return true;
			}

			public boolean checkArgumentsList(TclArgumentList list) {
				boolean wasArgs = false;
				boolean wasDef = false;
				for (TclArgument arg : list.getArguments()) {
					TclArgument argName = null;
					TclArgument defValue = null;
					List<TclArgument> res = checkArgument(arg);
					if (res.size() == 0)
						return false;// impossible
					if (res.size() > 0)
						argName = res.get(0);
					if (res.size() > 1)
						defValue = res.get(1);
					int start = argName.getStart();
					int end = (defValue == null) ? argName.getEnd() : defValue
							.getEnd();

					if (argName instanceof StringArgument
							&& "args".equals(((StringArgument) argName)
									.getValue())) {
						wasArgs = true;
						if (defValue != null) {
							wasDef = true;
							reporter.report(ICheckKinds.CHECK_ARGS_DEFAULT,
									"\"args\" cannot be defaulted", start, end,
									ITclErrorReporter.WARNING);
						}
					} else if (argName instanceof StringArgument
							&& "{}".equals(((StringArgument) argName)
									.getValue())) {
						reporter.report(ICheckKinds.CHECK_ARG_WITH_NO_NAME,
								"Argument with no name", start, end,
								ITclErrorReporter.WARNING);
					} else {
						if (wasArgs) {
							reporter.report(ICheckKinds.CHECK_ARG_AFTER_ARGS,
									"Argument specified after \"args\"", start,
									end, ITclErrorReporter.WARNING);
						}
						if (defValue != null) {
							wasDef = true;
						} else if (wasDef) {
							reporter.report(
									ICheckKinds.CHECK_NON_DEF_AFTER_DEF,
									"Non-default arg specified after default",
									start, end, ITclErrorReporter.WARNING);
						}
					}

				}
				return true;
			}

			// argument with no name
			public List<TclArgument> checkArgument(TclArgument arg) {
				List<TclArgument> res = new ArrayList<TclArgument>();
				if (arg instanceof StringArgument
						|| arg instanceof ComplexString
						|| arg instanceof Substitution
						|| arg instanceof VariableReference) {
					res.add(arg);
				} else if (arg instanceof TclArgumentList) {
					List<TclArgument> args = ((TclArgumentList) arg)
							.getArguments();
					if (args.size() > 0)
						res.add(args.get(0));
					if (args.size() > 1)
						res.add(args.get(1));
					if (args.get(0) instanceof TclArgumentList) {
						TclArgument sub = ((TclArgumentList) args.get(0))
								.getArguments().get(0);
						res.set(0, sub);
						if (sub instanceof StringArgument
								&& ((StringArgument) sub).getValue()
										.startsWith("{")) {
							reporter.report(
									ICheckKinds.CHECK_BAD_ARG_DEFINITION,
									"Bad argument definition", sub.getStart(),
									sub.getEnd(), ITclErrorReporter.WARNING);
						}
					}
				}
				return res;
			}
		});
	}

}