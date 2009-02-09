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
package org.eclipse.dltk.tcl.activestatedebugger.spawnpoint;

import org.eclipse.osgi.util.NLS;

public class TclSpawnpointMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.activestatedebugger.spawnpoint.TclSpawnpointMessages"; //$NON-NLS-1$
	public static String participantMarkerMessage_commandPlurar;
	public static String participantMarkerMessage_commandSingular;
	public static String participantMarkerMessage_template;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TclSpawnpointMessages.class);
	}

	private TclSpawnpointMessages() {
	}
}
