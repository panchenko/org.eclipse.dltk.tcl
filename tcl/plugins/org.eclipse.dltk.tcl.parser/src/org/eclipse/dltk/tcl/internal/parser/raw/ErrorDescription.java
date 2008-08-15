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
package org.eclipse.dltk.tcl.internal.parser.raw;

public class ErrorDescription {

	public static final int ERROR = 0;
	public static final int WARNING = 1;

	private String message;
	private int position;
	private int end = -1;
	private int kind;

	public ErrorDescription(String msg, int pos, int knd) {
		this.message = msg;
		this.position = pos;
		this.kind = knd;
	}
	public ErrorDescription(String msg, int pos, int end, int knd) {
		this.message = msg;
		this.position = pos;
		this.end = end;
		this.kind = knd;
	}

	protected int getKind() {
		return kind;
	}

	protected String getMessage() {
		return message;
	}

	protected int getPosition() {
		return position;
	}
	protected int getEnd() {
		if(end == -1) {
			return position;
		}
		return end;
	}

	public String toString() {
		return getMessage() + " at " + getPosition(); //$NON-NLS-1$
	}
}
