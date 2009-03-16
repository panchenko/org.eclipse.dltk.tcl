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

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.jface.viewers.ViewerComparator;

class SelectionDialogComparator extends ViewerComparator {

	private static final int PROJECT_CATEGORY = 1;
	private static final int FRAGMENT_CATEGORY = 2;
	private static final int FOLDER_CATEGORY = 3;
	private static final int MODEL_ELEMENT_CATEGORY = 4;

	@Override
	public int category(Object element) {
		if (element instanceof IScriptProject) {
			return PROJECT_CATEGORY;
		} else if (element instanceof IProjectFragment) {
			return FRAGMENT_CATEGORY;
		} else if (element instanceof IScriptFolder) {
			return FOLDER_CATEGORY;
		} else if (element instanceof IModelElement) {
			return MODEL_ELEMENT_CATEGORY;
		} else {
			return 0;
		}
	}

}