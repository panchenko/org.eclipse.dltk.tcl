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
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IPreferencesLookupDelegate;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.environment.EnvironmentManager;
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
import org.eclipse.dltk.tcl.activestatedebugger.preferences.ExternalPattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.Pattern;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.PatternListIO;
import org.eclipse.dltk.tcl.activestatedebugger.preferences.WorkspacePattern;

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
		IEnvironment environment = EnvironmentManager.getEnvironment(project);
		final List<Pattern> patterns = PatternListIO
				.decode(getString(TclActiveStateDebuggerConstants.INSTRUMENTATION_PATTERNS));
		if (!patterns.isEmpty()) {
			for (Pattern pattern : patterns) {
				if (pattern instanceof WorkspacePattern) {
					String[] stringPatterns = resolveWorkspacePattern(
							environment, (WorkspacePattern) pattern);
					if (pattern.isInclude()) {
						commands.instrumentInclude(stringPatterns);
					} else {
						commands.instrumentExclude(stringPatterns);
					}
				} else if (pattern instanceof ExternalPattern) {
					String[] stringPatterns = resolveExternalPattern(
							environment, (ExternalPattern) pattern);
					if (pattern.isInclude()) {
						commands.instrumentInclude(stringPatterns);
					} else {
						commands.instrumentExclude(stringPatterns);
					}
				}
			}
		}
	}

	/**
	 * @param environment
	 * @param pattern
	 * @return
	 */
	private String[] resolveExternalPattern(IEnvironment environment,
			ExternalPattern pattern) {
		return resolveFileHandle(environment
				.getFile(new Path(pattern.getPath())));
	}

	/**
	 * @param pattern
	 * @return
	 */
	private String[] resolveWorkspacePattern(IEnvironment environment,
			WorkspacePattern pattern) {
		final IResource resource = ResourcesPlugin.getWorkspace().getRoot()
				.findMember(new Path(pattern.getPath()));
		if (resource != null) {
			final URI uri = resource.getLocationURI();
			final IFileHandle file = environment.getFile(uri);
			return resolveFileHandle(file);
		}
		return CharOperation.NO_STRINGS;
	}

	private String[] resolveFileHandle(final IFileHandle file) {
		if (file != null) {
			final String path = new Path(file.toOSString()).toString();
			return new String[] { !file.isDirectory() ? path : path + "/*" }; //$NON-NLS-1$
		} else {
			return CharOperation.NO_STRINGS;
		}
	}

	private String getString(final String key) {
		return delegate.getString(TclActiveStateDebuggerPlugin.PLUGIN_ID, key);
	}

	private List<IMarker> loadSpawnpoints() throws CoreException {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
