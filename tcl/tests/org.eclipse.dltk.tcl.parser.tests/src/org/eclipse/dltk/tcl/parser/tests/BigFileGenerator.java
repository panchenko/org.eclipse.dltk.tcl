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
package org.eclipse.dltk.tcl.parser.tests;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.parser.PerformanceMonitor;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionUtils;
import org.eclipse.dltk.tcl.parser.definitions.NamespaceScopeProcessor;

public class BigFileGenerator {
	public static String generateBigFile001() throws Exception {
		StringBuffer buffer = new StringBuffer();
		NamespaceScopeProcessor processor = DefinitionManager.getInstance()
				.createProcessor();
		Command[] commands = processor.getCommands();
		PerformanceMonitor.getDefault().begin("Command reduction");
		for (int i = 0; i < commands.length; i++) {
			Map<String, Object> options = new HashMap<String, Object>();
			options.put(DefinitionUtils.GENERATE_VARIANTS, true);
			options.put(DefinitionUtils.SWITCH_COUNT, 3);
			Command[] rc = DefinitionUtils.reduceSwitches(commands[i], options);
			if (rc.length > 1) {
				for (int j = 0; j < rc.length; j++) {
					String cmd = DefinitionUtils.convertToString(rc[j], true);
					buffer.append(cmd);
					buffer.append("\n");
				}
			}
		}
		return buffer.toString();
	}
}
