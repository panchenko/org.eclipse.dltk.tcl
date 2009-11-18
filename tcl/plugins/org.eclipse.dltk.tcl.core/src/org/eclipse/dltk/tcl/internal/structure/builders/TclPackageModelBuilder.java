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

import org.eclipse.dltk.compiler.IElementRequestor.ImportInfo;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.structure.TclProcessorUtil;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;

public class TclPackageModelBuilder extends AbstractTclCommandModelBuilder {

	public boolean process(TclCommand command, ITclModelBuildContext context) {
		if (command.getArguments().isEmpty()) {
			return false;
		}
		final String subcmd = TclProcessorUtil.asString(command.getArguments()
				.get(0));
		if (!"require".equals(subcmd)) {
			return true;
		}
		if (command.getArguments().size() < 2) {
			return false;
		}
		TclArgument packageName = command.getArguments().get(1);
		ImportInfo importInfo = new ImportInfo();
		importInfo.sourceStart = packageName.getStart();
		importInfo.sourceEnd = packageName.getEnd() - 1;
		importInfo.containerName = org.eclipse.dltk.tcl.core.TclConstants.REQUIRE_CONTAINER;
		importInfo.name = TclProcessorUtil.asString(packageName);
		if (command.getArguments().size() > 2) {
			importInfo.version = TclProcessorUtil.asString(command
					.getArguments().get(2));
		}
		context.getRequestor().acceptImport(importInfo);
		return true;
	}

}
