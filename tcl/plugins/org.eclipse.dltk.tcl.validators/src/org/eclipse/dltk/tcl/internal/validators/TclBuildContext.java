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

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.core.SourceParserUtil;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;

public class TclBuildContext {

	public static TclModule getStatements(IBuildContext context) {
		// Parse and store statements here.
		ModuleDeclaration declaration = SourceParserUtil.getModuleDeclaration(
				context.getSourceModule(), context.getProblemReporter());
		if (declaration instanceof TclModuleDeclaration) {
			TclModuleDeclaration decl = (TclModuleDeclaration) declaration;
			TclModule tclModule = decl.getTclModule();
			if (tclModule != null) {
				return tclModule;
			}
		}
		TclParser parser = new TclParser();
		TclErrorCollector collector = new TclErrorCollector();
		TclModule module = parser.parseModule(
				new String(context.getContents()), collector, DefinitionManager
						.getInstance().getCoreProcessor());
		collector.reportAll(context.getProblemReporter(), context
				.getLineTracker());
		return module;
	}
}
