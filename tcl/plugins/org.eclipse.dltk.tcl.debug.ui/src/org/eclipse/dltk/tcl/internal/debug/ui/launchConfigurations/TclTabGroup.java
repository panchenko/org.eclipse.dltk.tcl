/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.launchConfigurations;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.launching.ScriptLaunchConfigurationConstants;
import org.eclipse.dltk.tcl.internal.debug.ui.interpreters.TclInterpreterTab;

public class TclTabGroup extends AbstractLaunchConfigurationTabGroup {
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new TclMainLaunchConfigurationTab(mode),
				new TclScriptArgumentsTab(), new TclInterpreterTab(),
				new EnvironmentTab(),
				// new SourceContainerLookupTab(),
				// new CommonTab()
				new CommonTab() {
					public void performApply(
							ILaunchConfigurationWorkingCopy configuration) {
						super.performApply(configuration);
						try {
							boolean config = configuration
									.getAttribute(
											ScriptLaunchConfigurationConstants.ATTR_USE_INTERACTIVE_CONSOLE,
											false);
							if (config) {
								configuration
										.setAttribute(
												IDebugUIConstants.ATTR_CAPTURE_IN_CONSOLE,
												(String) null);
							} else {
								configuration
										.setAttribute(
												IDebugUIConstants.ATTR_CAPTURE_IN_CONSOLE,
												(String) "true");
							}
						} catch (CoreException e) {
							if (DLTKCore.DEBUG) {
								e.printStackTrace();
							}
						}
					}
				} };
		setTabs(tabs);
	}
}
