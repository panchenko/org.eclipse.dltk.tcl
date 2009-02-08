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
package org.eclipse.dltk.tcl.internal.tclchecker.qfix;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.tclchecker.qfix.messages"; //$NON-NLS-1$
	public static String TclCheckerAnnotationResolution_internalError;
	public static String TclCheckerAnnotationResolution_markerDeleteError;
	public static String TclCheckerAnnotationResolution_problemDisappeared;
	public static String TclCheckerAnnotationResolution_revalidationError;
	public static String TclCheckerAnnotationResolution_wrongCommandStart;
	public static String TclCheckerAnnotationResolution_wrongMarkerAttributes;
	public static String TclCheckerFixUtils_errorLineLengthOverrun;
	public static String TclCheckerMarkerResolution_replaceWith;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
