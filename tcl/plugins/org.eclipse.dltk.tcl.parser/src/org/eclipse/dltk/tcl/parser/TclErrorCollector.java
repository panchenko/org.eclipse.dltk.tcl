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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.dltk.compiler.problem.DefaultProblem;
import org.eclipse.dltk.compiler.problem.DefaultProblemFactory;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.utils.TextUtils.DefaultSourceLineTracker;

public class TclErrorCollector implements ITclErrorReporter {
	private Set<TclError> errors = new HashSet<TclError>();

	public void report(int code, String message, String[] extraMessage,
			int start, int end, int kind) {
		boolean insertNewError = true;
		TclError errorToReplace = null;
		for (TclError error : errors) {
			if (error.getStart() == start && error.getEnd() == end) {
				if (error.getCode() < code) {
					errorToReplace = error;
					break;
				} else {
					insertNewError = false;
					break;
				}
			}
		}
		if (errorToReplace != null)
			errors.remove(errorToReplace);
		if (insertNewError)
			errors.add(new TclError(code, message, extraMessage, start, end,
					kind));
	}

	public void report(TclError e) {
		boolean insertNewError = true;
		TclError errorToReplace = null;
		int start = e.getStart();
		int end = e.getEnd();
		int code = e.getCode();
		for (TclError error : errors) {
			if (error.getStart() == start && error.getEnd() == end) {
				if (error.getCode() < code) {
					errorToReplace = error;
					break;
				} else {
					insertNewError = false;
					break;
				}
			}
		}
		if (errorToReplace != null)
			errors.remove(errorToReplace);
		if (insertNewError)
			errors.add(e);
	}

	public void addAll(TclErrorCollector collector) {
		for (TclError e : collector.errors) {
			report(e);
		}
	}

	public void reportAll(ITclErrorReporter reporter) {
		if (reporter != null) {
			for (TclError error : this.errors) {
				reporter.report(error.getCode(), error.getMessage(), error
						.getExtraArguments(), error.getStart(), error.getEnd(),
						error.getErrorKind());
			}
		}
	}

	public void reportAll(final IProblemReporter reporter,
			final ISourceLineTracker tracker) {
		reportAll(new ITclErrorReporter() {
			public void report(int code, String message, String[] extraMessage,
					int start, int end, int kind) {
				reporter
						.reportProblem(new DefaultProblem(
								message,
								code,
								extraMessage,
								kind == ITclErrorReporter.ERROR ? ProblemSeverities.Error
										: ProblemSeverities.Warning, start,
								end, tracker.getLineNumberOfOffset(start)));
			}
		});
	}

	public int getCount() {
		return this.errors.size();
	}

	public TclError[] getErrors() {
		return this.errors.toArray(new TclError[this.errors.size()]);
	}
}
