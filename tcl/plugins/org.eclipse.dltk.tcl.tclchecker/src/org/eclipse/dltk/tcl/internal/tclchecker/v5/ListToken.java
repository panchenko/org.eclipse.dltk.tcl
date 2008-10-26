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

import java.util.ArrayList;
import java.util.List;

public class ListToken implements IToken {

	private final List<IToken> children = new ArrayList<IToken>();

	public List<IToken> getChildren() {
		return children;
	}

	public String getText() {
		final StringBuilder sb = new StringBuilder();
		for (IToken token : children) {
			if (sb.length() != 0) {
				sb.append(' ');
			}
			sb.append(token.getText());
		}
		return sb.toString();
	}

	public boolean hasChildren() {
		return true;
	}

	public void addChild(IToken child) {
		children.add(child);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{"); //$NON-NLS-1$
		int index = 0;
		for (IToken token : children) {
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
