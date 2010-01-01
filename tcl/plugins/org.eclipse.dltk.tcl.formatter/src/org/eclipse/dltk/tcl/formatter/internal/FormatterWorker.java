package org.eclipse.dltk.tcl.formatter.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.compiler.util.Util;
import org.eclipse.dltk.formatter.IFormatterContext;
import org.eclipse.dltk.formatter.IFormatterDocument;
import org.eclipse.dltk.tcl.ast.AstPackage;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.formatter.TclFormatterConstants;
import org.eclipse.dltk.tcl.parser.TclParserUtils;
import org.eclipse.dltk.tcl.parser.TclVisitor;

public class FormatterWorker extends TclVisitor {

	private static final String BACKSLASH = "\\"; //$NON-NLS-1$
	private static final String PROC_DIRECTIVE = "proc"; //$NON-NLS-1$
	private static final String PACKAGE_DIRECTIVE = "package"; //$NON-NLS-1$

	private final IFormatterDocument document;
	private final IFormatterContext context;
	private final TclFormatterWriter writer;

	private int lastReaderPos = 0;
	private String prevLine = Util.EMPTY_STRING;
	private boolean lastComment = false;
	private boolean wasPackageDirective = false;

	/**
	 * @param document
	 * @param lineDelimiter
	 * @param indentGenerator
	 * @param wrapLength
	 */
	public FormatterWorker(TclFormatterWriter writer,
			IFormatterDocument document, IFormatterContext context) {
		this.writer = writer;
		this.document = document;
		this.context = context;
	}

	private static boolean isBackSlashLine(final IFormatterDocument document,
			String line) {
		return document
				.getBoolean(TclFormatterConstants.INDENT_AFTER_BACKSLASH)
				&& (line.contains(BACKSLASH + "\n") || line.contains(BACKSLASH
						+ "\r"));
	}

	private static List<String> splitComments(String input) {
		List<String> result = new ArrayList<String>();
		List<String> lines = splitLines(input);
		StringBuffer sb = new StringBuffer();
		for (String line : lines) {
			sb.append(line);
			if ("".equals(line.trim())) {
				result.add(sb.toString());
				sb.setLength(0);
			}
		}
		if (sb.length() > 0) {
			result.add(sb.toString());
		}
		return result;
	}

	private static List<String> splitLines(String text) {
		char[] input = new String(text).toCharArray();
		List<String> lines = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < input.length; i++) {
			sb.append(input[i]);
			if (input[i] == '\r') {
				if (i + 1 < input.length && input[i + 1] == '\n') {
					continue;
				} else {
					lines.add(sb.toString());
					sb.setLength(0);
				}
			}
			if (input[i] == '\n') {
				lines.add(sb.toString());
				sb.setLength(0);
			}
		}
		if (sb.length() > 0) {
			lines.add(sb.toString());
		}
		return lines;
	}

	private static boolean isIf0Comment(TclCommand command) {
		return "if".equals(command.getQualifiedName())
				&& command.getArguments().size() == 2
				&& command.getArguments().get(0) instanceof StringArgument
				&& "0".equals(((StringArgument) command.getArguments().get(0))
						.getValue())
				&& command.getArguments().get(1) instanceof Script;
	}

	private static boolean isCommandName(TclArgument arg) {
		return arg.eContainingFeature() == AstPackage.Literals.TCL_COMMAND__NAME;
	}

	/**
	 * 
	 * @param commands
	 * @param document
	 * @return formatted code
	 */
	public void format(List<TclCommand> commands) {
		TclParserUtils.traverse(commands, this);
		if (lastReaderPos < document.getLength()) {
			write(document.getLength());
		}
	}

	@Override
	public boolean visit(TclCommand command) {
		context.setIndenting(true);
		lastComment = false;
		// 'package' directive handling
		if (PACKAGE_DIRECTIVE.equals(command.getQualifiedName())) {
			if (wasPackageDirective) {
				context.setBlankLines(0);
			}
			wasPackageDirective = true;
		} else if (wasPackageDirective) {
			context.setBlankLines(document
					.getInt(TclFormatterConstants.LINES_FILE_AFTER_PACKAGE));
			wasPackageDirective = false;
		}
		// 'proc' directive handling
		if (PROC_DIRECTIVE.equals(command.getQualifiedName())) {
			List<String> comments = splitComments(document.get(lastReaderPos,
					command.getStart()));
			for (int i = 0; i < comments.size() - 1; i++) {
				write(lastReaderPos + comments.get(i).length());
			}
			context.setBlankLines(document
					.getInt(TclFormatterConstants.LINES_FILE_BETWEEN_PROC));
		} else if (isIf0Comment(command)) { // if 0 { comments }
			lastComment = true;
			write(command.getEnd());
			return false;
		}
		write(command.getName().getEnd());
		return true;
	}

	@Override
	public void endVisit(TclCommand command) {
		if (PROC_DIRECTIVE.equals(command.getQualifiedName())) {
			context.setBlankLines(document
					.getInt(TclFormatterConstants.LINES_FILE_BETWEEN_PROC));
		}
	}

	@Override
	public boolean visit(StringArgument arg) {
		if (isCommandName(arg)) {
			return false;
		}
		writeString(arg.getStart(), arg.getEnd());
		return false;
	}

	private void writeString(int start, int end) {
		write(start);
		if (!isBackSlashLine(document, prevLine)) {
			try {
				writer.ensureLineStarted(context);
			} catch (Exception e) {
				throw new UnexpectedFormatterException(e);
			}
		}
		final boolean savedIndenting = context.isIndenting();
		final int savedLinesPreserve = writer.getLinesPreserve();
		writer.setLinesPreserve(-1);
		context.setIndenting(false);
		write(end);
		context.setIndenting(savedIndenting);
		writer.setLinesPreserve(savedLinesPreserve);
		writer.ensureLineStarted(context);
	}

	@Override
	public boolean visit(TclArgumentList list) {
		context.incIndent();
		return true;
	}

	@Override
	public void endVisit(TclArgumentList list) {
		context.decIndent();
	}

	@Override
	public boolean visit(Script script) {
		if (isCommandName(script)) {
			return false;
		}
		if (script.getCommands().isEmpty()) {
			if (document.getBoolean(TclFormatterConstants.INDENT_SCRIPT)) {
				context.incIndent();
			}
			write(script.getContentEnd());
			return true;
		}
		if (script.getContentStart() > script.getStart()) {
			write(script.getContentStart());
			if (document.getBoolean(TclFormatterConstants.INDENT_SCRIPT)) {
				context.incIndent();
			}
			return true;
		} else {
			write(script.getEnd());
			return false;
		}
	}

	@Override
	public void endVisit(Script script) {
		write(script.getContentEnd());
		if (document.getBoolean(TclFormatterConstants.INDENT_SCRIPT)) {
			context.decIndent();
		}
		write(script.getEnd());
	}

	@Override
	public boolean visit(Substitution substitution) {
		if (isCommandName(substitution)) {
			return false;
		}
		write(substitution.getStart());
		return true;
	}

	@Override
	public boolean visit(ComplexString string) {
		if (isCommandName(string)) {
			return false;
		}
		writeString(string.getStart(), string.getEnd());
		return false; // don't process internals
	}

	@Override
	public boolean visit(VariableReference var) {
		if (isCommandName(var)) {
			return false;
		}
		write(var.getStart());
		write(var.getEnd());
		return false; // don't process internals
	}

	protected void write(int endOffset) {
		assert endOffset >= lastReaderPos;
		if (lastReaderPos == endOffset) {
			return;
		}
		boolean decIndent = false;
		// Indentation after backslash option
		if (isBackSlashLine(document, prevLine)) {
			context.setIndenting(!lastComment);
			context.incIndent();
			decIndent = true;
		}
		final String text = document.get(lastReaderPos, endOffset);
		final List<String> lines = splitLines(text);
		for (String line : lines) {
			final int end = lastReaderPos + line.length();
			// Wrapping comments option
			if (line.trim().startsWith("#")) {
				final boolean savedWrapping = context.isWrapping();
				context.setWrapping(document
						.getBoolean(TclFormatterConstants.WRAP_COMMENTS));
				try {
					writer.write(context, lastReaderPos, end);
				} catch (Exception e) {
					throw new UnexpectedFormatterException(e);
				}
				context.setWrapping(savedWrapping);
				lastComment = true;
			} else {
				try {
					writer.write(context, lastReaderPos, end);
				} catch (Exception e) {
					throw new UnexpectedFormatterException(e);
				}
				lastComment = false;
			}
			// Sets previous not blank line
			prevLine = "".equals(line.trim()) ? prevLine + line : line;
			lastReaderPos = end;
		}
		if (decIndent) {
			context.decIndent();
		}
	}

}
