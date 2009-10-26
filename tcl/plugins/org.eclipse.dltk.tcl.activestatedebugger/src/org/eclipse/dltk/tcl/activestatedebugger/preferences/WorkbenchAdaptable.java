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

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.model.IWorkbenchAdapter;

abstract class WorkbenchAdaptable implements IAdaptable, IWorkbenchAdapter {

	protected final SelectionDialogInput input;

	/**
	 * @param input
	 */
	public WorkbenchAdaptable(SelectionDialogInput input) {
		this.input = input;
	}

	@Override
	public int hashCode() {
		return input.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && getClass() == obj.getClass()) {
			return input.equals(((WorkbenchAdaptable) obj).input);
		} else {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		if (adapter == IWorkbenchAdapter.class)
			return this;
		return null;
	}

	public abstract ContainerType getContainerType();

	public abstract Object[] getChildren();

	public final Object[] getChildren(Object o) {
		return getChildren();
	}

	public Object getParent(Object o) {
		return null;
	}

}
