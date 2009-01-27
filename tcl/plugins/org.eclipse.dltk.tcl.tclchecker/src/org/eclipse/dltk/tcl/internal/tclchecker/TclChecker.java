/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.compiler.CharOperation;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.v5.Checker5OutputProcessor;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.dltk.validators.core.AbstractExternalValidator;
import org.eclipse.dltk.validators.core.CommandLine;
import org.eclipse.dltk.validators.core.ISourceModuleValidator;
import org.eclipse.dltk.validators.core.IValidatorOutput;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclChecker extends AbstractExternalValidator implements
		ISourceModuleValidator, ITclCheckerReporter, ILineTrackerFactory {
	private static final String PATTERN_TXT = "pattern.txt"; //$NON-NLS-1$

	protected IMarker reportErrorProblem(IResource resource,
			TclCheckerProblem problem, int start, int end,
			Map<String, Object> attributes) throws CoreException {
		return reportError(resource, problem.getLineNumber(), start, end,
				problem.getMessage(), attributes);
	}

	protected IMarker reportWarningProblem(IResource resource,
			TclCheckerProblem problem, int start, int end,
			Map<String, Object> attributes) throws CoreException {
		return reportWarning(resource, problem.getLineNumber(), start, end,
				problem.getMessage(), attributes);
	}

	private final IPreferenceStore store;
	private final IEnvironment environment;

	public TclChecker(IEnvironment environment) {
		this(TclCheckerPlugin.getDefault().getPreferenceStore(), environment);
	}

	public TclChecker(IPreferenceStore store, IEnvironment environment) {
		Assert.isNotNull(store, "store cannot be null"); //$NON-NLS-1$
		this.store = store;
		this.environment = environment;
	}

	private boolean canCheck() {
		return TclCheckerHelper.canExecuteTclChecker(store, environment);
	}

	public void check(final List<ISourceModule> sourceModules,
			IValidatorOutput console, IProgressMonitor monitor) {
		if (!canCheck()) {
			throw new IllegalStateException(
					Messages.TclChecker_cannot_be_executed);
		}
		if (monitor == null) {
			monitor = new NullProgressMonitor();
		}

		final IOutputProcessor processor;
		if (TclCheckerConstants.VERSION_5.equals(store
				.getString(TclCheckerConstants.PREF_VERSION))) {
			processor = new Checker5OutputProcessor(monitor, console, this,
					this);
		} else {
			processor = new Checker4OutputProcessor(monitor, console, this);
		}
		List<String> filenames = processor.initialize(environment,
				sourceModules);
		if (filenames.isEmpty()) {
			monitor.done();
			return;
		}
		final CommandLine cmdLine = new CommandLine();
		if (!TclCheckerHelper.buildCommandLine(store, cmdLine, environment,
				sourceModules.get(0).getScriptProject(), console)) {
			console.println(Messages.TclChecker_path_not_specified);
			return;
		}
		monitor.beginTask(Messages.TclChecker_executing,
				sourceModules.size() * 2 + 1);
		try {
			final IExecutionEnvironment execEnvironment = (IExecutionEnvironment) environment
					.getAdapter(IExecutionEnvironment.class);
			final IDeployment deployment = execEnvironment.createDeployment();
			try {
				final IPath pattern = deployFileList(deployment, filenames);
				if (pattern == null) {
					console.println(Messages.TclChecker_errorWritingFileList);
					return;
				}
				cmdLine.add("-@"); //$NON-NLS-1$
				cmdLine.add(deployment.getFile(pattern).toOSString());
				monitor.subTask(Messages.TclChecker_launching);
				executeProcess(processor, execEnvironment, cmdLine.toArray());
			} catch (CoreException e) {
				TclCheckerPlugin.log(IStatus.ERROR,
						Messages.TclChecker_cannot_be_executed, e);
			} finally {
				deployment.dispose();
			}
		} finally {
			monitor.done();
		}
	}

	public void executeProcess(final IOutputProcessor processor,
			final IExecutionEnvironment execEnvironment,
			final String[] commandLine) throws CoreException {
		if (DLTKCore.DEBUG) {
			processor.processLine(TextUtils.join(commandLine, ' '));
		}
		final Process process = execEnvironment.exec(commandLine, null,
				prepareEnvironment(execEnvironment));
		try {
			final TclCheckerErrorReader errorReader = new TclCheckerErrorReader(
					process.getErrorStream(), processor);
			errorReader.start();
			final TclCheckerOutputReader outputReader = new TclCheckerOutputReader(
					process.getInputStream(), processor);
			outputReader.start();
			final IProgressMonitor monitor = processor.getProgressMonitor();
			monitor.worked(1);
			while (outputReader.isAlive()) {
				try {
					outputReader.join(500);
				} catch (InterruptedException e) {
					// ignore
				}
				if (monitor.isCanceled()) {
					break;
				}
			}
		} finally {
			process.destroy();
		}
	}

	private String[] prepareEnvironment(IExecutionEnvironment execEnvironment) {
		Map<?, ?> map = execEnvironment.getEnvironmentVariables(false);
		String[] env = new String[map.size()];
		int i = 0;
		for (Iterator<?> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) map.get(key);
			env[i] = key + "=" + value; //$NON-NLS-1$
			++i;
		}
		return env;
	}

	private IPath deployFileList(IDeployment deployment, List<String> arguments) {
		ByteArrayOutputStream baros = new ByteArrayOutputStream();
		try {
			for (String path : arguments) {
				/*
				 * FIXME filename encoding on the remote system should be
				 * configurable
				 */
				baros.write((path + "\n").getBytes()); //$NON-NLS-1$
			}
			baros.close();
		} catch (IOException e) {
			// should not happen
		}
		try {
			return deployment.add(
					new ByteArrayInputStream(baros.toByteArray()), PATTERN_TXT);
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				TclCheckerPlugin.log(IStatus.ERROR,
						Messages.TclChecker_filelist_deploy_failed, e);
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	protected String getMarkerType() {
		return TclCheckerMarker.TYPE;
	}

	public IStatus validate(ISourceModule[] modules, IValidatorOutput console,
			IProgressMonitor monitor) {
		final List<ISourceModule> elements = new ArrayList<ISourceModule>();
		for (int i = 0; i < modules.length; i++) {
			final ISourceModule module = modules[i];
			final IResource resource = module.getResource();
			if (resource != null) {
				clean(resource);
				elements.add(module);
			}
		}
		if (elements.isEmpty()) {
			return Status.OK_STATUS;
		}
		check(elements, console, monitor);
		return Status.OK_STATUS;
	}

	private final Map<ISourceModule, ISourceLineTracker> lineTrackers = new HashMap<ISourceModule, ISourceLineTracker>();

	/**
	 * @param resource
	 * @param problem
	 * @param start
	 * @param end
	 * @throws CoreException
	 */
	public void report(ISourceModule module, TclCheckerProblem problem)
			throws CoreException {
		final ISourceLineTracker lineTracker = getLineTracker(module);
		final int start;
		final int end;
		final CoordRange bounds = problem.getRange();
		if (bounds == null) {
			final ISourceRange lineBounds = lineTracker
					.getLineInformation(problem.getLineNumber() - 1);
			start = lineBounds.getOffset();
			end = start + lineBounds.getLength();
		} else {
			start = calculateOffset(lineTracker, bounds.getStart());
			end = calculateOffset(lineTracker, bounds.getEnd());
		}
		final IResource resource = module.getResource();
		if (resource == null) {
			return;
		}
		if (problem.isError()) {
			reportErrorProblem(resource, problem, start, end, problem
					.getAttributes());
		} else if (problem.isWarning())
			reportWarningProblem(resource, problem, start, end, problem
					.getAttributes());
	}

	/**
	 * @param lineTracker
	 * @param start
	 * @return
	 */
	public int calculateOffset(ISourceLineTracker lineTracker, Coord coord) {
		final ISourceRange range = lineTracker.getLineInformation(coord
				.getLine() - 1);
		return range.getOffset() + coord.getColumn();
	}

	public ISourceLineTracker getLineTracker(ISourceModule module) {
		ISourceLineTracker lineTracker = lineTrackers.get(module);
		if (lineTracker == null) {
			char[] source;
			try {
				source = module.getSourceAsCharArray();
				if (source == null) {
					source = CharOperation.NO_CHAR;
				}
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
				source = CharOperation.NO_CHAR;
			}
			lineTracker = TextUtils.createLineTracker(source);
			lineTrackers.put(module, lineTracker);
		}
		return lineTracker;
	}

}
