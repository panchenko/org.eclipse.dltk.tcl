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
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.compiler.task.ITaskReporter;
import org.eclipse.dltk.compiler.task.ITodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskPreferences;
import org.eclipse.dltk.compiler.task.TodoTaskSimpleParser;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension2;
import org.eclipse.dltk.core.builder.IScriptBuilder.DependencyResponse;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.internal.parser.TclTodoTaskAstParser;
import org.eclipse.dltk.tcl.internal.validators.ChecksExtensionManager.TclCheckInfo;
import org.eclipse.dltk.tcl.internal.validators.packages.PackageRequireChecker;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;

public class TclCheckBuildParticipant implements IBuildParticipant,
		IBuildParticipantExtension, IBuildParticipantExtension2 {

	public static boolean TESTING_DO_CHECKS = true;
	public static boolean TESTING_DO_OPERATIONS = true;

	private PackageRequireChecker packagesBuilder = null;
	private final NamespaceScopeProcessor processor;
	private final TclCheckInfo[] checks = ChecksExtensionManager.getInstance()
			.getChecks();
	private final CheckPreferenceManager preferences = new CheckPreferenceManager(
			TclValidatorsCore.getDefault().getPluginPreferences());

	private final TodoTaskSimpleParser todoParser;

	public TclCheckBuildParticipant(IScriptProject project) {
		processor = DefinitionManager.getInstance().createProcessor();
		try {
			packagesBuilder = new PackageRequireChecker(project);
		} catch (IllegalStateException e) {
		} catch (CoreException e) {
		}
		todoParser = createTodoParser();
	}

	private static TodoTaskSimpleParser createTodoParser() {
		final ITodoTaskPreferences prefs = new TodoTaskPreferences(TclPlugin
				.getDefault().getPluginPreferences());
		if (prefs.isEnabled()) {
			final TclTodoTaskAstParser parser = new TclTodoTaskAstParser(prefs);
			if (parser.isValid()) {
				return parser;
			}
		}
		return null;
	}

	public void build(ISourceModule module, ModuleDeclaration ast,
			final IProblemReporter reporter) throws CoreException {
		try {
			if (!TESTING_DO_OPERATIONS) {
				return;
			}

			final String source = module.getSource();

			TclParser parser = new TclParser();
			TclErrorCollector errorCollector = new TclErrorCollector();

			parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR,
					false);
			List<TclCommand> commands = parser.parse(source, errorCollector,
					processor);
			final CodeModel codeModel = new CodeModel(source);
			if (packagesBuilder != null) {
				packagesBuilder.build(module, commands, reporter, codeModel);
			}
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
			if (todoParser != null) {
				ITaskReporter taskReporter = (ITaskReporter) reporter
						.getAdapter(ITaskReporter.class);
				if (taskReporter != null) {
					todoParser.parse(taskReporter, source.toCharArray());
				}
			}

			// Locate and execute all enabled checks
			errorCollector.reportAll(new ITclErrorReporter() {
				public void report(int code, String message,
						String[] extraMessage, int start, int end, int kind) {
					// if (extraMessage != null) {
					// for (int i = 0; i < extraMessage.length; i++) {
					// if (extraMessage[i].startsWith(SHORT)) {
					// message = message
					// + "\nExplanation:"
					// + extraMessage[i].substring(SHORT
					// .length());
					// }
					// }
					// }
					int line = codeModel.getLineNumber(start, start + 1);
					DefaultProblem problem = new DefaultProblem(
							message,
							code,
							extraMessage,
							kind == ITclErrorReporter.ERROR ? ProblemSeverities.Error
									: ProblemSeverities.Warning, start, end,
							line - 1);
					reporter.reportProblem(problem);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void beginBuild(int buildType) {
		if (packagesBuilder != null) {
			packagesBuilder.beginBuild(buildType);
		}
	}

	public void endBuild(IProgressMonitor monitor) {
		if (packagesBuilder != null) {
			packagesBuilder.endBuild(monitor);
		}
	}

	public void buildExternalModule(ISourceModule module, ModuleDeclaration ast)
			throws CoreException {
		if (packagesBuilder != null) {
			packagesBuilder.buildExternalModule(module, processor);
		}
	}

	@SuppressWarnings("unchecked")
	public DependencyResponse getDependencies(int buildType, Set localElements,
			Set externalElements, Set oldExternalFolders, Set externalFolders) {
		if (packagesBuilder != null) {
			return packagesBuilder.getDependencies(buildType, localElements,
					externalElements, oldExternalFolders, externalFolders);
		}
		return null;
	}
}
