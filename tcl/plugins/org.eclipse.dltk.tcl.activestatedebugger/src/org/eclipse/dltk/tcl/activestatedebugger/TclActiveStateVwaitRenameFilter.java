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
package org.eclipse.dltk.tcl.activestatedebugger;

import java.util.regex.Pattern;

import org.eclipse.dltk.dbgp.IDbgpStreamFilter;

public class TclActiveStateVwaitRenameFilter implements IDbgpStreamFilter {

	private Pattern pattern;

	public TclActiveStateVwaitRenameFilter() {
		try {
			pattern = Pattern
					.compile("Warning \\(issued by the debugger backend\\)\\.\nRenaming\\s+\"::vwait\" may crash the debugger\\.\n"); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			pattern = null;
		}
	}

	public String filter(String value, int stream) {
		if (pattern != null && pattern.matcher(value).matches()) {
			return null;
		} else {
			return value;
		}
	}

}
