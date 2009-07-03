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
package org.eclipse.dltk.tcl.internal.core.packages;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class TclVariableResolver {

	public static interface IVariableRegistry {
		/**
		 * Returns values for the specified variable or <code>null</code> if not
		 * defined
		 * 
		 * @param name
		 * @return
		 */
		String[] getValues(String name);
	}

	public static class SimpleVariableRegistry implements IVariableRegistry {

		private final Map<String, String> values;

		public SimpleVariableRegistry(Map<String, String> values) {
			this.values = values;
		}

		public String[] getValues(String name) {
			final String value = values.get(name);
			return value != null ? new String[] { value } : null;
		}

	}

	private final IVariableRegistry registry;

	public TclVariableResolver(IVariableRegistry registry) {
		this.registry = registry;
	}

	public static final char SUBST_START = '$';
	public static final char SUBST_PREFIX = '{';
	public static final char SUBST_SUFFIX = '}';

	public Set<String> resolve(String value) {
		final StringBuffer sb = new StringBuffer();
		int prev = 0;
		int pos;
		// search for the next instance of $ from the 'prev' position
		while ((pos = value.indexOf(SUBST_START, prev)) >= 0) {
			// if there was any text before this, add it as a fragment
			if (pos > prev) {
				sb.append(value.substring(prev, pos));
			}
			// if we are at the end of the string, we tack on a $ then move past
			// it
			if (pos == (value.length() - 1)) {
				sb.append(SUBST_START);
				prev = pos + 1;
			} else if (value.charAt(pos + 1) != SUBST_PREFIX) {
				if (isVarStart(value.charAt(pos + 1))) {
					int endName = pos + 1;
					while (endName < value.length()
							&& isVarPart(value.charAt(endName))) {
						++endName;
					}
					final String propertyName = value.substring(pos + 1,
							endName);
					final String[] varValue = registry.getValues(propertyName);
					if (varValue != null && varValue.length != 0) {
						// TODO handle all values?
						sb.append(varValue[0]);
					} else {
						sb.append(value.substring(pos, endName));
					}
					prev = endName;
				} else {
					sb.append(SUBST_START);
					prev = pos + 1;
				}
			} else {
				// property found, extract its name or bail on a typo
				final int endName = value.indexOf(SUBST_SUFFIX, pos);
				if (endName < 0) {
					prev = pos;
					break;
				}
				final String propertyName = value.substring(pos + 2, endName);
				final String[] varValue = registry.getValues(propertyName);
				if (varValue != null && varValue.length != 0) {
					// TODO handle all values?
					sb.append(varValue[0]);
				} else {
					sb.append(value.substring(pos, endName + 1));
				}
				prev = endName + 1;
			}
		}
		// no more $ signs found
		// if there is any tail to the file, append it
		if (prev < value.length()) {
			sb.append(value.substring(prev));
		}
		return Collections.singleton(sb.toString());
	}

	/**
	 * @param ch
	 * @return
	 */
	private static boolean isVarStart(char ch) {
		return Character.isJavaIdentifierStart(ch);
	}

	/**
	 * @param ch
	 * @return
	 */
	private boolean isVarPart(char ch) {
		return Character.isJavaIdentifierPart(ch);
	}

}
