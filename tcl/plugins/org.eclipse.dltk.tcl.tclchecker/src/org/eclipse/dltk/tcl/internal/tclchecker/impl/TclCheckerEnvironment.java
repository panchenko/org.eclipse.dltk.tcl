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
package org.eclipse.dltk.tcl.internal.tclchecker.impl;

import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.ITclCheckerEnvironment;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerHelper;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerPlugin;
import org.eclipse.jface.preference.IPreferenceStore;

public class TclCheckerEnvironment implements ITclCheckerEnvironment {

	private final IEnvironment environment;

	/**
	 * @param environment
	 */
	public TclCheckerEnvironment(IEnvironment environment) {
		this.environment = environment;
	}

	/**
	 * @return
	 */
	private IPreferenceStore getStore() {
		return TclCheckerPlugin.getDefault().getPreferenceStore();
	}

	public String getExecutablePath() {
		return TclCheckerHelper.getPaths(getStore()).get(environment);
	}

	public List<String> getPCXPaths() {
		return TclCheckerHelper.getPcxPaths(getStore()).get(environment);
	}

	public boolean isUsePCXPaths() {
		final Map<IEnvironment, String> values = TclCheckerHelper
				.getNoPCX(getStore());
		return !Boolean.valueOf(values.get(environment));
	}

	public void setExecutablePath(String path) {
		final IPreferenceStore store = getStore();
		Map<IEnvironment, String> paths = TclCheckerHelper.getPaths(store);
		final String oldPath = paths.get(environment);
		if (path != null) {
			if (path.equals(oldPath)) {
				return;
			}
			paths.put(environment, path);
		} else {
			if (oldPath == null) {
				return;
			}
			paths.remove(environment);
		}
		TclCheckerHelper.setPaths(store, paths);
	}

	public void setPCXPaths(List<String> paths) {
		final IPreferenceStore store = getStore();
		final Map<IEnvironment, List<String>> allPaths = TclCheckerHelper
				.getPcxPaths(store);
		allPaths.put(environment, paths);
		TclCheckerHelper.setPcxPaths(store, allPaths);
	}

	public void setUsePCXPaths(boolean value) {
		final IPreferenceStore store = getStore();
		final Map<IEnvironment, String> values = TclCheckerHelper
				.getNoPCX(store);
		values.put(environment, Boolean.valueOf(!value).toString());
		TclCheckerHelper.setNoPCX(store, values);
	}

}
