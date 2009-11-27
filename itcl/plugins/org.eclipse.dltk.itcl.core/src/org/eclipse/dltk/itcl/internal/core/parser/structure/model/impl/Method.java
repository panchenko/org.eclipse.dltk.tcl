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
package org.eclipse.dltk.itcl.internal.core.parser.structure.model.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMethod;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.structure.AbstractTclCommandModelBuilder.Parameter;

public class Method extends Member implements IMethod {

	private MethodKind kind = MethodKind.METHOD;
	private final List<Parameter> parameters = new ArrayList<Parameter>();
	private List<TclArgument> bodies = null;

	public MethodKind getKind() {
		return kind;
	}

	public void setKind(MethodKind kind) {
		this.kind = kind;
	}

	public List<Parameter> getParameters() {
		return parameters;
	}

	public List<TclArgument> getBodies() {
		return bodies != null ? bodies : Collections.<TclArgument> emptyList();
	}

	public void addBody(TclArgument body) {
		if (bodies == null) {
			bodies = new ArrayList<TclArgument>();
		}
		bodies.add(body);
	}

}
