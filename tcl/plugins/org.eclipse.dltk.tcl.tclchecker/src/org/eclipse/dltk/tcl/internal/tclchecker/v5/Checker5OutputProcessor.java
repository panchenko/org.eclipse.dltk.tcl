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
package org.eclipse.dltk.tcl.internal.tclchecker.v5;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.core.CorrectionEngine;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.corext.SourceRange;
import org.eclipse.dltk.tcl.internal.tclchecker.AbstractOutputProcessor;
import org.eclipse.dltk.tcl.internal.tclchecker.ITclCheckerReporter;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblem;
import org.eclipse.dltk.validators.core.IValidatorOutput;

public class Checker5OutputProcessor extends AbstractOutputProcessor {

	private final IValidatorOutput console;
	private final ITclCheckerReporter reporter;

	public Checker5OutputProcessor(IProgressMonitor monitor,
			IValidatorOutput console, ITclCheckerReporter reporter) {
		super(monitor);
		this.console = console;
		this.reporter = reporter;
	}

	public void processErrorLine(String line) {
		console.println(line);
	}

	private final StringBuilder buffer = new StringBuilder();

	public void processLine(String line) throws CoreException {
		console.println(line);
		buffer.append(line);
		buffer.append("\n"); //$NON-NLS-1$
		final List<IToken> tokens = new TclDictionaryParser()
				.parseDictionary(buffer.toString());
		if (tokens != null) {
			buffer.setLength(0);
			System.out.println(tokens);
			if (tokens.size() == 2 && !tokens.get(0).hasChildren()) {
				if (CMD_MESSAGE.equals(tokens.get(0).getText())
						&& tokens.get(1).hasChildren()) {
					final Map<String, IToken> attributes = parseAttributes(tokens
							.get(1).getChildren());
					if (attributes.containsKey(ATTR_FILE)
							&& attributes.containsKey(ATTR_LINE)
							&& attributes.containsKey(ATTR_MESSAGE_ID)
							&& attributes.containsKey(ATTR_MESSAGE_TEXT)) {
						processMessage(attributes);
					}
				} else if (CMD_PROGRESS.equals(tokens.get(0).getText())) {

				}
			}

		}
	}

	private void processMessage(final Map<String, IToken> attributes)
			throws IllegalArgumentException, CoreException {
		final String file = attributes.get(ATTR_FILE).getText();
		final int lineNumber = parseInt(attributes.get(ATTR_LINE));
		if (lineNumber < 0) {
			return;
		}
		final ISourceModule module = findSourceModule(new Path(file));
		if (module == null) {
			return;
		}
		final TclCheckerProblem problem = new TclCheckerProblem(file,
				lineNumber, attributes.get(ATTR_MESSAGE_ID).getText(),
				attributes.get(ATTR_MESSAGE_TEXT).getText());
		final int commandStart = parseInt(attributes.get(ATTR_COMMAND_START));
		final int commandLength = parseInt(attributes.get(ATTR_COMMAND_LENGTH));
		if (commandStart >= 0 && commandLength >= 0) {
			problem.setRange(new SourceRange(commandStart, commandLength));
		}
		final int errorStart = parseInt(attributes.get(ATTR_ERROR_START));
		final int errorLength = parseInt(attributes.get(ATTR_ERROR_LENGTH));
		if (errorStart >= 0 && errorLength >= 0) {
			problem.setErrorRange(new SourceRange(errorStart, errorLength));
		}
		if (attributes.containsKey(ATTR_SUGGESTED_CORRECTIONS)
				&& problem.getRange() != null) {
			final List<IToken> correctionList;
			final IToken corrections = attributes
					.get(ATTR_SUGGESTED_CORRECTIONS);
			if (corrections.hasChildren()) {
				correctionList = corrections.getChildren();
			} else {
				correctionList = Collections.singletonList(corrections);
			}
			final String[] suggestions = new String[correctionList.size()];
			for (int i = 0; i < correctionList.size(); ++i) {
				suggestions[i] = correctionList.get(i).getText();
			}
			problem.addAttribute(TclCheckerMarker.SUGGESTED_CORRECTIONS,
					CorrectionEngine.encodeArguments(suggestions));
			problem.addAttribute(TclCheckerMarker.COMMAND_START, commandStart);
			problem
					.addAttribute(TclCheckerMarker.COMMAND_LENGTH,
							commandLength);
		}
		reporter.report(module, problem);
	}

	private int parseInt(final IToken token) {
		try {
			return Integer.parseInt(token.getText());
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	/**
	 * @param children
	 * @return
	 */
	private Map<String, IToken> parseAttributes(List<IToken> children) {
		final Map<String, IToken> result = new HashMap<String, IToken>();
		for (int i = 0; i + 1 < children.size(); i += 2) {
			final IToken key = children.get(i);
			final IToken value = children.get(i + 1);
			if (!key.hasChildren()) {
				result.put(key.getText(), value);
			}
		}
		return result;
	}

	private static final String CMD_MESSAGE = "message"; //$NON-NLS-1$
	private static final String CMD_PROGRESS = "progress"; //$NON-NLS-1$

	private static final String ATTR_FILE = "file"; //$NON-NLS-1$
	private static final String ATTR_LINE = "line"; //$NON-NLS-1$
	private static final String ATTR_MESSAGE_ID = "messageID"; //$NON-NLS-1$
	private static final String ATTR_MESSAGE_TEXT = "messageText"; //$NON-NLS-1$

	private static final String ATTR_COMMAND_START = "commandStart"; //$NON-NLS-1$
	private static final String ATTR_COMMAND_LENGTH = "commandLength"; //$NON-NLS-1$
	private static final String ATTR_ERROR_START = "errorStart"; //$NON-NLS-1$
	private static final String ATTR_ERROR_LENGTH = "errorLength"; //$NON-NLS-1$

	private static final String ATTR_SUGGESTED_CORRECTIONS = "suggestedCorrections"; //$NON-NLS-1$

}
