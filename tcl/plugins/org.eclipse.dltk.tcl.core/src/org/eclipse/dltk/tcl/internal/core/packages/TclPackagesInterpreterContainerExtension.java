package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IAccessRule;
import org.eclipse.dltk.core.IBuildpathAttribute;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IInterpreterContainerExtension;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.internal.core.BuildpathEntry;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.core.TclCorePreferences;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.emf.common.util.EList;

public class TclPackagesInterpreterContainerExtension implements
		IInterpreterContainerExtension {

	public TclPackagesInterpreterContainerExtension() {
	}

	public void processEntres(IScriptProject project,
			List<IBuildpathEntry> buildpathEntries) {
		if (TclCorePreferences.USE_PACKAGE_CONCEPT) {
			return;
		}
		final IInterpreterInstall install;
		try {
			install = ScriptRuntime.getInterpreterInstall(project);
		} catch (CoreException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			return;
		}
		final List<IPath> externals = new ArrayList<IPath>();
		for (IBuildpathEntry entry : buildpathEntries) {
			if (entry.getEntryKind() == IBuildpathEntry.BPE_LIBRARY
					&& entry.isExternal()) {
				externals.add(entry.getPath());
			}
		}
		final Set<String> packageNames = new HashSet<String>();
		InterpreterContainerHelper.getInterpreterContainerDependencies(project,
				packageNames, packageNames);

		final List<TclPackageInfo> infos = TclPackagesManager.getPackageInfos(
				install, packageNames, true);
		if (infos.isEmpty()) {
			return;
		}
		IEnvironment env = EnvironmentManager.getEnvironment(project);
		if (env == null) {
			return;
		}
		Set<IPath> allPaths = new HashSet<IPath>();
		for (TclPackageInfo info : infos) {
			EList<String> sources = info.getSources();
			for (String path : sources) {
				IPath rpath = new Path(path).removeLastSegments(1);
				IPath fullPath = EnvironmentPathUtils.getFullPath(env, rpath);
				allPaths.add(fullPath);
			}
		}

		Set<IPath> rawEntries = new HashSet<IPath>(allPaths.size());
		for (IPath entryPath : allPaths) {
			if (!entryPath.isEmpty()) {
				// TODO Check this
				// resolve symlink
				// {
				// IFileHandle f = env.getFile(entryPath);
				// if (f == null)
				// continue;
				// entryPath = new Path(f.getCanonicalPath());
				// }
				if (rawEntries.contains(entryPath))
					continue;
				if (!isPrefixOf(externals, entryPath)) {
					// paths to exclude
					ArrayList<IPath> excluded = new ArrayList<IPath>();
					for (IPath otherPath : allPaths) {
						if (otherPath.isEmpty())
							continue;
						// TODO Check this
						// resolve symlink
						// {
						// IFileHandle f = env.getFile(entryPath);
						// if (f == null)
						// continue;
						// entryPath = new Path(f.getCanonicalPath());
						// }
						// compare, if it contains some another
						if (entryPath.isPrefixOf(otherPath)
								&& !otherPath.equals(entryPath)) {
							IPath pattern = otherPath.removeFirstSegments(
									entryPath.segmentCount()).append("*");
							if (!excluded.contains(pattern)) {
								excluded.add(pattern);
							}
						}
					}
					// Check for interpreter container libraries.
					buildpathEntries.add(DLTKCore.newLibraryEntry(entryPath,
							IAccessRule.EMPTY_RULES,
							new IBuildpathAttribute[0],
							BuildpathEntry.INCLUDE_ALL, excluded
									.toArray(new IPath[excluded.size()]),
							false, true));
					rawEntries.add(entryPath);
				}
			}
		}
	}

	/**
	 * Tests if the entryPath is contained in the specified list of paths.
	 * 
	 * @param paths
	 * @param entryPath
	 * @return
	 */
	private static boolean isPrefixOf(final List<IPath> paths, IPath entryPath) {
		for (IPath path : paths) {
			if (path.isPrefixOf(entryPath)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @since 2.0
	 */
	public void preProcessEntries(IScriptProject project,
			List<IBuildpathEntry> entries) {
		// TODO Auto-generated method stub

	}
}
