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
package org.eclipse.dltk.itcl.internal.core.parser.structure;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.IElementRequestor.MethodInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IClass;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMethod;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.core.codeassist.TclVisibilityUtils;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;

public class IncrTclBody extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		if (command.getArguments().size() != 3) {
			throw new TclModelProblem("Wrong number of arguments");
		}
		TclArgument nameArg = command.getArguments().get(0);
		if (!isSymbol(nameArg)) {
			throw new TclModelProblem("className::function expected", nameArg);
		}
		String classFunctionName = asSymbol(nameArg);
		int pos = classFunctionName.lastIndexOf("::");
		if (pos <= 0) {
			throw new TclModelProblem("className::function expected", nameArg);
		}
		String className = classFunctionName.substring(0, pos);
		String procName = classFunctionName.substring(pos + 2);
		IClass clazz = null;
		IncrTclNames names = IncrTclNames.get(context);
		if (names != null) {
			clazz = names.resolve(className);
		}
		int procModifiers = IIncrTclModifiers.AccIncrTcl
				| (TclVisibilityUtils.isPrivate(procName) ? Modifiers.AccPrivate
						: Modifiers.AccPublic);
		if (clazz == null) {
			report(context, nameArg, "Class not found",
					ProblemSeverities.Warning);
		} else {
			className = clazz.getName();
			if (className.startsWith("::")) {
				className = className.substring(2);
			}
			IMethod method = clazz.findMethod(procName);
			if (method != null) {
				procModifiers = method.getModifiers();
			} else {
				report(context, nameArg, "Method not found",
						ProblemSeverities.Warning);
			}
		}
		TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = nameArg.getStart();
		ti.nameSourceEnd = nameArg.getEnd() - 1;
		ti.modifiers = Modifiers.AccNameSpace;// IIncrTclModifiers.AccIncrTcl;
		if (clazz != null) {
			ti.superclasses = clazz.getSuperClasses();
		}
		ITclTypeHandler resolvedType = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), className);
		MethodInfo mi = new MethodInfo();
		mi.declarationStart = command.getStart();
		mi.nameSourceStart = nameArg.getStart();
		mi.nameSourceEnd = nameArg.getEnd() - 1;
		mi.modifiers = procModifiers;
		mi.name = procName;
		List<Parameter> parameters = new ArrayList<Parameter>();
		parseRawParameters(command.getArguments().get(1), parameters);
		fillParameters(mi, parameters);
		context.getRequestor().enterMethodRemoveSame(mi);
		TclArgument body = command.getArguments().get(2);
		if (body instanceof StringArgument) {
			int offset = body.getStart();
			String source = ((StringArgument) body).getValue();
			if (source.startsWith("{") && source.endsWith("}")) {
				++offset;
				source = source.substring(1, source.length() - 1);
			}
			context.parse(source, offset);
		}
		context.getRequestor().exitMethod(command.getEnd());
		resolvedType.leave(context.getRequestor());
		return false;
	}
}
