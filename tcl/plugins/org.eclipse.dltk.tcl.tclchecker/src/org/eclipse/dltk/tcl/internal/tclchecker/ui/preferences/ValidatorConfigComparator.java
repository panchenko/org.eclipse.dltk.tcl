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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

import java.util.Comparator;

import org.eclipse.dltk.tcl.internal.tclchecker.IContributedResource;
import org.eclipse.dltk.validators.configs.ValidatorConfig;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

public class ValidatorConfigComparator implements Comparator<ValidatorConfig> {

	public int compare(ValidatorConfig config1, ValidatorConfig config2) {
		if (config1.isReadOnly() != config2.isReadOnly()) {
			return config1.isReadOnly() ? -1 : +1;
		}
		if (config1.isReadOnly() && config1.eResource() != null
				&& config2.eResource() != null) {
			if (config1.eResource() != config2.eResource()) {
				int order1 = 0;
				int order2 = 0;
				if (config1.eResource() instanceof IContributedResource) {
					order1 = ((IContributedResource) config1.eResource())
							.getOrder();
				}
				if (config2.eResource() instanceof IContributedResource) {
					order2 = ((IContributedResource) config2.eResource())
							.getOrder();
				}
				if (order1 != order2) {
					return order1 - order2;
				}
				return String.CASE_INSENSITIVE_ORDER.compare(config1
						.eResource().getURI().toString(), config2.eResource()
						.getURI().toString());
			} else {
				final EList<EObject> contents = config1.eResource()
						.getContents();
				return contents.indexOf(config1) - contents.indexOf(config2);
			}
		} else {
			return String.CASE_INSENSITIVE_ORDER.compare(config1.getName(),
					config2.getName());
		}
	}

}
