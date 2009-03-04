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

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class InstrumentationPatternComparator extends ViewerComparator {

	@Override
	public int category(Object element) {
		if (element instanceof GlobPattern) {
			return 1;
		} else if (element instanceof WorkspacePattern) {
			return 2;
		} else if (element instanceof ExternalPattern) {
			return 3;
		} else {
			return 0;
		}
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int category1 = category(e1);
		int category2 = category(e2);
		if (category1 != category2) {
			return category1 - category2;
		}
		if (e1 instanceof Pattern && e2 instanceof Pattern) {
			String path1 = ((Pattern) e1).getPath();
			String path2 = ((Pattern) e2).getPath();
			if (path1 != null) {
				return path1.compareTo(path2);
			}
		}
		return super.compare(viewer, e1, e2);
	}

}
