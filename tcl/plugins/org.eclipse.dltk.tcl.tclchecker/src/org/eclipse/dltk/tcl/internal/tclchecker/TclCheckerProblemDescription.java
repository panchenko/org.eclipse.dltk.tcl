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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.tcl.tclchecker.TclCheckerPlugin;
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

	public static final String MESSAGE_ID_SEPARATOR = "::"; //$NON-NLS-1$

	private static final Map<String, CheckerMessage> messageDefinitions = new HashMap<String, CheckerMessage>();
	private static final Map<String, CheckerMessage> altDefinitions = new HashMap<String, CheckerMessage>();
	private static final List<MessageGroup> messageGroups = new ArrayList<MessageGroup>();

	private static CheckerMessage defaultMessage = null;

	private static final Set<String> reportedIds = new HashSet<String>();

	/**
	 * @param problemId
	 * @return
	 */
	public static CheckerMessage getProblem(String problemId) {
		loadIfNeeded();
		CheckerMessage message = messageDefinitions.get(problemId);
		if (message == null) {
			message = altDefinitions.get(problemId);
			if (message == null) {
				if (reportedIds.add(problemId)) {
					TclCheckerPlugin.warn("Unknown messageId " + problemId); //$NON-NLS-1$
				}
				if (defaultMessage == null) {
					defaultMessage = MessagesFactory.eINSTANCE
							.createCheckerMessage();
					defaultMessage.setCategory(MessageCategory.WARNING);
					defaultMessage.setExplanation("Unknown problem"); //$NON-NLS-1$
					defaultMessage.setMessageId("Unknown"); //$NON-NLS-1$
				}
				message = defaultMessage;
			}
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
					final MessageGroup messageGroup = (MessageGroup) e;
					messageGroups.add(messageGroup);
					for (CheckerMessage message : messageGroup.getMessages()) {
						final String id = message.getMessageId();
						if (DLTKCore.DEBUG
								&& messageDefinitions.containsKey(id)) {
							TclCheckerPlugin.error("Duplicate message id " //$NON-NLS-1$
									+ id);
						} else {
							messageDefinitions.put(id, message);
						}
					}
				}
			}
			for (CheckerMessage message : messageDefinitions.values()) {
				final String id = message.getMessageId();
				final int index = id.indexOf(MESSAGE_ID_SEPARATOR);
				if (index >= 0) {
					final String shortId = id.substring(index
							+ MESSAGE_ID_SEPARATOR.length());
					if (!messageDefinitions.containsKey(shortId)) {
						if (DLTKCore.DEBUG
								&& altDefinitions.containsKey(shortId)) {
							TclCheckerPlugin.error("Duplicate message id " //$NON-NLS-1$
									+ shortId);
						} else {
							altDefinitions.put(shortId, message);
						}
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

	/**
	 * @return
	 */
	public static List<MessageGroup> getProblemGroups() {
		loadIfNeeded();
		return Collections.unmodifiableList(messageGroups);
	}

}
