/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.launching;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class GenericTclInstall extends AbstractInterpreterInstall {
	public class BuiltinsHelper {
		StringBuffer source = new StringBuffer();

		long lastModified;

		void load() {
			Job loadTclBuiltin = new Job("Generate Tcl builtin file...") {
				protected IStatus run(final IProgressMonitor monitor) {
					monitor.beginTask("Generate Tcl builtin file",
							IProgressMonitor.UNKNOWN);
					IExecutionEnvironment exeEnv = getExecEnvironment();
					if (exeEnv == null)
						return Status.CANCEL_STATUS;

					String bundlePath = "scripts/builtins.tcl";
					String content = ScriptLaunchUtil
							.runEmbeddedScriptReadContent(
									exeEnv,
									bundlePath,
									TclLaunchingPlugin.getDefault().getBundle(),
									GenericTclInstall.this.getInstallLocation(),
									monitor);
					if (content != null) {
						source.append(content);
					}
					lastModified = System.currentTimeMillis();
					return Status.OK_STATUS;
				}
			};
			loadTclBuiltin.schedule();
			try {
				loadTclBuiltin.join();
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
		initialize();
		return helper.source.toString();
	}

	public long lastModified() {
		initialize();
		return helper.lastModified;
	}

	private synchronized void initialize() {
		if (helper == null) {
			helper = new BuiltinsHelper();
			helper.load();
		}
	}

	public String[] getBuiltinModules() {
		return new String[] { "builtins.tcl" }; //$NON-NLS-1$
	}
}