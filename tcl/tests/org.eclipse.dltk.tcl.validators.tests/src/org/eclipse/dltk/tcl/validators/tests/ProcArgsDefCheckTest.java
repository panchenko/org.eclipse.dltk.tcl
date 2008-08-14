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

package org.eclipse.dltk.tcl.validators.tests;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.internal.validators.checks.ProcArgsDefCheck;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.tests.TestScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class ProcArgsDefCheckTest extends TestCase {
	TestScopeProcessor processor = new TestScopeProcessor();

	@Test
	public void test001() throws Exception {
		String source = "proc cmd arg {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test002() throws Exception {
		String source = "proc cmd {arg} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test003() throws Exception {
		String source = "proc cmd {arg1 arg2} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test004() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2}} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test005() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2} args} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test006() throws Exception {
		String source = "proc cmd {{arg1} {{arg2}}} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test007() throws Exception {
		String source = "proc cmd {} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test008() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2 def22} args} {puts alpha}";
		typedCheck(source, true, TclErrorCollector.EXTRA_ARGUMENTS);
	}

	@Test
	public void test009() throws Exception {
		String source = "proc cmd {puts alpha}";
		typedCheck(source, true, TclErrorCollector.INVALID_ARGUMENT_COUNT);
	}

	@Test
	public void test010() throws Exception {
		String source = "proc cmd arg1 puts alpha";
		typedCheck(source, true, TclErrorCollector.EXTRA_ARGUMENTS);
	}

	@Test
	public void test011() throws Exception {
		String source = "proc cmd {{{{arg1}}}} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_BAD_ARG_DEFINITION);
	}

	@Test
	public void test012() throws Exception {
		String source = "proc c0 {a0 {a1 d1}} {puts $a0}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test013() throws Exception {
		String source = "proc cmd \"arg0 {{arg1}} {{arg2} {def2}} args\" {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test014() throws Exception {
		String source = "proc cmd0 arg {puts alpha}	";
		typedCheck(source, false, 0);
	}

	@Test
	public void test015() throws Exception {
		String source = "proc cmd1 {arg} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test016() throws Exception {
		String source = "proc cmd10 \"arg\" {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test017() throws Exception {
		String source = "proc cmd11 $arg {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test018() throws Exception {
		String source = "proc cmd2 {arg1 arg2} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test019() throws Exception {
		String source = "proc cmd3 {arg1 {arg2 def2}} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test020() throws Exception {
		// !!!
		String source = "proc cmd4 {arg1 {arg2 def2} args} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test021() throws Exception {
		String source = "proc cmd5 {{arg1} {{arg2}}} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test022() throws Exception {
		String source = "proc cmd6 {} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test023() throws Exception {
		String source = "proc cmd7 {arg0 {{arg1}}} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test024() throws Exception {
		String source = "proc cmd7 [arg0] {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test025() throws Exception {
		String source = "proc cmd7 arg0$s {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test026() throws Exception {
		String source = "proc cmd7 arg0[lala] {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test027() throws Exception {
		String source = "proc cmd {arg0 {arg1 def1} arg2} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_NON_DEF_AFTER_DEF);
	}

	@Test
	public void test028() throws Exception {
		String source = "proc cmd {arg0 args arg2} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_ARG_AFTER_ARGS);
	}

	@Test
	public void test029() throws Exception {
		String source = "proc cmd {arg0 {arg1 def10 def11}} {puts alpha}";
		typedCheck(source, true, TclErrorCollector.EXTRA_ARGUMENTS);
	}

	@Test
	public void test030() throws Exception {
		String source = "proc cmd {arg0 {args def}} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_ARGS_DEFAULT);
	}

	@Test
	public void test031() throws Exception {
		String source = "proc cmd {arg0 {{{arg1}}}} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_BAD_ARG_DEFINITION);
	}

	@Test
	public void test032() throws Exception {
		String source = "proc cmd {} {puts alpha}";
		typedCheck(source, false, 0);
	}

	@Test
	public void test033() throws Exception {
		String source = "proc cmd {arg {}} {puts alpha}";
		typedCheck(source, true, ICheckKinds.CHECK_ARG_WITH_NO_NAME);
	}

	private void typedCheck(String source, boolean isError, int errorCode)
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
		ITclCheck check = new ProcArgsDefCheck();
		check.checkCommands(module, errors, new HashMap<String, String>());
		TclCommand tclCommand = module.get(0);
		EList<TclArgument> arguments = tclCommand.getArguments();
		int scripts = 0;
		for (int i = 0; i < arguments.size(); i++) {
			if (arguments.get(i) instanceof Script) {
				scripts++;
			}
		}
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		if (isError) {
			TestCase.assertEquals(1, errors.getCount());
			TestCase.assertEquals(errorCode, (errors.getErrors()[0]).getCode());
		} else {
			TestCase.assertEquals(0, errors.getCount());
		}
	}
}