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

import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;

public class IncrTclClass extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		if (command.getArguments().size() != 2) {
			return false;
		}
		final TclArgument className = command.getArguments().get(0);
		if (!isSymbol(className)) {
			return false;
		}
		final TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = className.getStart();
		ti.nameSourceEnd = className.getEnd() - 1;
		ti.modifiers = IIncrTclModifiers.AccIncrTcl;
		final String name = asSymbol(className);
		ITclTypeHandler resolvedType = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), name);
		IncrTclNames.create(context).addType(resolvedType);
		//
		resolvedType.leave(context.getRequestor());
		return false;
	}
}
