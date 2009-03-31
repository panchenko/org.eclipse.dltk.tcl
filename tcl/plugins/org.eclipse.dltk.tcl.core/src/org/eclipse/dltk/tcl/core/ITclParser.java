package org.eclipse.dltk.tcl.core;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.tcl.core.TclParseUtil.CodeModel;

public interface ITclParser {
	// Used to parser inner elements
	void parse( String content, int offset, ASTNode parent );
	
	CodeModel getCodeModel();
	String getContent();
	String substring(int start, int end);
	IProblemReporter getProblemReporter();
	char[] getFileName();
	int getStartPos();
}
