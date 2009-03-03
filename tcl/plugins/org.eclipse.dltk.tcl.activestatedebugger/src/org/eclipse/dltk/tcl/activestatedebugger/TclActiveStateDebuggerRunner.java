/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger;

import java.net.URI;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.dbgp.IDbgpStackLevel;
import org.eclipse.dltk.dbgp.IDbgpStreamFilter;
import org.eclipse.dltk.debug.core.DLTKDebugConstants;
import org.eclipse.dltk.debug.core.DebugOption;
import org.eclipse.dltk.debug.core.IDbgpService;
import org.eclipse.dltk.debug.core.model.DefaultDebugOptions;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.debug.core.model.IScriptDebugThreadConfigurator;
import org.eclipse.dltk.internal.debug.core.model.ScriptDebugTarget;
import org.eclipse.dltk.internal.launching.LaunchConfigurationUtils;
import org.eclipse.dltk.launching.ExternalDebuggingEngineRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.launching.debug.DbgpConstants;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
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
	public static final String ENGINE_ID = "org.eclipse.dltk.tcl.activestatedebugger"; //$NON-NLS-1$

	private static final String ADDRESS_KEY = "-dbgp"; //$NON-NLS-1$
	private static final String SHELL_KEY = "-app-shell"; //$NON-NLS-1$
	private static final String IDE_KEY = "-ide-key"; //$NON-NLS-1$
	private static final String SCRIPT_KEY = "-app-file"; //$NON-NLS-1$
	private static final String LOG_KEY = "-log"; //$NON-NLS-1$
	private static final String LOG_FILE_KEY = "-logfile"; //$NON-NLS-1$
	private static final String ARGS_SEPARATOR = "--"; //$NON-NLS-1$

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

		final String exe = getInstall().getInstallLocation().toOSString();
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
				newConfig.addEnvVar("TCLDEVKIT_LOCAL", pdxFiles.toOSString()); //$NON-NLS-1$
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
		List<?> args = config.getScriptArgs();
		newConfig.clearScriptArgs();
		newConfig.addScriptArg(ARGS_SEPARATOR);
		newConfig.addScriptArgs(args);

		return newConfig;
	}

	protected IScriptDebugTarget createDebugTarget(ILaunch launch,
			IDbgpService dbgpService) throws CoreException {
		final ScriptDebugTarget target = new ScriptDebugTarget(
				getDebugModelId(), dbgpService, getSessionId(launch
						.getLaunchConfiguration()), launch, null,
				new TclDebugOptions());
		if (createPreferencesLookupDelegate(launch).getBoolean(
				getDebugPreferenceQualifier(),
				TclDebugConstants.DEBUG_STREAM_FILTER_VWAIT_RENAME_WARNING)) {
			target
					.setStreamFilters(new IDbgpStreamFilter[] { new TclActiveStateVwaitRenameFilter() });
		}
		return target;
	}

	private static class TclDebugOptions extends DefaultDebugOptions {
		public boolean get(BooleanOption option) {
			if (option == DebugOption.DBGP_ASYNC) {
				return false;
			} else if (option == DebugOption.DBGP_BREAKPOINT_UPDATE_LINE_NUMBER) {
				return false;
			} else if (option == DebugOption.ENGINE_SUPPORT_DATATYPES) {
				return false;
			} else if (option == DebugOption.ENGINE_STOP_BEFORE_FIRST_LINE) {
				return false;
			}
			return super.get(option);
		}

		@Override
		public IDbgpStackLevel[] filterStackLevels(IDbgpStackLevel[] levels) {
			if (levels.length > 1) {
				final int lastIndex = levels.length - 1;
				final URI uri = levels[lastIndex].getFileURI();
				if (DLTKDebugConstants.UNKNOWN_SCHEME.equals(uri.getScheme())
						&& "<console>".equals(uri.getFragment())) { //$NON-NLS-1$
					IDbgpStackLevel[] result = new IDbgpStackLevel[lastIndex];
					System.arraycopy(levels, 0, result, 0, lastIndex);
					return result;
				}
			}
			return levels;
		}
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
	 * @see
	 * org.eclipse.dltk.launching.DebuggingEngineRunner#getLogFileNamePreferenceKey
	 * ()
	 */
	protected String getLogFileNamePreferenceKey() {
		return TclActiveStateDebuggerConstants.LOG_FILE_NAME;
	}

	@Override
	protected IScriptDebugThreadConfigurator createThreadConfigurator(
			ILaunchConfiguration configuration) {
		IProject project = LaunchConfigurationUtils.getProject(configuration);
		return new TclActiveStateDebugThreadConfigurator(DLTKCore
				.create(project), new PreferencesLookupDelegate(project));
	}

	protected void abort(String message, Throwable exception, int code)
			throws CoreException {
		if (code == ScriptLaunchConfigurationConstants.ERR_DEBUGGING_ENGINE_NOT_CONFIGURED) {
			super
					.abort(
							Messages.TclActiveStateDebuggerRunner_errorEngineNotConfigured,
							exception, code);
		}
		super.abort(message, exception, code);
	}

}
