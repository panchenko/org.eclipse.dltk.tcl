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
package org.eclipse.dltk.tcl.internal.ui.preferences;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclCorePreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	protected static class TclCorePreferenceBlock extends AbstractOptionsBlock {

		private static final PreferenceKey[] KEYS = new PreferenceKey[] {
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_REMOTE),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_REMOTE) };

		/**
		 * @param context
		 * @param project
		 * @param allKeys
		 * @param container
		 */
		public TclCorePreferenceBlock(IStatusChangeListener context,
				IProject project, IWorkbenchPreferenceContainer container) {
			super(context, project, KEYS, container);
		}

		private void createCheckbox(Composite block, String label,
				PreferenceKey key) {
			final Button checkButton = SWTFactory.createCheckButton(block,
					label);
			GridData data = new GridData();
			data.horizontalIndent = 16;
			checkButton.setLayoutData(data);
			bindControl(checkButton, key, null);
		}

		protected Control createOptionsBlock(Composite parent) {
			Composite block = SWTFactory.createComposite(parent, parent
					.getFont(), 1, 1, GridData.FILL_HORIZONTAL);
			SWTFactory
					.createLabel(
							block,
							TclPreferencesMessages.TclCorePreferencePage_checkContentWithoutExtension,
							1);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_local, KEYS[0]);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_remote,
					KEYS[1]);
			SWTFactory
					.createLabel(
							block,
							TclPreferencesMessages.TclCorePreferencePage_checkContentAnyExtension,
							1);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_local, KEYS[2]);
			createCheckbox(block,
					TclPreferencesMessages.TclCorePreferencePage_remote,
					KEYS[3]);
			return block;
		}
	}

	protected AbstractOptionsBlock createOptionsBlock(
			IStatusChangeListener newStatusChangedListener, IProject project,
			IWorkbenchPreferenceContainer container) {
		return new TclCorePreferenceBlock(newStatusChangedListener, project,
				container);
	}

	protected String getHelpId() {
		return null;
	}

	protected String getProjectHelpId() {
		return null;
	}

	protected void setDescription() {
		// empty
	}

	protected void setPreferenceStore() {
		// empty
	}

	protected String getPreferencePageId() {
		return null;
	}

	protected String getPropertyPageId() {
		return null;
	}

}
