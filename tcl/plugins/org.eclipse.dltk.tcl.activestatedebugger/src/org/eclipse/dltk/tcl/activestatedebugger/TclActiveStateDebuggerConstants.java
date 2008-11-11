/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.jface.preference.IPreferenceStore;

public final class TclActiveStateDebuggerConstants {

	public static final String DEBUGGING_ENGINE_PATH_KEY = "debugging_engine_path"; //$NON-NLS-1$
	public static final String DEBUGGING_ENGINE_PDX_PATH_KEY = "debugging_engine_pdx_path"; //$NON-NLS-1$

	public static final String LOG_FILE_NAME = "log_file_name"; //$NON-NLS-1$

	public static final String INSTRUMENTATION_FEATURES = "instrumentationFeatures"; //$NON-NLS-1$
	public static final String INSTRUMENTATION_ERROR_ACTION = "instrumentationErrorAction"; //$NON-NLS-1$

	public static final String INSTRUMENTATION_INCLUDE = "instrumentationInclude"; //$NON-NLS-1$
	public static final String INSTRUMENTATION_EXCLUDE = "instrumentationExclude"; //$NON-NLS-1$

	public static void initalizeDefaults(IPreferenceStore store) {
		store.setDefault(DEBUGGING_ENGINE_PATH_KEY, Util.EMPTY_STRING);
		store.setDefault(DEBUGGING_ENGINE_PDX_PATH_KEY, Util.EMPTY_STRING);
		store.setDefault(LOG_FILE_NAME, Util.EMPTY_STRING);
		store.setDefault(INSTRUMENTATION_FEATURES, InstrumentationFeature.ITCL
				.name());
		store.setDefault(INSTRUMENTATION_ERROR_ACTION, Util.EMPTY_STRING);
		store.setDefault(INSTRUMENTATION_INCLUDE, Util.EMPTY_STRING);
		store.setDefault(INSTRUMENTATION_EXCLUDE, Util.EMPTY_STRING);
	}

	private TclActiveStateDebuggerConstants() {
		// private constructor
	}
}
