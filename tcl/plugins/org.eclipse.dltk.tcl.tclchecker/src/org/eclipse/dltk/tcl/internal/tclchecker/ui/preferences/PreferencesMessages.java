/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import org.eclipse.osgi.util.NLS;

public class PreferencesMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences.messages"; //$NON-NLS-1$

	public static String TclChecker_path;
	public static String TclChecker_pcxPath;

	public static String TclChecker_path_configureTitle;

	public static String TclChecker_path_configureMessage;

	public static String TclChecker_path_isempty;

	public static String TclChecker_path_isinvalid;

	public static String TclChecker_path_notexists;

	public static String TclChecker_path_notlookslike;

	public static String TclChecker_path_msgPattern;

	public static String TclChecker_suppressProblems;

	public static String TclChecker_problems_name;

	public static String TclChecker_problems_type;

	public static String TclChecker_problems_action;

	public static String TclChecker_processType_default;

	public static String TclChecker_processType_suppress;

	public static String TclChecker_processType_check;

	public static String TclChecker_processType_defaultAll;

	public static String TclChecker_processType_suppressAll;

	public static String TclChecker_processType_checkAll;

	public static String TclChecker_browse;

	public static String TclChecker_button_Add;
	public static String TclChecker_button_Edit;
	public static String TclChecker_button_Copy;
	public static String TclChecker_button_Remove;

	public static String TclChecker_column_ConfigurationName;
	public static String TclChecker_column_ConfigurationType;

	public static String TclChecker_column_InstanceEnvironmentName;
	public static String TclChecker_column_InstanceConfigurationName;

	public static String TclChecker_mode;

	public static String TclChecker_mode_default;
	public static String TclChecker_mode_none;
	public static String TclChecker_mode_errors;
	public static String TclChecker_mode_errorsAndUsageWarnings;
	public static String TclChecker_mode_errorsAndWarningsExceptUpgrade;
	public static String TclChecker_mode_all;
	public static String TclChecker_mode_all_tooltip;

	public static String TclChecker_work_name;

	public static String TclChecker_add_Configuration_Title;
	public static String TclChecker_edit_Configuration_Title;
	public static String TclChecker_edit_Environment_Title;

	public static String TclChecker_error;

	public static String TclChecker_tab_Instances;
	public static String TclChecker_tab_Configurations;

	public static String TclChecker_warning;

	static {
		NLS.initializeMessages(BUNDLE_NAME, PreferencesMessages.class);
	}
}
