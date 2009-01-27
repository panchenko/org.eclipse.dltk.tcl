/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.tclchecker.model.messages.CheckerMessage;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageCategory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessageGroup;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesFactory;
import org.eclipse.dltk.tcl.tclchecker.model.messages.MessagesPackage;
import org.eclipse.dltk.tcl.tclchecker.model.messages.util.MessagesResourceFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;

public class TclCheckerProblemDescription {

	private static final Map<String, CheckerMessage> messageDefinitions = new HashMap<String, CheckerMessage>();

	private static CheckerMessage defaultMessage = null;

	/**
	 * @param problemId
	 * @return
	 */
	public static CheckerMessage getProblem(String problemId) {
		loadIfNeeded();
		CheckerMessage message = messageDefinitions.get(problemId);
		if (message == null) {
			if (defaultMessage == null) {
				defaultMessage = MessagesFactory.eINSTANCE
						.createCheckerMessage();
				defaultMessage.setCategory(MessageCategory.ERROR);
				defaultMessage.setExplanation("Unknown problem"); //$NON-NLS-1$
				defaultMessage.setMessageId("Unknown"); //$NON-NLS-1$
			}
			message = defaultMessage;
		}
		return message;
	}

	private static void loadIfNeeded() {
		if (messageDefinitions.isEmpty()) {
			load();
		}
	}

	private static void load() {
		try {
			ResourceSet resourceSet = new ResourceSetImpl();
			resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
					.put(Resource.Factory.Registry.DEFAULT_EXTENSION,
							new MessagesResourceFactoryImpl());
			resourceSet.getPackageRegistry().put(MessagesPackage.eNS_URI,
					MessagesPackage.eINSTANCE);
			Resource r = resourceSet
					.getResource(
							URI
									.createPlatformPluginURI(
											TclCheckerPlugin.PLUGIN_ID
													+ "/resources/tclchecker-messages.xml", true), true); //$NON-NLS-1$
			for (EObject e : r.getContents()) {
				if (e instanceof MessageGroup) {
					for (CheckerMessage message : ((MessageGroup) e)
							.getMessages()) {
						messageDefinitions.put(message.getMessageId(), message);
					}
				}
			}
		} catch (Exception e) {
			TclCheckerPlugin.error(e);
		}
	}

	public static List<String> getProblemIdentifiers() {
		loadIfNeeded();
		return new ArrayList<String>(messageDefinitions.keySet());
	}

}
