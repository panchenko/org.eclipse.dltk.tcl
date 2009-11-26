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
package org.eclipse.dltk.tcl.internal.structure.builders;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclProcessorUtil;
import org.eclipse.emf.common.util.EList;

public class TclNamespaceModelBuilder extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context) {
		final EList<TclArgument> arguments = command.getArguments();
		if (arguments.isEmpty()) {
			report(context, command,
					"Syntax error: a namespace subcommand expected.",
					ProblemSeverities.Error);
			return false;
		}
		final String subcmd = TclProcessorUtil.asString(arguments.get(0));
		if (!"eval".equals(subcmd)) {
			return false;
		}
		if (arguments.size() < 3) {
			report(context, command,
					"Syntax error: namespace eval: name and script expected",
					ProblemSeverities.Error);
			return false;
		}
		TclArgument nameArg = arguments.get(1);
		TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = nameArg.getStart();
		ti.nameSourceEnd = nameArg.getEnd() - 1;
		ti.name = asSymbol(nameArg);
		ti.modifiers = Modifiers.AccNameSpace;

		ITclTypeHandler typeHanlder = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), ti.name);
		context.addHandler(command, typeHanlder);
		if (arguments.size() == 3 && arguments.get(2) instanceof Script) {
			return true;
		} else {
			final StringBuilder sb = new StringBuilder();
			final int offset = arguments.get(2).getStart();
			for (int i = 2; i < arguments.size(); ++i) {
				final TclArgument argument = arguments.get(i);
				final String value = SimpleCodePrinter.getArgumentString(
						argument, argument.getStart());
				int start = 0;
				int end = value.length();
				if (end >= 2 && value.charAt(0) == '{'
						&& value.charAt(end - 1) == '}') {
					++start;
					--end;
				}
				while (end > start
						&& Character.isWhitespace(value.charAt(end - 1))) {
					--end;
				}
				while (start < end
						&& Character.isWhitespace(value.charAt(start))) {
					++start;
				}
				// TODO optimization: copy spaces from array
				for (int j = offset + sb.length() - start; j < argument
						.getStart(); ++j) {
					sb.append(' ');
				}
				sb.append(value, start, end);
			}
			// TODO replace arguments with parsed content
			context.parse(sb.toString(), offset);
			context.leave(command);
			return false;
		}
	}
}
