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

package org.eclipse.dltk.tcl.internal.validators.checks;

import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.mixin.IMixinElement;
import org.eclipse.dltk.core.mixin.IMixinRequestor;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.internal.core.search.mixin.TclMixinModel;
import org.eclipse.dltk.tcl.internal.core.search.mixin.model.TclField;
import org.eclipse.dltk.tcl.internal.validators.ICheckKinds;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;
import org.eclipse.dltk.tcl.validators.ITclCheck;

public class UndefinedVariableCheck implements ITclCheck {

	public UndefinedVariableCheck() {
	}

	public void checkCommands(List<TclCommand> commands,
			final ITclErrorReporter reporter, Map<String, String> options,
			final IScriptProject project) {
		TclParserUtils.traverse(commands, new TclVisitor() {
			@Override
			public boolean visit(VariableReference ref) {
				String name = ref.getName();
				IMixinElement[] elements = TclMixinModel.getInstance()
						.getMixin(project).find(
								name.replaceAll("::",
										IMixinRequestor.MIXIN_NAME_SEPARATOR));
				String realName = name;
				if (realName.indexOf("::") != -1) {
					realName = realName
							.substring(realName.lastIndexOf("::") + 2);
				}
				boolean found = false;
				for (int i = 0; i < elements.length; i++) {
					Object[] objects = elements[i].getAllObjects();
					for (int j = 0; j < objects.length; j++) {
						if (objects[j] instanceof TclField) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					reporter.report(ICheckKinds.CHECK_UNDEFINED_VARIABLE,
							"Use of undefined variable:" + realName, null, ref
											.getStart(),
							ref.getEnd(), ITclErrorReporter.WARNING);
				}
				return true;
			}
		});
	}
}
