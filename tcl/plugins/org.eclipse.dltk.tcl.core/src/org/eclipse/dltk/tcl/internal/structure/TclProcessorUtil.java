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
package org.eclipse.dltk.tcl.internal.structure;

import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;

/**
 * @since 2.0
 */
public class TclProcessorUtil {

	/**
	 * @param arg
	 * @return
	 */
	public static String asString(TclArgument arg) {
		if (arg instanceof StringArgument) {
			final String value = ((StringArgument) arg).getValue();
			int len = value.length();
			if (len >= 2) {
				if (value.charAt(0) == '{' && value.charAt(len - 1) == '}') {
					return value.substring(1, len - 1);
				} else if (value.charAt(0) == '"'
						&& value.charAt(len - 1) == '"') {
					return value.substring(1, len - 1);
				}
			}
			return value;
		} else {
			return SimpleCodePrinter.getArgumentString(arg, arg.getStart());
		}
	}

}
