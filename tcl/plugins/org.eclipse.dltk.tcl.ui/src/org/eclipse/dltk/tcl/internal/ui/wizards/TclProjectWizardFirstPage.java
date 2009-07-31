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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.SelectionButtonDialogField;
import org.eclipse.dltk.internal.ui.wizards.dialogfields.StringButtonDialogField;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.dialogs.ControlStatus;
import org.eclipse.dltk.ui.dialogs.IProjectTemplate;
import org.eclipse.dltk.ui.dialogs.IProjectTemplateOperation;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.utils.LazyExtensionManager;
import org.eclipse.dltk.utils.LazyExtensionManager.Descriptor;
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

	static class ProjectTemplateDescriptor extends Descriptor<IProjectTemplate> {
		final String nature;
		final String name;
		final String browseButton;

		/**
		 * @param configurationElement
		 */
		public ProjectTemplateDescriptor(TclProjectTemplateManager manager,
				IConfigurationElement configurationElement) {
			super(manager, configurationElement);
			this.nature = configurationElement.getAttribute("nature");
			this.name = configurationElement.getAttribute("name");
			this.browseButton = configurationElement
					.getAttribute("browseButton");
		}

	}

	static class TclProjectTemplateManager extends
			LazyExtensionManager<IProjectTemplate> {

		/**
		 * @param extensionPoint
		 */
		public TclProjectTemplateManager() {
			super(DLTKUIPlugin.PLUGIN_ID + ".projectTemplate"); //$NON-NLS-1$
		}

		@Override
		protected boolean isValidDescriptor(
				Descriptor<IProjectTemplate> descriptor) {
			String natureId = ((ProjectTemplateDescriptor) descriptor).nature;
			return natureId == null || TclNature.NATURE_ID.equals(natureId);
		}

		@Override
		protected Descriptor<IProjectTemplate> createDescriptor(
				IConfigurationElement confElement) {
			return new ProjectTemplateDescriptor(this, confElement);
		}

	}

	private class TclLocationGroup extends LocationGroup {

		private class TclProjectTemplateEntry {
			final ProjectTemplateDescriptor descriptor;
			final SelectionButtonDialogField fLinkRadio;
			Button fBrowseButton;

			/**
			 * @param descriptor
			 */
			public TclProjectTemplateEntry(ProjectTemplateDescriptor descriptor) {
				this.descriptor = descriptor;
				fLinkRadio = new SelectionButtonDialogField(SWT.RADIO);
				fLinkRadio.setLabelText(descriptor.name);
			}

			/**
			 * @param group
			 */
			public void createControls(Group group) {
				fLinkRadio.doFillIntoGrid(group, 2);
				fBrowseButton = new Button(group, SWT.PUSH);
				fBrowseButton.setFont(group.getFont());
				fBrowseButton.setText(this.descriptor.browseButton);
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

			protected void doLink() {
				final IProjectTemplate projectTemplate = descriptor.get();
				if (projectTemplate != null) {
					IProject project = acquireProject();
					try {

						projectTemplate
								.setCurrentEnvironment(getInterpreterEnvironment());
						IProjectTemplateOperation operation = projectTemplate
								.configure(project, templateOperation,
										getShell());
						if (operation != null) {
							templateOperation = operation;
							fireEvent();
						}
					} finally {
						releaseProject(project);
					}
					fireEvent();
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

			/**
			 * @param handle
			 * @return
			 */
			public IStatus validate(IProject handle) {
				final IProjectTemplate projectTemplate = descriptor.get();
				if (projectTemplate != null) {
					return projectTemplate.validate(templateOperation);
				}
				return Status.OK_STATUS;
			}

		}

		private final List<TclProjectTemplateEntry> fOptions = new ArrayList<TclProjectTemplateEntry>();

		public TclLocationGroup() {
			super();
			TclProjectTemplateManager manager = new TclProjectTemplateManager();
			for (Iterator<Descriptor<IProjectTemplate>> i = manager
					.descriptorIterator(); i.hasNext();) {
				ProjectTemplateDescriptor d = (ProjectTemplateDescriptor) i
						.next();
				TclProjectTemplateEntry entry = new TclProjectTemplateEntry(d);
				entry.fLinkRadio.setDialogFieldListener(this);
				fOptions.add(entry);
			}
		}

		@Override
		protected void createControls(Group group, int numColumns) {
			super.createControls(group, numColumns);
			for (TclProjectTemplateEntry entry : fOptions) {
				entry.createControls(group);
			}
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
		public void dialogFieldChanged(DialogField field) {
			super.dialogFieldChanged(field);
			for (TclProjectTemplateEntry entry : fOptions) {
				if (field == entry.fLinkRadio) {
					entry.fBrowseButton.setEnabled(isValidProject()
							&& entry.fLinkRadio.isSelected());
					break;
				}
			}
		}

		@Override
		public boolean isInWorkspace() {
			if (super.isInWorkspace()) {
				return true;
			}
			for (TclProjectTemplateEntry entry : fOptions) {
				if (entry.fLinkRadio.isSelected()) {
					return true;
				}
			}
			return false;
		}

		@Override
		public IStatus validate(IProject handle) {
			IStatus status = super.validate(handle);
			if (status.isOK()) {
				for (TclProjectTemplateEntry entry : fOptions) {
					if (entry.fLinkRadio.isSelected()) {
						final IStatus entryStatus = entry.validate(handle);
						if (!entryStatus.isOK()) {
							return new ControlStatus(entryStatus.getSeverity(),
									entryStatus.getMessage(), entry.fLinkRadio
											.getSelectionButton());
						}
					}
				}
			}
			return status;
		}

		/**
		 * @return
		 */
		IProjectTemplateOperation getSelectedTemplateOperation() {
			for (TclProjectTemplateEntry entry : fOptions) {
				if (entry.fLinkRadio.isSelected()) {
					return entry.templateOperation;
				}
			}
			return null;
		}

		@Override
		public void update(Observable o, Object arg) {
			super.update(o, arg);
			if (o instanceof NameGroup) {
				for (TclProjectTemplateEntry entry : fOptions) {
					if (entry.fLinkRadio.isSelected()) {
						entry.fBrowseButton.setEnabled(isValidProject());
						break;
					}
				}
			}
		}

	}

	@Override
	protected LocationGroup createLocationGroup() {
		return new TclLocationGroup();
	}

	protected IProjectTemplateOperation getProjectTemplateOperation() {
		return ((TclLocationGroup) fLocationGroup)
				.getSelectedTemplateOperation();
	}

}
