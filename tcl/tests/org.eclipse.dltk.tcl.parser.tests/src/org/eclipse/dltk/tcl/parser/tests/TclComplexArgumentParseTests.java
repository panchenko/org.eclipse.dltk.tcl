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
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;

public class TclComplexArgumentParseTests extends TestCase {
	public Command createConstantsCommand() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			ComplexArgument arg = factory.createComplexArgument();
			arg.setLowerBound(1);
			arg.setUpperBound(2);

			Group g = factory.createGroup();
			g.setConstant(null);
			g.setLowerBound(0);
			g.setUpperBound(-1);

			TypedArgument t1 = factory.createTypedArgument();
			t1.setName("kind");
			t1.setType(ArgumentType.ANY);
			t1.setLowerBound(1);
			t1.setUpperBound(1);
			g.getArguments().add(t1);
			TypedArgument t2 = factory.createTypedArgument();
			t2.setName("script");
			t2.setType(ArgumentType.SCRIPT);
			t2.setLowerBound(1);
			t2.setUpperBound(1);
			g.getArguments().add(t2);

			arg.getArguments().add(g);

			command.getArguments().add(arg);
		}
		return command;
	}

	
	public void test001() throws Exception {
		String source = "constants";
		constantsCheck(source, 1, 0);
	}

	
	public void test002() throws Exception {
		String source = "constants {a {set a 20}} ";
		constantsCheck(source, 0, 1);
	}

	
	public void test003() throws Exception {
		String source = "constants {a {set a 20}} {a {set a 20}}";
		constantsCheck(source, 0, 2);
	}

	
	public void test004() throws Exception {
		String source = "constants {} {a {set a 20}}";
		constantsCheck(source, 0, 1);
	}

	
	public void test005() throws Exception {
		String source = "constants {a {set a 20} b {set a 20} c {set a 20}} {a {set a 20}}";
		constantsCheck(source, 0, 4);
	}

	
	public void test006() throws Exception {
		String source = "constants {\n" + "a {\n" + "	set a 20\n" + "}\n"
				+ "b {\n" + "	set a 20\n" + "}\n" + "c {\n" + "	set a 20\n"
				+ "}\n" + "} {a {set a 20}}";
		constantsCheck(source, 0, 4);
	}

	private void constantsCheck(String source, int errs, int codeBlocks)
			throws Exception {
		TclParser parser = new TclParser();
		TestScopeProcessor manager = new TestScopeProcessor();
		TclErrorCollector errors = new TclErrorCollector();
		manager.add(createConstantsCommand());
		List<TclCommand> module = parser.parse(source, errors, manager);
		TestCase.assertEquals(1, module.size());
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		final int[] scripts = { 0 };
		TclParserUtils.traverse(module, new TclVisitor() {
			@Override
			public boolean visit(Script script) {
				scripts[0]++;
				return true;
			}
		});
		TestCase.assertEquals(errs, errors.getCount());
		TestCase.assertEquals(codeBlocks, scripts[0]);
	}
}
