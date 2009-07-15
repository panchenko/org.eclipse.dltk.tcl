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
package org.eclipse.dltk.tcl.core.tests;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.dltk.core.DLTKContentTypeManager;
import org.eclipse.dltk.tcl.core.TclContentDescriber;
import org.eclipse.dltk.utils.TextUtils;

@SuppressWarnings("nls")
public class TclContentDescriberTests extends TestCase {

	private static class Description implements IContentDescription {

		public String getCharset() {
			return null;
		}

		public IContentType getContentType() {
			return null;
		}

		private final Map<QualifiedName, Object> properties = new HashMap<QualifiedName, Object>();

		public Object getProperty(QualifiedName key) {
			return properties.get(key);
		}

		public boolean isRequested(QualifiedName key) {
			return false;
		}

		public void setProperty(QualifiedName key, Object value) {
			properties.put(key, value);
		}

	}

	public void assertValid(String... lines) {
		TclContentDescriber describer = new TclContentDescriber();
		try {
			final IContentDescription description = new Description();
			describer.describe(new StringReader(TextUtils.join(lines, "\n")),
					description);
			assertTrue(Boolean.TRUE.equals(description
					.getProperty(DLTKContentTypeManager.DLTK_VALID)));
		} catch (IOException e) {
			fail(e.toString());
		}
		try {
			final IContentDescription description = new Description();
			describer.describe(new StringReader(TextUtils.join(lines, "\r\n")),
					description);
			assertTrue(Boolean.TRUE.equals(description
					.getProperty(DLTKContentTypeManager.DLTK_VALID)));
		} catch (IOException e) {
			fail(e.toString());
		}
	}

	public void testSimple() {
		assertValid("#!/bin/tclsh");
		assertValid("#!/bin/expect");
		assertValid("#!/bin/wish");
	}

	public void testEnv() {
		assertValid("#!/usr/bin/env tclsh");
		assertValid("#!/usr/bin/env expect");
		assertValid("#!/usr/bin/env wish");
	}

	public void testExec1() {
		assertValid("#!/bin/sh", "#\\",
				"exec $AUTOTEST/bin/expect \"$0\" \"$@\"");
	}

	public void testExec2() {
		assertValid("#!/bin/sh", "set me { $*", "shift", "shift",
				"exec $AUTOTEST/bin/expect $0 $*");
	}

}
