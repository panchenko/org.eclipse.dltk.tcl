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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.TreeSelectionControl.ICollector;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUILanguageManager;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.dltk.ui.util.SWTFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class InstrumentationPatternList {

	private final IProject parentProject;
	private Composite fMode;
	private Button fDefaultMode;
	private Button fSourceModulesMode;
	private Button fSelectionMode;
	private CheckboxTreeViewer fViewer;
	private TreeSelectionControl fSelectionControl;

	/**
	 * @param parent
	 * @param shell
	 * @param string
	 */
	public InstrumentationPatternList(IProject parentProject) {
		this.parentProject = parentProject;
	}

	public Control createControl(final Composite parent) {
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout topLayout = new GridLayout();
		topLayout.numColumns = 1;
		topLayout.marginHeight = 0;
		topLayout.marginWidth = 0;
		comp.setLayout(topLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);

		fMode = new Composite(comp, SWT.NONE);
		fMode.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		fMode.setLayout(new FillLayout());
		fDefaultMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeDefault);
		fSourceModulesMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeSources);
		fSelectionMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeSelection);
		SelectionListener modeListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isUpdate()) {
					boolean newEnabled = fSelectionMode.getSelection();
					if (fViewer.getControl().isEnabled() != newEnabled) {
						fViewer.getControl().setEnabled(newEnabled);
					}
				}
			}
		};
		fDefaultMode.addSelectionListener(modeListener);
		fSourceModulesMode.addSelectionListener(modeListener);
		fSelectionMode.addSelectionListener(modeListener);

		fViewer = new CheckboxTreeViewer(comp, SWT.BORDER);
		final GridData viewerLayoutData = new GridData(GridData.FILL_BOTH);
		viewerLayoutData.heightHint = new PixelConverter(parent)
				.convertHeightInCharsToPixels(15);
		fViewer.getTree().setLayoutData(viewerLayoutData);
		// fViewer.setContentProvider(new SelectionDialogTreeContentProvider());
		fViewer.setContentProvider(new ContentProvider());
		fViewer.setLabelProvider(DLTKUILanguageManager
				.createLabelProvider(TclNature.NATURE_ID));
		fViewer.setComparator(new SelectionDialogComparator());
		fSelectionControl = new TreeSelectionControl(fViewer) {
			@Override
			protected String getLabelOf(Object element) {
				if (element instanceof IModelElement) {
					return ((IModelElement) element).getPath().toString();
				}
				return super.getLabelOf(element);
			}
		};
		fSelectionControl.install();
		return comp;
	}

	private int updateCount = 0;

	private boolean isUpdate() {
		return updateCount != 0;
	}

	private void beginUpdate() {
		++updateCount;
	}

	private void endUpdate() {
		--updateCount;
	}

	private InstrumentationConfig configValue;

	/**
	 * @param value
	 */
	public void setValue(InstrumentationConfig config) {
		beginUpdate();
		try {
			this.configValue = config;
			final InstrumentationMode mode = config != null ? config.getMode()
					: InstrumentationMode.DEFAULT;
			fSelectionControl
					.setInput(parentProject == null ? new WorkspaceSelectionDialogInput()
							: new ProjectSelectionDialogInput(parentProject));
			if (InstrumentationMode.SOURCES.equals(mode)) {
				fSourceModulesMode.setSelection(true);
				fViewer.getControl().setEnabled(false);
				fSelectionControl.resetState();
			} else if (InstrumentationMode.SELECTION.equals(mode)) {
				fSelectionMode.setSelection(true);
				fViewer.getControl().setEnabled(true);
				final List<IModelElement> includes = new ArrayList<IModelElement>();
				final List<IModelElement> excludes = new ArrayList<IModelElement>();
				for (ModelElementPattern pattern : config.getModelElements()) {
					final List<IModelElement> output = pattern.isInclude() ? includes
							: excludes;
					final IModelElement element = DLTKCore.create(pattern
							.getHandleIdentifier());
					if (element != null) {
						output.add(element);
					}
				}
				if (TreeSelectionControl.DEBUG) {
					for (IModelElement element : includes) {
						System.out.println("+" + element.getPath()); //$NON-NLS-1$
					}
					for (IModelElement element : excludes) {
						System.out.println("-" + element.getPath()); //$NON-NLS-1$
					}
				}
				fSelectionControl.setInitialState(includes, excludes);
			} else {
				fDefaultMode.setSelection(true);
				fViewer.getControl().setEnabled(false);
				fSelectionControl.resetState();
			}
			fSelectionControl.aboutToOpen();
		} finally {
			endUpdate();
		}
	}

	/**
	 * @return
	 */
	public InstrumentationConfig getValue() {
		if (TreeSelectionControl.DEBUG) {
			fSelectionControl.dump("onSave"); //$NON-NLS-1$
		}
		if (configValue == null) {
			configValue = PreferencesFactory.eINSTANCE
					.createInstrumentationConfig();
		}
		if (fSelectionMode.getSelection()) {
			configValue.setMode(InstrumentationMode.SELECTION);
			final Set<String> includes = new HashSet<String>();
			final Set<String> excludes = new HashSet<String>();
			fSelectionControl.collectCheckedItems(new ICollector() {
				public void include(Object object) {
					if (object instanceof IModelElement) {
						includes.add(((IModelElement) object)
								.getHandleIdentifier());
					}
				}

				public void exclude(Object object) {
					if (object instanceof IModelElement) {
						excludes.add(((IModelElement) object)
								.getHandleIdentifier());
					}
				}
			});
			final List<ModelElementPattern> toRemove = new ArrayList<ModelElementPattern>();
			for (ModelElementPattern pattern : configValue.getModelElements()) {
				final Set<String> input = pattern.isInclude() ? includes
						: excludes;
				if (!input.remove(pattern.getHandleIdentifier())) {
					toRemove.add(pattern);
				}
			}
			for (ModelElementPattern pattern : toRemove) {
				configValue.getModelElements().remove(pattern);
			}
			for (String include : includes) {
				final ModelElementPattern pattern = PreferencesFactory.eINSTANCE
						.createModelElementPattern();
				pattern.setInclude(true);
				pattern.setHandleIdentifier(include);
				configValue.getModelElements().add(pattern);
			}
			for (String exclude : excludes) {
				final ModelElementPattern pattern = PreferencesFactory.eINSTANCE
						.createModelElementPattern();
				pattern.setInclude(false);
				pattern.setHandleIdentifier(exclude);
				configValue.getModelElements().add(pattern);
			}
		} else if (fSourceModulesMode.getSelection()) {
			configValue.setMode(InstrumentationMode.SOURCES);
			configValue.getModelElements().clear();
		} else {
			configValue.setMode(InstrumentationMode.DEFAULT);
			configValue.getModelElements().clear();
		}
		return configValue;
	}

}
