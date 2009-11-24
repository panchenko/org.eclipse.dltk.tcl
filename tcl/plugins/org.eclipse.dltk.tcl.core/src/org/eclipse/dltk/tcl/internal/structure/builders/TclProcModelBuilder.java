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

import java.util.List;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.internal.core.parser.processors.tcl.Messages;
import org.eclipse.dltk.tcl.internal.structure.ITclTypeHanlder;
import org.eclipse.dltk.tcl.internal.structure.TclProcessorUtil;
import org.eclipse.dltk.tcl.internal.structure.TclTypeResolver;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext.ITclModelHandler;

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
		if (mi.name.indexOf("::") != -1) {
			final String[] parts = TclParseUtil.tclSplit(mi.name);
			mi.name = parts[parts.length - 1];
		}
		mi.modifiers = TclVisibilityUtils.isPrivate(procName) ? Modifiers.AccPrivate
				: Modifiers.AccPublic;
		List<Parameter> parameters = parseParameters(command.getArguments()
				.get(1));
		fillParameters(mi, parameters);
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

}
