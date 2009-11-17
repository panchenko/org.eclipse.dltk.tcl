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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.internal.core.parser.processors.tcl.Messages;
import org.eclipse.dltk.tcl.internal.structure.ITclTypeHanlder;
import org.eclipse.dltk.tcl.internal.structure.TclProcessorUtil;
import org.eclipse.dltk.tcl.internal.structure.TclTypeResolver;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext.ITclModelHandler;
import org.eclipse.emf.common.util.EList;

public class TclProcModelBuilder extends AbstractTclCommandModelBuilder {

	private static class MethodExit implements ITclModelHandler {

		final int sourceEnd;

		public MethodExit(int sourceEnd) {
			this.sourceEnd = sourceEnd;
		}

		public void leave(ISourceElementRequestor requestor) {
			requestor.exitMethod(sourceEnd);
		}

	}

	public boolean process(TclCommand command, ITclModelBuildContext context) {
		if (command.getArguments().size() != 3) {
			report(context, command,
					Messages.TclProcProcessor_Wrong_Number_of_Arguments,
					ProblemSeverities.Error);
			return false;
		}
		final TclArgument arg0 = command.getArguments().get(0);
		String procName = TclProcessorUtil.asString(arg0);
		if (procName == null || procName.length() == 0) {
			report(context, command.getArguments().get(0),
					Messages.TclProcProcessor_Empty_Proc_Name,
					ProblemSeverities.Error);
			return false;
		}
		final MethodInfo mi = new MethodInfo();
		mi.declarationStart = command.getStart();
		mi.nameSourceStart = arg0.getStart();
		mi.nameSourceEnd = arg0.getEnd() - 1;
		mi.name = procName;
		mi.modifiers = TclVisibilityUtils.isPrivate(procName) ? Modifiers.AccPrivate
				: Modifiers.AccPublic;
		List<Parameter> parameters = parseParameters(command.getArguments()
				.get(1));
		if (!parameters.isEmpty()) {
			mi.parameterNames = new String[parameters.size()];
			boolean hasDefaults = false;
			for (Parameter parameter : parameters) {
				if (parameter.defaultValue != null) {
					hasDefaults = true;
					break;
				}
			}
			if (hasDefaults) {
				mi.parameterInitializers = new String[parameters.size()];
			}
			for (int i = 0; i < parameters.size(); ++i) {
				Parameter parameter = parameters.get(i);
				mi.parameterNames[i] = parameter.name;
				if (hasDefaults) {
					mi.parameterInitializers[i] = parameter.defaultValue;
				}
			}
		}
		// TODO if (extendedExitRequired(method)) {
		// exit = getExitExtended(method);
		// } else {
		final ITclTypeHanlder typeHanlder = context.get(TclTypeResolver.class)
				.resolveType(mi, command.getEnd(), procName);
		context.getRequestor().enterMethodRemoveSame(mi);
		context.addHandler(command, new MethodExit(command.getEnd()));
		context.addHandler(command, typeHanlder);
		return true;
	}

	private static class Parameter {
		final String name;
		final String defaultValue;

		public Parameter(String name) {
			this(name, null);
		}

		public Parameter(String name, String defaultValue) {
			this.name = name;
			this.defaultValue = defaultValue;
		}
	}

	private List<Parameter> parseParameters(TclArgument argument) {
		if (argument instanceof StringArgument) {
			return Collections.singletonList(new Parameter(
					((StringArgument) argument).getValue()));
		} else if (argument instanceof TclArgumentList) {
			final TclArgumentList list = (TclArgumentList) argument;
			final List<Parameter> parameters = new ArrayList<Parameter>(list
					.getArguments().size());
			for (TclArgument arg : list.getArguments()) {
				if (arg instanceof StringArgument) {
					parameters.add(new Parameter(((StringArgument) arg)
							.getValue()));
				} else if (arg instanceof TclArgumentList) {
					final EList<TclArgument> argWithInitializer = ((TclArgumentList) arg)
							.getArguments();
					if (argWithInitializer.size() >= 2) {
						parameters.add(new Parameter(
								asSymbol(argWithInitializer.get(0)),
								TclProcessorUtil.asString(argWithInitializer
										.get(1))));
					} else if (argWithInitializer.size() == 1) {
						parameters.add(new Parameter(
								asSymbol(argWithInitializer.get(0))));
					} else {
						parameters.add(new Parameter(Util.EMPTY_STRING));
					}
				} else {
					parameters.add(new Parameter(asSymbol(arg)));
				}
			}
			return parameters;
		} else {
			return Collections.singletonList(new Parameter(
					asSymbol(argument)));
		}
	}

}
