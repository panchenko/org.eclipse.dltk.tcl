package org.eclipse.dltk.tcl.internal.ui.text;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class TclRequireMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		if (TclCorrectionProcessor.isFixable(marker)) {
			String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
			if (pkgName != null) {
				IProject project = marker.getResource().getProject();
				IScriptProject scriptProject = DLTKCore.create(project);
				return new IMarkerResolution[] { new TclRequirePackageMarkerResolution(
						pkgName, scriptProject, (ISourceModule) DLTKCore
								.create(marker.getResource())) };
			}
		}
		return new IMarkerResolution[0];
	}
}
