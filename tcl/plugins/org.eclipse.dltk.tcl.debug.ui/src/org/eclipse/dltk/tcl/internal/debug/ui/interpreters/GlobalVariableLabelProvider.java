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
package org.eclipse.dltk.tcl.internal.debug.ui.interpreters;

import org.eclipse.dltk.ui.DLTKPluginImages;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class GlobalVariableLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		return DLTKPluginImages.get(DLTKPluginImages.IMG_FIELD_PUBLIC);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof GlobalVariableEntry) {
			GlobalVariableEntry var = (GlobalVariableEntry) element;
			return var.getName() + "=" + var.getValue(); //$NON-NLS-1$
		}
		return null;
	}

}
