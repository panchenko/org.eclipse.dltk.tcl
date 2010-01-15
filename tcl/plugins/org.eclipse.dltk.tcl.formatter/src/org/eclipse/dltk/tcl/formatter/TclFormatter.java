/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Sergey Kanshin)
 *******************************************************************************/
package org.eclipse.dltk.tcl.formatter;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.dltk.compiler.SourceElementRequestorAdaptor;
import org.eclipse.dltk.formatter.AbstractScriptFormatter;
import org.eclipse.dltk.formatter.FormatterContext;
import org.eclipse.dltk.formatter.FormatterDocument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.formatter.internal.DumpContentException;
import org.eclipse.dltk.tcl.formatter.internal.FormatterIndentDetector;
import org.eclipse.dltk.tcl.formatter.internal.FormatterWorker;
import org.eclipse.dltk.tcl.formatter.internal.Messages;
import org.eclipse.dltk.tcl.formatter.internal.TclFormatterPlugin;
import org.eclipse.dltk.tcl.formatter.internal.TclFormatterWriter;
import org.eclipse.dltk.tcl.formatter.internal.UnexpectedFormatterException;
import org.eclipse.dltk.tcl.internal.structure.TclSourceElementParser2;
import org.eclipse.dltk.ui.formatter.FormatterException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;

@SuppressWarnings("restriction")
public class TclFormatter extends AbstractScriptFormatter {
	protected static final String[] INDENTING = {
			TclFormatterConstants.INDENT_SCRIPT,
			TclFormatterConstants.INDENT_AFTER_BACKSLASH };

	protected static final String[] BLANK_LINES = {
			TclFormatterConstants.LINES_FILE_AFTER_PACKAGE,
			TclFormatterConstants.LINES_FILE_BETWEEN_PROC };

	private final String lineDelimiter;

	@SuppressWarnings("unchecked")
	public TclFormatter(String lineDelimiter, Map preferences) {
		super(preferences);
		this.lineDelimiter = lineDelimiter;
	}

	@Override
	public int detectIndentationLevel(IDocument document, int offset) {
		if (offset == 0) {
			return 0;
		}
		final String input = document.get();
		List<TclCommand> commands = parse(input);
		final FormatterIndentDetector detector = new FormatterIndentDetector(
				offset);
		return detector.getLevel(commands);
	}

	public TextEdit format(String source, int offset, int length, int indent)
			throws FormatterException {
		final String input = source.substring(offset, offset + length);
		List<TclCommand> commands = parse(input);
		final String output = format(input, commands, indent);
		if (output != null) {
			if (!input.equals(output)) {
				if (!isValidation()
						|| equalsIgnoreBlanks(new StringReader(input),
								new StringReader(output))) {
					return new ReplaceEdit(offset, length, output);
				} else {
					TclFormatterPlugin.log(new Status(IStatus.ERROR,
							TclFormatterPlugin.PLUGIN_ID, IStatus.OK,
							Messages.TclFormatter_contentCorrupted,
							new DumpContentException(input)));
				}
			} else {
				return new MultiTextEdit(); // NOP
			}
		}
		return null;
	}

	protected List<TclCommand> parse(final String input) {
		TclSourceElementParser2 parser = new TclSourceElementParser2();
		parser.setRequestor(new SourceElementRequestorAdaptor());
		return parser.parse(input, 0);
	}

	private String format(String input, List<TclCommand> commands, int indent)
			throws FormatterException {
		FormatterDocument document = createDocument(input);
		final TclFormatterWriter writer = new TclFormatterWriter(document,
				lineDelimiter, createIndentGenerator());
		writer
				.setWrapLength(getInt(TclFormatterConstants.WRAP_COMMENTS_LENGTH));
		writer.setLinesPreserve(getInt(TclFormatterConstants.LINES_PRESERVE));
		// tclWriter.setTrimEmptyLines(false);
		// tclWriter.setTrimTrailingSpaces(false);
		try {
			final FormatterContext context = new FormatterContext(indent);
			final FormatterWorker worker = new FormatterWorker(writer,
					document, context);
			worker.format(commands);
			writer.flush(context);
			return writer.getOutput();
		} catch (UnexpectedFormatterException e) {
			throw new FormatterException(e);
		}
	}

	private FormatterDocument createDocument(String input) {
		FormatterDocument document = new FormatterDocument(input);
		for (int i = 0; i < INDENTING.length; ++i) {
			document.setBoolean(INDENTING[i], getBoolean(INDENTING[i]));
		}
		for (int i = 0; i < BLANK_LINES.length; ++i) {
			document.setInt(BLANK_LINES[i], getInt(BLANK_LINES[i]));
		}
		document.setInt(TclFormatterConstants.FORMATTER_TAB_SIZE,
				getInt(TclFormatterConstants.FORMATTER_TAB_SIZE));
		document.setBoolean(TclFormatterConstants.WRAP_COMMENTS,
				getBoolean(TclFormatterConstants.WRAP_COMMENTS));
		return document;
	}

	protected boolean isValidation() {
		return !getBoolean(TclFormatterConstants.WRAP_COMMENTS);
	}

	private boolean equalsIgnoreBlanks(Reader inputReader, Reader outputReader) {
		LineNumberReader input = new LineNumberReader(inputReader);
		LineNumberReader output = new LineNumberReader(outputReader);
		for (;;) {
			final String inputLine = readLine(input);
			final String outputLine = readLine(output);
			if (inputLine == null) {
				if (outputLine == null) {
					return true;
				} else {
					return false;
				}
			} else if (outputLine == null) {
				return false;
			} else if (!inputLine.equals(outputLine)) {
				return false;
			}
		}
	}

	private String readLine(LineNumberReader reader) {
		String line;
		do {
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// should not happen
				return null;
			}
			if (line == null) {
				return line;
			}
			line = line.trim();
		} while (line.length() == 0);
		return line;
	}

}
