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
package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.osgi.util.NLS;

/**
 * @since 2.0
 */
public class TclInterpreterMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.ui.globalVariableMessages"; //$NON-NLS-1$
	public static String GlobalVariableBlock_Add;
	public static String GlobalVariableBlock_AddTitle;
	public static String GlobalVariableBlock_Edit;
	public static String GlobalVariableBlock_EditTitle;
	public static String GlobalVariableBlock_ErrorNoValue;
	public static String GlobalVariableBlock_Name;
	public static String GlobalVariableBlock_Remove;
	public static String GlobalVariableBlock_Value;
	public static String GlobalVariableContentProvider_overwriteMessage;
	public static String GlobalVariableContentProvider_overwriteTitle;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, TclInterpreterMessages.class);
	}

	private TclInterpreterMessages() {
	}
}
