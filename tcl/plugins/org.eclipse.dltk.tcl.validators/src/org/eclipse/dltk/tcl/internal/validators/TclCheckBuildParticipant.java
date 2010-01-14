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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.internal.validators.ChecksExtensionManager.TclCheckInfo;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;
import org.eclipse.emf.common.util.EList;

public class TclCheckBuildParticipant implements IBuildParticipant {

	public static boolean TESTING_DO_CHECKS = true;
	public static boolean TESTING_DO_OPERATIONS = true;

	private final TclCheckInfo[] checks = ChecksExtensionManager.getInstance()
			.getChecks();
	private final CheckPreferenceManager preferences = new CheckPreferenceManager(
			TclValidatorsCore.getDefault().getPluginPreferences());

	public TclCheckBuildParticipant(IScriptProject project) {
	}

	public void build(IBuildContext context) throws CoreException {
		try {
			if (!TESTING_DO_OPERATIONS) {
				return;
			}

			final ISourceModule module = context.getSourceModule();
			TclErrorCollector errorCollector = new TclErrorCollector();
			TclModule tclModule = TclBuildContext.getStatements(context);
			EList<TclCommand> commands = tclModule.getStatements();
			final ISourceLineTracker lineTracker = context.getLineTracker();
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
									scriptProject, lineTracker);
						}
					}
				}
			}
			final IProblemReporter reporter = context.getProblemReporter();
			// report errors to the build context
			errorCollector.reportAll(reporter, lineTracker);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
