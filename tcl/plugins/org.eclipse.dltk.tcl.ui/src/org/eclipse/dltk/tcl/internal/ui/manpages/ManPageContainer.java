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
package org.eclipse.dltk.tcl.internal.ui.manpages;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;

/**
 * @since 2.0
 */
public class ManPageContainer {

	private final Resource resource;

	public ManPageContainer() {
		this(new XMIResourceImpl());
	}

	public ManPageContainer(Resource resource) {
		this.resource = resource;
	}

	public Resource getResource() {
		return resource;
	}

	public List<Documentation> getDocumentations() {
		final EList<EObject> contents = resource.getContents();
		final List<Documentation> result = new ArrayList<Documentation>(
				contents.size());
		for (EObject object : contents) {
			if (object instanceof Documentation) {
				result.add((Documentation) object);
			}
		}
		return result;
	}

	public Documentation createDocumentation() {
		Documentation documentation = ManpagesFactory.eINSTANCE
				.createDocumentation();
		resource.getContents().add(documentation);
		return documentation;
	}

	public boolean isEmpty() {
		return resource.getContents().isEmpty();
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

}
