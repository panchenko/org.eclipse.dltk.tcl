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
import java.util.List;

public class TclCommand extends TclElement {

	private List<TclWord> words;

	public TclCommand() {
		words = new ArrayList<TclWord>();
	}

	public void addWord(TclWord w) {
		if (w.empty()) {
			return;
		}
		w.setEnd(w.getStart() + w.length() - 1);
		words.add(w);
	}

	public List<TclWord> getWords() {
		return words;
	}

	public String toString() {
		return "TclCommand" + words; //$NON-NLS-1$
	}
}
