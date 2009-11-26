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
package org.eclipse.dltk.tcl.structure;

import org.eclipse.dltk.tcl.ast.Node;

/**
 * @since 2.0
 */
@SuppressWarnings("serial")
public class TclModelProblem extends Exception {

	private final Node location;

	public TclModelProblem(String message) {
		super(message);
		this.location = null;
	}

	/**
	 * @param string
	 * @param command
	 */
	public TclModelProblem(String message, Node location) {
		super(message);
		this.location = location;
	}

}
