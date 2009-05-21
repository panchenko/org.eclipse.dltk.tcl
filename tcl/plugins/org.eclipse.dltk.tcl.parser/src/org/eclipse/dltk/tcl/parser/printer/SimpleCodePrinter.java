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
	public static String getArgumentString(TclArgument arg, int pos) {
		StringBuffer buff = new StringBuffer();
		int start = arg.getStart();
		if (start > pos) {
			for (int i = 0; i < (start - pos); ++i) {
				buff.append(" ");
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
				buff.append("{");
			} else if (carg.getKind() == 2) {
				buff.append("\"");
			}
			for (TclArgument tclArgument : eList) {
				buff
						.append(getArgumentString(tclArgument, pos
								+ buff.length()));
			}

			int end = arg.getEnd() - 1;
			if (carg.getKind() == 0) {
				end++;
			}
			int npos = pos + buff.length();
			if (end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(" ");
				}
			}
			if (carg.getKind() == 1) {
				buff.append("}");
			} else if (carg.getKind() == 2) {
				buff.append("\"");
			}
			return buff.toString();
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			EList<TclCommand> eList = st.getCommands();
			buff.append("{");
			boolean added = false;
			for (TclCommand tclArgument : eList) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    ");
				}
				buff.append(getCommandString(tclArgument, pos + buff.length()));
			}
			int end = arg.getEnd();
			int npos = pos + buff.length();
			if (end - 1 > npos) {
				for (int i = 0; i < ((end - 1) - npos); ++i) {
					buff.append(" ");
				}
			}
			buff.append("}");
			return buff.toString();

		} else if (arg instanceof VariableReference) {
			VariableReference variableReference = (VariableReference) arg;
			buff.append("$").append(variableReference.getName());
			if (variableReference.getIndex() != null) {
				buff.append("(").append(
						getArgumentString(variableReference.getIndex(), pos
								+ buff.length())).append(")");
			}
			return buff.toString();
		} else if (arg instanceof Substitution) {
			Substitution st = (Substitution) arg;
			EList<TclCommand> eList = st.getCommands();
			buff.append("[");
			boolean added = false;
			for (TclCommand tclArgument : eList) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    ");
				}
				buff.append(getCommandString(tclArgument, pos + buff.length()));
			}
			int end = arg.getEnd() - 1;
			int npos = pos + buff.length();
			if (end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(" ");
				}
			}
			buff.append("]");
			return buff.toString();
		} else if (arg instanceof TclArgumentList) {
			// We need to correct with appropriate number of spaces
			TclArgumentList st = (TclArgumentList) arg;
			EList<TclArgument> eList = st.getArguments();
			if (st.getKind() == 1) {
				buff.append("{");
			} else if (st.getKind() == 2) {
				buff.append("\"");
			}
			// boolean first = true;
			for (TclArgument tclArgument : eList) {
				buff
						.append(getArgumentString(tclArgument, pos
								+ buff.length()));
			}
			int end = arg.getEnd() - 1;
			if (st.getKind() == 0) {
				end++;
			}
			int npos = pos + buff.length();
			if (end > npos) {
				for (int i = 0; i < ((end) - npos); ++i) {
					buff.append(" ");
				}
			}

			if (st.getKind() == 1) {
				buff.append("}");
			} else if (st.getKind() == 2) {
				buff.append("\"");
			}
			return buff.toString();
		}
		return "";
	}

	public static String getCommandString(TclCommand command, int pos) {
		StringBuffer buff = new StringBuffer();
		int start = command.getStart();
		if (start > pos) {
			for (int i = 0; i < (start - pos); ++i) {
				buff.append(" ");
			}
		}
		EList<TclArgument> eList = command.getArguments();
		buff.append(getArgumentString(command.getName(), start));
		for (TclArgument tclArgument : eList) {
			buff.append(getArgumentString(tclArgument, pos + buff.length()));
		}
		return buff.toString();
	}

	public static String getCommandsString(List<TclCommand> commands) {
		StringBuffer buff = new StringBuffer();
		boolean first = false;
		int pos = 0;
		for (TclCommand tclArgument : commands) {
			if (!first) {
				first = true;
			} else {
				buff.append("\n");
			}
			String text = getCommandString(tclArgument, pos);
			buff.append(text);
			pos += text.length();
		}
		return buff.toString();
	}
}
