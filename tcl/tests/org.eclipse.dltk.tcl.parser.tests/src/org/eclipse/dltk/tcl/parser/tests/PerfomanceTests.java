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
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.internal.parser.TclSourceParser;
import org.eclipse.dltk.tcl.parser.ITclParserOptions;
import org.eclipse.dltk.tcl.parser.PerformanceMonitor;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class PerfomanceTests extends TestCase {
	
	public void testBigFilePerfomance() throws Exception {
		PerformanceMonitor.getDefault().begin("LOAD BIG FILE:");
		String contents = BigFileGenerator.generateBigFile001();
		PerformanceMonitor.getDefault().end("LOAD BIG FILE:");

		PerformanceMonitor.getDefault().begin("RAW PARSE BIG FILE:");
		TclParser parser = new TclParser();
		parser.parse(contents);
		PerformanceMonitor.getDefault().end("RAW PARSE BIG FILE:");

		// Parsing with processors and matching.
		NamespaceScopeProcessor processor = new NamespaceScopeProcessor();

		Scope scope = DefinitionLoader
				.loadDefinitions(new URL(
						"platform:///plugin/org.eclipse.dltk.tcl.tcllib/definitions/builtin.xml"));
		TestCase.assertNotNull(scope);
		processor.addScope(scope);
		parser = new TclParser("8.5");
		TclErrorCollector errors = new TclErrorCollector();
		parser.setOptionValue(ITclParserOptions.REPORT_UNKNOWN_AS_ERROR, true);
		PerformanceMonitor.getDefault().begin("PARSE BIG FILE:");
		List<TclCommand> module = parser.parse(contents, errors, processor);
		PerformanceMonitor.getDefault().end("PARSE BIG FILE:");

		PerformanceMonitor.getDefault().begin("OLD PARSE BIG FILE:");
		TclSourceParser p = new TclSourceParser();
		p.parse(null, contents.toCharArray(), null);
		PerformanceMonitor.getDefault().end("OLD PARSE BIG FILE:");
		// if (errors.getCount() > 0) {
		// TestUtils.outErrors(contents, errors);
		// }
		PerformanceMonitor.getDefault().print();
	}
}
