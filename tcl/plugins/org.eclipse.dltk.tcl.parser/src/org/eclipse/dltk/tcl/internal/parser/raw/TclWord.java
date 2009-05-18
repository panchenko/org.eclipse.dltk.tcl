/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.parser.raw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tcl word is a list of text pieces and substitutions.
 * 
 * @author fourdman
 * 
 */
public class TclWord extends TclElement {

	private final List<Object> contents;
	private int len = -1;

	TclWord() {
		this.contents = new ArrayList<Object>();
	}

	TclWord(List<Object> contents) {
		this.contents = new ArrayList<Object>(contents);
	}

	public void add(String text) {
		Object o = null;
		if (contents.size() > 0)
			o = contents.get(contents.size() - 1);
		if (o != null && o instanceof String) {
			contents.set(contents.size() - 1, ((String) o).concat(text));
		} else {
			contents.add(text);
		}
		if (len != -1) {
			len += text.length();
		}
	}

	public void add(char c) {
		if (len != -1) {
			len += 1;
		}
		add(String.valueOf(c));
	}

	public void add(ISubstitution s) {
		contents.add(s);
		len = -1;
	}

	public List<Object> getContents() {
		return contents;
	}

	public int length() {
		if (len == -1) {
			int result = 0;
			for (Iterator<Object> iter = contents.iterator(); iter.hasNext();) {
				Object o = iter.next();
				if (o instanceof TclElement) {
					TclElement el = (TclElement) o;
					result += el.getEnd() - el.getStart() + 1;
				} else if (o instanceof String) {
					result += ((String) o).length();
				}
			}
			len = result;
		}
		return len;
	}

	public String toString() {
		return "TclWord" + contents; //$NON-NLS-1$
	}
}
