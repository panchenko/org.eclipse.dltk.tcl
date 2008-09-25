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
package org.eclipse.dltk.tcl.parser;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.parser.messages"; //$NON-NLS-1$
	public static String TclArgumentMatcher_Argument_Of_Type_Expected;
	public static String TclArgumentMatcher_Argument_Of_Type_ExpectedDetail;
	public static String TclArgumentMatcher_Block_Argument_Expected;
	public static String TclArgumentMatcher_Error_Number_is_negative;
	public static String TclArgumentMatcher_Extra_Arguments;
	public static String TclArgumentMatcher_Invlid_Arguments;
	public static String TclArgumentMatcher_Missing_Argument;
	public static String TclArgumentMatcher_Missing_Group_Argument;
	public static String TclArgumentMatcher_Missing_Switch_Argument;
	public static String TclArgumentMatcher_Missing_Switch_Arg;
	public static String TclArgumentMatcher_Tcl_Substitution_Display;
	public static String TclParser_Command_Is_Deprecated;
	public static String TclParser_Command_Name_Is_Substitution;
	public static String TclParser_Command_Version_Is_Invalid;
	public static String TclParser_Unknown_Command;
	public static String TclParser_Command_Out_Of_Scope;
	public static String TclArgumentMatcher_Invalid_Arguments_And_Expected;
	public static String TclArgumentMatcher_Missing_TypedArgument;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
