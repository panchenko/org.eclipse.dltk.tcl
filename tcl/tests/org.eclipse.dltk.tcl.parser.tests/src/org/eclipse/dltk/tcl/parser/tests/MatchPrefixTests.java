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
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.impl.TclArgumentListImpl;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;
import org.eclipse.emf.common.util.EList;

public class MatchPrefixTests extends TestCase {
	NamespaceScopeProcessor processor;

	public void test001() throws Exception {
		String source = "array names arg";
		typedCheck(source, 0, 0);
	}

	public void test002() throws Exception {
		String source = "array name arg";
		typedCheck(source, 0, 0);
	}

	public void test003() throws Exception {
		String source = "array na arg";
		typedCheck(source, 0, 0);
	}

	public void test005() throws Exception {
		String source = "array n arg";
		typedCheck(source, 1, 0);
	}

	public void test006() throws Exception {
		String source = "array name";
		typedCheck(source, 1, 0);
	}

	public void test007() throws Exception {
		String source = "fconfigure stdin -blocking";
		typedCheck(source, 0, 0);
	}

	public void test008() throws Exception {
		String source = "fconfigure stdin -block";
		typedCheck(source, 0, 0);
	}

	public void test009() throws Exception {
		String source = "fconfigure stdin -b";
		typedCheck(source, 1, 0);
	}

	public void test010() throws Exception {
		String source = "fconfigure stdin -blockingg";
		typedCheck(source, 1, 0);
	}

	private void typedCheck(String source, int errs, int code) throws Exception {
		processor = DefinitionManager.getInstance().createProcessor();
		TclParser parser = TestUtils.createParser("8.4");
		TclErrorCollector errors = new TclErrorCollector();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, true);
		List<TclCommand> module = parser.parse(source, errors, processor);
		TestCase.assertEquals(1, module.size());
		TclCommand tclCommand = module.get(0);
		EList<TclArgument> args = tclCommand.getArguments();
		int scripts = 0;
		for (int i = 0; i < args.size(); i++) {
			if (args.get(i) instanceof Script) {
				scripts++;
				TestUtils.outCode(source, args.get(i).getStart(), args.get(i)
						.getEnd());
			}
			if (args.get(i) instanceof TclArgumentListImpl) {
				EList<TclArgument> innerArgs = ((TclArgumentList) args.get(i))
						.getArguments();
				for (int k = 0; k < innerArgs.size(); k++) {
					if (innerArgs.get(k) instanceof Script) {
						scripts++;
						TestUtils.outCode(source, innerArgs.get(i).getStart(),
								innerArgs.get(i).getEnd());
					}
				}
			}

		}
		if (errors.getCount() > 0) {
			TestUtils.outErrors(source, errors);
		}

		TestCase.assertEquals(code, scripts);
		TestCase.assertEquals(errs, errors.getCount());
	}
}
