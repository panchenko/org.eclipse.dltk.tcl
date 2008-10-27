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

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.ui.editor.IScriptAnnotation;
import org.eclipse.dltk.ui.text.IAnnotationResolution;
import org.eclipse.dltk.ui.text.IAnnotationResolution2;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.texteditor.IEditorStatusLine;
import org.eclipse.ui.texteditor.ITextEditor;

public class TclCheckerAnnotationResolution implements IAnnotationResolution,
		IAnnotationResolution2 {

	private final String replacement;
	private final ITextEditor editor;
	private final ISourceModule module;
	private final IMarker marker;

	public TclCheckerAnnotationResolution(String replacement,
			ITextEditor editor, ISourceModule module, IMarker marker) {
		this.replacement = replacement;
		this.editor = editor;
		this.module = module;
		this.marker = marker;
	}

	public String getLabel() {
		return TclCheckerFixUtils.getLabel(replacement);
	}

	public void run(IScriptAnnotation annotation, IDocument document) {
		if (editor.isDirty()) {
			final IResource resource = marker.getResource();
			final IMarkerFinder finder = TclCheckerFixUtils
					.createMarkerFinder(marker);
			editor.doSave(new NullProgressMonitor());
			// TODO revalidate only if not automatic
			if (TclCheckerFixUtils.revalidate(resource, module)) {
				final IMarker target = finder.find(resource);
				if (target != null) {
					updateDocument(target, document);
				} else {
					showError(Messages.TclCheckerAnnotationResolution_problemDisappeared);
				}
			} else {
				showError(Messages.TclCheckerAnnotationResolution_revalidationError);
			}
		} else {
			final IMarker target = TclCheckerFixUtils.verify(marker, module);
			if (target != null) {
				updateDocument(target, document);
			} else {
				showError(Messages.TclCheckerAnnotationResolution_problemDisappeared);
			}
		}
	}

	/**
	 * @param target
	 * @param document
	 */
	private void updateDocument(IMarker marker, IDocument document) {
		final int lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, -1);
		int commandStart = marker.getAttribute(TclCheckerMarker.COMMAND_START,
				-1);
		int commandLength = marker.getAttribute(
				TclCheckerMarker.COMMAND_LENGTH, -1);
		if (lineNumber < 0 || commandStart < 0 || commandLength < 0) {
			showError(Messages.TclCheckerAnnotationResolution_wrongMarkerAttributes);
			return;
		}
		try {
			final IRegion lineRegion = document
					.getLineInformation(lineNumber - 1);
			if (commandStart < lineRegion.getOffset()) {
				commandStart += calculateAdjustment(document, 0, lineNumber - 1);
			}
			if (commandStart >= lineRegion.getOffset()
					&& commandStart < lineRegion.getOffset()
							+ lineRegion.getLength()) {
				document.replace(commandStart, commandLength - 1, replacement);
				/* XXX -1 above is compensating bug in TclChecker output */
				try {
					marker.delete();
				} catch (CoreException e) {
					showError(Messages.TclCheckerAnnotationResolution_markerDeleteError
							+ e.getMessage());
				}
			} else {
				showError(Messages.TclCheckerAnnotationResolution_wrongCommandStart);
			}
		} catch (BadLocationException e) {
			showError(Messages.TclCheckerAnnotationResolution_internalError
					+ e.getMessage());
		}
	}

	private static int calculateAdjustment(IDocument document, int startLine,
			int endLine) throws BadLocationException {
		int result = 0;
		for (int i = startLine; i < endLine; ++i) {
			String delimiter = document.getLineDelimiter(i);
			if (delimiter != null && delimiter.length() > 1) {
				result += delimiter.length() - 1;
			}
		}
		return result;
	}

	/**
	 * @param string
	 */
	private void showError(String message) {
		final IEditorStatusLine statusLine = (IEditorStatusLine) editor
				.getAdapter(IEditorStatusLine.class);
		if (statusLine != null) {
			statusLine.setMessage(true, message, null);
		}
	}

	public String getDescription() {
		return TclCheckerFixUtils.getDescription(replacement);
	}
}
