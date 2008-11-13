/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Tcl ActiveState debugging engine preference page
 */
public class TclActiveStateDebuggerPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	static final PreferenceKey ENGINE_PATH = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY);

	static final PreferenceKey PDX_PATH = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY);

	static final PreferenceKey LOG_FILE_NAME = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.LOG_FILE_NAME);

	static final PreferenceKey INSTRUMENTATION_FEATURES = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.INSTRUMENTATION_FEATURES);

	static final PreferenceKey INSTRUMENTATION_ERROR_ACTION = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.INSTRUMENTATION_ERROR_ACTION);

	static final PreferenceKey INSTRUMENTATION_PATTERNS = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.INSTRUMENTATION_PATTERNS);

	private static final String PREFERENCE_PAGE_ID = "org.eclipse.dltk.tcl.preferences.debug.activestatedebugger"; //$NON-NLS-1$
	private static final String PROPERTY_PAGE_ID = "org.eclipse.dltk.tcl.propertyPage.debug.engines.activestatedebugger"; //$NON-NLS-1$

	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		final PreferenceKey[] keys = new PreferenceKey[] { ENGINE_PATH,
				PDX_PATH, LOG_FILE_NAME, INSTRUMENTATION_FEATURES,
				INSTRUMENTATION_ERROR_ACTION, INSTRUMENTATION_PATTERNS };
		return new TclActiveStateDebuggerBlock(newStatusChangedListener,
				project, keys, container);
	}

	protected String getHelpId() {
		return null;
	}

	protected String getPreferencePageId() {
		return PREFERENCE_PAGE_ID;
	}

	protected String getProjectHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	protected String getPropertyPageId() {
		return PROPERTY_PAGE_ID;
	}

	protected String getNatureId() {
		return TclNature.NATURE_ID;
	}

	protected void setDescription() {
		setDescription(PreferenceMessages.DebuggingEngineDescription);
	}

	protected void setPreferenceStore() {
		setPreferenceStore(TclActiveStateDebuggerPlugin.getDefault()
				.getPreferenceStore());
	}
}
