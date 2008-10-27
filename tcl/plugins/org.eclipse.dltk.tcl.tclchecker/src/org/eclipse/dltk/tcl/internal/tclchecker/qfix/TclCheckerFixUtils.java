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

public class TclCheckerFixUtils {

	public static String getLabel(String replacement) {
		final String[] lines = TextUtils.splitLines(replacement, 3);
		for (int i = 0; i < lines.length; ++i) {
			lines[i] = lines[i].trim();
		}
		return Messages.TclCheckerMarkerResolution_replaceWith
				+ TextUtils.join(lines, ' ');
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

}
