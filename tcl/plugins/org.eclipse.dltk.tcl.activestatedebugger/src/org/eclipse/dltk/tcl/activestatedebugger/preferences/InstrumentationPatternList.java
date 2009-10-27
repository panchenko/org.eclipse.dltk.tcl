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
import org.eclipse.dltk.tcl.activestatedebugger.InstrumentationUtils;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.TreeSelectionControl.ICollector;
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
		fSourceModulesMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeSources);
		fSelectionMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeSelection);
		fDefaultMode = SWTFactory.createRadioButtonNoLayoutData(fMode,
				PreferenceMessages.InstrumentationPatternList_ModeDefault);
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
		InstrumentationContentProvider cp = new InstrumentationContentProvider();
		fViewer.setContentProvider(cp);
		fViewer.setLabelProvider(new InstrumentationLabelProvider(cp));
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
			final InstrumentationMode mode = InstrumentationUtils
					.getMode(config);
			final SelectionDialogInput treeInput = parentProject == null ? new WorkspaceSelectionDialogInput()
					: new ProjectSelectionDialogInput(parentProject);
			fSelectionControl.setInput(treeInput);
			if (InstrumentationMode.SOURCES.equals(mode)) {
				fSourceModulesMode.setSelection(true);
				fViewer.getControl().setEnabled(false);
				fSelectionControl.resetState();
			} else if (InstrumentationMode.SELECTION.equals(mode)) {
				fSelectionMode.setSelection(true);
				fViewer.getControl().setEnabled(true);
				final List<Object> includes = new ArrayList<Object>();
				final List<Object> excludes = new ArrayList<Object>();
				for (Pattern pattern : config.getModelElements()) {
					final List<Object> output = pattern.isInclude() ? includes
							: excludes;
					final Object element = convert(pattern, treeInput);
					if (element != null) {
						output.add(element);
					}
				}
				if (TreeSelectionControl.DEBUG) {
					for (Object element : includes) {
						System.out.println("+" + element); //$NON-NLS-1$
					}
					for (Object element : excludes) {
						System.out.println("-" + element); //$NON-NLS-1$
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
	 * @since 2.0
	 */
	protected static Object convert(Pattern pattern,
			final SelectionDialogInput treeInput) {
		if (pattern instanceof ModelElementPattern) {
			return DLTKCore.create(((ModelElementPattern) pattern)
					.getHandleIdentifier());
		} else if (pattern instanceof ContainerPattern) {
			final ContainerType containerType = ((ContainerPattern) pattern)
					.getType();
			if (containerType == ContainerType.PACKAGES) {
				return new PackageContainerElement(treeInput);
			} else if (containerType == ContainerType.SOURCES) {
				return new SourceContainerElement(treeInput);
			} else {
				return new LibraryContainerElement(treeInput);
			}
		} else if (pattern instanceof PackagePattern) {
			return new PackageElement(((PackagePattern) pattern)
					.getPackageName());
		} else if (pattern instanceof SourcePattern) {
			return new SourceElement(((SourcePattern) pattern).getSourcePath());
		} else {
			return null;
		}
	}

	/**
	 * @since 2.0
	 */
	protected static Pattern unconvert(Object object) {
		if (object instanceof IModelElement) {
			final ModelElementPattern pattern = PreferencesFactory.eINSTANCE
					.createModelElementPattern();
			pattern.setHandleIdentifier(((IModelElement) object)
					.getHandleIdentifier());
			return pattern;
		} else if (object instanceof PackageElement) {
			final PackagePattern pattern = PreferencesFactory.eINSTANCE
					.createPackagePattern();
			pattern.setPackageName(((PackageElement) object).packageName);
			return pattern;
		} else if (object instanceof SourceElement) {
			final SourcePattern pattern = PreferencesFactory.eINSTANCE
					.createSourcePattern();
			pattern.setSourcePath(((SourceElement) object).path.toString());
			return pattern;
		} else if (object instanceof WorkbenchAdaptable) {
			final ContainerPattern pattern = PreferencesFactory.eINSTANCE
					.createContainerPattern();
			pattern.setType(((WorkbenchAdaptable) object).getContainerType());
			return pattern;
		} else {
			return null;
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
			final Set<Object> includes = new HashSet<Object>();
			final Set<Object> excludes = new HashSet<Object>();
			fSelectionControl.collectCheckedItems(new ICollector() {

				public void include(Object object) {
					includes.add(object);
				}

				public void exclude(Object object) {
					excludes.add(object);
				}
			});
			final SelectionDialogInput treeInput = new ProjectSelectionDialogInput(
					parentProject);
			final List<Pattern> toRemove = new ArrayList<Pattern>();
			for (Pattern pattern : configValue.getModelElements()) {
				final Set<Object> input = pattern.isInclude() ? includes
						: excludes;
				final Object oldItem = convert(pattern, treeInput);
				if (!input.remove(oldItem)) {
					toRemove.add(pattern);
				}
			}
			for (Pattern pattern : toRemove) {
				configValue.getModelElements().remove(pattern);
			}
			for (Object include : includes) {
				final Pattern pattern = unconvert(include);
				if (pattern != null) {
					pattern.setInclude(true);
					configValue.getModelElements().add(pattern);
				}
			}
			for (Object exclude : excludes) {
				final Pattern pattern = unconvert(exclude);
				if (pattern != null) {
					pattern.setInclude(false);
					configValue.getModelElements().add(pattern);
				}
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
