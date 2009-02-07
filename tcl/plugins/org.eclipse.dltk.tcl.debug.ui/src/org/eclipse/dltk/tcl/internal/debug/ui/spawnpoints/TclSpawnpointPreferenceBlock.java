/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.debug.ui.spawnpoints;

import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.IListAdapter;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.ListDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringDialogField;
import org.eclipse.dltk.tcl.internal.debug.TclDebugConstants;
import org.eclipse.dltk.tcl.internal.debug.TclDebugPlugin;
import org.eclipse.dltk.tcl.internal.debug.spawnpoint.SpawnpointCommandManager;
import org.eclipse.dltk.tcl.internal.debug.spawnpoint.SpawnpointCommands;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.preferences.AbstractOptionsBlock;
import org.eclipse.dltk.ui.preferences.PreferenceKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

public class TclSpawnpointPreferenceBlock extends AbstractOptionsBlock
		implements IListAdapter, ICheckStateListener {

	private static class TclSpawnpointInputDialog extends StatusDialog {

		private final StringDialogField fNameDialogField;
		private final List<String> existingEntries;

		public TclSpawnpointInputDialog(Shell parent, String value,
				List<String> existingEntries) {
			super(parent);
			this.existingEntries = existingEntries;
			if (value == null) {
				setTitle(TclSpawnpointMessages.inputDialog_newTitle);
			} else {
				setTitle(TclSpawnpointMessages.inputDialog_editTitle);
			}
			fNameDialogField = new StringDialogField();
			fNameDialogField
					.setLabelText(TclSpawnpointMessages.inputDialog_commandLabel);
			fNameDialogField.setDialogFieldListener(new IDialogFieldListener() {
				public void dialogFieldChanged(DialogField field) {
					doValidation();
				}
			});
			fNameDialogField.setText(value != null ? value : Util.EMPTY_STRING);
		}

		public String getResult() {
			return fNameDialogField.getText().trim();
		}

		protected Control createDialogArea(Composite parent) {
			Composite composite = (Composite) super.createDialogArea(parent);
			Composite inner = new Composite(composite, SWT.NONE);
			GridLayout layout = new GridLayout();
			layout.marginHeight = 0;
			layout.marginWidth = 0;
			layout.numColumns = 2;
			inner.setLayout(layout);
			fNameDialogField.doFillIntoGrid(inner, 2);
			LayoutUtil.setHorizontalGrabbing(fNameDialogField
					.getTextControl(null));
			LayoutUtil.setWidthHint(fNameDialogField.getTextControl(null),
					convertWidthInCharsToPixels(45));
			fNameDialogField.postSetFocusOnDialogField(parent.getDisplay());
			applyDialogFont(composite);
			return composite;
		}

		private void doValidation() {
			StatusInfo status = new StatusInfo();
			final String newText = fNameDialogField.getText();
			if (newText.length() == 0) {
				status
						.setError(TclSpawnpointMessages.inputDialog_errorEmptyCommandName);
			} else {
				if (!SpawnpointCommandManager.isValidCommandName(newText)) {
					status
							.setError(TclSpawnpointMessages.inputDialog_errorInvalidCommandName);
				} else if (existingEntries.contains(newText)) {
					status
							.setError(TclSpawnpointMessages.inputDialog_errorDuplicateCommandName);
				}
			}
			updateStatus(status);
		}

	}

	private class TclSpawnpointLabelProvider extends LabelProvider implements
			IFontProvider {

		private Font boldFont = null;

		public Font getFont(Object element) {
			if (contributed.contains(element)) {
				if (boldFont == null) {
					boldFont = JFaceResources.getDialogFontDescriptor()
							.setStyle(SWT.BOLD)
							.createFont(Display.getCurrent());
				}
				return boldFont;
			}
			return null;
		}

		@Override
		public void dispose() {
			if (boldFont != null) {
				boldFont.dispose();
				boldFont = null;
			}
			super.dispose();
		}

	}

	private static final int IDX_ADD = 0;
	private static final int IDX_EDIT = 1;
	private static final int IDX_REMOVE = 2;

	private static final PreferenceKey[] KEYS = new PreferenceKey[] { new PreferenceKey(
			TclDebugPlugin.PLUGIN_ID,
			TclDebugConstants.PREF_SPAWNPOINT_COMMANDS) };

	private final Set<String> contributed;

	/**
	 * @param context
	 * @param project
	 * @param container
	 */
	public TclSpawnpointPreferenceBlock(IStatusChangeListener context,
			IProject project, IWorkbenchPreferenceContainer container) {
		super(context, project, KEYS, container);
		this.contributed = SpawnpointCommandManager.getContributedCommands();
	}

	private SpawnpointCommands commands;
	private ListDialogField listDialogField;

	protected Control createOptionsBlock(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		control.setLayout(new GridLayout(3, false));
		final String[] buttons = new String[] {
				TclSpawnpointMessages.button_Add,
				TclSpawnpointMessages.button_Edit,
				TclSpawnpointMessages.button_Delete };
		listDialogField = new ListDialogField(this, buttons,
				new TclSpawnpointLabelProvider()) {

			protected TableViewer createTableViewer(Composite parent) {
				Table table = new Table(parent, getListStyle() | SWT.CHECK);
				table.setFont(parent.getFont());
				return new CheckboxTableViewer(table);
			}

		};
		listDialogField.doFillIntoGrid(control, 3);
		getCheckboxTableViewer().addCheckStateListener(this);
		return control;
	}

	@Override
	protected void initialize() {
		super.initialize();
		initValues();
	}

	public void customButtonPressed(ListDialogField field, int index) {
		final CheckboxTableViewer viewer = getCheckboxTableViewer();
		switch (index) {
		case IDX_ADD: {
			final String newValue = doEdit(null);
			if (newValue != null) {
				field.addElement(newValue);
				viewer.setChecked(newValue, true);
				commands.getCommands().add(newValue);
				commands.getSelectedCommands().add(newValue);
				saveCommands();
			}
			break;
		}
		case IDX_EDIT: {
			@SuppressWarnings("unchecked")
			final List<String> selection = field.getSelectedElements();
			if (canEdit(selection)) {
				final String oldValue = selection.get(0);
				final String newValue = doEdit(oldValue);
				if (newValue != null && !newValue.equals(oldValue)) {
					final boolean wasChecked = viewer.getChecked(oldValue);
					field.replaceElement(oldValue, newValue);
					viewer.setChecked(newValue, wasChecked);
					commands.getCommands().remove(oldValue);
					commands.getCommands().add(newValue);
					if (wasChecked) {
						commands.getSelectedCommands().remove(oldValue);
						commands.getSelectedCommands().add(newValue);
					}
					saveCommands();
				}
			}
			break;
		}
		case IDX_REMOVE: {
			@SuppressWarnings("unchecked")
			final List<String> selection = field.getSelectedElements();
			if (canDelete(selection)) {
				field.removeElements(selection);
				commands.getCommands().removeAll(selection);
				commands.getSelectedCommands().removeAll(selection);
				saveCommands();
			}
			break;
		}
		}
	}

	private CheckboxTableViewer getCheckboxTableViewer() {
		return (CheckboxTableViewer) listDialogField.getTableViewer();
	}

	/**
	 * @param value
	 * @return
	 */
	private String doEdit(String value) {
		final TclSpawnpointInputDialog dialog = new TclSpawnpointInputDialog(
				getShell(), value, commands.getCommands());
		if (dialog.open() == Window.OK) {
			return dialog.getResult();
		} else {
			return null;
		}
	}

	public void doubleClicked(ListDialogField field) {
		customButtonPressed(field, IDX_EDIT);
	}

	public void selectionChanged(ListDialogField field) {
		@SuppressWarnings("unchecked")
		final List<String> selection = field.getSelectedElements();
		field.enableButton(IDX_REMOVE, canDelete(selection));
		field.enableButton(IDX_EDIT, canEdit(selection));
	}

	private boolean canDelete(final List<String> selection) {
		if (selection.isEmpty()) {
			return false;
		}
		for (String selectedCommand : selection) {
			if (contributed.contains(selectedCommand)) {
				return false;
			}
		}
		return true;
	}

	private boolean canEdit(final List<String> selection) {
		return selection.size() == 1 && !contributed.contains(selection.get(0));
	}

	public void checkStateChanged(CheckStateChangedEvent event) {
		if (event.getChecked()) {
			commands.getSelectedCommands().add((String) event.getElement());
		} else {
			commands.getSelectedCommands().remove(event.getElement());
		}
		saveCommands();
	}

	private void saveCommands() {
		setString(KEYS[0], SpawnpointCommandManager.encode(commands));
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		initValues();
	}

	private void initValues() {
		this.commands = SpawnpointCommandManager.decode(getString(KEYS[0]));
		listDialogField.setElements(commands.getCommands());
		getCheckboxTableViewer().setCheckedElements(
				commands.getSelectedCommands().toArray());
	}

	@Override
	protected String getFullBuildDialogMessage() {
		return TclSpawnpointMessages.preferenceBlock_rebuildMessage;
	}

	@Override
	protected String getBuildDialogTitle() {
		return TclSpawnpointMessages.preferenceBlock_rebuildTitle;
	}
}
