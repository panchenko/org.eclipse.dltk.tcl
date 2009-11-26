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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclProcessorUtil;
import org.eclipse.dltk.xotcl.core.IXOTclModifiers;

public class XOTclClassCreate extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context) {
		int index = 0;
		if (index >= command.getArguments().size()) {
			report(context, command, "Incorrect XOTcl class declaration",
					ProblemSeverities.Error);
			return false;
		}
		TclArgument name = command.getArguments().get(index++);
		// Skip create command (optional)
		if (isSymbol(name) && XOTclModelDetector.CREATE.equals(asSymbol(name))) {
			if (index >= command.getArguments().size()) {
				report(context, command, "Incorrect XOTcl class declaration",
						ProblemSeverities.Error);
				return false;
			}
			name = command.getArguments().get(index++);
		}
		if (!isSymbol(name)) {
			report(context, command, "A name or 'create' command expected.",
					ProblemSeverities.Error);
			return false;
		}
		List<String> superclasses = new ArrayList<String>();
		List<TclArgument> fields = new ArrayList<TclArgument>();
		while (index < command.getArguments().size()) {
			TclArgument argument = command.getArguments().get(index++);
			if (isSymbol(argument)) {
				String optionName = asSymbol(argument);
				if ("-superclass".equals(optionName)) {
					if (index < command.getArguments().size()) {
						TclArgument sc = command.getArguments().get(index++);
						superclasses.add(asSymbol(sc));
					}
				} else if ("-parameter".equals(optionName)) {
					if (index < command.getArguments().size()) {
						TclArgument parameters = command.getArguments().get(
								index++);
						for (TclArgument a : parse(parameters)) {
							if (a instanceof StringArgument) {
								String aa = ((StringArgument) a).getValue();
								if (aa.startsWith("{") && aa.endsWith("}")
										|| aa.startsWith("\"")
										&& aa.endsWith("\'")) {
									List<TclArgument> parts = parse(a);
									if (!parts.isEmpty()) {
										TclArgument first = parts.get(0);
										if (first instanceof StringArgument) {
											fields.add(first);
										}
									}
								} else {
									fields.add(a);
								}
							}
						}
					}
				}
			}
		}
		final TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = name.getStart();
		ti.nameSourceEnd = name.getEnd() - 1;
		ti.name = asSymbol(name);
		ti.superclasses = superclasses.toArray(new String[superclasses.size()]);
		ti.modifiers = IXOTclModifiers.AccXOTcl;
		ITclTypeHandler typeHanlder = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), ti.name);
		XOTclNames.create(context).addType(typeHanlder);
		for (TclArgument parameter : fields) {
			final FieldInfo fi = new FieldInfo();
			fi.name = TclProcessorUtil.asString(parameter);
			fi.nameSourceStart = parameter.getStart();
			fi.nameSourceEnd = parameter.getEnd() - 1;
			fi.declarationStart = fi.nameSourceStart;
			fi.modifiers = Modifiers.AccPrivate;
			context.getRequestor().enterFieldCheckDuplicates(fi);
			context.getRequestor().exitField(fi.nameSourceEnd);
		}
		typeHanlder.leave(context.getRequestor());
		return false;
	}

	private List<TclArgument> parse(TclArgument argument) {
		int offset = argument.getStart();
		final String content;
		if (argument instanceof StringArgument) {
			final String value = ((StringArgument) argument).getValue();
			int len = value.length();
			if (len >= 2) {
				if (value.charAt(0) == '{' && value.charAt(len - 1) == '}') {
					content = value.substring(1, len - 1);
					++offset;
				} else if (value.charAt(0) == '"'
						&& value.charAt(len - 1) == '"') {
					content = value.substring(1, len - 1);
					++offset;
				} else {
					content = value;
				}
			} else {
				content = value;
			}
		} else {
			content = SimpleCodePrinter.getArgumentString(argument, argument
					.getStart());
		}
		TclParser parser = new TclParser();
		parser.setGlobalOffset(offset);
		List<TclCommand> commands = parser.parse(content);
		List<TclArgument> result = new ArrayList<TclArgument>();
		for (TclCommand c : commands) {
			result.add(c.getName());
			result.addAll(c.getArguments());
		}
		return result;
	}
}
