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
package org.eclipse.dltk.tcl.internal.ui.text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.tcl.internal.ui.TclUI;
import org.eclipse.dltk.ui.text.templates.CodeTemplateAccess;
import org.eclipse.dltk.ui.text.templates.CodeTemplateCategory;
import org.eclipse.dltk.ui.text.templates.ICodeTemplateCategory;
import org.eclipse.jface.text.templates.ContextTypeRegistry;
import org.eclipse.jface.text.templates.TemplateContextType;
import org.eclipse.ui.editors.text.templates.ContributionContextTypeRegistry;

public class TclCodeTemplateAccess extends CodeTemplateAccess {

	/**
	 * The key to store customized code templates.
	 */
	private static final String CODE_TEMPLATES_KEY = "org.eclipse.dltk.tcl.text.custom_code_templates"; //$NON-NLS-1$

	public TclCodeTemplateAccess() {
		super(TclUI.PLUGIN_ID, CODE_TEMPLATES_KEY, TclUI.getDefault()
				.getPreferenceStore());
	}

	protected ContextTypeRegistry createContextTypeRegistry() {
		final ContributionContextTypeRegistry registry = new ContributionContextTypeRegistry();
		registry.addContextType("org.eclipse.dltk.tcl.text.template.type.tcl");
		return registry;
	}

	private ICodeTemplateCategory[] categories = null;

	public ICodeTemplateCategory[] getCategories() {
		if (categories == null) {
			Iterator i = getCodeTemplateContextRegistry().contextTypes();
			List contextTypes = new ArrayList();
			while (i.hasNext()) {
				contextTypes.add(i.next());
			}
			categories = new ICodeTemplateCategory[] { new CodeTemplateCategory(
					"Files", true, (TemplateContextType[]) contextTypes
							.toArray(new TemplateContextType[contextTypes
									.size()])) };
		}
		return categories;
	}

	public ICodeTemplateCategory getCategoryOfContextType(String contextTypeId) {
		return getCategories()[0];
	}

}
