/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IExecutionEnvironment;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;

public class TclChecker {
	private static final String PATTERN_TXT = "pattern.txt"; //$NON-NLS-1$

	private static final String CHECKING = "checking:"; //$NON-NLS-1$

	private static final String SCANNING = "scanning:"; //$NON-NLS-1$

	protected static IMarker reportErrorProblem(IResource resource,
			TclCheckerProblem problem, int start, int end) throws CoreException {

		return TclCheckerMarker.setMarker(resource, problem.getLineNumber(),
				start, end, problem.getDescription().getMessage(),
				IMarker.SEVERITY_ERROR, IMarker.PRIORITY_NORMAL);
	}

	protected static IMarker reportWarningProblem(IResource resource,
			TclCheckerProblem problem, int start, int end) throws CoreException {

		return TclCheckerMarker.setMarker(resource, problem.getLineNumber(),
				start, end, problem.getDescription().getMessage(),
				IMarker.SEVERITY_WARNING, IMarker.PRIORITY_NORMAL);
	}

	private ISourceModule checkingModule;
	private IPreferenceStore store;

	public TclChecker(IPreferenceStore store) {
		if (store == null) {
			throw new NullPointerException("store cannot be null"); //$NON-NLS-1$
		}

		this.store = store;
	}

	public boolean canCheck(IEnvironment environment) {
		return TclCheckerHelper.canExecuteTclChecker(store, environment);
	}

	public void check(final List sourceModules, IProgressMonitor monitor,
			OutputStream consoleStream, IEnvironment environment) {
		if (!canCheck(environment)) {
			throw new IllegalStateException(
					Messages.TclChecker_cannot_be_executed);
		}

		List arguments = new ArrayList();
		Map pathToSource = new HashMap();
		Map moduleToCodeModel = new HashMap();
		for (Iterator iterator = sourceModules.iterator(); iterator.hasNext();) {
			ISourceModule module = (ISourceModule) iterator.next();
			try {
				String source = module.getSource();
				if (source.length() == 0) {
					continue;
				}
				CodeModel codeModel = new CodeModel(source);
				moduleToCodeModel.put(module, codeModel);
			} catch (ModelException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			IPath location = module.getResource().getLocation();
			String loc = null;
			if (location == null) {
				URI locationURI = module.getResource().getLocationURI();
				loc = environment.getFile(locationURI).toString();
			} else {
				loc = location.toString();
			}
			pathToSource.put(loc, module);
			arguments.add(loc);
		}
		if (arguments.isEmpty()) {
			if (monitor != null) {
				monitor.done();
			}
			return;
		}
		final PrintStream console = consoleStream != null ? new PrintStream(
				consoleStream, true) : null;
		List cmdLine = new ArrayList();
		if (!TclCheckerHelper
				.passOriginalArguments(store, cmdLine, environment)) {
			if (console != null) {
				console.println(Messages.TclChecker_path_not_specified);
			}
		}
		IExecutionEnvironment execEnvironment = (IExecutionEnvironment) environment
				.getAdapter(IExecutionEnvironment.class);
		IDeployment deployment = execEnvironment.createDeployment();

		final IPath pattern = deployFileList(deployment, arguments);
		if (pattern == null) {
			return;
		}

		cmdLine.add("-@"); //$NON-NLS-1$
		cmdLine.add(deployment.getFile(pattern).toOSString());
		Process process;
		BufferedReader input = null;

		int scanned = 0;
		int checked = 0;

		if (monitor == null)
			monitor = new NullProgressMonitor();

		monitor.beginTask(Messages.TclChecker_executing,
				sourceModules.size() * 2 + 1);

		Map map = execEnvironment.getEnvironmentVariables(false);

		String[] env = new String[map.size()];
		int i = 0;
		for (Iterator iterator = map.keySet().iterator(); iterator.hasNext();) {
			String key = (String) iterator.next();
			String value = (String) map.get(key);
			env[i] = key + "=" + value; //$NON-NLS-1$
			++i;
		}
		try {
			monitor.subTask(Messages.TclChecker_launching);
			process = execEnvironment.exec((String[]) cmdLine
					.toArray(new String[cmdLine.size()]), null, env);

			monitor.worked(1);

			input = new BufferedReader(new InputStreamReader(process
					.getInputStream()));

			String line;
			CodeModel model = null;
			while ((line = input.readLine()) != null) {
				if (console != null) {
					console.println(line);
				}
				TclCheckerProblem problem = TclCheckerHelper.parseProblem(line);
				if (monitor.isCanceled()) {
					process.destroy();
					return;
				}
				if (line.startsWith(SCANNING)) {
					String fileName = line.substring(SCANNING.length() + 1)
							.trim();
					fileName = Path.fromOSString(fileName).lastSegment();
					monitor.subTask(NLS.bind(Messages.TclChecker_scanning,
							fileName, String.valueOf(sourceModules.size()
									- scanned)));
					monitor.worked(1);
					scanned++;
				}
				if (line.startsWith(CHECKING)) {
					String fileName = line.substring(CHECKING.length() + 1)
							.trim();
					final IPath path = Path.fromOSString(fileName);
					final String checkingFile = path.toString();
					checkingModule = (ISourceModule) pathToSource
							.get(checkingFile);
					if (checkingModule == null) {
						checkingModule = findSourceModule(pathToSource, path);
					}
					model = (CodeModel) moduleToCodeModel.get(checkingModule);

					monitor.subTask(NLS.bind(Messages.TclChecker_checking, path
							.lastSegment(), String.valueOf(sourceModules.size()
							- checked)));
					monitor.worked(1);
					checked++;
				}
				if (problem != null && checkingModule != null) {
					if (model != null) {
						TclCheckerProblemDescription desc = problem
								.getDescription();

						int[] bounds = model
								.getBounds(problem.getLineNumber() - 1);

						IResource res = checkingModule.getResource();
						if (TclCheckerProblemDescription.isError(desc
								.getCategory())) {
							reportErrorProblem(res, problem, bounds[0],
									bounds[1]);
						} else if (TclCheckerProblemDescription.isWarning(desc
								.getCategory()))
							reportWarningProblem(res, problem, bounds[0],
									bounds[1]);
					}
				}
			}
			StringBuffer errorMessage = new StringBuffer();
			// We need also read errors.
			input = new BufferedReader(new InputStreamReader(process
					.getErrorStream()));

			while ((line = input.readLine()) != null) {
				// lines.add(line);
				if (console != null) {
					console.println(line);
				}
				errorMessage.append(line).append('\n');
				if (monitor.isCanceled()) {
					process.destroy();
					return;
				}
			}
			if (errorMessage.length() > 0) {
				TclCheckerPlugin.log(IStatus.ERROR,
						Messages.TclChecker_execution_error
								+ errorMessage.toString());
			}
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} finally {
			monitor.done();
			deployment.dispose();
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					if (DLTKCore.DEBUG) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Finds the source module comparing short file name with names in the Map.
	 * Returns {@link ISourceModule} if single match is found or
	 * <code>null</code> if there are no matches or if there are multiple
	 * matches.
	 * 
	 * @param pathToSource
	 * @param path
	 * @return
	 */
	private ISourceModule findSourceModule(Map pathToSource, IPath path) {
		final String shortFileName = path.lastSegment();
		String fullPath = null;
		for (Iterator iterator = pathToSource.keySet().iterator(); iterator
				.hasNext();) {
			final String p = (String) iterator.next();
			if (p.endsWith(shortFileName)) {
				if (fullPath != null) {
					return null;
				}
				fullPath = p;
			}
		}
		if (fullPath != null) {
			return (ISourceModule) pathToSource.get(fullPath);
		}
		return null;
	}

	private IPath deployFileList(IDeployment deployment, List arguments) {
		ByteArrayOutputStream baros = new ByteArrayOutputStream();
		try {
			for (Iterator arg = arguments.iterator(); arg.hasNext();) {
				String path = (String) arg.next();
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

}
