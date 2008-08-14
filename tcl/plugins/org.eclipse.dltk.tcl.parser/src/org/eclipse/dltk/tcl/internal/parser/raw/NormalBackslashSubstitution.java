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

public class NormalBackslashSubstitution extends TclElement implements
		ISubstitution {

	public static boolean iAm(CodeScanner input) {
		int c = input.read();
		if (c == -1)
			return false;
		if (c != '\\') {
			input.unread();
			return false;
		}
		boolean nl = TclTextUtils.isNewLine(input);
		input.unread();
		return !nl;
	}

	public boolean readMe(CodeScanner input, SimpleTclParser parser) throws TclParseException {
		if (!iAm(input))
			return false;
		setStart(input.getPosition());
		input.read();
		int c = input.read();
		int counter;
		switch (c) {
		case 'x':
			while (true) {
				c = input.read();
				if (!TclTextUtils.isHexDigit(c)) {
					input.unread();
					break;
				}
			}
			break;
		case 'u':
			counter = 0;
			while (counter < 4) {
				c = input.read();
				if (!TclTextUtils.isHexDigit(c)) {
					input.unread();
					break;
				}
				counter++;
			}
			break;
		default:
			if (TclTextUtils.isOctDigit(c)) {
				counter = 1;
				while (counter < 3) {
					c = input.read();
					if (!TclTextUtils.isOctDigit(c)) {
						input.unread();
						break;
					}
					counter++;
				}
			}
		}
		if (!input.isEOF())
			setEnd(input.getPosition() - 1);
		else
			setEnd(this.getStart());
		return true;
	}
}
