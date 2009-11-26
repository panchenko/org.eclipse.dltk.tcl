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
package org.eclipse.dltk.xotcl.internal.core.parser.structure;

import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;
import org.eclipse.dltk.xotcl.internal.core.parser.structure.XOTclNames.XOTclTypeInfo;

public class XOTclType {

	private static final String ATTR = XOTclType.class.getName();

	/**
	 * @param context
	 * @return
	 */
	public static XOTclType get(ITclModelBuildContext context) {
		return (XOTclType) context.getAttribute(ATTR);
	}

	private final String name;
	private final XOTclTypeInfo info;

	/**
	 * @param name
	 * @param info
	 * @param nameEnd
	 * @param nameStart
	 */
	XOTclType(String name, XOTclTypeInfo info) {
		this.name = name;
		this.info = info;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param context
	 */
	public void saveTo(ITclModelBuildContext context) {
		context.setAttribute(ATTR, this);
	}

}
