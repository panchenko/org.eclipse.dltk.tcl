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

import org.eclipse.core.runtime.IStatus;
import org.eclipse.dltk.ui.dialogs.StatusInfo;
import org.eclipse.dltk.ui.ModelElementLabelProvider;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.SelectionStatusDialog;

public class SelectionDialog extends SelectionStatusDialog implements
		ISelectionChangedListener {

	private CheckboxTreeViewer fViewer;

	private List<ViewerFilter> fFilters;

	private Object fInput;

	private Set<Object> fExisting;
	private Object fFocusElement;

	public SelectionDialog(Shell parent) {
		super(parent);

		setSelectionResult(null);
		setStatusLineAboveButtons(true);

		int shellStyle = getShellStyle();
		setShellStyle(shellStyle | SWT.MAX | SWT.RESIZE);

		fExisting = null;
		fFocusElement = null;
		fFilters = null;
	}

	public void setExisting(Object[] existing) {
		fExisting = new HashSet<Object>();
		for (int i = 0; i < existing.length; i++) {
			fExisting.add(existing[i]);
		}
	}

	/**
	 * Sets the tree input.
	 * 
	 * @param input
	 *            the tree input.
	 */
	public void setInput(Object input) {
		fInput = input;
	}

	/**
	 * Adds a filter to the tree viewer.
	 * 
	 * @param filter
	 *            a filter.
	 */
	public void addFilter(ViewerFilter filter) {
		if (fFilters == null) {
			fFilters = new ArrayList<ViewerFilter>(4);
		}
		fFilters.add(filter);
	}

	/**
	 * Handles cancel button pressed event.
	 */
	protected void cancelPressed() {
		setSelectionResult(null);
		super.cancelPressed();
	}

	/*
	 * @see SelectionStatusDialog#computeResult()
	 */
	protected void computeResult() {
		Object[] checked = fViewer.getCheckedElements();
		if (fExisting == null) {
			if (checked.length == 0) {
				checked = null;
			}
		} else {
			List<Object> result = new ArrayList<Object>();
			for (int i = 0; i < checked.length; i++) {
				Object elem = checked[i];
				if (!fExisting.contains(elem)) {
					result.add(elem);
				}
			}
			if (!result.isEmpty()) {
				checked = result.toArray();
			} else {
				checked = null;
			}
		}
		setSelectionResult(checked);
	}

	private void access$superCreate() {
		super.create();
	}

	/*
	 * @see Window#create()
	 */
	public void create() {

		BusyIndicator.showWhile(null, new Runnable() {
			public void run() {
				access$superCreate();

				fViewer.setCheckedElements(getInitialElementSelections()
						.toArray());

				fViewer.expandToLevel(2);
				if (fExisting != null) {
					for (Object item : fExisting) {
						fViewer.reveal(item);
					}
				}

				updateOKStatus();
			}
		});

	}

	/**
	 * Creates the tree viewer.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the tree viewer
	 */
	protected CheckboxTreeViewer createTreeViewer(Composite parent) {
		fViewer = new CheckboxTreeViewer(parent, SWT.BORDER);

		fViewer.setContentProvider(new InstrumentationContentProvider());
		fViewer.setLabelProvider(new ModelElementLabelProvider());
		fViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				updateOKStatus();
			}
		});

		fViewer.setComparator(new SelectionDialogComparator());
		if (fFilters != null) {
			for (ViewerFilter filter : fFilters)
				fViewer.addFilter(filter);
		}

		fViewer.setInput(fInput);

		return fViewer;
	}

	/**
	 * 
	 */
	protected void updateOKStatus() {
		computeResult();
		if (getResult() != null) {
			updateStatus(new StatusInfo());
		} else {
			updateStatus(new StatusInfo(IStatus.ERROR, "")); //$NON-NLS-1$
		}
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);

		createMessageArea(composite);
		CheckboxTreeViewer treeViewer = createTreeViewer(composite);

		GridData data = new GridData(GridData.FILL_BOTH);
		data.widthHint = convertWidthInCharsToPixels(60);
		data.heightHint = convertHeightInCharsToPixels(18);

		Tree treeWidget = treeViewer.getTree();
		treeWidget.setLayoutData(data);
		treeWidget.setFont(composite.getFont());

		treeViewer.addSelectionChangedListener(this);
		if (fExisting != null) {
			Object[] existing = fExisting.toArray();
			treeViewer.setGrayedElements(existing);
			setInitialSelections(existing);
		}
		if (fFocusElement != null) {
			treeViewer.setSelection(new StructuredSelection(fFocusElement),
					true);
		}
		treeViewer.addCheckStateListener(new ICheckStateListener() {
			public void checkStateChanged(CheckStateChangedEvent event) {
				forceExistingChecked(event);
			}
		});

		applyDialogFont(composite);
		return composite;
	}

	protected void forceExistingChecked(CheckStateChangedEvent event) {
		if (fExisting != null) {
			Object elem = event.getElement();
			if (fExisting.contains(elem)) {
				fViewer.setChecked(elem, true);
			}
		}
	}

	public void selectionChanged(SelectionChangedEvent event) {

	}

	public void setInitialFocus(Object focusElement) {
		fFocusElement = focusElement;
	}

}
