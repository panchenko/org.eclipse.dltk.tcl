/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.actions;

import org.eclipse.osgi.util.NLS;

public class ActionMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.debug.ui.actions.ActionMessages";//$NON-NLS-1$

	public static String ToggleSpawnpointAction_0;
	public static String ToggleSpawnpointAction_1;
	public static String ToggleSpawnpointAction_2;

	static {
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, ActionMessages.class);
	}

}
