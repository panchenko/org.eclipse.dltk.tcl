/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.launching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.console.ScriptConsoleServer;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.internal.launching.EnvironmentResolver;
import org.eclipse.dltk.internal.launching.execution.DeploymentManager;
import org.eclipse.dltk.launching.AbstractInterpreterRunner;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.tcl.launching.TclLaunchConfigurationConstants;
import org.eclipse.dltk.tcl.launching.TclLaunchConfigurationDelegate;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class TclInterpreterRunner extends AbstractInterpreterRunner {
	public TclInterpreterRunner(IInterpreterInstall install) {
		super(install);
	}

	protected String getProcessType() {
		return TclLaunchConfigurationConstants.ID_TCL_PROCESS_TYPE;
	}

	@Override
	protected String[] getEnvironmentVariablesAsStrings(InterpreterConfig config) {
		EnvironmentVariable[] vars = getInstall().getEnvironmentVariables();
		if (vars != null) {
			String var = config
					.getEnvVar(TclLaunchConfigurationDelegate.TCLLIBPATH_ENV_VAR);
			if (var != null) {
				List<EnvironmentVariable> resultingVars = new ArrayList<EnvironmentVariable>();
				for (EnvironmentVariable envVar : vars) {
					if (envVar.getName().equals(
							TclLaunchConfigurationDelegate.TCLLIBPATH_ENV_VAR)) {
						EnvironmentVariable[] variables = EnvironmentResolver
								.resolve(config.getEnvVars(),
										new EnvironmentVariable[] { envVar },
										true);
						String newValue = var
								+ " "
								+ TclLaunchConfigurationDelegate
										.convertToTclLibPathFormat(variables[0]
												.getValue());
						config
								.addEnvVar(
										TclLaunchConfigurationDelegate.TCLLIBPATH_ENV_VAR,
										newValue);
					} else {
						resultingVars.add(envVar);
					}
				}
				return config
						.getEnvironmentAsStringsIncluding(resultingVars
								.toArray(new EnvironmentVariable[resultingVars
										.size()]));
			}
		}
		return config.getEnvironmentAsStringsIncluding(vars);
	}

	protected void alterConfig(ILaunch launch, InterpreterConfig config) {
		super.alterConfig(launch, config);
		ILaunchConfiguration configuration = launch.getLaunchConfiguration();
		if (configuration != null) {
			boolean useTclConsole = false;
			try {
				useTclConsole = configuration
						.getAttribute(
								ScriptLaunchConfigurationConstants.ATTR_USE_INTERACTIVE_CONSOLE,
								false);
			} catch (CoreException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			if (useTclConsole) {
				ScriptConsoleServer server = ScriptConsoleServer.getInstance();
				String port = Integer.toString(server.getPort());

				try {
					IExecutionEnvironment executionEnvironment = config
							.getExecutionEnvironment();
					IDeployment deployment = executionEnvironment
							.createDeployment();
					DeploymentManager.getInstance().addDeployment(launch,
							deployment);
					IPath path = deployment.add(TclLaunchingPlugin.getDefault()
							.getBundle(), TclLaunchingPlugin.getDefault()
							.getConsoleProxy());
					IFileHandle scriptFile = deployment.getFile(path);

					String id = configuration
							.getAttribute(
									ScriptLaunchConfigurationConstants.ATTR_DLTK_CONSOLE_ID,
									(String) null);
					config.addInterpreterArg(scriptFile.toOSString());
					config.addInterpreterArg(DLTKDebugPlugin.getDefault()
							.getBindAddress());
					IPath scriptFilePath = config.getScriptFilePath();
					if (scriptFilePath == null) {
						config.setScriptFile(new Path("--noscript"));
						config.setNoFile(true);
					}
					config.addInterpreterArg(port);
					if (id != null) {
						config.addInterpreterArg(id);
					} else {
						throw new CoreException(
								new Status(IStatus.ERROR,
										TclLaunchingPlugin.PLUGIN_ID,
										"Error to obtain console ID. Please update launch configuratin."));
					}
				} catch (IOException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				} catch (CoreException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
