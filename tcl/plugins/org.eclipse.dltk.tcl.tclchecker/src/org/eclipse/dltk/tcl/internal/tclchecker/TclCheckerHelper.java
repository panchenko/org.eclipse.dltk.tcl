/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerVersion;
import org.eclipse.dltk.tcl.tclchecker.model.configs.MessageState;
import org.eclipse.dltk.utils.PlatformFileUtils;
import org.eclipse.dltk.validators.core.CommandLine;
import org.eclipse.dltk.validators.core.IValidatorOutput;

public final class TclCheckerHelper {

	private static final String PCX_OPTION = "-pcx"; //$NON-NLS-1$
	private static final String NO_PCX_OPTION = "-nopcx"; //$NON-NLS-1$

	private static final String SUPPRESS_OPTION = "-suppress"; //$NON-NLS-1$
	private static final String CHECK_OPTION = "-check"; //$NON-NLS-1$

	private static final String SUMMARY_OPTION = "-summary"; //$NON-NLS-1$
	private static final String VERBOSE_OPTION = "-verbose"; //$NON-NLS-1$

	public static boolean buildCommandLine(
			CheckerEnvironmentInstance environmentInstance,
			CheckerConfig config, CommandLine cmdLine,
			IEnvironment environment, IScriptProject project,
			IValidatorOutput console) {
		IFileHandle validatorFile = PlatformFileUtils
				.findAbsoluteOrEclipseRelativeFile(environment, new Path(
						environmentInstance.getExecutablePath()));
		cmdLine.add(validatorFile.toOSString());

		if (console.isEnabled() && config.isSummary()) {
			cmdLine.add(SUMMARY_OPTION);
			cmdLine.add(VERBOSE_OPTION);
		}

		// cmdLine.add(QUIET_OPTION);

		if (project != null && config.isUseTclVer()) {
			try {
				final IInterpreterInstall install = ScriptRuntime
						.getInterpreterInstall(project);
				if (install != null) {
					final String version = PackagesManager.getInstance()
							.getPackageVersion(install, "Tcl"); //$NON-NLS-1$
					if (version != null && version.length() != 0) {
						if (version.startsWith("8.")) { //$NON-NLS-1$
							int pos = version.indexOf('.', 2);
							if (pos < 0) {
								pos = version.length();
							}
							cmdLine.add("-use"); //$NON-NLS-1$
							/*
							 * Initially the Tcl<ver> argument was surrounded
							 * with double quotes, but on Linux this command if
							 * started from Eclipse work with errors [DLTK-839]
							 * (but from shell the same command work fine).
							 */
							cmdLine.add("Tcl" + version.substring(0, pos)); //$NON-NLS-1$
						}
					}
				}
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}

		if (config.getMode() != null) {
			final String[] options = config.getMode().getOptions();
			if (options.length != 0) {
				cmdLine.add(options);
			}
		}

		// Suppress
		if (config.isIndividualMessageStates()) {
			for (Map.Entry<String, MessageState> entry : config
					.getMessageStates()) {
				if (MessageState.CHECK.equals(entry.getValue())) {
					cmdLine.add(CHECK_OPTION);
					cmdLine.add(shortMessageId(entry.getKey()));
				} else if (MessageState.SUPPRESS.equals(entry.getValue())) {
					cmdLine.add(SUPPRESS_OPTION);
					cmdLine.add(shortMessageId(entry.getKey()));
				}
			}
		}

		if (!environmentInstance.isUsePcxFiles()) {
			cmdLine.add(NO_PCX_OPTION);
		} else {
			// pcx paths
			for (final String pcx : environmentInstance.getPcxFileFolders()) {
				IFileHandle handle = PlatformFileUtils
						.findAbsoluteOrEclipseRelativeFile(environment,
								new Path(pcx));
				cmdLine.add(PCX_OPTION);
				if (handle.exists()) {
					cmdLine.add(handle.toOSString());
				} else {
					cmdLine.add(pcx);
				}
			}
		}
		String cliOptions = environmentInstance.getInstance()
				.getCommandLineOptions();
		if (cliOptions != null && cliOptions.length() != 0) {
			cmdLine.add(new CommandLine(cliOptions));
		}
		cliOptions = config.getCommandLineOptions();
		if (cliOptions != null && cliOptions.length() != 0) {
			cmdLine.add(new CommandLine(cliOptions));
		}
		if (CheckerVersion.VERSION5.equals(environmentInstance.getInstance()
				.getVersion())) {
			cmdLine.add("-as"); //$NON-NLS-1$
			cmdLine.add("script"); //$NON-NLS-1$
		}
		return true;
	}

	/**
	 * @param messageId
	 * @return
	 */
	private static String shortMessageId(String messageId) {
		final int index = messageId
				.indexOf(TclCheckerProblemDescription.MESSAGE_ID_SEPARATOR);
		if (index >= 0) {
			return messageId.substring(index
					+ TclCheckerProblemDescription.MESSAGE_ID_SEPARATOR
							.length());
		} else {
			return messageId;
		}
	}

	private static boolean isValidPath(IEnvironment environment, String path) {
		if (path != null && path.length() != 0) {
			final Path pathObj = new Path(path);
			if (!pathObj.isEmpty()) {
				final IFileHandle file = PlatformFileUtils
						.findAbsoluteOrEclipseRelativeFile(environment, pathObj);
				return file.exists();
			}
		}
		return false;
	}

	public static boolean canExecuteTclChecker(
			CheckerEnvironmentInstance environmentInstance,
			IEnvironment environment) {
		return isValidPath(environment, environmentInstance.getExecutablePath());
	}
}
