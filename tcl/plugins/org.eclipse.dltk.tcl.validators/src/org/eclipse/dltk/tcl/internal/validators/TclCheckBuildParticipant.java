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
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.internal.validators.ChecksExtensionManager.TclCheckInfo;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;

public class TclCheckBuildParticipant implements IBuildParticipant {

	public static boolean TESTING_DO_CHECKS = true;
	public static boolean TESTING_DO_OPERATIONS = true;

	private final NamespaceScopeProcessor processor;
	private final TclCheckInfo[] checks = ChecksExtensionManager.getInstance()
			.getChecks();
	private final CheckPreferenceManager preferences = new CheckPreferenceManager(
			TclValidatorsCore.getDefault().getPluginPreferences());

	public TclCheckBuildParticipant(IScriptProject project) {
		processor = DefinitionManager.getInstance().createProcessor();
	}

	public void build(IBuildContext context) throws CoreException {
		try {
			if (!TESTING_DO_OPERATIONS) {
				return;
			}

			final ISourceModule module = context.getSourceModule();
			final String source = new String(context.getContents());

			TclParser parser = new TclParser();
			TclErrorCollector errorCollector = new TclErrorCollector();

			parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR,
					false);
			List<TclCommand> commands = parser.parse(source, errorCollector,
					processor);
			TclBuildContext.setStatements(context, commands);
			final CodeModel codeModel = new CodeModel(source);
			TclBuildContext.setCodeModel(context, codeModel);
			if (TESTING_DO_CHECKS) {
				// Perform all checks.
				for (int i = 0; i < checks.length; i++) {
					TclCheckInfo info = checks[i];
					if (preferences.isEnabled(info)) {
						if (info.getCommandName() == null) {
							ITclCheck check = info.getCheck();
							IScriptProject scriptProject = module
									.getScriptProject();
							check.checkCommands(commands, errorCollector,
									preferences.getOptions(info),
									scriptProject, codeModel);
						}
					}
				}
			}
			final ISourceLineTracker lineTracker = context.getLineTracker();
			final IProblemReporter reporter = context.getProblemReporter();
			// report errors to the build context
			errorCollector.reportAll(new ITclErrorReporter() {
				public void report(int code, String message,
						String[] extraMessage, int start, int end, int kind) {
					reporter
							.reportProblem(new DefaultProblem(
									message,
									code,
									extraMessage,
									kind == ITclErrorReporter.ERROR ? ProblemSeverities.Error
											: ProblemSeverities.Warning, start,
									end, lineTracker
											.getLineNumberOfOffset(start)));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
