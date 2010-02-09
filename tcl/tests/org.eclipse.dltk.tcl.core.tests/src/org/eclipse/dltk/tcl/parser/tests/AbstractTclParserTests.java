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
package org.eclipse.dltk.tcl.parser.tests;

import java.io.IOException;
import java.net.URL;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.env.ModuleSource;
import org.eclipse.dltk.compiler.problem.ProblemCollector;
import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.core.DLTKLanguageManager;
import org.eclipse.dltk.tcl.core.TclNature;
import org.eclipse.dltk.tcl.core.tests.model.Activator;

import junit.framework.TestCase;

public abstract class AbstractTclParserTests extends TestCase {

	protected char[] readResource(String resourceName) throws IOException {
		final URL resource = Activator.getDefault().getBundle().getEntry(
				resourceName);
		assertNotNull(resourceName + " is not found", resource); //$NON-NLS-1$
		return Util.getInputStreamAsCharArray(resource.openStream(), -1,
				AllParseTests.CHARSET);
	}

	protected ISourceParser getParser() {
		return DLTKLanguageManager.getSourceParser(TclNature.NATURE_ID);
	}

	protected ProblemCollector parse(final String resourceName)
			throws IOException {
		final ProblemCollector collector = new ProblemCollector();
		getParser().parse(new ModuleSource(readResource(resourceName)),
				collector);
		return collector;
	}

}
