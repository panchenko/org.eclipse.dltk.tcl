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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.internal.ui.manpages.ManPageFolder;
import org.eclipse.dltk.tcl.internal.ui.manpages.ManPageReader;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.ui.documentation.IScriptDocumentationProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;

public class TclManPagesDocumentationProvider implements
		IScriptDocumentationProvider {

	private List folders = null;

	public Reader getInfo(IMember element, boolean lookIntoParents,
			boolean lookIntoExternal) {
		return null;
	}

	public Reader getInfo(String content) {
		initalizeLocations(false);

		if (folders != null) {
			for (Iterator iterator = folders.iterator(); iterator.hasNext();) {
				ManPageFolder f = (ManPageFolder) iterator.next();
				Map pages = f.getKeywords().map();
				String ans = (String) pages.get(content);
				if (ans == null) {
					// Try to use first word of multiline call
					if (content.indexOf(' ') != -1) {
						String subContent = content.substring(0, content
								.indexOf(' '));
						ans = (String) pages.get(subContent);
					}
				}
				if (ans != null) {
					final File file = new File(f.getPath(), ans);
					if (file.isFile()) {
						try {
							return new FileReader(file);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		}

		return null;
	}

	private IPropertyChangeListener changeListener = null;

	private void initalizeLocations(boolean force) {
		if (!force && this.folders != null)
			return;

		final IPreferenceStore prefStore = TclUI.getDefault()
				.getPreferenceStore();

		final String value = prefStore
				.getString(TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS);

		this.folders = ManPageReader.read(value).getDocumentations().get(0)
				.getFolders();
		if (this.folders != null && changeListener == null) {
			changeListener = new IPropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent event) {
					initalizeLocations(true);
				}
			};
			prefStore.addPropertyChangeListener(changeListener);
		}
	}
}
