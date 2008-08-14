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

import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.emf.common.util.EList;
import org.junit.Test;

public class TclParserTests {
	@Test
	public void testParser001() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a 20\n" + "set c 30";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertEquals(2, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertEquals(2, st1.getArguments().size());
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("set", ref1.getValue());

		TclArgument exp2 = st1.getArguments().get(0);
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref2 = (StringArgument) exp2;
		TestCase.assertEquals("a", ref2.getValue());
		TclArgument exp3 = st1.getArguments().get(1);
		TestCase.assertTrue(exp3 instanceof StringArgument);
		StringArgument ref3 = (StringArgument) exp3;
		TestCase.assertEquals("20", ref3.getValue());
	}

	@Test
	public void testParser002() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a \"wer\"\n" + "set c 30";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertNotNull(module);
		TestCase.assertEquals(2, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("set", ref1.getValue());

		TestCase.assertEquals(2, st1.getArguments().size());
		TclArgument exp2 = st1.getArguments().get(0);
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref2 = (StringArgument) exp2;
		TestCase.assertEquals("a", ref2.getValue());
		TclArgument exp3 = st1.getArguments().get(1);
		TestCase.assertTrue(exp3 instanceof StringArgument);
		StringArgument ref3 = (StringArgument) exp3;
		TestCase.assertEquals("\"wer\"", ref3.getValue());
	}

	@Test
	public void testParser003() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a {wer}\n" + "set c 30";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertNotNull(module);
		TestCase.assertEquals(2, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("set", ref1.getValue());

		TestCase.assertEquals(2, st1.getArguments().size());
		TclArgument exp2 = st1.getArguments().get(0);
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref2 = (StringArgument) exp2;
		TestCase.assertEquals("a", ref2.getValue());
		TclArgument exp3 = st1.getArguments().get(1);
		TestCase.assertTrue(exp3 instanceof StringArgument);
		StringArgument ref3 = (StringArgument) exp3;
		TestCase.assertEquals("{wer}", ref3.getValue());
	}

	@Test
	public void testParser004() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a [wer]\n" + "set c 30";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertNotNull(module);
		TestCase.assertEquals(2, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("set", ref1.getValue());

		TestCase.assertEquals(2, st1.getArguments().size());
		TclArgument exp2 = st1.getArguments().get(0);
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref2 = (StringArgument) exp2;
		TestCase.assertEquals("a", ref2.getValue());

		TclArgument exp3 = st1.getArguments().get(1);
		TestCase.assertTrue(exp3 instanceof Substitution);
		Substitution subst = (Substitution) exp3;
		EList<TclCommand> list = subst.getCommands();
		TestCase.assertEquals(1, list.size());
		TclCommand anyCommand = list.get(0);
		TestCase.assertNotNull(anyCommand);
		EList<TclArgument> arguments = anyCommand.getArguments();
		TestCase.assertEquals(0, arguments.size());
		TclArgument argument = anyCommand.getName();
		TestCase.assertTrue(argument instanceof StringArgument);
		TestCase.assertEquals("wer", ((StringArgument) argument).getValue());
	}

	@Test
	public void testParser005() throws Exception {
		TclParser parser = new TclParser();
		String source = "puts \"alfa[de]be$teta\" $delta";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertEquals(1, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertEquals(2, st1.getArguments().size());
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("puts", ref1.getValue());
	}

	@Test
	public void testParser006() throws Exception {
		TclParser parser = new TclParser();
		String source = "incr i -$length";
		List<TclCommand> module = parser.parse(source);
		TestCase.assertEquals(1, module.size());

		TclCommand st1 = module.get(0);
		TclArgument exp1 = st1.getName();
		TestCase.assertEquals(2, st1.getArguments().size());
		TestCase.assertTrue(exp1 instanceof StringArgument);
		StringArgument ref1 = (StringArgument) exp1;
		TestCase.assertEquals("incr", ref1.getValue());
	}

	@Test
	public void testSimpleErrors001() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a \"This is\nset b 20";
		TclErrorCollector collector = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, collector, null);
		TestCase.assertEquals(1, module.size());
		TestUtils.outErrors(source, collector);
		TestCase.assertEquals(1, collector.getCount());
	}

	@Test
	public void testSimpleErrors002() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a [This is\nset b 20";
		TclErrorCollector collector = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, collector, null);
		TestCase.assertEquals(1, module.size());
		TestUtils.outErrors(source, collector);
		TestCase.assertEquals(1, collector.getCount());
	}

	@Test
	public void testSimpleErrors003() throws Exception {
		TclParser parser = new TclParser();
		String source = "set a {This is\nset b 20";
		TclErrorCollector collector = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, collector, null);
		TestCase.assertEquals(1, module.size());
		TestUtils.outErrors(source, collector);
		TestCase.assertEquals(1, collector.getCount());
	}
}
