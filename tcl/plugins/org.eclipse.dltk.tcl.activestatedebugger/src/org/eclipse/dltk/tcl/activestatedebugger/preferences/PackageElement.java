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
package org.eclipse.dltk.tcl.activestatedebugger.preferences;

class PackageElement {
	final String packageName;

	public PackageElement(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public int hashCode() {
		return packageName.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PackageElement) {
			final PackageElement other = (PackageElement) obj;
			return packageName.equals(other.packageName);
		} else {
			return false;
		}
	}
}