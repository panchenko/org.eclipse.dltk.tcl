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
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class TclGroupArgumentsParseTests extends TestCase {
	public Command createGroupCommand001() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			Group arg = factory.createGroup();
			arg.setLowerBound(1);
			arg.setUpperBound(2);
			arg.setConstant("-id");

			{
				TypedArgument arg2 = factory.createTypedArgument();
				arg2.setType(ArgumentType.SCRIPT);
				arg2.setName("beta");
				arg2.setLowerBound(1);
				arg2.setUpperBound(1);
				arg.getArguments().add(arg2);
			}
			command.getArguments().add(arg);
		}
		return command;
	}

	public Command createGroupCommand002() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			Group arg = factory.createGroup();
			arg.setLowerBound(0);
			arg.setUpperBound(2);
			arg.setConstant("-id");

			{
				TypedArgument arg2 = factory.createTypedArgument();
				arg2.setType(ArgumentType.SCRIPT);
				arg2.setName("beta");
				arg2.setLowerBound(1);
				arg2.setUpperBound(1);
				arg.getArguments().add(arg2);
			}
			command.getArguments().add(arg);
		}
		{
			Constant arg2 = factory.createConstant();
			arg2.setValue("--");
			arg2.setLowerBound(1);
			arg2.setUpperBound(1);
			command.getArguments().add(arg2);
		}
		{
			TypedArgument arg2 = factory.createTypedArgument();
			arg2.setType(ArgumentType.SCRIPT);
			arg2.setLowerBound(1);
			arg2.setUpperBound(1);
			command.getArguments().add(arg2);
		}

		return command;
	}

	@Test
	public void test001() throws Exception {
		String source = "constants -id {set a 20}";
		check001(source, 0, 1);
	}

	@Test
	public void test002() throws Exception {
		String source = "constants -id {set a 20} -id {set a 20}";
		check001(source, 0, 2);
	}

	@Test
	public void test003() throws Exception {
		String source = "constants";
		check001(source, 1, 0);
	}

	@Test
	public void test004() throws Exception {
		String source = "constants -id {set a 20} -id {set a 20} -id {set a 20}";
		check001(source, 1, 0);
	}

	@Test
	public void test005() throws Exception {
		String source = "constants -id {set a 20} -id {set a 20} -- {set a 20}";
		check002(source, 0, 3);
	}

	@Test
	public void test006() throws Exception {
		String source = "constants -id {set a 20} -- {set a 20}";
		check002(source, 0, 2);
	}

	@Test
	public void test007() throws Exception {
		String source = "constants -- {set a 20}";
		check002(source, 0, 1);
	}

	@Test
	public void test008() throws Exception {
		String source = "constants --- {set a 20}";
		check002(source, 1, 0);
	}

	@Test
	public void test009() throws Exception {
		String source = "constants -- {set a 20} {set a 20}";
		check002(source, 1, 0);
	}

	@Test
	public void test010() throws Exception {
		String source = "constants -- -- {set a 20} {set a 20}";
		check002(source, 1, 0);
	}

	@Test
	public void test011() throws Exception {
		String source = "constants -id { set a 30 } -- {set a 20} {set a 20}";
		check002(source, 1, 0);
	}

	@Test
	public void test012() throws Exception {
		String source = "constants -id { set a 30 } -- {set a 20}";
		check002(source, 0, 2);
	}

	private void check001(String source, int errs, int code) throws Exception {
		TestScopeProcessor manager = new TestScopeProcessor();
		manager.add(createGroupCommand001());
		check(source, errs, code, manager);
	}

	private void check002(String source, int errs, int code) throws Exception {
		TestScopeProcessor manager = new TestScopeProcessor();
		manager.add(createGroupCommand002());
		check(source, errs, code, manager);
	}

	private void check(String source, int errs, int code,
			TestScopeProcessor manager) throws Exception {
		TclParser parser = new TclParser();
		TclErrorCollector errors = new TclErrorCollector();
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
