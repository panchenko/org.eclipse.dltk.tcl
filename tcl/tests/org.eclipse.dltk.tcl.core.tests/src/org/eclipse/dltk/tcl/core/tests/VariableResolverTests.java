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
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver;
import org.eclipse.dltk.tcl.internal.core.packages.TclVariableResolver.SimpleVariableRegistry;

@SuppressWarnings("nls")
public class VariableResolverTests extends TestCase {

	private static TclVariableResolver createResolver() {
		return new TclVariableResolver(new SimpleVariableRegistry(Collections
                .<String, Object> emptyMap()));
	}

    private static TclVariableResolver createResolver(String key, Object value) {
		return new TclVariableResolver(new SimpleVariableRegistry(Collections
				.singletonMap(key, value)));
	}

	public void testNop() {
		TclVariableResolver resolver = createResolver();
        assertEquals(("a"), resolver.resolve("a"));
        assertEquals(("$a"), resolver.resolve("$a"));
        assertEquals(("${a"), resolver.resolve("${a"));
        assertEquals(("${a}"), resolver.resolve("${a}"));
        assertEquals("$a(alfa)", resolver.resolve("$a(alfa)"));
	}

	public void testSingle() {
		TclVariableResolver resolver = createResolver("name", "NAME");
        assertEquals(("NAME"), resolver.resolve("$name"));
        assertEquals(("NAME"), resolver.resolve("${name}"));
	}

	public void testSingleMixed() {
		TclVariableResolver resolver = createResolver("name", "NAME");
        assertEquals(("/NAME/"), resolver.resolve("/$name/"));
        assertEquals(("/NAME/"), resolver.resolve("/${name}/"));
    }

    public void testComplex() {
        TclVariableResolver resolver = createResolver("name", "VERY_VERY_BIG");
        assertEquals(("zaa/VERY_VERY_BIG/bbb"), resolver
                .resolve("zaa/$name/bbb"));
    }

    public void testSmaller() {
        TclVariableResolver resolver = createResolver("longVariable", "small");
        assertEquals(("zaa/small/bbb"), resolver
                .resolve("zaa/$longVariable/bbb"));
    }

    public void testEnvironmentResolve() {
        Map<String, String> envValues = new HashMap<String, String>();
        Map<String, Object> variables = new HashMap<String, Object>();
        envValues.put("mytest", "myvalue");
        variables.put("env", envValues);
        variables.put("gamma", "mytest");

        TclVariableResolver resolver = new TclVariableResolver(
                new SimpleVariableRegistry(variables));
        assertEquals("myvalue", resolver.resolve("$env(mytest)"));
        assertEquals("myvalue", resolver.resolve("$env($gamma)"));
	}
}
