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
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerConfig;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerEnvironmentInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.CheckerInstance;
import org.eclipse.dltk.tcl.tclchecker.model.configs.ConfigsPackage;
import org.eclipse.dltk.validators.core.ValidatorRuntime;
import org.eclipse.dltk.validators.internal.core.ValidatorsCore;
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

	public static interface ICheckerPredicate {
		boolean evaluate(CheckerInstance instance);
	}

	public static final ICheckerPredicate ALL = new ICheckerPredicate() {

		public boolean evaluate(CheckerInstance instance) {
			return true;
		}

	};

	public static final ICheckerPredicate AUTO = new ICheckerPredicate() {

		public boolean evaluate(CheckerInstance instance) {
			return instance.isAutomatic();
		}

	};

	public static class ValidatorInstanceRef {

		public ValidatorInstanceRef(
				CheckerEnvironmentInstance environmentInstance,
				CheckerConfig config) {
			this.environmentInstance = environmentInstance;
			this.config = config;
		}

		public final CheckerEnvironmentInstance environmentInstance;
		public final CheckerConfig config;
	}

	public static class ValidatorInstanceResponse {

		public ValidatorInstanceResponse(IEnvironment environment,
				Resource resource) {
			this.environment = environment;
			this.resource = resource;
		}

		public final IEnvironment environment;
		public final Resource resource;
		public final List<ValidatorInstanceRef> instances = new ArrayList<ValidatorInstanceRef>();

		public ResourceSet getResourceSet() {
			return resource.getResourceSet();
		}

		/**
		 * @return
		 */
		public String getEnvironmentId() {
			return environment.getId();
		}

		public boolean isEmpty() {
			return instances.isEmpty();
		}

		private List<CheckerConfig> commonConfigurations = null;

		public List<CheckerConfig> getCommonConfigurations() {
			if (commonConfigurations != null) {
				return commonConfigurations;
			}
			commonConfigurations = new ArrayList<CheckerConfig>();
			TclCheckerConfigUtils.collectConfigurations(commonConfigurations,
					resource);
			final EList<Resource> resources = resource.getResourceSet()
					.getResources();
			for (Resource r : resources.toArray(new Resource[resources.size()])) {
				if (r != resource) {
					TclCheckerConfigUtils.collectConfigurations(
							commonConfigurations, r);
				}
			}
			return commonConfigurations;
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
	public static ValidatorInstanceResponse getConfiguration(
			IScriptProject project, ICheckerPredicate predicate) {
		if (project != null && project.getProject() != null) {
			return getConfiguration(project.getProject(), predicate);
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
	public static ValidatorInstanceResponse getConfiguration(IProject project,
			ICheckerPredicate predicate) {
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
				project).getString(ValidatorsCore.PLUGIN_ID,
				ValidatorRuntime.PREF_CONFIGURATION);
		final Resource resource = loadConfiguration(configurationContent);
		final ValidatorInstanceResponse response = new ValidatorInstanceResponse(
				EnvironmentManager.getEnvironmentById(environmentId), resource);
		findConfiguration(response, predicate);
		return response;
	}

	private static void findConfiguration(ValidatorInstanceResponse response,
			ICheckerPredicate predicate) {
		loadContributedConfigurations(response.getResourceSet());
		for (EObject object : response.resource.getContents()) {
			if (object instanceof CheckerInstance) {
				final CheckerInstance instance = (CheckerInstance) object;
				if (predicate.evaluate(instance)) {
					final CheckerEnvironmentInstance environmentInstance = instance
							.findEnvironment(response.getEnvironmentId());
					if (environmentInstance != null
							&& environmentInstance.getExecutablePath() != null
							&& environmentInstance.getExecutablePath().trim()
									.length() != 0) {
						CheckerConfig favorite = instance.getFavorite();
						if (favorite == null
								&& !instance.getConfigs().isEmpty()) {
							favorite = instance.getConfigs().get(0);
						}
						if (favorite == null
								&& !response.getCommonConfigurations()
										.isEmpty()) {
							favorite = response.getCommonConfigurations()
									.get(0);
						}
						if (favorite != null) {
							response.instances.add(new ValidatorInstanceRef(
									environmentInstance, favorite));
						}
					}
				}
			}
		}
	}

	public static ValidatorInstanceResponse getConfiguration(
			IEnvironment environment, ICheckerPredicate predicate) {
		if (environment == null) {
			return null;
		}
		final Resource resource = loadConfiguration(ValidatorsCore.getDefault()
				.getPluginPreferences().getString(
						ValidatorRuntime.PREF_CONFIGURATION));
		final ValidatorInstanceResponse response = new ValidatorInstanceResponse(
				environment, resource);
		findConfiguration(response, predicate);
		return response;
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
