/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.ScriptElementImageProvider;
import org.eclipse.dltk.ui.ScriptElementLabels;
import org.eclipse.dltk.ui.viewsupport.AppearanceAwareLabelProvider;

/**
 * Provides the labels for the Package Explorer.
 * <p>
 * It provides labels for the packages in hierarchical layout and in all other
 * cases delegates it to its super class.
 * </p>
 */
public class InstrumentationLabelProvider extends AppearanceAwareLabelProvider {

	protected InstrumentationContentProvider fContentProvider;

	private boolean fIsFlatLayout = false;

	public InstrumentationLabelProvider(InstrumentationContentProvider cp) {
		super(DEFAULT_TEXTFLAGS | ScriptElementLabels.P_COMPRESSED
				| ScriptElementLabels.ALL_CATEGORY, DEFAULT_IMAGEFLAGS
				| ScriptElementImageProvider.SMALL_ICONS, TclUI.getDefault()
				.getPreferenceStore());
		Assert.isNotNull(cp);
		fContentProvider = cp;
	}

	private String getSpecificText(Object element) {
		if (!fIsFlatLayout && element instanceof IScriptFolder) {
			IScriptFolder fragment = (IScriptFolder) element;
			Object parent = fContentProvider
					.getHierarchicalPackageParent(fragment);
			if (parent instanceof IScriptFolder) {
				return getNameDelta((IScriptFolder) parent, fragment);
			} else if (parent instanceof IFolder) { // bug 152735
				return getNameDelta((IFolder) parent, fragment);
			}
		}
		return null;
	}

	public String getText(Object element) {
		String text = getSpecificText(element);
		if (text != null) {
			return decorateText(text, element);
		}
		return super.getText(element);
	}

	private String getNameDelta(IScriptFolder parent, IScriptFolder fragment) {
		String prefix = parent.getElementName()
				+ IScriptFolder.PACKAGE_DELIMITER;
		String fullName = fragment.getElementName();
		if (fullName.startsWith(prefix)) {
			return fullName.substring(prefix.length());
		}
		return fullName;
	}

	private String getNameDelta(IFolder parent, IScriptFolder fragment) {
		IPath prefix = parent.getFullPath();
		IPath fullPath = fragment.getPath();
		if (prefix.isPrefixOf(fullPath)) {
			StringBuffer buf = new StringBuffer();
			for (int i = prefix.segmentCount(); i < fullPath.segmentCount(); i++) {
				if (buf.length() > 0)
					buf.append(IScriptFolder.PACKAGE_DELIMITER);
				buf.append(fullPath.segment(i));
			}
			return buf.toString();
		}
		return fragment.getElementName();
	}

	public void setIsFlatLayout(boolean state) {
		fIsFlatLayout = state;
	}

}
