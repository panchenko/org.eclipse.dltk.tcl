package org.eclipse.dltk.tcl.internal.core.parser.processors.tcl;

import java.util.List;

import org.eclipse.dltk.ast.ASTListNode;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
import org.eclipse.dltk.tcl.ast.expressions.TclExecuteExpression;
import org.eclipse.dltk.tcl.core.AbstractTclCommandProcessor;
import org.eclipse.dltk.tcl.core.ITclParser;
import org.eclipse.dltk.tcl.core.ast.IfStatement;
import org.eclipse.dltk.tcl.core.ast.TclAdvancedExecuteExpression;

public class TclIfProcessor extends AbstractTclCommandProcessor {

	private static final String THEN = "then"; //$NON-NLS-1$
	private static final String ELSE = "else"; //$NON-NLS-1$
	private static final String ELSEIF = "elseif"; //$NON-NLS-1$

	private static class IfStatementError extends RuntimeException {

		final int start;
		final int end;

		/**
		 * @param tclIfProcessor_incorrectIfCondition
		 * @param node
		 * @param error
		 */
		public IfStatementError(String message, ASTNode node) {
			this(message, node.sourceStart(), node.sourceEnd());
		}

		/**
		 * @param message
		 * @param start
		 * @param end
		 * @param severity
		 */
		public IfStatementError(String message, int start, int end) {
			super(message);
			this.start = start;
			this.end = end;
		}

	}

	private static class IfContext {
		final ITclParser parser;
		final TclStatement statement;
		final List exprs;
		int index;

		public IfContext(ITclParser parser, TclStatement statement) {
			this.parser = parser;
			this.statement = statement;
			this.exprs = statement.getExpressions();
			index = 1;
		}

		public int start() {
			return statement.sourceStart();
		}

		public int end() {
			return statement.sourceEnd();
		}

		public int size() {
			return exprs.size();
		}

		public ASTNode get(int i) {
			return (ASTNode) exprs.get(i);
		}

		public ASTNode get() {
			return get(index++);
		}

		boolean isEOF() {
			return index >= exprs.size();
		}
	}

	public TclIfProcessor() {
	}

	public ASTNode process(TclStatement statement, ITclParser parser,
			ASTNode parent) {
		final IfContext context = new IfContext(parser, statement);
		IfStatement ifStatement = new IfStatement(context.start(), context
				.end());
		addToParent(parent, ifStatement);
		try {
			parseIf(context, ifStatement);
			if (!context.isEOF()) {
				final ASTNode extraBegin = context.get(context.index);
				final ASTNode extraEnd = context.get(context.size() - 1);
				report(parser, Messages.TclIfProcessor_unexpectedStatements,
						extraBegin.sourceStart(), extraEnd.sourceEnd(),
						ProblemSeverities.Error);
			}
		} catch (IfStatementError e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
			report(parser, e.getMessage(), e.start, e.end,
					ProblemSeverities.Error);
		}
		return ifStatement;
	}

	private void parseIf(IfContext context, IfStatement ifStatement) {
		ifStatement.acceptCondition(parseCondition(context));
		if (context.isEOF()) {
			throw new IfStatementError(
					Messages.TclIfProcessor_missingThenBlock, context.start(),
					context.end());
		}
		if (checkKeyword(context, THEN)) {
			if (context.isEOF()) {
				throw new IfStatementError(
						Messages.TclIfProcessor_missingThenBlock, context
								.start(), context.end());
			}
		}
		ifStatement.acceptThen(parseBranch(context, true,
				Messages.TclIfProcessor_incorrectThenBlock));
		if (!context.isEOF()) {
			if (checkKeyword(context, ELSEIF)) {
				final ASTNode elseIfKeyword = context.get(context.index - 1);
				final IfStatement nestedIf = new IfStatement(elseIfKeyword
						.sourceStart(), context.end());
				parseIf(context, nestedIf);
				ifStatement.acceptElse(nestedIf);
			} else {
				if (checkKeyword(context, ELSE)) {
					if (context.isEOF()) {
						throw new IfStatementError(
								Messages.TclIfProcessor_incorrectElse, context
										.start(), context.end());
					}
				}
				ifStatement.acceptElse(parseBranch(context, false,
						Messages.TclIfProcessor_incorrectElseBlock));
			}
		}
	}

	private Statement parseBranch(IfContext context, boolean wrapAsBlock,
			String message) {
		final ASTNode node = context.get();
		if (node instanceof TclBlockExpression) {
			final Block block = new Block(node.sourceStart(), node.sourceEnd());
			parseBlock(context.parser, block, (TclBlockExpression) node);
			return block;
		} else if (node instanceof SimpleReference) {
			final Block block = new Block(node.sourceStart(), node.sourceEnd());
			block.addStatement(node);
			return block;
		} else if (node instanceof TclStatement) {
			if (wrapAsBlock) {
				final Block block = new Block(node.sourceStart(), node
						.sourceEnd());
				block.addStatement(node);
				return block;
			} else {
				return (TclStatement) node;
			}
		} else if (node instanceof TclExecuteExpression) {
			if (wrapAsBlock) {
				final Block block = new Block(node.sourceStart(), node
						.sourceEnd());
				block.addStatement(node);
				return block;
			} else {
				return (TclExecuteExpression) node;
			}
		} else {
			throw new IfStatementError(message, context.start(), context.end());
		}
	}

	/**
	 * @param exprs
	 * @param index
	 * @param keyword
	 * @return
	 */
	private boolean checkKeyword(IfContext context, String keyword) {
		final ASTNode node = context.get(context.index);
		if (node instanceof SimpleReference) {
			final SimpleReference ref = (SimpleReference) node;
			if (keyword.equals(ref.getName())) {
				++context.index;
				return true;
			}
		}
		return false;
	}

	private void parseBlock(ITclParser parser, Block el,
			TclBlockExpression block) {
		String blockContent = block.getBlock();
		blockContent = blockContent.substring(1, blockContent.length() - 1);
		parser.parse(blockContent, block.sourceStart() + 1
				- parser.getStartPos(), el);
	}

	private ASTNode parseCondition(IfContext context) {
		if (context.isEOF()) {
			throw new IfStatementError(
					Messages.TclIfProcessor_missingCondition, context.start(),
					context.end());
		}
		ASTNode node = context.get();
		if (node instanceof TclBlockExpression) {
			TclBlockExpression bl = (TclBlockExpression) node;
			List parseBlock = bl.parseBlockSimple();
			ASTListNode list = new ASTListNode(bl.sourceStart() + 1, bl
					.sourceEnd() - 1);
			for (int j = 0; j < parseBlock.size(); j++) {
				Object st = parseBlock.get(j);
				if (st instanceof TclStatement) {
					List expressions = ((TclStatement) st).getExpressions();
					for (int k = 0; k < expressions.size(); k++) {
						ASTNode expr = (ASTNode) expressions.get(k);
						list.addNode(expr);
					}
				}
			}
			return list;
		} else if (node instanceof SimpleReference) {
			return node;
		} else if (node instanceof TclAdvancedExecuteExpression) {
			TclAdvancedExecuteExpression ex = (TclAdvancedExecuteExpression) node;
			List childs = ex.getChilds();
			return new ASTListNode(node.sourceStart(), node.sourceEnd(), childs);
		} else if (node instanceof TclExecuteExpression) {
			return node;
		}
		throw new IfStatementError(Messages.TclIfProcessor_incorrectCondition,
				node);
	}

}
