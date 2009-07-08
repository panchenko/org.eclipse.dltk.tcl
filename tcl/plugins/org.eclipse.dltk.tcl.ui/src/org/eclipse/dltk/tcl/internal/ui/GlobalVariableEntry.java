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
package org.eclipse.dltk.tcl.internal.ui;

import org.eclipse.core.runtime.Assert;

/**
 * @since 2.0
 */
public class GlobalVariableEntry {
	private final String name;
	private String value;

	public GlobalVariableEntry(GlobalVariableEntry var) {
		this(var.getName(), var.getValue());
	}

	public GlobalVariableEntry(String name, String value) {
		Assert.isNotNull(name);
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final GlobalVariableEntry other = (GlobalVariableEntry) obj;
		return name.equals(other.name);
	}

	@Override
	public String toString() {
		return this.name + "=" + this.value; //$NON-NLS-1$
	}
}
