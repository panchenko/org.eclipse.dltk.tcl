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

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;

public interface ITclModelBuildContext {

	public interface ITclModelHandler {
		void leave(ISourceElementRequestor requestor);
	}

	CharSequence getSource();

	/**
	 * @param beginIndex
	 * @param endIndex
	 * @return
	 */
	String substring(int beginIndex, int endIndex);

	ISourceElementRequestor getRequestor();

	ITclErrorReporter getErrorReporter();

	void addHandler(TclCommand command, ITclModelHandler handler);

	<E> E get(Class<E> clazz);

	void parse(String source, int offset);

	void leave(TclCommand command);

}
