/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import java.util.List;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.IProjectWizardInitializer.IProjectWizardState;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

final class TclProjectWizardFirstPage extends ProjectWizardFirstPage {

	private final List<TclProjectTemplateEntry> fOptions;

	public TclProjectWizardFirstPage(List<TclProjectTemplateEntry> options) {
		setTitle(TclWizardMessages.ProjectCreationWizardFirstPage_title);
		setDescription(TclWizardMessages.ProjectCreationWizardFirstPage_description);
		this.fOptions = options;
	}

	@Override
	protected boolean interpeterRequired() {
		/* Specially allow to create TCL project without interpreter */
		return false;
	}

	private class TclLocationGroup extends LocationGroup {

		@Override
		public void createControls(Composite composite) {
			if (fOptions.isEmpty()) {
				super.createControls(composite);
				return;
			}
			final int numColumns = 3;
			final Group group = new Group(composite, SWT.NONE);
			group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			group.setLayout(initGridLayout(new GridLayout(numColumns, false),
					true));
			group
					.setText(TclWizardMessages.TclProjectWizardFirstPage_LocationGroup_modeTitle);
			createModeControls(group, numColumns);
			for (TclProjectTemplateEntry entry : fOptions) {
				entry.fLinkRadio.setDialogFieldListener(this);
				entry.createControls(group, numColumns);
			}

			final Group groupLocation = new Group(composite, SWT.NONE);
			groupLocation.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			groupLocation.setLayout(initGridLayout(new GridLayout(numColumns,
					false), true));
			groupLocation
					.setText(TclWizardMessages.TclProjectWizardFirstPage_LocationGroup_locationTitle);
			createEnvironmentControls(groupLocation, numColumns);
			createLocationControls(groupLocation, numColumns);
		}

		@Override
		protected boolean isModeField(DialogField field, int kind) {
			if (super.isModeField(field, kind)) {
				return true;
			}
			if (kind == ANY || kind == WORKSPACE) {
				for (TclProjectTemplateEntry entry : fOptions) {
					if (field == entry.fLinkRadio) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		protected void initialize() {
			final IProjectWizardState state = getWizardState();
			final String mode = state.getMode();
			if (mode == null) {
				super.initialize();
			} else {
				fWorkspaceRadio.setSelection(IProjectWizardState.MODE_WORKSPACE
						.equals(mode));
				fExternalRadio.setSelection(IProjectWizardState.MODE_EXTERNAL
						.equals(mode));
				for (TclProjectTemplateEntry entry : fOptions) {
					entry.fLinkRadio.setSelection(entry.id.equals(mode));
				}
				if (state.getEnvironment() != null && canChangeEnvironment()) {
					setEnvironment(state.getEnvironment());
				}
				if (state.getExternalLocation() != null && canChangeLocation()) {
					fLocation.setText(state.getExternalLocation());
				}
			}
			setTooltip(fWorkspaceRadio, state
					.getToolTipText(IProjectWizardState.MODE_WORKSPACE));
			setTooltip(fExternalRadio, state
					.getToolTipText(IProjectWizardState.MODE_EXTERNAL));
			for (TclProjectTemplateEntry entry : fOptions) {
				setTooltip(entry.fLinkRadio, state.getToolTipText(entry.id));
			}
		}

		/**
		 * @param radio
		 * @param tooltip
		 */
		private void setTooltip(SelectionButtonDialogField radio, String tooltip) {
			radio.getSelectionButton().setToolTipText(tooltip);
		}

		@Override
		public boolean isInWorkspace() {
			if (super.isInWorkspace()) {
				return true;
			}
			return getSelectedEntry() != null;
		}

		@Override
		protected boolean canChangeEnvironment() {
			return super.canChangeEnvironment() || getSelectedEntry() != null;
		}

		boolean getDetect() {
			final TclProjectTemplateEntry entry = getSelectedEntry();
			return entry != null && entry.getTemplate().getDetect();
		}
	}

	protected TclProjectTemplateEntry getSelectedEntry() {
		for (TclProjectTemplateEntry entry : fOptions) {
			if (entry.isSelected()) {
				return entry;
			}
		}
		return null;
	}

	@Override
	protected IStatus validateProject() {
		IStatus status = super.validateProject();
		if (status != null && !status.isOK()) {
			return status;
		}
		final TclProjectTemplateEntry entry = getSelectedEntry();
		if (entry == null) {
			return status;
		}
		return status;
	}

	@Override
	protected LocationGroup createLocationGroup() {
		return new TclLocationGroup();
	}

	@Override
	public boolean getDetect() {
		return super.getDetect()
				|| ((TclLocationGroup) fLocationGroup).getDetect();
	}
}
