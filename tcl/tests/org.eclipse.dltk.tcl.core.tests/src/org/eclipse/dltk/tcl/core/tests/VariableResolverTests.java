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

import java.util.Collections;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver.SimpleVariableRegistry;

@SuppressWarnings("nls")
public class VariableResolverTests extends TestCase {

	private static TclVariableResolver createResolver() {
		return new TclVariableResolver(new SimpleVariableRegistry(Collections
				.<String, String> emptyMap()));
	}

	private static TclVariableResolver createResolver(String key, String value) {
		return new TclVariableResolver(new SimpleVariableRegistry(Collections
				.singletonMap(key, value)));
	}

	public void testNop() {
		TclVariableResolver resolver = createResolver();
		assertEquals(Collections.singleton("a"), resolver.resolve("a"));
		assertEquals(Collections.singleton("$a"), resolver.resolve("$a"));
		assertEquals(Collections.singleton("${a"), resolver.resolve("${a"));
		assertEquals(Collections.singleton("${a}"), resolver.resolve("${a}"));
	}

	public void testSingle() {
		TclVariableResolver resolver = createResolver("name", "NAME");
		assertEquals(Collections.singleton("NAME"), resolver.resolve("$name"));
		assertEquals(Collections.singleton("NAME"), resolver.resolve("${name}"));
	}

	public void testSingleMixed() {
		TclVariableResolver resolver = createResolver("name", "NAME");
		assertEquals(Collections.singleton("/NAME/"), resolver
				.resolve("/$name/"));
		assertEquals(Collections.singleton("/NAME/"), resolver
				.resolve("/${name}/"));
	}
}
