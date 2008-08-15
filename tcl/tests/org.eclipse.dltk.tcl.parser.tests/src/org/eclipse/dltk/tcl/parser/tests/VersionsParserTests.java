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
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class VersionsParserTests extends TestCase {
	
	public void testParseVersion() {
		TestCase.assertEquals(true, TclParserUtils.parseVersion("[8.1;8.2)",
				"8.1.1"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion(
				"[8.1;8.2)(8.4;8.5)", "8.4.1"));
		TestCase.assertEquals(false, TclParserUtils.parseVersion(
				"(8.1;8.2)(8.4;8.5)", "8.1"));
		TestCase.assertEquals(false, TclParserUtils.parseVersion(
				"[8.1;8.2)(8.4;8.5)", "8.5"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion(
				"[8.1;8.2)(8.4;8.5)", "8.1"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion(
				"[8.1;8.2)(8.4;-)", "8.6"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion(
				"[-;8.2)(8.4;8.5)", "8.0"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion(
				"[-;-)(8.4;8.5)", "8.0"));
		TestCase.assertEquals(false, TclParserUtils.parseVersion("[8.4.5;-)",
				"8.4.1"));
		TestCase.assertEquals(true, TclParserUtils.parseVersion("[8.4.5;-)",
				"8.4.6"));
	}

	
	public void testIsVersionValid() {
		TestCase.assertEquals(true, TclParserUtils.isVersionValid("[8.1;8.2)"));
		TestCase.assertEquals(true, TclParserUtils
				.isVersionValid("(8.1;8.2] \n [8.4;8.6)"));
		TestCase.assertEquals(false, TclParserUtils
				.isVersionValid("(8.1;8.2] ; [8.4;8.6)"));
		TestCase.assertEquals(false, TclParserUtils.isVersionValid("(8.1;8"));
		TestCase.assertEquals(false, TclParserUtils.isVersionValid("(8..1;8)"));
		TestCase.assertEquals(false, TclParserUtils.isVersionValid("(8.;8)"));
		TestCase.assertEquals(false, TclParserUtils
				.isVersionValid("(8.1;8.2]]"));
		TestCase.assertEquals(false, TclParserUtils
				.isVersionValid("(8.1);(8.2]"));
		TestCase.assertEquals(false, TclParserUtils.isVersionValid("8.1"));
	}

	
	public void testCompare() {
		TestCase.assertEquals(-1, TclParserUtils.compareVersions("8.1", "8.2"));
		TestCase.assertEquals(-1, TclParserUtils
				.compareVersions("8.4.1", "8.5"));
		TestCase.assertEquals(0, TclParserUtils.compareVersions("8.1", "8.1"));
		TestCase
				.assertEquals(1, TclParserUtils.compareVersions("8.2", "8.1.9"));
		TestCase
				.assertEquals(1, TclParserUtils.compareVersions("8.1.1", "8.1"));
	}

	private static Command createNamespaceCommand(DefinitionsFactory factory,
			String version) {
		Command command = factory.createCommand();
		command.setName("namespace");
		command.setVersion(version);

		Switch kind = factory.createSwitch();
		Group evalGroup = factory.createGroup();
		evalGroup.setConstant("eval");
		// Eval group
		TypedArgument namespaceEvalName = factory.createTypedArgument();
		namespaceEvalName.setLowerBound(1);
		namespaceEvalName.setUpperBound(1);
		namespaceEvalName.setName("namespaceName");
		namespaceEvalName.setType(ArgumentType.NAMESPACE);
		evalGroup.getArguments().add(namespaceEvalName);

		TypedArgument namespaceEvalScripts = factory.createTypedArgument();
		namespaceEvalScripts.setLowerBound(1);
		namespaceEvalScripts.setUpperBound(-1);
		namespaceEvalScripts.setName("scripts");
		namespaceEvalScripts.setType(ArgumentType.SCRIPT);
		evalGroup.getArguments().add(namespaceEvalScripts);

		kind.getGroups().add(evalGroup);
		command.getArguments().add(kind);
		return command;
	}

	
	public void testCommandVersions001() throws Exception {
		String v1 = "(-;8.5)";
		String v2 = "[8.5;-]";
		String v = "8.4";
		String vs = v1;
		testCommandVersionsDo(v1, v2, v, vs);
	}

	
	public void testCommandVersions002() throws Exception {
		String v1 = "(-;8.5)";
		String v2 = "[8.5;-]";
		String v = "8.4.1";
		String vs = v1;
		testCommandVersionsDo(v1, v2, v, vs);
	}

	
	public void testCommandVersions003() throws Exception {
		String v1 = null;
		String v2 = "[8.5;-]";
		String v = "8.4.1";
		String vs = v1;
		testCommandVersionsDo(v1, v2, v, vs);
	}

	
	public void testCommandVersions004() throws Exception {
		String v1 = "(-;8.5)";
		String v2 = "[8.5;-]";
		String v = "8.5";
		String vs = v2;
		testCommandVersionsDo(v1, v2, v, vs);
	}

	
	public void testCommandVersions005() throws Exception {
		String v1 = "(-;8.5)";
		String v2 = "[8.5;-]";
		String v = "8.6";
		String vs = v2;
		testCommandVersionsDo(v1, v2, v, vs);
	}

	
	public void testCommandVersions006() throws Exception {
		String v1 = "(8.4;8.5)";
		String v = "8.4.1";
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		processor.addScope(createNamespaceCommand(DefinitionsFactory.eINSTANCE,
				v1));
		String source = "namespace eval gamma {}";
		TclParser parser = new TclParser(v);
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertNotNull(module);
		TclCommand command = module.get(0);
		TestCase.assertEquals(v1, command.getDefinition().getVersion());
	}

	
	public void testCommandVersions007() throws Exception {
		String v1 = "(8.4;8.5)";
		String v = "8.3.1";
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		processor.addScope(createNamespaceCommand(DefinitionsFactory.eINSTANCE,
				v1));
		String source = "namespace eval gamma {}";
		TclParser parser = new TclParser(v);
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, errors.getCount());
		TestCase.assertNotNull(module);
		TclCommand command = module.get(0);
		TestCase.assertNull(command.getDefinition());
	}

	
	public void testDeprecated001() throws Exception {
		String v1 = "(8.4;8.5)";
		String v = "8.4.1";
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		Command cmd1 = createNamespaceCommand(DefinitionsFactory.eINSTANCE, v1);
		processor.addScope(cmd1);
		cmd1.setDeprecated("[8.4.5;-)");
		String source = "namespace eval gamma {}";
		TclParser parser = new TclParser(v);
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(0, errors.getCount());
		TestCase.assertNotNull(module);
		TclCommand command = module.get(0);
		TestCase.assertEquals(cmd1, command.getDefinition());
	}

	
	public void testDeprecated002() throws Exception {
		String v1 = "(8.4;8.5)";
		String v = "8.4.6";
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		Command cmd1 = createNamespaceCommand(DefinitionsFactory.eINSTANCE, v1);
		processor.addScope(cmd1);
		cmd1.setDeprecated("[8.4.5;-)");
		String source = "namespace eval gamma {}";
		TclParser parser = new TclParser(v);
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, errors.getCount());
		TestCase.assertNotNull(module);
		TclCommand command = module.get(0);
		TestCase.assertEquals(cmd1, command.getDefinition());
	}

	private void testCommandVersionsDo(String v1, String v2, String v, String vs) {
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();
		processor.addScope(createNamespaceCommand(DefinitionsFactory.eINSTANCE,
				v1));
		processor.addScope(createNamespaceCommand(DefinitionsFactory.eINSTANCE,
				v2));
		String source = "namespace eval gamma {}";
		TclParser parser = new TclParser(v);
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertNotNull(module);
		TclCommand command = module.get(0);
		TestCase.assertEquals(vs, command.getDefinition().getVersion());
	}
}
