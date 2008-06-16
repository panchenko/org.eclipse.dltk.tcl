/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.actions;

import org.eclipse.dltk.tcl.internal.ui.TclUILanguageToolkit;
import org.eclipse.dltk.ui.IDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.actions.OpenTypeAction;

public class TclOpenTypeAction extends OpenTypeAction {
	protected IDLTKUILanguageToolkit getUILanguageToolkit() {
		return TclUILanguageToolkit.getInstance();
	}

	protected String getOpenTypeErrorMessage() {
		return "An exception occurred while opening the namespace/class.";
	}

	protected String getOpenTypeErrorTitle() {
		return "Open Namespace/Class";
	}

	protected String getOpenTypeDialogMessage() {
		return "&Select a namespace/class to open (? = any character, * = any String, TZ = TimeZone):";
	}

	protected String getOpenTypeDialogTitle() {
		return "Open Namespace/Class";
	}
}
