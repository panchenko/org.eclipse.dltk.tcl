/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Sergey Kanshin)
 *******************************************************************************/
package org.eclipse.dltk.tcl.formatter.internal;

import java.util.List;

import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Node;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;

public class FormatterIndentDetector extends TclVisitor {

	private int level;
	private final int offset;
	private boolean indentDetected;
	private int result;

	public FormatterIndentDetector(int offset) {
		this.offset = offset;
	}

	public int getLevel(List<TclCommand> commands) {
		result = level = 0;
		indentDetected = false;
		TclParserUtils.traverse(commands, this);
		return result;
	}

	@Override
	public boolean visit(Script script) {
		++level;
		return detect(script);
	}

	@Override
	public void endVisit(Script script) {
		--level;
	}

	@Override
	public boolean visit(ComplexString list) {
		return detect(list);
	}

	@Override
	public boolean visit(StringArgument arg) {
		return detect(arg);
	}

	@Override
	public boolean visit(Substitution substitution) {
		return detect(substitution);
	}

	@Override
	public boolean visit(TclArgumentList list) {
		return detect(list);
	}

	@Override
	public boolean visit(TclCommand command) {
		return detect(command);
	}

	@Override
	public boolean visit(VariableReference list) {
		return detect(list);
	}

	private boolean detect(Node node) {
		if (!indentDetected && node.getStart() >= offset) {
			result = level;
			indentDetected = true;
		}
		return !indentDetected;
	}
}
