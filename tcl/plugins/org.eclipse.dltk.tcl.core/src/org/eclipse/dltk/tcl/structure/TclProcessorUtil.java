/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.structure;

import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext.ITclParserInput;

/**
 * @since 2.0
 */
public class TclProcessorUtil {

	public static String asString(TclArgument arg) {
		return asString(arg, true);
	}

	/**
	 * @param arg
	 * @return
	 */
	public static String asString(TclArgument arg, boolean unquote) {
		if (arg instanceof StringArgument) {
			final String value = ((StringArgument) arg).getValue();
			if (unquote) {
				int len = value.length();
				if (len >= 2) {
					if (value.charAt(0) == '{' && value.charAt(len - 1) == '}') {
						return value.substring(1, len - 1);
					} else if (value.charAt(0) == '"'
							&& value.charAt(len - 1) == '"') {
						return value.substring(1, len - 1);
					}
				}
			}
			return value;
		} else {
			return SimpleCodePrinter.getArgumentString(arg, arg.getStart());
		}
	}

	private static class TclParserInput implements ITclParserInput {
		private final String content;
		private final int start;
		private final int end;

		public TclParserInput(String content, int start, int end) {
			this.content = content;
			this.start = start;
			this.end = end;
		}

		public String getContent() {
			return content;
		}

		public int getStart() {
			return start;
		}

		public int getEnd() {
			return end;
		}

	}

	public static ITclParserInput asInput(TclArgument arg) {
		if (arg instanceof StringArgument) {
			String source = ((StringArgument) arg).getValue();
			int start = arg.getStart();
			int end = arg.getEnd();
			if (source.startsWith("{") && source.endsWith("}")) {
				source = source.substring(1, source.length() - 1);
				++start;
				--end;
			} else if (source.startsWith("\"") && source.endsWith("\"")) {
				source = source.substring(1, source.length() - 1);
				++start;
				--end;
			}
			return new TclParserInput(source, start, end);
		} else {
			return null;
		}
	}

}
