/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.tcl.internal.ui.documentation.ManPagesLocationsBlock;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore.OverlayKey;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

class ManPagesConfigurationBlock extends AbstractConfigurationBlock implements
		IStatusChangeListener, IShellProvider {

	private ManPagesLocationsBlock fBlock;

	public ManPagesConfigurationBlock(OverlayPreferenceStore store,
			PreferencePage page) {
		super(store, page);
		store.addKeys(createOverlayStoreKeys());
		fBlock = new ManPagesLocationsBlock(store, this);
	}

	private OverlayKey[] createOverlayStoreKeys() {
		List<OverlayKey> keys = new ArrayList<OverlayKey>();
		keys.add(new OverlayKey(OverlayPreferenceStore.STRING,
				TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS));
		return keys.toArray(new OverlayKey[keys.size()]);
	}

	/**
	 * Creates page for mark occurrences preferences.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the control for the preference page
	 */
	public Control createControl(Composite parent) {
		Composite control = new Composite(parent, SWT.NONE);// parent=scrolled
		GridLayout layout = new GridLayout();
		control.setLayout(layout);

		fBlock.createControl(control);

		return control;
	}

	@Override
	public void initialize() {
		super.initialize();
		fBlock.initialize();
	}

	@Override
	public void performDefaults() {
		super.performDefaults();
		fBlock.setDefaults();
	}

	@Override
	public void performOk() {
		super.performOk();
		fBlock.performApply();
	}

	@Override
	public Shell getShell() {
		return super.getShell();
	}

	public void statusChanged(IStatus status) {
		updateStatus(status);
	}

}
