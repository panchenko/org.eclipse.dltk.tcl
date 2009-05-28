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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.tcl.ast.ISubstitution;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.ArgumentType;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.definitions.ComplexArgument;
import org.eclipse.dltk.tcl.definitions.Constant;
import org.eclipse.dltk.tcl.definitions.Group;
import org.eclipse.dltk.tcl.definitions.Switch;
import org.eclipse.dltk.tcl.definitions.TypedArgument;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionUtils;
import org.eclipse.dltk.tcl.parser.definitions.SynopsisBuilder;
import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

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
	// private Command definition;
	private SynopsisBuilder synopsisBuilder = new SynopsisBuilder(false);

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
		public static final int POSSIBLE = -1;
		public static final int REGULAR = 0;
		public static final int IMPLICIT = 1;

		private int argumentsUsed = 0;
		private TclErrorCollector errors = new TclErrorCollector();
		private boolean matched = false;
		private boolean matchWithErrors = false;
		private int priority = REGULAR;
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

		public boolean isImplicit() {
			return priority >= IMPLICIT;
		}

		public int getPriority() {
			return priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		public void setSummaryPriorityOf(MatchResult r1, MatchResult r2) {
			this.priority = r1.getPriority() + r2.getPriority();
		}

		public void incrPriority() {
			this.priority++;
		}

		public void decrPriority() {
			this.priority--;
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
		// this.definition = definition;
		List<Argument> definitionArguments = definition.getArguments();

		EList<TclArgument> arguments = command.getArguments();
		int argSize = arguments.size();
		if (argSize == 0 && definitionArguments.size() == 0) {
			return true;
		}
		MatchResult result = matchArgument(arguments, 0, definitionArguments, 0);
		this.errors.addAll(result.getErrors());
		if (result.isMatched()) {
			this.codePositions.addAll(result.getBlockArguments());
			this.complexArguments.addAll(result.getComplexArguments());
			this.mappings.putAll(result.getMapping());
			if (result.getArgumentsUsed() == argSize) {
				return true;
			} else {
				if (result.getArgumentsUsed() < argSize) {
					reportExtraArguments(arguments, result.getArgumentsUsed(),
							this.errors);
					return true;
				}
			}
		}
		if (this.errors.getCount() == 0) {
			reportInvalidArgumentCount(this.command.getStart(), this.command
					.getEnd(), argSize, this.errors, definition);
		}
		return false;
	}

	private static class SubstitutedArgumentValue {
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
		int lsize = list.size();
		int[] positions = new int[lsize];
		for (int i = 0; i < lsize; i++) {
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
			} else {
				int rpriority = result.getPriority();
				int srPriority = sr.getPriority();
				if (rpriority > srPriority) {
					continue;
				} else if (rpriority < srPriority) {
					result = sr;
				} else if (!result.isMatched() && sr.isMatched()) {
					result = sr;
				} else {
					TclErrorCollector rErrors = result.getErrors();
					TclErrorCollector srErrors = sr.getErrors();
					int rArgsUsed = result.getArgumentsUsed();
					if (rErrors.getCount() > srErrors.getCount()
							&& rArgsUsed >= sr.getArgumentsUsed()) {
						result = sr;
					} else if (rErrors.getCount() >= srErrors.getCount()
							&& rArgsUsed < sr.getArgumentsUsed()) {
						result = sr;
					}
				}
			} /*
			 * else if (result.getErrors().getCount() <= sr.getErrors()
			 * .getCount() && result.getArgumentsUsed() <= sr
			 * .getArgumentsUsed()) { result = sr; }
			 */
		}
		return result;
	}

	private List<MatchResult> matchArgumentList(List<TclArgument> arguments,
			int pos, List<Argument> definition, int defPos) {
		List<MatchResult> results = new ArrayList<MatchResult>();
		if (definition.size() == defPos) {
			MatchResult result = new MatchResult();
			result.setMatched(true);
			result.setMatchWithErrors(false);
			result.setArgumentsUsed(0);
			results.add(result);
			return results; // End of match
		}
		Argument definitionArg = definition.get(defPos);

		List<MatchResult> list = matchDefinition(arguments, pos, definitionArg);

		int lsize = list.size();
		if (lsize == 0
				|| (lsize == 1 && !list.get(0).isMatched() && !list.get(0)
						.isImplicit())) {
			if (arguments.size() > pos + 1) {
				List<TclArgument> extraArgs = new ArrayList<TclArgument>();
				extraArgs.add(arguments.get(pos));
				List<MatchResult> srl = matchArgumentList(arguments, pos + 1,
						definition, defPos);
				for (MatchResult sr : srl) {
					if (sr.isMatched()) {
						reportExtraArguments(extraArgs, 0, sr.getErrors());
						// sr.setPriority(MatchResult.REGULAR);
						sr.setArgumentsUsed(sr.getArgumentsUsed() + 1);
						list.add(sr);
					}
				}
			}
		}

		TclErrorCollector collector = new TclErrorCollector();
		for (MatchResult r : list) {
			if (r.isMatched() || r.isImplicit()) {
				List<MatchResult> srl = matchArgumentList(arguments, pos
						+ r.getArgumentsUsed(), definition, defPos + 1);
				boolean matched = false;
				for (MatchResult sr : srl) {
					if (sr.isMatched()) {
						matched = true;
						sr.setSummaryPriorityOf(r, sr);
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
				if (!matched && srl.size() == 1) {
					for (MatchResult sr : srl) {
						collector.addAll(sr.getErrors());
					}
					MatchResult sr = srl.get(0);
					if (results.size() == 0) {
						MatchResult result = new MatchResult();
						result.setMatched(false);
						result.setSummaryPriorityOf(r, sr);
						result.setMatchWithErrors(true);
						result.getErrors().addAll(sr.getErrors());
						result.setArgumentsUsed(r.getArgumentsUsed()
								+ sr.getArgumentsUsed());
						results.add(result);
					}
				}

			} else {
				collector.addAll(r.getErrors());
			}
		}
		if (results.size() == 0) {
			int last = arguments.size() - pos;
			MatchResult result = new MatchResult();
			result.setMatched(false);
			result.setArgumentsUsed((last > 0) ? last : 0);
			result.setMatchWithErrors(last <= 0);
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
			for (MatchResult result : results)
				if (result.isMatched() && result.getArgumentsUsed() > 0) {
					if (arguments.get(pos) instanceof StringArgument) {
						result.incrPriority();
						result.incrPriority();
					}
				}
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
						// List<Integer> blockArguments= new
						// ArrayList<Integer>();
						// TODO send blockArguments to
						// TclParserUtils.parseCommandArguments()
						List<TclArgument> subArguments = TclParserUtils
								.parseCommandArguments(argument.getStart()
										+ substring.offset, substring.value,
										null);
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

	private void matchExpression(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		// TypedArgument typedArgument = (TypedArgument) definition;
		// TclArgument argument = arguments.get(pos);
		// SubstitutedArgumentValue substring =
		// getSubstitutedArgumentValue(argument);
		// if (substring.value == null) {
		// return;
		// }
		// List<TclArgument> subArguments = TclParserUtils
		// .parseCommandArguments(argument.getStart()
		// + substring.offset, substring.value);
		// TclArgumentList list = AstFactory.eINSTANCE.createTclArgumentList();
		// list.getArguments().addAll(subArguments);
		// arguments.set(pos, list);
		matchSinglePositionArgument(results, arguments, pos, definition,
				new ISinglePositionRule() {
					public boolean check(TclArgument argument,
							Argument definition, List<Integer> scriptPositions,
							int position, TclErrorCollector collector,
							List<ComplexArgumentResult> results) {

						// TypedArgument typedArgument = (TypedArgument)
						// definition;
						SubstitutedArgumentValue substring = getSubstitutedArgumentValue(argument);
						if (substring.value == null) {
							return true;
						}
						List<Integer> blockArguments = new ArrayList<Integer>();
						List<TclArgument> subArguments = TclParserUtils
								.parseCommandArguments(argument.getStart()
										+ substring.offset, substring.value,
										blockArguments);
						// EList<Argument> definitionArguments = typedArgument
						// .getArguments();
						// MatchResult res = matchArgument(subArguments, 0,
						// definitionArguments, 0);
						// if (res.isMatched()) {
						// if (res.getArgumentsUsed() ==
						// subArguments.size()) {
						ComplexArgumentResult cResult = new ComplexArgumentResult(
								position, subArguments, blockArguments);
						// cResult.getComplexArguments().addAll(
						// res.getComplexArguments());
						// collector.reportAll(res.getErrors());
						// cResult.setDefinition(typedArgument);
						results.add(cResult);
						// if (res.getArgumentsUsed() < subArguments.size()) {
						// reportExtraArguments(subArguments, res
						// .getArgumentsUsed(), collector);
						// }
						/*
						 * else {
						 * reportInvalidArgumentCount(argument.getStart(),
						 * argument.getEnd(), res .getArgumentsUsed(),
						 * collector); }
						 */
						return true;
						// } else {
						// }
						// }
						// return false;
					}

				}, false);
	}

	private void matchSwitchArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		Switch switchDef = (Switch) definition;
		int lowerBound = switchDef.getLowerBound();
		int upperBound = switchDef.getUpperBound();
		if (upperBound == -1) {
			upperBound = Integer.MAX_VALUE;
		}
		List<MatchResult> ress = new ArrayList<MatchResult>();
		Map<MatchResult, Integer> counts = new HashMap<MatchResult, Integer>();

		matchSwitch(arguments, pos, switchDef, ress, counts, 0, upperBound);

		int ressSize = ress.size();
		for (int i = 0; i < ressSize; i++) {
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
		int argSize = arguments.size();
		if (ressSize == 0 && argSize > pos && DefinitionUtils.isMode(switchDef)) {
			if (arguments.get(pos) instanceof ISubstitution) {
				MatchResult r = new MatchResult();
				r.setArgumentsUsed(1);
				r.setMatched(true);
				r.setMatchWithErrors(false);
				results.add(r);
			}
		}
		// Add empty variant if multiplicity support it.
		if (ressSize == 0 && lowerBound > 0) {
			// We should report error if not argument are specified, but
			// required.
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(false);
			r.setMatchWithErrors(false);
			TclArgument a = null;
			if (argSize > pos) {
				a = arguments.get(pos);
			}
			reportMissingSwitch(switchDef, r, a);
			results.add(r);
		} else if (lowerBound == 0) {
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(true);
			r.setMatchWithErrors(false);
			results.add(r);
		}
	}

	private void matchSwitch(List<TclArgument> arguments, int pos, Switch sw,
			List<MatchResult> ress, Map<MatchResult, Integer> counts,
			int count, int upperBound) {
		TclErrorCollector errors = new TclErrorCollector();
		if (pos >= arguments.size()) {
			return;
		}

		List<MatchResult> list = new ArrayList<MatchResult>();

		Map<Group, Argument> fitGroupMap = getFitGroupMap(arguments, pos, sw);

		Map<String, List<Group>> constFitGroupMap = new HashMap<String, List<Group>>();
		for (Group group : fitGroupMap.keySet()) {
			Argument selector = fitGroupMap.get(group);
			if (selector instanceof Constant) {
				List<Group> groups = constFitGroupMap.get(selector);
				if (groups == null) {
					groups = new ArrayList<Group>();
				}
				groups.add(group);
				constFitGroupMap.put(selector.getName(), groups);
				fitGroupMap.remove(fitGroupMap.get(group));
			}
		}
		if (constFitGroupMap.size() == 1) {
			for (String string : constFitGroupMap.keySet()) {
				((StringArgument) arguments.get(pos)).setValue(string);
				for (Group group : constFitGroupMap.get(string)) {
					matchGroupArgument(arguments, pos, group, list);
				}
			}
		} else {
			for (Group group : fitGroupMap.keySet()) {
				matchGroupArgument(arguments, pos, group, list);
			}
			if (list.size() == 1) {
				MatchResult r = list.get(0);
				if (sw.getLowerBound() > 0) {
					r.incrPriority();
				}
			}
		}

		int lSize = list.size();
		for (int j = 0; j < lSize; j++) {
			MatchResult r = list.get(j);
			if (r.isMatched() || r.isImplicit()/* && !r.isMatchWithErrors() */) {
				List<MatchResult> ress2 = new ArrayList<MatchResult>();
				counts.put(r, Integer.valueOf(count + 1));
				ress.add(r);
				if (count + 1 == upperBound) {
					continue;
				}
				if (r.getArgumentsUsed() > 0) {
					matchSwitch(arguments, pos + r.getArgumentsUsed(), sw,
							ress2, counts, count + 1, upperBound);
					for (int k = 0; k < ress2.size(); k++) {
						MatchResult r2 = ress2.get(k);
						if (r2.isMatched() && !r2.isMatchWithErrors()) {
							r2.setArgumentsUsed(r.getArgumentsUsed()
									+ r2.getArgumentsUsed());
							r2.setSummaryPriorityOf(r, r2);
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

		Constant constant = DefinitionUtils.extractGroupPseudoConstant(group);
		List<Argument> groupArguments = group.getArguments();
		if (constant != null) {
			List<Argument> newArgs = new ArrayList<Argument>();
			newArgs.add(constant);
			newArgs.addAll(group.getArguments());
			groupArguments = newArgs;
		}
		List<MatchResult> ress = new ArrayList<MatchResult>();
		Map<MatchResult, Integer> counts = new HashMap<MatchResult, Integer>();
		matchGroup(arguments, pos, groupArguments, ress, counts, 0, upperBound);
		int ressSize = ress.size();
		for (int i = 0; i < ressSize; i++) {
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
		if (ressSize == 0 && lowerBound > 0) {
			// We should report error if not argument are specified, but
			// required.
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(0);
			r.setMatched(false);
			r.setMatchWithErrors(false);
			reportMissingGroup(group, r);
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
			if (r.isMatched() || r.isImplicit() || r.isMatchWithErrors()) {
				List<MatchResult> ress2 = new ArrayList<MatchResult>();
				counts.put(r, Integer.valueOf(count + 1));
				ress.add(r);
				if (count + 1 == upperBound) {
					continue;
				}
				if (r.getArgumentsUsed() > 0) {
					matchGroup(arguments, pos + r.getArgumentsUsed(),
							groupArguments, ress2, counts, count + 1,
							upperBound);
					int ress2Size = ress2.size();
					for (int k = 0; k < ress2Size; k++) {
						MatchResult r2 = ress2.get(k);
						if (r2.isMatched()/* && !r2.isMatchWithErrors() */) {
							r2.setArgumentsUsed(r.getArgumentsUsed()
									+ r2.getArgumentsUsed());
							r2.setSummaryPriorityOf(r, r2);
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
		final String value = constDefinition.getName();
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
						}
						// else if (!constDefinition.isStrictMatch()
						// && argumentValue.value == null) {
						// return true;
						// }
						return false;
					}

				}, true);
	}

	private void matchTypedArgument(List<TclArgument> arguments, int pos,
			Argument definition, List<MatchResult> results) {
		TypedArgument arg = (TypedArgument) definition;
		final ArgumentType type = arg.getType();
		if (ArgumentType.EXPRESSION_VALUE == type.getValue()) {
			matchExpression(arguments, pos, definition, results);
		} else {
			matchSinglePositionArgument(results, arguments, pos, definition,
					new ISinglePositionRule() {
						public boolean check(TclArgument argument,
								Argument definition,
								List<Integer> scriptPositions, int position,
								TclErrorCollector collector,
								List<ComplexArgumentResult> complex) {
							if (checkType(argument, type, scriptPositions,
									position, collector)) {
								return true;
							}
							return false;
						}

					}, false);
		}
	}

	private void matchSinglePositionArgument(List<MatchResult> results,
			List<TclArgument> arguments, int pos, Argument definitionArg,
			ISinglePositionRule rule, boolean returnMaxMatchedResult) {
		int lowerBound = definitionArg.getLowerBound();
		int upperBound = definitionArg.getUpperBound();
		if (upperBound == -1) {
			upperBound = Integer.MAX_VALUE;
		}
		int count = 0;
		List<Integer> scriptPositions = new ArrayList<Integer>();
		List<ComplexArgumentResult> complexArguments = new ArrayList<ComplexArgumentResult>();
		// We need to check for low
		Map<Integer, TclErrorCollector> collectors = new HashMap<Integer, TclErrorCollector>();
		int argsSize = arguments.size();
		for (int i = 0; i < argsSize - pos; i++) {
			TclErrorCollector collector = new TclErrorCollector();

			TclArgument a = arguments.get(pos + i);
			if (rule.check(a, definitionArg, scriptPositions, pos + i,
					collector, complexArguments)) {
				collectors.put(Integer.valueOf(i), collector);
				count++;
			} else {
				if (i < lowerBound)
					collectors.put(Integer.valueOf(i), collector);
				break;
			}
			// Do not, check extra arguments
			if (count == upperBound) {
				break;
			}
		}
		if (count < lowerBound) {
			MatchResult r = new MatchResult();
			r.setArgumentsUsed(count);
			r.setMatched(count > 0);
			r.setMatchWithErrors(true);
			int start = this.command.getStart();
			int end = this.command.getEnd();
			if (argsSize > pos) {
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
		if (count >= lowerBound) {
			int up = count;
			if (up > upperBound) {
				up = upperBound;
			}
			int from = lowerBound;
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
		// if (lowerBound == 0) {
		// // Add empty variant if multiplicity support it.
		// MatchResult r = new MatchResult();
		// r.setArgumentsUsed(0);
		// r.setMatched(true);
		// r.setMatchWithErrors(false);
		// results.add(r);
		// }
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
			if (!value.startsWith("-")) {
				scriptPositions.add(Integer.valueOf(position));
				result = true;
			}
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
				Integer.parseInt(value);
				result = true;
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

	private Map<Group, Argument> getFitGroupMap(List<TclArgument> arguments,
			int pos, Switch sw) {
		// List<Group> fit = new ArrayList<Group>();
		Map<Group, Argument> fit = new HashMap<Group, Argument>();
		String prefix = null;
		TclArgument tclArgument = null;
		if (arguments != null && pos < arguments.size()) {
			tclArgument = arguments.get(pos);
			if (tclArgument != null) {
				if (tclArgument instanceof StringArgument) {
					prefix = ((StringArgument) tclArgument).getValue();
				}
			} else
				return fit;
		}
		Map<Group, Argument> selectorsMap = getSwitchSelectorMap(sw);
		for (Group group : selectorsMap.keySet()) {
			Argument selector = selectorsMap.get(group);
			if (selector instanceof Constant) {
				if (prefix != null) {
					if (sw.isCheckPrefix()
							&& selector.getName().startsWith(prefix)) {
						fit.put(group, selector);
					}
					if (selector.getName().equals(prefix)) {
						fit.clear();
						fit.put(group, selector);
						break;
					}
				}
			} else if (selector instanceof TypedArgument) {
				List<MatchResult> ress = matchDefinition(arguments, pos,
						selector);
				for (MatchResult r : ress) {
					if (r.isMatched()) {
						fit.put(group, selector);
						break;
					}
				}
			} else {
				fit.put(group, selector);
			}
		}
		return fit;
	}

	public Map<Group, Argument> getSwitchSelectorMap(Switch sw) {
		Map<Group, Argument> map = new HashMap<Group, Argument>();
		for (Group group : sw.getGroups()) {
			map.put(group, getFirstSelector(group));
		}
		return map;
	}

	public Argument getFirstSelector(Argument definition) {
		Argument selector = null;
		if (definition instanceof Constant
				|| definition instanceof TypedArgument
				|| definition instanceof ComplexArgument) {
			selector = DefinitionUtils.copyArgument(definition);
		} else if (definition instanceof Group) {
			Group group = (Group) definition;
			Constant constant = DefinitionUtils
					.extractGroupPseudoConstant(group);
			if (constant != null) {
				selector = constant;
			} else if (group.getArguments().size() > 0) {
				for (int i = 0; i < group.getArguments().size(); i++) {
					if (group.getArguments().get(i).getLowerBound() > 0) {
						selector = getFirstSelector(group.getArguments().get(i));
						break;
					}
				}
			}
		}
		return selector;
	}

	public List<Argument> getFinalSelectors(Argument definition) {
		List<Argument> selectors = new ArrayList<Argument>();
		if (definition instanceof Constant
				|| definition instanceof TypedArgument
				|| definition instanceof ComplexArgument) {
			selectors.add(DefinitionUtils.copyArgument(definition));
		} else if (definition instanceof Group) {
			Group group = (Group) definition;
			Constant constant = DefinitionUtils
					.extractGroupPseudoConstant(group);
			if (constant != null) {
				selectors.add(constant);
			} else {
				EList<Argument> groupArgs = group.getArguments();
				int groupArgsSize = groupArgs.size();
				if (groupArgsSize > 0) {
					for (int i = 0; i < groupArgsSize; i++) {
						Argument gArg = groupArgs.get(i);
						if (gArg.getLowerBound() > 0) {
							selectors.addAll(getFinalSelectors(gArg));
							break;
						}
					}
				}
			}
		} else if (definition instanceof Switch) {
			Switch sw = (Switch) definition;
			for (Group group : sw.getGroups()) {
				selectors.addAll(getFinalSelectors(group));
			}
		}
		return selectors;
	}

	public TclErrorCollector getErrorReporter() {
		return this.errors;
	}

	private String[] getExtraArgs() {
		return null;
		// return new String[] { IProblem.DESCRIPTION_ARGUMENT_PREFIX
		// + "Synopsis:\n" + getSynopsis() };
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
							NLS
									.bind(
											Messages.TclArgumentMatcher_Argument_Of_Type_ExpectedDetail,
											new Object[] {
													synopsisBuilder
															.typeToString(type),
													synopsisBuilder
															.argumentToString(argument),
													additional }),
							getExtraArgs(), argument.getStart(), argument
									.getEnd(), ITclErrorReporter.ERROR);
		} else {
			collector
					.report(
							ITclErrorReporter.INVALID_ARGUMENT_VALUE,
							NLS
									.bind(
											Messages.TclArgumentMatcher_Argument_Of_Type_Expected,
											new Object[] {
													synopsisBuilder
															.typeToString(type),
													synopsisBuilder
															.argumentToString(argument) }),
							getExtraArgs(), argument.getStart(), argument
									.getEnd(), ITclErrorReporter.ERROR);
		}
	}

	private void reportExtraArguments(List<TclArgument> list,
			int argumentsUsed, TclErrorCollector collector) {
		if (list.size() > argumentsUsed) {
			TclArgument begin = list.get(argumentsUsed);
			TclArgument end = list.get(list.size() - 1);
			String message = NLS
					.bind(Messages.TclArgumentMatcher_Extra_Arguments,
							new Object[] { Integer.valueOf(list.size()
									- argumentsUsed) });
			collector.report(ITclErrorConstants.EXTRA_ARGUMENTS, message,
					getExtraArgs(), begin.getStart(), end.getEnd(),
					ITclErrorConstants.WARNING);
		}
	}

	private void reportMissingArgument(Argument definitionArg, MatchResult r,
			int start, int end) {
		String value;
		String arg = synopsisBuilder.definitionToString(DefinitionUtils
				.minimizeBounds(definitionArg));
		if (definitionArg instanceof TypedArgument) {
			String type = synopsisBuilder.typeToString(
					((TypedArgument) definitionArg).getType()).toLowerCase();
			value = NLS.bind(Messages.TclArgumentMatcher_Missing_TypedArgument,
					new Object[] { type, arg });
		} else {
			value = NLS.bind(Messages.TclArgumentMatcher_Missing_Argument,
					new Object[] { arg });
		}
		r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, value,
				getExtraArgs(), start, end, ITclErrorReporter.ERROR);
	}

	// private void reportMissingArguments(List<Argument> definitions,
	// MatchResult r, int start, int end) {
	// if (definitions.size() == 1) {
	// reportMissingArgument(definitions.get(0), r, start, end);
	// } else {
	// String value = MessageFormat.format(
	// Messages.TclArgumentMatcher_Missing_Argument,
	// new Object[] { synopsisBuilder
	// .definitionToString(definitions) });
	// r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, value,
	// getExtraArgs(), start, end, ITclErrorReporter.ERROR);
	// }
	// }

	private void reportMissingGroup(Group group, MatchResult r) {
		Argument selector = getFirstSelector(group);
		String message = NLS.bind(Messages.TclArgumentMatcher_Missing_Argument,
				new Object[] { synopsisBuilder.definitionToString(selector) });
		r.getErrors().report(ITclErrorReporter.MISSING_ARGUMENT, message,
				getExtraArgs(), this.command.getStart(), this.command.getEnd(),
				ITclErrorReporter.ERROR);
	}

	private void reportMissingSwitch(Switch sw, MatchResult r,
			TclArgument tclArgument) {
		List<TclArgument> arguments = new ArrayList<TclArgument>();
		arguments.add(tclArgument);
		Map<Group, Argument> fitGroups = getFitGroupMap(arguments, 0, sw);
		List<Argument> selectors = new ArrayList<Argument>();
		if (fitGroups.size() == 0) {
			for (Group group : sw.getGroups()) {
				selectors.addAll(getFinalSelectors(group));
			}
		} else {
			for (Group group : sw.getGroups()) {
				if (fitGroups.containsKey(group))
					selectors.addAll(getFinalSelectors(group));
			}
		}
		String expected = synopsisBuilder.definitionToList(DefinitionUtils
				.minimizeBounds(selectors));
		String message = "";
		int code = 0;
		if (tclArgument != null) {
			message = NLS.bind(
					Messages.TclArgumentMatcher_Invalid_Arguments_And_Expected,
					new Object[] { expected,
							synopsisBuilder.argumentToString(tclArgument) });
			code = ITclErrorReporter.INVALID_ARGUMENT_VALUE;
			r.getErrors().report(code, message, getExtraArgs(),
					tclArgument.getStart(), tclArgument.getEnd(),
					ITclErrorReporter.ERROR);
			return;
		} else {
			message = NLS.bind(
					Messages.TclArgumentMatcher_Missing_Switch_Argument,
					new Object[] { expected });
			code = ITclErrorReporter.MISSING_ARGUMENT;
		}
		if (expected.length() == 0) {
			message = Messages.TclArgumentMatcher_Missing_Switch_Arg;
		}
		r.getErrors().report(code, message, getExtraArgs(),
				this.command.getStart(), this.command.getEnd(),
				ITclErrorReporter.ERROR);
	}

	public Map<Argument, int[]> getMappings() {
		return new HashMap<Argument, int[]>(this.mappings);
	}
}
