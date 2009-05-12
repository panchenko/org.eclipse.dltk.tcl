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
	public static String getArgumentString(TclArgument arg) {
		if (arg instanceof StringArgument) {
			// Simple absolute or relative source'ing.
			StringArgument argument = (StringArgument) arg;
			String value = argument.getValue();
			value = nameFromBlock(value, '\"', '\"');
			value = nameFromBlock(value, '{', '}');
			return value;
		} else if (arg instanceof ComplexString) {
			ComplexString carg = (ComplexString) arg;
			if (carg.getValue() != null) {
				return carg.getValue();
			}
			EList<TclArgument> eList = carg.getArguments();
			StringBuffer buff = new StringBuffer();
			for (TclArgument tclArgument : eList) {
				buff.append(getArgumentString(tclArgument));
			}
			return buff.toString();
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			EList<TclCommand> eList = st.getCommands();
			StringBuffer buff = new StringBuffer();
			buff.append("{");
			boolean added = false;
			for (TclCommand tclArgument : eList) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    ");
				}
				buff.append(getCommandString(tclArgument));
			}
			buff.append("}");
			return buff.toString();

		} else if (arg instanceof VariableReference) {
			VariableReference variableReference = (VariableReference) arg;
			String main = "$" + variableReference.getName();
			if (variableReference.getIndex() != null) {
				return main + "("
						+ getArgumentString(variableReference.getIndex()) + ")";
			}
			return main;
		} else if (arg instanceof Substitution) {
			Substitution st = (Substitution) arg;
			EList<TclCommand> eList = st.getCommands();
			StringBuffer buff = new StringBuffer();
			buff.append("[");
			boolean added = false;
			for (TclCommand tclArgument : eList) {
				if (!added) {
					added = true;
				} else {
					buff.append("\n    ");
				}
				buff.append(getCommandString(tclArgument));
			}
			buff.append("]");
			return buff.toString();
		} else if (arg instanceof TclArgumentList) {
			TclArgumentList st = (TclArgumentList) arg;
			return getArgumentString(st.getOriginalArgument());
		}
		return "";
	}

	public static String getCommandString(TclCommand command) {
		EList<TclArgument> eList = command.getArguments();
		StringBuffer buff = new StringBuffer();
		buff.append(getArgumentString(command.getName()));
		for (TclArgument tclArgument : eList) {
			buff.append(" ").append(getArgumentString(tclArgument));
		}
		return buff.toString();
	}

	public static String getCommandsString(List<TclCommand> commands) {
		StringBuffer buff = new StringBuffer();
		boolean first = false;
		for (TclCommand tclArgument : commands) {
			if (!first) {
				first = true;
			} else {
				buff.append("\n    ");
			}
			buff.append(getCommandString(tclArgument));
		}
		return buff.toString();
	}

	private static String nameFromBlock(String name, char c1, char c2) {
		if (name.charAt(0) == c1 && name.charAt(name.length() - 1) == c2) {
			return name.substring(1, name.length() - 1);
		}
		return name;
	}
}
