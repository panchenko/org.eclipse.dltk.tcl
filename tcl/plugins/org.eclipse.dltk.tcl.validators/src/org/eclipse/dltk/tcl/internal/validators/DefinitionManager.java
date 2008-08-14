/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/

package org.eclipse.dltk.tcl.internal.validators;

import java.io.IOException;
import java.net.URL;

import org.eclipse.dltk.tcl.definitions.Scope;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionExtensionManager;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionLoader;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class DefinitionManager {
	private static NamespaceScopeProcessor tempProcessor = null;

	public static NamespaceScopeProcessor createProcessor() {
		initialize();
		return tempProcessor.copy();
	}

	private static void initialize() {
		if (tempProcessor == null) {
			tempProcessor = new NamespaceScopeProcessor();
			URL[] locations = DefinitionExtensionManager.getInstance()
					.getLocations();
			for (int i = 0; i < locations.length; i++) {
				try {
					Scope scope = DefinitionLoader
							.loadDefinitions(locations[i]);
					tempProcessor.addScope(scope);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
