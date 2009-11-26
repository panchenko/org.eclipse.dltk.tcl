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
package org.eclipse.dltk.itcl.internal.core.parser.structure;

import org.eclipse.dltk.itcl.internal.core.parser.structure.IncrTclNames.IncrTclTypeInfo;
import org.eclipse.dltk.tcl.structure.ITclModelBuildContext;

public class IncrTclType {

	private static final String ATTR = IncrTclType.class.getName();

	private final String name;
	private final IncrTclTypeInfo info;

	/**
	 * @param name
	 * @param info
	 */
	public IncrTclType(String name, IncrTclTypeInfo info) {
		this.name = name;
		this.info = info;
	}

	/**
	 * @param context
	 */
	public void saveTo(ITclModelBuildContext context) {
		context.setAttribute(ATTR, this);
	}

}
