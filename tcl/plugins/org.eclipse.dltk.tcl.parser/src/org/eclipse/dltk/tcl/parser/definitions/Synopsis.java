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

public class Synopsis {

	private Command command;

	private String synopsis = "";

	static private DefinitionsFactory factory = DefinitionsFactory.eINSTANCE;

	public Synopsis(Command command) {
		if (command == null || command.getName() == null
				|| command.getName().equals("")) {
			throw new IllegalArgumentException();
		}
		this.command = command;
		synopsis = out(deploy());
	}

	private List<List<Argument>> deploy() {
		return deploy(copyArguments(command.getArguments()), 0);
	}

	private List<Argument> copyArguments(EList<Argument> arguments) {
		List<Argument> args = new ArrayList<Argument>();
		for (Argument argument : arguments) {
			args.add(DefinitionUtils.copyArgument(argument));
		}
		return args;
	}

	private List<List<Argument>> deploy(List<Argument> arguments, int pos) {
		List<List<Argument>> results = new ArrayList<List<Argument>>();
		if (pos >= arguments.size())
			return results;

		Argument argument = deploy(arguments.get(pos));

		if (argument.getUpperBound() == 0) {
			throw new IllegalArgumentException();
		}
		if (argument.getLowerBound() == -1) {
			throw new IllegalArgumentException();
		}
		if (argument.getUpperBound() != -1
				&& argument.getUpperBound() - argument.getLowerBound() < 0) {
			throw new IllegalArgumentException();
		}

		if (argument instanceof Switch) {
			if (argument.getUpperBound() == 1) {
				boolean isMode = true;
				for (Group group : ((Switch) argument).getGroups()) {
					if (group.getArguments().size() != 0) {
						isMode = false;
						break;
					}
				}
				// TODO
				List<Group> groups = ((Switch) argument).getGroups();
				if (groups.size() == 2) {
					if (groups.get(0).getArguments().size() == 1
							&& groups.get(1).getArguments().size() == 1) {
						Argument script = groups.get(0).getArguments().get(0);
						Argument complex = groups.get(1).getArguments().get(0);
						if (script instanceof TypedArgument
								&& ((TypedArgument) script).getType()
										.getValue() == ArgumentType.SCRIPT_VALUE
								&& complex instanceof TypedArgument
								&& ((TypedArgument) complex).getType()
										.getValue() == ArgumentType.ANY_VALUE
								&& complex.getUpperBound() == -1) {
							List<Argument> result = new ArrayList<Argument>();
							TypedArgument newScript = factory
									.createTypedArgument();
							newScript.setName(((TypedArgument) script)
									.getName());
							newScript.setLowerBound(0);
							newScript.setUpperBound(-1);
							if (argument.getLowerBound() == 1) {
								TypedArgument newScript2 = factory
										.createTypedArgument();
								newScript2.setName(((TypedArgument) script)
										.getName());
								result.add(newScript2);
							}
							result.add(newScript);
							results.add(result);
							// newScript
							// .setInfo(
							// "all the script arguments will be concatenated");
							return results;
						}
					}
				}
				if (isMode) {
					List<Argument> result = new ArrayList<Argument>();
					Constant mode = factory.createConstant();
					mode.setValue("mode");
					mode.setLowerBound(argument.getLowerBound());
					mode.setUpperBound(1);
					result.add(mode);
					results.add(result);
				} else {
					if (argument.getLowerBound() == 0) {
						results.add(new ArrayList<Argument>());
					}

					for (Group group : ((Switch) argument).getGroups()) {
						List<Argument> groupArgs = null;
						String constValue = ((Group) group).getConstant();
						if (constValue != null && !constValue.equals("")) {
							Constant constant = factory.createConstant();
							constant.setValue(constValue);
							groupArgs = new ArrayList<Argument>();
							groupArgs.add(constant);
							groupArgs.addAll(((Group) group).getArguments());
						} else {
							groupArgs = ((Group) group).getArguments();
						}
						results.addAll(deploy(groupArgs, 0));
					}
				}
			}
			// TODO 2 times or more
			else {
				List<Argument> result = new ArrayList<Argument>();

				List<List<Argument>> groupResults = new ArrayList<List<Argument>>();
				for (Group group : ((Switch) argument).getGroups()) {
					groupResults.addAll(deploy(((Group) deploy(group))
							.getArguments(), 0));
				}
				if (pos + 1 < arguments.size()) {
					Argument next = arguments.get(pos + 1);
					if (next instanceof Constant
							&& "--".equals(((Constant) next).getValue())) {
						Constant switches = factory.createConstant();
						switches.setValue("switches");
						StringBuilder infoValue = new StringBuilder();
						// infoValue
						// .append(out(groupResults))
						// .append("\n")
						// .append(Info.CommandSynopsis_EndOfSwitches_Info);
						//
						// switches.setInfo(Info.bind(
						// Info.CommandSynopsis_Switches_Info, "switches",
						// infoValue));

						switches.setLowerBound(0);
						switches.setUpperBound(-1);
						result.add(switches);
					}
				} else {
					for (List<Argument> groupResult : groupResults) {
						Group group = factory.createGroup();
						group.getArguments().addAll(groupResult);
						group.setLowerBound(0);
						group.setUpperBound(1);
						result.add(group);
					}
				}
				results.add(result);
			}
		} else {
			List<Argument> result = new ArrayList<Argument>();
			if (argument.getLowerBound() == 0) {
				if (argument.getUpperBound() == -1) {
					result.add(argument);
				} else if (argument.getUpperBound() == 1) {
					result.add(argument);
				} else {
					Argument copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(0);
					copy.setUpperBound(1);
					for (int i = 0; i < argument.getUpperBound(); i++) {
						result.add(copy);
					}
				}
			} else if (argument.getLowerBound() == 1) {
				if (argument.getUpperBound() == -1) {
					Argument copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(1);
					copy.setUpperBound(1);
					result.add(copy);
					copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(0);
					copy.setUpperBound(-1);
					result.add(copy);
				} else if (argument.getUpperBound() == 1) {
					result.add(argument);
				} else {
					Argument copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(1);
					copy.setUpperBound(1);
					for (int i = 0; i < argument.getUpperBound() - 1; i++)
						result.add(copy);
				}
			} else {
				Argument copy = DefinitionUtils.copyArgument(argument);
				copy.setLowerBound(1);
				copy.setUpperBound(1);
				for (int i = 0; i < argument.getLowerBound(); i++)
					result.add(copy);
				if (argument.getUpperBound() == -1) {
					copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(0);
					copy.setUpperBound(-1);
					result.add(copy);
				} else {
					copy = DefinitionUtils.copyArgument(argument);
					copy.setLowerBound(0);
					copy.setUpperBound(1);
					for (int i = argument.getLowerBound(); i < argument
							.getUpperBound(); i++)
						result.add(copy);
				}
			}
			results.add(result);
		}
		List<List<Argument>> results2 = deploy(arguments, pos + 1);
		if (results2 == null || results2.size() == 0)
			return results;
		List<List<Argument>> newResults = new ArrayList<List<Argument>>();
		for (List<Argument> result : results) {
			for (List<Argument> result2 : results2) {
				List<Argument> newResult = new ArrayList<Argument>();
				newResult.addAll(result);
				newResult.addAll(result2);
				newResults.add(newResult);
			}
		}
		return newResults;
	}

	private Argument deploy(Argument argument) {
		if (argument instanceof Constant) {
			return argument;
		} else if (argument instanceof TypedArgument) {
			// if (argument.getInfo() == null || "".equals(argument.getInfo()))
			// if (((TypedArgument) argument).getType().getValue() !=
			// ArgumentType.ANY_VALUE)
			// argument.setInfo(Info.bind(
			// Info.CommandSynopsis_TypedArgument_Info,
			// ((TypedArgument) argument).getName(),
			// ((TypedArgument) argument).getType()));
			return argument;
		} else if (argument instanceof Group) {
			List<Argument> arguments = null;
			String constValue = ((Group) argument).getConstant();
			if (constValue != null && !constValue.equals("")) {
				Constant constant = factory.createConstant();
				constant.setValue(constValue);
				arguments = new ArrayList<Argument>();
				arguments.add(constant);
				arguments.addAll(((Group) argument).getArguments());
			} else {
				arguments = ((Group) argument).getArguments();
			}

			List<List<Argument>> results = deploy(arguments, 0);
			if (results != null && results.size() == 1) {
				Group deployed = factory.createGroup();
				deployed.setLowerBound(argument.getLowerBound());
				deployed.setUpperBound(argument.getUpperBound());
				deployed.getArguments().addAll(results.get(0));
				return deployed;
			} else {
				Switch deployed = factory.createSwitch();
				deployed.setLowerBound(argument.getLowerBound());
				deployed.setUpperBound(argument.getUpperBound());
				for (List<Argument> result : results) {
					Group group = factory.createGroup();
					group.getArguments().addAll(result);
					deployed.getGroups().add(group);
				}
				return deployed;
			}
		} else if (argument instanceof ComplexArgument) {
			List<List<Argument>> results = deploy(((ComplexArgument) argument)
					.getArguments(), 0);
			if (results.size() == 1) {
				ComplexArgument deployed = factory.createComplexArgument();
				deployed.setLowerBound(argument.getLowerBound());
				deployed.setUpperBound(argument.getUpperBound());
				deployed.getArguments().addAll(results.get(0));
				return deployed;
			} else {
				Switch deployed = factory.createSwitch();
				deployed.setLowerBound(argument.getLowerBound());
				deployed.setUpperBound(argument.getUpperBound());
				for (List<Argument> result : results) {
					Group group = factory.createGroup();
					ComplexArgument complex = factory.createComplexArgument();
					complex.getArguments().addAll(result);
					group.getArguments().add(complex);
					deployed.getGroups().add(group);
				}
				return deployed;
			}
		}
		return argument;
	}

	public String toString() {
		return synopsis;
	}

	private String out(List<List<Argument>> deployed) {
		StringBuilder out = new StringBuilder();
		if (deployed == null || deployed.size() == 0) {
			out.append(command.getName());
		} else {
			boolean first = true;
			for (List<Argument> subCommand : deployed) {
				StringBuilder sub = out(subCommand);
				if (first)
					first = false;
				else
					out.append("\n");
				out.append(command.getName());
				if (!sub.toString().equals(""))
					out.append(" ").append(sub);
			}
		}
		return out.toString();
	}

	private StringBuilder out(List<Argument> subCommand) {
		StringBuilder out = new StringBuilder();
		boolean first = true;
		for (Argument argument : subCommand) {
			StringBuilder sub = out(argument);
			if (sub.toString().equals(""))
				continue;
			if (first)
				first = false;
			else
				out.append(" ");
			out.append(sub);
		}

		// for (Argument argument : subCommand) {
		// if (argument.getInfo() == null || !"".equals(argument.getInfo())) {
		// out.append("\n").append(argument.getInfo());
		// }
		// }

		return out;
	}

	private StringBuilder out(Argument argument) {
		StringBuilder out = new StringBuilder();
		if (argument instanceof Constant) {
			if (((Constant) argument).getValue().equals("--"))
				return out;
		}
		if (argument.getLowerBound() == 0) {
			out.append("?");
		}
		if (argument instanceof ComplexArgument) {
			out.append("{");
		}
		if (argument instanceof Constant) {
			out.append(((Constant) argument).getValue());
		} else if (argument instanceof TypedArgument) {
			out.append(((TypedArgument) argument).getName());
		} else if (argument instanceof Group) {
			out.append(out(((Group) argument).getArguments()));
		} else if (argument instanceof ComplexArgument) {
			out.append(out(((ComplexArgument) argument).getArguments()));
		}
		if (argument instanceof ComplexArgument) {
			out.append("}");
		}

		if (argument.getLowerBound() == 0) {
			if (argument.getUpperBound() == -1) {
				out.append(" ...?");
			} else if (argument.getUpperBound() == 1) {
				out.append("?");
			}
		}

		return out;
	}
}