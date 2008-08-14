/*******************************************************************************
 * Copyright (c) 2008 xored software, Inc.  
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html  
 *
 * Contributors:
 *     xored software, Inc. - initial API and Implementation (Andrei Sobolev)
 *******************************************************************************/
package org.eclipse.dltk.tcl.parser.definitions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.DefinitionsFactory;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.emf.common.util.EList;

public class DefinitionUtils {
	/**
	 * Generate switch variants.
	 */
	public final static String GENERATE_VARIANTS = "reduce_to_less";
	public static final String SWITCH_COUNT = "switch_count";

	private static boolean isSet(Map<String, Object> options, String key) {
		if (options == null) {
			return false;
		}
		if (!options.containsKey(key)) {
			return false;
		}
		Object object = options.get(key);
		if (object instanceof Boolean) {
			return ((Boolean) object).booleanValue();
		}
		return false;
	}

	private static int getInt(Map<String, Object> options, String key, int def) {
		if (options == null) {
			return def;
		}
		if (!options.containsKey(key)) {
			return def;
		}
		Object object = options.get(key);
		if (object instanceof Integer) {
			return ((Integer) object).intValue();
		}
		return def;
	}

	public static List<List<Argument>> reduceSwitches(List<Argument> list,
			Map<String, Object> options) {
		List<List<Argument>> argumentsList = new ArrayList<List<Argument>>();
		for (Argument argument : list) {
			List<Argument> args = reduceSwitchesArgument(argument, options);
			List<List<Argument>> newArgumentsList = new ArrayList<List<Argument>>();
			if (argumentsList.size() == 0) {
				for (Argument arg : args) {
					List<Argument> a = new ArrayList<Argument>();
					a.add(arg);
					newArgumentsList.add(a);
				}
			} else {
				for (Argument nextArgument : args) {
					// Add argument to all other. Fill existed and append
					for (List<Argument> al : argumentsList) {
						List<Argument> nal = new ArrayList<Argument>();
						for (Argument argument2 : al) {
							nal.add(copyArgument(argument2));
						}
						nal.add(nextArgument);
						newArgumentsList.add(nal);
					}
				}
			}
			argumentsList = newArgumentsList;
		}
		return argumentsList;
	}

	public static Command[] reduceSwitches(Command command) {
		return reduceSwitches(command, null);
	}

	public static Command[] reduceSwitches(Command command,
			Map<String, Object> options) {
		List<Command> commands = new ArrayList<Command>();
		EList<Argument> arguments = command.getArguments();
		List<List<Argument>> removeSwitches = reduceSwitches(arguments, options);
		for (List<Argument> list : removeSwitches) {
			Command newCommand = DefinitionsFactory.eINSTANCE.createCommand();
			newCommand.setDeprecated(command.getDeprecated());
			newCommand.setName(command.getName());
			newCommand.setVersion(command.getVersion());
			newCommand.getArguments().addAll(list);
			commands.add(newCommand);
		}
		return commands.toArray(new Command[commands.size()]);
	}

	private static List<Argument> reduceSwitchesArgument(Argument argument,
			Map<String, Object> options) {
		List<Argument> results = new ArrayList<Argument>();
		if (argument instanceof Switch) {
			Switch sw = (Switch) argument;
			if (isSet(options, GENERATE_VARIANTS)) {
				if (sw.getUpperBound() == -1) {
					sw = (Switch) copyArgument(sw);
					sw.setUpperBound(sw.getLowerBound()
							+ getInt(options, SWITCH_COUNT, 1));
				}
			}
			if (sw.getUpperBound() != -1) {
				List<List<Group>> switchVariants = new ArrayList<List<Group>>();
				for (int i = 0; i < sw.getLowerBound(); i++) {
					List<Group> list = processGroups(sw, 1, 1, options);
					switchVariants = updateVariants(switchVariants, list, false);
				}
				for (int i = sw.getLowerBound(); i < sw.getUpperBound(); i++) {
					List<Group> list = processGroups(sw, 0, 1, options);
					switchVariants = updateVariants(switchVariants, list,
							isSet(options, GENERATE_VARIANTS));
				}
				if (switchVariants.size() > 1) {
					for (List<Group> list : switchVariants) {
						addResult(results, list);
					}
				} else {
					List<Group> gg = switchVariants.get(0);
					if (gg.size() == 1) {
						results.add(gg.get(0));
					} else {
						addResult(results, gg);
					}
				}
			} else {
				// Just copy switch.
				EList<Group> groups = sw.getGroups();
				Switch nsw = copySwitch(sw);
				for (Group group : groups) {
					List<Argument> variants = reduceSwitchesArgument(group,
							options);
					for (Argument variant : variants) {
						nsw.getGroups().add((Group) variant);
					}
				}
				results.add(nsw);
			}

		} else if (argument instanceof Group) {
			Group g = (Group) argument;
			List<List<Argument>> removeSwitches = reduceSwitches(g
					.getArguments(), options);
			if (removeSwitches.size() > 0) {
				for (List<Argument> list : removeSwitches) {
					Group ng = copyGroup(g);
					ng.getArguments().addAll(list);
					results.add(ng);
				}
			} else {
				Group ng = copyGroup(g);
				results.add(ng);
			}
		} else if (argument instanceof ComplexArgument) {
			ComplexArgument c = (ComplexArgument) argument;
			List<List<Argument>> removeSwitches = reduceSwitches(c
					.getArguments(), options);
			if (removeSwitches.size() > 0) {
				for (List<Argument> list : removeSwitches) {
					ComplexArgument ng = copyComplexArgument(c);
					ng.getArguments().addAll(list);
					results.add(ng);
				}
			} else {
				ComplexArgument ng = copyComplexArgument(c);
				results.add(ng);
			}
		} else if (argument instanceof Constant) {
			Constant nc = copyConstant((Constant) argument);
			results.add(nc);
		} else if (argument instanceof TypedArgument) {
			TypedArgument tc = copyTypedArgument(argument);
			results.add(tc);
		}
		return results;
	}

	public static Switch copySwitch(Switch sw) {
		Switch nsw = DefinitionsFactory.eINSTANCE.createSwitch();
		nsw.setLowerBound(sw.getLowerBound());
		nsw.setUpperBound(sw.getUpperBound());
		return nsw;
	}

	public static TypedArgument copyTypedArgument(Argument argument) {
		TypedArgument tc = DefinitionsFactory.eINSTANCE.createTypedArgument();
		tc.setLowerBound(argument.getLowerBound());
		tc.setUpperBound(argument.getUpperBound());
		tc.setType(((TypedArgument) argument).getType());
		tc.setName(((TypedArgument) argument).getName());
		return tc;
	}

	public static Constant copyConstant(Constant argument) {
		Constant nc = DefinitionsFactory.eINSTANCE.createConstant();
		nc.setLowerBound(argument.getLowerBound());
		nc.setUpperBound(argument.getUpperBound());
		nc.setValue(argument.getValue());
		return nc;
	}

	public static ComplexArgument copyComplexArgument(ComplexArgument c) {
		ComplexArgument ng = DefinitionsFactory.eINSTANCE
				.createComplexArgument();
		ng.setLowerBound(c.getLowerBound());
		ng.setUpperBound(c.getUpperBound());
		return ng;
	}

	public static Group copyGroup(Group g) {
		Group ng = DefinitionsFactory.eINSTANCE.createGroup();
		ng.setLowerBound(g.getLowerBound());
		ng.setUpperBound(g.getUpperBound());
		ng.setConstant(g.getConstant());
		return ng;
	}

	public static void addResult(List<Argument> results, List<Group> list) {
		Group ng = DefinitionsFactory.eINSTANCE.createGroup();
		ng.setLowerBound(1);
		ng.setUpperBound(1);
		ng.getArguments().addAll(list);
		results.add(ng);
	}

	private static List<List<Group>> updateVariants(
			List<List<Group>> switchVariants, List<Group> list,
			boolean addPrevious) {
		List<List<Group>> resultList = new ArrayList<List<Group>>();
		if (switchVariants.size() == 0) {
			for (Group g : list) {
				List<Group> gg = new ArrayList<Group>();
				gg.add(g);
				resultList.add(gg);
			}
		} else {
			// We need to copy all of variants and add one element to each.
			for (Group g : list) {
				for (List<Group> nle : switchVariants) {
					Argument rg = copyArgument(g);
					List<Group> nl = new ArrayList<Group>();
					for (Group nlee : nle) {
						Argument nleer = copyArgument(nlee);
						nl.add((Group) nleer);
					}
					nl.add((Group) rg);
					resultList.add(nl);
				}
			}
		}
		if (addPrevious) {
			resultList.addAll(switchVariants);
		}

		return resultList;
	}

	public static Argument copyArgument(Argument a) {
		if (a instanceof Constant) {
			return copyConstant((Constant) a);
		} else if (a instanceof TypedArgument) {
			return copyTypedArgument(a);
		} else if (a instanceof Switch) {
			Switch s = (Switch) a;
			Switch ns = copySwitch(s);
			EList<Group> groups = s.getGroups();
			for (Group group : groups) {
				ns.getGroups().add((Group) copyArgument(group));
			}
			return ns;
		} else if (a instanceof Group) {
			Group g = (Group) a;
			Group ng = copyGroup(g);
			EList<Argument> arguments = g.getArguments();
			for (Argument argument : arguments) {
				ng.getArguments().add(copyArgument(argument));
			}
			return ng;
		} else if (a instanceof ComplexArgument) {
			ComplexArgument g = (ComplexArgument) a;
			ComplexArgument ng = copyComplexArgument(g);
			EList<Argument> arguments = g.getArguments();
			for (Argument argument : arguments) {
				ng.getArguments().add(copyArgument(argument));
			}
			return ng;
		}
		return null;
	}

	private static List<Group> processGroups(Switch sw, int l, int u,
			Map<String, Object> options) {
		EList<Group> groups = sw.getGroups();
		List<Group> results = new ArrayList<Group>();
		for (Group group : groups) {
			List<Argument> variants = reduceSwitchesArgument(group, options);
			for (Argument variant : variants) {
				Group ng = DefinitionsFactory.eINSTANCE.createGroup();
				ng.setLowerBound(l);
				ng.setUpperBound(u);
				ng.getArguments().add(variant);
				results.add(ng);
			}
		}
		return results;
	}

	public static String convertToString(Command command) {
		return convertToString(command, false);
	}

	public static String convertToString(Command command, boolean clean) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(command.getName());
		for (Argument arg : command.getArguments()) {
			buffer.append(' ');
			buffer.append(convertToString(arg, clean));
		}
		return buffer.toString();
	}

	private static String convertToString(Argument arg, boolean clean) {
		if (arg instanceof Constant) {
			return ((Constant) arg).getValue() + boundsToString(arg);
		} else if (arg instanceof TypedArgument) {
			TypedArgument typed = (TypedArgument) arg;
			if (!clean) {
				return typed.getName()
						/* + ":" + ((TypedArgument) arg).getType() */+ boundsToString(arg);
			} else {
				switch (typed.getType().getValue()) {
				case ArgumentType.SCRIPT_VALUE:
					return "{unk_cmd}";
				case ArgumentType.EXPRESSION_VALUE:
					return "{$i>0}";
				case ArgumentType.INDEX_VALUE:
					return "1";
				case ArgumentType.NOT_NEGATIVE_VALUE:
					return "1";
				case ArgumentType.INTEGER_VALUE:
					return "0";
				case ArgumentType.BOOLEAN_VALUE:
					return "true";
				case ArgumentType.VAR_NAME_VALUE:
					return "var";
				case ArgumentType.LEVEL_VALUE:
					return "#1";
				default:// Just a string
					return typed.getName();
				}
			}
		} else if (arg instanceof Group) {
			Group group = (Group) arg;
			EList<Argument> arguments = group.getArguments();
			StringBuffer b = new StringBuffer();
			if (group.getArguments().size() > 0) {
				if (!clean) {
					b.append('(');
				}
				String constant = group.getConstant();
				if (constant != null) {
					b.append(constant + " ");
				}
				boolean first = true;
				for (Argument argument : arguments) {
					if (!first) {
						b.append(" ");
					} else {
						first = false;
					}
					b.append(convertToString(argument, clean));
				}
				if (!clean) {
					b.append(')');
				}
				b.append(boundsToString(arg));
			} else {
				String constant = group.getConstant();
				if (constant != null) {
					b.append(constant);
				}
			}
			return b.toString();
		} else if (arg instanceof ComplexArgument) {
			EList<Argument> arguments = ((ComplexArgument) arg).getArguments();
			StringBuffer b = new StringBuffer();
			if (!clean) {
				b.append('^');
			} else {
				b.append('{');
			}
			boolean first = true;
			for (Argument argument : arguments) {
				if (!first) {
					b.append(" ");
				} else {
					first = false;
				}
				b.append(convertToString(argument, clean));
			}
			if (!clean) {
				b.append('^');
			} else {
				b.append('}');
			}
			b.append(boundsToString(arg));
			return b.toString();
		} else if (arg instanceof Switch) {
			if (clean) {
				throw new IllegalArgumentException();
			}
			EList<Group> arguments = ((Switch) arg).getGroups();
			StringBuffer b = new StringBuffer();
			b.append("{");
			boolean first = true;
			for (Argument argument : arguments) {
				if (!first) {
					b.append("|");
				} else {
					first = false;
				}
				b.append(convertToString(argument, clean));
			}
			b.append("}");
			b.append(boundsToString(arg));
			return b.toString();
		}
		return arg.toString();
	}

	private static String boundsToString(Argument arg) {
		return "";// "[" + arg.getLowerBound() + "," + arg.getUpperBound() +
		// "]";
	}
}
