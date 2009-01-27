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
package org.eclipse.dltk.tcl.tclchecker.model.messages;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class MessageTest {

	public static void main(String[] args) {
		CheckerMessage message = MessagesFactory.eINSTANCE
				.createCheckerMessage();
		message.setCategory(MessageCategory.ERROR);
		message.setMessageId("argAfterArgs");
		message
				.setExplanation("An argument has been specified after the args keyword in a procedure argument list. The args argument is treated like a normal parameter and does not collect the remaining parameters into a single list.");

		CheckerMessage message1 = MessagesFactory.eINSTANCE
				.createCheckerMessage();
		message1.setCategory(MessageCategory.WARNING);
		message1.setMessageId("argsNotDefault");
		message1
				.setExplanation("The args keyword cannot be initialized to contain a default value. Although the Tcl interpreter does not complain about this usage, the default value is ignored.");

		MessageGroup group = MessagesFactory.eINSTANCE.createMessageGroup();
		group.setId("a");
		group.getMessages().add(message);
		MessageGroup group1 = MessagesFactory.eINSTANCE.createMessageGroup();
		group1.setId("v");
		group1.getMessages().add(message1);

		StringWriter stringWriter = new StringWriter();
		save(stringWriter, new EObject[] { group, group1 });
		System.out.println(stringWriter.toString());
	}

	/**
	 * @param stringWriter
	 * @param objects
	 */
	private static void save(Writer writer, EObject[] objects) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
						new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(MessagesPackage.eNS_URI,
				MessagesPackage.eINSTANCE);
		XMLResource resource = (XMLResource) resourceSet.createResource(URI
				.createURI(MessagesPackage.eNS_URI));

		for (EObject object : objects) {
			resource.getContents().add(object);
		}

		resource.setEncoding("UTF-8");
		try {
			Map<String, Object> saveOptions = new HashMap<String, Object>();
			saveOptions.put(XMLResource.OPTION_DECLARE_XML, Boolean.FALSE);
			saveOptions.put(XMLResource.OPTION_FORMATTED, Boolean.TRUE);
			// saveOptions.put(XMLResource.OPTION_EXTENDED_META_DATA,
			// XBuilderUtil
			// .getExtendedMetadata());
			resource.save(new URIConverter.WriteableOutputStream(writer,
					resource.getEncoding()), saveOptions);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
