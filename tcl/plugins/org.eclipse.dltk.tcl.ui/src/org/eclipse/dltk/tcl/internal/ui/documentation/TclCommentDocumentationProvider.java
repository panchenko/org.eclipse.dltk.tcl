/*******************************************************************************
 * Copyright (c) 2005, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.ui.documentation;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.core.IBuffer;
import org.eclipse.dltk.core.IMember;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ISourceRange;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.ui.documentation.IScriptDocumentationProvider;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;

public class TclCommentDocumentationProvider implements
		IScriptDocumentationProvider {

	private static final char COMMENT_SIGN = '#';

	protected String getLine(Document d, int line) throws BadLocationException {
		return d.get(d.getLineOffset(line), d.getLineLength(line));
	}

	protected List getHeaderComment(IMember member) {
		// if (member instanceof IField) {
		// return null;
		// }
		try {
			ISourceRange range = member.getSourceRange();
			if (range == null)
				return null;

			IBuffer buf = null;

			ISourceModule compilationUnit = member.getSourceModule();
			if (!compilationUnit.isConsistent()) {
				return null;
			}

			buf = compilationUnit.getBuffer();

			final int start = range.getOffset();

			String contents = null;
			if (buf != null) {
				contents = buf.getContents();
			} else {
				contents = member.getSourceModule().getSource();
			}

			Document doc = new Document(contents);
			try {
				int line = doc.getLineOfOffset(start);
				--line; // look on the previous line
				if (line >= 0) {
					final List result = new ArrayList();
					do {
						final String curLine = getLine(doc, line).trim();
						if (curLine.length() > 0
								&& curLine.charAt(0) == COMMENT_SIGN) {
							result.add(0, curLine);
						} else if (!(curLine.length() == 0 && result.isEmpty())) {
							// skip empty lines between comment and code only
							break;
						}
					} while (--line >= 0);
					return result;
				}
			} catch (BadLocationException e) {
				// ignore
			}

		} catch (ModelException e) {
			// ignore
		}
		return null;
	}

	public Reader getInfo(IMember member, boolean lookIntoParents,
			boolean lookIntoExternal) {
		final List header = getHeaderComment(member);
		if (header != null && !header.isEmpty()) {
			return new StringReader(convertToHTML(header));
		} else {
			return null;
		}
	}

	protected String convertToHTML(List header) {
		final StringBuffer result = new StringBuffer();
		boolean paragraphStarted = false;
		for (int line = 0; line < header.size(); line++) {
			String str = (String) header.get(line);
			int begin = 0;
			int end = str.length();
			while (begin < end && str.charAt(begin) == COMMENT_SIGN) {
				++begin;
			}
			while (begin < end && Character.isWhitespace(str.charAt(begin))) {
				++begin;
			}
			while (begin < end && str.charAt(end - 1) == COMMENT_SIGN) {
				--end;
			}
			while (begin < end && Character.isWhitespace(str.charAt(end - 1))) {
				--end;
			}
			if (begin == end) {
				if (paragraphStarted) {
					result.append(P_END);
					paragraphStarted = false;
				}
			} else {
				str = str.substring(begin, end);
				if (str.matches("\\w+(\\s+\\w+)*:")) { //$NON-NLS-1$
					if (paragraphStarted) {
						result.append(P_END);
						paragraphStarted = false;
					}
					result.append("<h4>"); //$NON-NLS-1$
					result.append(TextUtils.escapeHTML(str));
					result.append("</h4>\n"); //$NON-NLS-1$
				} else {
					if (!paragraphStarted) {
						result.append(P_BEGIN);
						paragraphStarted = true;
					} else {
						result.append(LINE_BREAK);
					}
					result.append(TextUtils.escapeHTML(str));
				}
			}
		}
		if (paragraphStarted) {
			result.append(P_END);
		}
		return result.toString();
	}

	private static final String P_BEGIN = "<p>"; //$NON-NLS-1$
	private static final String P_END = "</p>"; //$NON-NLS-1$

	private static final String LINE_BREAK = "<br>\n"; //$NON-NLS-1$

	public Reader getInfo(String content) {
		return null;
	}
}
