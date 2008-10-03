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
package org.eclipse.dltk.tcl.internal.validators.task;

import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.builder.AbstractTodoTaskBuildParticipantType;
import org.eclipse.dltk.tcl.core.TclPlugin;

public class TclTodoParserFactory extends AbstractTodoTaskBuildParticipantType {

	@Override
	protected ITodoTaskPreferences getPreferences(IScriptProject project) {
		return new TodoTaskPreferences(TclPlugin.getDefault()
				.getPluginPreferences());
	}

}
