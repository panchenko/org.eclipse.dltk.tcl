/*******************************************************************************
 * Copyright (c) 2010 xored software, Inc.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Alex Panchenko)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.structure;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.IParameter;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.tests.model.AbstractModelTests;
import org.eclipse.dltk.tcl.core.tests.model.Activator;

public class SimpleStructureParserTests extends AbstractModelTests {

	private static final String PROJECT_NAME = "Structure";

	public SimpleStructureParserTests(String name) {
		super(Activator.PLUGIN_ID, name);
	}

	public static Test suite() {
		return new Suite(SimpleStructureParserTests.class);
	}

	@Override
	public void setUpSuite() throws Exception {
		super.setUpSuite();
		setUpScriptProject(PROJECT_NAME);
	}

	@Override
	public void tearDownSuite() throws Exception {
		deleteProject(PROJECT_NAME);
		super.tearDownSuite();
	}

	public void test1() throws ModelException {
		final ISourceModule module = DLTKCore
				.createSourceModuleFrom(getProject(PROJECT_NAME).getFile(
						new Path("src/structure1.tcl")));
		assertTrue(module.exists());
		final List<IMethod> methods = new ArrayList<IMethod>();
		module.accept(new IModelElementVisitor() {
			public boolean visit(IModelElement element) {
				if (element.getElementType() == IModelElement.METHOD) {
					methods.add((IMethod) element);
				}
				return true;
			}
		});
		assertEquals(1, methods.size());
		assertEquals("hello", methods.get(0).getElementName());
		IParameter[] parameters = methods.get(0).getParameters();
		assertEquals(1, parameters.length);
		assertEquals("name", parameters[0].getName());
		assertNull(parameters[0].getType());
		assertEquals("world", parameters[0].getDefaultValue());
	}

}
