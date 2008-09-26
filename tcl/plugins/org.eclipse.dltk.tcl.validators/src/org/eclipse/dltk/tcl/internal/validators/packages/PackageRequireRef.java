/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.internal.validators.packages;

public class PackageRequireRef {
	final int start;
	final int end;
	final String name;

	/**
	 * @param pkg
	 * @param start
	 * @param end
	 */
	public PackageRequireRef(String pkg, int start, int end) {
		this.end = end;
		this.name = pkg;
		this.start = start;
	}

}
