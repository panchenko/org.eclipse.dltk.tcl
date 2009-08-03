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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.ArgumentMatch;
import org.eclipse.dltk.tcl.ast.AstFactory;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.definitions.Argument;
import org.eclipse.dltk.tcl.definitions.Command;
import org.eclipse.dltk.tcl.internal.parser.raw.BracesSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.CommandSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.ISubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.QuotesSubstitution;
import org.eclipse.dltk.tcl.internal.parser.raw.SimpleTclParser;
import org.eclipse.dltk.tcl.internal.parser.raw.TclElement;
import org.eclipse.dltk.tcl.internal.parser.raw.TclParseException;
import org.eclipse.dltk.tcl.internal.parser.raw.TclScript;
import org.eclipse.dltk.tcl.internal.parser.raw.TclWord;
import org.eclipse.dltk.tcl.internal.parser.raw.VariableSubstitution;
import org.eclipse.dltk.tcl.parser.TclArgumentMatcher.ComplexArgumentResult;
import org.eclipse.dltk.tcl.parser.definitions.IScopeProcessor;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.emf.common.util.EList;
import org.eclipse.osgi.util.NLS;

public class TclParser implements ITclParserOptions {
	private static final boolean TRACE_PARSER = false;

	private String source;
	private ITclErrorReporter reporter;
	private IScopeProcessor scopeProcessor;
	private int globalOffset = 0;

	private Map<String, Boolean> options = new HashMap<String, Boolean>();

	private String version;

	public TclParser() {
		this(null);
	}

	public TclParser(String version) {
		this.version = version;
		setOptionValue(REPORT_UNKNOWN_AS_ERROR, false);
	}

	/**
	 * By default all not specified options are true.
	 */
	public boolean isOptionSet(String option) {
		Boolean bool = this.options.get(option);
		if (bool == null) {
			return true;
		}
		return bool.booleanValue();
	}

	public void setOptionValue(String option, boolean value) {
		this.options.put(option, value);
	}

	public List<TclCommand> parse(String source) {
		return parse(source, null, null);
	}

	public void setGlobalOffset(int globalOffset) {
		this.globalOffset = globalOffset;
	}

	public TclModule parseModule(String source, ITclErrorReporter reporter,
			IScopeProcessor scopeProcessor) {
		TclModule module = AstFactory.eINSTANCE.createTclModule();
		TclCodeModel codeModel = AstFactory.eINSTANCE.createTclCodeModel();
		module.setCodeModel(codeModel);
		module.setSize(source.length() + globalOffset);
		ISourceLineTracker tracker = TextUtils.createLineTracker(source);
		int[] offsets = tracker.getLineOffsets();
		String[] delimeters = tracker.getDelimeters();
		EList<Integer> loff = codeModel.getLineOffsets();
		for (int i = 0; i < offsets.length; i++) {
			loff.add(offsets[i] + globalOffset);
		}
		codeModel.getDelimeters().addAll(Arrays.asList(delimeters));

		this.source = source;
		this.reporter = reporter;
		this.scopeProcessor = scopeProcessor;
		List<TclCommand> tclCommands = new ArrayList<TclCommand>();
		parseToBlock(tclCommands, source, 0);

		module.getStatements().addAll(tclCommands);
		return module;
	}

	public List<TclCommand> parse(String source, ITclErrorReporter reporter,
			IScopeProcessor scopeProcessor) {
		this.source = source;
		this.reporter = reporter;
		this.scopeProcessor = scopeProcessor;
		List<TclCommand> tclCommands = new ArrayList<TclCommand>();
		parseToBlock(tclCommands, source, 0);
		return tclCommands;
	}

	private void parseToBlock(List<TclCommand> block, String partSource,
			int offset) {
		SimpleTclParser simpleParser = new SimpleTclParser(offset);

		try {
			PerformanceMonitor.getDefault().begin("RAW_PARSE_TIME");
			simpleParser.setProblemReporter(this.reporter);
			TclScript script = simpleParser.parse(partSource);
			PerformanceMonitor.getDefault().end("RAW_PARSE_TIME");
			List<org.eclipse.dltk.tcl.internal.parser.raw.TclCommand> commands = script
					.getCommands();
			processRawCommands(block, offset, commands);
		} catch (TclParseException e) {
			e.printStackTrace();
		}
	}

	private void processRawCommands(List<TclCommand> block, int offset,
			List<org.eclipse.dltk.tcl.internal.parser.raw.TclCommand> commands) {
		for (org.eclipse.dltk.tcl.internal.parser.raw.TclCommand command : commands) {
			// Basic TclCommand
			TclCommand st = parseTclCommand(command, offset, this.source);
			if (st != null) {
				TclCommand comm = processTclCommand(st);
				if (comm != null) {
					block.add(comm);
				}
			}
		}
	}

	/**
	 * Process TclCommand and build all required classes.
	 * 
	 * @param st
	 * @return
	 */
	private TclCommand processTclCommand(TclCommand st) {
		if (this.scopeProcessor == null) {
			return st;
		}
		EList<TclArgument> arguments = st.getArguments();
		TclArgument commandName = st.getName();
		if (commandName instanceof Substitution) {
			reportCommandNameSubstitution(commandName);
			return st;
		}
		if (commandName instanceof StringArgument) {
			String commandValue = ((StringArgument) commandName).getValue();
			Command[] definitions = this.scopeProcessor
					.getCommandDefinition(commandValue);
			TclErrorCollector parseErrors = new TclErrorCollector();
			boolean matched = false;

			st.setQualifiedName(this.scopeProcessor
					.getQualifiedName(commandValue));

			if (definitions != null && 0 != definitions.length) {
				for (int i = 0; i < definitions.length; i++) {
					perf("processTclCommand:" + definitions[i].getName());
					Command definition = definitions[i];
					if (TRACE_PARSER) {
						System.out.println("Matching command:"
								+ definition.getName());
						int start = st.getStart();
						int end = st.getEnd();
						System.out.println("Code:\n"
								+ source.substring(start, end));
					}
					boolean validVersion = true;
					if (this.version != null && definition.getVersion() != null) {
						validVersion = TclParserUtils.parseVersion(definition
								.getVersion(), this.version);
					}
					if (!validVersion) {
						reportInvalidVersion(st, commandValue, parseErrors,
								definition);
						perfDone("processTclCommand:"
								+ definitions[i].getName());
						continue;
					}
					// Match only command with compatible version.
					ISubstitutionManager manager = null;
					if (this.scopeProcessor != null) {
						manager = this.scopeProcessor.getSubstitutionManager();
					}
					TclArgumentMatcher matcher = new TclArgumentMatcher(st,
							this.options, manager);

					perf("matchTclCommand:" + definitions[i].getName());
					perf("GLOBAL_MATCH_TIME");
					if (TRACE_PARSER) {
						System.out.println("Matching command:"
								+ definitions[i].getName());
					}
					st.setDefinition(definition);
					st.setMatched(false);
					if (matcher.match(definition)) {
						st.setMatched(true);
						perfDone("matchTclCommand:" + definitions[i].getName());
						// Set deprecation
						if (definition.getDeprecated() != null
								&& this.version != null) {
							if (TclParserUtils.parseVersion(definition
									.getDeprecated(), this.version)) {
								reportDeprecatedError(st, commandValue,
										definition);
							}
						}
						if (!scopeProcessor.checkCommandScope(definition)) {
							reportOutOfScopeError(st, commandValue, definition);
						}

						this.scopeProcessor.processCommand(st);
						// Parse block arguments.
						int[] blockArguments = matcher.getBlockArguments();
						parseReplaceBlockArguments(arguments, blockArguments);
						// Convert arguments to argument lists and parse inner
						// block
						// arguments.
						ComplexArgumentResult[] complexArguments = matcher
								.getComplexArguments();
						processComplexArguments(arguments, complexArguments);

						this.scopeProcessor.endProcessCommand();
						matched = true;
						// We need to create mapping structure
						createMappings(st, matcher);
					} else {
						perf("matchTclCommand:" + definitions[i].getName());
					}
					repr("error count:" + definitions[i].getName(), matcher
							.getErrorReporter().getCount());
					perfDone("GLOBAL_MATCH_TIME");

					matcher.reportErrors(this.reporter);
					repr("error count:" + definitions[i].getName(), matcher
							.getErrorReporter().getCount());
					perfDone("processTclCommand:" + definitions[i].getName());
					return st;
				}

				if (!matched) {
					parseErrors.reportAll(this.reporter);

				} else {
					return st;
				}
			} else {
				if (isOptionSet(REPORT_UNKNOWN_AS_ERROR) && reporter != null) {
					this.reporter.report(ITclErrorReporter.UNKNOWN_COMMAND,
							Messages.TclParser_Unknown_Command + commandValue,
							null, commandName.getStart(), commandName.getEnd(),
							ITclErrorReporter.WARNING);
				}
			}
		}
		return st;
	}

	private void createMappings(TclCommand st, TclArgumentMatcher matcher) {
		EList<TclArgument> arguments = st.getArguments();
		EList<ArgumentMatch> matches = st.getMatches();
		Map<Argument, int[]> map = matcher.getMappings();
		for (Argument arg : map.keySet()) {
			int[] positions = map.get(arg);
			ArgumentMatch match = AstFactory.eINSTANCE.createArgumentMatch();
			match.setDefinition(arg);
			for (int i = 0; i < positions.length; i++) {
				int start = positions[0];
				int end = positions[1];
				for (int j = start; (j < end) & (j < arguments.size()); j++) {
					match.getArguments().add(arguments.get(j));
				}
			}
			matches.add(match);
		}
	}

	private void processComplexArguments(EList<TclArgument> arguments,
			ComplexArgumentResult[] complexArguments) {
		for (ComplexArgumentResult arg : complexArguments) {
			TclArgument original = arguments.get(arg.getArgumentNumber());
			int[] blockArguments2 = arg.getBlockArguments();
			List<TclArgument> arguments2 = arg.getArguments();
			parseReplaceBlockArguments(arguments2, blockArguments2);
			TclArgumentList list = AstFactory.eINSTANCE.createTclArgumentList();
			list.setDefinitionArgument(arg.getDefinition());
			list.getArguments().addAll(arguments2);
			list.setStart(original.getStart() + globalOffset);
			list.setEnd(original.getEnd() + globalOffset);
			List<ComplexArgumentResult> complexArguments2 = arg
					.getComplexArguments();
			if (complexArguments2.size() > 0) {
				processComplexArguments(list.getArguments(), complexArguments2
						.toArray(new ComplexArgumentResult[complexArguments2
								.size()]));
			}
			arguments.set(arg.getArgumentNumber(), list);
			list.setKind(0);
			if (original instanceof ComplexString) {
				list.setKind(((ComplexString) original).getKind());
			} else if (original instanceof StringArgument) {
				StringArgument sArg = (StringArgument) original;
				String value = sArg.getValue();
				list.setKind(0);
				if (value.startsWith("{") && value.endsWith("}")) {
					list.setKind(1);
				}
				if (value.startsWith("\"") && value.endsWith("\"")) {
					list.setKind(2);
				}
			}

			// list.setOriginalArgument(oldArgument);
		}
	}

	private void perf(String name) {
		if (PerformanceMonitor.PERFOMANCE_MONITORING_IS_ACTIVE) {
			PerformanceMonitor.getDefault().begin(name);
		}
	}

	private void perfDone(String name) {
		if (PerformanceMonitor.PERFOMANCE_MONITORING_IS_ACTIVE) {
			PerformanceMonitor.getDefault().end(name);
		}
	}

	private void repr(String name, int count) {
		if (PerformanceMonitor.PERFOMANCE_MONITORING_IS_ACTIVE) {
			PerformanceMonitor.getDefault().add(name, count);
		}
	}

	private void parseReplaceBlockArguments(List<TclArgument> arguments,
			int[] blockArguments) {
		for (int i = 0; i < blockArguments.length; i++) {
			StringArgument blockCode = (StringArgument) arguments
					.get(blockArguments[i]);
			Script script = AstFactory.eINSTANCE.createScript();
			script.setStart(blockCode.getStart() + globalOffset);
			script.setEnd(blockCode.getEnd() + globalOffset);
			String wordText = blockCode.getValue();
			if (wordText.startsWith("{") && wordText.endsWith("}")
					|| wordText.startsWith("\"") && wordText.endsWith("\"")) {
				script.setContentStart(script.getStart() + 1 + globalOffset);
				script.setContentEnd(script.getEnd() - 1 + globalOffset);
				parseToBlock(script.getCommands(), wordText.substring(1,
						wordText.length() - 1), blockCode.getStart() + 1
						- globalOffset);
			} else {
				script.setContentStart(script.getStart() + globalOffset);
				script.setContentEnd(script.getEnd() + globalOffset);
				parseToBlock(script.getCommands(), wordText, blockCode
						.getStart()
						- globalOffset);
			}
			arguments.set(blockArguments[i], script);
		}
	}

	private TclCommand parseTclCommand(
			org.eclipse.dltk.tcl.internal.parser.raw.TclCommand command,
			int offset, String content) {
		try {

			AstFactory factory = AstFactory.eINSTANCE;

			TclCommand tclCommand = factory.createTclCommand();
			List<TclWord> words = command.getWords();
			boolean name = true;
			int start = -1;
			for (Iterator<TclWord> iterator = words.iterator(); iterator
					.hasNext();) {
				TclWord word = iterator.next();
				if (start == -1) {
					start = word.getStart();
				}

				TclArgument exp = null;
				// wordText = SimpleTclParser.magicSubstitute(wordText);
				List<Object> contents = word.getContents();
				if (contents.size() == 1) {
					Object o = contents.get(0);
					exp = processWordContentAsExpression(offset, content,
							factory, word.getStart(), word.getEnd(), o);
				} else if (contents.size() > 1) {
					ComplexString literal = makeComplexString(offset, content,
							factory, word.getStart(), word.getEnd(), contents);
					exp = literal;
				}
				if (name) {
					tclCommand.setName(exp);
					name = false;
				} else {
					tclCommand.getArguments().add(exp);
				}
			}
			tclCommand.setStart(start + offset + globalOffset);
			tclCommand.setEnd(command.getEnd() + offset + 1 + globalOffset);
			return tclCommand;
		} catch (StringIndexOutOfBoundsException bounds) {
			if (reporter != null) {
				reporter.report(ITclErrorReporter.UNKNOWN, bounds.getMessage(),
						null, 0, 0, ITclErrorReporter.ERROR);
			}
			return null;
		}
	}

	private TclArgument processWordContentAsExpression(int offset,
			String content, AstFactory factory, int start, int end, Object o) {
		TclArgument exp;
		if (o instanceof QuotesSubstitution) {
			QuotesSubstitution qs = (QuotesSubstitution) o;

			List<Object> contents = qs.getContents();
			if (contents.size() == 1) {
				String wordText = null;
				wordText = content.substring(offset + start, offset + end + 1);
				StringArgument literal = factory.createStringArgument();
				literal.setStart(offset + qs.getStart() + globalOffset);
				literal.setEnd(offset + qs.getEnd() + 1 + globalOffset);
				literal.setValue(wordText);
				exp = literal;
			} else {
				ComplexString literal = makeComplexString(offset, content,
						factory, qs.getStart(), qs.getEnd(), contents);
				exp = literal;
			}
		} else if (o instanceof BracesSubstitution) {
			String wordText = null;
			wordText = content.substring(start + offset, end + offset + 1);
			BracesSubstitution bs = (BracesSubstitution) o;

			StringArgument block = factory.createStringArgument();
			block.setStart(offset + bs.getStart() + globalOffset);
			block.setEnd(offset + bs.getEnd() + 1 + globalOffset);
			block.setValue(wordText);
			exp = block;
		} else if (o instanceof CommandSubstitution) {
			CommandSubstitution bs = (CommandSubstitution) o;

			Substitution bl = factory.createSubstitution();
			bl.setStart(offset + bs.getStart() + globalOffset);
			bl.setEnd(offset + bs.getEnd() + 1 + globalOffset);
			TclScript script = bs.getScript();
			processRawCommands(bl.getCommands(), offset, script.getCommands());

			exp = bl;
		} else if (o instanceof VariableSubstitution) {
			VariableSubstitution bs = (VariableSubstitution) o;
			VariableReference ref = factory.createVariableReference();
			ref.setStart(offset + bs.getStart() + globalOffset);
			ref.setEnd(offset + bs.getEnd() + 1 + globalOffset);
			ref.setName(bs.getName());
			TclWord index = bs.getIndex();
			if (index != null) {
				if (index.getContents().size() == 1) {
					TclArgument a = processWordContentAsExpression(offset,
							content, factory, index.getStart(), index.getEnd(),
							index.getContents().get(0));
					if (a != null) {
						ref.setIndex(a);
					}
				} else if (index.getContents().size() > 1) {
					ComplexString literal = makeComplexString(offset, content,
							factory, index.getStart(), index.getEnd(), index
									.getContents());
					if (literal != null) {
						ref.setIndex(literal);
					}
				}
			}
			exp = ref;
		} else {
			String wordText = null;
			// if (!(o instanceof String)) {
			// try {
			wordText = content.substring(offset + start, offset + end + 1);
			// } catch (Exception e) {
			// if (o instanceof String) {
			// wordText = (String) o;
			// }
			// }
			// } else {
			// wordText = (String) o;
			// }

			StringArgument reference = factory.createStringArgument();
			reference.setStart(offset + start + globalOffset);
			reference.setEnd(offset + end + 1 + globalOffset);
			reference.setValue(wordText);
			exp = reference;
		}
		return exp;
	}

	private ComplexString makeComplexString(int offset, String content,
			AstFactory factory, int start, int end, List<Object> contents) {
		ComplexString literal = factory.createComplexString();
		literal.setStart(offset + start + globalOffset);
		literal.setEnd(offset + end + 1 + globalOffset);
		String value = content.substring(offset + start, offset + end + 1);
		literal.setKind(0);
		int add = 0;
		if (value.startsWith("{") && value.endsWith("}")) {
			literal.setKind(1);
			add = 1;
		}
		if (value.startsWith("\"") && value.endsWith("\"")) {
			literal.setKind(2);
			add = 1;
		}
		// literal.setValue(value);
		int pos = start;
		for (int i = 0; i < contents.size(); i++) {
			Object oo = contents.get(i);
			if (oo instanceof String) {
				String st = (String) oo;
				StringArgument a = factory.createStringArgument();
				a.setValue(st);
				a.setStart(pos + offset + globalOffset + (i != 0 ? add : 0));
				pos += st.length();
				a.setEnd(pos + offset + globalOffset + add
						+ ((add > 0 && (i == contents.size() - 1)) ? 1 : 0));
				literal.getArguments().add(a);
			} else if (oo instanceof ISubstitution && oo instanceof TclElement) {
				TclElement bs = (TclElement) oo;
				pos = bs.getEnd();
				TclArgument expr = processWordContentAsExpression(offset,
						content, factory, bs.getStart(), bs.getEnd(), oo);
				if (expr != null) {
					literal.getArguments().add(expr);
				}
			}
		}
		return literal;
	}

	private void reportCommandNameSubstitution(TclArgument commandName) {
		if (this.reporter == null)
			return;
		// this.reporter.report(ITclErrorReporter.COMMAND_WITH_NAME_SUBSTITUTION,
		// Messages.TclParser_Command_Name_Is_Substitution, null,
		// commandName.getStart(), commandName.getEnd(),
		// ITclErrorReporter.WARNING);
	}

	private void reportDeprecatedError(TclCommand st, String commandValue,
			Command definition) {
		if (this.reporter == null)
			return;
		String message = NLS.bind(Messages.TclParser_Command_Is_Deprecated,
				new Object[] { commandValue, definition.getDeprecated() });
		this.reporter.report(ITclErrorReporter.DEPRECATED_COMMAND, message,
				null, st.getStart(), st.getEnd(), ITclErrorReporter.WARNING);
	}

	private void reportInvalidVersion(TclCommand st, String commandValue,
			TclErrorCollector parseErrors, Command definition) {
		String message = NLS.bind(
				Messages.TclParser_Command_Version_Is_Invalid, new Object[] {
						commandValue,

						definition.getVersion() });
		parseErrors.report(ITclErrorReporter.INVALID_COMMAND_VERSION, message,
				null, st.getStart(), st.getEnd(), ITclErrorReporter.ERROR);
	}

	private void reportOutOfScopeError(TclCommand st, String commandValue,
			Command definition) {
		if (this.reporter == null)
			return;
		List<Command> scopes = definition.getScope();
		StringBuilder scopesList = new StringBuilder();
		for (int i = 0; i < scopes.size(); i++) {
			if (i == scopes.size() - 1)
				scopesList.append(" or ");
			else if (i != 0)
				scopesList.append(", ");
			scopesList.append(scopes.get(i).getName());
		}
		String message = NLS.bind(Messages.TclParser_Command_Out_Of_Scope,
				new Object[] { commandValue, scopesList });
		this.reporter.report(ITclErrorReporter.COMMAND_OUT_OF_SCOPE, message,
				null, st.getStart(), st.getEnd(), ITclErrorReporter.ERROR);
	}
}
