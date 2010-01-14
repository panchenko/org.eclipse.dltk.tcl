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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.internal.validators.checks.CommandRedefinitionCheck;
import org.eclipse.dltk.tcl.parser.TclError;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.dltk.tcl.parser.tests.TestUtils;
import org.eclipse.dltk.tcl.validators.ITclCheck;
import org.eclipse.dltk.utils.TextUtils;

public class CommandRedefinitionCheckTest extends TestCase {
	NamespaceScopeProcessor processor = DefinitionManager.getInstance()
			.createProcessor();

	public void test001() throws Exception {
		String source = "proc set {} {}";
		typedCheck(source, ICheckKinds.BUILTIN_COMMAND_REDEFINITION);
	}

	public void test002() throws Exception {
		String source = "proc set {} {}; proc set {} {}; proc set {} {}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		// errorCodes.add(ICheckKinds.BUILTIN_COMMAND_REDEFINITION);
		// errorCodes.add(ICheckKinds.BUILTIN_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.BUILTIN_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		typedCheck(source, errorCodes);
	}

	public void test003() throws Exception {
		String source = "proc myProc1 {} {}; proc myProc2 {} {}; "
				+ "proc myProc1 {} {}; proc myProc2 {} {}; "
				+ "proc myProc2 {} {}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		typedCheck(source, errorCodes);
	}

	public void test004() throws Exception {
		String source = "proc myProc1 {} {}\nproc myProc2 {} {}\n"
				+ "proc myProc1 {} {}\nproc myProc2 {} {}\n"
				+ "proc myProc2 {} {}";
		List<Integer> errorCodes = new ArrayList<Integer>();
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		errorCodes.add(ICheckKinds.USER_COMMAND_REDEFINITION);
		typedCheck(source, errorCodes);
	}

	public void test005() throws Exception {
		String source = "proc ::mclistbox::Find {w id {direction right}} {}";
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
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement element = stackTrace[2];
		System.out.println("%%%%%%%%%%%%%%%%Test:" + element.getMethodName());
		TclParser parser = TestUtils.createParser();
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		ITclCheck check = new CommandRedefinitionCheck();
		check.checkCommands(module, errors, new HashMap<String, String>(),
				null, TextUtils.createLineTracker(source));
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
	}
}
