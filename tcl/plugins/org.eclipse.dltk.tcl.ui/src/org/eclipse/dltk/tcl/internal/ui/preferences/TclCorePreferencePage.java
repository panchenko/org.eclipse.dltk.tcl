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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPropertyAndPreferencePage;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclCorePreferencePage extends
		AbstractConfigurationBlockPropertyAndPreferencePage {

	private static int IDX_ADD = 0;
	private static int IDX_EDIT = 1;
	private static int IDX_REMOVE = 2;

	protected static class TclCorePreferenceBlock extends AbstractOptionsBlock {

		private class TclCheckContentAdapter implements IListAdapter,
				IDialogFieldListener {

			public void customButtonPressed(ListDialogField field, int index) {
				String edited = null;
				if (index != IDX_ADD) {
					edited = (String) field.getSelectedElements().get(0);
				}
				if (index == IDX_ADD || index == IDX_EDIT) {
					TclCheckContentExcludeInputDialog dialog = new TclCheckContentExcludeInputDialog(
							getShell(), edited, field.getElements());
					if (dialog.open() == Window.OK) {
						if (edited != null) {
							field.replaceElement(edited, dialog.getResult());
						} else {
							field.addElement(dialog.getResult());
						}
					}
				}
			}

			private boolean canEdit(List selectedElements) {
				return selectedElements.size() == 1;
			}

			public void doubleClicked(ListDialogField field) {
				if (canEdit(field.getSelectedElements())) {
					customButtonPressed(field, IDX_EDIT);
				}
			}

			public void selectionChanged(ListDialogField field) {
				List selectedElements = field.getSelectedElements();
				field.enableButton(IDX_EDIT, canEdit(selectedElements));
			}

			public void dialogFieldChanged(DialogField field) {
				updateExcludes();
			}

		}

		protected void initialize() {
			super.initialize();
			final List excludePatterns = new ArrayList();
			final String[] patterns = TextUtils.split(getString(KEYS[4]),
					TclCorePreferences.CHECK_CONTENT_EXCLUDE_SEPARATOR);
			if (patterns != null) {
				excludePatterns.addAll(Arrays.asList(patterns));
			}
			excludeDialog.setElements(excludePatterns);
		}

		private void updateExcludes() {
			setString(KEYS[4], TextUtils.join(excludeDialog.getElements(),
					TclCorePreferences.CHECK_CONTENT_EXCLUDE_SEPARATOR));
		}

		private static final PreferenceKey[] KEYS = new PreferenceKey[] {
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EMPTY_EXTENSION_REMOTE),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_LOCAL),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_ANY_EXTENSION_REMOTE),
				new PreferenceKey(TclPlugin.PLUGIN_ID,
						TclCorePreferences.CHECK_CONTENT_EXCLUDES) };

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

		private ListDialogField excludeDialog;

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
			SWTFactory
					.createLabel(
							block,
							TclPreferencesMessages.TclCorePreferencePage_checkContentExcludes,
							1);

			final PixelConverter conv = new PixelConverter(block);
			final Composite excludeComposite = SWTFactory.createComposite(
					block, block.getFont(), 1, 1, GridData.FILL_HORIZONTAL);
//			((GridData) excludeComposite.getLayoutData()).heightHint = conv
//					.convertHeightInCharsToPixels(6);
			final GridLayout excludeLayout = new GridLayout();
			excludeLayout.numColumns = 2;
			excludeComposite.setLayout(excludeLayout);

			final TclCheckContentAdapter adapter = new TclCheckContentAdapter();
			final String[] buttons = new String[] {
					TclPreferencesMessages.TclCorePreferencePage_checkContentAddExclude,
					TclPreferencesMessages.TclCorePreferencePage_checkContentEditExclude,
					TclPreferencesMessages.TclCorePreferencePage_checkContentRemoveExclude };
			excludeDialog = new ListDialogField(adapter, buttons,
					new LabelProvider());
			excludeDialog.setDialogFieldListener(adapter);
			excludeDialog.setRemoveButtonIndex(IDX_REMOVE);

			excludeDialog.setViewerSorter(new ViewerSorter());
			excludeDialog.getListControl(excludeComposite).setLayoutData(
					new GridData(GridData.FILL_BOTH));
			excludeDialog.getButtonBox(excludeComposite).setLayoutData(
					new GridData(GridData.HORIZONTAL_ALIGN_FILL
							| GridData.VERTICAL_ALIGN_BEGINNING));
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
