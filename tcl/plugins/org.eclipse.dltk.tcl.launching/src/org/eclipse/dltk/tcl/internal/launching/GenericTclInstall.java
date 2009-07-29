/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.launching;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.caching.IContentCache;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class GenericTclInstall extends AbstractInterpreterInstall {
	public static class BuiltinsHelper {
		private static final String BUILTINST_INFORMATION = "tcl_builtins_information";

		private final GenericTclInstall install;

		public BuiltinsHelper(GenericTclInstall install) {
			this.install = install;
		}

		StringBuffer source = new StringBuffer();

		long lastModified;

		boolean initialized = false;

		void load() {
			final IContentCache cache = ModelManager.getModelManager()
					.getCoreCache();
			String builtins = cache.getCacheEntryAttributeString(install
					.getInstallLocation(), BUILTINST_INFORMATION, true);
			if (builtins != null) {
				source.append(builtins);
				lastModified = System.currentTimeMillis();
				initialized = true;
				return;
			}
			Job loadTclBuiltin = new Job("Generate Tcl builtin file...") {
				protected IStatus run(final IProgressMonitor monitor) {
					monitor.beginTask("Generate Tcl builtin file",
							IProgressMonitor.UNKNOWN);
					IExecutionEnvironment exeEnv = install.getExecEnvironment();
					if (exeEnv == null)
						return Status.CANCEL_STATUS;

					String bundlePath = "scripts/builtins.tcl";
					String content = ScriptLaunchUtil
							.runEmbeddedScriptReadContent(
									exeEnv,
									bundlePath,
									TclLaunchingPlugin.getDefault().getBundle(),
									install.getInstallLocation(), monitor);
					if (content != null) {
						cache.setCacheEntryAttribute(install
								.getInstallLocation(), BUILTINST_INFORMATION,
								content);
					}
					if (content != null) {
						source.append(content);
						lastModified = System.currentTimeMillis();
						initialized = true;
					}
					monitor.done();
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

	private static final Map<IInterpreterInstall, BuiltinsHelper> helpers = new HashMap<IInterpreterInstall, BuiltinsHelper>();

	// Builtins
	public String getBuiltinModuleContent(String name) {
		BuiltinsHelper helper = initialize();
		return helper.source.toString();
	}

	public long lastModified() {
		BuiltinsHelper helper = initialize();
		return helper.lastModified;
	}

	private BuiltinsHelper initialize() {
		BuiltinsHelper helper;
		synchronized (helpers) {
			helper = helpers.get(this);
			if (helper == null) {
				helper = new BuiltinsHelper(this);
				helpers.put(this, helper);
			}
		}
		synchronized (helper) {
			if (!helper.initialized) {
				helper.load();
			}
		}
		return helper;
	}

	public String[] getBuiltinModules() {
		return new String[] { "builtins.tcl" }; //$NON-NLS-1$
	}
}
