/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.internal.validators.checks;

import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclProc;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class UndefinedProcCheck implements ITclCheck {

	public UndefinedProcCheck() {
	}

	public void checkCommands(List<TclCommand> commands,
			final ITclErrorReporter reporter, Map<String, String> options,
			final IScriptProject project) {
		TclParserUtils.traverse(commands, new TclVisitor() {
			@Override
			public boolean visit(TclCommand command) {

				if (command.getDefinition() != null) {
					return true;
				}
				TclArgument name = command.getName();
				if (!(name instanceof StringArgument)) {
					return true;
				}
				String qName = command.getQualifiedName();
				if (qName == null) {
					return true;
				}
				IMixinElement[] elements = TclMixinModel.getInstance()
						.getMixin(project).find(
								qName.replaceAll("::",
										IMixinRequestor.MIXIN_NAME_SEPARATOR));
				String realName = ((StringArgument) name).getValue();
				if (realName.indexOf("::") != -1) {
					realName = realName
							.substring(realName.lastIndexOf("::") + 2);
				}
				boolean found = false;
				for (int i = 0; i < elements.length; i++) {
					Object[] objects = elements[i].getAllObjects();
					for (int j = 0; j < objects.length; j++) {
						if (objects[j] instanceof TclProc) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					reporter.report(ICheckKinds.CHECK_UNDEFINED_PROC,
							"Call to undefined proc:" + realName, name
									.getStart(), name.getEnd(),
							ITclErrorReporter.WARNING);
				}
				return true;

				// // Skip commands with definitions. They are found.
				// String name = command.getQualifiedName();
				// // Search all methods test
				// try {
				// SearchPattern createPattern = SearchPattern.createPattern(
				// name, IDLTKSearchConstants.METHOD,
				// IDLTKSearchConstants.DECLARATIONS,
				// SearchPattern.R_PATTERN_MATCH
				// | SearchPattern.R_EXACT_MATCH
				// | SearchPattern.R_CASE_SENSITIVE,
				// TclLanguageToolkit.getDefault());
				// SearchParticipant[] participants = new SearchParticipant[] {
				// SearchEngine
				// .getDefaultSearchParticipant() };
				// List sourceOnly = new SearchEngine().searchSourceOnly(
				// createPattern, participants, SearchEngine
				// .createWorkspaceScope(TclLanguageToolkit
				// .getDefault()),
				// new NullProgressMonitor());
				// if (sourceOnly == null || sourceOnly.size() == 0) {
				// reporter.report(ICheckKinds.CHECK_UNDEFINED_PROC,
				// "Call to undefined proc:" + name, command
				// .getName().getStart(), command
				// .getName().getEnd(),
				// ITclErrorReporter.WARNING);
				// }
				// } catch (CoreException e) {
				// e.printStackTrace();
				// }
				// return true;
			}
		});
	}
}
