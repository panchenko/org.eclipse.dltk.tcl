/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.itcl.internal.core.classes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.itcl.internal.core.IncrTCLPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class manage per project IncrTcl class name database. Hold classes for
 * all projects in workspace.
 */
public class IncrTclClassesManager implements IResourceChangeListener {
	private static final String CLASSES_TAG = "classes";
	private static final String PATH_ITCL_CLASSES = ".itcl_cl";
	private static final String CLASS_TAG = "class";
	private static IncrTclClassesManager sInstance;
	private boolean dirty = false;
	private int changes = 0;

	private Set classNames = new HashSet();

	public void clean() {
		this.classNames.clear();
	}

	public void add(String name) {
		classNames.add(name);
		dirty = true;
		changes++;
		if (changes > 10) {
			changes = 0;
			save();
		}
	}

	public void remove(String name) {
		classNames.remove(name);
	}

	public boolean isClass(String name) {
		return classNames.contains(name);
	}

	public void startup() {
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		load();
	}

	private synchronized void load() {
		IPath packages = IncrTCLPlugin.getDefault().getStateLocation().append(
				PATH_ITCL_CLASSES);
		File packagesFile = packages.toFile();
		if (packagesFile.exists()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder;
			try {
				builder = factory.newDocumentBuilder();
				Document document = builder.parse(new BufferedInputStream(
						new FileInputStream(packagesFile), 2048));
				populate(document.getDocumentElement());
			} catch (ParserConfigurationException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (SAXException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				if (DLTKCore.DEBUG) {
					e.printStackTrace();
				}
			}
		}
		dirty = false;
	}

	private synchronized void populate(Element documentElement) {
		NodeList childNodes = documentElement.getChildNodes();
		int length = childNodes.getLength();
		for (int i = 0; i < length; i++) {
			Node child = childNodes.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				if (child.getNodeName().equalsIgnoreCase(CLASS_TAG)) {
					Element el = (Element) child;
					String value = el.getAttribute("value");
					if (value.trim().length() > 0) {
						add(value);
					}
				}
			}
		}
	}

	private synchronized void save() {
		if (!dirty) {
			return;
		}
		dirty = false;
		IPath packages = IncrTCLPlugin.getDefault().getStateLocation().append(
				PATH_ITCL_CLASSES);
		File packagesFile = packages.toFile();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.newDocument();
			save(document);

			FileOutputStream fos = new FileOutputStream(packagesFile, false);
			BufferedOutputStream bos = new BufferedOutputStream(fos, 2048);

			TransformerFactory serFactory = TransformerFactory.newInstance();
			Transformer transformer = serFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$

			DOMSource source = new DOMSource(document);
			StreamResult outputTarget = new StreamResult(bos);
			transformer.transform(source, outputTarget);
			bos.close();
			fos.close();

		} catch (ParserConfigurationException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		} catch (TransformerException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
	}

	private void save(Document doc) {
		Element packages = doc.createElement(CLASSES_TAG); //$NON-NLS-1$
		doc.appendChild(packages);
		for (Iterator iterator = this.classNames.iterator(); iterator.hasNext();) {
			String value = (String) iterator.next();
			Element el = doc.createElement(CLASS_TAG);
			el.setAttribute("value", value);
			packages.appendChild(el);
		}

	}

	public void shutdown() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		save();
	}

	public static IncrTclClassesManager getDefault() {
		if (sInstance == null) {
			sInstance = new IncrTclClassesManager();
		}
		return sInstance;
	}

	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub

	}

	public boolean isClass(IProject project, String className) {
		return this.classNames.contains(className);
	}
}
