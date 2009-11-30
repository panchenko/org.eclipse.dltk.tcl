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
package org.eclipse.dltk.tcl.parser.tests;

import org.eclipse.dltk.tcl.core.TclParseUtil;

import junit.framework.TestCase;

@SuppressWarnings("nls")
public class TclParseUtilTests extends TestCase {

	public void testEscapeName() {
		assertEquals("A", TclParseUtil.escapeName("A"));
		assertEquals("\\u0A", TclParseUtil.escapeName("\n"));
		assertEquals("{A }", TclParseUtil.escapeName("A "));
		assertEquals("{ A }", TclParseUtil.escapeName(" A "));
		assertEquals("{ }", TclParseUtil.escapeName(" "));
		assertEquals("Hello world", TclParseUtil.escapeName("Hello\\" + "\n"
				+ " world"));
	}

}
