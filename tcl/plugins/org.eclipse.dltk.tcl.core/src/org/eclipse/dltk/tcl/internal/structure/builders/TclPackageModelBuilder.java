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
		if ("require".equals(subcmd)) {
			return processRequire(command, context);
		} else {
			// TODO handle other sub commands
			return false;
		}
	}

	private boolean processRequire(TclCommand command,
			ITclModelBuildContext context) {
		int index = 1;
		if (command.getArguments().size() <= index) {
			return false;
		}
		TclArgument pkg = command.getArguments().get(index);
		String packageName = TclProcessorUtil.asString(pkg);
		if ("-exact".equals(packageName)) {
			++index;
			if (command.getArguments().size() <= index) {
				return false;
			}
			pkg = command.getArguments().get(index);
			packageName = TclProcessorUtil.asString(pkg);
		}
		final ImportInfo importInfo = new ImportInfo();
		importInfo.sourceStart = pkg.getStart();
		importInfo.sourceEnd = pkg.getEnd() - 1;
		importInfo.containerName = org.eclipse.dltk.tcl.core.TclConstants.REQUIRE_CONTAINER;
		importInfo.name = packageName;
		if (command.getArguments().size() > index + 1) {
			final TclArgument version = command.getArguments().get(index + 1);
			importInfo.version = TclProcessorUtil.asString(version);
			importInfo.sourceEnd = version.getEnd() - 1;
		}
		context.getRequestor().acceptImport(importInfo);
		return false;
	}

}
