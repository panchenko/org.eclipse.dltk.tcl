package org.eclipse.dltk.tcl.parser.definitions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.tcl.ast.ArgumentMatch;
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

	private abstract class Synopsis {
		protected List<Argument> selectors = new ArrayList<Argument>();

		abstract public void insert(String prefix);

		abstract public Synopsis match(List<ArgumentMatch> matches);
	}

	private class SwitchSynopsis extends Synopsis {
		private List<Synopsis> children = new ArrayList<Synopsis>();

		public void insert(String prefix) {
			if (prefix == null || "".equals(prefix))
				return;
			for (Synopsis child : children) {
				child.insert(prefix);
			}
		}

		public Synopsis match(List<ArgumentMatch> matches) {
			for (Synopsis child : children) {
				Synopsis result = child.match(matches);
				if (result != null) {
					return result;
				}
			}
			if (selectors.size() == 0)
				return null;
			for (Argument selector : selectors) {
				for (ArgumentMatch match : matches) {
					if (DefinitionUtils.equalsArgumentIgnoreName(selector,
							match.getDefinition())) {
						return this;
					}
				}
			}
			return null;
		}

		public String toString() {
			StringBuilder out = new StringBuilder();
			boolean first = true;
			for (Synopsis child : children) {
				if (first)
					first = false;
				else
					out.append("\n");
				out.append(child.toString());
			}
			return out.toString();
		}
	}

	private class LeafSynopsis extends Synopsis {
		private String string = null;

		public void insert(String prefix) {
			if (prefix == null || "".equals(prefix))
				return;
			if (string == null)
				string = prefix;
			else
				string = prefix + " " + string;
		}

		public Synopsis match(List<ArgumentMatch> matches) {
			if (selectors.size() == 0)
				return null;
			for (Argument selector : selectors) {
				for (ArgumentMatch match : matches) {
					if (DefinitionUtils.equalsArgumentIgnoreName(selector,
							match.getDefinition())) {
						return this;
					}
				}
			}
			return null;
		}

		public String toString() {
			return (string == null) ? "" : string;
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

	public SynopsisBuilder(Command command) {
		if (command == null || command.getName() == null
				|| command.getName().equals("")) {
			throw new IllegalArgumentException();
		}
		processCommand(command);
	}

	@Override
	public String toString() {
		if (tclCommand == null)
			return root.toString();
		Synopsis synopsis = root.match(tclCommand.getMatches());
		if (synopsis == null)
			return root.toString();
		return synopsis.toString();
	}

	private void processCommand(Command command) {
		root = processArgument(command.getArguments(), 0);
		root.insert(command.getName());
	}

	private Synopsis processArgument(List<Argument> arguments, int pos) {
		if (pos >= arguments.size()) {
			LeafSynopsis leaf = new LeafSynopsis();
			if (arguments.size() == 0)
				return leaf;
			if (arguments.get(0) instanceof Switch) {
				for (Group group : ((Switch) arguments.get(0)).getGroups()) {
					String cval = group.getConstant();
					if (cval != null && !cval.equals("")) {
						Constant c = DefinitionsFactory.eINSTANCE
								.createConstant();
						c.setValue(cval);
						c.setName(cval);
						leaf.selectors.add(c);
					} else if (group.getArguments().size() != 0) {
						leaf.selectors.add(group.getArguments().get(0));
					}
				}
			} else if (arguments.get(0) instanceof Group) {
				Group group = ((Group) arguments.get(0));
				String cval = group.getConstant();
				if (cval != null && !cval.equals("")) {
					Constant c = DefinitionsFactory.eINSTANCE.createConstant();
					c.setValue(cval);
					c.setName(cval);
					leaf.selectors.add(c);
				} else if (group.getArguments().size() != 0) {
					leaf.selectors.add(group.getArguments().get(0));
				}
			} else if (arguments.get(0) instanceof ComplexArgument) {
				ComplexArgument complex = ((ComplexArgument) arguments.get(0));
				if (complex.getArguments().size() != 0)
					leaf.selectors.add(complex.getArguments().get(0));
			} else {
				leaf.selectors.add(arguments.get(0));
			}
			return leaf;
		}
		StringBuilder prefix = new StringBuilder();
		Argument argument = arguments.get(pos);

		if (argument instanceof Switch) {
			Switch sw = (Switch) argument;
			String replace = null;
			replace = tryToReplaceComplexScript(sw);
			if (replace == null) {
				replace = tryToReplaceComplexOptions((Switch) argument);
			}
			if (replace != null) {
				prefix.append(replace);
			} else {
				String name = argument.getName();
				if (sw.getUpperBound() == -1
						|| sw.getUpperBound() == sw.getGroups().size()) {
					if (sw.getGroups().size() <= 3) {
						boolean first = true;
						for (Group group : sw.getGroups()) {
							if (first) {
								first = false;
							} else {
								prefix.append(" ");
							}
							prefix.append("?").append(processInner(group))
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
						prefix.append("?");
						if (isThereValues) {
							prefix
									.append((name != null && !"".equals(name)) ? name
											: "option");
							prefix.append(" value ...?");
						} else {
							prefix
									.append((name != null && !"".equals(name)) ? name
											: "options");
							prefix.append(" ...?");
						}
					}
				} else {
					StringBuilder mode = new StringBuilder();
					boolean isMode = true;
					for (Group group : sw.getGroups()) {
						if (group.getArguments().size() != 0) {
							isMode = false;
							break;
						}
					}
					if (isMode) {
						if (sw.getGroups().size() <= 3) {
							boolean first = true;
							mode.append("[");
							for (Group group : sw.getGroups()) {
								if (first) {
									first = false;
								} else {
									mode.append("|");
								}
								mode.append(processInner(group));
							}
							mode.append("]");
						} else {
							mode
									.append((name != null && !"".equals(name)) ? name
											: "mode");
						}
						prefix.append(addBounds(mode.toString(), argument
								.getLowerBound(), argument.getUpperBound()));
					} else if (argument.getUpperBound() == 1) {
						SwitchSynopsis swSyn = new SwitchSynopsis();
						if (argument.getLowerBound() == 0) {
							swSyn.children.add(new LeafSynopsis());
						}

						for (Group group : sw.getGroups()) {
							Synopsis synopsis;
							String cval = group.getConstant();
							if (cval != null && !"".equals(cval)) {
								Constant c = DefinitionsFactory.eINSTANCE
										.createConstant();
								c.setValue(cval);
								c.setName(cval);
								List<Argument> list = new ArrayList<Argument>();
								list.add(c);
								list.addAll(group.getArguments());
								synopsis = processArgument(list, 0);
							} else {
								synopsis = processArgument(
										group.getArguments(), 0);
							}
							swSyn.children.add(synopsis);
						}
						return swSyn;
					} else {
						boolean first = true;
						prefix.append("[");
						for (Group group : sw.getGroups()) {
							if (first) {
								first = false;
							} else {
								prefix.append(" ");
							}
							prefix.append(processInner(group));
						}
						prefix.append("](").append(argument.getLowerBound())
								.append("-").append(argument.getUpperBound())
								.append(")");
					}
				}
			}
		} else {
			prefix.append(addBounds(processInner(argument).toString(),
					argument.getLowerBound(), argument.getUpperBound())
					.toString());
		}
		Synopsis synopsis = processArgument(arguments, pos + 1);
		synopsis.insert(prefix.toString());
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

	private StringBuilder processInner(Argument argument) {
		StringBuilder out = new StringBuilder();
		if (argument instanceof Constant) {
			out.append(((Constant) argument).getValue());
		} else if (argument instanceof TypedArgument) {
			out.append(((TypedArgument) argument).getName());
		} else if (argument instanceof Group) {
			String groupConst = ((Group) argument).getConstant();
			Synopsis leaf = processArgument(((Group) argument).getArguments(),
					0);
			if (leaf instanceof LeafSynopsis) {
				if (groupConst != null && !"".equals(groupConst))
					leaf.insert(groupConst);
				out.append(leaf.toString());
			} else
				throw new IllegalArgumentException();
		} else if (argument instanceof ComplexArgument) {
			String result = tryToReplaceArgs((ComplexArgument) argument);
			if (result != null) {
				out.append(result);
				return out;
			}
			Synopsis leaf = processArgument(((ComplexArgument) argument)
					.getArguments(), 0);

			if (leaf instanceof LeafSynopsis) {
				out.append("{");
				out.append(leaf.toString());
				out.append("}");
			} else
				throw new IllegalArgumentException();
		} else {
			throw new IllegalArgumentException();
		}
		return out;
	}

	private String tryToReplaceArgs(ComplexArgument argument) {
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
					return addBounds("args", argument.getLowerBound(), argument
							.getUpperBound());
				}
			}
		}
		return null;
	}

	private String tryToReplaceComplexScript(Switch argument) {
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

				String name = script.getName();
				return addBounds((name != null && !"".equals(name)) ? name
						: "script", argument.getLowerBound(), -1);
			}
		} else if (argument.getGroups().size() == 2
				&& argument.getGroups().get(0).getArguments().size() == 0
				&& "-".equals(argument.getGroups().get(0).getConstant())
				&& argument.getGroups().get(1).getArguments().size() == 1) {
			Argument script = argument.getGroups().get(1).getArguments().get(0);
			if (script instanceof TypedArgument
					&& ((TypedArgument) script).getType().getValue() == ArgumentType.SCRIPT_VALUE
					&& script.getLowerBound() == 1
					&& script.getUpperBound() == 1) {
				String name = script.getName();
				return (name != null && !"".equals(name)) ? name : "script";
			}
		}
		return null;
	}

	private String tryToReplaceComplexOptions(Switch argument) {
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
			String name = argument.getGroups().get(0).getArguments().get(0)
					.getName();
			name = (name != null && !"".equals(name)) ? name : "option";
			StringBuilder result = new StringBuilder();
			result.append("?").append(name).append("? ?value? ?").append(name)
					.append(" value ...?");
			return result.toString();
		}
		return null;
	}
}