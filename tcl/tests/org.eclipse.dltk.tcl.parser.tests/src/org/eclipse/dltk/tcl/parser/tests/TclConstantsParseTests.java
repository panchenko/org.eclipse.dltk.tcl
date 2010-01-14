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

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;

public class TclConstantsParseTests extends TestCase {
	public Command createConstantsCommand() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			Constant arg = factory.createConstant();
			arg.setName("alfa");
			arg.setLowerBound(1);
			arg.setUpperBound(1);
			command.getArguments().add(arg);
		}
		{
			Constant arg = factory.createConstant();
			arg.setName("beta");
			arg.setLowerBound(0);
			arg.setUpperBound(2);
			command.getArguments().add(arg);
		}
		{
			Constant arg = factory.createConstant();
			arg.setName("gamma");
			arg.setLowerBound(0);
			arg.setUpperBound(2);
			command.getArguments().add(arg);
		}
		return command;
	}

	public void test001() throws Exception {
		String source = "constants alfa gamma gamma";
		constantsCheck(source, 0);
	}

	public void test002() throws Exception {
		String source = "constants alfa beta gamma gamma";
		constantsCheck(source, 0);
	}

	public void test003() throws Exception {
		String source = "constants alfa beta gamma";
		constantsCheck(source, 0);
	}

	public void test004() throws Exception {
		String source = "constants alfa beta";
		constantsCheck(source, 0);
	}

	public void test005() throws Exception {
		String source = "constants alfa";
		constantsCheck(source, 0);
	}

	public void test006() throws Exception {
		String source = "constants alfa gamma";
		constantsCheck(source, 0);
	}

	public void test007() throws Exception {
		String source = "constants";
		constantsCheck(source, 1);
	}

	public void test008() throws Exception {
		String source = "constants alfa alfa";
		constantsCheck(source, 1);
	}

	public void test009() throws Exception {
		String source = "constants alfa alfa beta beta";
		constantsCheck(source, 1);
	}

	public void test010() throws Exception {
		String source = "constants alfa alfa beta beta beta";
		constantsCheck(source, 1);
	}

	public void test011() throws Exception {
		String source = "constants alfa alfa beta beta beta gamma";
		constantsCheck(source, 1);
	}

	public void test012() throws Exception {
		String source = "constants alfa alfa beta beta beta gamma gamma gamma";
		constantsCheck(source, 1);
	}

	public void test013() throws Exception {
		String source = "constants alfa beta beta beta gamma gamma gamma";
		constantsCheck(source, 1);
	}

	public void test014() throws Exception {
		String source = "constants [alfa] beta gamma";
		constantsCheck(source, 1);
	}

	public void test015() throws Exception {
		String source = "constants [alfa] alfa beta gamma";
		constantsCheck(source, 1);
	}

	public void test016() throws Exception {
		String source = "constants [alfa] [beta] gamma";
		constantsCheck(source, 1);
	}

	public void test017() throws Exception {
		String source = "constants beta gamma";
		constantsCheck(source, 1);
	}

	private void constantsCheck(String source, int errs) throws Exception {
		TclParser parser = TestUtils.createParser();
		TestScopeProcessor manager = new TestScopeProcessor();
		TclErrorCollector errors = new TclErrorCollector();
		manager.add(createConstantsCommand());
		List<TclCommand> module = parser.parse(source, errors, manager);
		TestCase.assertEquals(1, module.size());
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		TestCase.assertEquals(errs, errors.getCount());
	}
}
