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
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.ui.text.ScriptMarkerResoltionUtils;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class TclCheckerMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		final String[] corrections = CorrectionEngine.decodeArguments(marker
				.getAttribute(TclCheckerMarker.SUGGESTED_CORRECTIONS, null));
		if (corrections != null) {
			final IMarkerResolution[] result = new IMarkerResolution[corrections.length];
			for (int i = 0; i < corrections.length; ++i) {
				result[i] = new TclCheckerMarkerResolution(corrections[i]);
			}
			return result;
		}
		return ScriptMarkerResoltionUtils.NO_RESOLUTIONS;
	}
}
