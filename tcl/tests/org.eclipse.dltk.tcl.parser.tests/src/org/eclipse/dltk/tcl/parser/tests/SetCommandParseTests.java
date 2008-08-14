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

import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class SetCommandParseTests {
	public Command createConstantsCommand() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("set");
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.VAR_NAME);
			arg.setLowerBound(1);
			arg.setUpperBound(1);
			arg.setName("varName");
			command.getArguments().add(arg);
		}
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.ANY);
			arg.setName("value");
			arg.setLowerBound(0);
			arg.setUpperBound(1);
			command.getArguments().add(arg);
		}
		return command;
	}

	@Test
	public void test001() throws Exception {
		String source = "set var value";
		typedCheck(source, 0, 0);
	}

	@Test
	public void test002() throws Exception {
		String source = "set var";
		typedCheck(source, 0, 0);
	}

	@Test
	public void test003() throws Exception {
		String source = "set var value value";
		typedCheck(source, 1, 0);
	}

	/*
	 * @Test public void test004() throws Exception { String source =
	 * "set {var} value"; typedCheck(source, 1, 1); }
	 */

	private void typedCheck(String source, int errs, int code) throws Exception {
		TclParser parser = new TclParser();
		TestScopeProcessor manager = new TestScopeProcessor();
		TclErrorCollector errors = new TclErrorCollector();
		manager.add(createConstantsCommand());
		List<TclCommand> module = parser.parse(source, errors, manager);
		TestCase.assertEquals(1, module.size());
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
		TestCase.assertEquals(code, scripts);
		TestCase.assertEquals(errs, errors.getCount());
	}
}
