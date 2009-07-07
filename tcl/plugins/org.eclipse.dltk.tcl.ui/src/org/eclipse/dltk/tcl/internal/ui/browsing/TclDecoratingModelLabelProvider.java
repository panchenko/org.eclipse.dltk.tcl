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
package org.eclipse.dltk.tcl.internal.ui.browsing;

import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageFragment;
import org.eclipse.dltk.ui.viewsupport.DecoratingModelLabelProvider;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;

public class TclDecoratingModelLabelProvider extends
		DecoratingModelLabelProvider {

	/**
	 * @param labelProvider
	 */
	public TclDecoratingModelLabelProvider(ScriptUILabelProvider labelProvider) {
		super(labelProvider);
	}

	@Override
	public String getText(Object element) {
		if (element instanceof TclPackageFragment) {
			final TclPackageFragment fragment = (TclPackageFragment) element;
			try {
				final IModelElement[] children = fragment.getChildren();
				if (children.length == 1) {
					return super.getText(children[0]);
				}
			} catch (ModelException e) {
				// ignore
			}
			return fragment.getPackageName();
		}
		return super.getText(element);
	}
}
