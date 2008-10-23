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
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.core.environment.IFileHandle;

public abstract class AbstractOutputProcessor implements IOutputProcessor {

	private final IProgressMonitor monitor;
	private final List<ISourceModule> sourceModules = new ArrayList<ISourceModule>();
	private final Map<String, ISourceModule> pathToSource = new HashMap<String, ISourceModule>();

	public AbstractOutputProcessor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public IProgressMonitor getProgressMonitor() {
		return monitor;
	}

	public List<String> initialize(IEnvironment environment,
			List<ISourceModule> modules) {
		sourceModules.clear();
		pathToSource.clear();
		sourceModules.addAll(modules);
		final List<String> filenames = new ArrayList<String>();
		for (final ISourceModule module : modules) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}
			final IResource resource = module.getResource();
			final IPath location = resource.getLocation();
			final String loc;
			if (location == null) {
				final IFileHandle file = environment.getFile(resource
						.getLocationURI());
				if (file != null) {
					loc = file.toString();
				} else {
					loc = null;
				}
			} else {
				loc = location.toString();
			}
			if (loc != null) {
				pathToSource.put(loc, module);
				filenames.add(loc);
			}
		}
		return filenames;
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
	protected ISourceModule findSourceModule(IPath path) {
		final ISourceModule result = pathToSource.get(path.toString());
		if (result != null) {
			return result;
		}
		final String shortFileName = path.lastSegment();
		String fullPath = null;
		for (final String p : pathToSource.keySet()) {
			if (p.endsWith(shortFileName)) {
				if (fullPath != null) {
					return null;
				}
				fullPath = p;
			}
		}
		if (fullPath != null) {
			return pathToSource.get(fullPath);
		}
		return null;
	}

	protected int getModuleCount() {
		return sourceModules.size();
	}

	protected void subTask(String subTask) {
		monitor.subTask(subTask);
		monitor.worked(1);
	}

}
