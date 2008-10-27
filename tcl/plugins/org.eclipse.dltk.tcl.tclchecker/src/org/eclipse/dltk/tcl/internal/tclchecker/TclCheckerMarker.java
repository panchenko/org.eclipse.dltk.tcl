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
package org.eclipse.dltk.tcl.internal.tclchecker;

public class TclCheckerMarker {

	public static final String TYPE = TclCheckerPlugin.PLUGIN_ID
			+ ".tclcheckerproblem"; //$NON-NLS-1$

	public static final String SUGGESTED_CORRECTIONS = TclCheckerPlugin.PLUGIN_ID
			+ ".suggestedCorrections"; //$NON-NLS-1$

	public static final String COMMAND_START = TclCheckerPlugin.PLUGIN_ID
			+ ".commandStart"; //$NON-NLS-1$

	public static final String COMMAND_LENGTH = TclCheckerPlugin.PLUGIN_ID
			+ ".commandLength"; //$NON-NLS-1$

	public static final String MESSAGE_ID = TclCheckerPlugin.PLUGIN_ID
			+ ".messageId"; //$NON-NLS-1$

	public static final String TIMESTAMP = TclCheckerPlugin.PLUGIN_ID
			+ ".timestamp"; //$NON-NLS-1$

}
