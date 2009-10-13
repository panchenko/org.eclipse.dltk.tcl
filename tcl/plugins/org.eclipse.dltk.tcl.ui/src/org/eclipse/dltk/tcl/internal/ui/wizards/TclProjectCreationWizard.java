/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.wizards;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.internal.ui.TclImages;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.dialogs.IProjectTemplate;
import org.eclipse.dltk.ui.wizards.ProjectWizard;
import org.eclipse.dltk.utils.LazyExtensionManager.Descriptor;
import org.eclipse.jface.wizard.IWizardPage;

public class TclProjectCreationWizard extends ProjectWizard {

	public static final String ID_WIZARD = "org.eclipse.dltk.tcl.internal.ui.wizards.TclProjectWizard"; //$NON-NLS-1$

	protected TclProjectWizardFirstPage fFirstPage;
	protected TclProjectWizardSecondPage fSecondPage;

	public TclProjectCreationWizard() {
		setDefaultPageImageDescriptor(TclImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
		setWindowTitle(TclWizardMessages.ProjectCreationWizard_title);
	}

	private final List<TclProjectTemplateEntry> fOptions = new ArrayList<TclProjectTemplateEntry>();

	private boolean optionsLoaded = false;

	private void loadOptions() {
		final TclProjectTemplateManager manager = new TclProjectTemplateManager();
		for (Descriptor<IProjectTemplate> descriptor : manager.descriptors()) {
			final IProjectTemplate template = descriptor.get();
			if (template != null) {
				final String id = descriptor
						.getAttribute(TclProjectTemplateManager.ATTR_ID);
				final String name = descriptor
						.getAttribute(TclProjectTemplateManager.ATTR_NAME);
				final TclProjectTemplateEntry entry = new TclProjectTemplateEntry(
						id, name, template);
				fOptions.add(entry);
			}
		}
	}

	@Override
	public void addPages() {
		super.addPages();
		if (!optionsLoaded) {
			loadOptions();
			optionsLoaded = true;
		}
		fFirstPage = new TclProjectWizardFirstPage(fOptions);
		addPage(fFirstPage);
		for (TclProjectTemplateEntry entry : fOptions) {
			for (IWizardPage page : entry.getTemplate().getPages()) {
				addPage(page);
				optional.put(page, entry);
			}
		}
		fSecondPage = new TclProjectWizardSecondPage(fFirstPage);
		addPage(fSecondPage);
	}

	private final Map<IWizardPage, TclProjectTemplateEntry> optional = new IdentityHashMap<IWizardPage, TclProjectTemplateEntry>();

	/**
	 * @since 2.0
	 */
	@Override
	public boolean isEnabledPage(IWizardPage page) {
		final TclProjectTemplateEntry entry = optional.get(page);
		return entry == null || entry.isSelected();
	}

}
