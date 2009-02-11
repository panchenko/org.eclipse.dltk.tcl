/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.text.folding;

import java.util.List;

import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.text.folding.SourceCodeFoldingPreferenceBlock;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;

/**
 * Tcl source code folding preferences.
 */
public class TclFoldingPreferenceBlock extends SourceCodeFoldingPreferenceBlock {

	private Button fFoldOtherEnabled;
	private Button fInitFoldOtherBlocks;
	
	private ListBlock fExcludePatterns;
	private ListBlock fIncludePatterns;

	public TclFoldingPreferenceBlock(OverlayPreferenceStore store,
			PreferencePage page) {
		super(store, page);
	}
	
	public Control createControl(Composite parent) {
		Control control = super.createControl(parent);
		
		SWTFactory.setUseSelectionInverse(fFoldOtherEnabled);
		createDependency(fFoldOtherEnabled, new Control[] {fInitFoldOtherBlocks});
		
		return control;
	}

	protected void addInitiallyFoldOptions(Group group) {
		super.addInitiallyFoldOptions(group);

		fInitFoldOtherBlocks = createCheckBox(group,
				TclFoldingMessages.DefaultFoldingPreferenceBlock_other,
				TclPreferenceConstants.EDITOR_FOLDING_INIT_OTHER);
	}

	protected void addOverlayKeys(List keys) {
		super.addOverlayKeys(keys);

		keys.add(new OverlayPreferenceStore.OverlayKey(
				OverlayPreferenceStore.INT,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS));

		keys.add(new OverlayPreferenceStore.OverlayKey(
				OverlayPreferenceStore.STRING,
				TclPreferenceConstants.EDITOR_FOLDING_INCLUDE_LIST));

		keys.add(new OverlayPreferenceStore.OverlayKey(
				OverlayPreferenceStore.STRING,
				TclPreferenceConstants.EDITOR_FOLDING_EXCLUDE_LIST));

		keys.add(new OverlayPreferenceStore.OverlayKey(
				OverlayPreferenceStore.BOOLEAN,
				TclPreferenceConstants.EDITOR_FOLDING_INIT_OTHER));
	}

	protected void createOptionsControl(Composite composite) {
		Group group = SWTFactory.createGroup(composite,
				TclFoldingMessages.TclFoldingPreferenceBlock_10, 1, 1,
				GridData.FILL_HORIZONTAL);

		fFoldOtherEnabled = createRadioButton(
				group,
				TclFoldingMessages.TclFoldingPreferenceBlock_11,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
				Integer
						.valueOf(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_OFF));

		createRadioButton(
				group,
				TclFoldingMessages.TclFoldingPreferenceBlock_12,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
				Integer
						.valueOf(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_EXCLUDE));

		fExcludePatterns = new ListBlock(group,
				TclPreferenceConstants.EDITOR_FOLDING_EXCLUDE_LIST);

		createRadioButton(
				group,
				TclFoldingMessages.TclFoldingPreferenceBlock_13,
				TclPreferenceConstants.EDITOR_FOLDING_BLOCKS,
				Integer
						.valueOf(TclPreferenceConstants.EDITOR_FOLDING_BLOCKS_INCLUDE));

		fIncludePatterns = new ListBlock(group,
				TclPreferenceConstants.EDITOR_FOLDING_INCLUDE_LIST);
	}

	protected String getInitiallyFoldClassesKey() {
		return TclPreferenceConstants.EDITOR_FOLDING_INIT_NAMESPACES;
	}

	protected String getInitiallyFoldMethodsKey() {
		return TclPreferenceConstants.EDITOR_FOLDING_INIT_BLOCKS;
	}

	protected String getInitiallyFoldClassesText() {
		return TclFoldingMessages.DefaultFoldingPreferenceBlock_innerTypes;
	}

	protected String getInitiallyFoldMethodsText() {
		return TclFoldingMessages.DefaultFoldingPreferenceBlock_methods;
	}

	protected class ListBlock {
		private ListViewer fList;
		private String fKey;
		private Button fAddButton;
		private Button fRemoveButton;

		public ListBlock(Composite parent, String key) {
			fKey = key;
			createControl(parent);
		}

		private Control createControl(Composite parent) {
			Font font = parent.getFont();
			Composite comp = new Composite(parent, SWT.NONE);
			GridLayout topLayout = new GridLayout();
			topLayout.numColumns = 2;
			topLayout.marginHeight = 0;
			topLayout.marginWidth = 0;
			comp.setLayout(topLayout);
			GridData gd = new GridData(GridData.FILL_BOTH);
			comp.setLayoutData(gd);
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
					TclFoldingMessages.TclFoldingPreferenceBlock_0);
			fAddButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					IInputValidator validator = new IInputValidator() {
						public String isValid(String newText) {
							if (newText.trim().length() > 0
									&& newText.matches("[_a-zA-Z]*")) //$NON-NLS-1$
								return null;
							return TclFoldingMessages.TclFoldingPreferenceBlock_2;
						}
					};
					InputDialog dlg = new InputDialog(null,
							TclFoldingMessages.TclFoldingPreferenceBlock_3,
							TclFoldingMessages.TclFoldingPreferenceBlock_4,
							"", validator); //$NON-NLS-3$
					if (dlg.open() == InputDialog.OK) {
						fList.add(dlg.getValue());
						save();
					}
				}
			});
			fRemoveButton = createPushButton(pathButtonComp,
					TclFoldingMessages.TclFoldingPreferenceBlock_6);
			fRemoveButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
				}

				public void widgetSelected(SelectionEvent e) {
					ISelection s = fList.getSelection();
					if (s instanceof IStructuredSelection) {
						IStructuredSelection sel = (IStructuredSelection) s;
						fList.remove(sel.toArray());
						save();
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
		public int getButtonWidthHint(Button button) {
			button.setFont(JFaceResources.getDialogFont());
			PixelConverter converter = new PixelConverter(button);
			int widthHint = converter
					.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
			return Math.max(widthHint, button.computeSize(SWT.DEFAULT,
					SWT.DEFAULT, true).x);
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

		public void save() {
			String items[] = getEntries();
			StringBuffer buf = new StringBuffer();
			for (int i = 0; i < items.length; i++) {
				buf.append(items[i]);
				if (i != items.length - 1)
					buf.append(","); //$NON-NLS-1$
			}
			getPreferenceStore().setValue(fKey, buf.toString());
		}

		public void initialize() {
			String val = getPreferenceStore().getString(fKey);
			if (val != null) {
				String items[] = val.split(","); //$NON-NLS-1$
				setEntries(items);
			}

		}

		public void performDefault() {
			String val = getPreferenceStore().getDefaultString(fKey);
			if (val != null) {
				String items[] = val.split(","); //$NON-NLS-1$
				setEntries(items);
			}
		}
	}

	public void initialize() {
		super.initialize();
		fExcludePatterns.initialize();
		fIncludePatterns.initialize();
	}

	public void performDefaults() {
		super.performDefaults();
		fExcludePatterns.performDefault();
		fIncludePatterns.performDefault();
	}
}
