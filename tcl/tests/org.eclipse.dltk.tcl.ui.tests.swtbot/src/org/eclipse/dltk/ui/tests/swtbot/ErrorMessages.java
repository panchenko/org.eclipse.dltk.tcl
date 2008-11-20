/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation
 *******************************************************************************/
package org.eclipse.dltk.ui.tests.swtbot;

import java.text.MessageFormat;

import org.eclipse.osgi.util.NLS;

public class ErrorMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.ui.tests.swtbot.ErrorMessages"; //$NON-NLS-1$

	public static String Common_internalFatalError;
	public static String Common_initError;
	public static String Common_defInterprNotFound;
	public static String Common_altInterprNotFound;
	public static String Common_prjInterprNotFound;
	public static String Common_prjInterprFound;
	public static String Common_invalidInterprCount;
	public static String Common_errOpenPerspective;
	public static String Common_invalidPerspective;

	public static String Common_errChecked;
	public static String Common_errEnabled;
	public static String Common_errNotChecked;
	public static String Common_errColumnCount;

	public static String Common_errSetFocus;

	public static String Project_errNotTclProject;
	public static String Project_errNotFound;
	public static String Project_errDelete;
	public static String Project_errClose;
	public static String Project_errOpen;
	public static String Project_errContentExist;
	public static String Project_errContentNotExist;

	public static String ProjectContent_errFolderNotExist;
	public static String ProjectContent_errSourceFolderNotExist;
	public static String ProjectContent_errScriptNotExist;
	public static String ProjectContent_errFileNotExist;

	public static String ProjectContent_errFolderExist;
	public static String ProjectContent_errSourceFolderExist;
	public static String ProjectContent_errScriptExist;
	public static String ProjectContent_errFileExist;

	public static String Interpreter_errInterprLibFound;
	public static String Interpreter_errInterprLibNotFound;
	public static String Interpreter_errSeveralInterprLibFound;

	static {
		NLS.initializeMessages(BUNDLE_NAME, ErrorMessages.class);
	}

	private ErrorMessages() {
	}

	public static String format(String message, Object object) {
		return MessageFormat.format(message, new Object[] { object });
	}

	public static String format(String message, Object[] objects) {
		return MessageFormat.format(message, objects);
	}

}
