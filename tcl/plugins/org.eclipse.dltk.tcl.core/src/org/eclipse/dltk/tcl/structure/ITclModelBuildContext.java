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

import java.util.List;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;

/**
 * @since 2.0
 */
public interface ITclModelBuildContext {

	int NO_TRAVERSE = 1;

	public interface ITclModelHandler {
		void leave(ISourceElementRequestor requestor);
	}

	public interface ITclParserInput {
		String getContent();

		int getStart();

		int getEnd();
	}

	ISourceElementRequestor getRequestor();

	ITclErrorReporter getErrorReporter();

	void addHandler(TclCommand command, ITclModelHandler handler);

	<E> E get(Class<E> clazz);

	List<TclCommand> parse(String source, int offset);

	Script parse(TclArgument script);

	Script parse(TclArgument script, int options);

	void leave(TclCommand command);

	void enterNamespace(ITclTypeHandler namespace);

	void leaveNamespace(ITclTypeHandler namespace);

	Object getAttribute(String name);

	void setAttribute(String name, Object value);

}
