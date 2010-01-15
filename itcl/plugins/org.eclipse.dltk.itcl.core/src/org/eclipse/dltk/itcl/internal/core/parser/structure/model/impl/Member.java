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

import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMember;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IRange;
import org.eclipse.dltk.tcl.ast.Node;

public abstract class Member implements IMember {

	private String name;
	private Visibility visibility = Visibility.PRIVATE;
	private int nameStart;
	private int nameEnd;
	private int start;
	private int end;

	public int getNameStart() {
		return nameStart;
	}

	public void setNameStart(int nameStart) {
		this.nameStart = nameStart;
	}

	public int getNameEnd() {
		return nameEnd;
	}

	public void setNameEnd(int nameEnd) {
		this.nameEnd = nameEnd;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public void setNameRange(Node node) {
		setNameStart(node.getStart());
		setNameEnd(node.getEnd() - 1);
	}

	public void setRange(Node node) {
		setStart(node.getStart());
		setEnd(node.getEnd());
	}

	public void setRange(IRange range) {
		setStart(range.getStart());
		setEnd(range.getEnd());
	}

	public String getName() {
		return name;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setVisibility(Visibility visibility) {
		this.visibility = visibility;
	}

}
