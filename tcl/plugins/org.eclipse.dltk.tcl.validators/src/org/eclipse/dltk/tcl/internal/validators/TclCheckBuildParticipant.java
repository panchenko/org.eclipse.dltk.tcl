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

package org.eclipse.dltk.tcl.internal.validators;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.internal.validators.ChecksExtensionManager.TclCheckInfo;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;
import org.eclipse.dltk.validators.core.IBuildParticipant;

public class TclCheckBuildParticipant implements IBuildParticipant {
	public static boolean TESTING_DO_CHECKS = true;
	public static boolean TESTING_DO_OPERATIONS = true;

	@Override
	public void build(ISourceModule module, ModuleDeclaration ast,
			final IProblemReporter reporter) throws CoreException {
		if( !TESTING_DO_OPERATIONS) {
			return;
		}
		final TclCheckInfo[] checks = ChecksExtensionManager.getInstance()
				.getChecks();

		final CheckPreferenceManager preferences = new CheckPreferenceManager(
				TclValidatorsCore.getDefault().getPluginPreferences());
		String source = module.getSource();

		TclParser parser = new TclParser();
		TclErrorCollector errorCollector = new TclErrorCollector();
		NamespaceScopeProcessor processor = DefinitionManager.createProcessor();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, false);
		List<TclCommand> commands = parser.parse(source, errorCollector,
				processor);
		if (TESTING_DO_CHECKS) {
			// Perform all checks.
			for (int i = 0; i < checks.length; i++) {
				TclCheckInfo info = checks[i];
				if (preferences.isEnabled(info)) {
					if (info.getCommandName() == null) {
						ITclCheck check = info.getCheck();
						check.checkCommands(commands, errorCollector,
								preferences.getOptions(info));
					}
				}
			}
		}

		// Locate and execute all enabled checks

		final CodeModel codeModel = new CodeModel(source);
		errorCollector.reportAll(new ITclErrorReporter() {
			@Override
			public void report(int code, String message, int start, int end,
					int kind) {
				int line = codeModel.getLineNumber(start, end);
				DefaultProblem problem = new DefaultProblem(
						message,
						code,
						null,
						kind == ITclErrorReporter.ERROR ? ProblemSeverities.Error
								: ProblemSeverities.Warning, start, end, line);
				reporter.reportProblem(problem);
			}
		});
	}
}
