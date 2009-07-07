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

import org.eclipse.dltk.ui.browsing.ProjectsView;
import org.eclipse.dltk.ui.viewsupport.DecoratingModelLabelProvider;
import org.eclipse.dltk.ui.viewsupport.ScriptUILabelProvider;
import org.eclipse.jface.viewers.IContentProvider;

public class TclProjectsView extends ProjectsView {

	@Override
	protected DecoratingModelLabelProvider createDecoratingLabelProvider(
			ScriptUILabelProvider provider) {
		return new TclDecoratingModelLabelProvider(provider);
	}

	@Override
	protected IContentProvider createContentProvider() {
		return new TclProjectAndSourceFolderContentProvider(this, getToolkit());
	}

}
