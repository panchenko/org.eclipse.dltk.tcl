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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;

public class EnvironmentContainer {

	private boolean initialized = false;
	private final Map<String, IEnvironment> environments = new HashMap<String, IEnvironment>();

	public void initialize() {
		if (!initialized) {
			initialized = true;
			for (IEnvironment environment : EnvironmentManager
					.getEnvironments()) {
				environments.put(environment.getId(), environment);
			}
		}
	}

	/**
	 * @return
	 */
	public Set<String> getEnvironmentIds() {
		return environments.keySet();
	}

	/**
	 * @param environmentId
	 * @return
	 */
	public IEnvironment get(String environmentId) {
		return environments.get(environmentId);
	}

	/**
	 * @param environmentId
	 * @return
	 */
	public String getName(String environmentId) {
		final IEnvironment environment = environments.get(environmentId);
		return environment != null ? environment.getName() : environmentId;
	}

}
