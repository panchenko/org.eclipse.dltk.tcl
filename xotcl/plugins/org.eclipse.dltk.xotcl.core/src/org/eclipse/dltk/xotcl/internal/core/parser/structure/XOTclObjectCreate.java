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

import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;
import org.eclipse.dltk.xotcl.core.IXOTclModifiers;

public class XOTclObjectCreate extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		int index = 0;
		if (index >= command.getArguments().size()) {
			throw new TclModelProblem("Incorrect XOTcl object declaration",
					command);
		}
		TclArgument name = command.getArguments().get(index++);
		// Skip create command (optional)
		if (isSymbol(name) && XOTclModelDetector.CREATE.equals(asSymbol(name))) {
			if (index >= command.getArguments().size()) {
				throw new TclModelProblem("Incorrect XOTcl object declaration",
						command);
			}
			name = command.getArguments().get(index++);
		}
		if (!isSymbol(name)) {
			throw new TclModelProblem("A name or 'create' command expected.",
					command);
		}
		final TypeInfo ti = new TypeInfo();
		ti.declarationStart = command.getStart();
		ti.nameSourceStart = name.getStart();
		ti.nameSourceEnd = name.getEnd() - 1;
		ti.name = asSymbol(name);
		ti.superclasses = CharOperation.NO_STRINGS;
		ti.modifiers = IXOTclModifiers.AccXOTcl
				| IXOTclModifiers.AccXOTclObject;
		ITclTypeHandler typeHanlder = context.get(ITclTypeResolver.class)
				.resolveType(ti, command.getEnd(), ti.name);
		typeHanlder.leave(context.getRequestor());
		XOTclNames.create(context).addType(typeHanlder);
		return false;
	}

}
