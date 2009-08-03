/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.console;

import java.io.IOException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.dltk.console.ScriptConsoleServer;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.debug.core.DLTKDebugPlugin;
import org.eclipse.dltk.internal.launching.execution.DeploymentManager;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class TclConsoleUtil {
	public static ILaunch runDefaultTclInterpreter(TclInterpreter interpreter)
			throws CoreException, IOException {
		final ConsoleDeployment deployment = deployConsoleScript(EnvironmentManager
				.getLocalEnvironment());
		if (deployment == null) {
			return null;
		}
		final ILaunch launch = ScriptLaunchUtil.runScript(TclNature.NATURE_ID,
				deployment.scriptFile, null, null,
				constructConsoleArgs(interpreter), null);
		registerForCleanup(launch, deployment, interpreter);
		return launch;
	}

	/**
	 * @since 1.1
	 */
	public static ILaunch runTclInterpreter(IInterpreterInstall install,
			TclInterpreter interpreter) throws CoreException, IOException {
		final ConsoleDeployment deployment = deployConsoleScript(install
				.getEnvironment());
		if (deployment == null) {
			return null;
		}
		final ILaunch launch = ScriptLaunchUtil.runScript(install,
				deployment.scriptFile, null, null,
				constructConsoleArgs(interpreter), null);
		registerForCleanup(launch, deployment, interpreter);
		return launch;
	}

	private static class ConsoleDeployment {
		final IFileHandle scriptFile;
		final IDeployment deployment;

		public ConsoleDeployment(IDeployment deployment, IFileHandle scriptFile) {
			this.deployment = deployment;
			this.scriptFile = scriptFile;
		}

	}

	private static ConsoleDeployment deployConsoleScript(
			IEnvironment environment) throws IOException {
		final IExecutionEnvironment exeEnv = (IExecutionEnvironment) environment
				.getAdapter(IExecutionEnvironment.class);
		final IDeployment deployment = exeEnv.createDeployment();
		if (deployment == null) {
			return null;
		}
		final IPath path = deployment
				.add(TclLaunchingPlugin.getDefault().getBundle(),
						TclLaunchingPlugin.getDefault().getConsoleProxy());
		return new ConsoleDeployment(deployment, deployment.getFile(path));
	}

	private static String[] constructConsoleArgs(TclInterpreter interpreter) {
		ScriptConsoleServer server = ScriptConsoleServer.getInstance();
		return new String[] { DLTKDebugPlugin.getDefault().getBindAddress(),
				Integer.toString(server.getPort()),
				server.register(interpreter) };
	}

	private static void registerForCleanup(final ILaunch launch,
			final ConsoleDeployment deployment, TclInterpreter interpreter) {
		if (launch == null) {
			return;
		}
		DeploymentManager.getInstance().addDeployment(launch,
				deployment.deployment);
		interpreter.addCloseOperation(new Runnable() {
			public void run() {
				IProcess[] processes = launch.getProcesses();
				if (processes != null) {
					for (int i = 0; i < processes.length; i++) {
						try {
							processes[i].terminate();
						} catch (DebugException e) {
							if (DLTKCore.DEBUG) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		});
	}

}
