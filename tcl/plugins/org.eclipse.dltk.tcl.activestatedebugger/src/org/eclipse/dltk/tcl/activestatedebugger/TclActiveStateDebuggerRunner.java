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
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.dbgp.IDbgpStreamFilter;
import org.eclipse.dltk.debug.core.DLTKDebugConstants;
import org.eclipse.dltk.debug.core.DebugOption;
import org.eclipse.dltk.debug.core.IDbgpService;
import org.eclipse.dltk.debug.core.model.DefaultDebugOptions;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.debug.core.model.IScriptDebugThreadConfigurator;
import org.eclipse.dltk.debug.core.model.IScriptStackFrame;
import org.eclipse.dltk.internal.debug.core.model.ScriptDebugTarget;
import org.eclipse.dltk.internal.launching.LaunchConfigurationUtils;
import org.eclipse.dltk.launching.ExternalDebuggingEngineRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.launching.debug.DbgpConnectionConfig;
import org.eclipse.dltk.launching.debug.DebuggingUtils;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.TclActiveStateDebuggerEnvironment;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;
import org.eclipse.dltk.tcl.launching.TclLaunchConfigurationConstants;
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
	 * @see ExternalDebuggingEngineRunner#alterConfig(InterpreterConfig,String)
	 */
	@Override
	protected InterpreterConfig alterConfig(InterpreterConfig config,
			PreferencesLookupDelegate delegate) {

		IFileHandle file = getDebuggingEnginePath(delegate);

		final String exe = getInstall().getInstallLocation().toOSString();
		DbgpConnectionConfig dbgpConfig = DbgpConnectionConfig.load(config);
		IEnvironment env = getInstall().getEnvironment();

		String pathKeyValue = getDebuggingPreference(delegate,
				TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY);

		String path = EnvironmentPathUtils.decodePaths(pathKeyValue).get(env);

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
		newConfig.addInterpreterArg(dbgpConfig.getHost() + ':'
				+ dbgpConfig.getPort());

		newConfig.addInterpreterArg(SHELL_KEY);
		newConfig.addInterpreterArg(exe);

		newConfig.addInterpreterArg(IDE_KEY);
		newConfig.addInterpreterArg(dbgpConfig.getSessionId());

		String logFileName = getLogFileName(delegate, dbgpConfig.getSessionId());
		if (logFileName != null) {
			newConfig.addInterpreterArg(LOG_KEY);
			newConfig.addInterpreterArg(LOG_FILE_KEY);
			newConfig.addInterpreterArg(logFileName);
		}

		// newConfig.addInterpreterArg("-doinstrument");
		// newConfig.addInterpreterArg("{itcl}");

		newConfig.addInterpreterArg(SCRIPT_KEY);

		// Script arguments
		List<String> args = config.getScriptArgs();
		newConfig.clearScriptArgs();
		newConfig.addScriptArg(ARGS_SEPARATOR);
		newConfig.addScriptArgs(args);

		return newConfig;
	}

	@Override
	protected IScriptDebugTarget createDebugTarget(ILaunch launch,
			IDbgpService dbgpService) throws CoreException {
		final ScriptDebugTarget target = new ScriptDebugTarget(
				getDebugModelId(), dbgpService, getSessionId(launch
						.getLaunchConfiguration()), launch, null,
				new TclDebugOptions());
		if (createPreferencesLookupDelegate(launch).getBoolean(
				getDebugPreferenceQualifier(),
				TclDebugConstants.DEBUG_STREAM_FILTER_COMMAND_RENAME_WARNING)) {
			target
					.setStreamFilters(new IDbgpStreamFilter[] { new TclActiveStateCommandRenameFilter() });
		}
		return target;
	}

	private static class TclDebugOptions extends DefaultDebugOptions {

		private static final String CONSOLE = "<console>"; //$NON-NLS-1$

		@Override
		public boolean get(BooleanOption option) {
			if (option == DebugOption.DBGP_ASYNC) {
				return false;
			} else if (option == DebugOption.DBGP_BREAKPOINT_UPDATE_LINE_NUMBER) {
				return false;
			} else if (option == DebugOption.ENGINE_SUPPORT_DATATYPES) {
				return false;
			} else if (option == DebugOption.ENGINE_STOP_BEFORE_CODE) {
				return false;
			} else if (option == DebugOption.ENGINE_VALIDATE_STACK) {
				return true;
			}
			return super.get(option);
		}

		@Override
		public IScriptStackFrame[] filterStackLevels(IScriptStackFrame[] frames) {
			if (frames.length > 1) {
				final int lastIndex = frames.length - 1;
				final URI uri = frames[lastIndex].getSourceURI();
				if (DLTKDebugConstants.UNKNOWN_SCHEME.equals(uri.getScheme())
						&& CONSOLE.equals(uri.getFragment())) {
					IScriptStackFrame[] result = new IScriptStackFrame[lastIndex];
					System.arraycopy(frames, 0, result, 0, lastIndex);
					return result;
				}
			}
			return super.filterStackLevels(frames);
		}

		@Override
		public boolean isValidStack(IScriptStackFrame[] frames) {
			if (frames.length == 1) {
				final URI uri = frames[0].getSourceURI();
				if (DLTKDebugConstants.UNKNOWN_SCHEME.equals(uri.getScheme())
						&& CONSOLE.equals(uri.getFragment())) {
					return false;
				}
			}
			return true;
		}
	}

	/*
	 * @see DebuggingEngineRunner#getDebuggingEngineId()
	 */
	@Override
	protected String getDebuggingEngineId() {
		return ENGINE_ID;
	}

	/*
	 * @see ExternalDebuggingEngineRunner#getDebuggingEnginePreferenceKey()
	 */
	@Override
	protected String getDebuggingEnginePreferenceKey() {
		return TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY;
	}

	/*
	 * @see DebuggingEngineRunner#getDebuggingEnginePreferenceQualifier()
	 */
	@Override
	protected String getDebuggingEnginePreferenceQualifier() {
		return TclActiveStateDebuggerPlugin.PLUGIN_ID;
	}

	/*
	 * @see DebuggingEngineRunner#getDebugPreferenceQualifier()
	 */
	@Override
	protected String getDebugPreferenceQualifier() {
		return TclDebugPlugin.PLUGIN_ID;
	}

	/*
	 * @see DebuggingEngineRunner#getLogFileNamePreferenceKey()
	 */
	@Override
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

	@Override
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

	/*
	 * @see DebuggingEngineRunner#isLoggingEnabled()
	 */
	@Override
	protected boolean isLoggingEnabled(IPreferencesLookupDelegate delegate) {
		final Map<String, Boolean> values = TclActiveStateDebuggerEnvironment
				.decodeBooleans(delegate.getString(
						getDebuggingEnginePreferenceQualifier(),
						TclActiveStateDebuggerConstants.LOG_ENABLE_KEY));
		final Boolean b = values.get(getInstall().getEnvironmentId());
		return b == null || b.booleanValue();
	}

	@Override
	protected String getProcessType() {
		return TclLaunchConfigurationConstants.ID_TCL_PROCESS_TYPE;
	}

	@Override
	protected IFileHandle getDebuggingEnginePath(
			PreferencesLookupDelegate delegate) {
		IFileHandle handle = super.getDebuggingEnginePath(delegate);
		if (handle == null) {
			final String paths = delegate.getString(
					getDebuggingEnginePreferenceQualifier(),
					getDebuggingEnginePreferenceKey());
			final IEnvironment env = getInstall().getEnvironment();
			String path = EnvironmentPathUtils.decodePaths(paths).get(env);
			if (path == null) {
				path = DebuggingUtils.getDefaultEnginePath(env, ENGINE_ID);
				if (path != null && path.length() != 0) {
					return PlatformFileUtils.findAbsoluteOrEclipseRelativeFile(
							env, new Path(path));
				}
			}
		}
		return handle;
	}

}
