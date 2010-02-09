package org.eclipse.dltk.tcl.internal.parser;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserExtension;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.builder.ISourceLineTracker;
import org.eclipse.dltk.tcl.ast.TclStatement;
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
import org.eclipse.dltk.tcl.internal.parser.raw.SimpleTclParser;
import org.eclipse.dltk.tcl.internal.parser.raw.TclCommand;
import org.eclipse.dltk.tcl.internal.parser.raw.TclParseException;
import org.eclipse.dltk.tcl.internal.parser.raw.TclScript;
import org.eclipse.dltk.utils.TextUtils;

/**
 * @deprecated
 */
public class TclSourceParser extends AbstractSourceParser implements
		ITclSourceParser, ITclParser, ISourceParserExtension {
	private IProblemReporter problemReporter;
	protected ISourceLineTracker codeModel;
	protected String content;
	private String fileName;
	private int startPos = 0;
	boolean useProcessors = true;
	private int flags;
	private boolean useDetectors = true;

	private ModuleDeclaration moduleDeclaration;

	public ModuleDeclaration parse(IModuleSource input,
			IProblemReporter reporter) {
		initDetectors();
		this.problemReporter = reporter;
		this.content = input.getSourceContents();
		this.codeModel = TextUtils.createLineTracker(this.content);
		this.fileName = input.getFileName();

		this.moduleDeclaration = new ModuleDeclaration(this.content.length());

		this.parse(this.content, 0, moduleDeclaration);
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
				// Replace execute expressions and parse they content.
				convertExecuteToBlocks(st);
			}
			return st;
		}

		public void setCurrentASTTree(ModuleDeclaration module) {
		}

		public void setDetectedParameter(Object parameter) {
		}
	};
	private ITclCommandDetector[] detectors;

	private void convertExecuteToBlocks(TclStatement st) {
		ASTNode[] nodes = (ASTNode[]) st.getExpressions().toArray(
				new ASTNode[st.getCount()]);
		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] instanceof TclExecuteExpression) {

				TclExecuteExpression tclExecuteExpression = ((TclExecuteExpression) nodes[i]);
				String expression = tclExecuteExpression.getExpression();
				if (expression.length() > 2) {
					expression = expression.substring(1,
							expression.length() - 1);
					TclAdvancedExecuteExpression newExpr = new TclAdvancedExecuteExpression(
							nodes[i].sourceStart(), nodes[i].sourceEnd());
					nodes[i] = newExpr;
					st.setExpressions(Arrays.asList(nodes));
					parse(expression, nodes[i].sourceStart() + 1
							- getStartPos(), newExpr);
				}
			}
		}
		st.setExpressions(Arrays.asList(nodes));
	}

	public void parse(String content, int offset, ASTNode decl) {
		initDetectors();
		TclScript script = null;
		try {
			script = SimpleTclParser.staticParse(content);
		} catch (TclParseException e) {
			if (DLTKCore.DEBUG_PARSER) {
				e.printStackTrace();
			}
			return;
		}
		if (script == null) {
			return;
		}
		List commands = script.getCommands();
		for (Iterator iter = commands.iterator(); iter.hasNext();) {
			TclCommand command = (TclCommand) iter.next();
			// Command handling
			TclStatement st = TclParseUtil.convertToAST(command, this
					.getFileName(), offset, this.content, this.startPos);
			if (st == null) {
				continue; // could be null on errors in source code
			}
			ITclCommandProcessor processor = this.locateProcessor(st, content,
					offset, decl);
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
				} catch (Exception e) {
					TclPlugin.error(e);
				}
			}
		}
	}

	private ITclCommandProcessor locateProcessor(TclStatement command,
			String content, int offset, ASTNode decl) {
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
									.setBuildRuntimeModelFlag(isBuildingRuntimeModel());
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

	public ISourceLineTracker getCodeModel() {
		return this.codeModel;
	}

	public String getContent() {
		return this.content;
	}

	public String substring(int start, int end) {
		if (start > end) {
			return "";
		}
		try {
			return this.content.substring(start - this.startPos, end
					- this.startPos);
		} catch (IndexOutOfBoundsException e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return "####error####";
	}

	public IProblemReporter getProblemReporter() {
		return this.problemReporter;
	}

	public String getFileName() {
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

	public void setFlags(int flags) {
		this.flags = flags;
	}

	private boolean isBuildingRuntimeModel() {
		return false; // (flags & ISourceParserConstants.RUNTIME_MODEL) != 0
	}

	public void setUseDetectors(boolean b) {
		this.useDetectors = false;
	}

	public void setProblemReporter(IProblemReporter problemReporter2) {
		this.problemReporter = problemReporter2;
	}

	public void setModuleDeclaration(ModuleDeclaration moduleDeclaration) {
		this.moduleDeclaration = moduleDeclaration;
	}

	public void setContent(String content2) {
		this.content = content2;
	}

}
