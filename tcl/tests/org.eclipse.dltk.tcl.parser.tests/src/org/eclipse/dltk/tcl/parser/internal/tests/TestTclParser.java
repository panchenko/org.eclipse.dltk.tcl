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

import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
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

	protected static void verify(boolean value) {
		if (!value) {
			Assert.fail();
		}
	}

	public static void verifyLiterals(List<TclCommand> commands,
			final String content, final int offset) {
		// TODO only if there were no syntax errors
		TclParserUtils.traverse(commands, new TclVisitor() {

			@Override
			public boolean visit(Script script) {
				verify(script.getContentStart() >= script.getStart());
				verify(script.getContentEnd() <= script.getEnd());
				for (TclCommand command : script.getCommands()) {
					verify(command.getStart() >= script.getStart()
							&& command.getEnd() <= script.getEnd());
				}
				return super.visit(script);
			}

			@Override
			public boolean visit(TclArgumentList list) {
				for (TclArgument argument : list.getArguments()) {
					verify(argument.getStart() >= list.getStart()
							&& argument.getEnd() <= list.getEnd());
				}
				return super.visit(list);
			}

			@Override
			public boolean visit(TclCommand command) {
				TclArgument name = command.getName();
				verify(name.getStart() >= command.getStart()
						&& name.getEnd() <= command.getEnd());
				for (TclArgument argument : command.getArguments()) {
					verify(argument.getStart() >= command.getStart()
							&& argument.getEnd() <= command.getEnd());
				}
				return super.visit(command);
			}

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
