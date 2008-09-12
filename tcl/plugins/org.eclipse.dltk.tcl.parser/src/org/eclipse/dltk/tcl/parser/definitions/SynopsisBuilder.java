package org.eclipse.dltk.tcl.parser.definitions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.tcl.ast.ArgumentMatch;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;

public class SynopsisBuilder {
	private TclCommand tclCommand;
	private Synopsis root;

	private class SynopsisWord {
		private String word;
		private boolean isBold = false;
		private boolean isItalic = false;

		public SynopsisWord(String word) {
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

	private class Synopsis {
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
					c.setValue(cval);
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
					String cvalue = ((Constant) child.selector).getValue();
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
					String c = ((Constant) matched.get(0).selector).getValue();
					if (value.equals(c))
						return matched;
				}
				for (Synopsis child : matched) {
					String c = ((Constant) child.selector).getValue();
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

		public String getShortSynopsis(TclCommand tclCommand) {
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
						String subResult = synopsises.get(0).getShortSynopsis(
								tclCommand);
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
							String subResult = synopsis
									.getShortSynopsis(tclCommand);
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

	public SynopsisBuilder(TclCommand tclCommand) {
		this.tclCommand = tclCommand;
		Command command = tclCommand.getDefinition();
		if (command == null || command.getName() == null
				|| command.getName().equals("")) {
			throw new IllegalArgumentException();
		}
		processCommand(command);
	}

	private boolean asHtml;

	public SynopsisBuilder(Command command) {
		this(command, false);
	}

	public SynopsisBuilder(Command command, boolean asHtml) {
		if (command == null || command.getName() == null
				|| command.getName().equals("")) {
			throw new IllegalArgumentException();
		}
		this.asHtml = asHtml;
		processCommand(command);
	}

	@Override
	public String toString() {
		if (tclCommand == null)
			return join(root.getSynopsis(), asHtml ? "<br>" : "\n");
		return root.getShortSynopsis(tclCommand);
	}

	public String getSynopsis() {
		return join(root.getSynopsis(), asHtml ? "<br>" : "\n");
	}

	public String getShortSynopsis() {
		return root.getShortSynopsis(tclCommand);
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

	private void processCommand(Command command) {
		root = processArgument(command.getArguments(), 0);
		if (asHtml)
			root.insert("<b>" + command.getName() + "</b>");
		else
			root.insert(command.getName());
	}

	private Synopsis processArgument(List<Argument> arguments, int pos) {
		if (pos >= arguments.size()) {
			Synopsis leaf = new Synopsis();
			if (arguments.size() != 0)
				leaf.selector = arguments.get(0);
			return leaf;
		}
		StringBuilder prefix = new StringBuilder();
		Argument argument = arguments.get(pos);

		if (argument instanceof Switch) {
			Synopsis synopsis = processSubCommands((Switch) argument);
			if (synopsis != null) {
				if (arguments.size() != 0)
					synopsis.selector = arguments.get(0);
				return synopsis;
			}
		}
		prefix.append(outArgument(argument, argument.getLowerBound(), argument
				.getUpperBound()));
		Synopsis synopsis = processArgument(arguments, pos + 1);
		if (prefix != null)
			synopsis.insert(prefix.toString());
		return synopsis;
	}

	private Synopsis processSubCommands(Switch sw) {
		if (isComplexScript(sw) || isComplexOptions(sw) || isOptions(sw)
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
				c.setValue(cval);
				c.setName(cval);
				List<Argument> list = new ArrayList<Argument>();
				list.add(c);
				list.addAll(group.getArguments());
				child = processArgument(list, 0);
			} else {
				child = processArgument(group.getArguments(), 0);
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

	private String outArgument(Argument argument, int lower, int upper) {
		StringBuilder out = new StringBuilder();
		if (argument instanceof Constant) {
			out.append(new SynopsisConstant(((Constant) argument).getValue())
					.toString());
			// out.append(((Constant) argument).getValue());
		} else if (argument instanceof TypedArgument) {
			out.append(new SynopsisArgument(((TypedArgument) argument)
					.getName()).toString());
			// out.append(((TypedArgument) argument).getName());
		} else if (argument instanceof Group) {
			Group gr = (Group) argument;
			String inner = outArguments(gr.getArguments());
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
			if (isArgs(ca))
				out.append(new SynopsisArgument("args").toString());
			else
				out.append("{").append(outArguments(ca.getArguments())).append(
						"}");
		} else if (argument instanceof Switch) {
			Switch sw = (Switch) argument;
			if (isComplexScript(sw)) {
				Argument script = sw.getGroups().get(1).getArguments().get(0);
				String name = script.getName();
				return addBounds(new SynopsisArgument((name != null && !""
						.equals(name)) ? name : "script").toString(), argument
						.getLowerBound(), -1);
			} else if (isPossibleEmptyScript(sw)) {
				Argument script = sw.getGroups().get(1).getArguments().get(0);
				String name = script.getName();
				return new SynopsisArgument(
						(name != null && !"".equals(name)) ? name : "script")
						.toString();
			} else if (isComplexOptions(sw)) {
				String name = sw.getGroups().get(0).getArguments().get(0)
						.getName();
				name = new SynopsisArgument(
						(name != null && !"".equals(name)) ? name : "option")
						.toString();
				String value = new SynopsisArgument("value").toString();
				return out.append("?").append(name).append("? ?").append(value)
						.append("? ?").append(name).append(" ").append(value)
						.append(" ...?").toString();
			} else if (isOptions(sw)) {
				String name = sw.getName();
				if (sw.getGroups().size() <= 3) {
					boolean first = true;
					for (Group group : sw.getGroups()) {
						if (first) {
							first = false;
						} else {
							out.append(" ");
						}
						out.append("?").append(outArgument(group, 1, 1))
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
						out.append(outArgument(group, 1, 1));
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
				out.append(outArgument(group, group.getLowerBound(), group
						.getUpperBound()));
			}
			out.append("]");

		} else
			throw new IllegalArgumentException();
		return addBounds(out.toString(), lower, upper);
	}

	private String outArguments(List<Argument> arguments) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Argument argument : arguments) {
			if (first)
				first = false;
			else
				out.append(" ");
			out.append(outArgument(argument, argument.getLowerBound(), argument
					.getUpperBound()));
		}
		return out.toString();
	}

	private boolean isOptions(Switch sw) {
		return sw.getLowerBound() == 0
				&& (sw.getUpperBound() == -1 || sw.getUpperBound() == sw
						.getGroups().size());
	}

	private boolean isMode(Switch sw) {
		for (Group group : sw.getGroups()) {
			if (group.getArguments().size() != 0)
				return false;
		}
		return true;
	}

	private boolean isArgs(ComplexArgument argument) {
		ComplexArgument argList = argument;
		if (argList.getLowerBound() == 1 && argList.getUpperBound() == 1
				&& argList.getArguments().size() == 1
				&& argList.getArguments().get(0) instanceof ComplexArgument) {
			ComplexArgument argDef = (ComplexArgument) argList.getArguments()
					.get(0);
			if (argDef.getLowerBound() == 0
					&& argDef.getUpperBound() == -1
					&& argDef.getArguments().size() == 2
					&& argDef.getArguments().get(0) instanceof ComplexArgument
					&& ((ComplexArgument) argDef.getArguments().get(0))
							.getArguments().size() == 1) {
				Argument argName = ((ComplexArgument) argDef.getArguments()
						.get(0)).getArguments().get(0);
				Argument argValue = argDef.getArguments().get(1);
				if ("arg".equals(argName.getName())
						&& "value".equals(argValue.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isComplexScript(Switch argument) {
		if (argument.getGroups().size() == 2
				&& argument.getGroups().get(0).getArguments().size() == 1
				&& argument.getGroups().get(1).getArguments().size() == 1) {
			Argument script = argument.getGroups().get(0).getArguments().get(0);
			Argument complex = argument.getGroups().get(1).getArguments()
					.get(0);
			if (script instanceof TypedArgument
					&& ((TypedArgument) script).getType().getValue() == ArgumentType.SCRIPT_VALUE
					&& script.getLowerBound() == 1
					&& script.getUpperBound() == 1
					&& complex instanceof TypedArgument
					&& ((TypedArgument) complex).getType().getValue() == ArgumentType.ANY_VALUE
					&& complex.getLowerBound() == 1
					&& complex.getUpperBound() == -1) {
				return true;
			}
		}
		return false;
	}

	private boolean isPossibleEmptyScript(Switch argument) {
		if (argument.getGroups().size() == 2
				&& argument.getGroups().get(0).getArguments().size() == 0
				&& "-".equals(argument.getGroups().get(0).getConstant())
				&& argument.getGroups().get(1).getArguments().size() == 1) {
			Argument script = argument.getGroups().get(1).getArguments().get(0);
			if (script instanceof TypedArgument
					&& ((TypedArgument) script).getType().getValue() == ArgumentType.SCRIPT_VALUE
					&& script.getLowerBound() == 1
					&& script.getUpperBound() == 1) {
				return true;
			}
		}
		return false;
	}

	private boolean isComplexOptions(Switch argument) {
		if (argument.getGroups().size() == 2
				&& argument.getGroups().get(0).getArguments().size() == 1
				&& argument.getGroups().get(1).getArguments().size() == 1
				&& argument.getGroups().get(0).getArguments().get(0) instanceof Switch
				&& argument.getGroups().get(1).getArguments().get(0) instanceof Switch
				&& argument.getGroups().get(0).getArguments().get(0)
						.getLowerBound() == 0
				&& argument.getGroups().get(0).getArguments().get(0)
						.getUpperBound() == 1
				&& argument.getGroups().get(1).getArguments().get(0)
						.getLowerBound() == 0
				&& argument.getGroups().get(1).getArguments().get(0)
						.getUpperBound() == -1) {
			return true;
		}
		return false;
	}
}