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
package org.eclipse.dltk.tcl.internal.validators.packages;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension2;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.core.builder.IScriptBuilder.DependencyResponse;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.internal.core.packages.Messages;
import org.eclipse.dltk.tcl.internal.core.packages.PackagesManager;
import org.eclipse.dltk.tcl.internal.validators.TclBuildContext;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.osgi.util.NLS;

public class PackageRequireChecker implements IBuildParticipant,
		IBuildParticipantExtension, IBuildParticipantExtension2 {

	private final IScriptProject project;
	private final IInterpreterInstall install;

	private final PackagesManager manager = PackagesManager.getInstance();
	private final PackageCollector packageCollector = new PackageCollector();

	private final Set<String> knownPackageNames;
	private final Set<IPath> buildpath;
	private final Map<String, Boolean> availabilityCache = new HashMap<String, Boolean>();

	private final List<ModuleInfo> modules = new ArrayList<ModuleInfo>();

	private final Set<String> providedByRequiredProjects = new HashSet<String>();

	private static class ModuleInfo {
		final String name;
		final ISourceLineTracker lineTracker;
		final List<PackageRequireRef> requireDirectives;
		final IProblemReporter reporter;

		public ModuleInfo(String moduleName, ISourceLineTracker codeModel,
				IProblemReporter reporter,
				List<PackageRequireRef> requireDirectives) {
			this.name = moduleName;
			this.lineTracker = codeModel;
			this.reporter = reporter;
			this.requireDirectives = requireDirectives;
		}

	}

	/**
	 * @param project
	 * @throws CoreException
	 * @throws IllegalStateException
	 *             if associated interpreter could not be found
	 */
	@SuppressWarnings("unchecked")
	public PackageRequireChecker(IScriptProject project) throws CoreException,
			IllegalStateException {
		this.project = project;
		install = ScriptRuntime.getInterpreterInstall(project);
		if (install == null) {
			// thrown exception is caught in the TclPackageCheckerType
			throw new IllegalStateException(NLS.bind(
					Messages.TclCheckBuilder_interpreterNotFound, project
							.getElementName()));
		}
		knownPackageNames = manager.getPackageNames(install);
		buildpath = getBuildpath(project);
	}

	private int buildType;

	public void beginBuild(int buildType) {
		this.buildType = buildType;
		if (buildType != IBuildParticipantExtension.FULL_BUILD) {
			@SuppressWarnings("unchecked")
			final Set<String> projectPackages = manager
					.getInternalPackageNames(install, project);
			packageCollector.getPackagesProvided().addAll(projectPackages);
		}
		loadProvidedPackagesFromRequiredProjects();
	}

	private void loadProvidedPackagesFromRequiredProjects() {
		final IBuildpathEntry[] resolvedBuildpath;
		try {
			resolvedBuildpath = project.getResolvedBuildpath(true);
		} catch (ModelException e) {
			TclPlugin.error(e);
			return;
		}
		final IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();
		for (int i = 0; i < resolvedBuildpath.length; i++) {
			final IBuildpathEntry entry = resolvedBuildpath[i];
			if (entry.getEntryKind() == IBuildpathEntry.BPE_PROJECT) {
				final IPath path = entry.getPath();
				final IProject project = workspaceRoot.getProject(path
						.lastSegment());
				if (project.exists()) {
					@SuppressWarnings("unchecked")
					final Set<String> projectPackages = manager
							.getInternalPackageNames(install, project);
					providedByRequiredProjects.addAll(projectPackages);
				}
			}
		}
	}

	private final NamespaceScopeProcessor processor = DefinitionManager
			.getInstance().createProcessor();

	public void buildExternalModule(IBuildContext context) throws CoreException {
		final String source = new String(context.getContents());
		if (source.indexOf(PackageCollector.PACKAGE) == -1) {
			return;
		}
		final TclParser parser = new TclParser();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, false);
		final List<TclCommand> commands = parser.parse(source, null, processor);
		packageCollector.getRequireRefs().clear();
		packageCollector.process(commands);
	}

	public void build(IBuildContext context) throws CoreException {
		List<TclCommand> statements = TclBuildContext.getStatements(context);
		if (statements == null) {
			return;
		}
		packageCollector.getRequireRefs().clear();
		packageCollector.process(statements);
		if (!packageCollector.getRequireRefs().isEmpty()) {
			modules.add(new ModuleInfo(context.getSourceModule()
					.getElementName(), context.getLineTracker(), context
					.getProblemReporter(), new ArrayList<PackageRequireRef>(
					packageCollector.getRequireRefs())));
		}
	}

	public void endBuild(IProgressMonitor monitor) {
		if (buildType != IBuildParticipantExtension.RECONCILE_BUILD) {
			// Save packages provided by the project
			manager.setInternalPackageNames(install, project, packageCollector
					.getPackagesProvided());
		}
		monitor.subTask(Messages.TclCheckBuilder_retrievePackages);
		// initialize manager caches after they are collected
		final Set<String> requiredPackages = packageCollector
				.getRequirePackages();
		if (!requiredPackages.isEmpty()) {
			manager.getPathsForPackages(install, requiredPackages);
			manager.getPathsForPackagesWithDeps(install, requiredPackages);
		}
		// process all modules
		int remainingWork = modules.size();
		for (ModuleInfo moduleInfo : modules) {
			monitor.subTask(NLS.bind(Messages.TclCheckBuilder_processing,
					moduleInfo.name, Integer.toString(remainingWork)));
			for (PackageRequireRef ref : moduleInfo.requireDirectives) {
				checkPackage(ref, moduleInfo.reporter, moduleInfo.lineTracker);
			}
			--remainingWork;
		}
	}

	private static Set<IPath> getBuildpath(IScriptProject project) {
		final IBuildpathEntry[] resolvedBuildpath;
		try {
			resolvedBuildpath = project.getResolvedBuildpath(true);
		} catch (ModelException e1) {
			TclPlugin.error(e1);
			return Collections.emptySet();
		}
		final Set<IPath> buildpath = new HashSet<IPath>();
		for (int i = 0; i < resolvedBuildpath.length; i++) {
			final IBuildpathEntry entry = resolvedBuildpath[i];
			if (entry.getEntryKind() == IBuildpathEntry.BPE_LIBRARY
					&& entry.isExternal()) {
				buildpath.add(EnvironmentPathUtils
						.getLocalPath(entry.getPath()));
			}
		}
		return buildpath;
	}

	private void reportPackageProblem(PackageRequireRef pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_REQUIRED_PACKAGE, new String[] { pkgName },
				ProblemSeverities.Error, pkg.start, pkg.end, lineTracker
						.getLineNumberOfOffset(pkg.start)));
	}

	private void checkPackage(PackageRequireRef pkg, IProblemReporter reporter,
			ISourceLineTracker lineTracker) {
		final String packageName = pkg.name;

		if (packageCollector.getPackagesProvided().contains(packageName)) {
			return;
		}
		if (providedByRequiredProjects.contains(packageName)) {
			return;
		}

		// Report unknown packages
		if (!knownPackageNames.contains(packageName)) {
			reportPackageProblem(pkg, reporter, NLS.bind(
					Messages.TclCheckBuilder_unknownPackage, packageName),
					packageName, lineTracker);
			return;
		}

		// Receive main package and it paths.
		if (!isAvailable(packageName)) {
			reportPackageProblem(pkg, reporter, NLS.bind(
					Messages.TclCheckBuilder_unresolvedDependencies,
					packageName), packageName, lineTracker);
			return;
		}

		@SuppressWarnings("unchecked")
		final Set<String> dependencies = manager.getDependencies(packageName,
				install).keySet();
		for (String dependencyName : dependencies) {
			if (!isAvailable(dependencyName)) {
				reportPackageProblem(pkg, reporter, NLS.bind(
						Messages.TclCheckBuilder_unresolvedDependencies,
						packageName), packageName, lineTracker);
				return;
			}
		}
	}

	/**
	 * returns <code>true</code> if package is available, <code>false</code> if
	 * not
	 * 
	 * @param packageName
	 * @return
	 */
	private boolean isAvailable(String packageName) {
		final Boolean result = availabilityCache.get(packageName);
		if (result != null) {
			return result.booleanValue();
		}
		if (packageCollector.getPackagesProvided().contains(packageName)) {
			availabilityCache.put(packageName, Boolean.TRUE);
			return true;
		}
		if (providedByRequiredProjects.contains(packageName)) {
			availabilityCache.put(packageName, Boolean.TRUE);
			return true;
		}
		final boolean res = isOnBuildpath(buildpath, manager
				.getPathsForPackage(install, packageName));
		availabilityCache.put(packageName, Boolean.valueOf(res));
		return res;
	}

	/**
	 * Returns <code>true</code> if <code>paths</code> are contained in the
	 * <code>buildpath</code>
	 * 
	 * @param buildpath
	 * @param paths
	 * @return
	 */
	private static boolean isOnBuildpath(Set<IPath> buildpath, IPath[] paths) {
		if (paths != null) {
			for (final IPath path : paths) {
				if (!isOnBuildpath(buildpath, path)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns <code>true</code> if <code>path</code> is contained in the
	 * <code>buildpath</code>
	 * 
	 * @param buildpath
	 * @param path
	 * @return
	 */
	private static boolean isOnBuildpath(Set<IPath> buildpath, IPath path) {
		if (!buildpath.contains(path)) {
			for (IPath pp : buildpath) {
				if (pp.isPrefixOf(path)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public DependencyResponse getDependencies(int buildType, Set localElements,
			Set externalElements, Set oldExternalFolders, Set externalFolders) {
		if (buildType == IBuildParticipantExtension.FULL_BUILD
				|| !oldExternalFolders.equals(externalFolders)) {
			return DependencyResponse.FULL_EXTERNAL_BUILD;
		} else {
			return null;
		}
	}

}
