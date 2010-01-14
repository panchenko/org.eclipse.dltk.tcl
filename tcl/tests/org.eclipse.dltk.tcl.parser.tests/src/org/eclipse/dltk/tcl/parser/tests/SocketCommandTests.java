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
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class SocketCommandTests extends TestCase {
	NamespaceScopeProcessor processor;

	public void test001() throws Exception {
		String source = "socket $host $port";
		typedCheck(source, 0, 0);
	}

	public void test002() throws Exception {
		String source = "socket -server [list ::ftpd::PasvAccept $sock] 0";
		typedCheck(source, 0, 0);
	}

	public void test003() throws Exception {
		String source = "set data(sock2a) [socket -server [list ::ftpd::PasvAccept $sock] 0]";
		typedCheck(source, 0, 0);
	}

	public void test004() throws Exception {
		String source = "socket -server lala";
		typedCheck(source, 1, 0);
	}

	private void typedCheck(String source, int errs, int code) throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
		TclParser parser = TestUtils.createParser();
		TclErrorCollector errors = new TclErrorCollector();
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, module.size());
		// TclCommand tclCommand = module.get(0);
		// EList<TclArgument> arguments = tclCommand.getArguments();
		final int[] scripts = { 0 };
		TclParserUtils.traverse(module, new TclVisitor() {
			@Override
			public boolean visit(Script script) {
				scripts[0]++;
				return true;
			}
		});
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}
		TestCase.assertEquals(code, scripts[0]);
		TestCase.assertEquals(errs, errors.getCount());
	}
}
