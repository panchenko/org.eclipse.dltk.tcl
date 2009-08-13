package org.eclipse.dltk.tcl.parser.printer;

import java.util.List;

import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.emf.common.util.EList;

public class SimpleCodePrinter {

	private static final String SPACE = " "; //$NON-NLS-1$
	private static final String QUOTE = "\""; //$NON-NLS-1$
	private static final String LEFT_BRACKET = "["; //$NON-NLS-1$
	private static final String RIGHT_BRACKET = "]"; //$NON-NLS-1$
	private static final String LEFT_BRACE = "{"; //$NON-NLS-1$
	private static final String RIGHT_BRACE = "}"; //$NON-NLS-1$

	public static String getArgumentString(TclArgument arg, int pos) {
		return getArgumentString(arg, pos, true);
	}

	/**
	 * @since 1.1
	 */
	public static String getArgumentString(TclArgument arg, int pos,
			boolean addSpaces) {
		StringBuilder buff = new StringBuilder();
		int start = arg.getStart();
		if (addSpaces && start > pos) {
			for (int i = 0; i < (start - pos); ++i) {
				buff.append(SPACE);
			}
		}
		if (arg instanceof StringArgument) {
			// Simple absolute or relative source'ing.
			StringArgument argument = (StringArgument) arg;
			String value = argument.getValue();
			// value = nameFromBlock(value, '\"', '\"');
			// value = nameFromBlock(value, '{', '}');
			buff.append(value);
			return buff.toString();
		} else if (arg instanceof ComplexString) {
			ComplexString carg = (ComplexString) arg;
			// if (carg.getValue() != null) {
			// return carg.getValue();
			// }
			EList<TclArgument> eList = carg.getArguments();
			if (carg.getKind() == 1) {
				buff.append(LEFT_BRACE);
			} else if (carg.getKind() == 2) {
				buff.append(QUOTE);
			}
			for (TclArgument tclArgument : eList) {
				buff.append(getArgumentString(tclArgument, pos + buff.length(),
						addSpaces));
			}

			int end = arg.getEnd() - 1;
			if (carg.getKind() == 0) {
				end++;
			}
			int npos = pos + buff.length();
			if (addSpaces && end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(SPACE);
				}
			}
			if (carg.getKind() == 1) {
				buff.append(RIGHT_BRACE);
			} else if (carg.getKind() == 2) {
				buff.append(QUOTE);
			}
			return buff.toString();
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			buff.append(LEFT_BRACE);
			boolean added = false;
			for (TclCommand command : st.getCommands()) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    "); //$NON-NLS-1$
				}
				buff.append(getCommandString(command, pos + buff.length(),
						addSpaces));
			}
			int end = arg.getEnd();
			int npos = pos + buff.length();
			if (addSpaces && end - 1 > npos) {
				for (int i = 0; i < ((end - 1) - npos); ++i) {
					buff.append(SPACE);
				}
			}
			buff.append(RIGHT_BRACE);
			return buff.toString();

		} else if (arg instanceof VariableReference) {
			VariableReference variableReference = (VariableReference) arg;
			buff.append("$").append(variableReference.getName());
			if (variableReference.getIndex() != null) {
				buff.append("(").append(
						getArgumentString(variableReference.getIndex(), pos
								+ buff.length(), addSpaces)).append(")");
			}
			return buff.toString();
		} else if (arg instanceof Substitution) {
			Substitution st = (Substitution) arg;
			buff.append(LEFT_BRACKET);
			boolean added = false;
			for (TclCommand command : st.getCommands()) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    "); //$NON-NLS-1$
				}
				buff.append(getCommandString(command, pos + buff.length()));
			}
			int end = arg.getEnd() - 1;
			int npos = pos + buff.length();
			if (addSpaces && end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(SPACE);
				}
			}
			buff.append(RIGHT_BRACKET);
			return buff.toString();
		} else if (arg instanceof TclArgumentList) {
			// We need to correct with appropriate number of spaces
			TclArgumentList st = (TclArgumentList) arg;
			EList<TclArgument> eList = st.getArguments();
			if (st.getKind() == 1) {
				buff.append(LEFT_BRACE);
			} else if (st.getKind() == 2) {
				buff.append(QUOTE);
			}
			// boolean first = true;
			for (TclArgument tclArgument : eList) {
				buff.append(getArgumentString(tclArgument, pos + buff.length(),
						addSpaces));
			}
			int end = arg.getEnd() - 1;
			if (st.getKind() == 0) {
				end++;
			}
			int npos = pos + buff.length();
			if (addSpaces && end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(SPACE);
				}
			}

			if (st.getKind() == 1) {
				buff.append(RIGHT_BRACE);
			} else if (st.getKind() == 2) {
				buff.append(QUOTE);
			}
			return buff.toString();
		}
		return "";
	}

	public static String getCommandString(TclCommand command, int pos) {
		return getCommandString(command, pos, true);
	}

	/**
	 * @since 1.1
	 */
	public static String getCommandString(TclCommand command, int pos,
			boolean addSpaces) {
		StringBuilder buff = new StringBuilder();
		int start = command.getStart();
		if (addSpaces && start > pos) {
			for (int i = 0; i < (start - pos); ++i) {
				buff.append(SPACE);
			}
		}
		buff.append(getArgumentString(command.getName(), start, addSpaces));
		for (TclArgument tclArgument : command.getArguments()) {
			buff.append(getArgumentString(tclArgument, pos + buff.length(),
					addSpaces));
		}
		return buff.toString();
	}

	public static String getCommandsString(List<TclCommand> commands) {
		return getCommandsString(commands, true);
	}

	/**
	 * @since 1.1
	 */
	public static String getCommandsString(List<TclCommand> commands,
			boolean addSpaces) {
		StringBuilder buff = new StringBuilder();
		boolean first = false;
		int pos = 0;
		for (TclCommand command : commands) {
			if (!first) {
				first = true;
			} else {
				buff.append("\n");
			}
			String text = getCommandString(command, pos, addSpaces);
			buff.append(text);
			pos += text.length();
		}
		return buff.toString();
	}
}
