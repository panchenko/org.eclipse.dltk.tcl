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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.dialogs.PropertyPage;

public class TclCheckerProblemPropertyPage extends PropertyPage {

	private static class ProblemRow {

		public ProblemRow(IMarker marker, String attribute, Object value) {
			this.marker = marker;
			this.attribute = attribute;
			this.value = value;
		}

		final IMarker marker;
		final String attribute;
		final Object value;
	}

	private static class ProblemContentProvider implements
			IStructuredContentProvider {

		private static final String[] ATTRIBUTES = {
				TclCheckerMarker.MESSAGE_ID, TclCheckerMarker.COMMAND_START,
				TclCheckerMarker.COMMAND_END,
				TclCheckerMarker.AUTO_CORRECTABLE,
				TclCheckerMarker.SUGGESTED_CORRECTIONS };

		public Object[] getElements(Object inputElement) {
			if (inputElement instanceof IMarker) {
				try {
					final IMarker marker = (IMarker) inputElement;
					final Map<?, ?> attributes = marker.getAttributes();
					final List<ProblemRow> result = new ArrayList<ProblemRow>();
					if (attributes != null) {
						for (int i = 0; i < ATTRIBUTES.length; ++i) {
							System.out.println(marker
									.getAttribute(ATTRIBUTES[i]));
							if (attributes.containsKey(ATTRIBUTES[i])) {
								result.add(new ProblemRow(marker,
										ATTRIBUTES[i], attributes
												.get(ATTRIBUTES[i])));
							}
						}
					}
					return result.toArray();
				} catch (CoreException e) {
					TclCheckerPlugin.error(e);
				}
			}
			return new Object[0];
		}

		/*
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {
			// empty
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// empty
		}

	}

	private static class ProblemLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ProblemRow) {
				final ProblemRow problem = (ProblemRow) element;
				if (columnIndex == 0) {
					return problem.attribute;
				} else if (columnIndex == 1) {
					return problem.value.toString();
				}
			}
			return Util.EMPTY_STRING;
		}

	}

	@Override
	protected Control createContents(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		TableViewer viewer = new TableViewer(content, SWT.BORDER
				| SWT.FULL_SELECTION);
		viewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		final TableColumn nameColumn = new TableColumn(viewer.getTable(),
				SWT.LEFT);
		nameColumn.setText("Name"); //$NON-NLS-1$
		final TableColumn valueColumn = new TableColumn(viewer.getTable(),
				SWT.LEFT);
		valueColumn.setText("Value"); //$NON-NLS-1$
		viewer.setContentProvider(new ProblemContentProvider());
		viewer.setLabelProvider(new ProblemLabelProvider());
		final TableColumnLayout tableLayout = new TableColumnLayout();
		tableLayout.setColumnData(nameColumn, new ColumnWeightData(50));
		tableLayout.setColumnData(valueColumn, new ColumnWeightData(50));
		content.setLayout(tableLayout);
		final IMarker marker = (IMarker) getElement().getAdapter(IMarker.class);
		if (marker != null) {
			viewer.setInput(marker);
		}
		return content;
	}

}
