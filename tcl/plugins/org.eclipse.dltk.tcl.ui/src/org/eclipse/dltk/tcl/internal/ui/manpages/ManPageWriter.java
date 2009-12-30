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

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.tcl.ui.TclPreferenceConstants;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.emf.ecore.xmi.XMLResource;

/**
 * @since 2.0
 */
public class ManPageWriter {

	/**
	 * @since 2.0
	 */
	public static String write(ManPageContainer input) {
		if (input == null || input.isEmpty()) {
			return ManPageReader.getDefault();
		}
		try {
			final StringWriter writer = new StringWriter();
			final Map<String, Object> saveOptions = new HashMap<String, Object>();
			saveOptions.put(XMLResource.OPTION_FORMATTED, Boolean.FALSE);
			saveOptions.put(XMLResource.OPTION_ENCODING, ManPageReader.ENCODING);
			input.getResource().save(
					new URIConverter.WriteableOutputStream(writer, ManPageReader.ENCODING),
					saveOptions);
			return writer.toString();
		} catch (Exception e) {
			TclUI.error("Error serializing " //$NON-NLS-1$
					+ TclPreferenceConstants.DOC_MAN_PAGES_LOCATIONS);
			return null;
		}
	}

}
