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
package org.eclipse.dltk.tcl.internal.tclchecker.v5;

import java.util.Collections;
import java.util.List;

public class RawToken implements IToken {

	private final TclDictionaryParser parser;
	private final String rawText;
	private List<IToken> tokens = null;

	public RawToken(TclDictionaryParser parser, String rawText) {
		this.parser = parser;
		this.rawText = rawText;
	}

	public List<IToken> getChildren() {
		if (tokens == null) {
			final List<IToken> children = parser.parseDictionary(rawText);
			if (children != null) {
				tokens = children;
			} else {
				final IToken word = new WordToken(rawText);
				tokens = Collections.<IToken> singletonList(word);
			}
		}
		return tokens;
	}

	public String getText() {
		return rawText;
	}

	public boolean hasChildren() {
		return true;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{"); //$NON-NLS-1$
		int index = 0;
		for (IToken token : getChildren()) {
			if (index != 0) {
				sb.append(' ');
			}
			sb.append(token.toString());
			++index;
		}
		sb.append("}"); //$NON-NLS-1$		
		return sb.toString();
	}

}
