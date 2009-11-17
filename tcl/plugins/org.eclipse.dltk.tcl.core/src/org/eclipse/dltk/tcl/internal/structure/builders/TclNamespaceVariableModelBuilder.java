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
package org.eclipse.dltk.tcl.internal.structure.builders;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;

/**
 * <quote>This command is normally used within a <code>>namespace eval</code>
 * command to create one or more variables within a namespace. Each variable
 * name is initialized with value. The value for the last variable is
 * optional<quote>
 */
public class TclNamespaceVariableModelBuilder extends
		AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context) {
		for (int i = 0; i < command.getArguments().size(); i += 2) {
			processField(command, command.getArguments().get(i), context);
		}
		return false;
	}

}
