/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.tests;

import java.io.IOException;

import org.eclipse.dltk.compiler.problem.ProblemCollector;

public class TclIfProcessorTests extends AbstractTclParserTests {

	public void testElseIf() throws IOException {
		final ProblemCollector collector = parse("scripts/elseif.tcl"); //$NON-NLS-1$
		if (collector.hasErrors()) {
			fail(collector.getErrors().toString());
		}
	}

}
