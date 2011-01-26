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
package org.eclipse.dltk.tcl.parser;

import org.eclipse.dltk.compiler.problem.ProblemSeverity;

public class TclError {
	private int code;
	private String message;
	private int start;
	private int end;
	private ProblemSeverity errorKind;
	private String[] extraArguments;

	public TclError(int code, String message, String[] extraArguments,
			int start, int end, ProblemSeverity kind) {
		this.code = code;
		this.message = message;
		this.extraArguments = extraArguments;
		this.start = start;
		this.end = end;
		this.errorKind = kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + end;
		result = prime * result + (message == null ? 0 : message.hashCode());
		result = prime * result + start;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TclError other = (TclError) obj;
		if (code != other.code)
			return false;
		if (end != other.end)
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public ProblemSeverity getErrorKind() {
		return errorKind;
	}

	public void setErrorKind(ProblemSeverity errorKind) {
		this.errorKind = errorKind;
	}

	public String[] getExtraArguments() {
		return extraArguments;
	}

	public void setExtraArguments(String[] extraArguments) {
		this.extraArguments = extraArguments;
	}

	@Override
	public String toString() {
		return "[" + start + ".." + end + "]" + code + ":" + message;
	}
}
