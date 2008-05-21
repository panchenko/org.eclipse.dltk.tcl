/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.core;

import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.DLTKContentTypeManager;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;

public class TclLanguageToolkit extends AbstractLanguageToolkit {

	private boolean acceptDir(IFileHandle f, IEnvironment environment) {
		if (f.isDirectory()) {
			IFileHandle[] listFiles = f.getChildren();
			if (listFiles != null) {
				for (int i = 0; i < listFiles.length; i++) {
					if (listFiles[i].isFile()
							&& DLTKContentTypeManager
									.isValidFileNameForContentType(
											TclLanguageToolkit.getDefault(),
											listFiles[i].getName())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static final String TCL_CONTENT_TYPE = "org.eclipse.dltk.tclContentType";
	protected static IDLTKLanguageToolkit sInstance = new TclLanguageToolkit();

	public TclLanguageToolkit() {
	}

	public boolean validateSourcePackage(IPath path, IEnvironment environment) {
		IFileHandle file = environment.getFile(path);
		if (file.isDirectory()) {
			IFileHandle members[] = file.getChildren();
			for (int i = 0; i < members.length; i++) {
				String name = members[i].getName();
				if (DLTKContentTypeManager.isValidFileNameForContentType(this,
						members[i].getPath())) {
					return true;
				}
				if (name.toLowerCase().equals("pkgindex.tcl")
						|| name.toLowerCase().equals("tclindex")) {
					return true;
				}
				if (acceptDir(members[i], environment)) {
					return true;
				}
			}
		}
		return false;
	}

	public String getNatureId() {
		return TclNature.NATURE_ID;
	}

	public static IDLTKLanguageToolkit getDefault() {
		return sInstance;
	}

	public String getLanguageName() {
		return "Tcl";
	}

	public String getLanguageContentType() {
		return TCL_CONTENT_TYPE;
	}

}
