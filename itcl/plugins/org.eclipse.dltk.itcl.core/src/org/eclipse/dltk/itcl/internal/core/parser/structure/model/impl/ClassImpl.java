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
import java.util.List;
import java.util.Stack;

import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IClass;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMember;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMethod;
import org.eclipse.dltk.itcl.internal.core.parser.structure.model.IMember.Visibility;

public class ClassImpl implements IClass {

	private final List<String> superclasses = new ArrayList<String>();
	private final List<IMember> members = new ArrayList<IMember>();
	private final Stack<Visibility> visibilities = new Stack<Visibility>();
	private String name;

	public void addMember(IMember member) {
		members.add(member);
	}

	public void addSuperclass(String name) {
		superclasses.add(name);
	}

	public Visibility peekVisibility() {
		return visibilities.isEmpty() ? Visibility.PRIVATE : visibilities
				.peek();
	}

	public Visibility popVisibility() {
		return visibilities.pop();
	}

	public void pushVisibility(Visibility visibility) {
		visibilities.push(visibility);
	}

	public String[] getSuperClasses() {
		return superclasses.toArray(new String[superclasses.size()]);
	}

	public List<IMember> getMembers() {
		return members;
	}

	public IMethod findMethod(String name) {
		for (IMember member : members) {
			if (member instanceof IMethod && name.equals(member.getName())) {
				return (IMethod) member;
			}
		}
		return null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param namespace
	 */
	public void setName(String name) {
		this.name = name;
	}

}
