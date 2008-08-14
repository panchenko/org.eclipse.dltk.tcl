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

package org.eclipse.dltk.tcl.parser.tests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.tcl.parser.ITclErrorReporter;

public class TestTclParserErrorReporter implements ITclErrorReporter {
	public Map<Integer, Integer> errors = new HashMap<Integer, Integer>();
	public int total = 0;

	public TestTclParserErrorReporter() {
	}

	@Override
	public void report(int code, String value, int start, int end, int kind) {
		Integer key = new Integer(code);
		Integer old = errors.get(key);
		if (old == null) {
			errors.put(key, new Integer(1));
		} else {
			errors.put(key, new Integer(old.intValue() + 1));
		}
		total++;
	}
}
