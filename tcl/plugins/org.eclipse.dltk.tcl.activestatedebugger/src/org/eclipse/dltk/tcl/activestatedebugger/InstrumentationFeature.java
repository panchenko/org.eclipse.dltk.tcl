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
package org.eclipse.dltk.tcl.activestatedebugger;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.compiler.util.Util;

public enum InstrumentationFeature {
	AUTOLOAD("autoload", Messages.instrumentation_autoload_caption), //$NON-NLS-1$
	DYNPROC("dynproc", Messages.instrumentation_dynproc_caption), //$NON-NLS-1$
	ITCL("itcl", Messages.instrumentation_itcl_caption), //$NON-NLS-1$
	TCLX("tclx", Messages.instrumentation_tclx_caption), //$NON-NLS-1$
	EXPECT("expect", Messages.instrumentation_expect_caption); //$NON-NLS-1$

	private final String value;
	private final String caption;

	InstrumentationFeature(String value, String caption) {
		this.value = value;
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public String getCaption() {
		return caption;
	}

	public static Set<InstrumentationFeature> decode(String iFeatures) {
		if (iFeatures == null) {
			return Collections.emptySet();
		}
		final Set<InstrumentationFeature> result = new HashSet<InstrumentationFeature>();
		final String[] parts = iFeatures.split(","); //$NON-NLS-1$
		for (int i = 0; i < parts.length; ++i) {
			try {
				result.add(valueOf(parts[i]));
			} catch (IllegalArgumentException e) {
				// ignore
			}
		}
		return result;
	}

	/**
	 * @param features
	 * @return
	 */
	public static String encode(Set<InstrumentationFeature> features) {
		if (features == null) {
			return Util.EMPTY_STRING;
		}
		final StringBuilder sb = new StringBuilder();
		for (InstrumentationFeature feature : features) {
			if (sb.length() != 0) {
				sb.append(',');
			}
			sb.append(feature.name());
		}
		return sb.toString();
	}
}
