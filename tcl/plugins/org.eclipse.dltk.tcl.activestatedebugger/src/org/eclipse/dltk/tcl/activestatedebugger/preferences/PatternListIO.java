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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class PatternListIO {

	private static final String ENCODING = "UTF-8"; //$NON-NLS-1$

	public static List<Pattern> decode(String value) {
		final List<Pattern> result = new ArrayList<Pattern>();
		if (value != null && value.length() != 0) {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
					.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
							new XMIResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(PreferencesPackage.eNS_URI,
					PreferencesPackage.eINSTANCE);
			XMLResource resource = (XMLResource) resourceSet.createResource(URI
					.createURI(PreferencesPackage.eNS_URI));
			try {
				resource.load(new URIConverter.ReadableInputStream(value,
						ENCODING), null);
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
			for (EObject object : resource.getContents()) {
				if (object instanceof Pattern) {
					result.add((Pattern) object);
				}
			}
		}
		return result;
	}

	public static String encode(List<Pattern> patterns) {
		if (patterns.isEmpty()) {
			return Util.EMPTY_STRING;
		}
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(PreferencesPackage.eNS_URI,
				PreferencesPackage.eINSTANCE);
		XMLResource resource = (XMLResource) resourceSet.createResource(URI
				.createURI(PreferencesPackage.eNS_URI));
		for (Pattern pattern : patterns) {
			resource.getContents().add(pattern);
		}
		resource.setEncoding(ENCODING);
		StringWriter stringWriter = new StringWriter();
		try {
			Map<String, Object> saveOptions = new HashMap<String, Object>();
			saveOptions.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
			saveOptions.put(XMLResource.OPTION_FORMATTED, Boolean.FALSE);
			resource.save(new URIConverter.WriteableOutputStream(stringWriter,
					resource.getEncoding()), saveOptions);
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return stringWriter.toString();
	}

}
