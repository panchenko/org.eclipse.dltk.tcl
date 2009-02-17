/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;

public final class TclCheckerConstants {
	private TclCheckerConstants() {
	}

	/**
	 * XMI representation of the configuration data
	 */
	public static final String PREF_CONFIGURATION = "tclchecker.configuration"; //$NON-NLS-1$

	/**
	 * Version of the TclChecker preferences
	 */
	public static final String PREF_VERSION = "tclchecker.preferences.version"; //$NON-NLS-1$

	public static final String TCL_DEVKIT_LOCAL_VARIABLE = "TCLDEVKIT_LOCAL"; //$NON-NLS-1$

	public static final String CONFIGURATION_EXTENSION_POINT_NAME = TclCheckerPlugin.PLUGIN_ID
			+ ".configuration"; //$NON-NLS-1$
}
