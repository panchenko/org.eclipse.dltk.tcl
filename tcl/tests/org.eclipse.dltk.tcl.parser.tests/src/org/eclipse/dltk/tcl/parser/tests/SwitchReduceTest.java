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

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.PerformanceMonitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionUtils;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class SwitchReduceTest extends TestCase {
	public Command createCommand001() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			Switch sw = factory.createSwitch();
			sw.setLowerBound(1);
			sw.setUpperBound(2);
			{
				Group arg = factory.createGroup();
				arg.setLowerBound(1);
				arg.setUpperBound(1);
				arg.setConstant("-id");

				{
					TypedArgument arg2 = factory.createTypedArgument();
					arg2.setType(ArgumentType.SCRIPT);
					arg2.setName("beta");
					arg2.setLowerBound(1);
					arg2.setUpperBound(1);
					arg.getArguments().add(arg2);
				}
				sw.getGroups().add(arg);
			}
			{
				Group arg = factory.createGroup();
				arg.setLowerBound(1);
				arg.setUpperBound(1);
				arg.setConstant("-value");
				{
					TypedArgument arg2 = factory.createTypedArgument();
					arg2.setType(ArgumentType.SCRIPT);
					arg2.setName("gamma");
					arg2.setLowerBound(1);
					arg2.setUpperBound(1);
					arg.getArguments().add(arg2);
				}
				sw.getGroups().add(arg);
			}
			command.getArguments().add(sw);
		}
		return command;
	}

	public Command createCommand002() throws Exception {
		DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

		Command command = factory.createCommand();

		command.setName("constants");
		{
			Switch sw = factory.createSwitch();
			sw.setLowerBound(1);
			sw.setUpperBound(1);
			{
				Group arg = factory.createGroup();
				arg.setLowerBound(1);
				arg.setUpperBound(1);
				arg.setConstant("-id");

				{
					TypedArgument arg2 = factory.createTypedArgument();
					arg2.setType(ArgumentType.SCRIPT);
					arg2.setName("beta");
					arg2.setLowerBound(1);
					arg2.setUpperBound(1);
					arg.getArguments().add(arg2);
				}
				sw.getGroups().add(arg);
			}
			{
				Group arg = factory.createGroup();
				arg.setLowerBound(1);
				arg.setUpperBound(1);
				arg.setConstant("-value");
				{
					TypedArgument arg2 = factory.createTypedArgument();
					arg2.setType(ArgumentType.SCRIPT);
					arg2.setName("gamma");
					arg2.setLowerBound(1);
					arg2.setUpperBound(1);
					arg.getArguments().add(arg2);
				}
				sw.getGroups().add(arg);
			}
			command.getArguments().add(sw);
		}
		return command;
	}

	public void testReplaceSwitch001() throws Exception {
		System.out.println("TEST:"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		Command[] commands = DefinitionUtils.reduceSwitches(createCommand001());
		TestCase.assertEquals(4, commands.length);
		for (int i = 0; i < commands.length; i++) {
			System.out.println("#" + i + ":"
					+ DefinitionUtils.convertToString(commands[i]));
		}
	}

	public void testReplaceSwitch002() throws Exception {
		System.out.println("TEST:"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		Command[] commands = DefinitionUtils.reduceSwitches(createCommand002());
		TestCase.assertEquals(2, commands.length);
		for (int i = 0; i < commands.length; i++) {
			System.out.println("#" + i + ":"
					+ DefinitionUtils.convertToString(commands[i]));
		}
	}

	NamespaceScopeProcessor processor;

	public void setUp() throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
	}

	public void testReplaceSwitch004() throws Exception {
		System.out.println("TEST:"
				+ Thread.currentThread().getStackTrace()[1].getMethodName());
		Command commands[] = processor.getCommandDefinition("open");
		PerformanceMonitor.getDefault().begin("Command reduction");
		for (int i = 0; i < commands.length; i++) {
			System.out.println("#ORIGINAL:" + i + ":"
					+ DefinitionUtils.convertToString(commands[i]));
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(DefinitionUtils.GENERATE_VARIANTS, true);
			Command[] rc = DefinitionUtils.reduceSwitches(commands[i], options);
			if (rc.length > 1) {
				for (int j = 0; j < rc.length; j++) {
					System.out.println(DefinitionUtils.convertToString(rc[j],
							true));
				}
			}
		}
		PerformanceMonitor.getDefault().end("Command reduction");
		PerformanceMonitor.getDefault().print();
	}
}
