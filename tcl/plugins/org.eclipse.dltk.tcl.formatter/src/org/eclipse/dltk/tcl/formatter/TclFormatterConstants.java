/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Sergey Kanshin)
 *******************************************************************************/
package org.eclipse.dltk.tcl.formatter;

import org.eclipse.dltk.ui.CodeFormatterConstants;

@SuppressWarnings("nls")
public class TclFormatterConstants {

	public static final String FORMATTER_TAB_CHAR = CodeFormatterConstants.FORMATTER_TAB_CHAR;
	public static final String FORMATTER_TAB_SIZE = CodeFormatterConstants.FORMATTER_TAB_SIZE;
	public static final String FORMATTER_INDENTATION_SIZE = CodeFormatterConstants.FORMATTER_INDENTATION_SIZE;

	public static final String INDENT_SCRIPT = "indent.script";
	public static final String INDENT_AFTER_BACKSLASH = "indent.after.backslash";

	public static final String LINES_FILE_AFTER_PACKAGE = "line.file.require.after";

	public static final String LINES_FILE_BETWEEN_PROC = "line.file.proc.between";

	public static final String LINES_PRESERVE = "lines.preserve";

	public static final String WRAP_COMMENTS = "wrap.comments";
	public static final String WRAP_COMMENTS_LENGTH = "wrap.comments.length";

	public static final String FORMATTER_PROFILES = "formatter.profiles";
	public static final String FORMATTER_ACTIVE_PROFILE = "formatter.profiles.active";
}
