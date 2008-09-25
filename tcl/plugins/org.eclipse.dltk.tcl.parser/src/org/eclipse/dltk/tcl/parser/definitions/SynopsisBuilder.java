package org.eclipse.dltk.tcl.parser.definitions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.tcl.ast.ArgumentMatch;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.Messages;
import org.eclipse.emf.common.util.EList;

public class SynopsisBuilder {
	// private TclCommand tclCommand;
	// private Synopsis root;

	private class SynopsisWord {
		private String word;
		private boolean isBold = false;
		private boolean isItalic = false;

		public SynopsisWord(String word, boolean asHtml) {
			this.word = word;
		}

		public SynopsisWord(String word, boolean isBold, boolean isItalic) {
			this.word = word;
			this.isBold = isBold;
			this.isItalic = isItalic;
		}

		@Override
		public String toString() {
			if (asHtml) {
				StringBuilder result = new StringBuilder();
				if (isBold)
					result.append("<b>");
				if (isItalic)
					result.append("<i>");
				result.append(word);
				if (isItalic)
					result.append("</i>");
				if (isBold)
					result.append("</b>");
				return result.toString();
			}
			return word;
		}
	}

	private class SynopsisConstant extends SynopsisWord {
		public SynopsisConstant(String word) {
			super(word, true, false);
		}
	}

	private class SynopsisArgument extends SynopsisWord {
		public SynopsisArgument(String word) {
			super(word, false, true);
		}
	}

	private static class Synopsis {
		protected Argument selector;
		private List<Synopsis> children = new ArrayList<Synopsis>();
		private String string = null;

		public Synopsis() {
		}

		public Synopsis(String string) {
			this.string = string;
		}

		public void insert(String prefix) {
			if (prefix == null || "".equals(prefix))
				return;
			if (string == null)
				string = prefix;
			else
				string = prefix + " " + string;
		}

		private boolean matchArgument(Argument argument, Argument selector) {
			if (selector == null)
				return false;
			if (selector instanceof Switch) {
				for (Group group : ((Switch) selector).getGroups()) {
					if (matchArgument(argument, group))
						return true;
				}
			} else if (selector instanceof Group) {
				Group group = ((Group) selector);
				String cval = group.getConstant();
				if (cval != null && !cval.equals("")) {
					Constant c = DefinitionsFactory.eINSTANCE.createConstant();
					c.setName(cval);
					return DefinitionUtils
							.equalsArgumentIgnoreName(c, argument);
				} else if (group.getArguments().size() != 0) {
					return DefinitionUtils.equalsArgumentIgnoreName(group
							.getArguments().get(0), argument);
				}
			} else if (selector instanceof ComplexArgument) {
				ComplexArgument complex = ((ComplexArgument) selector);
				if (complex.getArguments().size() != 0)
					return DefinitionUtils.equalsArgumentIgnoreName(complex
							.getArguments().get(0), argument);
			} else {
				return DefinitionUtils.equalsArgumentIgnoreName(selector,
						argument);
			}
			return false;
		}

		private List<Synopsis> matchPrefix(TclArgument tclArgument) {
			List<Synopsis> matched = new ArrayList<Synopsis>();
			if (!(tclArgument instanceof StringArgument))
				return matched;
			String value = ((StringArgument) tclArgument).getValue();
			for (Synopsis child : children) {
				if (child.selector instanceof Constant) {
					String cvalue = ((Constant) child.selector).getName();
					if (cvalue == null)
						continue;
					matched.add(child);
				}
			}
			if (matched.size() == 0)
				return matched;
			List<Synopsis> notMatched = new ArrayList<Synopsis>();
			for (int i = 0; i < value.length(); i++) {
				if (matched.size() == 1) {
					String c = ((Constant) matched.get(0).selector).getName();
					if (value.equals(c))
						return matched;
				}
				for (Synopsis child : matched) {
					String c = ((Constant) child.selector).getName();
					if (c.length() <= i || value.charAt(i) != c.charAt(i)) {
						notMatched.add(child);
					}
				}
				if (notMatched.size() != 0) {
					matched.removeAll(notMatched);
					notMatched.clear();
				}
			}
			return matched;
		}

		public boolean match(List<ArgumentMatch> matches) {
			for (ArgumentMatch match : matches) {
				if (matchArgument(match.getDefinition(), selector)) {
					return true;
				}
			}
			return false;
		}

		public String getShortHint(TclCommand tclCommand, Synopsis root) {
			if (children.size() == 0) {
				return (string == null) ? "" : string;
			}
			List<Synopsis> synopsises = null;
			StringBuilder result = new StringBuilder();
			if (tclCommand != null) {
				List<ArgumentMatch> matches = tclCommand.getMatches();
				if (matches.size() == 0 && this == root
						&& tclCommand.getArguments().size() != 0) {
					synopsises = matchPrefix(tclCommand.getArguments().get(0));
					if (synopsises.size() == 1) {
						String subResult = synopsises.get(0).getShortHint(
								tclCommand, root);
						if (string != null)
							result.append(string);
						if (!"".equals(subResult))
							result.append(" ").append(subResult);
						return result.toString();
					}
					if (synopsises.size() == 0)
						synopsises = children;
				} else {
					synopsises = children;
					for (Synopsis synopsis : synopsises) {
						if (synopsis.match(matches)) {
							String subResult = synopsis.getShortHint(
									tclCommand, root);
							if (string != null)
								result.append(string);
							if (!"".equals(subResult))
								result.append(" ").append(subResult);
							return result.toString();
						}
					}
				}
			} else
				synopsises = children;
			result.append(string);
			boolean first = true;
			for (Synopsis synopsis : synopsises) {
				if (first) {
					result.append(" [");
					first = false;
				} else
					result.append("|");
				if (synopsis.string == null) {
					result.append("...");
					continue;
				}
				String[] words = synopsis.string.split(" ");
				result.append(words[0]);
				if (synopsis.children.size() != 0 || words.length > 1)
					result.append(" ...");
			}
			result.append("]");
			return result.toString();
		}

		public List<String> getSynopsis() {
			List<String> results = new ArrayList<String>();
			if (children.size() == 0) {
				results.add((string == null) ? "" : string);
			}
			for (Synopsis child : children) {
				List<String> subResults = child.getSynopsis();
				if (subResults.size() == 0) {
					results.add("");
				}
				for (String subResult : subResults) {
					StringBuilder result = new StringBuilder();
					if (string != null)
						result.append(string);
					if (!"".equals(subResult))
						result.append(" ").append(subResult);
					results.add(result.toString());
				}
			}
			return results;
		}
	}

	// public SynopsisBuilder(TclCommand tclCommand) {
	// this(tclCommand, false);
	// }

	// public SynopsisBuilder(TclCommand tclCommand, boolean asHtml) {
	// this.tclCommand = tclCommand;
	// Command command = tclCommand.getDefinition();
	// if (command == null || command.getName() == null
	// || command.getName().equals("")) {
	// throw new IllegalArgumentException();
	// }
	// processCommand(command);
	// }

	private boolean asHtml;

	public SynopsisBuilder() {
		this(false);
	}

	public SynopsisBuilder(boolean asHtml) {
		this.asHtml = asHtml;
	}

	// public SynopsisBuilder(Command command) {
	// this(command, false);
	// }
	//
	// public SynopsisBuilder(Command command, boolean asHtml) {
	// if (command == null || command.getName() == null
	// || command.getName().equals("")) {
	// throw new IllegalArgumentException();
	// }
	// this.asHtml = asHtml;
	// processCommand(command);
	// }

	// @Override
	// public String toString() {
	// if (tclCommand == null)
	// return join(root.getSynopsis(), asHtml ? "<br>" : "\n");
	// return root.getShortSynopsis(tclCommand);
	// }

	public String getSynopsis(Command command) {
		Synopsis root = processCommand(command);
		return join(root.getSynopsis(), asHtml ? "<br>" : "\n");
	}

	public String getShortHint(TclCommand tclCommand) {
		Synopsis root = processCommand(tclCommand.getDefinition());
		return root.getShortHint(tclCommand, root);
	}

	private String join(List<String> strings, String delim) {
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String string : strings) {
			if (first)
				first = false;
			else
				result.append(delim);
			result.append(string);
		}
		return result.toString();
	}

	private Synopsis processCommand(Command command) {
		Synopsis root = processArgumentList(command.getArguments(), 0);
		String commandName = new SynopsisConstant(command.getName()).toString();
		root.insert(commandName);
		return root;
	}

	private Synopsis processArgumentList(List<Argument> arguments, int pos) {
		if (pos >= arguments.size()) {
			Synopsis leaf = new Synopsis();
			if (arguments.size() != 0)
				leaf.selector = arguments.get(0);
			return leaf;
		}
		StringBuilder prefix = new StringBuilder();
		Argument argument = arguments.get(pos);

		if (argument instanceof Switch) {
			Synopsis synopsis = processSubCommands(arguments, pos);
			if (synopsis != null) {
				if (arguments.size() != 0)
					synopsis.selector = arguments.get(0);
				return synopsis;
			}
		}
		prefix.append(definitionToString(argument, argument.getLowerBound(),
				argument.getUpperBound()));
		Synopsis synopsis = processArgumentList(arguments, pos + 1);
		if (prefix != null)
			synopsis.insert(prefix.toString());
		return synopsis;
	}

	private Synopsis processSubCommands(List<Argument> arguments, int pos) {
		Switch sw = (Switch) arguments.get(pos);
		if (DefinitionUtils.isComplexScript(sw)
				|| DefinitionUtils.isComplexOptions(sw) || isOptions(sw)
				|| isMode(sw))
			return null;
		Synopsis synopsis = new Synopsis();
		if (sw.getLowerBound() == 0) {
			synopsis.children.add(new Synopsis());
		}

		for (Group group : sw.getGroups()) {
			Synopsis child;
			String cval = group.getConstant();
			if (cval != null && !"".equals(cval)) {
				Constant c = DefinitionsFactory.eINSTANCE.createConstant();
				c.setName(cval);
				c.setName(cval);
				List<Argument> list = new ArrayList<Argument>();
				list.add(c);
				list.addAll(group.getArguments());
				child = processArgumentList(list, 0);
			} else {
				child = processArgumentList(group.getArguments(), 0);
			}
			synopsis.children.add(child);
		}
		return synopsis;
	}

	private String addBounds(String inner, int lower, int upper) {
		StringBuilder result = new StringBuilder();
		if (lower == 0) {
			if (upper == -1) {
				result.append("?").append(inner).append(" ...?");
			} else {
				if (upper <= 2) {
					boolean first = true;
					for (int i = 0; i < upper; i++) {
						if (first) {
							first = false;
						} else {
							result.append(" ");
						}
						result.append("?").append(inner).append("?");
					}
				} else {
					result.append(inner).append("(").append(lower).append("-")
							.append(upper);
				}
			}
		} else {
			if (upper <= 2) {
				boolean first = true;
				for (int i = 0; i < lower; i++) {
					if (first) {
						first = false;
					} else {
						result.append(" ");
					}
					result.append(inner);
				}
				if (upper == -1) {
					result.append(" ?").append(inner).append(" ...?");
				} else {
					for (int i = 0; i < upper - lower; i++) {
						result.append(" ");
						result.append("?").append(inner).append("?");
					}
				}
			} else {
				result.append(inner).append("(").append(lower).append("-")
						.append(upper).append(")");
			}
		}
		return result.toString();
	}

	public String definitionToList(List<Argument> definitions) {
		if (definitions.size() == 1)
			return definitionToString(definitions.get(0));

		List<String> list = new ArrayList<String>();
		for (Argument definition : definitions) {
			String value = definitionToString(definition);
			if (!list.contains(value))
				list.add(value);
		}

		StringBuilder result = new StringBuilder();
		boolean first = true;
		for (String s : list) {
			if (first) {
				first = false;
			} else {
				result.append(",");
			}
			result.append(s);
		}
		return result.toString();
	}

	public String definitionToString(Argument argument, int lower, int upper) {
		StringBuilder out = new StringBuilder();
		List<Argument> replacable = new ArrayList<Argument>();
		if (DefinitionUtils.isReplacableWithTemplates(argument, replacable)) {
			if (replacable.size() == 1) {
				argument = replacable.get(0);
				lower = argument.getLowerBound();
				upper = argument.getUpperBound();
			} else {
				return definitionToString(replacable);
			}
		}
		if (argument instanceof Constant) {
			out.append(new SynopsisConstant(((Constant) argument).getName())
					.toString());
		} else if (argument instanceof TypedArgument) {
			out.append(new SynopsisArgument(((TypedArgument) argument)
					.getName()).toString());
		} else if (argument instanceof Group) {
			Group gr = (Group) argument;
			String inner = definitionToString(gr.getArguments());
			String cval = gr.getConstant();
			boolean isConst = cval != null && !"".equals(cval);
			boolean isInner = inner != null && !"".equals(inner);
			if (isConst && isInner)
				out.append(new SynopsisConstant(cval).toString()).append(" ")
						.append(inner);
			else if (isConst)
				out.append(new SynopsisConstant(cval).toString());
			else if (isInner)
				out.append(inner);
		} else if (argument instanceof ComplexArgument) {
			ComplexArgument ca = (ComplexArgument) argument;
			// List<Argument> definitions = new ArrayList<Argument>();
			// if (DefinitionUtils.isArgs(ca, definitions))
			// out.append(definitionToString(definitions));
			// else
			out.append("{").append(definitionToString(ca.getArguments()))
					.append("}");
		} else if (argument instanceof Switch) {
			Switch sw = (Switch) argument;
			// List<Argument> definitions = new ArrayList<Argument>();
			// if (DefinitionUtils.isComplexScript(sw, definitions)) {
			// } else if (DefinitionUtils.isPossibleEmptyScript(sw,
			// definitions)) {
			// } else if (DefinitionUtils.isComplexOptions(sw, definitions)) {
			// }
			// if (definitions.size() > 0) {
			// return definitionToString(definitions);
			// } else
			if (isOptions(sw)) {
				String name = sw.getName();
				if (sw.getGroups().size() <= 3) {
					boolean first = true;
					for (Group group : sw.getGroups()) {
						if (first) {
							first = false;
						} else {
							out.append(" ");
						}
						out.append("?").append(definitionToString(group, 1, 1))
								.append("?");
					}
				} else {
					boolean isThereValues = true;
					for (Group group : sw.getGroups()) {
						if (group.getArguments().size() == 0) {
							isThereValues = false;
							break;
						}
					}
					out.append("?");
					if (isThereValues) {
						name = new SynopsisArgument((name != null && !""
								.equals(name)) ? name : "option").toString();
						String value = new SynopsisArgument("value").toString();
						out.append(name).append(" ").append(value).append(
								" ...?");
					} else {
						name = new SynopsisArgument((name != null && !""
								.equals(name)) ? name : "options").toString();
						out.append(name).append(" ...?");
					}
				}
				return out.toString();
			} else if (isMode(sw)) {
				String name = sw.getName();
				name = new SynopsisArgument(
						(name != null && !"".equals(name)) ? name : "mode")
						.toString();
				if (sw.getGroups().size() <= 3) {
					boolean first = true;
					out.append("[");
					for (Group group : sw.getGroups()) {
						if (first) {
							first = false;
						} else {
							out.append("|");
						}
						out.append(definitionToString(group, 1, 1));
					}
					out.append("]");
				} else {
					out.append(name);
				}
				return addBounds(out.toString(), sw.getLowerBound(), sw
						.getUpperBound());
			}
			boolean first = true;
			out.append("[");
			for (Group group : sw.getGroups()) {
				if (first) {
					first = false;
				} else {
					out.append("|");
				}
				out.append(definitionToString(group, group.getLowerBound(),
						group.getUpperBound()));
			}
			out.append("]");

		} else
			throw new IllegalArgumentException();
		return addBounds(out.toString(), lower, upper);
	}

	public String definitionToString(List<Argument> arguments) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Argument argument : arguments) {
			if (first)
				first = false;
			else
				out.append(" ");
			out.append(definitionToString(argument));
		}
		return out.toString();
	}

	public String definitionToString(Argument argument) {
		return definitionToString(argument, argument.getLowerBound(), argument
				.getUpperBound());
	}

	public String argumentToString(TclArgument argument) {
		if (argument instanceof StringArgument) {
			return ((StringArgument) argument).getValue();
		} else if (argument instanceof ComplexString) {
			ComplexString complexString = (ComplexString) argument;
			StringBuffer buffer = new StringBuffer();
			EList<TclArgument> arguments = complexString.getArguments();
			for (TclArgument tclArgument : arguments) {
				buffer.append(argumentToString(tclArgument));
			}
			return buffer.toString();
		} else if (argument instanceof VariableReference) {
			return ((VariableReference) argument).getName();
		} else if (argument instanceof Substitution) {
			return Messages.TclArgumentMatcher_Tcl_Substitution_Display;
		}
		return null;
	}

	public String typeToString(ArgumentType type) {
		switch (type) {
		case ANY:
			return Info.TclArgumentType_Any;
		case BOOLEAN:
			return Info.TclArgumentType_Boolean;
		case CMD_NAME:
			return Info.TclArgumentType_CmdName;
		case EXPRESSION:
			return Info.TclArgumentType_Expression;
		case INDEX:
			return Info.TclArgumentType_Index;
		case INTEGER:
			return Info.TclArgumentType_Integer;
		case LEVEL:
			return Info.TclArgumentType_Level;
		case NAMESPACE:
			return Info.TclArgumentType_Namespace;
		case NOT_NEGATIVE:
			return Info.TclArgumentType_NotNegative;
		case PACKAGE:
			return Info.TclArgumentType_Package;
		case SCRIPT:
			return Info.TclArgumentType_Script;
		case VAR_NAME:
			return Info.TclArgumentType_VarName;
		}
		return null;
	}

	public static boolean isOptions(Switch sw) {
		return sw.getLowerBound() == 0
				&& (sw.getUpperBound() == -1 || sw.getUpperBound() == sw
						.getGroups().size());
	}

	public static boolean isMode(Switch sw) {
		for (Group group : sw.getGroups()) {
			if (group.getArguments().size() != 0)
				return false;
		}
		return true;
	}
}