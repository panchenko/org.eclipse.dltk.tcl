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

import java.net.URL;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclispe.dltk.tcl.parser.internal.tests.Activator;

public class LoadDefinitionTests extends TestCase {

	
	public void testLoad001() throws Exception {
		TestScopeProcessor processor = new TestScopeProcessor();
		Scope scope = DefinitionLoader
				.loadDefinitions(new URL(
						"platform:///plugin/org.eclipse.dltk.tcl.parser.tests/definitions/test0.xml"));
		TestCase.assertNotNull(scope);
		processor.add(scope);
		Command[] setCommand = processor.getCommandDefinition("set");
		TestCase.assertNotNull(setCommand[0]);
		TestCase.assertEquals("set", setCommand[0].getName());
		Command[] unsetCommand = processor.getCommandDefinition("unset");
		TestCase.assertNotNull(unsetCommand[0]);
		TestCase.assertEquals("unset", unsetCommand[0].getName());
	}

	
	public void testLoad002() throws Exception {
		TestScopeProcessor processor = new TestScopeProcessor();
		Scope scope = DefinitionLoader.loadDefinitions(Activator.getDefault()
				.getBundle().getEntry("/definitions/test0.xml"));
		TestCase.assertNotNull(scope);
		processor.add(scope);
		Command[] setCommand = processor.getCommandDefinition("set");
		TestCase.assertNotNull(setCommand);
		TestCase.assertEquals("set", setCommand[0].getName());
		Command[] unsetCommand = processor.getCommandDefinition("unset");
		TestCase.assertNotNull(unsetCommand);
		TestCase.assertEquals("unset", unsetCommand[0].getName());
	}
}
