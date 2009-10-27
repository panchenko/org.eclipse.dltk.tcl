/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerPattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.ContainerType;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationContentProvider;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.LibraryContainerElement;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.PackagePattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.SourcePattern;
import org.eclipse.dltk.tcl.core.TclPackagesManager;
import org.eclipse.dltk.tcl.core.packages.TclPackageInfo;

/**
 * @since 2.0
 */
public class InstrumentationConfigProcessor extends InstrumentationSetup {

	private final IInterpreterInstall install;
	private final IScriptProject project;

	/**
	 * 
	 */
	public InstrumentationConfigProcessor(IProject project) {
		super(EnvironmentManager.getEnvironment(project));
		this.project = DLTKCore.create(project);
		IInterpreterInstall ii;
		try {
			ii = ScriptRuntime.getInterpreterInstall(this.project);
		} catch (CoreException e) {
			TclActiveStateDebuggerPlugin.warn(e);
			ii = null;
		}
		this.install = ii;
	}

	/**
	 * @param project
	 */
	public InstrumentationConfigProcessor(IScriptProject project,
			IInterpreterInstall install) {
		super(EnvironmentManager.getEnvironment(project));
		this.project = project;
		this.install = install;
	}

	/**
	 * @param config
	 */
	public void configure(InstrumentationConfig config) {
		if (environment == null) {
			return;
		}
		final InstrumentationMode mode = InstrumentationUtils.getMode(config);
		if (mode == InstrumentationMode.DEFAULT) {
			return;
		}
		final InstrumentationContentProvider provider = new InstrumentationContentProvider();
		final Set<IModelElement> processed = new HashSet<IModelElement>();
		final Set<IScriptProject> projects = new HashSet<IScriptProject>();
		InstrumentationUtils.collectProjects(projects, project);
		final Map<ContainerType, Boolean> containerIncludes = new HashMap<ContainerType, Boolean>();
		if (mode == InstrumentationMode.SOURCES) {
			for (IScriptProject project : projects) {
				collect(provider, processed, project);
				addProject(project, true);
			}
		} else {
			for (Pattern pattern : config.getModelElements()) {
				if (pattern instanceof ModelElementPattern) {
					final IModelElement element = DLTKCore
							.create(((ModelElementPattern) pattern)
									.getHandleIdentifier());
					if (element == null) {
						continue;
					}
					collect(provider, processed, element);
					if (element instanceof ISourceModule) {
						addSourceModule((ISourceModule) element, pattern
								.isInclude());
					} else if (element instanceof IScriptFolder) {
						addScriptFolder((IScriptFolder) element, pattern
								.isInclude());
					} else if (element instanceof IProjectFragment) {
						final IProjectFragment fragment = (IProjectFragment) element;
						addProjectFragment(fragment, pattern.isInclude());
					} else if (element instanceof IScriptProject) {
						addProject((IScriptProject) element, pattern
								.isInclude());
					}
				} else if (pattern instanceof PackagePattern) {
					if (install != null) {
						final PackagePattern pp = (PackagePattern) pattern;
						final TclPackageInfo info = TclPackagesManager
								.getPackageInfo(install, pp.getPackageName(),
										true);
						if (info != null) {
							for (String source : info.getSources()) {
								addFileHandle(environment.getFile(new Path(
										source)), false, pattern.isInclude());
							}
						}
					}
				} else if (pattern instanceof SourcePattern) {
					final SourcePattern sp = (SourcePattern) pattern;
					addFileHandle(environment.getFile(new Path(sp
							.getSourcePath())), false, pattern.isInclude());
				} else if (pattern instanceof ContainerPattern) {
					final ContainerPattern c = (ContainerPattern) pattern;
					containerIncludes.put(c.getType(), pattern.isInclude());
				}
			}
			for (IScriptProject project : projects) {
				if (processed.add(project)) {
					addProject(project, false);
				}
			}
		}
		// process remaining external libraries
		for (IProjectFragment fragment : LibraryContainerElement
				.collectExternalFragments(projects)) {
			if (processed.add(fragment)) {
				addProjectFragment(fragment, isIncluded(containerIncludes,
						ContainerType.LIBRARIES));
			}
		}
	}

	private boolean isIncluded(Map<ContainerType, Boolean> containerIncludes,
			ContainerType containerType) {
		final Boolean value = containerIncludes.get(containerType);
		return value != null && value.booleanValue();
	}

	/**
	 * @param provider
	 * @param processed
	 * @param element
	 */
	private static void collect(InstrumentationContentProvider provider,
			Set<IModelElement> processed, IModelElement element) {
		if (processed.add(element)) {
			Object parent = provider.getParent(element);
			if (parent != null && parent instanceof IModelElement) {
				collect(provider, processed, (IModelElement) parent);
			}
		}
	}

}
