package org.eclipse.dltk.tcl.internal.core.sources;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.emf.common.util.EList;

public class TclSourcesUtils {
	public static Set<IPath> getBuildpath(IScriptProject project,
			Set<IScriptProject> projectVisitSet) {
		if (projectVisitSet.contains(project)) {
			return Collections.emptySet();
		}
		projectVisitSet.add(project);
		final IBuildpathEntry[] resolvedBuildpath;
		try {
			resolvedBuildpath = project.getResolvedBuildpath(false);
		} catch (ModelException e1) {
			return Collections.emptySet();
		}
		final Set<IPath> buildpath = new HashSet<IPath>();
		for (int i = 0; i < resolvedBuildpath.length; i++) {
			final IBuildpathEntry entry = resolvedBuildpath[i];
			if (entry.getEntryKind() == IBuildpathEntry.BPE_LIBRARY
					&& entry.isExternal()) {
				buildpath.add(entry.getPath());
			} else if (entry.getEntryKind() == IBuildpathEntry.BPE_PROJECT) {
				IPath path = entry.getPath();
				IProject prj = ResourcesPlugin.getWorkspace().getRoot()
						.getProject(path.lastSegment());
				if (prj != null && prj.isAccessible()) {
					IScriptProject scrPrj = DLTKCore.create(prj);
					if (scrPrj != null && scrPrj.exists()) {
						buildpath.addAll(getBuildpath(scrPrj, projectVisitSet));
					}
				}
			}
		}
		return buildpath;
	}

	public static Set<IPath> getPackages(IScriptProject project,
			IInterpreterInstall install) {
		Set<IPath> result = new HashSet<IPath>();
		Set<String> packages = InterpreterContainerHelper
				.getInterpreterContainerDependencies(project);

		List<TclPackageInfo> packageInfos = TclPackagesManager.getPackageInfos(
				install, packages, true);
		for (TclPackageInfo tclPackageInfo : packageInfos) {
			EList<String> sources = tclPackageInfo.getSources();
			for (String source : sources) {
				result.add(new Path(source));
			}
		}

		return result;
	}

	public static void fillSources(IInterpreterInstall install,
			IScriptProject scriptProject, Set<IPath> sources) {
		Set<IScriptProject> visitedProjects = new HashSet<IScriptProject>();
		Set<IPath> buildpath = getBuildpath(scriptProject, visitedProjects);
		Set<IPath> packageFiles = getPackages(scriptProject, install);

		List<TclModuleInfo> modules = TclPackagesManager.getTclProject(
				scriptProject.getElementName()).getModules();
		for (TclModuleInfo tclModuleInfo : modules) {
			EList<TclSourceEntry> sourced = tclModuleInfo.getSourced();
			EList<UserCorrection> corrections = tclModuleInfo
					.getSourceCorrections();
			Map<String, String> correctionMap = new HashMap<String, String>();
			for (UserCorrection userCorrection : corrections) {
				correctionMap.put(userCorrection.getOriginalValue(),
						userCorrection.getUserValue());
			}
			for (TclSourceEntry source : sourced) {
				String value = null;
				if (correctionMap.containsKey(source.getValue())) {
					value = correctionMap.get(source.getValue());
				}

				if (value == null) {
					continue;
				}
				IPath path = Path.fromPortableString(value);
				IPath pathParent = path.removeLastSegments(1);
				if (buildpath.contains(pathParent)) {
					continue; // File are on buildpath
				}
				// Check for file in some package
				if (packageFiles.contains(path)) {
					continue; // File are on buildpath
				}
				// Check for project and it references buildpath

				IFile file = ResourcesPlugin.getWorkspace().getRoot()
						.getFileForLocation(path);
				if (file != null) {
					boolean onBuildpath = false;
					for (IScriptProject p : visitedProjects) {
						if (p.isOnBuildpath(file)) {
							onBuildpath = true;
							break;
						}
					}
					if (onBuildpath) {
						continue;
					}
				}
				sources.add(path);
			}
		}
	}
}
