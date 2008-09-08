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

package org.eclipse.dltk.tcl.validators.internal.tests;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.internal.validators.checks.ArgumentsDefinitionCheck;
import org.eclipse.dltk.tcl.parser.TclError;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.tests.TestScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.emf.common.util.EList;

public class ArgumentsDefinitionCheckTest extends TestCase {
	TestScopeProcessor processor = new TestScopeProcessor();

	public void test001() throws Exception {
		String source = "proc cmd arg {puts alpha}";
		typedCheck(source);
	}

	public void test002() throws Exception {
		String source = "proc cmd {arg} {puts alpha}";
		typedCheck(source);
	}

	public void test003() throws Exception {
		String source = "proc cmd {arg1 arg2} {puts alpha}";
		typedCheck(source);
	}

	public void test004() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2}} {puts alpha}";
		typedCheck(source);
	}

	public void test005() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2} {arg3 def3} args} {puts alpha}";
		typedCheck(source);
	}

	public void test006() throws Exception {
		String source = "proc cmd {{arg1} {{arg2}}} {puts alpha}";
		typedCheck(source);
	}

	public void test007() throws Exception {
		String source = "proc cmd {} {puts alpha}";
		typedCheck(source);
	}

	public void test008() throws Exception {
		String source = "proc cmd {arg1 {arg2 def2 def22} args} {puts alpha}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(TclErrorCollector.EXTRA_ARGUMENTS);
		errorCodes.add(ICheckKinds.CHECK_BAD_ARG_DEFINITION);
		typedCheck(source, errorCodes);
	}

	// public void test009() throws Exception {
	// String source = "proc cmd {puts alpha}";
	// typedCheck(source, TclErrorCollector.INVALID_ARGUMENT_COUNT);
	// }

	// public void test010() throws Exception {
	// String source = "proc cmd arg1 puts alpha";
	// typedCheck(source, TclErrorCollector.EXTRA_ARGUMENTS);
	// }

	public void test011() throws Exception {
		String source = "proc cmd {{{{arg1}}}} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_BAD_ARG_DEFINITION);
	}

	public void test013() throws Exception {
		String source = "proc cmd \"arg0 {{arg1}} {{arg2} {def2}} args\" {puts alpha}";
		typedCheck(source);
	}

	public void test016() throws Exception {
		String source = "proc cmd10 \"arg\" {puts alpha}";
		typedCheck(source);
	}

	public void test017() throws Exception {
		String source = "proc cmd11 $arg {puts alpha}";
		typedCheck(source);
	}

	public void test024() throws Exception {
		String source = "proc cmd7 [arg0] {puts alpha}";
		typedCheck(source);
	}

	public void test025() throws Exception {
		String source = "proc cmd7 arg0$s {puts alpha}";
		typedCheck(source);
	}

	public void test026() throws Exception {
		String source = "proc cmd7 arg0[lala] {puts alpha}";
		typedCheck(source);
	}

	public void test027() throws Exception {
		String source = "proc cmd {arg0 {arg1 def1} arg2} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_NON_DEF_AFTER_DEF);
	}

	public void test028() throws Exception {
		String source = "proc cmd {arg0 args arg2} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_ARG_AFTER_ARGS);
	}

	public void test030() throws Exception {
		String source = "proc cmd {arg0 {args def}} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_ARGS_DEFAULT);
	}

	public void test031() throws Exception {
		String source = "proc cmd {arg0 {{{arg1}}}} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_BAD_ARG_DEFINITION);
	}

	public void test033() throws Exception {
		String source = "proc cmd {arg {}} {puts alpha}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(TclErrorCollector.EXTRA_ARGUMENTS);
		errorCodes.add(ICheckKinds.CHECK_BAD_ARG_DEFINITION);
		typedCheck(source, errorCodes);
	}

	public void test034() throws Exception {
		String source = "proc cmd {arg {arg}} {puts alpha}";
		typedCheck(source, ICheckKinds.CHECK_SAME_ARG_NAME);
	}

	public void test035() throws Exception {
		String source = "apply {{args arg} {puts alpha}} 1 2";
		typedCheck(source, ICheckKinds.CHECK_ARG_AFTER_ARGS);
	}

	public void test036() throws Exception {
		String source = "proc cmd {{args {}}} {puts alpha}";
		typedCheck(source);
	}

	private void typedCheck(String source) throws Exception {
		typedCheck(source, new ArrayList<Integer>());
	}

	private void typedCheck(String source, int errorCode) throws Exception {
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(errorCode);
		typedCheck(source, errorCodes);
	}

	private void typedCheck(String source, List<Integer> errorCodes)
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
		ITclCheck check = new ArgumentsDefinitionCheck();
		check.checkCommands(module, errors, new HashMap<String, String>(),
				null, new CodeModel(source));

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
		TestCase.assertEquals(errorCodes.size(), errors.getCount());
		int count = 0;
		for (int code : errorCodes) {
			for (TclError tclError : errors.getErrors()) {
				if (tclError.getCode() == code)
					count++;
			}
		}
		TestCase.assertEquals(errorCodes.size(), count);
	}
}
