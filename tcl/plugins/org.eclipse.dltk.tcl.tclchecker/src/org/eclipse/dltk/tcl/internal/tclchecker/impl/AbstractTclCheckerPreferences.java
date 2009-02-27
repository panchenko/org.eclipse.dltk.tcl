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
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
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

	/*
	 * @see ITclCheckerPreferences#getConfigurations()
	 */
	public List<CheckerConfig> getCommonConfigurations() {
		final List<CheckerConfig> instances = new ArrayList<CheckerConfig>();
		TclCheckerConfigUtils.collectConfigurations(instances, resource);
		for (Resource r : contributedResources) {
			TclCheckerConfigUtils.collectConfigurations(instances, r);
		}
		return Collections.unmodifiableList(instances);
	}

	/*
	 * @see ITclCheckerPreferences#getEnvironment(java.lang.String)
	 */
	public List<CheckerInstance> getInstances() {
		List<CheckerInstance> instances = new ArrayList<CheckerInstance>();
		for (EObject object : resource.getContents()) {
			if (object instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) object;
				instances.add(instance);
			}
		}
		return Collections.unmodifiableList(instances);
	}

	/*
	 * @see ITclCheckerPreferences#newInstance()
	 */
	public CheckerInstance newInstance() {
		final CheckerInstance instance = ConfigsFactory.eINSTANCE
				.createCheckerInstance();
		resource.getContents().add(instance);
		return instance;
	}

	/*
	 * @see ITclCheckerPreferences#removeInstance(CheckerInstance)
	 */
	public boolean removeInstance(CheckerInstance instance) {
		return resource.getContents().remove(instance);
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
