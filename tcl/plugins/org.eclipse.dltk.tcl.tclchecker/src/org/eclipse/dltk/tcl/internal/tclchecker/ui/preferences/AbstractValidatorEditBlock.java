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
package org.eclipse.dltk.tcl.internal.tclchecker.ui.preferences;

public abstract class AbstractValidatorEditBlock implements IValidatorEditBlock {

	private IValidatorDialogContext context;

	public final void init(IValidatorDialogContext context, Object object) {
		this.context = context;
		doInit(context, object);
	}

	protected IValidatorDialogContext getContext() {
		return context;
	}

	/**
	 * @param context
	 * @param object
	 */
	protected abstract void doInit(IValidatorDialogContext context,
			Object object);

	protected final void validate() {
		validate(null);
	}

	protected void validate(Object hint) {
		final IValidationHandler handler = context.getValidationHandler();
		if (handler != null) {
			handler.validate(hint);
		}
	}

}
