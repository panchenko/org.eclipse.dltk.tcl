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

import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.internal.ui.wizards.NewWizardMessages;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.dialogs.IProjectTemplate;
import org.eclipse.dltk.ui.dialogs.IProjectTemplateOperation;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.utils.LazyExtensionManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

final class TclProjectWizardFirstPage extends ProjectWizardFirstPage {

	public TclProjectWizardFirstPage() {
		setTitle(TclWizardMessages.ProjectCreationWizardFirstPage_title);
		setDescription(TclWizardMessages.ProjectCreationWizardFirstPage_description);
	}

	final class TclInterpreterGroup extends AbstractInterpreterGroup {

		public TclInterpreterGroup(Composite composite) {
			super(composite);
		}

		protected String getCurrentLanguageNature() {
			return TclNature.NATURE_ID;
		}

		protected String getIntereprtersPreferencePageId() {
			return "org.eclipse.dltk.tcl.preferences.interpreters"; //$NON-NLS-1$
		}
	}

	protected IInterpreterGroup createInterpreterGroup(Composite parent) {
		return new TclInterpreterGroup(parent);
	}

	protected boolean interpeterRequired() {
		/* Specially allow to create TCL project without interpreter */
		return false;
	}

	private class TclLocationGroup extends LocationGroup {

		private final SelectionButtonDialogField fLinkRadio;
		private Button fBrowseButton;

		public TclLocationGroup() {
			super();
			fLinkRadio = new SelectionButtonDialogField(SWT.RADIO);
			fLinkRadio.setDialogFieldListener(this);
			fLinkRadio
					.setLabelText("Link existing source into workspace project");
		}

		@Override
		protected void createControls(Group group, int numColumns) {
			super.createControls(group, numColumns);
			fLinkRadio.doFillIntoGrid(group, 2);

			fBrowseButton = new Button(group, SWT.PUSH);
			fBrowseButton.setFont(group.getFont());
			fBrowseButton
					.setText(NewWizardMessages.ScriptProjectWizardFirstPage_LocationGroup_browseButton_desc);
			fBrowseButton.setEnabled(fLinkRadio.isSelected());
			fBrowseButton.addSelectionListener(new SelectionListener() {
				public void widgetDefaultSelected(SelectionEvent e) {
					doLink();
				}

				public void widgetSelected(SelectionEvent e) {
					doLink();
				}
			});
			fBrowseButton.setLayoutData(StringButtonDialogField
					.gridDataForButton(fBrowseButton, 1));
		}

		@Override
		protected boolean isModeField(DialogField field, int kind) {
			return super.isModeField(field, kind)
					|| ((kind == ANY || kind == WORKSPACE) && field == fLinkRadio);
		}

		@Override
		public void dialogFieldChanged(DialogField field) {
			super.dialogFieldChanged(field);
			if (field == fLinkRadio) {
				fBrowseButton.setEnabled(fLinkRadio.isSelected());
			}
		}

		@Override
		public boolean isInWorkspace() {
			return super.isInWorkspace() || fLinkRadio.isSelected();
		}

		protected void doLink() {
			final String extensionPoint = DLTKUIPlugin.PLUGIN_ID
					+ ".projectTemplate";
			final Iterator<IProjectTemplate> iterator = new LazyExtensionManager<IProjectTemplate>(
					extensionPoint).iterator();
			if (iterator.hasNext()) {
				IProjectTemplate projectTemplate = iterator.next();
				IProject project = acquireProject();
				try {
					IProjectTemplateOperation operation = projectTemplate
							.configure(project, templateOperation, getShell());
					if (operation != null) {
						templateOperation = operation;
						fireEvent();
					}
				} finally {
					releaseProject(project);
				}
			}
		}

		private IProject acquireProject() {
			return ((TclProjectCreationWizard) getWizard()).fSecondPage
					.acquireProject();
		}

		private void releaseProject(IProject project) {
			((TclProjectCreationWizard) getWizard()).fSecondPage
					.releaseProject();
		}

		private IProjectTemplateOperation templateOperation;

		@Override
		public IStatus validate(IProject handle) {
			IStatus status = super.validate(handle);
			if (status.isOK() && fLinkRadio.isSelected()
					&& templateOperation == null) {
				return new StatusInfo(IStatus.ERROR,
						"Specify the sources to be linked into the project");
			}
			return status;
		}

	}

	@Override
	protected LocationGroup createLocationGroup() {
		return new TclLocationGroup();
	}

	protected IProjectTemplateOperation getProjectTemplateOperation() {
		return ((TclLocationGroup) fLocationGroup).templateOperation;
	}

}
