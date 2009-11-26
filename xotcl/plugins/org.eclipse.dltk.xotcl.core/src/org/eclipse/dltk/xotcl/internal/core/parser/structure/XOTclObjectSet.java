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

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.compiler.IElementRequestor.FieldInfo;
import org.eclipse.dltk.compiler.IElementRequestor.TypeInfo;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;
import org.eclipse.dltk.tcl.structure.ITclTypeResolver;
import org.eclipse.dltk.xotcl.core.IXOTclModifiers;

public class XOTclObjectSet extends AbstractTclCommandModelBuilder {

	public boolean process(final TclCommand command,
			ITclModelBuildContext context) {
		final XOTclType type = XOTclType.get(context);
		if (type == null) {
			return false;
		}
		if (command.getArguments().size() >= 3) {
			final TclArgument field = command.getArguments().get(1);
			if (isSymbol(field)) {
				TypeInfo ti = new TypeInfo();
				ti.modifiers = Modifiers.AccNameSpace;
				ti.declarationStart = command.getStart();
				ti.nameSourceStart = field.getStart();
				ti.nameSourceEnd = field.getEnd() - 1;
				ITclTypeHandler resolvedType = context.get(
						ITclTypeResolver.class).resolveType(ti,
						command.getEnd(), type.getName());
				processField(command, field, asSymbol(field),
						IXOTclModifiers.AccXOTcl, context, resolvedType,
						new FieldInitializer() {
							@Override
							public void initialize(FieldInfo info) {
								info.declarationStart = command.getStart();
							}
						});
			}
		}
		return false;
	}

}
