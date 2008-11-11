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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.io.File;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class InstrumentationPatternList {

	private ListViewer fList;
	private final String title;
	private Button fAddButton;
	private Button fRemoveButton;

	/**
	 * @param parent
	 * @param string
	 */
	public InstrumentationPatternList(Composite parent, String title,
			String message) {
		this.title = title;
		createControl(parent, message);
	}

	private Control createControl(final Composite parent, String message) {
		Font font = parent.getFont();
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 2;
		topLayout.marginHeight = 0;
		topLayout.marginWidth = 0;
		comp.setLayout(topLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);

		Label messageLabel = new Label(comp, SWT.NONE);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL);
		gd.horizontalSpan = 2;
		messageLabel.setText(message);
		messageLabel.setLayoutData(gd);

		fList = new ListViewer(comp);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 6;
		fList.getControl().setLayoutData(gd);
		Composite pathButtonComp = new Composite(comp, SWT.NONE);
		GridLayout pathButtonLayout = new GridLayout();
		pathButtonLayout.marginHeight = 0;
		pathButtonLayout.marginWidth = 0;
		pathButtonComp.setLayout(pathButtonLayout);
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.HORIZONTAL_ALIGN_FILL);
		pathButtonComp.setLayoutData(gd);
		pathButtonComp.setFont(font);
		fAddButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_pattern_AddButton);
		fAddButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IInputValidator validator = new IInputValidator() {
					public String isValid(String newText) {
						newText = newText.trim();
						if (newText.length() == 0) {
							return PreferenceMessages.instrumentation_emptyPatternMessage;
						}
						if (newText.indexOf(File.pathSeparator) >= 0) {
							return NLS
									.bind(
											PreferenceMessages.instrumentation_pathSeparatorInPatten,
											String.valueOf(File.pathSeparator));
						}
						return null;
					}
				};
				InputDialog dlg = new InputDialog(
						parent.getShell(),
						PreferenceMessages.instrumentation_patternDialogTitle,
						title
								+ PreferenceMessages.instrumentation_patternDialogPromptSuffix,
						Util.EMPTY_STRING, validator);
				if (dlg.open() == Window.OK) {
					fList.add(dlg.getValue());
				}
			}
		});
		fRemoveButton = createPushButton(pathButtonComp,
				PreferenceMessages.instrumentation_pattern_RemoveButton);
		fRemoveButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ISelection s = fList.getSelection();
				if (s instanceof IStructuredSelection) {
					IStructuredSelection sel = (IStructuredSelection) s;
					fList.remove(sel.toArray());
				}
			}
		});
		return comp;
	}

	protected Button createPushButton(Composite parent, String label) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData();
		button.setLayoutData(gd);
		gd.widthHint = getButtonWidthHint(button);
		gd.horizontalAlignment = GridData.FILL;
		return button;
	}

	/**
	 * Returns a width hint for a button control.
	 */
	private int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter
				.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT,
				true).x);
	}

	private String[] getEntries() {
		return fList.getList().getItems();
	}

	private void setEntries(String items[]) {
		fList.remove(fList.getList().getItems());
		for (int i = 0; i < items.length; i++) {
			if (items[i].trim().length() > 0)
				fList.add(items[i]);
		}
	}

	/**
	 * @param string
	 */
	public void setInput(String string) {
		setEntries(TextUtils.split(string, File.pathSeparatorChar));
	}

	/**
	 * @return
	 */
	public String getValue() {
		return TextUtils.join(getEntries(), File.pathSeparatorChar);
	}

}
