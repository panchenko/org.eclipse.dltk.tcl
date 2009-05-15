package org.eclipse.dltk.tcl.internal.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserExtension;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.ComplexString;
import org.eclipse.dltk.tcl.ast.Script;
import org.eclipse.dltk.tcl.ast.StringArgument;
import org.eclipse.dltk.tcl.ast.Substitution;
import org.eclipse.dltk.tcl.ast.TclArgument;
import org.eclipse.dltk.tcl.ast.TclArgumentList;
import org.eclipse.dltk.tcl.ast.TclCodeModel;
import org.eclipse.dltk.tcl.ast.TclCommand;
import org.eclipse.dltk.tcl.ast.TclModule;
import org.eclipse.dltk.tcl.ast.TclModuleDeclaration;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.ast.VariableReference;
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
import org.eclipse.dltk.tcl.ast.expressions.TclExecuteExpression;
import org.eclipse.dltk.tcl.core.ITclCommandDetector;
import org.eclipse.dltk.tcl.core.ITclCommandDetectorExtension;
import org.eclipse.dltk.tcl.core.ITclCommandProcessor;
import org.eclipse.dltk.tcl.core.ITclParser;
import org.eclipse.dltk.tcl.core.ITclSourceParser;
import org.eclipse.dltk.tcl.core.TclParseUtil;
import org.eclipse.dltk.tcl.core.TclPlugin;
import org.eclipse.dltk.tcl.core.ITclCommandDetector.CommandInfo;
import org.eclipse.dltk.tcl.core.ast.TclAdvancedExecuteExpression;
import org.eclipse.dltk.tcl.internal.parser.ext.CommandManager;
import org.eclipse.dltk.tcl.parser.TclErrorCollector;
import org.eclipse.dltk.tcl.parser.TclParser;
import org.eclipse.dltk.tcl.parser.definitions.DefinitionManager;
import org.eclipse.dltk.tcl.parser.printer.SimpleCodePrinter;
import org.eclipse.dltk.utils.TextUtils;
import org.eclipse.emf.common.util.EList;

public class NewTclSourceParser extends AbstractSourceParser implements
		ITclParser, ISourceParser, ISourceParserExtension, ITclSourceParser {
	private IProblemReporter problemReporter;
	private char[] fileName;
	private int startPos = 0;
	boolean useProcessors = true;
	private boolean useDetectors = true;

	private TclModuleDeclaration moduleDeclaration;
	private TclModule tclModule;
	ISourceLineTracker tracker;

	public ModuleDeclaration parse(char[] fileName, TclModule tclModule,
			IProblemReporter reporter) {
		processedForContentNodes.clear();
		initDetectors();
		this.tclModule = tclModule;
		TclCodeModel model = this.tclModule.getCodeModel();
		EList<Integer> list = model.getLineOffsets();
		int[] offsets = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			offsets[i] = list.get(i);
		}
		EList<String> delimeters = model.getDelimeters();
		String[] delimetersAsArray = delimeters.toArray(new String[delimeters
				.size()]);
		this.tracker = new TextUtils.DefaultSourceLineTracker(tclModule
				.getSize(), offsets, delimetersAsArray);
		this.problemReporter = reporter;
		this.fileName = fileName;

		this.moduleDeclaration = new TclModuleDeclaration(tclModule.getSize());
		this.moduleDeclaration.setTclModule(tclModule);
		this.parse(tclModule, moduleDeclaration);
		return moduleDeclaration;
	}

	private void initDetectors() {
		if (detectors == null) {
			detectors = CommandManager.getInstance().getDetectors();
		}
	}

	private ITclCommandProcessor localProcessor = new ITclCommandProcessor() {
		public ASTNode process(TclStatement st, ITclParser parser,
				ASTNode parent) {
			if (parent != null) {
				TclParseUtil.addToDeclaration(parent, st);
				// Re process internal blocks.
			}
			return st;
		}

		public void setCurrentASTTree(ModuleDeclaration module) {
		}

		public void setDetectedParameter(Object parameter) {
		}
	};
	private ITclCommandDetector[] detectors;

	protected void parse(TclModule module, ASTNode decl) {
		processedForContentNodes.clear();
		initDetectors();

		List<TclCommand> commands = module.getStatements();
		for (Iterator<TclCommand> iter = commands.iterator(); iter.hasNext();) {
			TclCommand command = iter.next();
			// Command handling
			TclStatement st = convertToAST(command);
			if (st == null) {
				continue; // could be null on errors in source code
			}
			runStatementProcessor(decl, st);
		}
	}

	private Set<ASTNode> processedForContentNodes = new HashSet();

	private void runStatementProcessor(ASTNode decl, TclStatement st) {
		ITclCommandProcessor processor = this.locateProcessor(st, decl);
		if (processor != null) {
			try {
				ASTNode nde = processor.process(st, this, decl);
				if (nde == null) {
					nde = localProcessor.process(st, this, decl);
				}
				if (nde != null && this.useDetectors) {
					for (int i = 0; i < this.detectors.length; i++) {
						if (detectors[i] != null) {
							detectors[i].processASTNode(nde);
						}
					}
				}
				if (nde != null) {
					nde.traverse(new ASTVisitor() {
						public boolean visit(TypeDeclaration s)
								throws Exception {
							if (processedForContentNodes.add(s)) {
								List stats = s.getStatements();
								processStatements(s, stats);
							}
							return true;
						}

						private void processStatements(ASTNode s, List stats) {
							List<ASTNode> statements = new ArrayList<ASTNode>(
									stats);
							stats.clear();
							for (ASTNode node : statements) {
								if (node instanceof TclStatement) {
									runStatementProcessor(s,
											(TclStatement) node);
								} else {
									stats.add(node);
								}
							}
						}

						public boolean visit(MethodDeclaration s)
								throws Exception {
							if (processedForContentNodes.add(s)) {
								List stats = s.getStatements();
								processStatements(s, stats);
							}
							return true;
						}

						public boolean visit(Expression s) throws Exception {
							if (s instanceof Block) {
								if (processedForContentNodes.add(s)) {
									Block bl = (Block) s;
									List stats = bl.getStatements();
									processStatements(bl, stats);
								}
								return true;
							} else if (s instanceof TclAdvancedExecuteExpression) {
								if (processedForContentNodes.add(s)) {
									TclAdvancedExecuteExpression ex = (TclAdvancedExecuteExpression) s;
									List stats = ex.getStatements();
									processStatements(ex, stats);
								}
							} else if (s instanceof TclExecuteExpression) {
								// This should not happen at all.
							}
							return true;
						}
					});
					// Re process internal elements.
				}
			} catch (Exception e) {
				TclPlugin.error(e);
			}
		}

	}

	private TclStatement convertToAST(TclCommand command) {
		List<ASTNode> expressions = new ArrayList<ASTNode>();
		expressions.add(convertToAST(command.getName()));
		for (TclArgument arg : command.getArguments()) {
			expressions.add(convertToAST(arg));
		}
		return new TclStatement(expressions);
	}

	private ASTNode convertToAST(TclArgument arg) {
		if (arg instanceof StringArgument) {
			// Simple absolute or relative source'ing.
			StringArgument argument = (StringArgument) arg;
			String value = argument.getValue();
			return stringToAST(argument, value);
		} else if (arg instanceof ComplexString) {
			ComplexString carg = (ComplexString) arg;
			return stringToAST(carg, SimpleCodePrinter.getArgumentString(carg));
		} else if (arg instanceof Script) {
			Script st = (Script) arg;
			EList<TclCommand> eList = st.getCommands();
			Block block = new Block(st.getStart(), st.getEnd());
			for (TclCommand tclArgument : eList) {
				TclStatement stat = convertToAST(tclArgument);
				block.addStatement(stat);
			}
			return block;

		} else if (arg instanceof VariableReference) {
			VariableReference variableReference = (VariableReference) arg;
			String content = SimpleCodePrinter
					.getArgumentString(variableReference);
			return new SimpleReference(arg.getStart(), arg.getEnd(), content);
		} else if (arg instanceof Substitution) {
			Substitution st = (Substitution) arg;
			EList<TclCommand> eList = st.getCommands();
			TclAdvancedExecuteExpression block = new TclAdvancedExecuteExpression(
					st.getStart(), st.getEnd());
			for (TclCommand cmd : eList) {
				TclStatement stat = convertToAST(cmd);
				block.addStatement(stat);
			}
			// block.acceptStatements(exprs);
			return block;
		} else if (arg instanceof TclArgumentList) {
			TclArgumentList st = (TclArgumentList) arg;
			String str = SimpleCodePrinter.getArgumentString(st);
			return stringToAST(st, str);
		}
		throw new RuntimeException(
				"TODO: Not all cases are matched in TCL Parser AST converter");
	}

	private ASTNode stringToAST(TclArgument argument, String value) {
		if (value.startsWith("{")
				&& (value.endsWith("}") || argument.getEnd() == moduleDeclaration
						.sourceEnd())) {
			// This is block argument
			return new TclBlockExpression(argument.getStart(), argument
					.getEnd(), value);
		} else if (value.startsWith("\"")
				&& (value.endsWith("\"") || argument.getEnd() == moduleDeclaration
						.sourceEnd())) {
			// This is string literal
			return new StringLiteral(argument.getStart(), argument.getEnd(),
					value);
		} else {
			int len = argument.getEnd() - argument.getStart();
			if (value.length() > len) {
				value = value.substring(0, len);
			}
			// Simple reference
			return new SimpleReference(argument.getStart(), argument.getEnd(),
					value);
		}
	}

	private ITclCommandProcessor locateProcessor(TclStatement command,
			ASTNode decl) {
		if (this.useProcessors == false) {
			return localProcessor;
		}

		if (command != null && command.getCount() > 0) {
			Expression expr = command.getAt(0);
			if (!(expr instanceof SimpleReference)) {
				return localProcessor;
			}
			String name = ((SimpleReference) expr).getName();
			if (name.startsWith("::")) {
				name = name.substring(2);
			}

			ITclCommandProcessor processor = CommandManager.getInstance()
					.getProcessor(name);
			if (processor == null) {
				// advanced command detection.
				if (this.useDetectors) {
					for (int i = 0; i < detectors.length; i++) {
						if (detectors[i] == null) {
							continue;
						}
						if (detectors[i] instanceof ITclCommandDetectorExtension) {
							((ITclCommandDetectorExtension) detectors[i])
									.setBuildRuntimeModelFlag(false);
						}
						CommandInfo commandName = detectors[i].detectCommand(
								command, this.moduleDeclaration, decl);
						if (commandName != null) {
							processor = CommandManager.getInstance()
									.getProcessor(commandName.commandName);
							if (processor != null) {
								processor
										.setDetectedParameter(commandName.parameter);
							}
							break;
						}
					}
				}
			}
			if (processor != null) {
				processor.setCurrentASTTree(this.moduleDeclaration);
				return processor;
			}
		}
		return this.localProcessor;
	}

	public IProblemReporter getProblemReporter() {
		return this.problemReporter;
	}

	public char[] getFileName() {
		return this.fileName;
	}

	public void setOffset(int offset) {
		this.startPos = offset;
	}

	public int getStartPos() {
		return this.startPos;
	}

	public void setProcessorsState(boolean state) {
		this.useProcessors = state;
	}

	public void setUseDetectors(boolean b) {
		this.useDetectors = false;
	}

	public ISourceLineTracker getCodeModel() {
		return tracker;
	}

	public void parse(String content, int offset, ASTNode parent) {
		processedForContentNodes.clear();
		TclSourceParser parser = new TclSourceParser();
		parser.setOffset(offset);
		parser.setModuleDeclaration(moduleDeclaration);
		parser.setProblemReporter(getProblemReporter());
		parser.setContent(content);
		parser.setUseDetectors(useDetectors);
		parser.setProcessorsState(useProcessors);
		parser.parse(content, 0, parent);
	}

	public ModuleDeclaration parse(final char[] fileName, char[] source,
			final IProblemReporter reporter) {
		processedForContentNodes.clear();
		TclParser newParser = new TclParser();
		TclErrorCollector collector = null;
		if (reporter != null) {
			collector = new TclErrorCollector();
		}
		TclModule module = newParser.parseModule(new String(source), collector,
				DefinitionManager.getInstance().getCoreProcessor());
		// TODO: Add error passing to reporter here.
		return parse(fileName, module, reporter);
	}

	public void setFlags(int flags) {
	}
}
