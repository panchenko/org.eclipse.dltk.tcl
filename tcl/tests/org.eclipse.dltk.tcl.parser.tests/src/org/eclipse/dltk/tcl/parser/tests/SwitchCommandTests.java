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

import java.net.URL;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class SwitchCommandTests extends TestCase {
	TestScopeProcessor processor = new TestScopeProcessor();

	@Test
	public void test001() throws Exception {
		String source = "switch $some {a {puts $some}}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test002() throws Exception {
		String source = "switch -glob -- -some {a concat2}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test003() throws Exception {
		String source = "switch -glob -- $some {a \"puts $some\"}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test004() throws Exception {
		String source = "switch -glob -- $some {a {puts $some} b \"puts $some\" c break}";
		typedCheck(source, 0, 3);
	}

	@Test
	public void test006() throws Exception {
		String source = "switch -exact -- $caller {cancel {puts cancel}}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test007() throws Exception {
		String source = "switch $caller {cancel return cancel2 return}";
		typedCheck(source, 0, 2);
	}

	@Test
	public void test008() throws Exception {
		String source = "switch -- $caller {cancel return cancel2 return}";
		typedCheck(source, 0, 2);
	}

	@Test
	public void test009() throws Exception {
		String source = "switch -- $caller cancel return cancel2 return2";
		typedCheck(source, 0, 2);
	}

	@Test
	public void test010() throws Exception {
		// cancel - unknown command - ats_rmserver
		String source = "switch $caller {{cancel} {puts cancel}}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test011() throws Exception {
		// -jobs unknown command - autoeasy_abort
		String source = "switch -exact -- $i { -jobs { set flag 1} }";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test012() throws Exception {
		// -jobs unknown command - autoeasy_abort
		String source = "switch -exact -- $i { \"jobs\" { set flag 1} }";
		typedCheck(source, 0, 1);
	}

	private void typedCheck(final String source, int errs, int code)
			throws Exception {
		Scope scope = DefinitionLoader
				.loadDefinitions(new URL(
						"platform:///plugin/org.eclipse.dltk.tcl.tcllib/definitions/builtin.xml"));
		TestCase.assertNotNull(scope);
		processor.add(scope);
		TclParser parser = new TclParser();
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, module.size());
		TclCommand tclCommand = module.get(0);
		EList<TclArgument> args = tclCommand.getArguments();

		final int[] scripts = { 0 };
		TclParserUtils.traverse(module, new TclVisitor() {
			@Override
			public boolean visit(Script script) {
				scripts[0]++;
				TestUtils.outCode(source, script.getStart(), script.getEnd());
				return true;
			}
		});
		TestCase.assertEquals(code, scripts[0]);

		/*
		 * int scripts0 = 0; for (int i = 0; i < args.size(); i++) { if
		 * (args.get(i) instanceof Script) { scripts0++;
		 * TestUtils.outCode(source, args.get(i).getStart(),
		 * args.get(i).getEnd()); } if (args.get(i) instanceof
		 * TclArgumentListImpl) { EList<TclArgument> innerArgs =
		 * ((TclArgumentList)args.get(i)).getArguments(); for (int k = 0; k <
		 * innerArgs.size(); k++) { if (innerArgs.get(k) instanceof Script) {
		 * scripts0++; TestUtils.outCode(source, innerArgs.get(i).getStart(),
		 * innerArgs.get(i).getEnd()); } } }
		 * 
		 * } TestCase.assertEquals(code, scripts0);
		 */

		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		TestCase.assertEquals(errs, errors.getCount());
	}
}
