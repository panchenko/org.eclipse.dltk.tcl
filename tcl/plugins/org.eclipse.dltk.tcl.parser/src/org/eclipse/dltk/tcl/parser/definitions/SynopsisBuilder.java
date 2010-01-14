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
	private final static String NULL_SYNOPSIS = ""; //$NON-NLS-1$
	private final static String NULL_LINE = "..."; //$NON-NLS-1$
	private final static String NULL_ARG_DEFINITON_SYNOPSIS = "arg"; //$NON-NLS-1$
	private final static String NULL_TCL_ARGUMENT_SYNOPSIS = "none"; //$NON-NLS-1$

	private final static String ENDLESS_BOUNDS_END = " ..."; //$NON-NLS-1$
	private final static String POSSIBLE_START = "?"; //$NON-NLS-1$
	private final static String POSSIBLE_END = "?"; //$NON-NLS-1$
	private final static String SWITCH_START = "["; //$NON-NLS-1$
	private final static String SWITCH_END = "]"; //$NON-NLS-1$
	private final static String SWITCH_SEPARATOR = "|"; //$NON-NLS-1$
	private final static String COMPLEX_ARGUMENT_START = "{"; //$NON-NLS-1$
	private final static String COMPLEX_ARGUMENT_END = "}"; //$NON-NLS-1$

	private final static String BOUNDS_START = "("; //$NON-NLS-1$
	private final static String BOUNDS_END = ")"; //$NON-NLS-1$
	private final static String BOUNDS_SEPARATOR = "-"; //$NON-NLS-1$

	private final static String DEFINITION_SEPARATOR = " "; //$NON-NLS-1$
	private final static String LINE_SEPARATOR = "\n"; //$NON-NLS-1$
	private final static String HTML_LINE_SEPARATOR = "<br/>"; //$NON-NLS-1$
	private final static String LIST_SEPARATOR = ","; //$NON-NLS-1$

	private final static String HTML_CONST_START = "<b>"; //$NON-NLS-1$
	private final static String HTML_CONST_END = "</b>"; //$NON-NLS-1$
	private final static String HTML_ARGUMENT_START = "<i>"; //$NON-NLS-1$
	private final static String HTML_ARGUMENT_END = "</i>"; //$NON-NLS-1$

	private final static String CONST_START = ""; //$NON-NLS-1$
	private final static String CONST_END = ""; //$NON-NLS-1$
	private final static String ARGUMENT_START = ""; //$NON-NLS-1$
	private final static String ARGUMENT_END = ""; //$NON-NLS-1$

	private class SynopsisWord {
		private String word;
		private String start;
		private String end;

		public SynopsisWord(String word, String start, String end) {
			this.word = word;
			this.start = start;
			this.end = end;
		}

		@Override
		public String toString() {
			StringBuilder result = new StringBuilder();
			result.append(start);
			result.append(word);
			result.append(end);
			return result.toString();
		}
	}

	private class SynopsisConstant extends SynopsisWord {
		public SynopsisConstant(String word) {
			super(word, (asHtml) ? HTML_CONST_START : CONST_START,
					(asHtml) ? HTML_CONST_END : CONST_END);
		}
	}

	private class SynopsisArgument extends SynopsisWord {
		public SynopsisArgument(String word) {
			super(word, (asHtml) ? HTML_ARGUMENT_START : ARGUMENT_START,
					(asHtml) ? HTML_ARGUMENT_END : ARGUMENT_END);
		}
	}

	private static class Synopsis {
		protected Argument selector;
		private List<Synopsis> children = new ArrayList<Synopsis>();
		private String string = null;

		public Synopsis() {
		}

		public void insert(String prefix) {
			if (prefix == null || prefix.length() == 0)
				return;
			if (string == null)
				string = prefix;
			else
				string = prefix + DEFINITION_SEPARATOR + string;
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
				if (cval != null && cval.length() > 0) {
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
				return (string == null) ? NULL_SYNOPSIS : string;
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
						if (subResult.length() > 0)
							result.append(DEFINITION_SEPARATOR).append(
									subResult);
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
							if (subResult.length() > 0)
								result.append(DEFINITION_SEPARATOR).append(
										subResult);
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
					result.append(DEFINITION_SEPARATOR).append(SWITCH_START);
					first = false;
				} else
					result.append(SWITCH_SEPARATOR);
				if (synopsis.string == null) {
					result.append(NULL_LINE);
					continue;
				}
				String[] words = synopsis.string.split(DEFINITION_SEPARATOR);
				result.append(words[0]);
				if (synopsis.children.size() != 0 || words.length > 1)
					result.append(ENDLESS_BOUNDS_END);
			}
			result.append(SWITCH_END);
			return result.toString();
		}

		public List<String> getSynopsis() {
			List<String> results = new ArrayList<String>();
			if (children.size() == 0) {
				results.add((string == null) ? NULL_SYNOPSIS : string);
			}
			for (Synopsis child : children) {
				List<String> subResults = child.getSynopsis();
				if (subResults.size() == 0) {
					results.add(NULL_SYNOPSIS);
				}
				for (String subResult : subResults) {
					StringBuilder result = new StringBuilder();
					if (string != null)
						result.append(string);
					if (subResult.length() > 0)
						result.append(DEFINITION_SEPARATOR).append(subResult);
					results.add(result.toString());
				}
			}
			return results;
		}
	}

	private boolean asHtml;

	public SynopsisBuilder() {
		this(false);
	}

	public SynopsisBuilder(boolean asHtml) {
		this.asHtml = asHtml;
	}

	public String getSynopsis(Command command) {
		if (command == null)
			return NULL_LINE;
		Synopsis root = processCommand(command);
		return join(root.getSynopsis(), asHtml ? HTML_LINE_SEPARATOR
				: LINE_SEPARATOR);
	}

	public String getShortHint(TclCommand tclCommand) {
		if (tclCommand == null || tclCommand.getDefinition() == null)
			return NULL_LINE;
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
		Argument argument = arguments.get(pos);

		if (argument instanceof Switch) {
			Synopsis synopsis = processSubCommands(arguments, pos);
			if (synopsis != null) {
				if (arguments.size() != 0)
					synopsis.selector = arguments.get(0);
				return synopsis;
			}
		}
		StringBuilder prefix = new StringBuilder();
		prefix.append(definitionToString(argument, argument.getLowerBound(),
				argument.getUpperBound()));
		Synopsis synopsis = processArgumentList(arguments, pos + 1);
		synopsis.insert(prefix.toString());
		return synopsis;
	}

	private Synopsis processSubCommands(List<Argument> arguments, int pos) {
		Switch sw = (Switch) arguments.get(pos);
		if (DefinitionUtils.isOptions(sw) || DefinitionUtils.isMode(sw)
				|| (sw.getName() != null && sw.getName().length() > 0))
			return null;
		Synopsis synopsis = new Synopsis();
		if (sw.getLowerBound() == 0) {
			synopsis.children.add(new Synopsis());
		}

		for (Group group : sw.getGroups()) {
			Synopsis child;
			String cval = group.getConstant();
			if (cval != null && cval.length() > 0) {
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
				result.append(POSSIBLE_START).append(inner).append(
						ENDLESS_BOUNDS_END).append(POSSIBLE_END);
			} else {
				if (upper <= 2) {
					boolean first = true;
					for (int i = 0; i < upper; i++) {
						if (first) {
							first = false;
						} else {
							result.append(DEFINITION_SEPARATOR);
						}
						result.append(POSSIBLE_START).append(inner).append(
								POSSIBLE_END);
					}
				} else {
					result.append(inner).append(BOUNDS_START).append(lower)
							.append(BOUNDS_SEPARATOR).append(upper).append(
									BOUNDS_END);
				}
			}
		} else {
			if (upper <= 2) {
				boolean first = true;
				for (int i = 0; i < lower; i++) {
					if (first) {
						first = false;
					} else {
						result.append(DEFINITION_SEPARATOR);
					}
					result.append(inner);
				}
				if (upper == -1) {
					result.append(DEFINITION_SEPARATOR).append(POSSIBLE_START)
							.append(inner).append(ENDLESS_BOUNDS_END).append(
									POSSIBLE_END);
				} else {
					for (int i = 0; i < upper - lower; i++) {
						result.append(DEFINITION_SEPARATOR).append(
								POSSIBLE_START).append(inner).append(
								POSSIBLE_END);
					}
				}
			} else {
				result.append(inner).append(BOUNDS_START).append(lower).append(
						BOUNDS_SEPARATOR).append(upper).append(BOUNDS_END);
			}
		}
		return result.toString();
	}

	public String definitionToList(List<Argument> definitions) {
		if (definitions == null || definitions.size() == 0)
			return NULL_SYNOPSIS;
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
				result.append(LIST_SEPARATOR);
			}
			result.append(s);
		}
		return result.toString();
	}

	public String definitionToString(Argument argument, int lower, int upper) {
		if (argument == null)
			return addBounds(new SynopsisArgument(NULL_ARG_DEFINITON_SYNOPSIS)
					.toString(), lower, upper);
		StringBuilder out = new StringBuilder();
		if (argument instanceof Constant) {
			out.append(new SynopsisConstant(((Constant) argument).getName())
					.toString());
		} else if (argument instanceof TypedArgument
				|| (argument.getName() != null && argument.getName().length() > 0)) {
			out.append(new SynopsisArgument(argument.getName()).toString());
		} else if (argument instanceof Group) {
			Group gr = (Group) argument;
			String inner = definitionToString(gr.getArguments());
			String cval = gr.getConstant();
			boolean isConst = cval != null && cval.length() > 0;
			boolean isInner = inner != null && inner.length() > 0;
			if (isConst && isInner)
				out.append(new SynopsisConstant(cval).toString()).append(
						DEFINITION_SEPARATOR).append(inner);
			else if (isConst)
				out.append(new SynopsisConstant(cval).toString());
			else if (isInner)
				out.append(inner);
		} else if (argument instanceof ComplexArgument) {
			ComplexArgument ca = (ComplexArgument) argument;
			out.append(COMPLEX_ARGUMENT_START).append(
					definitionToString(ca.getArguments())).append(
					COMPLEX_ARGUMENT_END);
		} else if (argument instanceof Switch) {
			Switch sw = (Switch) argument;
			if (DefinitionUtils.isOptions(sw)) {
				boolean first = true;
				for (Group group : sw.getGroups()) {
					if (first) {
						first = false;
					} else {
						out.append(DEFINITION_SEPARATOR);
					}
					out.append(POSSIBLE_START).append(
							definitionToString(group, 1, 1)).append(
							POSSIBLE_END);
				}
				return out.toString();
			} else if (DefinitionUtils.isMode(sw)) {
				boolean first = true;
				out.append(SWITCH_START);
				for (Group group : sw.getGroups()) {
					if (first) {
						first = false;
					} else {
						out.append(SWITCH_SEPARATOR);
					}
					out.append(definitionToString(group, 1, 1));
				}
				out.append(SWITCH_END);
				return addBounds(out.toString(), sw.getLowerBound(), sw
						.getUpperBound());
			}
			boolean first = true;
			out.append(SWITCH_START);
			for (Group group : sw.getGroups()) {
				if (first) {
					first = false;
				} else {
					out.append(SWITCH_SEPARATOR);
				}
				out.append(definitionToString(group, group.getLowerBound(),
						group.getUpperBound()));
			}
			out.append(SWITCH_END);

		} else
			throw new IllegalArgumentException();
		return addBounds(out.toString(), lower, upper);
	}

	public String definitionToString(List<Argument> definitions) {
		if (definitions == null || definitions.size() == 0)
			return NULL_SYNOPSIS;
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Argument definition : definitions) {
			if (first)
				first = false;
			else
				out.append(DEFINITION_SEPARATOR);
			out.append(definitionToString(definition));
		}
		return out.toString();
	}

	public String definitionToString(Argument definition) {
		if (definition == null)
			return NULL_ARG_DEFINITON_SYNOPSIS;
		return definitionToString(definition, definition.getLowerBound(),
				definition.getUpperBound());
	}

	public String argumentToString(TclArgument argument) {
		if (argument == null)
			return NULL_TCL_ARGUMENT_SYNOPSIS;
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
}