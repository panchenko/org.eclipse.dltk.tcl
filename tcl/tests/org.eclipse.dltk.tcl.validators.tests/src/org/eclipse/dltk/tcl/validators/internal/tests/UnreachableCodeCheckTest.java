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

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.internal.validators.checks.UnreachableCodeCheck;
import org.eclipse.dltk.tcl.parser.TclError;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.tests.TestScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class UnreachableCodeCheckTest extends TestCase {
	TestScopeProcessor processor = new TestScopeProcessor();

	public void test001() throws Exception {
		String source = "puts 1; return; puts 2";
		typedCheck(source, ICheckKinds.UNREACHABLE_CODE);
	}

	public void test002() throws Exception {
		String source = "puts 1; return;";
		typedCheck(source);
	}

	public void test003() throws Exception {
		String source = "puts 1; return; puts 2; puts 3; puts 4";
		typedCheck(source, ICheckKinds.UNREACHABLE_CODE);
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
		ITclCheck check = new UnreachableCodeCheck();
		check.checkCommands(module, errors, new HashMap<String, String>(),
				null, new CodeModel(source));
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
