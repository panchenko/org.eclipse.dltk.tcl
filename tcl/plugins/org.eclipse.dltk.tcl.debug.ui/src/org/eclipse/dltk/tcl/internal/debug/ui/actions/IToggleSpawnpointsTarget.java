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
package org.eclipse.dltk.tcl.internal.debug.ui.actions;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.ui.IWorkbenchPart;

public interface IToggleSpawnpointsTarget {

	/**
	 * @param part
	 * @param selection
	 * @return
	 */
	boolean canToggleSpawnpoints(IWorkbenchPart part, ITextSelection selection);

	/**
	 * @param part
	 * @param selection
	 */
	void toggleSpawnpoints(IWorkbenchPart part, ITextSelection selection)
			throws CoreException;

}
