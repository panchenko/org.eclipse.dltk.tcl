/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/

package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.debug.ui.preferences.ExternalDebuggingEngineOptionsBlock;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerConstants;
import org.eclipse.dltk.tcl.activestatedebugger.TclActiveStateDebuggerPlugin;
import org.eclipse.dltk.ui.environment.EnvironmentPathBlock;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Tcl ActiveState debugging engine preference page
 */
public class TclActiveStateDebuggerPreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	static PreferenceKey ENGINE_PATH = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PATH_KEY);

	static PreferenceKey PDX_PATH = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.DEBUGGING_ENGINE_PDX_PATH_KEY);

	static PreferenceKey ENABLE_LOGGING = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.ENABLE_LOGGING);

	static PreferenceKey LOG_FILE_PATH = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.LOG_FILE_PATH);

	static PreferenceKey LOG_FILE_NAME = new PreferenceKey(
			TclActiveStateDebuggerPlugin.PLUGIN_ID,
			TclActiveStateDebuggerConstants.LOG_FILE_NAME);

	private static String PREFERENCE_PAGE_ID = "org.eclipse.dltk.tcl.preferences.debug.activestatedebugger";
	private static String PROPERTY_PAGE_ID = "org.eclipse.dltk.tcl.propertyPage.debug.engines.activestatedebugger";

	/*
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#createOptionsBlock(org.eclipse.dltk.ui.util.IStatusChangeListener,
	 *      org.eclipse.core.resources.IProject,
	 *      org.eclipse.ui.preferences.IWorkbenchPreferenceContainer)
	 */
	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {

		return new ExternalDebuggingEngineOptionsBlock(
				newStatusChangedListener, project, new PreferenceKey[] {
						ENGINE_PATH, PDX_PATH, ENABLE_LOGGING, LOG_FILE_PATH,
						LOG_FILE_NAME }, container) {
			private EnvironmentPathBlock pdxPath;

			protected void createEngineBlock(Composite parent) {
				super.createEngineBlock(parent);
				createPDXGroup(parent);
			}

			protected void createOtherBlock(Composite parent) {
				addDownloadLink(parent,
						PreferenceMessages.DebuggingEngineDownloadPage,
						PreferenceMessages.DebuggingEngineDownloadPageLink);
			}

			private void createPDXGroup(final Composite parent) {

				final Group group = SWTFactory.createGroup(parent,
						PreferenceMessages.DebuggingEnginePDXGroup, 3, 1,
						GridData.FILL_BOTH);
				pdxPath = new EnvironmentPathBlock();
				pdxPath.createControl(group);
				Map paths = EnvironmentPathUtils.decodePaths(getString(PDX_PATH));
				pdxPath.setPaths(paths);
			}
			
			protected boolean processChanges(
					IWorkbenchPreferenceContainer container) {
				String pdxPathKeyValue = EnvironmentPathUtils.encodePaths(pdxPath.getPaths());
				setString(PDX_PATH, pdxPathKeyValue);
				return super.processChanges(container);
			}

			protected PreferenceKey getDebuggingEnginePathKey() {
				return ENGINE_PATH;
			}

			protected PreferenceKey getEnableLoggingPreferenceKey() {
				return ENABLE_LOGGING;
			}

			protected PreferenceKey getLogFileNamePreferenceKey() {
				return LOG_FILE_NAME;
			}

			protected PreferenceKey getLogFilePathPreferenceKey() {
				return LOG_FILE_PATH;
			}
		};
	}

	/*
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#getHelpId()
	 */
	protected String getHelpId() {
		return null;
	}

	/*
	 * @see org.eclipse.dltk.internal.ui.preferences.PropertyAndPreferencePage#getPreferencePageId()
	 */
	protected String getPreferencePageId() {
		return PREFERENCE_PAGE_ID;
	}

	/*
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#getProjectHelpId()
	 */
	protected String getProjectHelpId() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @see org.eclipse.dltk.internal.ui.preferences.PropertyAndPreferencePage#getPropertyPageId()
	 */
	protected String getPropertyPageId() {
		return PROPERTY_PAGE_ID;
	}

	/*
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#setDescription()
	 */
	protected void setDescription() {
		setDescription(PreferenceMessages.DebuggingEngineDescription);
	}

	/*
	 * @see org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage#setPreferenceStore()
	 */
	protected void setPreferenceStore() {
		setPreferenceStore(TclActiveStateDebuggerPlugin.getDefault()
				.getPreferenceStore());
	}
}
