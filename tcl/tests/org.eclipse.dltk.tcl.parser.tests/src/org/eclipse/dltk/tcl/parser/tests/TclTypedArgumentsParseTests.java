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

import org.eclipse.dltk.tcl.ast.ArgumentMatch;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class TclTypedArgumentsParseTests extends TestCase {
	public Command createConstantsCommand() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.ANY);
			arg.setLowerBound(1);
			arg.setUpperBound(1);
			arg.setName("alfa");
			command.getArguments().add(arg);
		}
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.SCRIPT);
			arg.setName("beta");
			arg.setLowerBound(0);
			arg.setUpperBound(1);
			command.getArguments().add(arg);
		}
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.ANY);
			arg.setName("gamma");
			arg.setLowerBound(1);
			arg.setUpperBound(1);
			command.getArguments().add(arg);
		}
		{
			TypedArgument arg = factory.createTypedArgument();
			arg.setType(ArgumentType.SCRIPT);
			arg.setName("delta");
			arg.setLowerBound(1);
			arg.setUpperBound(1);
			command.getArguments().add(arg);
		}
		return command;
	}

	@Test
	public void test001() throws Exception {
		String source = "constants alfa {set a 20} gamma {set a 20}";
		TclCommand cmd = typedCheck(source, 0, 2);
		List<ArgumentMatch> list = cmd.getMatches();
		TestCase.assertEquals(4, list.size());

		TclArgument[] alfaMatch = TclParserUtils.getTypedMatch(cmd, "alfa");
		TestCase.assertEquals(1, alfaMatch.length);

		TclArgument[] betaMatch = TclParserUtils.getTypedMatch(cmd, "beta");
		TestCase.assertEquals(1, betaMatch.length);

		TclArgument[] gammaMatch = TclParserUtils.getTypedMatch(cmd, "gamma");
		TestCase.assertEquals(1, gammaMatch.length);

		TclArgument[] deltaMatch = TclParserUtils.getTypedMatch(cmd, "delta");
		TestCase.assertEquals(1, deltaMatch.length);

	}

	@Test
	public void test002() throws Exception {
		String source = "constants alfa gamma {set a 20}";
		TclCommand cmd = typedCheck(source, 0, 1);
		List<ArgumentMatch> list = cmd.getMatches();
		TestCase.assertEquals(4, list.size());

		TclArgument[] alfaMatch = TclParserUtils.getTypedMatch(cmd, "alfa");
		TestCase.assertEquals(1, alfaMatch.length);

		TclArgument[] betaMatch = TclParserUtils.getTypedMatch(cmd, "beta");
		TestCase.assertEquals(0, betaMatch.length);

		TclArgument[] gammaMatch = TclParserUtils.getTypedMatch(cmd, "gamma");
		TestCase.assertEquals(1, gammaMatch.length);

		TclArgument[] deltaMatch = TclParserUtils.getTypedMatch(cmd, "delta");
		TestCase.assertEquals(1, deltaMatch.length);
	}

	@Test
	public void test003() throws Exception {
		String source = "constants alfa [alfa gamma] gamma {set a 20}";
		typedCheck(source, 0, 1);
	}

	@Test
	public void test004() throws Exception {
		String source = "constants alfa {set a 20} [gamma] {set a 20}";
		TclCommand command = typedCheck(source, 0, 2);
		List<ArgumentMatch> list = command.getMatches();
		TestCase.assertEquals(4, list.size());

		TclArgument[] alfaMatch = TclParserUtils.getTypedMatch(command, "alfa");
		TestCase.assertEquals(1, alfaMatch.length);

		TclArgument[] betaMatch = TclParserUtils.getTypedMatch(command, "beta");
		TestCase.assertEquals(1, betaMatch.length);

		TclArgument[] gammaMatch = TclParserUtils.getTypedMatch(command,
				"gamma");
		TestCase.assertEquals(1, gammaMatch.length);

		TclArgument[] deltaMatch = TclParserUtils.getTypedMatch(command,
				"delta");
		TestCase.assertEquals(1, deltaMatch.length);
	}

	@Test
	public void test005() throws Exception {
		String source = "constants alfa alfa {set a 20} [gamma] {set a 20}";
		typedCheck(source, 1, 0);
	}

	@Test
	public void test006() throws Exception {
		String source = "constants alfa {set} [gamma] set";
		typedCheck(source, 0, 2);
	}

	private TclCommand typedCheck(String source, int errs, int code)
			throws Exception {
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
		return tclCommand;
	}
}
