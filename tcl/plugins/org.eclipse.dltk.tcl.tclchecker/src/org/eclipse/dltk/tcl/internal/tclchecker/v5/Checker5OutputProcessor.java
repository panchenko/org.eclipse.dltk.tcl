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
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.internal.tclchecker.AbstractOutputProcessor;
import org.eclipse.dltk.tcl.internal.tclchecker.Coord;
import org.eclipse.dltk.tcl.internal.tclchecker.CoordRange;
import org.eclipse.dltk.tcl.internal.tclchecker.ILineTrackerFactory;
import org.eclipse.dltk.tcl.internal.tclchecker.ITclCheckerReporter;
import org.eclipse.dltk.tcl.internal.tclchecker.Messages;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerMarker;
import org.eclipse.dltk.tcl.internal.tclchecker.TclCheckerProblem;
import org.eclipse.dltk.validators.core.IValidatorOutput;
import org.eclipse.osgi.util.NLS;

public class Checker5OutputProcessor extends AbstractOutputProcessor {

	private final IValidatorOutput console;
	private final ITclCheckerReporter reporter;
	private final ILineTrackerFactory lineTrackerFactory;

	public Checker5OutputProcessor(IProgressMonitor monitor,
			IValidatorOutput console, ITclCheckerReporter reporter,
			ILineTrackerFactory lineTrackerFactory) {
		super(monitor);
		this.console = console;
		this.reporter = reporter;
		this.lineTrackerFactory = lineTrackerFactory;
	}

	public void processErrorLine(String line) {
		console.println(line);
	}

	private final StringBuilder buffer = new StringBuilder();

	private int scanned = 0;
	private int checked = 0;

	public void processLine(String line) throws CoreException {
		console.println(line);
		buffer.append(line);
		buffer.append("\n"); //$NON-NLS-1$
		final List<IToken> tokens = new TclDictionaryParser()
				.parseDictionary(buffer.toString());
		if (tokens != null) {
			buffer.setLength(0);
			if (tokens.size() >= 2 && !tokens.get(0).hasChildren()) {
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
				} else if (tokens.size() == 3
						&& CMD_PROGRESS.equals(tokens.get(0).getText())) {
					final String progressSubCmd = tokens.get(1).getText();
					if (PROGRESS_SCANNING.equals(progressSubCmd)) {
						subTask(NLS.bind(Messages.TclChecker_scanning, tokens
								.get(2).getText(), String
								.valueOf(getModuleCount() - scanned)));
						scanned++;
					} else if (PROGRESS_CHECKING.equals(progressSubCmd)) {
						subTask(NLS.bind(Messages.TclChecker_checking, tokens
								.get(2).getText(), String
								.valueOf(getModuleCount() - checked)));
						++checked;
					}
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
		final String messageId = attributes.get(ATTR_MESSAGE_ID).getText();
		final TclCheckerProblem problem = new TclCheckerProblem(file,
				lineNumber, messageId, attributes.get(ATTR_MESSAGE_TEXT)
						.getText());
		final Coord commandStart = parseCoord(attributes
				.get(ATTR_COMMAND_START));
		final Coord commandEnd = parseCoord(attributes.get(ATTR_COMMAND_END));
		if (commandStart != null && commandEnd != null) {
			problem.setRange(new CoordRange(commandStart, commandEnd));
			if (attributes.containsKey(ATTR_SUGGESTED_CORRECTIONS)) {
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
				final ISourceLineTracker lineTracker = lineTrackerFactory
						.getLineTracker(module);
				problem.addAttribute(TclCheckerMarker.SUGGESTED_CORRECTIONS,
						CorrectionEngine.encodeArguments(suggestions));
				problem.addAttribute(TclCheckerMarker.COMMAND_START,
						lineTrackerFactory.calculateOffset(lineTracker,
								commandStart));
				problem.addAttribute(TclCheckerMarker.COMMAND_END,
						lineTrackerFactory.calculateOffset(lineTracker,
								commandEnd));
				problem.addAttribute(TclCheckerMarker.MESSAGE_ID, messageId);
				problem.addAttribute(TclCheckerMarker.TIMESTAMP, String
						.valueOf(module.getResource().getModificationStamp()));
				problem.addAttribute(TclCheckerMarker.AUTO_CORRECTABLE,
						attributes.get(ATTR_AUTO_CORRECTABLE).getText());
			}
		}
		final Coord errorStart = parseCoord(attributes.get(ATTR_ERROR_START));
		final Coord errorEnd = parseCoord(attributes.get(ATTR_ERROR_END));
		if (errorStart != null && errorEnd != null) {
			problem.setErrorRange(new CoordRange(errorStart, errorEnd));
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

	private Coord parseCoord(final IToken token) {
		if (token != null) {
			final String s = token.getText();
			final int pos = s.indexOf(' ');
			if (pos > 0) {
				try {
					return new Coord(Integer.parseInt(s.substring(0, pos)),
							Integer.parseInt(s.substring(pos + 1)));
				} catch (NumberFormatException e) {
					// fall through
				}
			}
		}
		return null;
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

	private static final String PROGRESS_SCANNING = "scanning"; //$NON-NLS-1$
	private static final String PROGRESS_CHECKING = "checking"; //$NON-NLS-1$

	private static final String ATTR_FILE = "file"; //$NON-NLS-1$
	private static final String ATTR_LINE = "line"; //$NON-NLS-1$
	private static final String ATTR_MESSAGE_ID = "messageID"; //$NON-NLS-1$
	private static final String ATTR_MESSAGE_TEXT = "messageText"; //$NON-NLS-1$

	private static final String ATTR_COMMAND_START = "commandStart,portable"; //$NON-NLS-1$
	private static final String ATTR_COMMAND_END = "commandEnd,portable"; //$NON-NLS-1$
	private static final String ATTR_ERROR_START = "errorStart,portable"; //$NON-NLS-1$
	private static final String ATTR_ERROR_END = "errorEnd,portable"; //$NON-NLS-1$

	private static final String ATTR_SUGGESTED_CORRECTIONS = "suggestedCorrections"; //$NON-NLS-1$
	private static final String ATTR_AUTO_CORRECTABLE = "autoCorrectable"; //$NON-NLS-1$

}
