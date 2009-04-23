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
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.internal.launching.InterpreterMessages;
import org.eclipse.dltk.launching.EnvironmentVariable;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptLaunchUtil;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.launching.TclLaunchingPlugin;

public class GenericTclInstallType extends AbstractInterpreterInstallType {
	private static final String CORRECT_INTERPRETER_PATTERN = "#DLTK INTERPRETER TEST:5";

	private static final String INSTALL_TYPE_NAME = "Generic Tcl";

	private static final String[] INTERPRETER_NAMES = { "tclsh", "tclsh84",
			"tclsh8.4", "tclsh85", "tclsh8.5",

			"wish", "wish84", "wish8.4", "wish85", "wish8.5",

			"vtk",

			"expect",

			"base-tcl-linux", "base-tk-linux",

			"base-tcl-thread", "base-tk-thread", "base-tcl8.5-thread",
			"base-tk8.5-thread" };

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

	public IStatus validateInstallLocation(IFileHandle installLocation) {
		if (!installLocation.exists() || !installLocation.isFile()) {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNonExistentOrInvalidInstallLocation,
					null);
		}
		IEnvironment environment = installLocation.getEnvironment();
		IExecutionEnvironment executionEnvironment = (IExecutionEnvironment) environment
				.getAdapter(IExecutionEnvironment.class);

		String output = ScriptLaunchUtil.runEmbeddedScriptReadContent(
				executionEnvironment, "scripts/test.tcl", TclLaunchingPlugin
						.getDefault().getBundle(), installLocation,
				new NullProgressMonitor());
		String[] lines = output.split("\\n");
		boolean correct = false;
		for (int i = 0; i < lines.length; i++) {
			if (CORRECT_INTERPRETER_PATTERN.equals(lines[i])) {
				correct = true;
			}
		}

		if (correct) {
			return createStatus(IStatus.OK, "", null); //$NON-NLS-1$
		} else {
			return createStatus(IStatus.ERROR,
					InterpreterMessages.errNoInterpreterExecutablesFound, null);
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
