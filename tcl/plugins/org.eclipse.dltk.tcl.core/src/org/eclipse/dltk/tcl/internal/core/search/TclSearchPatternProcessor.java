/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Yuri Strot)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.core.search;

import org.eclipse.dltk.core.ISearchPatternProcessor;

public class TclSearchPatternProcessor implements ISearchPatternProcessor {

	public char[] extractDeclaringTypeQualification(String patternString) {
		int pos1 = patternString.lastIndexOf("::");
		if (pos1 != -1) {
			String p = patternString.substring(0, pos1);
			int pos2 = p.lastIndexOf("::");
			if (pos2 != -1) {
				return patternString.substring(0, pos2).toCharArray();
			}
			return null;
		}
		return null;
	}

	public char[] extractDeclaringTypeSimpleName(String patternString) {
		int pos1 = patternString.lastIndexOf("::");
		if (pos1 != -1) {
			String p = patternString.substring(0, pos1);
			return getLastTclNameElement(p).toCharArray();
		}
		return null;
	}

	public char[] extractSelector(String patternString) {
		return getLastTclNameElement(patternString).toCharArray();
	}

	private String getLastTclNameElement(String patternString) {
		int pos = patternString.lastIndexOf("::");
		if (pos != -1) {
			return patternString.substring(pos + 2);
		}
		return patternString;
	}

	public String getDelimiterReplacementString() {
		return "::";
	}

	public String extractTypeChars(String patternString) {
		return getLastTclNameElement(patternString);
	}

	public char[] extractTypeQualification(String patternString) {
		int pos1 = patternString.lastIndexOf("::");
		if (pos1 != -1) {
			return patternString.substring(0, pos1).toCharArray();
		}
		return null;
	}
}
