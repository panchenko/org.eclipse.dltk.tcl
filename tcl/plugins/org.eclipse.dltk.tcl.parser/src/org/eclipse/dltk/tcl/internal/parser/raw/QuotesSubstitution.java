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

public class QuotesSubstitution extends TclElement implements ISubstitution {

	private List<Object> contents;

	public QuotesSubstitution() {
		contents = new ArrayList<Object>();
	}

	public List<Object> getContents() {
		return contents;
	}

	public static boolean iAm(CodeScanner scanner) {
		int c = scanner.read();
		if (c == -1) {
			return false;
		}
		scanner.unread();
		return (c == '"');
	}

	private void append(char c) {
		Object o = null;
		if (contents.size() > 0)
			o = contents.get(contents.size() - 1);
		if (o != null && o instanceof String) {
			contents.set(contents.size() - 1, (String) o + c);
		} else {
			contents.add("" + c); //$NON-NLS-1$
		}
	}

	public boolean readMe(CodeScanner input, SimpleTclParser parser)
			throws TclParseException {
		if (!iAm(input))
			return false;
		setStart(input.getPosition());
		input.read();
		int c;
		while (true) {
			ISubstitution s = parser.getCVB(input);

			if (s != null) {
				s.readMe(input, parser);
				contents.add(s);
			} else {
				c = input.read();
				if (c == -1) {
					parser.handleError(new ErrorDescription(
							Messages.QuotesSubstitution_1, getStart(), input
									.getPosition(), ErrorDescription.ERROR));
					break;
				}
				append((char) c);
				if (c == '"') {
					break;
				}
			}
		}
		if (!input.isEOF()) {
			/*
			 * c = input.read(); if (!TclTextUtils.isWhitespace(c) && ( c !=
			 * CodeScanner.EOF) && (c != ']') && (c != ';')) { boolean cont =
			 * SimpleTclParser.handleError(new
			 * ErrorDescription("extra characters after closing-quote",
			 * input.getPosition(), ErrorDescription.ERROR)); if (!cont) throw
			 * new TclParseException("extra characters after closing-quote",
			 * input.getPosition()); do { c = input.read(); } while (c != -1 &&
			 * !TclTextUtils.isWhitespace(c)); input.unread(); } else
			 * input.unread();
			 */
			setEnd(input.getPosition() - 1);
		} else
			setEnd(input.getPosition());

		return true;
	}
}
