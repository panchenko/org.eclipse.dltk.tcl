package org.eclipse.dltk.tcl.core;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.compiler.env.IModuleSource;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

public interface ITclSourceParser extends ISourceParser {
	void setOffset(int offset);

	void setProcessorsState(boolean state);

	/*
	 * Redeclare with different return type
	 * 
	 * @see ISourceParser#parse(org.eclipse.dltk.compiler.env.ISourceModule,
	 * org.eclipse.dltk.compiler.problem.IProblemReporter)
	 */
	ModuleDeclaration parse(IModuleSource input, IProblemReporter reporter);
}
