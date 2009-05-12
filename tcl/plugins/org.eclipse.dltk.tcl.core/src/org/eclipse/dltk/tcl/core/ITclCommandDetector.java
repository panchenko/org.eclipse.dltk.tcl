package org.eclipse.dltk.tcl.core;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.tcl.ast.TclStatement;

/**
 * Class used to dynamicaly n XOTcl command from tcl command.
 * 
 * @author haiodo
 * 
 */

public interface ITclCommandDetector {
	public static class CommandInfo {
		public String commandName;
		public Object parameter;

		public CommandInfo(String name, Object parameter) {
			this.commandName = name;
			this.parameter = parameter;
		}
	}

	/**
	 * Called after processing of each node.
	 */
	void processASTNode(ASTNode node);

	/**
	 * Should not scan all module. It could be really slow.
	 */
	CommandInfo detectCommand(TclStatement statement, ModuleDeclaration module, ASTNode decl);
}
