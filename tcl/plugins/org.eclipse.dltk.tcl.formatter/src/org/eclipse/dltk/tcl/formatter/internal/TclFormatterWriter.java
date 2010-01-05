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
package org.eclipse.dltk.tcl.formatter.internal;

import org.eclipse.dltk.formatter.FormatterWriter;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.formatter.IFormatterIndentGenerator;

public class TclFormatterWriter extends FormatterWriter {

	/**
	 * @param document
	 * @param lineDelimiter
	 * @param indentGenerator
	 */
	public TclFormatterWriter(IFormatterDocument document,
			String lineDelimiter, IFormatterIndentGenerator indentGenerator) {
		super(document, lineDelimiter, indentGenerator);
	}

}
