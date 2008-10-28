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
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.TclChecker;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.dltk.validators.core.NullValidatorOutput;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;

public class TclCheckerFixUtils {

	public static String getLabel(String replacement) {
		final int totalLines = TextUtils.countLines(replacement);
		final String[] lines = TextUtils.splitLines(replacement, 3);
		for (int i = 0; i < lines.length; ++i) {
			lines[i] = lines[i].trim();
		}
		String result = Messages.TclCheckerMarkerResolution_replaceWith
				+ TextUtils.join(lines, ' ');
		if (totalLines > lines.length) {
			result += " ..."; //$NON-NLS-1$
		}
		return result;
	}

	public static String getDescription(String replacement) {
		return "<pre>" + replacement + "</pre>"; //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static class MarkerFinder implements IMarkerFinder {

		final int lineNumber;
		final String message;
		final String messageId;

		public MarkerFinder(IMarker pattern) {
			this.lineNumber = pattern.getAttribute(IMarker.LINE_NUMBER, -1);
			this.message = pattern.getAttribute(IMarker.MESSAGE, null);
			this.messageId = pattern.getAttribute(TclCheckerMarker.MESSAGE_ID,
					null);
		}

		public IMarker find(IResource resource) {
			final IMarker[] markers;
			try {
				markers = resource.findMarkers(TclCheckerMarker.TYPE, true,
						IResource.DEPTH_INFINITE);
			} catch (CoreException e) {
				TclCheckerPlugin.log(IStatus.ERROR, e.toString(), e);
				return null;
			}
			if (lineNumber < 0 || message == null || messageId == null) {
				return null;
			}
			for (int i = 0; i < markers.length; ++i) {
				final IMarker m = markers[i];
				if (lineNumber == m.getAttribute(IMarker.LINE_NUMBER, -1)
						&& message
								.equals(m.getAttribute(IMarker.MESSAGE, null))
						&& messageId.equals(m.getAttribute(
								TclCheckerMarker.MESSAGE_ID, null))) {
					return m;
				}
			}
			return null;
		}

	}

	public static IMarker verify(IMarker marker, ISourceModule module) {
		final IResource resource = marker.getResource();
		if (resource.getType() != IResource.FILE) {
			return null;
		}
		final String savedStamp = marker.getAttribute(
				TclCheckerMarker.TIMESTAMP, null);
		if (savedStamp != null
				&& savedStamp.equals(String.valueOf(resource
						.getModificationStamp()))) {
			return marker;
		} else {
			final IMarkerFinder finder = createMarkerFinder(marker);
			if (revalidate(resource, module)) {
				return finder.find(resource);
			} else {
				return null;
			}
		}
	}

	public static boolean revalidate(final IResource resource,
			ISourceModule module) {
		if (module == null) {
			module = (ISourceModule) DLTKCore.create((IFile) resource);
			if (module == null) {
				return false;
			}
		}
		final IEnvironment environment = EnvironmentManager
				.getEnvironment(module);
		if (environment == null) {
			return false;
		}
		new TclChecker(environment).validate(new ISourceModule[] { module },
				new NullValidatorOutput(), new NullProgressMonitor());
		return true;
	}

	public static IMarkerFinder createMarkerFinder(IMarker marker) {
		final IMarkerFinder finder = new MarkerFinder(marker);
		return finder;
	}

	/**
	 * @param target
	 * @param document
	 */
	public static void updateDocument(IMarker marker, IDocument document,
			String replacement, ITclCheckerQFixReporter reporter) {
		final int lineNumber = marker.getAttribute(IMarker.LINE_NUMBER, -1);
		int commandStart = marker.getAttribute(TclCheckerMarker.COMMAND_START,
				-1);
		int commandLength = marker.getAttribute(
				TclCheckerMarker.COMMAND_LENGTH, -1);
		if (lineNumber < 0 || commandStart < 0 || commandLength < 0) {
			reporter
					.showError(Messages.TclCheckerAnnotationResolution_wrongMarkerAttributes);
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
					reporter
							.showError(Messages.TclCheckerAnnotationResolution_markerDeleteError
									+ e.getMessage());
				}
			} else {
				reporter
						.showError(Messages.TclCheckerAnnotationResolution_wrongCommandStart);
			}
		} catch (BadLocationException e) {
			reporter
					.showError(Messages.TclCheckerAnnotationResolution_internalError
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

}
