/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.internal.tests;

import java.util.List;

import junit.framework.Assert;

import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.parser.definitions.IScopeProcessor;

public class TestTclParser extends TclParser {

	public TestTclParser() {
		super();
	}

	public TestTclParser(String version) {
		super(version);
	}

	@Override
	public List<TclCommand> parse(String source, ITclErrorReporter reporter,
			IScopeProcessor scopeProcessor) {
		final List<TclCommand> commands = super.parse(source, reporter,
				scopeProcessor);
		verifyLiterals(commands, source, getGlobalOffset());
		return commands;
	}

	public static void verifyLiterals(List<TclCommand> commands,
			final String content, final int offset) {
		// TODO only if there are no syntax errors
		TclParserUtils.traverse(commands, new TclVisitor() {
			@Override
			public boolean visit(StringArgument arg) {
				final String expected = content.substring(arg.getStart()
						- offset, arg.getEnd() - offset);
				if (!expected.equals(arg.getRawValue()))
					Assert.assertEquals(expected, arg.getRawValue());
				return super.visit(arg);
			}
		});
	}

}
