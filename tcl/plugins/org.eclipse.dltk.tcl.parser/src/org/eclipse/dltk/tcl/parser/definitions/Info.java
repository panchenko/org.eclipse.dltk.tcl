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
	
	public static String CommandSynopsis_TypedArgument_Info;
	public static String CommandSynopsis_Switches_Info;
	public static String CommandSynopsis_EndOfSwitches_Info; 
	public static String CommandSynopsis_Mode_Info;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Info.class);
	}

	private Info() {
	}
}
