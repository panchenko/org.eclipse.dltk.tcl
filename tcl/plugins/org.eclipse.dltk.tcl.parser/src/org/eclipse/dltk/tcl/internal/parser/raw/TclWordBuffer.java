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
package org.eclipse.dltk.tcl.internal.parser.raw;

import java.util.ArrayList;
import java.util.List;

class TclWordBuffer {

	enum State {
		START, CONTENT
	}

	private TclWordBuffer.State state;
	private int start;
	private final List<Object> contents = new ArrayList<Object>();
	private final StringBuilder string = new StringBuilder();

	public TclWordBuffer.State getState() {
		return state;
	}

	public void setState(TclWordBuffer.State state) {
		this.state = state;
	}

	public void reset() {
		state = State.START;
		contents.clear();
		string.setLength(0);
	}

	public boolean isEmpty() {
		return contents.isEmpty() && string.length() == 0;
	}

	public TclWord buildWord() {
		flushStringBuffer();
		if (!isEmpty()) {
			final TclWord word = new TclWord(contents);
			word.setStart(start);
			state = State.START;
			contents.clear();
			return word;
		} else {
			return null;
		}
	}

	private final void flushStringBuffer() {
		if (string.length() != 0) {
			contents.add(string.toString());
			string.setLength(0);
		}
	}

	public void setStart(int start) {
		this.start = start;
	}

	public void add(ISubstitution s) {
		flushStringBuffer();
		contents.add(s);
		state = State.CONTENT;
	}

	public void add(char ch) {
		string.append(ch);
		state = State.CONTENT;
	}

	public List<Object> getContents() {
		flushStringBuffer();
		return contents;
	}

}
