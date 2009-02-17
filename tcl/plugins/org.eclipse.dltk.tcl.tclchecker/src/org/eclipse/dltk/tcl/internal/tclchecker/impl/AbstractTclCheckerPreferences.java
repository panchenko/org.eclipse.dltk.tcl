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
package org.eclipse.dltk.tcl.internal.tclchecker.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerConfigUtils;
import org.eclipse.dltk.tcl.tclchecker.ITclCheckerPreferences;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;

public abstract class AbstractTclCheckerPreferences implements
		ITclCheckerPreferences {

	private Resource resource;
	private List<Resource> contributedResources;

	protected void initialize() {
		this.resource = TclCheckerConfigUtils
				.loadConfiguration(readConfiguration());
		contributedResources = TclCheckerConfigUtils
				.loadContributedConfigurations(this.resource.getResourceSet());
	}

	/**
	 * @return
	 */
	protected abstract String readConfiguration();

	protected abstract void writeConfiguration(String value);

	protected abstract ISingleEnvironmentPredicate createEnvironmentPredicate(
			String environmentId);

	/*
	 * @see ITclCheckerPreferences#getConfigurations()
	 */
	public List<ConfigInstance> getConfigurations() {
		final List<ConfigInstance> instances = new ArrayList<ConfigInstance>();
		TclCheckerConfigUtils.collectConfigurations(instances, resource);
		for (Resource r : contributedResources) {
			TclCheckerConfigUtils.collectConfigurations(instances, r);
		}
		return Collections.unmodifiableList(instances);
	}

	/*
	 * @see ITclCheckerPreferences#getEnvironment(java.lang.String)
	 */
	public CheckerInstance getEnvironment(String environmentId) {
		final ISingleEnvironmentPredicate predicate = createEnvironmentPredicate(environmentId);
		for (EObject object : resource.getContents()) {
			if (object instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) object;
				if (predicate.evaluate(instance.getEnvironmentId())) {
					return instance;
				}
			}
		}
		final CheckerInstance instance = ConfigsFactory.eINSTANCE
				.createCheckerInstance();
		instance.setEnvironmentId(predicate.getEnvironmentId());
		resource.getContents().add(instance);
		return instance;
	}

	/*
	 * @see ITclCheckerPreferences#newConfiguration()
	 */
	public ConfigInstance newConfiguration() {
		final ConfigInstance instance = ConfigsFactory.eINSTANCE
				.createConfigInstance();
		resource.getContents().add(instance);
		return instance;
	}

	/*
	 * @see ITclCheckerPreferences#removeConfiguration(ConfigInstance)
	 */
	public boolean removeConfiguration(ConfigInstance config) {
		return resource.getContents().remove(config);

	}

	/*
	 * @see org.eclipse.dltk.tcl.tclchecker.ITclCheckerPreferences#save()
	 */
	public void save() throws CoreException {
		try {
			writeConfiguration(TclCheckerConfigUtils
					.saveConfiguration(resource));
		} catch (IOException e) {
			throw new CoreException(new Status(IStatus.ERROR,
					TclCheckerPlugin.PLUGIN_ID, e.getMessage(), e));
		}
	}

}
