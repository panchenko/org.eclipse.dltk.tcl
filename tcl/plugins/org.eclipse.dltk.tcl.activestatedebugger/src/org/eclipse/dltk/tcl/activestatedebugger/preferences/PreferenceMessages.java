/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.osgi.util.NLS;

public class PreferenceMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.activestatedebugger.preferences.PreferenceMessages"; //$NON-NLS-1$

	static {
		NLS.initializeMessages(BUNDLE_NAME, PreferenceMessages.class);
	}

	public static String DebuggingEnginePDXGroup;
	public static String DebuggingEngineDescription;
	public static String DebuggingEngineDownloadPage;
	public static String DebuggingEngineDownloadPageLink;
	public static String instrumentation_emptyPatternMessage;
	public static String instrumentation_errorAction_default_caption;
	public static String instrumentation_errorAction_label;
	public static String instrumentation_options;
	public static String instrumentation_tab;
	public static String instrumentation_pathSeparatorInPatten;
	public static String instrumentation_pattern_AddButton;
	public static String instrumentation_pattern_RemoveButton;
	public static String instrumentation_patternDialogPromptSuffix;
	public static String instrumentation_patternDialogTitle;
	public static String instrumentation_patterns_message;
	public static String instrumentation_patternsGroup;
	public static String path_tab;
}
