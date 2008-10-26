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
package org.eclipse.dltk.tcl.internal.tclchecker.qfix;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;

public class TclCheckerMarkerResolution implements IMarkerResolution {

	private final int index;
	private final String replacement;

	/**
	 * @param i
	 * @param string
	 */
	public TclCheckerMarkerResolution(int index, String replacement) {
		this.index = index;
		this.replacement = replacement;
	}

	public String getLabel() {
		return replacement;
	}

	public void run(IMarker marker) {
		// TODO Auto-generated method stub
System.out.println("NOP"); //$NON-NLS-1$
	}

}
