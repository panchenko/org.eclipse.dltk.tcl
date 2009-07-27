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
package org.eclipse.dltk.tcl.internal.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.tcl.core.packages.TclPackagesFactory;
import org.eclipse.dltk.tcl.core.packages.VariableValue;
import org.eclipse.dltk.ui.dialogs.IStatusDialog;
import org.eclipse.dltk.ui.dialogs.MultipleInputDialog;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Control used to edit the environment variables associated with a Interpreter
 * install
 * @since 2.0
 */
public class GlobalVariableBlock implements SelectionListener,
		ISelectionChangedListener {

	private static final String NAME_LABEL = TclInterpreterMessages.GlobalVariableBlock_Name;
	private static final String VALUE_LABEL = TclInterpreterMessages.GlobalVariableBlock_Value;

	// widgets
	protected GlobalVariableContentProvider fContentProvider;
	protected TreeViewer fVariablesViewer;
	private Button fRemoveButton;
	private Button fAddButton;
	private Button fEditButton;

	private final IStatusDialog fDialog;

	public GlobalVariableBlock(IStatusDialog fDialog) {
		this.fDialog = fDialog;
	}

	/**
	 * Creates and returns the source lookup control.
	 * 
	 * @param parent
	 *            the parent widget of this control
	 */
	public void createControlsIn(Composite parent) {
		Font font = parent.getFont();

		fVariablesViewer = new TreeViewer(parent);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 6;
		fVariablesViewer.getControl().setLayoutData(gd);
		fContentProvider = new GlobalVariableContentProvider();
		fVariablesViewer.setContentProvider(fContentProvider);
		fVariablesViewer.setLabelProvider(getLabelProvider());
		fVariablesViewer.setInput(this);
		fVariablesViewer.addSelectionChangedListener(this);

		Composite pathButtonComp = new Composite(parent, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL);
		pathButtonComp.setLayoutData(gd);
		pathButtonComp.setFont(font);

		fAddButton = createPushButton(pathButtonComp,
				TclInterpreterMessages.GlobalVariableBlock_Add);
		fAddButton.addSelectionListener(this);

		fEditButton = createPushButton(pathButtonComp,
				TclInterpreterMessages.GlobalVariableBlock_Edit);
		fEditButton.addSelectionListener(this);

		fRemoveButton = createPushButton(pathButtonComp,
				TclInterpreterMessages.GlobalVariableBlock_Remove);
		fRemoveButton.addSelectionListener(this);
	}

	/**
	 * Creates and returns a button
	 * 
	 * @param parent
	 *            parent widget
	 * @param label
	 *            label
	 * @return Button
	 */
	protected Button createPushButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		button.setText(label);
		setButtonLayoutData(button);
		return button;
	}

	/**
	 * Updates buttons and status based on current libraries
	 */
	public void update() {
		updateButtons();
		IStatus status = Status.OK_STATUS;
		GlobalVariableEntry[] vars = fContentProvider.getVariables();
		for (GlobalVariableEntry var : vars) {
			if (var.getValue() == null || var.getValue().length() == 0) {
				status = new StatusInfo(IStatus.ERROR,
						TclInterpreterMessages.GlobalVariableBlock_ErrorNoValue);
				break;
			}
		}
		updateDialogStatus(status);
	}

	public void widgetSelected(SelectionEvent e) {
		Object source = e.getSource();
		if (source == fRemoveButton) {
			fContentProvider.remove(((IStructuredSelection) fVariablesViewer
					.getSelection()).toList());
		} else if (source == fAddButton) {
			handleAdd();
		} else if (source == fEditButton) {
			edit((IStructuredSelection) fVariablesViewer.getSelection());
		}
		update();
	}

	private void edit(IStructuredSelection selection) {
		GlobalVariableEntry var = (GlobalVariableEntry) selection
				.getFirstElement();
		if (var == null) {
			return;
		}
		MultipleInputDialog dialog = new MultipleInputDialog(
				fDialog.getShell(),
				TclInterpreterMessages.GlobalVariableBlock_EditTitle);
		dialog.addTextField(NAME_LABEL, var.getName(), false);
		dialog.addVariablesField(VALUE_LABEL, var.getValue(), true);

		if (dialog.open() != Window.OK) {
			return;
		}
		String name = dialog.getStringValue(NAME_LABEL);
		String value = dialog.getStringValue(VALUE_LABEL);
		if (!var.getName().equals(name)) {
			final GlobalVariableEntry newVar = new GlobalVariableEntry(name,
					value);
			if (fContentProvider.replaceVariable(newVar, var)) {
				fVariablesViewer.refresh();
				fVariablesViewer.setSelection(new StructuredSelection(newVar),
						true);
			}
		} else {
			var.setValue(value);
			fVariablesViewer.refresh(true);
		}
	}

	public void widgetDefaultSelected(SelectionEvent e) {
	}

	private void handleAdd() {
		GlobalVariableEntry newVar = add();
		if (newVar != null) {
			fContentProvider.replaceVariable(newVar, null);
			fVariablesViewer
					.setSelection(new StructuredSelection(newVar), true);
			fVariablesViewer.refresh();
			update();
		}
	}

	private GlobalVariableEntry add() {
		MultipleInputDialog dialog = new MultipleInputDialog(
				fDialog.getShell(),
				TclInterpreterMessages.GlobalVariableBlock_AddTitle);
		dialog.addTextField(NAME_LABEL, null, false);
		dialog.addVariablesField(VALUE_LABEL, null, true);

		if (dialog.open() != Window.OK) {
			return null;
		}

		String name = dialog.getStringValue(NAME_LABEL);
		String value = dialog.getStringValue(VALUE_LABEL);

		if (name != null && value != null && name.length() > 0
				&& value.length() > 0) {
			return new GlobalVariableEntry(name.trim(), value.trim());
		}
		return null;
	}

	public void selectionChanged(SelectionChangedEvent event) {
		updateButtons();
	}

	/**
	 * Refresh the enable/disable state for the buttons.
	 */
	private void updateButtons() {
		IStructuredSelection selection = (IStructuredSelection) fVariablesViewer
				.getSelection();
		fRemoveButton.setEnabled(!selection.isEmpty());
		fEditButton.setEnabled(selection.size() == 1);
	}

	public void setValues(final EMap<String, VariableValue> vars) {
		List<GlobalVariableEntry> list = new ArrayList<GlobalVariableEntry>();
		for (Map.Entry<String, VariableValue> entry : vars.entrySet()) {
			if (entry.getValue() != null) {
				list.add(new GlobalVariableEntry(entry.getKey(), entry
						.getValue().getValue()));
			}
		}
		fContentProvider.setVariables(list.toArray(new GlobalVariableEntry[list
				.size()]));
		update();
	}

	public EMap<String, VariableValue> getValues() {
		EMap<String, VariableValue> map = new BasicEMap<String, VariableValue>();
		for (GlobalVariableEntry var : fContentProvider.getVariables()) {
			VariableValue value = TclPackagesFactory.eINSTANCE
					.createVariableValue();
			value.setValue(var.getValue());
			map.put(var.getName(), value);
		}
		return map;
	}

	protected IBaseLabelProvider getLabelProvider() {
		return new GlobalVariableLabelProvider();
	}

	protected void updateDialogStatus(IStatus status) {
		// fDialog.setSystemLibraryStatus(status);
		fDialog.updateStatusLine();
	}

	protected void setButtonLayoutData(Button button) {
		fDialog.setButtonLayoutData(button);
	}

	public static boolean equalsEMap(EMap<String, VariableValue> a,
			EMap<String, VariableValue> b) {
		if (a.size() != b.size()) {
			return false;
		}
		for (Map.Entry<String, VariableValue> entry : a.entrySet()) {
			final VariableValue value = b.get(entry.getKey());
			if (value == null) {
				return false;
			}
			if (!EcoreUtil.equals(entry.getValue(), value)) {
				return false;
			}
		}
		return true;
	}

}
