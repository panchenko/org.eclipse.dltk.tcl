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

import org.eclipse.osgi.util.NLS;

/**
 * @author Alexey
 *
 */
public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.tclchecker.messages"; //$NON-NLS-1$
	public static String TclChecker_cannot_be_executed;
	public static String TclChecker_checking;
	public static String TclChecker_executing;
	public static String TclChecker_execution_error;
	public static String TclChecker_filelist_deploy_failed;
	public static String TclChecker_launching;
	public static String TclChecker_path_not_specified;
	public static String TclChecker_scanning;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
