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
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class UnreachableCodeCheck implements ITclCheck {

	public UnreachableCodeCheck() {
		// TODO Auto-generated constructor stub
	}

	public void checkCommands(final List<TclCommand> tclCommands,
			final ITclErrorReporter reporter, Map<String, String> options,
			IScriptProject project, CodeModel codeModel) {
		TclParserUtils.traverse(tclCommands, new TclVisitor() {
			private boolean check = true;
			private boolean error = false;

			int level = 0;

			int errorLevel = -1;

			@Override
			public boolean visit(TclCommand tclCommand) {
				if (!check)
					return false;
				if (error) {

					reporter.report(ICheckKinds.UNREACHABLE_CODE,
							"Unreachable code", null, tclCommand.getStart(),
							tclCommand.getEnd(), ITclErrorReporter.ERROR);
					error = check = false;
					return false;
				} else {
					if (tclCommand == null
							|| tclCommand.getDefinition() == null) {
						level++;
						return true;
					}
					if ("return".equals(tclCommand.getDefinition().getName())) {
						error = true;
						errorLevel = level;
						return false;
					}
					level++;
					return true;
				}
			}

			public void endVisit(TclCommand tclCommand) {
				level--;
				if (level != errorLevel)
					error = false;
				check = true;
			}
		});
	}
}