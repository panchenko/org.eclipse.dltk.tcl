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
package org.eclipse.dltk.tcl.activestatedebugger;

public enum ErrorAction {
	STOP_ALWAYS("stopAlways", Messages.ErrorAction_stopAlways), //$NON-NLS-1$
	STOP_UNCAUGHT("stopUncaught", Messages.ErrorAction_stopUncaught), //$NON-NLS-1$
	STOP_NEVER("stopNever", Messages.ErrorAction_stopNever); //$NON-NLS-1$

	private final String value;
	private final String caption;

	ErrorAction(String value, String caption) {
		this.value = value;
		this.caption = caption;
	}

	public String getValue() {
		return value;
	}

	public String getCaption() {
		return caption;
	}

	/**
	 * @return
	 */
	public static String[] getNames() {
		final ErrorAction[] errorActions = values();
		final String[] names = new String[errorActions.length];
		for (int i = 0; i < errorActions.length; ++i) {
			names[i] = errorActions[i].getValue();
		}
		return names;
	}

	/**
	 * Returns the {@link ErrorAction} instance matching the specified value or
	 * <code>null</code>.
	 * 
	 * @param value
	 * @return
	 */
	public static ErrorAction decode(String value) {
		if (value != null) {
			try {
				return ErrorAction.valueOf(value);
			} catch (IllegalArgumentException e) {
				// fall thru
			}
		}
		return null;
	}

}
