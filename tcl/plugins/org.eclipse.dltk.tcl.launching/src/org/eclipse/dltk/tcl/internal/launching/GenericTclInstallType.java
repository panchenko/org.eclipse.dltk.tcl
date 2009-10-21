/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.launching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.internal.launching.DLTKLaunchingPlugin;
import org.eclipse.dltk.internal.launching.InterpreterMessages;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.LibraryLocation;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.internal.core.packages.ProcessOutputCollector;
import org.eclipse.dltk.tcl.launching.TclLaunchConfigurationDelegate;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;
import org.osgi.framework.Bundle;

public class GenericTclInstallType extends AbstractInterpreterInstallType {
	private static final String CORRECT_INTERPRETER_PATTERN = TclPackagesManager.END_OF_STREAM;

	private static final String INSTALL_TYPE_NAME = "Generic Tcl";
	/**
	 * @since 1.1
	 */
	public static final String TYPE_ID = "org.eclipse.dltk.internal.debug.ui.launcher.GenericTclInstallType";
	private static final String[] INTERPRETER_NAMES = { "tclsh", "tclsh84",
			"tclsh8.4", "tclsh85", "tclsh8.5", "tclsh8.6",

			"wish", "wish84", "wish85", "wish86", "wish8.4", "wish85",
			"wish8.5", "wish8.6",

			"vtk",

			"expect",

			"base-tcl-linux", "base-tk-linux",

			"base-tcl-thread", "base-tk-thread", "base-tcl8.5-thread",
			"base-tcl8.6-thread", "base-tk8.5-thread", "base-tk8.6-thread" };

	public String getNatureId() {
		return TclNature.NATURE_ID;
	}

	public String getName() {
		return INSTALL_TYPE_NAME;
	}

	protected String getPluginId() {
		return TclLaunchingPlugin.PLUGIN_ID;
	}

	protected String[] getPossibleInterpreterNames() {
		return INTERPRETER_NAMES;
	}

	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new GenericTclInstall(this, id);
	}

	protected void filterEnvironment(Map environment) {
		// make sure that $auto_path is clean
		environment.remove("TCLLIBPATH");
		// block wish from showing window under linux
		environment.remove("DISPLAY");
	}

	public IStatus validateInstallLocation(IFileHandle installLocation,
			EnvironmentVariable[] variables, LibraryLocation[] libraries,
			IProgressMonitor monitor) {
		/* Progress monitoring */monitor.beginTask("Validate Tcl interpreter",
				100);
		try {
			if (!installLocation.exists() || !installLocation.isFile()) {
				return createStatus(
						IStatus.ERROR,
						InterpreterMessages.errNonExistentOrInvalidInstallLocation,
						null);
			}
			IEnvironment environment = installLocation.getEnvironment();
			IExecutionEnvironment executionEnvironment = (IExecutionEnvironment) environment
					.getAdapter(IExecutionEnvironment.class);
			/* Progress monitoring */monitor
					.subTask("Deploy validation script");
			IDeployment deployment = executionEnvironment.createDeployment();
			if (deployment == null) {
				// happens if RSE is not initialized yet or no connection
				// established
				return createStatus(IStatus.ERROR,
						"Failed to deploy validation script to host:"
								+ environment.getName(), null);
			}
			List<String> output = null;
			Bundle bundle = TclPlugin.getDefault().getBundle();
			try {
				IFileHandle builderFile = deployment.getFile(deployment.add(
						bundle, "scripts/dltk.tcl"));
				/* Progress monitoring */monitor.worked(10);
				InterpreterConfig config = ScriptLaunchUtil
						.createInterpreterConfig(executionEnvironment,
								builderFile, builderFile.getParent());
				config.addScriptArg("get-pkgs");
				// Configure environment variables
				final Map<String, String> envVars = new HashMap<String, String>();

				Map<String, String> envVars2 = executionEnvironment
						.getEnvironmentVariables(false);
				if (envVars2 != null) {
					envVars.putAll(envVars2);
				}

				config.addEnvVars(envVars);
				config.removeEnvVar("DISPLAY"); //$NON-NLS-1$
				addTclLibPath(config, libraries, environment);
				/* Progress monitoring */monitor
						.subTask("Running validation script");
				String[] cmdLine = config.renderCommandLine(
						executionEnvironment.getEnvironment(), installLocation
								.toOSString());

				String[] environmentAsStrings = config
						.getEnvironmentAsStringsIncluding(variables);
				IPath workingDirectoryPath = config.getWorkingDirectoryPath();
				if (DLTKLaunchingPlugin.TRACE_EXECUTION) {
					ScriptLaunchUtil.traceExecution(
							"runScript with interpreter", cmdLine, //$NON-NLS-1$
							environmentAsStrings);
				}
				final Process process = executionEnvironment.exec(cmdLine,
						workingDirectoryPath, environmentAsStrings);
				/* Progress monitoring */monitor.worked(10);

				SubProgressMonitor sm = new SubProgressMonitor(monitor, 70);
				sm.beginTask("Running validation script",
						IProgressMonitor.UNKNOWN);
				sm.done();
				output = ProcessOutputCollector.execute(process, sm);

				int exitValue = process.exitValue();
				if (exitValue != 0) {
					return createStatus(IStatus.ERROR,
							"Interpreter return abnormal exit code:"
									+ exitValue, null);
				}
			} catch (IOException e1) {
				if (DLTKCore.DEBUG) {
					e1.printStackTrace();
				}
				return null;
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
				return null;
			} finally {
				deployment.dispose();
			}
			if (output == null) {
				return createStatus(IStatus.ERROR,
						InterpreterMessages.errNoInterpreterExecutablesFound,
						null);
			}
			boolean correct = false;
			for (String s : output) {
				if (CORRECT_INTERPRETER_PATTERN.equals(s)) {
					correct = true;
				}
			}

			if (correct) {
				monitor.subTask("Processing validation result");
				// Parse list of packages from output
				List<String> list = TclPackagesManager
						.extractPackagesFromContent(output);
				monitor.worked(10);
				return new StatusWithPackages(list);
			} else {
				return createStatus(IStatus.ERROR,
						InterpreterMessages.errNoInterpreterExecutablesFound,
						null);
			}
		} finally {
			monitor.done();
		}
	}

	private void addTclLibPath(InterpreterConfig config,
			LibraryLocation[] libraries, IEnvironment environment) {
		if (libraries == null) {
			return;
		}
		String currentValue = config
				.removeEnvVar(TclLaunchConfigurationDelegate.TCLLIBPATH_ENV_VAR);

		IPath paths[] = new IPath[libraries.length];
		int i = 0;
		for (LibraryLocation loc : libraries) {
			paths[i++] = EnvironmentPathUtils
					.getLocalPath(loc.getLibraryPath());
		}
		StringBuffer sb = new StringBuffer();
		for (i = 0; i < paths.length; ++i) {
			final IFileHandle file = config.getEnvironment().getFile(paths[i]);
			if (file != null) {
				if (sb.length() != 0) {
					sb.append(' ');
				}
				sb.append('{');
				sb.append(file.toOSString());
				sb.append('}');
			}
		}
		if (currentValue != null) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append(TclLaunchConfigurationDelegate
					.convertToTclLibPathFormat(currentValue));
			// sb.append(currentValue).append(" ");
		}
		if (sb.length() != 0) {
			config.addEnvVar(TclLaunchConfigurationDelegate.TCLLIBPATH_ENV_VAR,
					sb.toString());
		}
	}

	protected ILookupRunnable createLookupRunnable(
			final IFileHandle installLocation, final List locations,
			final EnvironmentVariable[] variables) {
		return new ILookupRunnable() {
			public void run(IProgressMonitor monitor) {
				// This retrieval could not receive paths in some cases.
				// String result = retrivePaths(installLocation, locations,
				// monitor, createPathFile(), variables);
				// This is safe retrieval
				// String[] autoPath = DLTKTclHelper.getDefaultPath(
				// installLocation, variables);
				// IEnvironment env = installLocation.getEnvironment();
				// if (autoPath != null) {
				// for (int i = 0; i < autoPath.length; i++) {
				// Path libraryPath = new Path(autoPath[i]);
				// IFileHandle file = env.getFile(libraryPath);
				// if (file.exists()) {
				// locations.add(new LibraryLocation(libraryPath));
				// }
				// }
				// }
			}
		};
	}

	protected String[] parsePaths(String res) {
		ArrayList paths = new ArrayList();
		String subs = null;
		int index = 0;
		String result = res;
		if (result.startsWith(DLTK_PATH_PREFIX)) {
			result = result.substring(DLTK_PATH_PREFIX.length());
		}
		while (index < result.length()) {
			// skip whitespaces
			while (index < result.length()
					&& Character.isWhitespace(result.charAt(index)))
				index++;
			if (index == result.length())
				break;

			if (result.charAt(index) == '{') {
				int start = index;
				while (index < result.length() && result.charAt(index) != '}')
					index++;
				if (index == result.length())
					break;
				subs = result.substring(start + 1, index);
			} else {
				int start = index;
				while (index < result.length() && result.charAt(index) != ' ')
					index++;
				subs = result.substring(start, index);
			}

			paths.add(subs);
			index++;
		}

		return (String[]) paths.toArray(new String[paths.size()]);
	}

	protected ILog getLog() {
		return TclLaunchingPlugin.getDefault().getLog();
	}

	protected IPath createPathFile(IDeployment deployment) throws IOException {
		return null;
	}
}
