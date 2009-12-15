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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
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
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.TclProblems;
import org.eclipse.dltk.tcl.core.packages.TclModuleInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;
import org.eclipse.dltk.tcl.core.packages.TclPackagesFactory;
import org.eclipse.dltk.tcl.core.packages.TclSourceEntry;
import org.eclipse.dltk.tcl.core.packages.UserCorrection;
import org.eclipse.dltk.tcl.indexing.PackageSourceCollector;
import org.eclipse.dltk.tcl.internal.core.packages.DefaultVariablesRegistry;
import org.eclipse.dltk.tcl.internal.core.packages.TclPackageSourceModule;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.tcl.internal.validators.TclBuildContext;
import org.eclipse.dltk.tcl.validators.TclValidatorsCore;
import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

public class PackageRequireSourceAnalyser implements IBuildParticipant,
		IBuildParticipantExtension, IBuildParticipantExtension2 {

	private final IScriptProject project;
	private final IInterpreterInstall install;

	private final TclVariableResolver variableResolver;

	private final PackageSourceCollector packageCollector = new PackageSourceCollector();

	private final List<ModuleInfo> modules = new ArrayList<ModuleInfo>();

	private static class ModuleInfo {
		final String name;
		final ISourceLineTracker lineTracker;
		final TclModuleInfo moduleInfo;
		final IProblemReporter reporter;
		final IPath moduleLocation;
		final ISourceModule sourceModule;

		public ModuleInfo(String moduleName, ISourceLineTracker codeModel,
				IProblemReporter reporter, TclModuleInfo moduleInfo,
				IPath moduleLocation, ISourceModule sourceModule) {
			this.name = moduleName;
			this.lineTracker = codeModel;
			this.reporter = reporter;
			this.moduleInfo = moduleInfo;
			this.moduleLocation = moduleLocation;
			this.sourceModule = sourceModule;
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
		if (!project.exists()) {
			// thrown exception is caught in the PackageRequireCheckerFactory
			throw new IllegalStateException(NLS.bind(
					Messages.TclCheckBuilder_interpreterNotFound, project
							.getElementName()));
		}
		install = ScriptRuntime.getInterpreterInstall(project);
		if (install == null) {
			// thrown exception is caught in the PackageRequireCheckerFactory
			throw new IllegalStateException(NLS.bind(
					Messages.TclCheckBuilder_interpreterNotFound, project
							.getElementName()));
		}
		knownInfos = TclPackagesManager.getPackageInfos(install);
		variableResolver = new TclVariableResolver(
				new DefaultVariablesRegistry(project));
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

	private List<TclPackageInfo> knownInfos;

	public void buildExternalModule(IBuildContext context) throws CoreException {

		// Try to restore information from cache
		ISourceModule module = context.getSourceModule();
		if (module instanceof TclPackageSourceModule) {
			/* Do not process modules which are parts of packages */
			return;
		}
		// TclModuleInfo info = collectCachedInfo(module);

		// if (info == null) {
		TclModule tclModule = TclBuildContext.getStatements(context);
		List<TclCommand> statements = tclModule.getStatements();

		// packageCollector.getRequireRefs().clear();
		packageCollector.process(statements, module);
		// }

		addInfoForModule(context, module, null);
	}

	public void build(IBuildContext context) throws CoreException {

		ISourceModule module = context.getSourceModule();
		// TclModuleInfo info = collectCachedInfo(module);
		// Check cached information first
		// if (info == null) {
		TclModule tclModule = TclBuildContext.getStatements(context);
		List<TclCommand> statements = tclModule.getStatements();
		if (statements == null) {
			return;
		}
		// packageCollector.getRequireRefs().clear();
		packageCollector.process(statements, module);
		// }
		addInfoForModule(context, module, null);
	}

	private void addInfoForModule(IBuildContext context, ISourceModule module,
			TclModuleInfo info) {
		IPath modulePath = module.getPath();
		IEnvironment env = EnvironmentManager.getEnvironment(project);
		if (module.getResource() != null) {
			modulePath = module.getResource().getLocation();
			if (modulePath == null) {
				URI uri = module.getResource().getLocationURI();
				if (uri != null) {
					// Resolve URI using uri resolver to real file location
					modulePath = updateModulePath(modulePath, env, uri);
				}
			} else {
				// Try to resolve local path to some remote one.
				URI uri = module.getResource().getLocationURI();
				if (uri != null) {
					modulePath = updateModulePath(modulePath, env, uri);
				}
			}
		}
		TclModuleInfo mInfo = packageCollector
				.getCreateCurrentModuleInfo(module);
		if (info != null) {
			mInfo.getProvided().addAll(info.getProvided());
			mInfo.getRequired().addAll(info.getRequired());
			mInfo.getSourced().addAll(info.getSourced());
		}
		modules.add(new ModuleInfo(module.getElementName(), context
				.getLineTracker(), context.getProblemReporter(), mInfo,
				modulePath, module));
	}

	private IPath updateModulePath(IPath modulePath, IEnvironment env, URI uri) {
		URI[] uris = EnvironmentManager.resolve(uri);
		if (uris.length > 0) {
			IFileHandle file = env.getFile(uris[0]);
			if (file != null) {
				return file.getPath();
			}
		}
		return modulePath;
	}

	public void endBuild(IProgressMonitor monitor) {
		monitor.subTask(Messages.TclCheckBuilder_retrievePackages);
		// initialize manager caches after they are collected
		final Set<String> names = new HashSet<String>();
		final Set<String> autoNames = new HashSet<String>();
		InterpreterContainerHelper.getInterpreterContainerDependencies(project,
				names, autoNames);
		// process all modules
		final Set<String> newDependencies = new HashSet<String>();
		int remainingWork = modules.size();
		IEnvironment environment = EnvironmentManager
				.getEnvironment(this.project);
		for (ModuleInfo moduleInfo : modules) {
			monitor.subTask(NLS.bind(Messages.TclCheckBuilder_processing,
					moduleInfo.name, Integer.toString(remainingWork)));

			for (TclSourceEntry ref : moduleInfo.moduleInfo.getRequired()) {
				// Check for user override for selected source value
				EList<UserCorrection> corrections = moduleInfo.moduleInfo
						.getPackageCorrections();
				List<TclSourceEntry> toCheck = new ArrayList<TclSourceEntry>();

				for (UserCorrection userCorrection : corrections) {
					if (userCorrection.getOriginalValue()
							.equals(ref.getValue())) {
						for (String correction : userCorrection.getUserValue()) {
							TclSourceEntry to = TclPackagesFactory.eINSTANCE
									.createTclSourceEntry();
							to.setEnd(ref.getEnd());
							to.setStart(ref.getStart());
							to.setValue(correction);
							toCheck.add(to);
						}
						break;
					}
				}
				if (toCheck.isEmpty()) {
					final String resolved = variableResolver.resolve(ref
							.getValue());
					if (resolved == null || resolved.equals(ref.getValue())) {
						toCheck.add(ref);
					} else if (resolved != null) {
						TclSourceEntry to = TclPackagesFactory.eINSTANCE
								.createTclSourceEntry();
						to.setEnd(ref.getEnd());
						to.setStart(ref.getStart());
						to.setValue(resolved);
						toCheck.add(to);
					}
				}
				for (TclSourceEntry tclSourceEntry : toCheck) {
					checkPackage(tclSourceEntry, moduleInfo.reporter,
							moduleInfo.lineTracker, newDependencies, names,
							autoNames);
				}
			}
			// TODO: Add option to disable checking of sourced items from here.
			checkSources(environment, moduleInfo);

			--remainingWork;
		}
		// Collect other project items
		List<TclModuleInfo> list = packageCollector.getModules().get(project);
		if (list != null) {
			for (TclModuleInfo moduleInfo : list) {
				for (TclSourceEntry ref : moduleInfo.getRequired()) {
					// Check for user override for selected source value
					EList<UserCorrection> corrections = moduleInfo
							.getPackageCorrections();
					List<TclSourceEntry> toCheck = new ArrayList<TclSourceEntry>();

					for (UserCorrection userCorrection : corrections) {
						if (userCorrection.getOriginalValue().equals(
								ref.getValue())) {
							for (String correction : userCorrection
									.getUserValue()) {
								TclSourceEntry to = TclPackagesFactory.eINSTANCE
										.createTclSourceEntry();
								to.setEnd(ref.getEnd());
								to.setStart(ref.getStart());
								to.setValue(correction);
								toCheck.add(to);
							}
							break;
						}
					}
					if (toCheck.isEmpty()) {
						final String resolved = variableResolver.resolve(ref
								.getValue());
						if (resolved == null || resolved.equals(ref.getValue())) {
							toCheck.add(ref);
						} else {
							// resolved != null;
							TclSourceEntry to = TclPackagesFactory.eINSTANCE
									.createTclSourceEntry();
							to.setEnd(ref.getEnd());
							to.setStart(ref.getStart());
							to.setValue(resolved);
							toCheck.add(to);
						}
					}
					for (TclSourceEntry tclSourceEntry : toCheck) {
						checkPackage(tclSourceEntry, null, null,
								newDependencies, names, autoNames);
					}
				}
			}
		}

		// if (buildType != IBuildParticipantExtension.RECONCILE_BUILD
		// && isAutoAddPackages() && !newDependencies.isEmpty()) {
		// if (names.addAll(newDependencies)) {
		// InterpreterContainerHelper.setInterpreterContainerDependencies(
		// project, names);
		// }
		// }
		if (buildType != IBuildParticipantExtension.RECONCILE_BUILD) {
			List<TclModuleInfo> mods = packageCollector.getModules().get(
					project);
			List<TclModuleInfo> result = new ArrayList<TclModuleInfo>();
			// Clean modules without required items
			for (TclModuleInfo tclModuleInfo : mods) {
				// Clean old corrections.
				EList<UserCorrection> pkgCorrections = tclModuleInfo
						.getPackageCorrections();
				cleanCorrections(pkgCorrections, tclModuleInfo.getRequired());
				EList<UserCorrection> sourceCorrections = tclModuleInfo
						.getSourceCorrections();
				cleanCorrections(sourceCorrections, tclModuleInfo.getSourced());
				if (!(tclModuleInfo.getProvided().isEmpty()
						&& tclModuleInfo.getRequired().isEmpty()
						&& tclModuleInfo.getSourced().isEmpty()
						&& sourceCorrections.isEmpty() && pkgCorrections
						.isEmpty())) {
					result.add(tclModuleInfo);
				}
			}
			// Save packages provided by the project
			TclPackagesManager.setProjectModules(project.getElementName(),
					result);
			/*
			 * TODO: Replace with correct model update event here.
			 */
			InterpreterContainerHelper.setInterpreterContainerDependencies(
					project, names, newDependencies);

			// Do delta refresh
			try {
				ModelManager.getModelManager().getDeltaProcessor()
						.checkExternalChanges(new IModelElement[] { project },
								new NullProgressMonitor());
			} catch (ModelException e) {
				DLTKCore
						.error(
								Messages.PackageRequireSourceAnalyser_ModelUpdateFailure,
								e);
			}
		}
	}

	private void cleanCorrections(EList<UserCorrection> corrections,
			EList<TclSourceEntry> entries) {
		TOP: for (Iterator<UserCorrection> i = corrections.iterator(); i
				.hasNext();) {
			final UserCorrection userCorrection = i.next();
			for (TclSourceEntry tclSourceEntry : entries) {
				if (tclSourceEntry.getValue().equals(
						userCorrection.getOriginalValue())) {
					continue TOP;
				}
			}
			i.remove();
		}
	}

	private void checkSources(IEnvironment environment, ModuleInfo moduleInfo) {
		IPath moduleLocation = moduleInfo.moduleLocation;
		if (moduleLocation == null) {
			return;
		}
		IPath folder = moduleLocation.removeLastSegments(1);
		// Convert path to real path.
		for (TclSourceEntry source : moduleInfo.moduleInfo.getSourced()) {
			boolean needToAddCorrection = false;
			boolean isVariable = false;
			Set<IPath> sourcedPaths = new HashSet<IPath>();
			EList<UserCorrection> corrections = moduleInfo.moduleInfo
					.getSourceCorrections();
			for (UserCorrection userCorrection : corrections) {
				if (!userCorrection.isVariable()
						&& userCorrection.getOriginalValue().equals(
								source.getValue())) {
					for (String userValue : userCorrection.getUserValue()) {
						final IPath sourcedPath;
						if (environment.isLocal()) {
							sourcedPath = Path.fromOSString(userValue);
						} else {
							userValue = userValue.replace('\\', '/');
							sourcedPath = Path.fromPortableString(userValue);
						}
						sourcedPaths.add(sourcedPath);
					}
				}
			}
			if (sourcedPaths.isEmpty()) {
				needToAddCorrection = true;
				final String resolved = variableResolver.resolve(source
						.getValue());
				isVariable = resolved == null
						|| !resolved.contains(source.getValue());
				if (resolved != null) {
					final IPath sourcedPath = resolveSourceValue(folder,
							resolved, environment);
					if (sourcedPath != null) {
						sourcedPaths.add(sourcedPath);
					}
				}
			}
			for (IPath sourcedPath : sourcedPaths) {
				IFileHandle file = environment.getFile(sourcedPath);
				if (file != null) {
					if (!file.exists()) {
						reportSourceProblemCorrection(
								source,
								moduleInfo.reporter,
								NLS
										.bind(
												Messages.PackageRequireSourceAnalyser_CouldNotLocateSourcedFile,
												file.toOSString()), source
										.getValue(), moduleInfo.lineTracker);
					} else {
						if (file.isDirectory()) {
							reportSourceProblemCorrection(
									source,
									moduleInfo.reporter,
									Messages.PackageRequireSourceAnalyser_FolderSourcingNotSupported,
									source.getValue(), moduleInfo.lineTracker);
						} else
						// Add user correction if not specified yet.
						if (needToAddCorrection) {
							if (!isAutoAddPackages()) {
								reportSourceProblem(
										source,
										moduleInfo.reporter,
										Messages.PackageRequireSourceAnalyser_SourceNotAddedToBuildpath,
										source.getValue(),
										moduleInfo.lineTracker);
							} else {
								UserCorrection correction = TclPackagesFactory.eINSTANCE
										.createUserCorrection();
								correction.setOriginalValue(source.getValue());
								correction.getUserValue().add(file.toString());
								correction.setVariable(isVariable);
								corrections.add(correction);
							}
						}
					}
				}
			}
			if (sourcedPaths.isEmpty()) {
				if (!TclPackagesManager.isValidName(source.getValue())) {
					reportSourceProblemCorrection(
							source,
							moduleInfo.reporter,
							NLS
									.bind(
											Messages.PackageRequireSourceAnalyser_CouldNotLocateSourcedFileCorrectionRequired,
											source.getValue()), source
									.getValue(), moduleInfo.lineTracker);
				}
				// if (!corrections.isEmpty()) {
				// corrections.clear();
				// }
			}
		}
	}

	private IPath resolveSourceValue(IPath folder, String value,
			IEnvironment environment) {
		final IPath valuePath;
		value = value.trim();
		if (value.startsWith("\"") && value.endsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		}
		if (value.startsWith("'") && value.endsWith("'")) {
			value = value.substring(1, value.length() - 1);
		}
		if (environment.isLocal()) {
			valuePath = Path.fromOSString(value);
		} else {
			value = value.replace('\\', '/');
			valuePath = new Path(value);
		}
		if (valuePath.isAbsolute()) {
			return valuePath;
		} else if (TclPackagesManager.isValidName(value)) {
			return folder.append(valuePath);
		} else {
			return null;
		}
	}

	private void reportPackageProblem(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		if (reporter == null) {
			return;
		}
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_REQUIRED_PACKAGE, new String[] { pkgName },
				ProblemSeverities.Warning, pkg.getStart(), pkg.getEnd(),
				lineTracker.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportPackageProblemCorrection(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		if (reporter == null) {
			return;
		}
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_REQUIRED_PACKAGE_CORRECTION,
				new String[] { pkgName }, ProblemSeverities.Warning, pkg
						.getStart(), pkg.getEnd(), lineTracker
						.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportSourceProblem(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		if (reporter == null) {
			return;
		}
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_SOURCE, new String[] { pkgName },
				ProblemSeverities.Warning, pkg.getStart(), pkg.getEnd(),
				lineTracker.getLineNumberOfOffset(pkg.getStart())));
	}

	private void reportSourceProblemCorrection(TclSourceEntry pkg,
			IProblemReporter reporter, String message, String pkgName,
			ISourceLineTracker lineTracker) {
		reporter.reportProblem(new DefaultProblem(message,
				TclProblems.UNKNOWN_SOURCE_CORRECTION,
				new String[] { pkgName }, ProblemSeverities.Warning, pkg
						.getStart(), pkg.getEnd(), lineTracker
						.getLineNumberOfOffset(pkg.getStart())));
	}

	private void checkPackage(TclSourceEntry pkg, IProblemReporter reporter,
			ISourceLineTracker lineTracker, Set<String> newDependencies,
			Set<String> names, Set<String> autoNames) {
		final String packageName = pkg.getValue();

		List<TclModuleInfo> collected = packageCollector.getModules().get(
				project);
		if (collected != null) {
			if (isProvided(packageName, collected)) {
				return;
			}
		}
		if (providedByRequiredProjects != null) {
			if (isProvided(packageName, providedByRequiredProjects)) {
				return;
			}
		}

		if (!isKnownPackage(packageName)) {
			if (!TclPackagesManager.isValidName(packageName)) {
				reportPackageProblemCorrection(
						pkg,
						reporter,
						NLS
								.bind(
										Messages.PackageRequireSourceAnalyser_CouldNotDetectPackageCorrectionRequired,
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
			if (!names.contains(packageName)
					&& !autoNames.contains(packageName)) {
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

	private boolean isProvided(String packageName,
			Collection<TclModuleInfo> infos) {
		for (TclModuleInfo tclModuleInfo : infos) {
			EList<TclSourceEntry> provided = tclModuleInfo.getProvided();
			for (TclSourceEntry tclSourceEntry : provided) {
				if (tclSourceEntry.getValue().equals(packageName)) {
					IModelElement element = DLTKCore.create(tclModuleInfo
							.getHandle());
					// Check for file existence
					if (element != null && element.exists()) {
						return true; // Found provided package
					}
				}
			}
		}
		return false;
	}

	private boolean isKnownPackage(final String packageName) {
		for (TclPackageInfo info : this.knownInfos) {
			if (info.getName().equals(packageName)) {
				return true;
			}
		}
		return false;
	}

	private final boolean isAutoAddPackages() {
		return autoAddPackages;
	}

	@SuppressWarnings("unchecked")
	public DependencyResponse getDependencies(int buildType, Set localElements,
			Set externalElements, Set oldExternalFolders, Set externalFolders) {
		if (buildType == IBuildParticipantExtension.FULL_BUILD
				|| !oldExternalFolders.equals(externalFolders)) {
			return null;
			// TODO return DependencyResponse.FULL_EXTERNAL_BUILD;
		} else {
			return null;
		}
	}
}
