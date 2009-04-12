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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.ui.environment.EnvironmentPathBlock;
import org.eclipse.dltk.ui.util.PixelConverter;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class TclLoggingPathBlock extends EnvironmentPathBlock {

	private Map<String, Boolean> enableLogging = new HashMap<String, Boolean>();

	public void setEnableLogging(Map<String, Boolean> values) {
		this.enableLogging.clear();
		this.enableLogging.putAll(values);
		getViewer().refresh();
	}

	public Map<String, Boolean> getEnableLogging() {
		return Collections.unmodifiableMap(enableLogging);
	}

	private class TclLoggingPathLabelProvider extends PathLabelProvider {
		public TclLoggingPathLabelProvider() {
			super(2);
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (columnIndex == 1) {
				final Boolean value = enableLogging
						.get(((IEnvironment) element).getId());
				return value == null || value.booleanValue() ? PreferenceMessages.TclLoggingPathBlock_Yes
						: PreferenceMessages.TclLoggingPathBlock_No;
			} else {
				return super.getColumnText(element, columnIndex);
			}
		}
	}

	private class EnableEditingSupport extends EditingSupport {

		private final ComboBoxCellEditor editor;

		/**
		 * @param viewer
		 */
		private EnableEditingSupport(ColumnViewer viewer) {
			super(viewer);
			editor = new ComboBoxCellEditor((Composite) viewer.getControl(),
					new String[] { PreferenceMessages.TclLoggingPathBlock_No,
							PreferenceMessages.TclLoggingPathBlock_Yes },
					SWT.READ_ONLY);
		}

		protected boolean canEdit(Object element) {
			return true;
		}

		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		protected Object getValue(Object element) {
			final Boolean value = enableLogging.get(((IEnvironment) element)
					.getId());
			return value == null || value.booleanValue() ? 1 : 0;
		}

		protected void setValue(Object element, Object value) {
			enableLogging.put(((IEnvironment) element).getId(), Boolean
					.valueOf(((Integer) value).intValue() != 0));
			getViewer().refresh();
			fireValueChanged();
		}
	}

	/*
	 * @see EnvironmentPathBlock#createPathLabelProvider()
	 */
	@Override
	protected PathLabelProvider createPathLabelProvider() {
		return new TclLoggingPathLabelProvider();
	}

	@Override
	protected void initColumns(TableViewer viewer, PixelConverter conv) {
		initEnvironmentColumn(viewer, conv);
		initEnableLoggingColumn(viewer, conv);
		initPathColumn(viewer, conv);
	}

	protected void initEnableLoggingColumn(final TableViewer viewer,
			PixelConverter conv) {
		TableViewerColumn enableColumn = new TableViewerColumn(viewer, SWT.NULL);
		enableColumn.getColumn().setText(
				PreferenceMessages.TclLoggingPathBlock_enable);
		enableColumn.getColumn().setWidth(conv.convertWidthInCharsToPixels(12));
		enableColumn.setEditingSupport(new EnableEditingSupport(viewer));
	}

}
