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
package org.eclipse.dltk.tcl.ui.manpages.impl;

import java.io.IOException;
import java.io.StringWriter;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.dltk.tcl.ui.manpages.Documentation;
import org.eclipse.dltk.tcl.ui.manpages.ManPageResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * @since 2.0
 */
public class ManPageResourceImpl extends XMIResourceImpl implements
		ManPageResource {

	private class ContentList<E extends EObject> extends AbstractList<E> {

		private final Class<E> clazz;

		public ContentList(Class<E> clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		@Override
		public E get(int index) {
			if (index < 0) {
				throw new IndexOutOfBoundsException();
			}
			for (EObject object : getContents()) {
				if (clazz.isInstance(object)) {
					if (index == 0) {
						return (E) object;
					}
					--index;
				}
			}
			throw new IndexOutOfBoundsException();
		}

		@Override
		public int size() {
			int size = 0;
			for (EObject object : getContents()) {
				if (clazz.isInstance(object)) {
					++size;
				}
			}
			return size;
		}

		@Override
		public boolean add(E e) {
			getContents().add(e);
			return true;
		}

	}

	public static final String ENCODING = "UTF-8"; //$NON-NLS-1$

	public <T extends EObject> List<T> select(Class<T> clazz) {
		return new ContentList<T>(clazz);
	}

	public Documentation findDefault() {
		for (Documentation documentation : getDocumentations()) {
			if (documentation.isDefault()) {
				return documentation;
			}
		}
		return null;
	}

	public Documentation findByName(String name) {
		if (name != null) {
			for (Documentation documentation : getDocumentations()) {
				if (name.equals(documentation.getName())) {
					return documentation;
				}
			}
		}
		return null;
	}

	public Documentation findById(String id) {
		if (id != null) {
			for (Documentation documentation : getDocumentations()) {
				if (id.equals(documentation.getId())) {
					return documentation;
				}
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return getContents().isEmpty();
	}

	public void verify() {
		for (Documentation documentation : getDocumentations()) {
			final String id = documentation.getId();
			if (id == null || id.length() == 0) {
				documentation.setId(EcoreUtil.generateUUID());
			}
		}
		checkDefault();
	}

	public List<Documentation> getDocumentations() {
		return select(Documentation.class);
	}

	public void checkDefault() {
		final List<Documentation> documentations = getDocumentations();
		final List<Documentation> defaults = new ArrayList<Documentation>();
		for (Documentation doc : documentations) {
			if (doc.isDefault()) {
				defaults.add(doc);
			}
		}
		if (defaults.size() == 1) {
			return;
		}
		if (documentations.isEmpty()) {
			return;
		}
		if (defaults.isEmpty()) {
			documentations.get(0).setDefault(true);
		} else {
			for (int i = 1; i < defaults.size(); ++i) {
				defaults.get(i).setDefault(false);
			}
		}
	}

	@Override
	public void save(Map<?, ?> options) throws IOException {
		final StringWriter writer = new StringWriter();
		final Map<Object, Object> saveOptions = new HashMap<Object, Object>();
		if (options != null) {
			saveOptions.putAll(options);
		}
		saveOptions.put(XMLResource.OPTION_FORMATTED, Boolean.FALSE);
		saveOptions.put(XMLResource.OPTION_ENCODING, ENCODING);
		save(new URIConverter.WriteableOutputStream(writer, ENCODING),
				saveOptions);
		TclUI.getDefault().getPreferenceStore().setValue(
				TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS,
				writer.toString());
	}

}
