/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.core;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class TclPlugin extends Plugin {

	public static final String PLUGIN_ID = "org.eclipse.dltk.tcl.core"; //$NON-NLS-1$

	/**
	 * @since 2.0
	 */
	public static final String PREF_LOCAL_VALIDATOR = PLUGIN_ID
			+ ".localValidator"; //$NON-NLS-1$					

	// The shared instance.
	private static TclPlugin plugin;

	public static boolean REPORT_PARSER_PROBLEMS = true;

	/**
	 * The constructor.
	 */
	public TclPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		savePluginPreferences();
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static TclPlugin getDefault() {
		return plugin;
	}

	/**
	 * @since 2.0
	 */
	public static void error(String message) {
		plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message));
	}

	public static void error(String message, Throwable t) {
		plugin.getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, message, t));
	}

	public static void error(Throwable t) {
		plugin.getLog().log(
				new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK,
						t.getMessage(), t));
	}

}
