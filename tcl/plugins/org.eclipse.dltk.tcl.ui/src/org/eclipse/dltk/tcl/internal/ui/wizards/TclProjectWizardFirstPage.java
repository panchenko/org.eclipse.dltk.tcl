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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
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
import org.eclipse.dltk.ui.wizards.IProjectWizardExtension;
import org.eclipse.dltk.ui.wizards.IProjectWizardExtensionContext;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.IProjectWizardExtension.IValidationRequestor;
import org.eclipse.dltk.utils.LazyExtensionManager;
import org.eclipse.dltk.utils.LazyExtensionManager.Descriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

final class TclProjectWizardFirstPage extends ProjectWizardFirstPage implements
		IValidationRequestor {

	public TclProjectWizardFirstPage() {
		setTitle(TclWizardMessages.ProjectCreationWizardFirstPage_title);
		setDescription(TclWizardMessages.ProjectCreationWizardFirstPage_description);
	}

	@Override
	public String getScriptNature() {
		return TclNature.NATURE_ID;
	}

	@Override
	protected boolean interpeterRequired() {
		/* Specially allow to create TCL project without interpreter */
		return false;
	}

	static class ProjectTemplateDescriptor extends Descriptor<IProjectTemplate> {

		private static final String ATTR_NATURE = "nature"; //$NON-NLS-1$
		private static final String ATTR_NAME = "name"; //$NON-NLS-1$
		private static final String ATTR_BROWSE_BUTTON = "browseButton"; //$NON-NLS-1$

		final String nature;
		final String name;
		final String browseButton;

		/**
		 * @param configurationElement
		 */
		public ProjectTemplateDescriptor(TclProjectTemplateManager manager,
				IConfigurationElement configurationElement) {
			super(manager, configurationElement);
			this.nature = configurationElement.getAttribute(ATTR_NATURE);
			this.name = configurationElement.getAttribute(ATTR_NAME);
			this.browseButton = configurationElement
					.getAttribute(ATTR_BROWSE_BUTTON);
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

	static class ProjectWizardExtensionDescriptor extends
			Descriptor<IProjectWizardExtension> {

		private static final String ATTR_NATURE = "nature"; //$NON-NLS-1$

		final String nature;

		public ProjectWizardExtensionDescriptor(
				TclProjectWizardExtensionManager manager,
				IConfigurationElement configurationElement) {
			super(manager, configurationElement);
			this.nature = configurationElement.getAttribute(ATTR_NATURE);
		}

	}

	static class TclProjectWizardExtensionManager extends
			LazyExtensionManager<IProjectWizardExtension> {

		/**
		 * @param extensionPoint
		 */
		public TclProjectWizardExtensionManager() {
			super(DLTKUIPlugin.PLUGIN_ID + ".projectWizardExtension"); //$NON-NLS-1$
		}

		@Override
		protected boolean isValidDescriptor(
				Descriptor<IProjectWizardExtension> descriptor) {
			String natureId = ((ProjectWizardExtensionDescriptor) descriptor).nature;
			return natureId == null || TclNature.NATURE_ID.equals(natureId);
		}

		@Override
		protected Descriptor<IProjectWizardExtension> createDescriptor(
				IConfigurationElement confElement) {
			return new ProjectWizardExtensionDescriptor(this, confElement);
		}

	}

	private class TclProjectWizardExtensionContext implements
			IProjectWizardExtensionContext {

		private final Composite composite;

		public TclProjectWizardExtensionContext(Composite composite) {
			this.composite = composite;
		}

		public Composite getControl() {
			return composite;
		}

		public GridLayout initGridLayout(GridLayout layout, boolean margins) {
			return TclProjectWizardFirstPage.this.initGridLayout(layout,
					margins);
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
			public void createControls(Composite group) {
				fLinkRadio.doFillIntoGrid(group, 2);
				fBrowseButton = new Button(group, SWT.PUSH);
				fBrowseButton.setFont(group.getFont());
				fBrowseButton.setText(this.descriptor.browseButton);
				fBrowseButton.setEnabled(fLinkRadio.isSelected());
				fBrowseButton.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						execute();
					}
				});
				fBrowseButton.setLayoutData(StringButtonDialogField
						.gridDataForButton(fBrowseButton, 1));
			}

			protected void execute() {
				final IProjectTemplate projectTemplate = descriptor.get();
				if (projectTemplate != null) {
					IProject project = acquireProject();
					try {

						projectTemplate.setCurrentEnvironment(getEnvironment());
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

			protected boolean isSelected() {
				return fLinkRadio.isSelected();
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
		protected void createModeControls(Composite group, int numColumns) {
			super.createModeControls(group, numColumns);
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
							&& entry.isSelected());
					break;
				}
			}
		}

		@Override
		public boolean isInWorkspace() {
			if (super.isInWorkspace()) {
				return true;
			}
			return getSelectedEntry() != null;
		}

		@Override
		public boolean canChangeEnvironment() {
			return getSelectedEntry() != null;
		}

		@Override
		public IStatus validate(IProject handle) {
			IStatus status = super.validate(handle);
			if (status.isOK()) {
				final TclProjectTemplateEntry entry = getSelectedEntry();
				if (entry != null) {
					final IStatus entryStatus = entry.validate(handle);
					if (!entryStatus.isOK()) {
						return new ControlStatus(entryStatus.getSeverity(),
								entryStatus.getMessage(), entry.fLinkRadio
										.getSelectionButton());
					}
				}
			}
			return status;
		}

		protected TclProjectTemplateEntry getSelectedEntry() {
			for (TclProjectTemplateEntry entry : fOptions) {
				if (entry.isSelected()) {
					return entry;
				}
			}
			return null;
		}

		/**
		 * @return
		 */
		protected IProjectTemplateOperation getSelectedTemplateOperation() {
			final TclProjectTemplateEntry entry = getSelectedEntry();
			return entry != null ? entry.templateOperation : null;
		}

		@Override
		public void update(Observable o, Object arg) {
			super.update(o, arg);
			if (o instanceof NameGroup) {
				final TclProjectTemplateEntry entry = getSelectedEntry();
				if (entry != null) {
					entry.fBrowseButton.setEnabled(isValidProject());
				}
			}
		}

	}

	@Override
	protected IStatus validateProject() {
		IStatus status = super.validateProject();
		if (status != null && !status.isOK()) {
			return status;
		}
		final TclLocationGroup.TclProjectTemplateEntry entry = ((TclLocationGroup) fLocationGroup)
				.getSelectedEntry();
		if (entry == null) {
			return status;
		}
		final IProjectTemplate template = entry.descriptor.get();
		if (template == null) {
			return status;
		}
		for (IProjectWizardExtension extension : wizardExtensions) {
			IProject project = null;
			if (fNameGroup.getName().length() != 0) {
				project = getProjectHandle();
			}
			IStatus extensionStatus = extension.validate(project,
					getEnvironment(), template);
			if (!extensionStatus.isOK()) {
				return extensionStatus;
			}
		}
		return status;
	}

	@Override
	protected LocationGroup createLocationGroup() {
		return new TclLocationGroup();
	}

	public void postConfigure(IProgressMonitor monitor, final IProject project)
			throws InterruptedException, CoreException {
		IProjectTemplateOperation templateOperation = ((TclProjectCreationWizard) getWizard()).fFirstPage
				.getProjectTemplateOperation();
		if (templateOperation != null) {
			final IStatus status = templateOperation.execute(project,
					getShell(), monitor);
			if (!status.isOK()) {
				if (status.getException() instanceof InterruptedException) {
					throw (InterruptedException) status.getException();
				}
				throw new CoreException(status);
			}
		}
		for (IProjectWizardExtension extension : wizardExtensions) {
			extension.postConfigure(project, monitor);
		}
	}

	protected IProjectTemplateOperation getProjectTemplateOperation() {
		return ((TclLocationGroup) fLocationGroup)
				.getSelectedTemplateOperation();
	}

	private List<IProjectWizardExtension> wizardExtensions = new ArrayList<IProjectWizardExtension>();

	@Override
	protected void createCustomGroups(Composite composite) {
		super.createCustomGroups(composite);
		final TclProjectWizardExtensionContext context = new TclProjectWizardExtensionContext(
				composite);
		for (Iterator<IProjectWizardExtension> i = new TclProjectWizardExtensionManager()
				.iterator(); i.hasNext();) {
			final IProjectWizardExtension extension = i.next();
			wizardExtensions.add(extension);
			extension.createControls(context);
			extension.setValidationRequestor(this);
		}
	}

	public void validate() {
		fNameGroup.dialogFieldChanged(null);
	}
}
