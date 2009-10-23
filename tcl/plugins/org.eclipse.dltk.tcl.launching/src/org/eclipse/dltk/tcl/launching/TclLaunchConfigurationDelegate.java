/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;
import org.eclipse.dltk.launching.InterpreterConfig;
import org.eclipse.dltk.tcl.core.TclLibpathUtils;
import org.eclipse.dltk.tcl.core.TclNature;

public class TclLaunchConfigurationDelegate extends
		AbstractScriptLaunchConfigurationDelegate {

	public String getLanguageId() {
		return TclNature.NATURE_ID;
	}

	protected InterpreterConfig createInterpreterConfig(
			ILaunchConfiguration configuration, ILaunch launch)
			throws CoreException {
		InterpreterConfig config = super.createInterpreterConfig(configuration,
				launch);
		if (config != null) {
			addLibpathEnvVar(config, configuration);
			checkEnvVars(config, configuration);
		}

		return config;
	}

	protected void addLibpathEnvVar(InterpreterConfig config,
			ILaunchConfiguration configuration) throws CoreException {
		String currentValue = config.removeEnvVar(TclLibpathUtils.TCLLIBPATH);
		IPath[] paths = createBuildPath(configuration, config.getEnvironment());

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < paths.length; ++i) {
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
			sb.append(TclLibpathUtils.convertToTclLibPathFormat(currentValue));
			// sb.append(currentValue).append(" ");
		}
		if (sb.length() != 0) {
			config.addEnvVar(TclLibpathUtils.TCLLIBPATH, sb.toString());
		}
	}

	protected void checkEnvVars(InterpreterConfig config,
			ILaunchConfiguration configuration) {
	}

}
