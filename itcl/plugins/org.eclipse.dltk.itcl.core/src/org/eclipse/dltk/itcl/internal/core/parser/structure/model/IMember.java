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

import org.eclipse.dltk.ast.Modifiers;
import org.eclipse.dltk.tcl.ast.Node;

public interface IMember {

	public enum Visibility {
		PUBLIC(Modifiers.AccPublic), PROTECTED(Modifiers.AccProtected), PRIVATE(
				Modifiers.AccPrivate);

		private int modifiers;

		Visibility(int modifiers) {
			this.modifiers = modifiers;
		}

		public int getModifiers() {
			return modifiers;
		}
	}

	String getName();

	void setName(String name);

	int getNameStart();

	int getNameEnd();

	int getStart();

	int getEnd();

	void setNameStart(int nameStart);

	void setNameEnd(int nameEnd);

	void setStart(int start);

	void setEnd(int end);

	void setNameRange(Node node);

	void setRange(Node node);

	Visibility getVisibility();

	void setVisibility(Visibility visibility);

}
