/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.activestatedebugger;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IExternalSourceModule;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.IProjectFragment;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.EnvironmentPathUtils;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;
import org.eclipse.dltk.dbgp.IDbgpFeature;
import org.eclipse.dltk.dbgp.IDbgpSpawnpoint;
import org.eclipse.dltk.dbgp.commands.IDbgpSpawnpointCommands;
import org.eclipse.dltk.dbgp.exceptions.DbgpException;
import org.eclipse.dltk.debug.core.model.IScriptBreakpointPathMapper;
import org.eclipse.dltk.debug.core.model.IScriptDebugTarget;
import org.eclipse.dltk.debug.core.model.IScriptDebugThreadConfigurator;
import org.eclipse.dltk.debug.core.model.IScriptThread;
import org.eclipse.dltk.internal.debug.core.model.ScriptLineBreakpoint;
import org.eclipse.dltk.internal.debug.core.model.ScriptThread;
import org.eclipse.dltk.internal.debug.core.model.operations.DbgpDebugger;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationConfig;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.InstrumentationMode;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.ModelElementPattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.PatternListIO;

public class TclActiveStateDebugThreadConfigurator implements
		IScriptDebugThreadConfigurator {
	private boolean initialized = false;

	private final IScriptProject project;
	private final IPreferencesLookupDelegate delegate;

	/**
	 * @param project
	 * @param delegate
	 */
	public TclActiveStateDebugThreadConfigurator(IScriptProject project,
			IPreferencesLookupDelegate delegate) {
		this.project = project;
		this.delegate = delegate;
	}

	public void configureThread(DbgpDebugger engine, ScriptThread scriptThread) {
		if (initialized) {
			return;
		}
		initialized = true;
		try {
			IDbgpFeature tclFeature = engine.getFeature("tcl_instrument_set"); //$NON-NLS-1$
			if (tclFeature.isSupported()) {
				ActiveStateInstrumentCommands commands = new ActiveStateInstrumentCommands(
						engine.getSession().getCommunicator());
				initializeDebugger(commands);
			}
		} catch (DbgpException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private static class InstrumentationConfigurator {

		final IEnvironment environment;
		final Map<String, Boolean> patterns = new HashMap<String, Boolean>();

		public InstrumentationConfigurator(IEnvironment environment) {
			this.environment = environment;
		}

		/**
		 * @param pattern
		 * @return
		 */
		public void addWorkspace(IPath path, boolean isDirectory,
				boolean include) {
			final IResource resource = getWorkspaceRoot().findMember(path);
			if (resource != null) {
				final URI uri = resource.getLocationURI();
				final IFileHandle file = environment.getFile(uri);
				addFileHandle(file, isDirectory, include);
			}
		}

		/**
		 * @param environment
		 * @param pattern
		 * @return
		 */
		public void addExternal(IPath path, boolean isDirectory, boolean include) {
			addFileHandle(environment.getFile(EnvironmentPathUtils
					.getLocalPath(path)), isDirectory, include);
		}

		private void addFileHandle(final IFileHandle file, boolean isDirectory,
				boolean include) {
			if (file != null) {
				String path = file.getPath().toString();
				if (isDirectory) {
					path += "/*"; //$NON-NLS-1$
				}
				patterns.put(path, Boolean.valueOf(include));
			}
		}

		public void send(ActiveStateInstrumentCommands commands)
				throws DbgpException {
			final String[] paths = patterns.keySet().toArray(
					new String[patterns.size()]);
			Arrays.sort(paths);
			for (String path : paths) {
				final Boolean include = patterns.get(path);
				if (include != null && include.booleanValue()) {
					commands.instrumentInclude(path);
				} else {
					commands.instrumentExclude(path);
				}
			}
		}
	}

	private void initializeDebugger(ActiveStateInstrumentCommands commands)
			throws DbgpException {
		final Set<InstrumentationFeature> selectedFeatures = InstrumentationFeature
				.decode(getString(TclActiveStateDebuggerConstants.INSTRUMENTATION_FEATURES));
		for (InstrumentationFeature feature : InstrumentationFeature.values()) {
			commands.instrumentSet(feature, selectedFeatures.contains(feature));
		}
		final ErrorAction errorAction = ErrorAction
				.decode(getString(TclActiveStateDebuggerConstants.INSTRUMENTATION_ERROR_ACTION));
		if (errorAction != null) {
			commands.setErrorAction(errorAction);
		}
		final InstrumentationConfig config = PatternListIO
				.decode(getString(TclActiveStateDebuggerConstants.INSTRUMENTATION_PATTERNS));
		if (config != null && config.getMode() != InstrumentationMode.DEFAULT) {
			if (config.getMode() == InstrumentationMode.SOURCES) {
				// TODO InstrumentationMode.SOURCES
			} else {
				final IEnvironment environment = EnvironmentManager
						.getEnvironment(project);
				if (environment != null) {
					final InstrumentationConfigurator configurator = new InstrumentationConfigurator(
							environment);
					for (ModelElementPattern pattern : config
							.getModelElements()) {
						final IModelElement element = DLTKCore.create(pattern
								.getHandleIdentifier());
						if (element == null) {
							continue;
						}
						if (element instanceof ISourceModule) {
							final ISourceModule module = (ISourceModule) element;
							if (module instanceof IExternalSourceModule) {
								if (!module.isBuiltin())
									continue;
								configurator.addExternal(module.getPath(),
										false, pattern.isInclude());
							} else {
								configurator.addWorkspace(module.getPath(),
										false, pattern.isInclude());
							}
						} else if (element instanceof IScriptFolder) {
							final IScriptFolder folder = (IScriptFolder) element;
							if (folder.isReadOnly()) {
								configurator.addExternal(folder.getPath(),
										true, pattern.isInclude());
							} else {
								configurator.addWorkspace(folder.getPath(),
										true, pattern.isInclude());
							}
						} else if (element instanceof IProjectFragment) {
							final IProjectFragment fragment = (IProjectFragment) element;
							if (fragment.isExternal()) {
								if (fragment.isBuiltin())
									continue;
								configurator.addExternal(fragment.getPath(),
										true, pattern.isInclude());
							} else {
								configurator.addWorkspace(fragment.getPath(),
										true, pattern.isInclude());
							}
						} else if (element instanceof IScriptProject) {
							final IScriptProject project = (IScriptProject) element;
							configurator.addWorkspace(project.getPath(), true,
									pattern.isInclude());
						}
					}
					configurator.send(commands);
				}
			}
		}
	}

	protected static IWorkspaceRoot getWorkspaceRoot() {
		return ResourcesPlugin.getWorkspace().getRoot();
	}

	private String getString(final String key) {
		return delegate.getString(TclActiveStateDebuggerPlugin.PLUGIN_ID, key);
	}

	private List<IMarker> loadSpawnpoints() throws CoreException {
		final IWorkspaceRoot root = getWorkspaceRoot();
		final IMarker[] markers = root.findMarkers(
				TclActiveStateDebuggerConstants.SPAWNPOINT_MARKER_TYPE, true,
				IResource.DEPTH_INFINITE);
		if (markers.length == 0) {
			return Collections.emptyList();
		}
		final IEnvironment environment = EnvironmentManager
				.getEnvironment(project);
		if (environment == null) {
			return Arrays.asList(markers);
		}
		final Map<IProject, Boolean> projectStatus = new HashMap<IProject, Boolean>();
		final List<IMarker> result = new ArrayList<IMarker>();
		for (IMarker marker : markers) {
			final IProject project = marker.getResource().getProject();
			Boolean status = projectStatus.get(project);
			if (status == null) {
				if (environment.equals(EnvironmentManager
						.getEnvironment(project))) {
					status = Boolean.TRUE;
				} else {
					status = Boolean.FALSE;
				}
				projectStatus.put(project, status);
			}
			if (status.booleanValue()) {
				result.add(marker);
			}
		}
		return result;
	}

	public void initializeBreakpoints(IScriptThread thread) {
		try {
			final List<IMarker> spawnpoints = loadSpawnpoints();
			final IDbgpSpawnpointCommands commands = (IDbgpSpawnpointCommands) thread
					.getDbgpSession().get(IDbgpSpawnpointCommands.class);
			final IScriptBreakpointPathMapper pathMapper = ((IScriptDebugTarget) thread
					.getDebugTarget()).getPathMapper();
			for (final IMarker spawnpoint : spawnpoints) {
				final URI uri = ScriptLineBreakpoint.makeUri(new Path(
						spawnpoint.getResource().getLocationURI().getPath()));
				try {
					final int lineNumber = spawnpoint.getAttribute(
							IMarker.LINE_NUMBER, 0) + 1;
					final IDbgpSpawnpoint p = commands.setSpawnpoint(pathMapper
							.map(uri), lineNumber, true);
					// TODO save spawnpoint id - need SpawnpointManager?
				} catch (DbgpException e) {
					// TODO save spawnpoint error - need SpawnpointManager?
					TclActiveStateDebuggerPlugin.warn(e);
				}
			}
		} catch (CoreException e) {
			TclActiveStateDebuggerPlugin.warn(e);
		}
	}
}
