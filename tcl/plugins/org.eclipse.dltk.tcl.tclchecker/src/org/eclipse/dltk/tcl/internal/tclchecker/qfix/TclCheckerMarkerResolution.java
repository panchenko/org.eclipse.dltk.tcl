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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;

public class TclCheckerMarkerResolution implements IMarkerResolution,
		IMarkerResolution2 {

	private final String replacement;

	public TclCheckerMarkerResolution(String replacement) {
		this.replacement = replacement;
	}

	public String getLabel() {
		return TclCheckerFixUtils.getLabel(replacement);
	}

	public void run(IMarker marker) {
		final IMarker target = TclCheckerFixUtils.verify(marker, null);
		if (target != null) {
			fixMarker(target);
		}
	}

	/**
	 * @param marker
	 */
	private void fixMarker(IMarker marker) {
		final ISourceModule module = (ISourceModule) DLTKCore
				.create((IFile) marker.getResource());
		if (module == null) {
			return;
		}
		try {
			module.becomeWorkingCopy(null, new NullProgressMonitor());
			try {
				IDocument document = new Document(module.getSource());
				TclCheckerFixUtils.updateDocument(marker, document,
						replacement, new ITclCheckerQFixReporter() {

							public void showError(String message) {
								// NOP
							}
						});
				module.getBuffer().setContents(document.get());
				module.commitWorkingCopy(true, new NullProgressMonitor());
			} finally {
				module.discardWorkingCopy();
			}
		} catch (ModelException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	public String getDescription() {
		return TclCheckerFixUtils.getDescription(replacement);
	}

	public Image getImage() {
		return null;
	}

}
