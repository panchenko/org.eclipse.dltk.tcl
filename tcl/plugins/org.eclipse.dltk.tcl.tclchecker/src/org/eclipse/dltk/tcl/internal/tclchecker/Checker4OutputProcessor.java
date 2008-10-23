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
package org.eclipse.dltk.tcl.internal.tclchecker;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.validators.core.IValidatorOutput;
import org.eclipse.osgi.util.NLS;

public class Checker4OutputProcessor extends AbstractOutputProcessor {

	private static final String CHECKING = "checking:"; //$NON-NLS-1$

	private static final String SCANNING = "scanning:"; //$NON-NLS-1$

	private static final String REGEX = "((?:\\w:)?[^:]+):(\\d+)\\s+\\((\\w+)\\)\\s+(.*)"; //$NON-NLS-1$

	private final Pattern pattern = Pattern.compile(REGEX);

	private final IValidatorOutput console;
	private final ITclCheckerReporter reporter;

	public Checker4OutputProcessor(IProgressMonitor monitor,
			IValidatorOutput console, ITclCheckerReporter reporter) {
		super(monitor);
		this.console = console;
		this.reporter = reporter;
	}

	public TclCheckerProblem parseProblem(String problem) {
		Matcher matcher = pattern.matcher(problem);

		if (!matcher.find())
			return null;

		String file = matcher.group(1);
		int lineNumber = Integer.parseInt(matcher.group(2));
		String messageID = matcher.group(3);
		String message = matcher.group(4);

		return new TclCheckerProblem(file, lineNumber, messageID, message);
	}

	private int scanned = 0;
	private int checked = 0;

	private ISourceModule currentModule = null;

	public void processLine(String line) throws CoreException {
		console.println(line);
		if (line.startsWith(SCANNING)) {
			String fileName = line.substring(SCANNING.length() + 1).trim();
			fileName = Path.fromOSString(fileName).lastSegment();
			subTask(NLS.bind(Messages.TclChecker_scanning, fileName, String
					.valueOf(getModuleCount() - scanned)));
			scanned++;
		} else if (line.startsWith(CHECKING)) {
			String fileName = line.substring(CHECKING.length() + 1).trim();
			final IPath path = Path.fromOSString(fileName);
			currentModule = findSourceModule(path);
			subTask(NLS.bind(Messages.TclChecker_checking, path.lastSegment(),
					String.valueOf(getModuleCount() - checked)));
			checked++;
		} else if (isValidModule()) {
			final TclCheckerProblem problem = parseProblem(line);
			if (problem != null) {
				reporter.report(currentModule, problem);
			}
		}
	}

	protected boolean isValidModule() {
		return currentModule != null;
	}

	public void processErrorLine(String line) {
		console.println(line);
	}

}
