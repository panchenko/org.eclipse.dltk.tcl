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
package org.eclipse.dltk.ui.tests.swtbot.operations;

import java.text.MessageFormat;

import org.eclipse.osgi.util.NLS;

/**
 * @author Sergey
 * 
 */
public class OperationMessages extends NLS {
	private static final String BUNDLE_NAME = "org.eclipse.dltk.ui.tests.swtbot.operations.OperationMessages"; //$NON-NLS-1$
	public static String InterpreterOperations_btn_add;
	public static String InterpreterOperations_btn_add_library;
	public static String InterpreterOperations_btn_edit;
	public static String InterpreterOperations_col_location;
	public static String InterpreterOperations_col_name;
	public static String InterpreterOperations_col_type;
	public static String InterpreterOperations_context_menu_build_path;
	public static String InterpreterOperations_menu_configure_build_path;
	public static String InterpreterOperations_dlg_add_interpreter;
	public static String InterpreterOperations_dlg_add_library;
	public static String InterpreterOperations_dlg_edit_library;
	public static String InterpreterOperations_tab_libraries;
	public static String InterpreterOperations_tcl_interpreter_type;
	public static String InterpreterOperations_build_path_can_not_contain_mult_interpr;
	public static String Operations_dlg_confirm_delete;
	public static String Operations_dlg_create_tcl_file;
	public static String Operations_dlg_delete_resource;
	public static String Operations_dlg_new_file;
	public static String Operations_dlg_new_folder;
	public static String Operations_dlg_new_project;
	public static String Operations_dlg_new_source_folder;
	public static String Operations_dlg_preferences;
	public static String Operations_dlg_prj_properties;
	public static String Operations_dlg_rename;
	public static String Operations_dlg_rename_script_project;
	public static String Operations_dlg_rename_source_folder;
	public static String Operations_dlg_rename_source_module;
	public static String Operations_menu_copy;
	public static String Operations_menu_debug_as;
	public static String Operations_menu_delete;
	public static String Operations_menu_edit;
	public static String Operations_menu_file;
	public static String Operations_menu_navigate;
	public static String Operations_menu_new;
	public static String Operations_menu_new_file;
	public static String Operations_menu_new_folder;
	public static String Operations_menu_new_project;
	public static String Operations_menu_new_source_folder;
	public static String Operations_menu_open;
	public static String Operations_menu_open_declaration;
	public static String Operations_menu_paste;
	public static String Operations_menu_preferences;
	public static String Operations_menu_refactor;
	public static String Operations_menu_rename;
	public static String Operations_menu_run_as;
	public static String Operations_menu_tcl_file;
	public static String Operations_menu_tcl_project;
	public static String Operations_menu_window;
	public static String Operations_preferences_tcl;
	public static String Operations_script_popup_close_prj;
	public static String Operations_script_popup_open_prj;
	public static String Operations_script_popup_properties;
	public static String Operations_view_console;
	public static String Operations_view_script_explorer;
	public static String ProjectOperations_dlg_btn_dlt_prj;
	public static String ProjectOperations_check_delete_package;
	public static String ProjectOperations_check_delete_contents;
	public static String ProjectOperations_radio_delete_contents;
	public static String ProjectOperations_radio_do_not_delete_contents;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, OperationMessages.class);
	}

	private OperationMessages() {
	}

	public static String format(String message, Object object) {
		return MessageFormat.format(message, new Object[] { object });
	}

	public static String format(String message, Object[] objects) {
		return MessageFormat.format(message, objects);
	}

}
