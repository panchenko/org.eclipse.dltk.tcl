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

import java.util.List;

import org.eclipse.dltk.tcl.internal.parser.raw.BracesSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.CodeScanner;
import org.eclipse.dltk.tcl.internal.parser.raw.ErrorDescription;
import org.eclipse.dltk.tcl.internal.parser.raw.ICodeScanner;
import org.eclipse.dltk.tcl.internal.parser.raw.ISubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.MagicBackslashSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.NormalBackslashSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.SimpleTclParser;
import org.eclipse.dltk.tcl.internal.parser.raw.TclCommand;
import org.eclipse.dltk.tcl.internal.parser.raw.TclElement;
import org.eclipse.dltk.tcl.internal.parser.raw.TclParseException;
import org.eclipse.dltk.tcl.internal.parser.raw.TclScript;
import org.eclipse.dltk.tcl.internal.parser.raw.TclWord;

public final class TclDictionaryParser extends SimpleTclParser {

	@Override
	public ISubstitution getCVB(ICodeScanner input) throws TclParseException {
		if (NormalBackslashSubstitution.iAm(input))
			return new NormalBackslashSubstitution();
		if (MagicBackslashSubstitution.iAm(input))
			return new MagicBackslashSubstitution();
		return null;
	}

	private static class UnexpectedError extends RuntimeException {

		private static final long serialVersionUID = 1L;

	}

	@Override
	public boolean handleError(ErrorDescription error) {
		throw new UnexpectedError();
	}

	@Override
	public TclScript parse(String content) {
		try {
			return parse(new CodeScanner(content), false, null);
		} catch (UnexpectedError e) {
			// e.printStackTrace();
			return null;
		} catch (TclParseException e) {
			// e.printStackTrace();
			return null;
		}
	}

	public List<IToken> parseDictionary(String input) {
		TclScript script = parse(input);
		if (script != null) {
			return convert(0, input, script.getCommands()).getChildren();
		} else {
			return null;
		}
	}

	private ListToken convert(int offset, String content,
			List<TclCommand> commands) {
		final ListToken result = new ListToken();
		for (int i = 0; i < commands.size(); ++i) {
			final TclCommand command = commands.get(i);
			final List<TclWord> words = command.getWords();
			for (int j = 0; j < words.size(); ++j) {
				final TclWord word = words.get(j);
				final List<Object> contents = word.getContents();
				if (contents.size() == 1
						&& contents.get(0) instanceof BracesSubstitution) {
					final String substText = content.substring(offset
							+ word.getStart() + 1, offset + word.getEnd());
					result.addChild(new RawToken(this, substText));
				} else {
					boolean join = false;
					final List<IToken> children = result.getChildren();
					for (Object item : contents) {
						if (item instanceof String) {
							if (join) {
								IToken prev = children.get(children.size() - 1);
								assert (prev instanceof WordToken);
								children.set(children.size() - 1,
										new WordToken(prev.getText() + item));
							} else {
								result.addChild(new WordToken((String) item));
							}
						} else if (item instanceof TclElement) {
							final TclElement element = (TclElement) item;
							final String wordText = content.substring(offset
									+ element.getStart(), offset
									+ element.getEnd() + 1);
							if (element instanceof NormalBackslashSubstitution) {
								if (!children.isEmpty()) {
									IToken prev = children
											.get(children.size() - 1);
									if (prev instanceof WordToken) {
										children.set(children.size() - 1,
												new WordToken(prev.getText()
														+ wordText));
										join = true;
										continue;
									}
								}
							}
							join = false;
							result.addChild(new WordToken(wordText));
						}
					}
				}
			}
		}
		return result;
	}
}
