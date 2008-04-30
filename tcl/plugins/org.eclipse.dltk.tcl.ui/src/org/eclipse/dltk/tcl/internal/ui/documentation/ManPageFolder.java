/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *     xored software, Inc. - TCL ManPageFolder management refactoring (Alex Panchenko <alex@xored.com>)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.documentation;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ManPageFolder {
	private String path;
	private final Map pages = new HashMap();

	public ManPageFolder(String path) {
		this.path = path;
	}

	public void addPage(String keyword, String file) {
		pages.put(keyword, file);
	}

	public boolean verify() {
		if (path == null || path.length() == 0)
			return false;
		final File file = new File(path);
		return file.exists() && file.isDirectory();
	}

	public String getPath() {
		return path;
	}

	public Map getPages() {
		return pages;
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof ManPageFolder))
			return false;
		if (obj == this)
			return true;
		final ManPageFolder other = (ManPageFolder) obj;
		return (path != null ? path.equals(other.path) : other.path == null)
				&& other.pages.equals(this.pages);
	}

}
