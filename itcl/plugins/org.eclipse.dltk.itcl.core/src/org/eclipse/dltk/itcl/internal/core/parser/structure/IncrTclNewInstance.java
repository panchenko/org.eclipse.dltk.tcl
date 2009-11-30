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

import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IClass;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.tcl.structure.TclModelProblem;

public class IncrTclNewInstance extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context)
			throws TclModelProblem {
		if (command.getArguments().isEmpty()) {
			return false;
		}
		IClass type = IncrTclNames.getType(context);
		if (type == null) {
			return false;
		}
		TclArgument varName = command.getArguments().get(0);
		if (isSymbol(varName)) {
			FieldInfo fi = new FieldInfo();
			fi.declarationStart = command.getStart();
			fi.nameSourceStart = varName.getStart();
			fi.nameSourceEnd = varName.getEnd() - 1;
			fi.name = asSymbol(varName);
			fi.modifiers = IIncrTclModifiers.AccIncrTcl;
			ITclTypeHandler typeHandler = context.get(ITclTypeResolver.class)
					.resolveMemberType(fi, command.getEnd(), fi.name);
			if (context.getRequestor().enterFieldCheckDuplicates(fi)) {
				context.getRequestor().exitField(command.getEnd());
			}
			typeHandler.leave(context.getRequestor());
		}
		return false;
	}

}
