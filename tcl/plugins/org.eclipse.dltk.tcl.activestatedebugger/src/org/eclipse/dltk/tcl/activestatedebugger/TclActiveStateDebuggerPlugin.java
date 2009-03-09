/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.TclActiveStateDebuggerEnvironment;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class TclActiveStateDebuggerPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.dltk.tcl.activestatedebugger"; //$NON-NLS-1$

	// The shared instance
	private static TclActiveStateDebuggerPlugin plugin;

	public TclActiveStateDebuggerPlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static TclActiveStateDebuggerPlugin getDefault() {
		return plugin;
	}

	/**
	 * @param environment
	 * @return
	 */
	public static ITclActiveStateDebuggerEnvironment getEnvironmentPreferences(
			IEnvironment environment) {
		Assert.isNotNull(environment);
		return new TclActiveStateDebuggerEnvironment(environment);
	}

	public static void log(int severity, String message, Throwable t) {
		getDefault().getLog().log(new Status(severity, PLUGIN_ID, message, t));
	}

	public static void warn(String message) {
		log(IStatus.WARNING, message, null);
	}

	public static void warn(Throwable e) {
		warn(e.getMessage(), e);
	}

	public static void warn(String message, Throwable t) {
		log(IStatus.WARNING, message, t);
	}

}
