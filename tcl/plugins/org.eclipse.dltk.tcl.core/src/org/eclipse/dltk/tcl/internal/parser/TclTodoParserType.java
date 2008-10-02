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

import org.eclipse.core.runtime.Preferences;
import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.core.builder.AbstractTodoTaskBuildParticipantType;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPlugin;

public class TclTodoParserType extends AbstractTodoTaskBuildParticipantType {

	private static final String ID = "org.eclipse.dltk.tcl.todo"; //$NON-NLS-1$
	private static final String NAME = "Tcl TODO task parser"; //$NON-NLS-1$

	public TclTodoParserType() {
		super(ID, NAME);
	}

	public String getNature() {
		return TclNature.NATURE_ID;
	}

	protected Preferences getPreferences() {
		return TclPlugin.getDefault().getPluginPreferences();
	}

	protected IBuildParticipant getBuildParticipant(
			ITodoTaskPreferences preferences) {
		return new TclTodoTaskAstParser(preferences);
	}
}
