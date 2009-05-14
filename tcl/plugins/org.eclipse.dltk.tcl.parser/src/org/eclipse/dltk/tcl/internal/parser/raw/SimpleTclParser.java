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

import java.text.ParseException;

import org.eclipse.dltk.tcl.parser.ITclErrorConstants;
import org.eclipse.dltk.tcl.parser.ITclErrorReporter;

public class SimpleTclParser {
	private ITclErrorReporter reporter = null;

	public void setProblemReporter(ITclErrorReporter reporter) {
		this.reporter = reporter;
	}

	public SimpleTclParser() {
		this(0);
	}

	public SimpleTclParser(int sourceOffset) {
		this.sourceOffset = sourceOffset;
	}

	private final int sourceOffset;

	/**
	 * Report an error. Should return true parser should continue work, or
	 * false, if it should stop.
	 * 
	 * @param error
	 * @return
	 */
	public boolean handleError(ErrorDescription error) {
		if (this.reporter != null) {
			this.reporter.report(0, error.getMessage(), null, sourceOffset
					+ error.getPosition(), sourceOffset + error.getEnd() + 1,
					ITclErrorConstants.ERROR);
		}
		return true;
	}

	public static String magicSubstitute(String src) {
		String regex = "\\\\\\r*\\n\\s*"; //$NON-NLS-1$
		return src.replaceAll(regex, " "); //$NON-NLS-1$
	}

	public ISubstitution getCVB(ICodeScanner input) throws TclParseException {

		if (CommandSubstitution.iAm(input))
			return new CommandSubstitution();

		if (VariableSubstitution.iAm(input))
			return new VariableSubstitution();

		if (NormalBackslashSubstitution.iAm(input))
			return new NormalBackslashSubstitution();

		if (MagicBackslashSubstitution.iAm(input))
			return new MagicBackslashSubstitution();

		return null;
	}

	private TclCommand nextCommand(ICodeScanner input, boolean nest,
			final TclWordBuffer wordBuffer) throws TclParseException {
		TclCommand cmd = new TclCommand();
		cmd.setStart(input.getPosition());
		wordBuffer.reset();

		while (true) {
			int ch = input.read();
			boolean eof = (ch == ICodeScanner.EOF);
			if (eof && cmd.isEmpty() && wordBuffer.isEmpty()) {
				return STOP_EOF;
			}
			if (TclTextUtils.isTrueWhitespace(ch) || eof) {
				final TclWord word = wordBuffer.buildWord();
				if (word != null) {
					cmd.addWord(word);
				}
				if (eof)
					break;
				else
					continue;
			} else {
				input.unread();
				if (wordBuffer.getState() != TclWordBuffer.State.CONTENT) {
					wordBuffer.setStart(input.getPosition());
				}
			}
			if (wordBuffer.isEmpty() && BracesSubstitution.iAm(input)) {
				BracesSubstitution s = new BracesSubstitution();
				s.readMe(input, this);
				wordBuffer.add(s);
				continue;
			}
			if (wordBuffer.isEmpty() && QuotesSubstitution.iAm(input)) {
				QuotesSubstitution s = new QuotesSubstitution();
				s.readMe(input, this);
				wordBuffer.add(s);
				continue;
			}
			if (cmd.isEmpty() && wordBuffer.isEmpty()) {
				if (ch == '#') {
					input.read();
					TclTextUtils.runToLineEnd(input);
					return null;
				}
				if (ch == ']' && nest) {
					input.read();
					return STOP;
				}
			} else {
				if (ch == ']' && nest) {
					final TclWord word = wordBuffer.buildWord();
					if (word != null) {
						cmd.addWord(word);
					}
					break;
				}
			}

			ISubstitution s = this.getCVB(input);
			if (s != null) {
				s.readMe(input, this);
				if (s instanceof MagicBackslashSubstitution) {
					TclWord word = wordBuffer.buildWord();
					if (word != null) {
						// XXX setEnd() is called in addWord() too
						word
								.setEnd(((MagicBackslashSubstitution) s)
										.getStart() - 1);
						cmd.addWord(word);
					}
				} else {
					wordBuffer.add(s);
				}
				continue;
			}

			boolean cmdEnd = false;
			switch (ch) {
			case '\r':
				input.read();
				int c1 = input.read();
				if (c1 == '\n') {
					cmdEnd = true;
				} else if (c1 == ICodeScanner.EOF) {
					cmdEnd = true;
				} else {
					input.unread();
					wordBuffer.add((char) ch);
				}
				break;
			case '\n':
				input.read();
				cmdEnd = true;
				break;
			case ';':
				input.read();
				cmdEnd = true;
				break;
			default:
				input.read();
				// if (!TclTextUtils.isWhitespace(ch))
				wordBuffer.add((char) ch);
			}
			if (cmdEnd) {
				final TclWord word = wordBuffer.buildWord();
				if (word != null) {
					cmd.addWord(word);
				}
				break;
			}
		}
		int wordsSize = cmd.getWords().size();
		if (wordsSize > 0) {
			TclWord w = cmd.getWords().get(wordsSize - 1);
			cmd.setEnd(w.getEnd());
		} else
			cmd.setEnd(cmd.getStart());
		return cmd;
	}

	public interface IEOFHandler {
		void handle();
	}

	private static final TclCommand STOP = new TclCommand();

	private static final TclCommand STOP_EOF = new TclCommand();

	/**
	 * Parses input. If nest is <code>true</code> treats ] command as end.
	 * 
	 * @param input
	 * @param nest
	 * @throws ParseException
	 */
	public TclScript parse(ICodeScanner input, boolean nest, IEOFHandler handler)
			throws TclParseException {
		final TclWordBuffer wordBuffer = new TclWordBuffer();
		TclScript script = new TclScript();
		script.setStart(input.getPosition());
		while (true) {
			TclCommand cmd = nextCommand(input, nest, wordBuffer);
			if (cmd == STOP) {
				break;
			} else if (cmd == STOP_EOF) {
				if (handler != null) {
					handler.handle();
				}
				break;
			}

			if (cmd == null || cmd.getWords().size() == 0)
				continue;

			script.addCommand(cmd);
		}
		script.setEnd(input.getPosition() - 1);
		return script;
	}

	public TclScript parse(String content) throws TclParseException {
		ICodeScanner scanner = new CodeScanner(content);
		TclScript script = parse(scanner, false, null);
		return script;
	}

	public static TclScript staticParse(String content)
			throws TclParseException {
		SimpleTclParser parser = new SimpleTclParser();
		return parser.parse(content);
	}
}
