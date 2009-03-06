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
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.TreeSelectionControl.ICollector;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.ui.DLTKUILanguageManager;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class InstrumentationPatternList {

	private final IProject parentProject;
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
		topLayout.numColumns = 2;
		topLayout.marginHeight = 0;
		topLayout.marginWidth = 0;
		comp.setLayout(topLayout);
		GridData gd = new GridData(GridData.FILL_BOTH);
		comp.setLayoutData(gd);

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
		fSelectionControl = new TreeSelectionControl(fViewer);
		fSelectionControl.install();
		fSelectionControl
				.setInput(parentProject == null ? new WorkspaceSelectionDialogInput()
						: new ProjectSelectionDialogInput(parentProject));
		fSelectionControl.setInitialState(includes, excludes);
		fSelectionControl.aboutToOpen();
		return comp;
	}

	private static final ArrayList<Object> includes = new ArrayList<Object>();
	private static final ArrayList<Object> excludes = new ArrayList<Object>();

	/**
	 * @param value
	 */
	public void setValue(List<Pattern> model) {
		// fList.setInput(model);
		// for (Pattern pattern : model) {
		// fList.setChecked(pattern, pattern.isInclude());
		// }
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Pattern> getValue() {
		if (DLTKCore.DEBUG) {
			fSelectionControl.dump();
		}
		includes.clear();
		excludes.clear();
		ICollector collector = new ICollector() {
			public void include(Object object) {
				includes.add(object);
			}

			public void exclude(Object arg0) {
				excludes.add(arg0);
			}
		};
		fSelectionControl.collectCheckedItems(collector);
		return Collections.emptyList();
		// return (List<Pattern>) fList.getInput();
	}

}
