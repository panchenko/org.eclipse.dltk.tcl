package org.eclipse.dltk.tcl.internal.ui.text;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptModelMarker;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.ui.text.ScriptMarkerResoltionUtils;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class TclRequireMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		if (TclSourcePackageCorrectionProcessor.isFixable(marker)) {
			if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
				String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
				if (pkgName != null) {
					IProject project = marker.getResource().getProject();
					IScriptProject scriptProject = DLTKCore.create(project);
					return new IMarkerResolution[] { new TclRequirePackageMarkerResolution(
							pkgName, scriptProject) };
				}
			} else if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
				String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
				if (pkgName != null) {
					final IResource resource = marker.getResource();
					if (resource.getType() == IResource.FILE) {
						ISourceModule module = (ISourceModule) DLTKCore
								.create((IFile) resource);
						IProject project = resource.getProject();
						IScriptProject scriptProject = DLTKCore.create(project);
						return new IMarkerResolution[] { new TclRequirePackageCorrectionMarkerResolution(
								pkgName, scriptProject, module) };
					}
				}
			}
		}
		return ScriptMarkerResoltionUtils.NO_RESOLUTIONS;
	}
}
