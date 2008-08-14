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
package org.eclipse.dltk.tcl.parser;

import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;

public class TclVisitor {
	public boolean visit(TclCommand command) {
		return true;
	}

	public void endVisit(TclCommand command) {
	}

	public boolean visit(StringArgument arg) {
		return true;
	}

	public void endVisit(StringArgument arg) {
	}

	public boolean visit(Script script) {
		return true;
	}

	public void endVisit(Script script) {
	}

	public boolean visit(Substitution substitution) {
		return true;
	}

	public void endVisit(Substitution substitution) {
	}

	public boolean visit(TclArgumentList list) {
		return true;
	}

	public void endVisit(TclArgumentList list) {
	}

	public boolean visit(ComplexString list) {
		return true;
	}

	public void endVisit(ComplexString list) {
	}

	public boolean visit(VariableReference list) {
		return true;
	}

	public void endVisit(VariableReference list) {
	}
}
