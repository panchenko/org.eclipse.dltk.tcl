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

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IBuildpathEntry;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.ScriptProjectUtil;
import org.eclipse.dltk.core.builder.IBuildContext;
import org.eclipse.dltk.core.builder.IBuildParticipant;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension;
import org.eclipse.dltk.core.builder.IBuildParticipantExtension2;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.core.builder.IScriptBuilder.DependencyResponse;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.internal.core.ModelManager;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.InterpreterContainerHelper;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesFactory;
import org.eclipse.dltk.tcl.core.packages.TclProjectInfo;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.tcl.internal.core.packages.Messages;
import org.eclipse.dltk.tcl.internal.validators.TclBuildContext;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;
import org.eclipse.dltk.tcl.validators.packages.PackageSourceCollector;
import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

public class PackageRequireSourceAnalyser implements IBuildParticipant,
		IBuildParticipantExtension, IBuildParticipantExtension2 {

	private final IScriptProject project;
	private final IInterpreterInstall install;

	private final PackageSourceCollector packageCollector = new PackageSourceCollector();

	// private final Set<IPath> buildpath;
	// private final Map<String, Boolean> availabilityCache = new
	// HashMap<String, Boolean>();

	private final List<ModuleInfo> modules = new ArrayList<ModuleInfo>();

	private static class ModuleInfo {
		final String name;
		final ISourceLineTracker lineTracker;
		final TclModuleInfo moduleInfo;
		final IProblemReporter reporter;
		final IPath moduleLocation;

		public ModuleInfo(String moduleName, ISourceLineTracker codeModel,
				IProblemReporter reporter, TclModuleInfo moduleInfo,
				IPath moduleLocation) {
			this.name = moduleName;
			this.lineTracker = codeModel;
			this.reporter = reporter;
			this.moduleInfo = moduleInfo;
			this.moduleLocation = moduleLocation;
		}

	}

	/**
	 * @param project
	 * @throws CoreException
	 * @throws IllegalStateException
	 *             if associated interpreter could not be found
	 */
	public PackageRequireSourceAnalyser(IScriptProject project)
			throws CoreException, IllegalStateException {
		this.project = project;
		install = ScriptRuntime.getInterpreterInstall(project);
		if (install == null) {
			// thrown exception is caught in the PackageRequireCheckerFactory
			throw new IllegalStateException(NLS.bind(
					Messages.TclCheckBuilder_interpreterNotFound, project
							.getElementName()));
		}
		knownInfos = TclPackagesManager.getPackageInfos(install);
		// buildpath = getBuildpath(project);
	}

	private int buildType;
	private boolean autoAddPackages;
	private Set<TclModuleInfo> providedByRequiredProjects = new HashSet<TclModuleInfo>();

	public boolean beginBuild(int buildType) {
		this.buildType = buildType;
		this.autoAddPackages = ScriptProjectUtil.isBuilderEnabled(project);
		List<TclModuleInfo> moduleInfos = new ArrayList<TclModuleInfo>();
		moduleInfos.addAll(TclPackagesManager.getProjectModules(project
				.getElementName()));
		if (buildType == IBuildParticipantExtension.FULL_BUILD) {
			// We need to clear all information of builds instead of correction
			// information. Empty modules will be removed later.
			for (TclModuleInfo tclModuleInfo : moduleInfos) {
				tclModuleInfo.getProvided().clear();
				tclModuleInfo.getRequired().clear();
				tclModuleInfo.getSourced().clear();
			}
		}
		packageCollector.getModules().put(project, moduleInfos);
		loadProvidedPackagesFromRequiredProjects();
		return true;
	}

	private void loadProvidedPackagesFromRequiredProjects() {
		final IBuildpathEntry[] resolvedBuildpath;
		try {
			resolvedBuildpath = project.getResolvedBuildpath(true);
		} catch (ModelException e) {
			TclValidatorsCore.error(e);
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
					List<TclModuleInfo> list = TclPackagesManager
							.getProjectModules(project.getName());
					providedByRequiredProjects.addAll(list);
				}
			}
		}
	}

	private final NamespaceScopeProcessor processor = DefinitionManager
			.getInstance().createProcessor();

	private static final char[] PACKAGE_CHARS = PackageSourceCollector.PACKAGE
			.toCharArray();
	private List<TclPackageInfo> knownInfos;

	public void buildExternalModule(IBuildContext context) throws CoreException {
		final char[] contents = context.getContents();
		if (CharOperation.indexOf(PACKAGE_CHARS, contents, false) == -1) {
			return;
		}
		final TclParser parser = new TclParser();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, false);
		final List<TclCommand> commands = parser.parse(new String(contents),
				null, processor);
		// packageCollector.getRequireRefs().clear();
		ISourceModule module = context.getSourceModule();
		packageCollector.process(commands, module);

		addInfoForModule(context, module);
	}

	private void addInfoForModule(IBuildContext context, ISourceModule module) {
		IPath modulePath = module.getPath();
		IEnvironment env = EnvironmentManager.getEnvironment(project);
		if (module.getResource() != null) {
			modulePath = module.getResource().getLocation();
			if (modulePath == null) {
				URI uri = module.getResource().getLocationURI();
				if (uri != null) {
					IFileHandle file = env.getFile(uri);
					if (file != null) {
						modulePath = file.getPath();
					}
				}
			}
		}
		modules
				.add(new ModuleInfo(module.getElementName(), context
						.getLineTracker(), context.getProblemReporter(),
						packageCollector.getCreateCurrentModuleInfo(module),
						modulePath));
	}

	public void build(IBuildContext context) throws CoreException {
		List<TclCommand> statements = TclBuildContext.getStatements(context);
		if (statements == null) {
			return;
		}
		// packageCollector.getRequireRefs().clear();
		ISourceModule module = context.getSourceModule();
		packageCollector.process(statements, module);
		addInfoForModule(context, module);
	}

	public void endBuild(IProgressMonitor monitor) {
		monitor.subTask(Messages.TclCheckBuilder_retrievePackages);
		// initialize manager caches after they are collected
		@SuppressWarnings("unchecked")
		final Set<String> names = InterpreterContainerHelper
				.getInterpreterContainerDependencies(project);
		// process all modules
		final Set<String> newDependencies = new HashSet<String>();
		int remainingWork = modules.size();
		IEnvironment environment = EnvironmentManager
				.getEnvironment(this.project);
		TclProjectInfo projectInfo = TclPackagesManager.getTclProject(project
				.getElementName());
		for (ModuleInfo moduleInfo : modules) {
			monitor.subTask(NLS.bind(Messages.TclCheckBuilder_processing,
					moduleInfo.name, Integer.toString(remainingWork)));

			for (TclSourceEntry ref : moduleInfo.moduleInfo.getRequired()) {
				// Check for user override for selected source value
				EList<UserCorrection> corrections = projectInfo
						.getPackageCorrections();
				TclSourceEntry toCheck = ref;
				for (UserCorrection userCorrection : corrections) {
					if (userCorrection.getOriginalValue()
							.equals(ref.getValue())) {
						toCheck = TclPackagesFactory.eINSTANCE
								.createTclSourceEntry();
						toCheck.setEnd(ref.getEnd());
						toCheck.setStart(ref.getStart());
						toCheck.setValue(userCorrection.getUserValue());
						break;
					}
				}
				checkPackage(toCheck, moduleInfo.reporter,
						moduleInfo.lineTracker, newDependencies, names);
			}
			IPath folder = moduleInfo.moduleLocation.removeLastSegments(1);
			// Convert path to real path.

			for (TclSourceEntry source : moduleInfo.moduleInfo.getSourced()) {
				EList<UserCorrection> corrections = moduleInfo.moduleInfo
						.getSourceCorrections();
				IPath sourcedPath = null;
				boolean needToAddCorrection = false;
				for (UserCorrection userCorrection : corrections) {
					if (userCorrection.getOriginalValue().equals(
							source.getValue())) {
						String userValue = userCorrection.getUserValue();

						if (environment.isLocal()) {
							sourcedPath = Path.fromOSString(userValue);
						} else {
							userValue = userValue.replace('\\', '/');
							sourcedPath = Path.fromPortableString(source
									.getValue());
						}
						break;
					}
				}
				if (sourcedPath == null) {
					sourcedPath = resolveSourceValue(folder, source,
							environment);
					needToAddCorrection = true;
				}
				if (sourcedPath != null) {
					IFileHandle file = environment.getFile(sourcedPath);
					if (file != null) {
						if (!file.exists()) {
							reportSourceProblem(source, moduleInfo.reporter,
									"Could not locate sourced file:"
											+ file.toOSString(), source
											.getValue(), moduleInfo.lineTracker);
						} else {
							if (file.isDirectory()) {
								reportSourceProblem(
										source,
										moduleInfo.reporter,
										"Sourcing of folders are not supported",
										source.getValue(),
										moduleInfo.lineTracker);
							} else
							// Add user correction if not specified yet.
							if (needToAddCorrection) {
								if (!isAutoAddPackages()) {
									reportSourceProblem(
											source,
											moduleInfo.reporter,
											"Source are not added to project buildpath.",
											source.getValue(),
											moduleInfo.lineTracker);
								} else {
									UserCorrection correction = TclPackagesFactory.eINSTANCE
											.createUserCorrection();
									correction.setOriginalValue(source
											.getValue());
									correction.setUserValue(file.toString());
									corrections.add(correction);
								}
							}
						}
					}
				} else {
					if (!TclPackagesManager.isValidName(source.getValue())) {
						reportSourceProblemCorrection(
								source,
								moduleInfo.reporter,
								"Could not locate sourced file. Correction is required.",
								source.getValue(), moduleInfo.lineTracker);
					}
				}
			}

			--remainingWork;
		}
		if (buildType != IBuildParticipantExtension.RECONCILE_BUILD
				&& isAutoAddPackages() && !newDependencies.isEmpty()) {
			if (names.addAll(newDependencies)) {
				InterpreterContainerHelper.setInterpreterContainerDependencies(
						project, names);
			}
		}
		if (buildType != IBuildParticipantExtension.RECONCILE_BUILD) {
			List<TclModuleInfo> mods = packageCollector.getModules().get(
					project);
			List<TclModuleInfo> result = new ArrayList<TclModuleInfo>();
			// Clean modules without required items
			for (TclModuleInfo tclModuleInfo : mods) {
				if (!(tclModuleInfo.getProvided().isEmpty()
						&& tclModuleInfo.getRequired().isEmpty()
						&& tclModuleInfo.getSourced().isEmpty() && tclModuleInfo
						.getSourceCorrections().isEmpty())) {
					result.add(tclModuleInfo);
				}
			}
			// Save packages provided by the project
			TclPackagesManager.setProjectModules(project.getElementName(),
					result);
			// Do delta refresh
			try {
				ModelManager.getModelManager().getDeltaProcessor()
						.checkExternalChanges(new IModelElement[] { project },
								new NullProgressMonitor());
			} catch (ModelException e) {
				DLTKCore.error("Failed to call for model update:", e);
			}
		}
	}

	private IPath resolveSourceValue(IPath folder, TclSourceEntry source,
			IEnvironment environment) {
		String value = source.getValue();
		IPath valuePath = null;
		if (environment.isLocal()) {
			valuePath = Path.fromOSString(source.getValue());
		} else {
			value = value.replace('\\', '/');
			valuePath = Path.fromPortableString(source.getValue());
		}
		IPath sourcedPath = null;
		if (valuePath.isAbsolute()) {
			sourcedPath = valuePath;
		} else {
			if (TclPackagesManager.isValidName(value)) {
				sourcedPath = folder.append(valuePath);
			}
		}
		return sourcedPath;
	}

	private void reportPackageProblem(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_REQUIRED_PACKAGE, new String[] { pkgName },
				ProblemSeverities.Error, pkg.getStart(), pkg.getEnd(),
				lineTracker.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportPackageProblemCorrection(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION,
				new String[] { pkgName }, ProblemSeverities.Error, pkg
						.getStart(), pkg.getEnd(), lineTracker
						.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportSourceProblem(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_SOURCE, new String[] { pkgName },
				ProblemSeverities.Error, pkg.getStart(), pkg.getEnd(),
				lineTracker.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportSourceProblemCorrection(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_SOURCE_CORRECTION,
				new String[] { pkgName }, ProblemSeverities.Error, pkg
						.getStart(), pkg.getEnd(), lineTracker
						.getLineNumberOfOffset(pkg.getStart())));
	}

	private void checkPackage(TclSourceEntry pkg, IProblemReporter reporter,
			ISourceLineTracker lineTracker, Set<String> newDependencies,
			Set<String> names) {
		final String packageName = pkg.getValue();

		List<TclModuleInfo> list = new ArrayList<TclModuleInfo>();
		List<TclModuleInfo> collected = packageCollector.getModules().get(
				project);
		if (collected != null) {
			list.addAll(collected);
		}
		if (providedByRequiredProjects != null) {
			list.addAll(this.providedByRequiredProjects);
		}

		for (TclModuleInfo tclModuleInfo : list) {
			IModelElement element = DLTKCore.create(tclModuleInfo.getHandle());
			// Check for file existance
			if (element != null && element.exists()) {
				EList<TclSourceEntry> provided = tclModuleInfo.getProvided();
				for (TclSourceEntry tclSourceEntry : provided) {
					if (tclSourceEntry.getValue().equals(packageName)) {
						return; // Found provided package
					}
				}
			}
		}

		// Report unknown packages
		boolean found = false;
		for (TclPackageInfo info : this.knownInfos) {
			if (info.getName().equals(packageName)) {
				found = true;
				break;
			}
		}
		if (!found) {
			if (!TclPackagesManager.isValidName(packageName)) {
				reportPackageProblemCorrection(
						pkg,
						reporter,
						NLS
								.bind(
										"Could not detect required package. Correction is required",
										packageName), packageName, lineTracker);
				return;
			} else {
				reportPackageProblem(pkg, reporter, NLS.bind(
						Messages.TclCheckBuilder_unknownPackage, packageName),
						packageName, lineTracker);
			}
			return;
		}

		// Receive main package and it paths.
		if (!isAutoAddPackages()) {
			// Check for already added packages for case then builder is not
			// enabled.
			if (!names.contains(packageName)) {
				reportPackageProblem(pkg, reporter, NLS.bind(
						Messages.TclCheckBuilder_unresolvedDependencies,
						packageName), packageName, lineTracker);
			}
			return;
		}
		newDependencies.add(packageName);

		// final Set<TclPackageInfo> dependencies = TclPackagesManager
		// .getDependencies(install, packageName, true);
		// if (dependencies != null) {
		// for (TclPackageInfo dependencyName : dependencies) {
		// if (!isAutoAddPackages()) {
		// reportPackageProblem(pkg, reporter, NLS.bind(
		// Messages.TclCheckBuilder_unresolvedDependencies,
		// packageName), packageName, lineTracker);
		// return;
		// // newDependencies.add(dependencyName);
		// }
		// }
		// }
	}

	private final boolean isAutoAddPackages() {
		return autoAddPackages;
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
