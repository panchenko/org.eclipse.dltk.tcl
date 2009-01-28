/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

public final class TclCheckerConstants {
	private TclCheckerConstants() {
	}

	public static final String PREF_PATH = "tclchecker.path"; //$NON-NLS-1$

	public static final String PREF_MODE = "tclchecker.mode"; //$NON-NLS-1$
	public static final String PREF_SUMMARY = "tclchecker.summary"; //$NON-NLS-1$
	public static final String PREF_USE_TCL_VER = "tclchecker.use_tcl_ver"; //$NON-NLS-1$

	public static final String PREF_PCX_PATH = "tclchecker.pcx.path"; //$NON-NLS-1$
	public static final String PREF_NO_PCX = "tclchecker.no_pcx"; //$NON-NLS-1$

	public static final String PREF_VERSION = "tclchecker.version"; //$NON-NLS-1$
	public static final String CLI_OPTIONS = "tclchecker.cli.options"; //$NON-NLS-1$

	public static final String VERSION_4 = "4"; //$NON-NLS-1$
	public static final String VERSION_5 = "5"; //$NON-NLS-1$

	public static final int MODE_NONE = -1;

	public static final int MODE_ERRORS = 0;

	public static final int MODE_ERRORS_AND_USAGE_WARNINGS = 1;

	public static final int MODE_ERRORS_AND_WARNINGS_EXCEPT_UPGRADE = 2;

	public static final int MODE_ALL = 3;

	public static final int MODE_DEFAULT = MODE_ERRORS_AND_WARNINGS_EXCEPT_UPGRADE;

	public static final String TCL_DEVKIT_LOCAL_VARIABLE = "TCLDEVKIT_LOCAL"; //$NON-NLS-1$

	public static final int PROCESS_TYPE_DEFAULT = 0;
	public static final int PROCESS_TYPE_SUPPRESS = 1;
	public static final int PROCESS_TYPE_CHECK = 2;
}
