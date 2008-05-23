/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger;

import java.util.List;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.debug.core.model.IScriptDebugThreadConfigurator;
import org.eclipse.dltk.launching.ExternalDebuggingEngineRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.debug.DbgpConstants;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;
import org.eclipse.dltk.utils.PlatformFileUtils;

/**
 * Debugging engine implementation for ActiveState's tcl debugging engine.
 * 
 * <p>
 * see: <a href=
 * "http://aspn.activestate.com/ASPN/docs/Komodo/komodo-doc-debugtcl.html">
 * http://aspn.activestate.com/ASPN/docs/Komodo/komodo-doc-debugtcl.html</a>
 * </p>
 */
public class TclActiveStateDebuggerRunner extends ExternalDebuggingEngineRunner {
	public static final String ENGINE_ID = "org.eclipse.dltk.tcl.activestatedebugger";

	private static final String ADDRESS_KEY = "-dbgp";
	private static final String SHELL_KEY = "-app-shell";
	private static final String IDE_KEY = "-ide-key";
	private static final String SCRIPT_KEY = "-app-file";
	private static final String LOG_KEY = "-log";
	private static final String LOG_FILE_KEY = "-logfile";
	private static final String ARGS_SEPARATOR = "--";

	public TclActiveStateDebuggerRunner(IInterpreterInstall install) {
		super(install);
	}

	/*
	 * @see
	 * org.eclipse.dltk.launching.ExternalDebuggingEngineRunner#alterConfig(
	 * org.eclipse.dltk.launching.InterpreterConfig, java.lang.String)
	 */
	protected InterpreterConfig alterConfig(InterpreterConfig config,
			PreferencesLookupDelegate delegate) {

		IFileHandle file = getDebuggingEnginePath(delegate);

		final String exe = getInstall().getInstallLocation().toString();
		final String host = (String) config
				.getProperty(DbgpConstants.HOST_PROP);
		final String port = (String) config
				.getProperty(DbgpConstants.PORT_PROP);
		final String sessionId = (String) config
				.getProperty(DbgpConstants.SESSION_ID_PROP);

		IEnvironment env = getInstall().getEnvironment();

		String pathKeyValue = getDebuggingPreference(delegate,
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY);

		String path = (String) EnvironmentPathUtils.decodePaths(pathKeyValue)
				.get(env);

		InterpreterConfig newConfig = (InterpreterConfig) config.clone();

		if (path != null) {
			IFileHandle pdxFiles = PlatformFileUtils
					.findAbsoluteOrEclipseRelativeFile(env, new Path(path));

			if (pdxFiles.exists()) {
				newConfig.addEnvVar("TCLDEVKIT_LOCAL", pdxFiles.toOSString());
			}
		}

		// Additional property
		newConfig.setProperty(OVERRIDE_EXE, file.toString());

		// Interpreter arguments
		newConfig.addInterpreterArg(ADDRESS_KEY);
		newConfig.addInterpreterArg(host + ':' + port);

		newConfig.addInterpreterArg(SHELL_KEY);
		newConfig.addInterpreterArg(exe);

		newConfig.addInterpreterArg(IDE_KEY);
		newConfig.addInterpreterArg(sessionId);

		String logFileName = getLogFileName(delegate, sessionId);
		if (logFileName != null) {
			newConfig.addInterpreterArg(LOG_KEY);
			newConfig.addInterpreterArg(LOG_FILE_KEY);
			newConfig.addInterpreterArg(logFileName);
		}

		// newConfig.addInterpreterArg("-doinstrument");
		// newConfig.addInterpreterArg("{itcl}");

		newConfig.addInterpreterArg(SCRIPT_KEY);

		// Script arguments
		List args = config.getScriptArgs();
		newConfig.clearScriptArgs();
		newConfig.addScriptArg(ARGS_SEPARATOR);
		newConfig.addScriptArgs(args);

		return newConfig;
	}

	/*
	 * @see
	 * org.eclipse.dltk.launching.DebuggingEngineRunner#getDebuggingEngineId()
	 */
	protected String getDebuggingEngineId() {
		return ENGINE_ID;
	}

	/*
	 * @seeorg.eclipse.dltk.launching.ExternalDebuggingEngineRunner#
	 * getDebuggingEnginePreferenceKey()
	 */
	protected String getDebuggingEnginePreferenceKey() {
		return TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY;
	}

	/*
	 * @seeorg.eclipse.dltk.launching.DebuggingEngineRunner#
	 * getDebuggingEnginePreferenceQualifier()
	 */
	protected String getDebuggingEnginePreferenceQualifier() {
		return TclActiveStateDebuggerPlugin.PLUGIN_ID;
	}

	/*
	 * @see
	 * org.eclipse.dltk.launching.DebuggingEngineRunner#getDebugPreferenceQualifier
	 * ()
	 */
	protected String getDebugPreferenceQualifier() {
		return TclDebugPlugin.PLUGIN_ID;
	}

	/*
	 * @seeorg.eclipse.dltk.launching.DebuggingEngineRunner#
	 * getLoggingEnabledPreferenceKey()
	 */
	protected String getLoggingEnabledPreferenceKey() {
		return TclActiveStateDebuggerConstants.ENABLE_LOGGING;
	}

	/*
	 * @see
	 * org.eclipse.dltk.launching.DebuggingEngineRunner#getLogFileNamePreferenceKey
	 * ()
	 */
	protected String getLogFileNamePreferenceKey() {
		return TclActiveStateDebuggerConstants.LOG_FILE_NAME;
	}

	/*
	 * @see
	 * org.eclipse.dltk.launching.DebuggingEngineRunner#getLogFilePathPreferenceKey
	 * ()
	 */
	protected String getLogFilePathPreferenceKey() {
		return TclActiveStateDebuggerConstants.LOG_FILE_PATH;
	}

	protected IScriptDebugThreadConfigurator createThreadConfigurator() {
		return new TclActiveStateDebugThreadConfigurator();
	}

}
