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
package org.eclipse.dltk.tcl.ui.manpages.dialogs;

import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.documentation.ManPagesLocationsBlock;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @since 2.0
 */
public class ManPagesConfigurationDialog extends StatusDialog implements
		IStatusChangeListener, IShellProvider {

	private ManPagesLocationsBlock fBlock;

	public ManPagesConfigurationDialog(Shell shell) {
		super(shell);
		setTitle("Tcl Manual Pages");
		setShellStyle(getShellStyle() | SWT.RESIZE);
		fBlock = new ManPagesLocationsBlock(this, true);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite dialogArea = (Composite) super.createDialogArea(parent);
		final Composite control = new Composite(dialogArea, SWT.NONE);
		final GridLayout layout = new GridLayout();
		layout.marginWidth = layout.marginHeight = 0;
		control.setLayout(layout);
		control.setLayoutData(new GridData(GridData.FILL_BOTH));
		fBlock.createControl(control);
		fBlock.initialize();
		return dialogArea;
	}

	@Override
	protected void okPressed() {
		try {
			fBlock.save();
		} catch (IOException e) {
			ErrorDialog.openError(getShell(), "Error saving manual pages", e
					.getMessage(), new Status(IStatus.ERROR, TclUI.PLUGIN_ID, e
					.getMessage(), e));
			return;
		}
		super.okPressed();
	}

	@Override
	public Shell getShell() {
		return super.getShell();
	}

	public void statusChanged(IStatus status) {
		updateStatus(status);
	}

}
