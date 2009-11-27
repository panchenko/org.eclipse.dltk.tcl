/*******************************************************************************
 * Copyright (c) 2009 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.itcl.internal.core.parser.structure.model;

import java.util.List;

import org.eclipse.dltk.itcl.internal.core.IIncrTclModifiers;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder.Parameter;

public interface IMethod extends IMember {

	enum MethodKind {
		PROC(IIncrTclModifiers.AccIncrTclProc), METHOD(0), CONSTRUCTOR(
				IIncrTclModifiers.AccConstructor, true), DESTRUCTOR(
				IIncrTclModifiers.AccDestructor, true);

		private final int modifiers;
		private final boolean maskVisibility;

		MethodKind(int modifiers) {
			this(modifiers, false);
		}

		MethodKind(int modifiers, boolean maskVisibility) {
			this.modifiers = modifiers;
			this.maskVisibility = maskVisibility;
		}

		public int getModifiers() {
			return modifiers;
		}

		public boolean isMaskVisibility() {
			return maskVisibility;
		}
	}

	MethodKind getKind();

	void setKind(MethodKind kind);

	List<Parameter> getParameters();

	List<TclArgument> getBodies();

	void addBody(TclArgument tclArgument);

}
