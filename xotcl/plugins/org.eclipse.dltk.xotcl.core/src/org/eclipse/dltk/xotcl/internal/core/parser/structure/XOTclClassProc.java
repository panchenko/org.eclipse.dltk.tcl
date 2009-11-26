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
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.core.parser.processors.tcl.Messages;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;
import org.eclipse.dltk.tcl.structure.TclProcessorUtil;
import org.eclipse.dltk.xotcl.core.IXOTclModifiers;

public class XOTclClassProc extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		XOTclType type = XOTclType.get(context);
		if (type == null) {
			return false;
		}
		if (command.getArguments().size() < 4) {
			throw new TclModelProblem(
					Messages.TclProcProcessor_Wrong_Number_of_Arguments);
		}
		TclArgument procName = command.getArguments().get(1);
		if (!isSymbol(procName)) {
			throw new TclModelProblem("Wrong proc name", procName);
		}
		TypeInfo ti = new TypeInfo();
		ti.modifiers = Modifiers.AccNameSpace;
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = procName.getStart();
		ti.nameSourceEnd = procName.getEnd() - 1;
		ITclTypeHandler resolvedType = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), type.getSimpleName());
		//
		final MethodInfo mi = new MethodInfo();
		mi.declarationStart = command.getStart();
		mi.nameSourceStart = procName.getStart();
		mi.nameSourceEnd = procName.getEnd() - 1;
		mi.name = asSymbol(procName);
		mi.modifiers = IXOTclModifiers.AccXOTcl;
		List<Parameter> params = new ArrayList<Parameter>();
		parseRawParameters(command.getArguments().get(2), params);
		fillParameters(mi, params);
		context.getRequestor().enterMethodRemoveSame(mi);
		//
		TclArgument body = command.getArguments().get(3);
		context.parse(TclProcessorUtil.asString(body), body.getStart());
		//
		context.getRequestor().exitMethod(command.getEnd());
		resolvedType.leave(context.getRequestor());
		return false;
	}

}
