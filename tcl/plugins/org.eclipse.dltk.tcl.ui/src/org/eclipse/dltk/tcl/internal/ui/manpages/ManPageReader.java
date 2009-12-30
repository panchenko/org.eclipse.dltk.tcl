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
package org.eclipse.dltk.tcl.internal.ui.manpages;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @since 2.0
 */
public class ManPageReader {

	public static String getDefault() {
		return Util.EMPTY_STRING;
	}

	/**
	 * @since 2.0
	 */
	public static ManPageContainer read(String data) {
		if (data == null || data.length() == 0) {
			return new ManPageContainer();
		} else if (data.contains("xmlns:xmi") && data.contains("xmi:version")) { //$NON-NLS-1$ //$NON-NLS-2$
			return readXMI(data);
		} else {
			return readXML(data);
		}
	}

	private static ManPageContainer readXMI(String data) {
		final Resource resource = new XMIResourceImpl();
		try {
			resource.load(new URIConverter.ReadableInputStream(data, ENCODING),
					null);
		} catch (IOException e) {
			TclUI.error("Error parsing " //$NON-NLS-1$
					+ TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS, e);
		}
		final ManPageContainer container = new ManPageContainer(resource);
		container.checkDefault();
		return container;
	}

	private static ManPageContainer readXML(String data) {
		// Wrapper the stream for efficient parsing

		// Do the parsing and obtain the top-level node
		Element config = null;
		try {
			DocumentBuilder parser = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder();
			parser.setErrorHandler(new DefaultHandler());
			config = parser.parse(new InputSource(new StringReader(data)))
					.getDocumentElement();
		} catch (Exception e) {
			TclUI.error("Error parsing " //$NON-NLS-1$
					+ TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS, e);
			return null;
		}
		if (!config.getNodeName().equalsIgnoreCase(TOP_ELEMENT)) {
			TclUI.error(TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS
					+ " - bad top level node"); //$NON-NLS-1$
			return null;
		}
		final ManPageContainer input = new ManPageContainer();
		final Documentation documentation = input.createDocumentation();
		documentation.setName("Tcl Documentation");
		documentation.setDefault(true);

		NodeList list = config.getChildNodes();
		int length = list.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			short type = node.getNodeType();
			if (type == Node.ELEMENT_NODE
					&& node.getNodeName().equalsIgnoreCase(LOCATION_ELEMENT)) {
				Element location = (Element) node;
				String path = location.getAttribute(LOCATION_PATH_ATTRIBUTE);
				ManPageFolder folder = ManpagesFactory.eINSTANCE
						.createManPageFolder();
				folder.setPath(path);
				NodeList locationChilds = location.getChildNodes();
				int pages = locationChilds.getLength();
				for (int j = 0; j < pages; ++j) {
					node = locationChilds.item(j);
					type = node.getNodeType();
					if (type == Node.ELEMENT_NODE
							&& node.getNodeName()
									.equalsIgnoreCase(PAGE_ELEMENT)) {
						Element word = (Element) node;
						String kw = word.getAttribute(PAGE_KEYWORD_ATTRIBUTE);
						String file = word.getAttribute(PAGE_FILE_ATTRIBUTE);
						folder.getKeywords().put(kw, file);
					}
				}
				documentation.getFolders().add(folder);
			}
		}
		return input;
	}

	static final String ENCODING = "UTF-8"; //$NON-NLS-1$

	private static final String TOP_ELEMENT = "manPages"; //$NON-NLS-1$

	private static final String LOCATION_ELEMENT = "location"; //$NON-NLS-1$

	private static final String LOCATION_PATH_ATTRIBUTE = "path"; //$NON-NLS-1$

	private static final String PAGE_ELEMENT = "page"; //$NON-NLS-1$

	private static final String PAGE_FILE_ATTRIBUTE = "file"; //$NON-NLS-1$

	private static final String PAGE_KEYWORD_ATTRIBUTE = "keyword"; //$NON-NLS-1$

}
