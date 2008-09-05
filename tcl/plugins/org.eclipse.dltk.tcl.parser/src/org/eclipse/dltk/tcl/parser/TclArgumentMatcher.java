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
package org.eclipse.dltk.tcl.parser;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.ISubstitution;
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
import org.eclipse.dltk.tcl.parser.definitions.SynopsisBuilder;
import org.eclipse.emf.common.util.EList;

public class TclArgumentMatcher {
	public static final String SHORT_ARG = "short:";
	public static final String SYNOPSIS_ARG = "synopsis:";

	private List<Integer> codePositions;
	private TclErrorCollector errors;
	private TclCommand command;
	// private Map<String, Boolean> options;
	private List<ComplexArgumentResult> complexArguments;
	private ISubstitutionManager substitutionManager;
	private HashMap<Argument, int[]> mappings;
	private Command definition;

	private static interface ISinglePositionRule {
		boolean check(TclArgument argument, Argument definition,
				List<Integer> scriptPositions, int position,
				TclErrorCollector collector, List<ComplexArgumentResult> results);
	}

	public static class ComplexArgumentResult {
		private List<Integer> blockArguments = new ArrayList<Integer>();
		private List<TclArgument> arguments = new ArrayList<TclArgument>();
		private int argumentNumber = -1;
		private ComplexArgument definition;
		private List<ComplexArgumentResult> complexArguments = new ArrayList<ComplexArgumentResult>();

		public List<ComplexArgumentResult> getComplexArguments() {
			return complexArguments;
		}

		public void setComplexArguments(
				List<ComplexArgumentResult> complexArguments) {
			this.complexArguments = complexArguments;
		}

		public int[] getBlockArguments() {
			return getArrayFromList(this.blockArguments);
		}

		public List<Integer> getBlockArgumentsList() {
			return this.blockArguments;
		}

		public void setBlockArguments(List<Integer> blockArguments) {
			this.blockArguments = blockArguments;
		}

		public List<TclArgument> getArguments() {
			return arguments;
		}

		public void setArguments(List<TclArgument> arguments) {
			this.arguments = arguments;
		}

		public int getArgumentNumber() {
			return argumentNumber;
		}

		public void setArgumentNumber(int argumentNumber) {
			this.argumentNumber = argumentNumber;
		}

		public ComplexArgumentResult(int argumentNumber,
				List<TclArgument> arguments, List<Integer> blockArguments) {
			super();
			this.argumentNumber = argumentNumber;
			this.arguments = arguments;
			this.blockArguments = blockArguments;
		}

		public ComplexArgument getDefinition() {
			return definition;
		}

		public void setDefinition(ComplexArgument definition) {
			this.definition = definition;
		}
	}

	private static class MatchResult {
		private int argumentsUsed = 0;
		private TclErrorCollector errors = new TclErrorCollector();
		private boolean matched = false;
		private boolean matchWithErrors = false;
		private List<Integer> blockArguments = new ArrayList<Integer>();
		private List<ComplexArgumentResult> complexArguments = new ArrayList<ComplexArgumentResult>();
		// Map arguments to positions
		private Map<Argument, int[]> mapping = new HashMap<Argument, int[]>();

		public int getArgumentsUsed() {
			return this.argumentsUsed;
		}

		public void setArgumentsUsed(int argumentsUsed) {
			this.argumentsUsed = argumentsUsed;
		}

		public boolean isMatched() {
			return this.matched;
		}

		public void setMatched(boolean matched) {
			this.matched = matched;
		}

		public TclErrorCollector getErrors() {
			return this.errors;
		}

		public boolean isMatchWithErrors() {
			return this.matchWithErrors;
		}

		public void setMatchWithErrors(boolean matchWithErrors) {
			this.matchWithErrors = matchWithErrors;
		}

		public List<Integer> getBlockArguments() {
			return this.blockArguments;
		}

		public List<ComplexArgumentResult> getComplexArguments() {
			return this.complexArguments;
		}

		public Map<Argument, int[]> getMapping() {
			return this.mapping;
		}
	}

	public TclArgumentMatcher(TclCommand command, Map<String, Boolean> map,
			ISubstitutionManager substitutionManager) {
		this.command = command;
		// this.options = map;
		this.substitutionManager = substitutionManager;
	}

	public boolean match(Command definition) {
		// Initialize required fields
		this.codePositions = new ArrayList<Integer>();
		this.errors = new TclErrorCollector();
		this.complexArguments = new ArrayList<ComplexArgumentResult>();
		this.mappings = new HashMap<Argument, int[]>();
		this.definition = definition;
		List<Argument> definitionArguments = definition.getArguments();

		EList<TclArgument> arguments = command.getArguments();
		if (arguments.size() == 0 && definitionArguments.size() == 0) {
			return true;
		}
		MatchResult result = matchArgument(arguments, 0, definitionArguments, 0);
		this.errors.addAll(result.getErrors());
		if (result.isMatched()) {
			if (result.getArgumentsUsed() == arguments.size()) {
				this.codePositions.addAll(result.getBlockArguments());
				this.complexArguments.addAll(result.getComplexArguments());
				this.mappings.putAll(result.getMapping());
				return true;
			} else {
				if (result.getArgumentsUsed() < arguments.size()) {
					reportExtraArguments(arguments, result.getArgumentsUsed(),
							this.errors);
					return true;
				}
			}
		}
		if (this.errors.getCount() == 0) {
			reportInvalidArgumentCount(this.command.getStart(), this.command
					.getEnd(), arguments.size(), this.errors, definition);
		}

		return false;
	}

	private class SubstitutedArgumentValue {
		String value;
		int offset = 0;
	}

	private SubstitutedArgumentValue getSubstitutedArgumentValue(TclArgument arg) {
		if (arg instanceof StringArgument) {
			String value = ((StringArgument) arg).getValue();
			if (value.startsWith("{") && value.endsWith("}")) {
				SubstitutedArgumentValue val = new SubstitutedArgumentValue();
				val.value = value.substring(1, value.length() - 1);
				val.offset = 1;
				return val;
			} else if (value.startsWith("\"") && value.endsWith("\"")) {
				SubstitutedArgumentValue val = new SubstitutedArgumentValue();
				val.value = value.substring(1, value.length() - 1);
				val.offset = 1;
				return val;
			}
			SubstitutedArgumentValue val = new SubstitutedArgumentValue();
			val.value = value;
			val.offset = 0;
			return val;
		} else if (arg instanceof ISubstitution) {
			if (substitutionManager != null) {
				SubstitutedArgumentValue val = new SubstitutedArgumentValue();
				val.value = substitutionManager.substitute((ISubstitution) arg);
				val.offset = 0;
				return val;
			}
		}
		SubstitutedArgumentValue val = new SubstitutedArgumentValue();
		val.value = null;
		val.offset = 0;
		return val;
	}

	/**
	 * Report all errors from previous match.
	 * 
	 * @param reporter
	 */
	public void reportErrors(ITclErrorReporter reporter) {
		if (this.errors != null && reporter != null) {
			this.errors.reportAll(reporter);
		}
	}

	/**
	 * Return code block argument indexes to parse.
	 */
	public int[] getBlockArguments() {
		return getArrayFromList(this.codePositions);
	}

	private static int[] getArrayFromList(List<Integer> list) {
		int[] positions = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			positions[i] = list.get(i).intValue();
		}
		return positions;
	}

	public ComplexArgumentResult[] getComplexArguments() {
		return this.complexArguments
				.toArray(new ComplexArgumentResult[this.complexArguments.size()]);
	}

	// Chooses the best one match.
	private MatchResult matchArgument(List<TclArgument> arguments, int pos,
			List<Argument> definitionArguments, int defPos) {
		List<MatchResult> results = matchArgumentList(arguments, pos,
				definitionArguments, defPos);
		MatchResult result = null;
		for (MatchResult sr : results) {
			if (result == null) {
				result = sr;
			} else if (result.getErrors().getCount() > sr.getErrors()
					.getCount()
					&& result.getArgumentsUsed() > sr.getArgumentsUsed()) {
				result = sr;
			} else if (result.getErrors().getCount() >= sr.getErrors()
					.getCount()
					&& result.getArgumentsUsed() < sr.getArgumentsUsed()) {
				result = sr;
			} /*
			 * else if (result.getErrors().getCount() <= sr.getErrors()
			 * .getCount() && result.getArgumentsUsed() <= sr
			 * .getArgumentsUsed()) { result = sr; }
			 */
		}
		return result;
	}

	private List<MatchResult> matchArgumentList(List<TclArgument> arguments,
			int pos, List<Argument> definitionArguments, int defPos) {
		List<MatchResult> results = new ArrayList<MatchResult>();
		if (definitionArguments.size() == defPos) {
			MatchResult result = new MatchResult();
			result.setMatched(true);
			result.setMatchWithErrors(false);
			result.setArgumentsUsed(0);
			results.add(result);
			return results; // End of match
		}
		Argument definitionArg = definitionArguments.get(defPos);

		List<MatchResult> list = matchDefinition(arguments, pos, definitionArg);
		TclErrorCollector collector = new TclErrorCollector();
		for (int i = 0; i < list.size(); i++) {
			MatchResult r = list.get(i);
			if (r.isMatched()) {
				List<MatchResult> srl = matchArgumentList(arguments, pos
						+ r.getArgumentsUsed(), definitionArguments, defPos + 1);
				for (MatchResult sr : srl) {
					if (sr.isMatched()) {
						sr.setMatchWithErrors(sr.isMatchWithErrors()
								|| r.isMatchWithErrors());
						sr.setArgumentsUsed(sr.getArgumentsUsed()
								+ r.getArgumentsUsed());
						sr.getErrors().addAll(r.getErrors());
						sr.getBlockArguments().addAll(r.getBlockArguments());
						sr.getMapping().putAll(r.getMapping());
						sr.getComplexArguments()
								.addAll(r.getComplexArguments());
						results.add(sr);
					}
				}

			} else {
				collector.addAll(r.getErrors());
			}
		}
		if (results.size() == 0) {
			MatchResult result = new MatchResult();
			result.setMatched(false);
			result.setMatchWithErrors(false);
			result.getErrors().addAll(collector);
			results.add(result);
		}
		return results;
	}

	// private Argument findNextRequiredArgument(
	// List<Argument> definitionArguments, int defPos) {
	// for (int i = defPos + 1; i < definitionArguments.size(); i++) {
	// Argument a = definitionArguments.get(i);
	// if (a.getLowerBound() > 0) {
	// return a;
	// }
	// }
	// return definitionArguments.get(defPos);
	// }

	private List<MatchResult> matchDefinition(List<TclArgument> arguments,
			int pos, Argument definition) {
		List<MatchResult> results = new ArrayList<MatchResult>();
		if (definition instanceof Constant) {
			matchConstant(arguments, pos, definition, results);
		} else if (definition instanceof TypedArgument) {
			matchTypedArgument(arguments, pos, definition, results);
		} else if (definition instanceof Group) {
			matchGroupArgument(arguments, pos, definition, results);
		} else if (definition instanceof Switch) {
			matchSwitchArgument(arguments, pos, definition, results);
		} else if (definition instanceof ComplexArgument) {
			matchComplexArgument(arguments, pos, definition, results);
		}
		return results;
	}

	private void matchComplexArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {

		matchSinglePositionArgument(results, arguments, pos, definition,
				new ISinglePositionRule() {
					public boolean check(TclArgument argument,
							Argument definition, List<Integer> scriptPositions,
							int position, TclErrorCollector collector,
							List<ComplexArgumentResult> results) {

						ComplexArgument complexArgument = (ComplexArgument) definition;
						SubstitutedArgumentValue substring = getSubstitutedArgumentValue(argument);
						if (substring.value == null) {
							return true;
						}

						List<TclArgument> subArguments = TclParserUtils
								.parseCommandArguments(argument.getStart()
										+ substring.offset, substring.value);
						EList<Argument> definitionArguments = complexArgument
								.getArguments();
						MatchResult res = matchArgument(subArguments, 0,
								definitionArguments, 0);
						if (res.isMatched()) {
							// if (res.getArgumentsUsed() ==
							// subArguments.size()) {
							ComplexArgumentResult cResult = new ComplexArgumentResult(
									position, subArguments, res
											.getBlockArguments());
							cResult.getComplexArguments().addAll(
									res.getComplexArguments());
							collector.reportAll(res.getErrors());
							cResult.setDefinition(complexArgument);
							results.add(cResult);
							if (res.getArgumentsUsed() < subArguments.size()) {
								reportExtraArguments(subArguments, res
										.getArgumentsUsed(), collector);
							} /*
							 * else {
							 * reportInvalidArgumentCount(argument.getStart(),
							 * argument.getEnd(), res .getArgumentsUsed(),
							 * collector); }
							 */
							return true;
							// } else {
							// }
						}
						return false;
					}

				}, false);
	}

	private void matchSwitchArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		Switch switchDef = (Switch) definition;
		EList<Group> groups = switchDef.getGroups();
		int lowerBound = switchDef.getLowerBound();
		int upperBound = switchDef.getUpperBound();
		if (upperBound == -1) {
			upperBound = Integer.MAX_VALUE;
		}
		List<MatchResult> ress = new ArrayList<MatchResult>();
		Map<MatchResult, Integer> counts = new HashMap<MatchResult, Integer>();

		matchSwitch(arguments, pos, groups, switchDef.isCheckPrefix(), ress,
				counts, 0, upperBound);

		for (int i = 0; i < ress.size(); i++) {
			MatchResult r = ress.get(i);
			Integer count = counts.get(r);
			if (count != null && count.intValue() < lowerBound) {
				r.setMatchWithErrors(true);
				reportMissingArgument(definition, r, this.command.getStart(),
						command.getEnd());
				results.add(r);
			}
			if (count != null && count.intValue() >= lowerBound
					&& count.intValue() <= upperBound) {
				results.add(r);
			}
			// if (count != null && count.intValue() > upperBound) {
			// r.setMatchWithErrors(true);
			// reportInvalidArgumentCount(definition, r);
			// results.add(r);
			// }
		}
		// Add empty variant if multiplicity support it.
		if (ress.size() == 0 && lowerBound > 0) {
			// We should report error if not argument are specified, but
			// required.
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(false);
			r.setMatchWithErrors(false);
			reportMissingSwitchArgument(groups, r);
			results.add(r);
		} else if (lowerBound == 0) {
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(true);
			r.setMatchWithErrors(false);
			results.add(r);
		}
	}

	private String collectGroupConstants(List<Group> groups) {
		StringBuffer args = new StringBuffer();
		for (int i = 0; i < groups.size(); i++) {
			String cons = groups.get(i).getConstant();
			if (cons != null && cons.length() > 0) {
				if (args.length() == 0) {
					args.append(cons);
				} else {
					args.append("," + cons);
				}
			}
		}
		return args.toString();
	}

	public boolean matchGroupPrefix(List<TclArgument> arguments, int pos,
			List<Group> groups, List<Group> matchedGroups) {
		if (pos >= arguments.size()) {
			matchedGroups.addAll(groups);
			return false;
		}
		TclArgument argument = arguments.get(pos);
		if (argument instanceof Substitution) {
			matchedGroups.addAll(groups);
			return true;
		} else if (argument instanceof VariableReference) {
			matchedGroups.addAll(groups);
			return true;
		} else if (argument instanceof ComplexString) {
			matchedGroups.addAll(groups);
			return true;
		} else if (argument instanceof StringArgument) {
			List<Group> noPrefixGroups = new ArrayList<Group>();
			List<Group> notMatchedGroups = new ArrayList<Group>();
			String value = ((StringArgument) argument).getValue();
			for (Group group : groups) {
				if (group.getConstant() != null
						&& group.getConstant().length() != 0) {
					matchedGroups.add(group);
					if (group.getConstant().equals(value)) {
						matchedGroups.clear();
						matchedGroups.add(group);
						return true;
					}
				} else {
					noPrefixGroups.add(group);
				}
			}
			for (int i = 0; i < value.length(); i++) {
				for (Group group : matchedGroups) {
					String constant = group.getConstant();
					if (constant.length() <= i
							|| value.charAt(i) != constant.charAt(i)) {
						notMatchedGroups.add(group);
					}
				}
				if (notMatchedGroups.size() != 0) {
					matchedGroups.removeAll(notMatchedGroups);
					notMatchedGroups.clear();
				}
			}
			if (matchedGroups.size() < 1) {
				matchedGroups.addAll(noPrefixGroups);
				return true;
			} else if (matchedGroups.size() > 1) {
				return false;
			} else {
				((StringArgument) argument).setValue(matchedGroups.get(0)
						.getConstant());
				return true;
			}
		}
		return false;
	}

	private void matchSwitch(List<TclArgument> arguments, int pos,
			List<Group> groups, boolean checkPrefix, List<MatchResult> ress,
			Map<MatchResult, Integer> counts, int count, int upperBound) {
		TclErrorCollector errors = new TclErrorCollector();
		if (pos >= arguments.size()) {
			return;
		}
		List<MatchResult> list = new ArrayList<MatchResult>();
		List<Group> matchedGroups = new ArrayList<Group>();

		boolean matched = true;
		if (checkPrefix) {
			matched = matchGroupPrefix(arguments, pos, groups, matchedGroups);
			if (matched)
				for (int i = 0; i < matchedGroups.size(); i++) {
					matchGroupArgument(arguments, pos, matchedGroups.get(i),
							list);
				}
		} else {
			for (int i = 0; i < groups.size(); i++) {
				matchGroupArgument(arguments, pos, groups.get(i), list);
			}
		}

		for (int j = 0; j < list.size(); j++) {
			MatchResult r = list.get(j);
			if (r.isMatched() && !r.isMatchWithErrors()) {
				List<MatchResult> ress2 = new ArrayList<MatchResult>();
				counts.put(r, new Integer(count + 1));
				ress.add(r);
				if (count + 1 == upperBound) {
					continue;
				}
				if (r.getArgumentsUsed() > 0) {
					matchSwitch(arguments, pos + r.getArgumentsUsed(), groups,
							checkPrefix, ress2, counts, count + 1, upperBound);
					for (int k = 0; k < ress2.size(); k++) {
						MatchResult r2 = ress2.get(k);
						if (r2.isMatched() && !r2.isMatchWithErrors()) {
							r2.setArgumentsUsed(r.getArgumentsUsed()
									+ r2.getArgumentsUsed());
							r2.getBlockArguments()
									.addAll(r.getBlockArguments());
							r2.getErrors().addAll(r.getErrors());
							r2.getMapping().putAll(r.getMapping());
							r2.getComplexArguments().addAll(
									r.getComplexArguments());
							ress.add(r2);
						}
					}
				}
			} else {
				errors.addAll(r.getErrors());
			}
		}
	}

	private void matchGroupArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		Group group = (Group) definition;
		int lowerBound = group.getLowerBound();
		int upperBound = group.getUpperBound();
		if (upperBound == -1) {
			upperBound = Integer.MAX_VALUE;
		}
		List<Argument> groupArguments = group.getArguments();
		if (group.getConstant() != null && group.getConstant().length() != 0) {
			List<Argument> newArgs = new ArrayList<Argument>();
			Constant constant = DefinitionsFactory.eINSTANCE.createConstant();
			constant.setValue(group.getConstant());
			constant.setLowerBound(1);
			constant.setUpperBound(1);
			constant.setStrictMatch(true);
			newArgs.add(constant);
			newArgs.addAll(group.getArguments());
			groupArguments = newArgs;
		}
		List<MatchResult> ress = new ArrayList<MatchResult>();
		Map<MatchResult, Integer> counts = new HashMap<MatchResult, Integer>();
		matchGroup(arguments, pos, groupArguments, ress, counts, 0, upperBound);
		for (int i = 0; i < ress.size(); i++) {
			MatchResult r = ress.get(i);
			Integer count = counts.get(r);
			if (count != null && count.intValue() < lowerBound) {
				r.setMatchWithErrors(true);
				reportMissingArgument(definition, r, this.command.getStart(),
						command.getEnd());
				results.add(r);
			}
			if (count != null && count.intValue() >= lowerBound
					&& count.intValue() <= upperBound) {
				results.add(r);
			}
			// if (count != null && count.intValue() > upperBound) {
			// r.setMatchWithErrors(true);
			// reportInvalidArgumentCount(definition, r);
			// results.add(r);
			// }
		}
		if (ress.size() == 0 && lowerBound > 0) {
			// We should report error if not argument are specified, but
			// required.
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(false);
			r.setMatchWithErrors(false);
			reportGroupConstantMissing(group, r);
			results.add(r);
		} else if (/* ress.size() == 0 && */lowerBound == 0) {
			// Add empty variant if multiplicity support it.
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(true);
			r.setMatchWithErrors(false);
			results.add(r);
		}
	}

	private void matchGroup(List<TclArgument> arguments, int pos,
			List<Argument> groupArguments, List<MatchResult> ress,
			Map<MatchResult, Integer> counts, int count, int upperBound) {
		TclErrorCollector errors = new TclErrorCollector();
		if (pos >= arguments.size()) {
			return;
		}
		// TODO: Multiple results is very slow. Why?
		// List<MatchResult> results = matchArgumentList(arguments, pos,
		// groupArguments, 0);
		//
		// for (MatchResult r : results) {
		for (int i = 0; i < 1; ++i) {
			MatchResult r = matchArgument(arguments, pos, groupArguments, 0);
			if (r.isMatched()) {
				List<MatchResult> ress2 = new ArrayList<MatchResult>();
				counts.put(r, new Integer(count + 1));
				ress.add(r);
				if (count + 1 == upperBound) {
					continue;
				}
				if (r.getArgumentsUsed() > 0) {
					matchGroup(arguments, pos + r.getArgumentsUsed(),
							groupArguments, ress2, counts, count + 1,
							upperBound);
					for (int k = 0; k < ress2.size(); k++) {
						MatchResult r2 = ress2.get(k);
						if (r2.isMatched() && !r2.isMatchWithErrors()) {
							r2.setArgumentsUsed(r.getArgumentsUsed()
									+ r2.getArgumentsUsed());
							r2.getBlockArguments()
									.addAll(r.getBlockArguments());
							r2.getErrors().addAll(r.getErrors());
							r2.getMapping().putAll(r.getMapping());
							r2.getComplexArguments().addAll(
									r.getComplexArguments());
							ress.add(r2);
						}
					}
				}
			} else {
				errors.addAll(r.getErrors());
			}
		}
	}

	private void matchConstant(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		final Constant constDefinition = (Constant) definition;
		final String value = constDefinition.getValue();
		matchSinglePositionArgument(results, arguments, pos, definition,
				new ISinglePositionRule() {
					public boolean check(TclArgument argument,
							Argument definition, List<Integer> scriptPositions,
							int position, TclErrorCollector collector,
							List<ComplexArgumentResult> complex) {
						SubstitutedArgumentValue argumentValue = getSubstitutedArgumentValue(argument);
						// TODO: Add option to strictly or not check constants.
						if (value.equals(argumentValue.value)) {
							return true;
						} else if (!constDefinition.isStrictMatch()
								&& argumentValue.value == null) {
							return true;
						}
						return false;
					}

				}, true);
	}

	private void matchTypedArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		TypedArgument arg = (TypedArgument) definition;
		final ArgumentType type = arg.getType();
		matchSinglePositionArgument(results, arguments, pos, definition,
				new ISinglePositionRule() {
					public boolean check(TclArgument argument,
							Argument definition, List<Integer> scriptPositions,
							int position, TclErrorCollector collector,
							List<ComplexArgumentResult> complex) {
						if (checkType(argument, type, scriptPositions,
								position, collector)) {
							return true;
						}
						return false;
					}

				}, false);
	}

	protected boolean checkType(TclArgument argument, ArgumentType type,
			List<Integer> scriptPositions, int position,
			TclErrorCollector collector) {
		boolean result = false;
		boolean reported = false;
		SubstitutedArgumentValue arg = getSubstitutedArgumentValue(argument);
		String value = arg.value;
		if (value == null) {
			// This is not resolved substitution.
			// Pass as checked true.
			return true;
		}
		switch (type.getValue()) {
		case ArgumentType.SCRIPT_VALUE: {
			scriptPositions.add(new Integer(position));
			result = true;
			break;
		}
		case ArgumentType.INTEGER_VALUE: {
			try {
				if (value.startsWith("+")) {
					value = value.substring(1);
				}
				Integer.parseInt(value);
				result = true;
			} catch (NumberFormatException e) {
				result = false;
				// reported = true;
				// reportInvalidArgumentValue(argument, type, collector, e
				// .getMessage());
			}
			break;
		}
		case ArgumentType.NOT_NEGATIVE_VALUE: {
			try {
				int i = Integer.parseInt(value);
				result = i >= 0;
				if (!result) {
					reported = true;
					reportInvalidArgumentValue(
							argument,
							type,
							collector,
							Messages.TclArgumentMatcher_Error_Number_is_negative);
				}
			} catch (NumberFormatException e) {
				result = false;
			}
			break;
		}
		case ArgumentType.INDEX_VALUE: {
			result = false;
			try {
				int i = Integer.parseInt(value);
				result = i >= 0;
			} catch (NumberFormatException e) {
			}
			if (!result) {
				if (value.startsWith("end")) {
					String sub = value.substring(3);
					if (sub.length() == 0) {
						result = true;
					} else {
						char c = sub.charAt(0);
						if (c == '+' || c == '-') {
							String sub2 = sub.substring(1);
							try {
								int i = Integer.parseInt(sub2);
								result = i >= 0;
							} catch (NumberFormatException e) {
							}
						}
					}
				} else {
					int pos = value.indexOf('+');
					if (pos == -1) {
						pos = value.indexOf('-');
					}
					if (pos != -1) {
						String p1 = value.substring(0, pos);
						String p2 = value.substring(pos + 1);
						try {
							int i = Integer.parseInt(p1);
							result = i > 0;
						} catch (NumberFormatException e) {
							result = false;
						}
						if (result) {
							try {
								int i = Integer.parseInt(p2);
								result = i >= 0;
							} catch (NumberFormatException e) {
								result = false;
							}
						}
					}
				}
			}
			break;
		}
		default:
			result = true;
		}
		// Report generic error message
		if (!result && !reported) {
			reportInvalidArgumentValue(argument, type, collector, null);
		}

		return result;
	}

	private void matchSinglePositionArgument(List<MatchResult> results,
			List<TclArgument> arguments, int pos, Argument definitionArg,
			ISinglePositionRule rule, boolean returnMaxMatchedResult) {
		int lowBound = definitionArg.getLowerBound();
		int upperBound = definitionArg.getUpperBound();
		if (upperBound == -1) {
			upperBound = Integer.MAX_VALUE;
		}
		int count = 0;
		List<Integer> scriptPositions = new ArrayList<Integer>();
		List<ComplexArgumentResult> complexArguments = new ArrayList<ComplexArgumentResult>();
		// We need to check for low
		Map<Integer, TclErrorCollector> collectors = new HashMap<Integer, TclErrorCollector>();
		for (int i = 0; i < arguments.size() - pos; i++) {
			TclErrorCollector collector = new TclErrorCollector();
			collectors.put(new Integer(i), collector);
			TclArgument a = arguments.get(pos + i);
			if (rule.check(a, definitionArg, scriptPositions, pos + i,
					collector, complexArguments)) {
				count++;
			} else {
				break;
			}
			// Do not, check extra arguments
			if (count == upperBound) {
				break;
			}
		}
		if (count < lowBound) {
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(count);
			r.setMatched(count > 0);
			r.setMatchWithErrors(true);
			int start = this.command.getStart();
			int end = this.command.getEnd();
			if (arguments.size() > pos) {
				TclArgument arg = arguments.get(pos);
				start = arg.getStart();
				end = arg.getEnd();
			}
			reportMissingArgument(definitionArg, r, start, end);
			// Add argument errors
			for (Integer integer : collectors.keySet()) {
				if (integer.intValue() <= count) {
					r.getErrors().addAll(collectors.get(integer));
				}
			}
			r.getMapping().put(definitionArg, new int[] { pos, pos + count });
			results.add(r);
		}
		if (count >= lowBound) {
			int up = count;
			if (up > upperBound) {
				up = upperBound;
			}
			int from = lowBound;
			if (returnMaxMatchedResult) {
				from = count;
			}
			for (int i = from; i <= up; i++) {
				MatchResult r = new MatchResult();
				r.setArgumentsUsed(i);
				r.setMatched(true);
				r.setMatchWithErrors(false);
				for (Integer integer : scriptPositions) {
					if (integer.intValue() < pos + i) {
						r.getBlockArguments().add(integer);
					}
				}
				for (ComplexArgumentResult arg : complexArguments) {
					if (arg.getArgumentNumber() < pos + i) {
						r.getComplexArguments().add(arg);
					}
				}
				// Add argument errors
				for (Integer integer : collectors.keySet()) {
					if (integer.intValue() <= i) {
						r.getErrors().addAll(collectors.get(integer));
					}
				}
				r.getMapping().put(definitionArg, new int[] { pos, pos + i });
				results.add(r);
			}
		}
		// if (count > upperBound) {
		// for (int i = upperBound + 1; i <= count; i++) {
		// MatchResult r = new MatchResult();
		// r.setArgumentsUsed(i);
		// r.setMatched(true);
		// r.setMatchWithErrors(true);
		// TclArgument begin = arguments.get(pos + upperBound);
		// TclArgument end = begin;
		// if (pos + count < arguments.size()) {
		// end = arguments.get(pos + count);
		// } else {
		// end = arguments.get(arguments.size() - 1);
		// }
		// r.getErrors().report(
		// ITclErrorReporter.INVALID_ARGUMENT_COUNT,
		// "To many arguments:"
		// + definitionToString(definitionArg),
		// begin.getEnd(), end.getEnd(), ITclErrorReporter.ERROR);
		// for (Integer integer : scriptPositions) {
		// if (integer.intValue() < pos + i) {
		// r.getBlockArguments().add(integer);
		// }
		// }
		// for (ComplexArgumentResult arg : complexArguments) {
		// if (arg.getArgumentNumber() < pos + i) {
		// r.getComplexArguments().add(arg);
		// }
		// }
		// // Add argument errors
		// for (Integer integer : collectors.keySet()) {
		// if (integer.intValue() <= i) {
		// r.getErrors().addAll(collectors.get(integer));
		// }
		// }
		// r.getMapping().put(definitionArg, new int[] { pos, pos + i });
		// results.add(r);
		// }
		// }
	}

	private String definitionToString(Argument definitionArg) {
		if (definitionArg instanceof Constant) {
			return ((Constant) definitionArg).getValue();
		} else if (definitionArg instanceof TypedArgument) {
			return ((TypedArgument) definitionArg).getName();
		} else if (definitionArg instanceof Group) {
			Group g = (Group) definitionArg;
			if (g.getConstant() != null) {
				return g.getConstant();
			}
		}
		return "";
	}

	public TclErrorCollector getErrorReporter() {
		return this.errors;
	}

	private String getSynopsis() {
		SynopsisBuilder synopsis = new SynopsisBuilder(this.definition);
		String synText = synopsis.toString();

		if (synText.length() > 0) {
			return synText;
		}
		return "";
	}

	//
	// private String getShortSynopsis() {
	// SynopsisBuilder synopsis = new SynopsisBuilder(this.command);
	// String synText = synopsis.toString();
	// if (synText.length() > 0) {
	// return synText;
	// }
	// return "";
	// }

	private String[] getExtraArgs() {
		return new String[] { IProblem.DESCRIPTION_ARGUMENT_PREFIX
				+ "Synapsis:\n" + getSynopsis() };
		// return null;// new String[] { SYNOPSIS_ARG + getSynopsis(),
		// SHORT_ARG + getShortSynopsis() };
	}

	// Error reporting
	private void reportInvalidArgumentCount(int start, int end, int count,
			TclErrorCollector collector, Command definition) {
		String message = Messages.TclArgumentMatcher_Invlid_Arguments;
		collector.report(ITclErrorReporter.INVALID_ARGUMENT_COUNT, message,
				getExtraArgs(), start, end, ITclErrorConstants.ERROR);
	}

	// private void reportInvalidComplexArgumentValue(TclArgument argument,
	// TclErrorCollector collector) {
	// String message = Messages.TclArgumentMatcher_Block_Argument_Expected;
	// collector
	// .report(ITclErrorReporter.INVALID_ARGUMENT_VALUE, message,
	// argument.getStart(), argument.getEnd(),
	// ITclErrorReporter.ERROR);
	// }

	private void reportInvalidArgumentValue(TclArgument argument,
			ArgumentType type, TclErrorCollector collector, String additional) {
		if (additional != null) {
			collector
					.report(
							ITclErrorReporter.INVALID_ARGUMENT_VALUE,
							MessageFormat
									.format(
											Messages.TclArgumentMatcher_Argument_Of_Type_ExpectedDetail,
											new Object[] { type.getName(),
													argumentToString(argument),
													additional }),
							getExtraArgs(), argument.getStart(), argument
									.getEnd(), ITclErrorReporter.ERROR);
		} else {
			collector
					.report(
							ITclErrorReporter.INVALID_ARGUMENT_VALUE,
							MessageFormat
									.format(
											Messages.TclArgumentMatcher_Argument_Of_Type_Expected,
											new Object[] { type.getName(),
													argumentToString(argument) }),
							getExtraArgs(), argument.getStart(), argument
									.getEnd(), ITclErrorReporter.ERROR);
		}
	}

	private void reportExtraArguments(List<TclArgument> list,
			int argumentsUsed, TclErrorCollector collector) {
		if (list.size() > argumentsUsed) {
			TclArgument begin = list.get(argumentsUsed);
			TclArgument end = list.get(list.size() - 1);
			String message = MessageFormat.format(
					Messages.TclArgumentMatcher_Extra_Arguments,
					new Object[] { new Integer(list.size() - argumentsUsed) });
			collector.report(ITclErrorConstants.EXTRA_ARGUMENTS, message,
					getExtraArgs(), begin.getStart(), end.getEnd(),
					ITclErrorConstants.WARNING);
		}
	}

	private void reportMissingArgument(Argument definitionArg, MatchResult r,
			int start, int end) {
		String value = MessageFormat.format(
				Messages.TclArgumentMatcher_Missing_Argument,
				new Object[] { definitionToString(definitionArg) });
		r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, value,
				getExtraArgs(), start, end, ITclErrorReporter.ERROR);
	}

	private String argumentToString(TclArgument argument) {
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

	private void reportGroupConstantMissing(Group group, MatchResult r) {
		String message = MessageFormat.format(
				Messages.TclArgumentMatcher_Missing_Group_Argument,
				new Object[] { group.getConstant() });
		r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, message,
				getExtraArgs(), this.command.getStart(), this.command.getEnd(),
				ITclErrorReporter.ERROR);
	}

	private void reportMissingSwitchArgument(EList<Group> groups, MatchResult r) {
		String message = MessageFormat.format(
				Messages.TclArgumentMatcher_Missing_Switch_Argument,
				new Object[] { collectGroupConstants(groups) });
		r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, message,
				getExtraArgs(), this.command.getStart(), this.command.getEnd(),
				ITclErrorReporter.ERROR);
	}

	public Map<Argument, int[]> getMappings() {
		return new HashMap<Argument, int[]>(this.mappings);
	}
}
