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
package org.eclipse.dltk.tcl.internal.parser;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskAstParser;
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
import org.eclipse.dltk.validators.core.IBuildParticipant;

public class TclTodoTaskAstParser extends TodoTaskAstParser implements
		IBuildParticipant {

	public TclTodoTaskAstParser(ITodoTaskPreferences preferences) {
		super(preferences);
	}

	protected boolean isSimpleNode(ASTNode node) {
		if (node instanceof TclBlockExpression) {
			return true;
		}
		return super.isSimpleNode(node);
	}

}
