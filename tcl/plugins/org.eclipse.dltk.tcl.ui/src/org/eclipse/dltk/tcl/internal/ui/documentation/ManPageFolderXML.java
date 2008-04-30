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
package org.eclipse.dltk.tcl.internal.ui.documentation;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

public class ManPageFolderXML {

	public static List read(String data) {
		if (data == null || data.length() == 0) {
			return new ArrayList();
		}
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

		List folders = new ArrayList();

		NodeList list = config.getChildNodes();
		int length = list.getLength();
		for (int i = 0; i < length; ++i) {
			Node node = list.item(i);
			short type = node.getNodeType();
			if (type == Node.ELEMENT_NODE
					&& node.getNodeName().equalsIgnoreCase(LOCATION_ELEMENT)) {
				Element location = (Element) node;
				String path = location.getAttribute(LOCATION_PATH_ATTRIBUTE);
				ManPageFolder folder = new ManPageFolder(path);
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
						folder.addPage(kw, file);
					}
				}
				folders.add(folder);
			}
		}

		return folders;
	}

	public static String write(List folders) {
		if (folders == null)
			return null;
		try {
			// Create the Document and the top-level node
			DocumentBuilderFactory dfactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder;
			docBuilder = dfactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();
			Element topElement = doc.createElement(TOP_ELEMENT);
			doc.appendChild(topElement);
			for (Iterator i = folders.iterator(); i.hasNext();) {
				ManPageFolder f = (ManPageFolder) i.next();
				Element location = doc.createElement(LOCATION_ELEMENT);
				topElement.appendChild(location);
				location.setAttribute(LOCATION_PATH_ATTRIBUTE, f.getPath());
				for (Iterator j = f.getPages().entrySet().iterator(); j
						.hasNext();) {
					final Map.Entry entry = (Map.Entry) j.next();
					String name = (String) entry.getKey();
					String file = (String) entry.getValue();
					Element page = doc.createElement(PAGE_ELEMENT);
					location.appendChild(page);
					page.setAttribute(PAGE_KEYWORD_ATTRIBUTE, name);
					page.setAttribute(PAGE_FILE_ATTRIBUTE, file);
				}
			}
			final StringWriter output = new StringWriter();
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			// transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// //$NON-NLS-1$
			transformer.transform(new DOMSource(doc), new StreamResult(output));
			return output.toString();
		} catch (Exception e) {
			TclUI.error("Error serializing " //$NON-NLS-1$
					+ TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS);
			return null;
		}
	}

	private static final String TOP_ELEMENT = "manPages"; //$NON-NLS-1$

	private static final String LOCATION_ELEMENT = "location"; //$NON-NLS-1$

	private static final String LOCATION_PATH_ATTRIBUTE = "path"; //$NON-NLS-1$

	private static final String PAGE_ELEMENT = "page"; //$NON-NLS-1$

	private static final String PAGE_FILE_ATTRIBUTE = "file"; //$NON-NLS-1$

	private static final String PAGE_KEYWORD_ATTRIBUTE = "keyword"; //$NON-NLS-1$

}
