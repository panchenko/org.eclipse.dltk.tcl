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
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.core.environment.EnvironmentManager;
import org.eclipse.dltk.core.environment.IEnvironment;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerFavorite;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class TclCheckerConfigUtils {

	public static class InstanceConfigPair {
		public final CheckerInstance instance;
		public final CheckerConfig config;

		private InstanceConfigPair(CheckerInstance instance,
				CheckerConfig config) {
			this.instance = instance;
			this.config = config;
		}
	}

	/**
	 * Retrieves the TclChecker configuration for the specified project or
	 * <code>null</code> if not found (not configured) for the environment of
	 * this project.
	 * 
	 * @param project
	 * @return
	 */
	public static InstanceConfigPair getConfiguration(IScriptProject project) {
		if (project != null && project.getProject() != null) {
			return getConfiguration(project.getProject());
		} else {
			return null;
		}
	}

	/**
	 * Retrieves the TclChecker configuration for the specified project or
	 * <code>null</code> if not found (not configured) for the environment of
	 * this project.
	 * 
	 * @param project
	 * @return
	 */
	public static InstanceConfigPair getConfiguration(IProject project) {
		final String environmentId;
		if (project != null) {
			environmentId = EnvironmentManager.getEnvironmentId(project);
			if (environmentId == null) {
				return null;
			}
		} else {
			return null;
		}
		final String configurationContent = new PreferencesLookupDelegate(
				project).getString(TclCheckerPlugin.PLUGIN_ID,
				TclCheckerConstants.PREF_CONFIGURATION);
		final Resource resource = loadConfiguration(configurationContent);
		return findConfiguration(resource, environmentId);
	}

	private static CheckerInstance findInstance(final Resource resource,
			final String environmentId) {
		final List<CheckerInstance> instances = new ArrayList<CheckerInstance>();
		CheckerInstance favorite = null;
		for (EObject object : resource.getContents()) {
			if (object instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) object;
				if (environmentId.equals(instance.getEnvironmentId())) {
					instances.add(instance);
				}
			} else if (object instanceof CheckerFavorite) {
				favorite = ((CheckerFavorite) object).getEnvironments().get(
						environmentId);
			}
		}
		if (!instances.isEmpty()) {
			if (instances.size() > 1 && favorite != null
					&& instances.contains(favorite)) {
				return favorite;
			}
			return instances.get(0);
		}
		return null;
	}

	private static CheckerConfig findConfig(final Resource resource) {
		final List<CheckerConfig> configs = new ArrayList<CheckerConfig>();
		CheckerConfig favorite = null;
		for (EObject object : resource.getContents()) {
			if (object instanceof CheckerConfig) {
				configs.add((CheckerConfig) object);
			} else if (object instanceof CheckerFavorite) {
				favorite = ((CheckerFavorite) object).getConfig();
			}
		}
		final EList<Resource> resources = resource.getResourceSet()
				.getResources();
		for (Resource r : resources.toArray(new Resource[resources.size()])) {
			if (r != resource) {
				collectConfigurations(configs, r);
			}
		}
		if (!configs.isEmpty()) {
			if (configs.size() > 1 && favorite != null
					&& configs.contains(favorite)) {
				return favorite;
			}
			return configs.get(0);
		}
		return null;
	}

	private static InstanceConfigPair findConfiguration(
			final Resource resource, final String environmentId) {
		loadContributedConfigurations(resource.getResourceSet());
		final CheckerInstance instance = findInstance(resource, environmentId);
		if (instance != null) {
			final CheckerConfig config = findConfig(resource);
			if (config != null) {
				return new InstanceConfigPair(instance, config);
			}
		}
		return null;
	}

	public static InstanceConfigPair getConfiguration(IEnvironment environment) {
		if (environment == null) {
			return null;
		}
		final Resource resource = loadConfiguration(TclCheckerPlugin
				.getDefault().getPreferenceStore().getString(
						TclCheckerConstants.PREF_CONFIGURATION));
		return findConfiguration(resource, environment.getId());
	}

	public static Resource loadConfiguration(String content) {
		final ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(ConfigsPackage.eNS_URI,
				ConfigsPackage.eINSTANCE);
		Resource resource = resourceSet.createResource(URI
				.createURI(ConfigsPackage.eNS_URI));
		if (content != null && content.length() != 0) {
			try {
				final Map<String, Object> loadOptions = new HashMap<String, Object>();
				loadOptions.put(XMLResource.OPTION_RECORD_UNKNOWN_FEATURE,
						Boolean.TRUE);
				resource.load(new URIConverter.ReadableInputStream(content,
						ENCODING), loadOptions);
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		if (!resource.isLoaded()) {
			resource.getContents().clear();
		}
		return resource;
	}

	public static final String ENCODING = "UTF-8"; //$NON-NLS-1$

	public static String saveConfiguration(Resource resource)
			throws IOException {
		final StringWriter writer = new StringWriter();
		final Map<String, Object> saveOptions = new HashMap<String, Object>();
		saveOptions.put(XMLResource.OPTION_FORMATTED, Boolean.FALSE);
		resource.save(new URIConverter.WriteableOutputStream(writer, ENCODING),
				saveOptions);
		return writer.toString();
	}

	public static List<Resource> loadContributedConfigurations(
			ResourceSet resourceSet) {
		final IConfigurationElement[] elements = Platform
				.getExtensionRegistry().getConfigurationElementsFor(
						TclCheckerConstants.CONFIGURATION_EXTENSION_POINT_NAME);
		if (elements.length != 0) {
			final List<Resource> result = new ArrayList<Resource>();
			for (IConfigurationElement element : elements) {
				final String pathName = "/" + element.getContributor().getName() //$NON-NLS-1$
						+ "/" + element.getAttribute("resource"); //$NON-NLS-1$ //$NON-NLS-2$
				final URI uri = URI.createPlatformPluginURI(pathName, true);
				final Resource r = resourceSet.createResource(uri);
				try {
					r.load(null);
					result.add(r);
				} catch (IOException e) {
					// TODO log error
					resourceSet.getResources().remove(r);
				}
			}
			return result;
		} else {
			return Collections.emptyList();
		}
	}

	public static void collectConfigurations(List<CheckerConfig> instances,
			Resource r) {
		for (EObject object : r.getContents()) {
			if (object instanceof CheckerConfig) {
				instances.add((CheckerConfig) object);
			}
		}
	}

}
