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

import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.TclModelProblem;
import org.eclipse.dltk.xotcl.core.IXOTclModifiers;

public class XOTclClassNewInstance extends AbstractTclCommandModelBuilder {

	public boolean process(final TclCommand command,
			ITclModelBuildContext context) throws TclModelProblem {
		XOTclType type = XOTclType.get(context);
		if (type == null) {
			return false;
		}
		int index = 0;
		if (index >= command.getArguments().size()) {
			throw new TclModelProblem(
					"Incorrect XOTcl class instance declaration", command);
		}
		TclArgument name = command.getArguments().get(index++);
		// Skip create
		if (isSymbol(name) && XOTclModelDetector.CREATE.equals(asSymbol(name))) {
			if (index >= command.getArguments().size()) {
				throw new TclModelProblem(
						"Incorrect XOTcl class instance declaration", command);
			}
			name = command.getArguments().get(index++);
		}
		if (!isSymbol(name)) {
			throw new TclModelProblem("A name or 'create' command expected.",
					command);
		}
		processField(command, name, null, IXOTclModifiers.AccXOTcl, context,
				null, new FieldInitializer() {
					@Override
					public void initialize(FieldInfo info) {
						info.declarationStart = command.getStart();
					}
				});

		// final TypeInfo ti = new TypeInfo();
		// ti.declarationStart = command.getStart();
		// ti.nameSourceStart = name.getStart();
		// ti.nameSourceEnd = name.getEnd() - 1;
		// ti.name = asSymbol(name);
		// ti.superclasses = CharOperation.NO_STRINGS;
		// ti.modifiers = IXOTclModifiers.AccXOTcl
		// | IXOTclModifiers.AccXOTclObject;
		// ITclTypeHandler typeHanlder = context.get(ITclTypeResolver.class)
		// .resolveType(ti, command.getEnd(), ti.name);
		// typeHanlder.leave(context.getRequestor());
		// XOTclNames.create(context).addType(typeHanlder);

		// TODO Auto-generated method stub
		return false;
	}

}
