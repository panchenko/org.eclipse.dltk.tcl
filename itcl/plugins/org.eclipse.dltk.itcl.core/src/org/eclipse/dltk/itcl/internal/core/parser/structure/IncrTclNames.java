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
package org.eclipse.dltk.itcl.internal.core.parser.structure;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.tcl.structure.ITclTypeHandler;

public class IncrTclNames {

	private static final String ATTR = IncrTclNames.class.getName();

	public static IncrTclNames get(ITclModelBuildContext context) {
		return (IncrTclNames) context.getAttribute(ATTR);
	}

	public static IncrTclNames create(ITclModelBuildContext context) {
		IncrTclNames names = get(context);
		if (names == null) {
			names = new IncrTclNames();
			context.setAttribute(ATTR, names);
		}
		return names;
	}
	
	static class IncrTclTypeInfo {
		final String[] segments;

		public IncrTclTypeInfo(String[] segments) {
			this.segments = segments;
		}

		/**
		 * @param parts
		 * @return
		 */
		public boolean endsWith(String[] parts) {
			final int pLen = parts.length;
			final int sLen = segments.length;
			if (pLen <= sLen) {
				final int offset = sLen - pLen;
				for (int i = 0; i < pLen; ++i) {
					if (!parts[i].equals(segments[offset + i])) {
						return false;
					}
				}
				return true;
			}
			return false;
		}

		/**
		 * @return
		 */
		public String getSimpleName() {
			return segments[segments.length - 1];
		}

	}

	private final Map<String, Map<String, IncrTclTypeInfo>> names = new HashMap<String, Map<String, IncrTclTypeInfo>>();

	/**
	 * @param typeInfo
	 * @param resolvedType
	 */
	public void addType(ITclTypeHandler resolvedType) {
		String namespace = resolvedType.getNamespace();
		if (namespace.startsWith("::")) { //$NON-NLS-1$
			namespace = namespace.substring(2);
		}
		if (namespace.length() == 0) {
			return;
		}
		final String[] segments = TclParseUtil.tclSplit(namespace);
		if (segments.length == 0) {
			return;
		}
		final String lastName = segments[segments.length - 1];
		Map<String, IncrTclTypeInfo> types = names.get(lastName);
		if (types == null) {
			types = new HashMap<String, IncrTclTypeInfo>();
			names.put(lastName, types);
		}
		if (!types.containsKey(namespace)) {
			types.put(namespace, new IncrTclTypeInfo(segments));
		}
	}

	/**
	 * @param name
	 * @param nameArgument
	 * @return
	 */
	public IncrTclType resolve(String name) {
		String[] segments = TclParseUtil.tclSplit(name);
		if (segments.length == 0) {
			return null;
		}
		final Map<String, IncrTclTypeInfo> types = names
				.get(segments[segments.length - 1]);
		if (types == null) {
			return null;
		}
		for (Map.Entry<String, IncrTclTypeInfo> entry : types.entrySet()) {
			if (entry.getValue().endsWith(segments)) {
				return new IncrTclType(entry.getKey(), entry.getValue());
			}
		}
		return null;
	}


}
