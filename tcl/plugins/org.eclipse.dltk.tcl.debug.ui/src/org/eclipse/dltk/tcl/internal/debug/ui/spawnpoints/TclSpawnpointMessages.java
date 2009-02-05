/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.spawnpoints;

import org.eclipse.osgi.util.NLS;

public class TclSpawnpointMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.debug.ui.spawnpoints.TclSpawnpointMessages"; //$NON-NLS-1$
	public static String button_Add;
	public static String button_Delete;
	public static String button_Edit;
	public static String inputDialog_commandLabel;
	public static String inputDialog_editTitle;
	public static String inputDialog_errorDuplicateCommandName;
	public static String inputDialog_errorEmptyCommandName;
	public static String inputDialog_errorInvalidCommandName;
	public static String inputDialog_newTitle;
	public static String preferenceBlock_rebuildMessage;
	public static String preferenceBlock_rebuildTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TclSpawnpointMessages.class);
	}

	private TclSpawnpointMessages() {
	}
}
