package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptModelMarker;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.ui.text.ScriptMarkerResoltionUtils;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolutionGenerator;

public class TclRequireMarkerResolutionGenerator implements
		IMarkerResolutionGenerator {

	public IMarkerResolution[] getResolutions(IMarker marker) {
		if (TclSourcePackageCorrectionProcessor.isFixable(marker)) {
			int idValue = marker.getAttribute(IScriptModelMarker.ID, 0);
			if (idValue == TclProblems.UNKNOWN_REQUIRED_PACKAGE) {
				String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
				if (pkgName != null) {
					IProject project = marker.getResource().getProject();
					IScriptProject scriptProject = DLTKCore.create(project);
					return new IMarkerResolution[] { new TclRequirePackageMarkerResolution(
							pkgName, scriptProject) };
				}
			} else if (marker.getAttribute(IScriptModelMarker.ID, 0) == TclProblems.UNKNOWN_SOURCE_CORRECTION) {
				String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
				if (pkgName != null) {
					IProject project = marker.getResource().getProject();
					IScriptProject scriptProject = DLTKCore.create(project);
					IResource resource = marker.getResource();
					if (resource.getType() == IResource.FILE) {
						ISourceModule file = (ISourceModule) DLTKCore
								.create(resource);
						List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();
						resolutions
								.add(new TclSourceCorrectionMarkerResolution(
										pkgName, scriptProject, file));

						addGlobalVariableCorrections(pkgName, project,
								scriptProject, resolutions);

						return resolutions
								.toArray(new IMarkerResolution[resolutions
										.size()]);
					}
				}
			} else if (idValue == TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION) {
				String pkgName = CorrectionEngine.getProblemArguments(marker)[0];
				if (pkgName != null) {
					final IResource resource = marker.getResource();
					if (resource.getType() == IResource.FILE) {
						ISourceModule module = (ISourceModule) DLTKCore
								.create((IFile) resource);
						IProject project = resource.getProject();
						IScriptProject scriptProject = DLTKCore.create(project);
						List<IMarkerResolution> resolutions = new ArrayList<IMarkerResolution>();
						resolutions
								.add(new TclRequirePackageCorrectionMarkerResolution(
										pkgName, scriptProject, module));
						addGlobalVariableCorrections(pkgName, project,
								scriptProject, resolutions);
						return resolutions
								.toArray(new IMarkerResolution[resolutions
										.size()]);
					}
				}
			}
		}
		return ScriptMarkerResoltionUtils.NO_RESOLUTIONS;
	}

	private void addGlobalVariableCorrections(String pkgName, IProject project,
			IScriptProject scriptProject, List<IMarkerResolution> resolutions) {
		String[] names = TclVariableResolver.extractVariableNames(pkgName);
		Map<String, String> eMap = TclPackagesManager.getVariables(project
				.getName());
		if (names != null) {
			for (String var : names) {
				if (!eMap.containsKey(var)) {
					resolutions
							.add(new TclGlobalVariableSourceCorrectionMarkerResolution(
									var, scriptProject));
				}
			}
		}
	}
}
