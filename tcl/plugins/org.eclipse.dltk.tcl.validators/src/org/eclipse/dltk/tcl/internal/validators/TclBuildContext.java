/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.validators;

import java.util.List;

import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.tcl.ast.TclCommand;

public class TclBuildContext {

	private static final String ATTR_NEW_AST = "NEW_AST"; //$NON-NLS-1$

	@SuppressWarnings("unchecked")
	public static List<TclCommand> getStatements(IBuildContext context) {
		return (List<TclCommand>) context.get(ATTR_NEW_AST);
	}

	public static void setStatements(IBuildContext context,
			List<TclCommand> commands) {
		context.set(ATTR_NEW_AST, commands);
	}

}
