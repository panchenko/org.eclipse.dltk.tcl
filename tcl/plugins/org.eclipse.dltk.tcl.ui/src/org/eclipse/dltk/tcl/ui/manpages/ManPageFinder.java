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
package org.eclipse.dltk.tcl.ui.manpages;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.utils.CharArraySequence;

/**
 * @since 2.0
 */
public class ManPageFinder {

	private static final String CONTENTS_HTM = "contents.htm"; //$NON-NLS-1$

	private static final List<String> SKIP_DIRS = Arrays.asList("TkLib", //$NON-NLS-1$
			"TclLib", "Keywords", "UserCmd"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	public void find(Documentation documentation, File dir) {
		if (!dir.isDirectory())
			return;
		if (SKIP_DIRS.contains(dir.getName()))
			return;
		final File[] children = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return file.isDirectory()
						|| file.getName().startsWith(CONTENTS_HTM);
			}
		});
		for (File child : children) {
			if (child.isDirectory()) {
				find(documentation, child);
			} else if (child.getName().startsWith(CONTENTS_HTM)) {
				final String path = dir.getAbsolutePath();
				if (documentation.findFolder(path) == null) {
					ManPageFolder folder = ManpagesFactory.eINSTANCE
							.createManPageFolder();
					folder.setPath(path);
					parseContentsFile(child, folder);
					if (!folder.getKeywords().isEmpty()) {
						documentation.getFolders().add(folder);
					}
				}
			}
		}
	}

	private void parseContentsFile(File c, ManPageFolder folder) {
		final char[] result;
		try {
			result = Util.getFileCharContent(c, null);
		} catch (IOException e) {
			TclUI.error("Error reading " + c, e); //$NON-NLS-1$
			return;
		}
		Pattern pattern = Pattern.compile(
				"<a\\s+href=\"([a-zA-Z_0-9]+\\.html?)\"\\s*>(\\w+)</a>", //$NON-NLS-1$
				Pattern.CASE_INSENSITIVE);
		final Matcher matcher = pattern.matcher(new CharArraySequence(result));
		while (matcher.find()) {
			String file = matcher.group(1);
			if (file.equalsIgnoreCase("Copyright.htm")) //$NON-NLS-1$
				continue;
			String word = matcher.group(2);
			folder.addPage(word, file);
		}
	}

}
