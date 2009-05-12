package org.eclipse.dltk.tcl.internal.core.parser.processors.tcl;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.compiler.problem.ProblemSeverities;
import org.eclipse.dltk.tcl.ast.TclStatement;
import org.eclipse.dltk.tcl.ast.expressions.TclBlockExpression;
import org.eclipse.dltk.tcl.core.AbstractTclCommandProcessor;
import org.eclipse.dltk.tcl.core.ITclParser;
import org.eclipse.dltk.tcl.core.ast.TclForStatement;

public class TclForCommandProcessor extends AbstractTclCommandProcessor {

	public ASTNode process(TclStatement statement, ITclParser parser,
			ASTNode parent) {
		if (statement.getCount() != 5) {
			this.report(parser, "Syntax error: exactly " + (5 - 1)
					+ " arguments expected.", statement,
					ProblemSeverities.Error);
			return null;
		}
		TclForStatement forStatement = new TclForStatement(statement
				.sourceStart(), statement.sourceEnd());
		addToParent(parent, forStatement);

		Expression procCode = statement.getAt(statement.getCount() - 1);
		if (procCode instanceof Block) {
			forStatement.acceptBlock((Block) procCode);
		}
		if (procCode instanceof TclBlockExpression) {
			Block block = new Block(procCode.sourceStart(), procCode
					.sourceEnd());

			String content = ((TclBlockExpression) procCode).getBlock();
			if (content.startsWith("{") && content.endsWith("}")) {
				content = content.substring(1, content.length() - 1);
			}
			forStatement.acceptBlock(block);
			parser.parse(content, procCode.sourceStart() + 1- parser.getStartPos(), block);
		}

		return forStatement;
	}
}
