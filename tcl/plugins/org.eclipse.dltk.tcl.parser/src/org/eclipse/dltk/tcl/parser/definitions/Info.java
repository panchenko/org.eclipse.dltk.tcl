/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.definitions;

import org.eclipse.osgi.util.NLS;

public class Info extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.parser.definitions.info"; //$NON-NLS-1$

	public static String TclArgumentType_Any;
	public static String TclArgumentType_Boolean;
	public static String TclArgumentType_CmdName;
	public static String TclArgumentType_Expression;
	public static String TclArgumentType_Index;
	public static String TclArgumentType_Integer;
	public static String TclArgumentType_Level;
	public static String TclArgumentType_Namespace;
	public static String TclArgumentType_NotNegative;
	public static String TclArgumentType_Package;
	public static String TclArgumentType_Script;
	public static String TclArgumentType_VarName;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Info.class);
	}

	private Info() {
	}
}
