/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.launching;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class GenericTclInstall extends AbstractInterpreterInstall {
	public class BuiltinsHelper {
		StringBuffer source = new StringBuffer();

		void load() throws IOException, CoreException {
			IExecutionEnvironment exeEnv = getExecEnvironment();
			if (exeEnv == null)
				return;

			IDeployment deployment = exeEnv.createDeployment();

			final IPath builder = deployment.add(TclLaunchingPlugin
					.getDefault().getBundle(), "scripts/builtins.tcl"); //$NON-NLS-1$

			IFileHandle builderFile = deployment.getFile(builder);
			InterpreterConfig config = ScriptLaunchUtil
					.createInterpreterConfig(exeEnv, builderFile, builderFile
							.getParent());
			// config.addInterpreterArg("-KU"); //$NON-NLS-1$
			final Process process = ScriptLaunchUtil.runScriptWithInterpreter(
					exeEnv, GenericTclInstall.this.getInstallLocation()
							.toOSString(), config);
			Thread readerThread = new Thread(new Runnable() {
				public void run() {
					BufferedReader input = null;
					try {
						input = new BufferedReader(new InputStreamReader(
								process.getInputStream()));

						String line = null;
						while ((line = input.readLine()) != null) {
							source.append(line);
							source.append("\n");
						}
					} catch (IOException e) {
						if (DLTKCore.DEBUG) {
							e.printStackTrace();
						}
					} finally {
						if (input != null) {
							try {
								input.close();
							} catch (IOException e) {
								if (DLTKCore.DEBUG) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			});
			try {
				readerThread.start();
				readerThread.join(10000);
			} catch (InterruptedException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}

		}
	}

	BuiltinsHelper helper = null;

	public GenericTclInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}

	public IInterpreterRunner getInterpreterRunner(String mode) {
		IInterpreterRunner runner = super.getInterpreterRunner(mode);

		if (runner != null) {
			return runner;
		}

		if (mode.equals(ILaunchManager.RUN_MODE)) {
			return new TclInterpreterRunner(this);
		}

		return null;
	}

	public String getNatureId() {
		return TclNature.NATURE_ID;
	}

	// Builtins
	public String getBuiltinModuleContent(String name) {
		if (helper == null) {
			helper = new BuiltinsHelper();
			try {
				helper.load();
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
		return helper.source.toString();
	}

	public String[] getBuiltinModules() {
		return new String[] { "builtins.tcl" };
	}
}