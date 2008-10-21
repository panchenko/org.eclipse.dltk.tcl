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

public class CodeScanner implements ICodeScanner {

	private char[] content;
	private int pos;

	public CodeScanner(String content) {
		if (content != null) {
			this.content = content.toCharArray();
		} else {
			this.content = null;
		}
		pos = 0;
	}

	public int read() {
		if (isEOF())
			return EOF;
		char c = content[pos];
		pos++;
		return c;
	}

	public boolean isEOF() {
		return pos >= content.length;
	}

	public void unread() {
		pos--;
	}

	public int getPosition() {
		if (isEOF()) {
			return content.length - 1;
		} else {
			return pos;
		}
	}

}
